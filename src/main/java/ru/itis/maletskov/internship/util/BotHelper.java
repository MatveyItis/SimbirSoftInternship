package ru.itis.maletskov.internship.util;

public final class BotHelper {
    private BotHelper() {
    }

    private static final String CHAT_BOT_INFO = "Available commands:\n" +
            "//room create {name of the room} - create the room. \n" +
            "\t ";
    private static final String Y_BOT_INFO = "";

    public static String getChatBotInfo() {
        return CHAT_BOT_INFO;
    }

    public static String getYBotInfo() {
        return Y_BOT_INFO;
    }
}
