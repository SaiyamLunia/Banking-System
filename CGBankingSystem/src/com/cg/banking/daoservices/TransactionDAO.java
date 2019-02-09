package com.cg.banking.daoservices;
import java.util.List;
import com.cg.banking.beans.Transaction;
public interface TransactionDAO {
	Transaction save(int accountNumber, Transaction transaction);
	Transaction findOne(int accountNumber, int transactionId);
	List<Transaction> findAll(int accountNumber);
}
