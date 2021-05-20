package com.global.library.api.enums;

public enum RequestStatusName {
    CANCELLED("cancelled"),
    CREATED("created"),
    CONFIRMED("confirmed"),
    PROCESSED("processed"),
    RETURNED("returned");

    private String nameDB;

    RequestStatusName(String nameDB) {
        this.nameDB = nameDB;
    }

    public String getNameDB() {
        return nameDB;
    }
}
