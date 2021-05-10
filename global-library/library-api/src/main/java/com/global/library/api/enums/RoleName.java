package com.global.library.api.enums;

public enum RoleName {
    ROLE_ADMIN(1, "ADMIN"),
    ROLE_STUDENT(2, "STUDENT"),
    ROLE_PROFESSOR(3, "PROFESSOR"),
    ROLE_USER(4, "USER");

    private final long id;
    private final String name;

    RoleName(long id, String name) {
        this.id = id;
        this.name = name;

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
