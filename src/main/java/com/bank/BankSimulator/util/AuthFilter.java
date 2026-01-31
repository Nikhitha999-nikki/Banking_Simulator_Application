package com.bank.BankSimulator.util;

import static spark.Spark.*;

import com.bank.BankSimulator.auth.AuthService;
public class AuthFilter {
	public static void apply() {
		before("/accounts/*", (req, res) -> {
		    String token = req.headers("Authorization");
		    if (token == null || !AuthService.isAuthorized(token)) {
		        res.type("application/json");
		        halt(401, "{\"message\":\"Unauthorized\"}");
		    }
		});

		
	}
}
