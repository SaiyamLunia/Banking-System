package com.cg.banking.services;
import java.util.List;
import java.util.Map;
import com.cg.banking.beans.Account;
import com.cg.banking.beans.Transaction;
import com.cg.banking.daoservices.AccountDAO;
import com.cg.banking.daoservices.AccountDAOImpl;
import com.cg.banking.daoservices.TransactionDAO;
import com.cg.banking.daoservices.TransactionDAOImpl;
import com.cg.banking.exceptions.AccountBlockedException;
import com.cg.banking.exceptions.AccountNotFoundException;
import com.cg.banking.exceptions.BankingServicesDownException;
import com.cg.banking.exceptions.InsufficientAmountException;
import com.cg.banking.exceptions.InvalidAccountTypeException;
import com.cg.banking.exceptions.InvalidAmountException;
import com.cg.banking.exceptions.InvalidPinNumberException;
public class BankingServicesImpl implements BankingServices{
	private AccountDAO accountDao=new AccountDAOImpl();
	private TransactionDAO transactionDao=new TransactionDAOImpl();
	private static int maxInvalidPinAttempts=3;
	Account account = null;
	@Override
	public Account openAccount(String accountType, float initBalance)
			throws InvalidAmountException, InvalidAccountTypeException, BankingServicesDownException {
		Account account=new Account(accountType,initBalance);
		return account=accountDao.save(account);
	}
	@Override
	public float depositAmount(int accountNumber, float amount)
			throws AccountNotFoundException, BankingServicesDownException, AccountBlockedException {
		account = getAccountDetails(accountNumber);
		if(account.getAccountStatus().equalsIgnoreCase("Blocked"))
			throw new AccountBlockedException("Your account is blocked.");
		else
			account.setAccountBalance(account.getAccountBalance()+amount);
		transactionDao.save( accountNumber, new Transaction(amount,"Deposit"));
		return account.getAccountBalance();
	}
	@Override
	public float withdrawAmount(int accountNumber, float amount, int pinNumber) throws InsufficientAmountException,
			AccountNotFoundException, InvalidPinNumberException, BankingServicesDownException, AccountBlockedException {
		account = getAccountDetails(accountNumber);
		if(account.getAccountStatus().equalsIgnoreCase("Blocked"))
			throw new AccountBlockedException("Your Account is Blocked. Contact with the Bank immediately.");
		if(account.getPinNumber()!=pinNumber) {
			account.incrementInvalidPinAttempt();
			if(account.getInvalidPinAttempt()==maxInvalidPinAttempts) {
				account.setAccountStatus("Blocked");
				throw new AccountBlockedException("Invalid pin entered too many times, account blocked.");
			}
			throw new InvalidPinNumberException("Invalid Pin Number, "+(maxInvalidPinAttempts-account.getInvalidPinAttempt())+ " attempts left");
		}
		if(account.getInvalidPinAttempt()>0 &&  account.getAccountStatus().equalsIgnoreCase("Active"))
			account.resetInvalidPinAttempt();
		if(account.getAccountBalance()-amount<500)
			throw new InsufficientAmountException("Cannot withdraw. Minimum balance should be greater than or equal to 500");
		else{
			account.setAccountBalance(account.getAccountBalance()-amount);
			transactionDao.save((int) accountNumber, new Transaction(amount,"Withdraw"));
		}
		return account.getAccountBalance();
	}

	@Override
	public boolean fundTransfer(int accountNoTo, int accountNoFrom, float transferAmount, int pinNumber)
			throws InsufficientAmountException, AccountNotFoundException, InvalidPinNumberException,
			BankingServicesDownException, AccountBlockedException {
		Account customerNoTo = getAccountDetails(accountNoTo);
		Account customerNoFrom = getAccountDetails(accountNoFrom);
		if(customerNoTo.getAccountStatus().equalsIgnoreCase("Blocked"))
			throw new AccountBlockedException("Reciever Account is blocked. Transfer can't be done.");
		if(customerNoFrom.getAccountStatus().equalsIgnoreCase("Blocked"))
			throw new AccountBlockedException("Your Account is blocked. Transfer can't be done.");
		withdrawAmount(accountNoFrom, transferAmount, pinNumber);
		transactionDao.save(customerNoFrom.getAccountNo(), new Transaction(transferAmount,"Withdraw"));
		depositAmount(accountNoTo, transferAmount);
		transactionDao.save(customerNoTo.getAccountNo(), new Transaction(transferAmount,"Deposit"));
		return true;
	}
	@Override
	public Account getAccountDetails(int accountNo) throws AccountNotFoundException, BankingServicesDownException {
		account=accountDao.findOne(accountNo);
		if(account==null)
			throw new AccountNotFoundException("Your account does not exist.");
		else
			return account;
	}

	@Override
	public List<Account> getAllAccountDetails() throws BankingServicesDownException {
		return accountDao.findAll();
	}
	@Override
	public Map<Long,Transaction> getAccountAllTransaction(long accountNo)
			throws BankingServicesDownException, AccountNotFoundException {
		return null;
	}
	@Override
	public String accountStatus(int accountNumber)
			throws BankingServicesDownException, AccountNotFoundException, AccountBlockedException {
		account = getAccountDetails(accountNumber);
		if(account.getAccountStatus().equalsIgnoreCase("Blocked"))
			throw new AccountBlockedException("Your account is blocked.");
		else
			return "Active";
	}
}