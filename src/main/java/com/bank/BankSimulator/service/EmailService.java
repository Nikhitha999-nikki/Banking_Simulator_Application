package com.bank.BankSimulator.service;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailService {
    private static final String EMAIL = "noreplysbipamidi@gmail.com";

    private static final String PASSWORD = "yvxt weiq qzhd mzsv";

    public static void sendOTP(String toEmail, String otp) throws Exception {

        Properties properties = new Properties();

        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session = Session.getInstance(properties,

                new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(EMAIL,PASSWORD);

                    }

                });
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(EMAIL));

        message.setRecipients(

                Message.RecipientType.TO,

                InternetAddress.parse(toEmail)

        );

        message.setSubject("Bank Simulator Password Reset OTP");

        message.setText(

                "Your OTP is : " + otp +

                "\n\nDo not share this OTP to others." +
                "\n\nNeed help? Reach us at anytime at working hours." +
                "\n\nWarm regards,\n" + 
                "\b\bBank Simulator Team"

        );

        Transport.send(message);

    }
}
