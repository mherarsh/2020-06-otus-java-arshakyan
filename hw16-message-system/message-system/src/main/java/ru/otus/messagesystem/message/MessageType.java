package ru.otus.messagesystem.message;

public enum MessageType {
    USER_SAVE("UserSave"),
    USER_GET("UserGet"),
    USER_GET_ALL("UserGetAll");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
