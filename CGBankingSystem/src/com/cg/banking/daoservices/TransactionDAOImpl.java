package com.cg.banking.daoservices;
import java.util.ArrayList;
import java.util.List;
import com.cg.banking.beans.Transaction;
import com.cg.banking.util.BankingDBUtil;
public class TransactionDAOImpl implements TransactionDAO{
	@Override
	public Transaction save(int accountNumber, Transaction transaction) {
		transaction.setTransactionId(BankingDBUtil.getTRANSACTION_NUMBER());
		BankingDBUtil.customer.get(accountNumber).getTransactions().put((long)transaction.getTransactionId(),transaction);
		return transaction;
	}
	@Override
	public Transaction findOne(int accountNumber, int transactionId) {
		return BankingDBUtil.customer.get(accountNumber).getTransactions().get(transactionId);
	}
	@Override
	public List<Transaction> findAll(int accountNumber) {
		return new ArrayList<Transaction>(BankingDBUtil.customer.get(accountNumber).getTransactions().values());
	}
}
