package ru.itis.maletskov.internship.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Greeting {
    private String content;

    public Greeting(String content) {
        this.content = content;
    }
}
