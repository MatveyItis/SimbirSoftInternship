package ru.itis.maletskov.internship.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ChatCommands {
    public static Set<String> chatCommandSet = Stream.of(
            "//room create", "//room remove", "//room connect",
            "//room connect -l", "//room disconnect",
            "//room disconnect -l", "//room disconnect -l -m", "//room help")
            .collect(Collectors.toCollection(HashSet::new));

    public static Set<String> userCommandsSet = Stream.of(
            "//user rename -l", "//user ban -l", "//user ban -l -m",
            "//user moderator -n", "//user moderator -d", "//user help")
            .collect(Collectors.toCollection(HashSet::new));

    public static Set<String> yBotCommandsSet = Stream.of(
            "//yBot find -k -l", "//yBot find -k -l -V -L", "//yBot help")
            .collect(Collectors.toCollection(HashSet::new));
}
