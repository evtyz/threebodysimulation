package stl.threebodysimulation;

class ConfirmationMessage {

    // TODO: Documentation

    enum Type {
        CSV_CONFIRMATION {
            String getTitle() {
                return "Save CSV Confirmation";
            }
            String getMessageTemplate() {
                return "This simulation will overwrite the following CSV:\n'%s'\nAre you sure you want to proceed?";
            }
        },
        TEMPLATE_CONFIRMATION {
            String getTitle() {
                return "Save Template Confirmation";
            }
            String getMessageTemplate() {
                return "You are about to overwrite the following save:\n'%s'\nAre you sure you want to proceed?";
            }
        };

        abstract String getTitle();

        abstract String getMessageTemplate();
    }

    private String title;

    private String message;

    ConfirmationMessage(Type type, String filepath) {
        title = type.getTitle();
        message = String.format(type.getMessageTemplate(), filepath);
    }

    String getTitle() {
        return title;
    }

    String getMessage() {
        return message;
    }

}
