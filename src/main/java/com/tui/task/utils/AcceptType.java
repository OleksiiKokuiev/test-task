package com.tui.task.utils;

public enum AcceptType {
    JSON("application/json"),
    XML("application/xml");

    private final String value;

    AcceptType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
