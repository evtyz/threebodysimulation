package stl.threebodysimulation;

/**
 * A class that provides a template for popup messages that involve custom file names.
 */
class FilenameSpecificMessage implements PopupMessage {

    /**
     * The title of the message.
     */
    private final String title;
    /**
     * The message text.
     */
    private final String message;

    /**
     * Constructs a new popup message with a filename.
     *
     * @param type     The type of message it is.
     * @param filepath The filepath that it points to.
     */
    FilenameSpecificMessage(Type type, String filepath) {
        title = type.getTitle();
        message = String.format(type.getMessageTemplate(), filepath);
    }

    /**
     * Gets the title of the message.
     *
     * @return The title of the message.
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Gets the confirmation message.
     *
     * @return The confirmation message.
     */
    @Override
    public String getMessage() {
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
         * An error that occurs if a file is unable to be deleted.
         */
        DELETE_ERROR {
            @Override
            String getTitle() {
                return "Delete File Error";
            }

            @Override
            String getMessageTemplate() {
                return "The following file could not be deleted:\n'%s'\nIt might be open in another window.";
            }
        },
        /**
         * An error that occurs when the user attempts to overwrite a CSV file that refuses to be edited.
         */
        OVERWRITE_ERROR {
            @Override
            String getTitle() {
                return "Overwrite CSV Error";
            }

            @Override
            String getMessageTemplate() {
                return "The following file could not be edited:\n'%s'\nIt might be open in another window.";
            }
        },
        ;

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
