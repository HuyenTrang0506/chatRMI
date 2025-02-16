package server;

import java.io.Serializable;

public class ChatMessageObj implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sender;
    private String content;

    public ChatMessageObj(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return sender + ": " + content;
    }
}