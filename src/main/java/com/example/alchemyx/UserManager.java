package com.example.alchemyx;
public class UserManager {
    private static String userName;

    public static void setUserName(String name) {
        userName = name;
    }

    public static String getUserName() {
        return userName;
    }
}
