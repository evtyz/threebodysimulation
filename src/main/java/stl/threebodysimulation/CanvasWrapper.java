package stl.threebodysimulation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;

/**
 * A wrapper that manages the graphics of a canvas UI object.
 */
class CanvasWrapper {
    /**
     * An array of the radii of each particle respectively.
     */
    final double[] circleDiameter = {0, 0, 0};
    /**
     * A 2D array of the old canvas position values
     */
    final double[][] oldCanvasPos = new double[3][2];
    /**
     * The canvas to draw particles on.
     */
    private final Canvas canvas;
    /**
     * The canvas to draw grid lines on.
     */
    private final Canvas gridCanvas;
    /**
     * The canvas to draw trails on.
     */
    private final Canvas trailCanvas;
    /**
     * The graphics supplied to the particle canvas.
     */
    private final GraphicsContext particlesGC;
    /**
     * The graphics supplied to the grid line canvas
     */
    private final GraphicsContext gridGC;
    /**
     * The graphics supplied to the trail canvas
     */
    private final GraphicsContext trailGC;
    /**
     * An array of the X and Y positions of a particle.
     */
    double[] canvasPos = {0, 0};
    /**
     * The average mass of all the particles.
     */
    double avgMass;
    /**
     * The sum of the masses of the particles
     */
    double massSum;
    /**
     * The scale factor for the canvas size adjustment (x,y)
     */
    double particleScale = 1;
    /**
     * The scale factor for the canvas orientation adjustment (x,y)
     */
    double[] translationScale = new double[2];
    /**
     * The particles to draw on the canvas.
     */
    private Particle[] particles;
    /**
     * Whether the canvas should show particle trails.
     */
    private boolean trails;
    /**
     * Whether the canvas should show the center of mass of the particles.
     */
    private boolean centerOfMass;

    /**
     * Constructs a basic CanvasWrapper object for a particular canvas UI element.
     *
     * @param canvas The main canvas for particles.
     * @param gridCanvas The canvas that manages gridlines.
     * @param trailCanvas The canvas that manages trails.
     */
    CanvasWrapper(Canvas canvas, Canvas gridCanvas, Canvas trailCanvas) {
        this.canvas = canvas;
        this.gridCanvas = gridCanvas;
        this.trailCanvas = trailCanvas;
        particlesGC = canvas.getGraphicsContext2D();
        gridGC = gridCanvas.getGraphicsContext2D();
        trailGC = trailCanvas.getGraphicsContext2D();
    }

    /**
     * Adjusts the original rectangle by the length of the buffer.
     *
     * @param originalRectangle The coordinates of the corners of the original rectangle.
     * @param buffer            The length by which the new rectangle is extended outwards.
     * @return The new rectangle adjusted by the buffer length.
     */
    private static double[][] calculateRectangle(double[][] originalRectangle, double buffer) {

        // Declaring the new rectangle variable
        double[][] newRectangle = new double[4][2];

        // Setting a constant minimum buffer proportion
        final double BUFFER_PROPORTION = 0.1;

        // Calculates the buffer space by which to adjust the original rectangle
        double bufferProportionalWidth = (originalRectangle[2][0] - originalRectangle[0][0]) * BUFFER_PROPORTION + buffer;
        double bufferProportionalHeight = (originalRectangle[1][1] - originalRectangle[0][1]) * BUFFER_PROPORTION + buffer;

        // Adjusting point 1 (lower left)
        newRectangle[0][0] = originalRectangle[0][0] - bufferProportionalWidth;
        newRectangle[0][1] = originalRectangle[0][1] - bufferProportionalHeight;

        // Adjusting point 2 (upper left)
        newRectangle[1][0] = originalRectangle[1][0] - bufferProportionalWidth;
        newRectangle[1][1] = originalRectangle[1][1] + bufferProportionalHeight;

        // Adjusting point 3 (lower right)
        newRectangle[2][0] = originalRectangle[2][0] + bufferProportionalWidth;
        newRectangle[2][1] = originalRectangle[2][1] - bufferProportionalHeight;

        // Adjusting point 4 (upper right)
        newRectangle[3][0] = originalRectangle[3][0] + bufferProportionalWidth;
        newRectangle[3][1] = originalRectangle[3][1] + bufferProportionalHeight;

        return newRectangle;
    }

