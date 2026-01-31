package com.bank.BankSimulator.service;

import java.math.BigDecimal;


import com.bank.BankSimulator.exception.AccountNotFoundException;
import com.bank.BankSimulator.exception.InsufficientBalanceException;
import com.bank.BankSimulator.exception.InvalidAmountException;
import com.bank.BankSimulator.model.Account;
import com.bank.BankSimulator.repository.TransactionRepository;
import com.bank.BankSimulator.util.FileReportUtil;

public class TransactionService {
	private AccountService accountService;
	private TransactionRepository transactionRepository;
	private AlertService alertService;

	public TransactionService(AccountService accountService, TransactionRepository transactionRepository,AlertService alertService) {
		this.accountService = accountService;
		this.transactionRepository = transactionRepository;
		 this.alertService = alertService; 

	}

	public void deposit(String accNo, BigDecimal amount) throws InvalidAmountException, AccountNotFoundException {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidAmountException("Amount should not be negative");
		}

		Account acc = accountService.getAccount(accNo);
		acc.credit(amount);

		FileReportUtil.writeLine("DEPOSITE | Acc: " + accNo + " | Amount: " + amount);

		transactionRepository.logTransaction("DEPOSIT", accNo, amount.doubleValue(), null);

		alertService.checkBalance(acc);
	}

	public void withdraw(String accNo, BigDecimal amount)
			throws InvalidAmountException, AccountNotFoundException, InsufficientBalanceException {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidAmountException("Amount should not be negative");
		}

		Account account = accountService.getAccount(accNo);

		if (account.getBalance().compareTo(amount) < 0) {
			throw new InsufficientBalanceException("Insufficient Balance");
		}

		account.debit(amount);

		FileReportUtil.writeLine("WITHDRAW | Acc: " + accNo + " | Amount: " + amount);

		transactionRepository.logTransaction("WITHDRAW", accNo, amount.doubleValue(), null);
		alertService.checkBalance(account);
	}

	public void transfer(String fromAcc, String toAcc, BigDecimal amount)
			throws AccountNotFoundException, InsufficientBalanceException {

		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Amount should not be negative");
		}

		Account sender = accountService.getAccount(fromAcc);
		Account recevier = accountService.getAccount(toAcc);

		if (sender.getBalance().compareTo(amount) < 0) {
			throw new InsufficientBalanceException("Insufficient Balance");
		}

		sender.debit(amount);
		recevier.credit(amount);

		FileReportUtil.writeLine("TRANSFER | FromAcc: " + fromAcc + " | ToAccount: " + toAcc + " | Amount " + amount);

		transactionRepository.logTransaction("TRANSFER", fromAcc, amount.doubleValue(), toAcc);
		alertService.checkBalance(sender);
		alertService.checkBalance(recevier);
	}

}
