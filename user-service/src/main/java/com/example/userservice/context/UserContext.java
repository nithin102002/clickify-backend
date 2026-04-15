package com.example.userservice.context;

public class UserContext {

    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();

    public static void setUser(String username) {
        currentUser.set(username);
    }

    public static String getUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }
}