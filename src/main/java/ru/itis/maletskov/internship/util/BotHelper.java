package ru.itis.maletskov.internship.util;

public final class BotHelper {

    private static final String CHAT_BOT_INFO = "Info for rooms: \n" +
            "//room create {name of the room} - create the room \n" +
            "\t -c - private room \n" +
            "//room remove {name of the room} - remove the room \n" +
            "//room rename {new name of the room} - rename the room \n" +
            "//room connect {name of the room} - connect the room \n" +
            "\t -l - user login \n" +
            "//room disconnect - exit from the current room \n" +
            "\t -l - user login \n" +
            "\t -m - minute count \n" +
            "//room disconnect {name of the room} - exit from the room \n" +
            "For users: \n" +
            "//user rename {user login} - rename login of the user \n" +
            "//user ban - ban user -l {login user} -m {minute count} \n" +
            "\t -l - user login, deports user from all rooms \n" +
            "\t -m - minute count, time for which the user will not be able to enter the room \n" +
            "//user moderator {user login} \n" +
            "\t -n - nominate to moderator \n" +
            "\t -d - downgrade to user \n" +
            "//help - help info";

    private static final String Y_BOT_INFO = "Info for yBot: \n" +
            "//yBot find -k {name of the channel} -w {name of the video} \n" +
            "\t -v - view count \n" +
            "\t -l - like count \n" +
            "//yBot changelInfo {name of the chanel} - return 5 last videos from the channel \n" +
            "//yBot videoCommentRandom -k {name of the channel} -w {name of the video} - return 1 random comment from video \n" +
            "//yBot help - help info";

    private BotHelper() {
    }

    public static String getChatBotInfo() {
        return CHAT_BOT_INFO;
    }

    public static String getYBotInfo() {
        return Y_BOT_INFO;
    }
}
