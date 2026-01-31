package com.bank.BankSimulator;

import java.math.BigDecimal;
import java.util.Collection;
import com.bank.BankSimulator.exception.AccountNotFoundException;
import com.bank.BankSimulator.exception.InvalidAmountException;
import com.bank.BankSimulator.model.Account;
import com.bank.BankSimulator.repository.AccountRepository;
import com.bank.BankSimulator.service.AccountService;

public class AccountTest {
	public static void main(String[] args) throws InvalidAmountException, AccountNotFoundException
	{
		try {
			AccountRepository repo = new AccountRepository();
			AccountService service = new AccountService(repo);
			
			Account acc1 = service.createAccount("nikki", "nikhithamalapala@gmail.com", new BigDecimal("2500"));
			Account acc2 = service.createAccount("nandhu", "nikhithacm027.21057@gmail.com", new BigDecimal("1000"));
//			System.out.println(acc1);
//			System.out.println(acc2);
			
			//Fetch Account by Account Number
			Account newAcc1 = service.getAccount("1000000");
			//System.out.println(newAcc1);
			
			// List All Accounts
			Collection<Account> allAccounts = service.listAll();
			for(Account a: allAccounts)
			{
				System.out.println(a);
			}
		}
			catch(InvalidAmountException | AccountNotFoundException e) {
				e.printStackTrace();
				
			}
		
		
		
	}
}

