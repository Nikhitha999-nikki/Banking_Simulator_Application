package com.bank.BankSimulator.auth;

import static spark.Spark.*;
public class AuthController {
	public static void routes() {

        post("/register", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            boolean success = UserRepository.register(username, password);
            if (!success) {
                res.status(400);
                return "User already exists";
            }
            return "Registration successful";
        });

        post("/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            String token = AuthService.login(username, password);
            if (token == null) {
                res.status(401);
                return "Invalid credentials";
            }
            return token;
        });
    }

}
