package com.bank.BankSimulator.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class TransactionRepository {
	public void logTransaction(String type,String accNo, double amount , String target_number)
	{
		String query = "INSERT INTO transactions(type,account_number,amount,target_account) values(?,?,?,?)";
		try (Connection con = DBConnection.getConnection();
			     PreparedStatement pstmt = con.prepareStatement(query);
				){
			pstmt.setString(1,type);
			pstmt.setString(2, accNo);
			pstmt.setDouble(3, amount);
			pstmt.setString(4, target_number);
			
			pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("DB Insert Error :"+e.getMessage());
		}

	}
	public List<Map<String, Object>> getTransactionsByAccount(String accNo) {

		List<Map<String, Object>> transactions = new ArrayList<>();

		String query =
			"SELECT id, type, account_number, amount, target_account, timestamp " +
			"FROM transactions " +
			"WHERE account_number = ? " +
			"ORDER BY timestamp DESC";

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(query)
		) {

			pstmt.setString(1, accNo);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Map<String, Object> transaction = new HashMap<>();

				transaction.put("id", rs.getInt("id"));
				transaction.put("type", rs.getString("type"));
				transaction.put("accountNumber", rs.getString("account_number"));
				transaction.put("amount", rs.getBigDecimal("amount"));
				transaction.put("targetAccount", rs.getString("target_account"));
				transaction.put("timestamp", rs.getTimestamp("timestamp"));

				transactions.add(transaction);
			}

		} catch (Exception e) {
			System.out.println("DB Fetch Error : " + e.getMessage());
		}

		return transactions;
	}
}
