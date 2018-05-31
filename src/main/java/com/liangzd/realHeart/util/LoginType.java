package com.liangzd.realHeart.util;

public enum LoginType {
    USER("User"), ADMIN("Admin");

    private String type;

    private LoginType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}