    /**
     * Sets the graphics options and particles for the canvas.
     *
     * @param scales   An 2D array of doubles that represent the max/min x/y coordinates of the particles in the first 10 seconds of simulation.
     * @param settings The SimulationSettings object that options and particles will be read from.
     */
    void setupScalesAndSettings(double[][] scales, SimulationSettings settings) {
        clearCanvas();

        // Establishes the trail width
        trailGC.setLineWidth(1.0);

        // Establishes the center of gravity guide width
        particlesGC.setLineWidth(1.0);

        // Establishes the grid line canvas line width
        gridGC.setLineWidth(0.4);

        // Initializing the imported variables
        trails = settings.getTrails();
        centerOfMass = settings.getCenterOfGravity();
        this.particles = settings.getParticles();

        // Calculate the sum of the particle masses
        massSum = particles[0].getMass() + particles[1].getMass() + particles[2].getMass();

        // Coordinates of the rectangle that the canvas represents.
        double[][] canvasRectangle = calculateRectangle(scales, calculateBuffer(particles));

        // Geometric mean of masses.
        avgMass = Math.cbrt(particles[0].getMass() * particles[1].getMass() * particles[2].getMass());

        // Attempting to normalize the masses.
        for (int i = 0; i < 3; i++) {
            circleDiameter[i] = Math.cbrt(particles[i].getMass() / avgMass) * 4 + 8;
        }

        // Clears the trails canvas of any existing trails and grid lines
        trailGC.clearRect(0, 0, trailCanvas.getWidth(), trailCanvas.getHeight());
        gridGC.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        // Sets the scale factors for drawing on the canvas
        setScaleFactors(canvasRectangle);

        // Initializes the oldCanvasPos variable with the original position values
        for (int i = 0; i < 3; i++) {
            oldCanvasPos[i] = returnRelativePosition(particles[i].getPosition());
        }

        double gridInterval = calculateGridInterval();

        printVerticalGridlines(gridInterval);
        printHorizontalGridlines(gridInterval);

        particlesGC.setFont(Font.font("Verdana", 50));
    }

    /**
     * Prints the vertical gridlines based on the calculated grid interval.
     *
     * @param interval The absolute distance between each vertical gridline.
     */
    private void printVerticalGridlines(double interval) {
        // left end of the canvas
        double base = translationScale[0];

        double currentGridline = calculateFirstGridline(base, interval);

        // Draws all gridlines that fit on the canvas
        while (currentGridline < translationScale[0] + particleScale * 800) {
            // If it is the zero gridline, make it darker
            if (Math.abs(currentGridline) < Math.pow(10, -10)) {
                gridGC.setFill(Color.BLACK);
                gridGC.setStroke(Color.BLACK);
            } else {
                gridGC.setFill(Color.DARKGRAY);
                gridGC.setStroke(Color.DARKGRAY);
            }
            double relativeCurrentGridline = returnRelativePosition(new double[]{currentGridline, 0})[0];
            gridGC.strokeLine(relativeCurrentGridline, -20, relativeCurrentGridline, 800);
            // Draw the label for the gridline, but only if it isn't too close to the left side of the canvas (to prevent overlap)
            if (!(relativeCurrentGridline < 60)) {
                drawRotatedText(gridGC, String.format("%g", currentGridline), 270, relativeCurrentGridline - 10, 710);
            }
            currentGridline += interval;
        }
    }

    /**
     * Draws a rotated piece of text. Modified from jewelsea's answer at https://stackoverflow.com/questions/18260421/how-to-draw-image-rotated-on-javafx-canvas.
     *
     * @param gc    The GraphicsContext to draw it on.
     * @param text  The text to draw.
     * @param angle The angle to draw the text.
     * @param tlpx  The x position of the text.
     * @param tlpy  The y position of the text.
     */
    private void drawRotatedText(GraphicsContext gc, String text, double angle, double tlpx, double tlpy) {
        gc.save(); // saves the current state on stack, including the current transform
        Rotate r = new Rotate(angle, tlpx, tlpy);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.fillText(text, tlpx, tlpy);
        gc.restore(); // back to original state (before rotation)
    }

