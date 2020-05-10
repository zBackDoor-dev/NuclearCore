package me.darkboy.nuclear.utils;

public enum Permissions {
    FLY_PERM("nuclearcore.fly"),
    ADMIN("nuclearcore.admin");

    private String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
