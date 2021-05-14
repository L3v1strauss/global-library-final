package com.global.library.api.enums;

public enum OrderByQuerys {
    NAME("name"),
    RATING("rating");

    private final String name;

    OrderByQuerys(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