    /**
     * Calculates the first gridline based on a corner coordinate and a grid interval.
     *
     * @param base The corner coordinate.
     * @param interval The grid interval.
     * @return The coordinate of the first gridline.
     */
    private static double calculateFirstGridline(double base, double interval) {
        if (base < 0) { // Modulo works differently with positive and negative numbers in Java
            return base + Math.abs(base % interval);
        } else if (base > 0) {
            return base + (interval - base % interval);
        } else {
            return 0;
        }
    }

    /**
     * Prints the horizontal gridlines based on the calculated grid interval.
     *
     * @param interval The absolute distance between each horizontal gridline.
     */
    private void printHorizontalGridlines(double interval) {
        // Top end of the canvas
        double base = translationScale[1];

        double currentGridline = calculateFirstGridline(base, interval);

        // Fill in gridlines that appear on canvas
        while (currentGridline > translationScale[1] - particleScale * 720) {
            // Zero gridline is darker (approximation due to double inaccuracy)
            if (Math.abs(currentGridline) < Math.pow(10, -10)) {
                gridGC.setFill(Color.BLACK);
                gridGC.setStroke(Color.BLACK);
            } else {
                gridGC.setFill(Color.DARKGRAY);
                gridGC.setStroke(Color.DARKGRAY);
            }

            // Draws the vertical gridlines on the canvas
            double relativeCurrentGridline = returnRelativePosition(new double[]{0, currentGridline})[1];
            gridGC.strokeLine(-20, relativeCurrentGridline, 850, relativeCurrentGridline);
            gridGC.fillText(String.format("%g", currentGridline), 10, relativeCurrentGridline - 10);

            currentGridline -= interval;
        }
    }

    /**
     * Gets the grid interval for a given scale factor.
     *
     * @return The grid interval.
     */
    private double calculateGridInterval() {

        // Calculates the grid interval coefficient for determining the actual interval
        double rawGridInterval = 100 * particleScale;
        double base = Math.pow(10, Math.floor(Math.log10(rawGridInterval)));
        double coefficient = rawGridInterval / base;

        // Determines the interval based on the coefficient
        if (coefficient <= 1.75) {
            return 1.0 * base;
        } else if (coefficient <= 4) {
            return 2.0 * base;
        } else if (coefficient <= 8) {
            return 5.0 * base;
        }
        return 10 * base;
    }

    /**
     * Calculates the scales by which to adjust the canvas size and orientation.
     *
     * @param canvasRectangle The corner coordinates of the original rectangle adjusted for buffer.
     */
    private void setScaleFactors(double[][] canvasRectangle) {

        // Declaring variables used by the method
        double aspectFactor;
        double aspectAdjust;
        double adjDiff;
        double newHeight;
        double newWidth;
        double rectangleHeight;
        double rectangleWidth;
        double[] particleScaleArray = {0, 0};

        // Calculates absolute height and width of the rectangle
        rectangleHeight = Math.abs(canvasRectangle[1][1] - canvasRectangle[0][1]);
        rectangleWidth = Math.abs(canvasRectangle[3][0] - canvasRectangle[1][0]);

        if (rectangleHeight == 0 || rectangleWidth == 0) {
            particleScale = 1;
            translationScale = new double[]{-400, 360};
            return;
        }

        // Calculates the aspect ratios of the canvas and the rectangle
        double rectangleAspect = rectangleWidth / rectangleHeight;
        double canvasAspect = 10.0 / 9.0;

        // Adjusts the rectangle to fit the canvas aspect ratio
        if (rectangleAspect > canvasAspect) {
            aspectFactor = rectangleAspect / canvasAspect;
            aspectAdjust = rectangleHeight * aspectFactor;
            adjDiff = (aspectAdjust - rectangleHeight) / 2;

            canvasRectangle[1][1] = canvasRectangle[1][1] + adjDiff;
            canvasRectangle[3][1] = canvasRectangle[3][1] + adjDiff;
            canvasRectangle[0][1] = canvasRectangle[0][1] - adjDiff;
            canvasRectangle[2][1] = canvasRectangle[2][1] - adjDiff;
        } else if (rectangleAspect < canvasAspect) {
            aspectFactor = canvasAspect / rectangleAspect;
            aspectAdjust = rectangleWidth * aspectFactor;
            adjDiff = (aspectAdjust - rectangleWidth) / 2;

            canvasRectangle[2][0] = canvasRectangle[2][0] + adjDiff;
            canvasRectangle[3][0] = canvasRectangle[3][0] + adjDiff;
            canvasRectangle[0][0] = canvasRectangle[0][0] - adjDiff;
            canvasRectangle[1][0] = canvasRectangle[1][0] - adjDiff;
        }


        // Defines the new height and width of the rectangle
        newHeight = Math.abs(canvasRectangle[1][1] - canvasRectangle[0][1]);
        newWidth = Math.abs(canvasRectangle[3][0] - canvasRectangle[1][0]);

        // Calculates a scale factor by which to adjust the canvas particles
        particleScaleArray[0] = ((newHeight - 720) / 720) + 1;
        particleScaleArray[1] = ((newWidth - 800) / 800) + 1;
        particleScale = (particleScaleArray[0] + particleScaleArray[1]) / 2;

        // Calculates the coordinates of the upper left corner of the rectangle
        translationScale[0] = canvasRectangle[1][0];
        translationScale[1] = canvasRectangle[1][1];
    }

