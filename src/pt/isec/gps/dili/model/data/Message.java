package pt.isec.gps.dili.model.data;

public class Message {
    private final String field;
    private final MessageType type;
    private final String message;

    public Message(String field, MessageType type, String message) {
        this.field = field;
        this.type = type;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public MessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
