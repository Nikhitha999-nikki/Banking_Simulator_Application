package com.bank.BankSimulator.auth;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AuthService {
	private static final Set<String> activeTokens = new HashSet<>();

    public static String login(String username, String password) {
        if (UserRepository.validate(username, password)) {
            String token = UUID.randomUUID().toString();
            activeTokens.add(token);
            return token;
        }
        return null;
    }

    public static boolean isAuthorized(String token) {
        return activeTokens.contains(token);
    }
}
