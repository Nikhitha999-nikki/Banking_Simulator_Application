package com.bank.BankSimulator.auth;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
	private static final Map<String, String> users = new HashMap<>();

    public static boolean register(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, password);
        return true;
    }

    public static boolean validate(String username, String password) {
        return users.containsKey(username) &&
               users.get(username).equals(password);
    }
}
