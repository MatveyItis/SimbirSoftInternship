package ru.itis.maletskov.internship.util.validator;

public enum FieldNames {
    LOGIN("login"),
    PASSWORD("password");

    private String name;

    FieldNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
