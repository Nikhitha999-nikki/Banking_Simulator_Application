package com.bank.BankSimulator;

import java.math.BigDecimal;

import com.bank.BankSimulator.exception.AccountNotFoundException;
import com.bank.BankSimulator.exception.InsufficientBalanceException;
import com.bank.BankSimulator.exception.InvalidAmountException;
import com.bank.BankSimulator.model.Account;
import com.bank.BankSimulator.repository.AccountRepository;
import com.bank.BankSimulator.repository.TransactionRepository;
import com.bank.BankSimulator.service.AccountService;
import com.bank.BankSimulator.service.AlertService;
import com.bank.BankSimulator.service.TransactionService;

public class TransactionTest {
	public static void main(String[] args) throws AccountNotFoundException, InvalidAmountException, InsufficientBalanceException
	{
		AccountRepository accRepo = new AccountRepository();
		TransactionRepository trxRepo = new TransactionRepository();
		AccountService accService = new AccountService(accRepo);
		AlertService alertService = new AlertService(new BigDecimal("1000"));
		TransactionService trx = new TransactionService(accService, trxRepo,alertService);
		try {
			Account fromAccount = accService.createAccount("nikki", "nikhithamalapala@gmail.com", new BigDecimal("2500"));
			Account toAccount = accService.createAccount("nandu","nandhu@gmail.com",new BigDecimal("500"));
			System.out.println(fromAccount);
			System.out.println("---------------------------------------------------------");
			System.out.println(toAccount);

		}
		catch(InvalidAmountException e)
		{
			e.printStackTrace();
		}
		
		try {
			Account fromAccount = accService.createAccount("nikki", "nikhithamalapala@gmail.com", new BigDecimal("2500"));
			Account toAccount = accService.createAccount("nandu","nandhu@gmail.com",new BigDecimal("500"));
			trx.withdraw("1000001",new BigDecimal("10"));
			System.out.println(fromAccount);
			System.out.println("---------------------------------------------------------");
			System.out.println(toAccount);

		}
		catch(InvalidAmountException e)
		{
			e.printStackTrace();
		}
		
		
	}
}
