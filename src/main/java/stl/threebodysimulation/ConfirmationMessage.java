package stl.threebodysimulation;

/**
 * A class that provides a template for user confirmation messages.
 */
class ConfirmationMessage {

    /**
     * The types of confirmation messages, and their corresponding message templates and titles
     */
    enum Type {
        /**
         * For confirmations related to CSV files.
         */
        CSV_CONFIRMATION {
            String getTitle() {
                return "Save CSV Confirmation";
            }
            String getMessageTemplate() {
                return "This simulation will overwrite the following CSV:\n'%s'\nAre you sure you want to proceed?";
            }
        },
        /**
         * For confirmations related to tbsettings files.
         */
        TEMPLATE_CONFIRMATION {
            String getTitle() {
                return "Save Template Confirmation";
            }
            String getMessageTemplate() {
                return "You are about to overwrite the following save:\n'%s'\nAre you sure you want to proceed?";
            }
        };

        /**
         * Returns the title of the message.
         *
         * @return The title of the message.
         */
        abstract String getTitle();

        /**
         * Returns the template of the message for use in String.format.
         *
         * @return The template of the message.
         */
        abstract String getMessageTemplate();
    }

    /**
     * The title of the message.
     */
    private final String title;

    /**
     * The message text.
     */
    private final String message;

    /**
     * Constructs a new confirmation message
     *
     * @param type The type of message it is.
     * @param filepath The filepath that it points to.
     */
    ConfirmationMessage(Type type, String filepath) {
        title = type.getTitle();
        message = String.format(type.getMessageTemplate(), filepath);
    }

    /**
     * Gets the title of the message.
     *
     * @return The title of the message.
     */
    String getTitle() {
        return title;
    }

    /**
     * Gets the confirmation message.
     *
     * @return The confirmation message.
     */
    String getMessage() {
        return message;
    }

}
