package stl.threebodysimulation;

/**
 * A class that provides a template for user confirmation messages.
 */
class ConfirmationMessage {

    /**
     * The title of the message.
     */
    private final String title;
    /**
     * The message text.
     */
    private final String message;

    /**
     * Constructs a new confirmation message with a filename. Should only be used for overwriting files.
     *
     * @param type     The type of message it is.
     * @param filepath The filepath that it points to.
     */
    ConfirmationMessage(Type type, String filepath) {
        title = type.getTitle();
        message = String.format(type.getMessageTemplate(), filepath);
    }

    /**
     * Constructs a new confirmation message without a filename. Should only be used for non-filename confirmations.
     *
     * @param type The type of message it is.
     */
    @SuppressWarnings("SameParameterValue") // In case other errors come up, this constructor will stay parameterized.
    ConfirmationMessage(Type type) {
        title = type.getTitle();
        message = type.getMessageTemplate();
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
         * For confirmations related to overwriting tbsettings files.
         */
        TEMPLATE_CONFIRMATION {
            String getTitle() {
                return "Save Template Confirmation";
            }

            String getMessageTemplate() {
                return "You are about to overwrite the following save:\n'%s'\nAre you sure you want to proceed?";
            }
        },
        /**
         * For confirmations related to deleting tbsettings files.
         */
        DELETE_CONFIRMATION {
            String getTitle() {
                return "Delete Template Confirmation";
            }

            String getMessageTemplate() {
                return "You are about to delete the following template:\n'%s'\nAre you sure you want to proceed?";
            }
        },
        /**
         * For confirmations related to loading settings files.
         */
        LOAD_CONFIRMATION {
            String getTitle() {
                return "Load Template Confirmation";
            }

            String getMessageTemplate() {
                return "You are about to overwrite existing settings with a saved template. Are you sure you want to proceed?";
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

}
