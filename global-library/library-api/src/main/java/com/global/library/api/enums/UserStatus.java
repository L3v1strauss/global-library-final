package com.global.library.api.enums;

public enum UserStatus {
    Disabled(0, "Disactive"), Enabled(1, "Active");

    private final int id;
    private final String name;

    UserStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
