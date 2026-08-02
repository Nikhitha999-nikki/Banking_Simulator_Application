package com.bank.BankSimulator.auth;

import com.bank.BankSimulator.service.EmailService;
import com.bank.BankSimulator.service.OTPService;
import com.bank.BankSimulator.service.OTPStorage;
import com.google.gson.Gson;

import static spark.Spark.post;
public class AuthController {
	private static final Gson gson = new Gson();

	private static class EmailRequest {
		String email;
	}
    private static class ResetPasswordRequest {
        String email;
        String password;
    }
    private static class OTPRequest {
		String email;
        String otp;
	}
    private static class Response {

            boolean success;
            String message;

            Response(boolean success, String message) {
                this.success = success;
                this.message = message;
            }
    }
    private static class GoogleLoginRequest {
        String credential;   
    }

	public static void routes() {

    post("/register", (req, res) -> {

        String username = req.queryParams("username");
        String email = req.queryParams("email");
        String password = req.queryParams("password");

        boolean success = UserRepository.register(username, email, password);

        if (!success) {

            res.status(400);
            return "Already Registered";

        }

        return "Registration Successful";

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
    post("/forgot-password", (req, res) -> {

        res.type("application/json");

        EmailRequest data = gson.fromJson(req.body(), EmailRequest.class);

        // Email Format Validation
        if (data.email == null || !data.email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            res.status(400);

            return gson.toJson(
                new Response(false, "Please enter a valid email address"));
        }

        // Check email exists in database
        if (!UserRepository.emailExists(data.email)) {

            res.status(404);

            return gson.toJson(
                new Response(false, "Email is not registered"));
        }

        System.out.println("Email : " + data.email);

        
        // Generate OTP
        String otp = OTPService.generateOTP();

        // Save OTP in memory
        OTPStorage.saveOtp(data.email, otp);

        System.out.println("Generated OTP : " + otp);

        // Send OTP
        EmailService.sendOTP(data.email, otp);

        return gson.toJson(
            new Response(true, "OTP sent successfully"));

    });
    post("/verify-otp", (req, res) -> {

        res.type("application/json");

        OTPRequest data = gson.fromJson(req.body(), OTPRequest.class);

        String storedOTP = OTPStorage.getOtp(data.email);

        if (storedOTP == null) {
            return gson.toJson(
                new Response(false, "OTP Expired")
            );
        }

        if (!storedOTP.equals(data.otp)) {
            return gson.toJson(
                new Response(false, "Invalid OTP")
            );
        }

        OTPStorage.removeOtp(data.email);

        return gson.toJson(
            new Response(true, "OTP Verified Successfully")
        );

    });
    post("/reset-password", (req, res) -> {
        res.type("application/json");
        ResetPasswordRequest data = gson.fromJson(req.body(), ResetPasswordRequest.class);
        boolean success = UserRepository.updatePassword(data.email, data.password);
        if (!success) {
            return gson.toJson(new Response(false, "Password reset failed"));
        }
        return gson.toJson(new Response(true, "Password reset successful"));
    });
    post("/google-login", (req, res) -> {

        res.type("application/json");

        GoogleLoginRequest data =
                gson.fromJson(req.body(), GoogleLoginRequest.class);

        System.out.println("Google Credential:");
        System.out.println(data.credential);

        return gson.toJson(
            new Response(true, "Google Login Received")
        );

    });
    }

}
