package com.cg.banking.test;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.cg.banking.beans.Account;
import com.cg.banking.exceptions.AccountBlockedException;
import com.cg.banking.exceptions.AccountNotFoundException;
import com.cg.banking.exceptions.BankingServicesDownException;
import com.cg.banking.exceptions.InsufficientAmountException;
import com.cg.banking.exceptions.InvalidPinNumberException;
import com.cg.banking.services.BankingServices;
import com.cg.banking.services.BankingServicesImpl;
import com.cg.banking.util.BankingDBUtil;
public class BankingServiceTest {

	private static BankingServices services;
	@BeforeClass
	public static void setUpTestEnv() {
		services=new BankingServicesImpl();
	}
	@Before
	public void setUpTestData() {
		Account account1=new Account(5001, "Savings", "Active", 5000,1234);
		Account account2=new Account(5002, "Savings", "Blocked", 10000,2345);
		BankingDBUtil.customer.put(account1.getAccountNo(), account1);
		BankingDBUtil.customer.put(account2.getAccountNo(), account2);
		BankingDBUtil.ACCOUNT_NUMBER=5002;
	}
	
//	********************************Deposit*****************************************************
	@Test
	public void testDepositAmountForValidAccountNumber(){
		Account account=new Account(5001, "Savings", "Active", 5000,1234);
		account.setAccountBalance(services.depositAmount(account.getAccountNo(), 5000));
		int expectedAmount=(int) account.getAccountBalance();
		int actualAmount=10000;
		Assert.assertEquals(expectedAmount, actualAmount);
	}
	@Test(expected=AccountNotFoundException.class)
	public void testDepositAmountForInvalidAccountNumber() {
		services.depositAmount(4554, 5000);
	}
//	*****************************Withdraw***********************************************************
	@Test
	public void testWithdrawAmountForValidAccountNumberAndValidPinNumber() {
		Account account=new Account(5001, "Savings", "Active", 5000,1234);
		account.setAccountBalance(services.withdrawAmount(account.getAccountNo(), 2000,account.getPinNumber()));
		int expectedAmount=(int) account.getAccountBalance();
		int actualAmount=3000;
		Assert.assertEquals(expectedAmount, actualAmount);
	}
	@Test(expected=AccountNotFoundException.class)
	public void testWithdrawAmountForInvalidAccountNumberAndValidPinNumber() throws AccountNotFoundException{
		services.withdrawAmount(5008, 5000, 4545);
	}
	@Test(expected=InvalidPinNumberException.class)
	public void testWithdrawAmountForValidAccountNumberAndInvalidPinNumber() throws InvalidPinNumberException{
		services.withdrawAmount(5001, 2000, 7894);
	}
	@Test(expected=AccountBlockedException.class)
	public void testWithdrawAmountForBlockedAccount()throws AccountBlockedException{
		services.withdrawAmount(5002, 2000, 2345);
	}
	@Test(expected=InsufficientAmountException.class)
	public void testWithdrawAmountForInsufficientAccount()throws InsufficientAmountException{
		services.withdrawAmount(5001, 6000, 1234);
	}
//	*****************************************************************************************************
	@Test(expected=AccountNotFoundException.class)
	public void testGetAccountDetailsForInvalidAccountNumber() throws AccountNotFoundException{
		services.getAccountDetails(12345);
	}
	@Test
	public void testGetAccountDetailsForValidAccountNumber() {
		Account expectedAccount=new Account(5001, "Savings", "Active", 5000,1234);
		Account actualAccount=services.getAccountDetails(5001);
		Assert.assertEquals(expectedAccount, actualAccount);
	}
	@Test
	public void testGetAllAccountDetails() throws BankingServicesDownException{
		Account account1=new Account(5001, "Savings", "Active", 5000,1234);
		Account account2=new Account(5002, "Savings", "Blocked", 10000,2345);
		
		ArrayList<Account> expectedAccountList=new ArrayList<Account>();
		expectedAccountList.add(account1);
		expectedAccountList.add(account2);
		ArrayList<Account>actualAccountList=(ArrayList<Account>) services.getAllAccountDetails();
		Assert.assertEquals(expectedAccountList, actualAccountList);
	}
	@Test
	public void testAccountStatusForValidAccountNumber() throws AccountBlockedException{
		String expectedStatus="Active";
		String actualStatus=services.accountStatus(5001);
		Assert.assertEquals(expectedStatus, actualStatus);
	}
	
	@After
	public void tearDownTestData() {
		BankingDBUtil.customer.clear();
		BankingDBUtil.ACCOUNT_NUMBER=100;
	}
	@AfterClass
	public static void tearDownTestEnv() {
		services=null;
	}
}