    /**
     * Returns a buffer size based on particles and an original rectangle.
     *
     * @param particles The particle objects that contain information about velocity and mass.
     * @return An integer that represents the buffer size.
     */
    private double calculateBuffer(Particle[] particles) {
        // Declares buffer variable
        double buffer;

        // Constructs an array of the absolute values of the particle velocities
        double[] particleSquares = new double[3];
        for (int i = 0; i < 3; i++) {
            particleSquares[i] =
                    particles[i].getVelocity()[0] * particles[i].getVelocity()[0] +
                            particles[i].getVelocity()[1] * particles[i].getVelocity()[1];
        }

        // Calculates the average squared velocity
        double avgSquaredVelocity = Math.sqrt((particleSquares[0] * particles[0].getMass() + particleSquares[1] * particles[1].getMass()
                + particleSquares[2] * particles[2].getMass()) / massSum);

        // Determines the buffer as a product of ASV and a constant
        buffer = avgSquaredVelocity * 5;

        return buffer;
    }

    /**
     * Gives the relative canvas position of a drawn element given it's real position
     *
     * @param absolutePosition The real position of the element
     * @return The position of the element on the canvas
     */
    private double[] returnRelativePosition(double[] absolutePosition) {
        return new double[]{
                (absolutePosition[0] - translationScale[0]) / particleScale,
                -(absolutePosition[1] - translationScale[1]) / particleScale
        };
    }

