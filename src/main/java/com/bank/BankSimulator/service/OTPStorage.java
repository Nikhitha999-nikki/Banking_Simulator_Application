package com.bank.BankSimulator.service;

import java.util.HashMap;
import java.util.Map;

public class OTPStorage {
    // String otp=OTPService.generateOTP();
    private static final Map<String,String> otpMap=new HashMap<>();
    public static void saveOtp(String email,String otp)
    {
        otpMap.put(email,otp);
    }
    public static String getOtp(String email)
    {
        return otpMap.get(email);
    }
    public static void removeOtp(String email)
    {
        otpMap.remove(email);
    }
    
}
