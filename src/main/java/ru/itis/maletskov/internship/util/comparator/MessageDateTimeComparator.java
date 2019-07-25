package ru.itis.maletskov.internship.util.comparator;

import ru.itis.maletskov.internship.model.Message;

import java.util.Comparator;

public class MessageDateTimeComparator implements Comparator<Message> {
    @Override
    public int compare(Message message1, Message message2) {
        return message1.getDateTime().compareTo(message2.getDateTime());
    }
}
