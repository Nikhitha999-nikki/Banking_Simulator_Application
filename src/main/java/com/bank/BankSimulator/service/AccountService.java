package com.bank.BankSimulator.service;

import java.math.BigDecimal;

import java.util.Collection;

import com.bank.BankSimulator.exception.InvalidAmountException;
import com.bank.BankSimulator.exception.AccountNotFoundException;
import com.bank.BankSimulator.model.Account;
import com.bank.BankSimulator.repository.AccountRepository;

public class AccountService {
	private AccountRepository repo;
	
	public AccountService(AccountRepository repo)
	{
		this.repo=repo;
	}
	public Account createAccount(String holderName, String email, BigDecimal openingBalance)throws InvalidAmountException{
		if(openingBalance.compareTo(BigDecimal.ZERO)<0)
		{
			throw new InvalidAmountException("opening balance cannot be negative");
			
		}
		Account account = new Account(holderName, email, openingBalance);
		repo.save(account);
		return account;
	}
	public Account getAccount(String accountNumber)throws AccountNotFoundException{
		Account account=repo.findAccountByNumber(accountNumber);
		if(account==null)
		{
			throw new AccountNotFoundException("Account not found: " + accountNumber);
		}
		return account;
	}
	
	public Collection<Account> listAll()
	{
		return repo.findAll();
	}
}
