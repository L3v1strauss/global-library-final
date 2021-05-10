package com.global.library.api.enums;

public enum GenreName {
    FICTION(1), SCIENCE(2), MATH(3), ECONOMY(4), COMPUTER_SCIENCE(5);

    private final long id;

    GenreName(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
