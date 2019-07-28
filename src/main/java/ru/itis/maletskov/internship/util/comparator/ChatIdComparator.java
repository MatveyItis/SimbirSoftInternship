package ru.itis.maletskov.internship.util.comparator;

import ru.itis.maletskov.internship.model.Chat;

import java.util.Comparator;

public class ChatIdComparator implements Comparator<Chat> {
    @Override
    public int compare(Chat c1, Chat c2) {
        return c2.getId().compareTo(c1.getId());
    }
}
