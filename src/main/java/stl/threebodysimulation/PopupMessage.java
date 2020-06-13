package stl.threebodysimulation;

/**
 * The common interface for popup messages, with a title and a message.
 */
interface PopupMessage {

    /**
     * Gets the title of the error.
     *
     * @return The title of the error, to be displayed in the window border.
     */
    String getTitle();

    /**
     * Gets the message of the error.
     *
     * @return The message of the error, to be displayed in the body of the error popup.
     */
    String getMessage();
}