    /**
     * Updates the canvas according to the current state of the particles.
     */
    void updateCanvas() {
        clearCanvas();

        // Displays the positions of the particles on the canvas
        for (int i = 0; i < 3; i++) {

            // Determines the position of the particle on the canvas
            canvasPos = returnRelativePosition(particles[i].getPosition());

            // Draws the particle
            particlesGC.setFill(particles[i].getColor());
            particlesGC.fillOval(canvasPos[0] - (circleDiameter[i] / 2), canvasPos[1] - (circleDiameter[i] / 2), circleDiameter[i], circleDiameter[i]);


            // Conditionally draws trails
            if (trails) {
                trailGC.setStroke(particles[i].getColor());
                trailGC.strokeLine(oldCanvasPos[i][0], oldCanvasPos[i][1], canvasPos[0], canvasPos[1]);
            }

            // Sets the current canvas position as the old canvas position
            oldCanvasPos[i][0] = canvasPos[0];
            oldCanvasPos[i][1] = canvasPos[1];

            // Conditionally draws the off-canvas position indicator
            if ((canvasPos[0] < 0 || canvasPos[0] > 800) || (canvasPos[1] < 0 || canvasPos[1] > 720)) {
                double[] indicatorArgs = findIndicatorArguments(canvasPos);
                drawRotatedText(particlesGC, "^", indicatorArgs[0], indicatorArgs[1], indicatorArgs[2]);
            }
        }

        // Conditionally draws the center of mass
        if (centerOfMass) {

            double[] centerOfMassAbsolutePosition = new double[2];

            // Calculates the center of mass
            for (int i = 0; i < 2; i++) {
                centerOfMassAbsolutePosition[i] = ((particles[0].getMass() * particles[0].getPosition()[i])
                        + (particles[1].getMass() * particles[1].getPosition()[i])
                        + (particles[2].getMass() * particles[2].getPosition()[i])) / massSum;
            }

            double[] centerOfMassRelativePosition = returnRelativePosition(centerOfMassAbsolutePosition);


            // Displays the center of mass
            particlesGC.setFill(Color.valueOf("#555555"));
            particlesGC.fillOval((centerOfMassRelativePosition[0] - 5), (centerOfMassRelativePosition[1] - 5), 10, 10);

            particlesGC.setStroke(Color.valueOf("#555555"));
            for (int i = 0; i < 2; i++) {
                for (int j = i + 1; j < 3; j++) {
                    particlesGC.strokeLine(oldCanvasPos[i][0], oldCanvasPos[i][1], oldCanvasPos[j][0], oldCanvasPos[j][1]);
                }
            }
            for (int i = 0; i < 3; i++) {
                particlesGC.strokeLine(oldCanvasPos[i][0], oldCanvasPos[i][1], centerOfMassRelativePosition[0], centerOfMassRelativePosition[1]);
            }

        }
    }

    /**
     * Clears the canvas.
     */
    void clearCanvas() {
        particlesGC.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Returns the display parameters of an off-screen indicator for a particle at a certain canvas position.
     *
     * @param canvasPos The canvas position [x, y] of an off-screen particle.
     * @return A double array [angle, x, y] that denotes the display parameters of an off-screen indicator.
     */
    private double[] findIndicatorArguments(double[] canvasPos) {

        final int OFFSET = 17; // Offset: artificially center indicators.
        final int LOCK = 25; // Prevents indicators from drifting past corners.

        // Declares the cases for drawing the indicators.
        final double[] UPPER_LEFT = {315, 20, 50};
        final double[] UPPER_RIGHT = {45, 750, 20};
        final double[] LOWER_LEFT = {225, 50, 700};
        final double[] LOWER_RIGHT = {135, 775, 670};

        // Cases A1, A2, A3 (x fixed)
        if (canvasPos[0] < 0) {
            if (canvasPos[1] <= LOCK) {
                return UPPER_LEFT;
            } else if (canvasPos[1] >= LOCK && canvasPos[1] <= 720 - LOCK) {
                return new double[]{270, 50, canvasPos[1] + OFFSET};
            } else if (canvasPos[1] >= 720 - LOCK) {
                return LOWER_LEFT;
            }
        }

        // Cases B1, B2, B3 (x fixed)
        else if (canvasPos[0] > 800) {
            if (canvasPos[1] <= LOCK) {
                return UPPER_RIGHT;
            } else if (canvasPos[1] >= LOCK && canvasPos[1] <= 720 - LOCK) {
                return new double[]{90, 750, canvasPos[1] - OFFSET};
            } else if (canvasPos[1] >= 720 - LOCK) {
                return LOWER_RIGHT;
            }
        }

        // Case C1, C2 (y fixed)
        else {
            if (canvasPos[1] <= 0) {
                if (canvasPos[0] <= LOCK) {
                    return UPPER_LEFT;
                } else if (canvasPos[0] > LOCK && canvasPos[0] <= 800 - LOCK) {
                    return new double[]{0, canvasPos[0] - OFFSET, 50};
                } else {
                    return UPPER_RIGHT;
                }

            } else if (canvasPos[1] >= 720) {
                if (canvasPos[0] <= LOCK) {
                    return LOWER_LEFT;
                } else if (canvasPos[0] > LOCK && canvasPos[0] <= 800 - LOCK) {
                    return new double[]{180, canvasPos[0] + OFFSET, 670};
                } else {
                    return LOWER_RIGHT;
                }
            }
        }
        return new double[]{0, 0, 0}; // Should never happen.
    }
}
