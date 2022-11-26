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

    @Override
    public String toString() {
        return message;
    }
}
