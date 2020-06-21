# Three-Body Simulation
A JavaFX app that computationally simulates the [three-body problem](https://en.wikipedia.org/wiki/Three-body_problem) in classical mechanics.

Built for an ICS3U culminating project, by Evan Zheng and Vladislav Aleinikov, June 2020.

## Downloads
Download information can be found under the [releases](https://github.com/richmondvan/threebodysimulation/releases) section. 
The app currently only officially supports Windows x64.

## Functionality
A screenshot of the UI can be seen here:

![Screenshot](samples/sample1.gif)

Users enter the following global settings:
- The time to skip to
- The speed of simulation
- The preferred number format to display
- Visual preferences

As well as the following information about three different point-mass particles:
- Mass
- X and Y positions
- X and Y velocities
- Color

The simulation runs according to these settings. 
A live view of the particles at a given time is shown in the display, while live readings of position, velocity, and acceleration are shown in the panel above.
Users also have the option to log the positions, velocities, and accelerations into a CSV (comma-separated-value) file for later analysis.

Users can save templates with custom settings and access them later in the Saves tab. These template files can be loaded into other devices that have the app installed.
There are also some default templates that offer interesting scenarios for the particles.

For more information, check the manual inside the application.

## Libraries and Tools Used
- [JavaFX](https://openjfx.io/) for the GUI framework.
- [JBootX](https://github.com/dicolar/jbootx) for Bootstrap-like visual styling.
- [Apache Commons Math](http://commons.apache.org/proper/commons-math/) for integrations of ordinary differential equations.
- [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/) for logging statistics and templates into CSV files.
- [GraalVM](https://www.graalvm.org/) for native-image compilation of Java programs.
- [Gluon Client-Maven-Plugin](https://github.com/gluonhq/client-maven-plugin) for GraalVM compatibility with JavaFX.
- [Ikonli](https://github.com/kordamp/ikonli) for UI icons.
- [Launch4j](http://launch4j.sourceforge.net/) for packaging .jar files into .exe.
- [Apache Maven Shade](https://maven.apache.org/plugins/maven-shade-plugin/) for building .jar files.

## License
See [LICENSE](https://github.com/richmondvan/threebodysimulation/blob/master/LICENSE) file.
