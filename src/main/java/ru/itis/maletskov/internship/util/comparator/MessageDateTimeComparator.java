package ru.itis.maletskov.internship.util.comparator;

import ru.itis.maletskov.internship.dto.MessageDto;

import java.util.Comparator;

public class MessageDateTimeComparator implements Comparator<MessageDto> {
    @Override
    public int compare(MessageDto message1, MessageDto message2) {
        return message1.getDateTime().compareTo(message2.getDateTime());
    }
}
