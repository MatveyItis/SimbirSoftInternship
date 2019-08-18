package ru.itis.maletskov.internship.util.exception;

public final class ExceptionMessages {
    public static final String CHAT_NOT_FOUND_MESSAGE = "Chat with name or id := %s is not found";
    public static final String CHAT_ALREADY_EXISTS_MESSAGE = "Chat with name: %s already exists. Please select other name";
    public static final String USER_NOT_FOUND_MESSAGE = "User with username := %s is not found";
    public static final String USER_WITH_ID_NOT_FOUND_MESSAGE = "User with id := %s is not found";
    public static final String USER_IS_BANNED_MESSAGE = "User with username := %s is banned";
    public static final String CANNOT_ADD_TO_PRIVATE_CHAT_MESSAGE = "Cannot add user to private chat. Insufficient rights";
    public static final String USER_ALREADY_IN_CHAT_MESSAGE = "Cannot add user to chat because user already in chat";
    public static final String INSUFFICIENT_RIGHTS_MESSAGE = "Insufficient rights";
    public static final String CANNOT_NOMINATE_TO_MODERATOR = "Cannot nominate. '%s' is already a moderator";
    public static final String CANNOT_DOWNGRADE_TO_USER = "Cannot downgrade. '%s' is already user";
    public static final String CANNOT_DISCONNECT_FROM_CHAT = "Cannot disconnect from chat '%s'";
    public static final String INVALID_COMMAND_MESSAGE = "Invalid command. Use '//help' or '//yBot help' for more information";
    public static final String MESSAGE_NOT_FOUND = "Message with id := %s is not found";
    public static final String USERNAME_ALREADY_EXISTS_MESSAGE = "User with name '%s' already exists";
    public static final String CANNOT_DISCONNECT_FROM_CHAT_ADMIN_MESSAGE = "Cannot disconnect from chat '%s'. You are admin or owner.";
    public static final String CANNOT_DISCONNECT_OWNER_FROM_CHAT_MESSAGE = "Cannot disconnect owner from chat '%s'. Insufficient rights";
    public static final String USER_IS_NOT_IN_THE_CHAT_MESSAGE = "User '%s' is not in the chat";

    private ExceptionMessages() {
    }
}
