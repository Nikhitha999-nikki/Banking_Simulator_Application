package com.bank.BankSimulator.auth;

import com.google.gson.Gson;

import static spark.Spark.post;
public class AuthController {
	private static final Gson gson = new Gson();

	private static class EmailRequest {
		String email;
	}

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
        // post("/forgot-password", (req, res) -> {
        //     res.type("application/json");
        //     Gson gson = new Gson();
        //     EmailRequest data = gson.fromJson(req.body(), EmailRequest.class);
        //     System.out.println("Email : " + data.email);
        //     Map<String, Object> response = new HashMap<>();
        //     response.put("success", true);
        //     response.put("message", "Email received successfully");

        //     return gson.toJson(response);
        // });
    }

}
