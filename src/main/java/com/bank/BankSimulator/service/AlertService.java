package com.bank.BankSimulator.service;

import java.math.BigDecimal;
import com.bank.BankSimulator.util.EmailUtil;
import com.bank.BankSimulator.model.Account;

public class AlertService {
	private final BigDecimal threshold;
	
	public AlertService(BigDecimal threshold)
	{
		this.threshold = threshold;
	}
	
	public void checkBalance(Account account)
	{
		if(account.getBalance().compareTo(threshold)<=0)
		{
			String subject = "⚠ Low Balance Alert – Account " + account.getAccountNumber();
			String message =
			        "Dear " + account.getHolderName() + ",\n\n"
			      + "This is a friendly reminder that your account balance has fallen below the minimum threshold.\n\n"
			      + "📌 Current Balance: " + account.getBalance() + "\n"
			      + "📌 Account Number: " + account.getAccountNumber() + "\n\n"
			      + "To avoid any service interruptions or penalties, please deposit funds at your earliest convenience.\n\n"
			      + "If you have recently made a deposit, please ignore this message.\n\n"
			      + "Thank you for banking with us.\n\n"
			      + "Warm regards,\n"
			      + "Bank Simulator Team";
			
			EmailUtil.sendEmail(account.getEmail(),subject,message);
		}
	}
}
