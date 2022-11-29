package model.data;

public class Message {
    String field;
    MessageType type;
    String message;

    public Message(String field, MessageType type, String message) {
        this.field = field;
        this.type = type;
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return message;
    }
}
