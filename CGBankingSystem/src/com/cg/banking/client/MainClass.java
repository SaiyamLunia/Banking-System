package com.cg.banking.client;
import java.util.Scanner;
import com.cg.banking.beans.Account;
import com.cg.banking.exceptions.AccountBlockedException;
import com.cg.banking.exceptions.AccountNotFoundException;
import com.cg.banking.exceptions.BankingServicesDownException;
import com.cg.banking.exceptions.InsufficientAmountException;
import com.cg.banking.exceptions.InvalidAccountTypeException;
import com.cg.banking.exceptions.InvalidAmountException;
import com.cg.banking.exceptions.InvalidPinNumberException;
import com.cg.banking.services.BankingServices;
import com.cg.banking.services.BankingServicesImpl;
public class MainClass {
	public static void main(String[] args) {
		int accountNumber;
		int pinNumber;
		int amount;
		BankingServices bankingService =new BankingServicesImpl();
		Account account1=null;
		Account account2=null;
		Account account3=null;	
		Account account4=null;	
		try {
			account1=bankingService.openAccount("Savings", 2000);
			account1.setAccountStatus("Active");
			account2=bankingService.openAccount("Savings", 9000);
			account2.setAccountStatus("Active");
			account3=bankingService.openAccount("Current", 50000);
			account3.setAccountStatus("Active");
			account4=bankingService.openAccount("Current", 75000);
			account4.setAccountStatus("Blocked");
		}catch (BankingServicesDownException | InvalidAccountTypeException e) {
			System.out.println("Error: "+e.getMessage());
		}
		System.out.println("Account Details:\n "+account1);
		System.out.println("Account Details:\n "+account2);
		System.out.println("Account Details:\n "+account3);
		System.out.println("Account Details:\n "+account4);
		Scanner sc=new Scanner(System.in);
		
		int choice;
		do {
			System.out.println("******************************");
			System.out.println("\nSelect from Below options: ");
			System.out.println("Press 1 for Money Withdrawal");
			System.out.println("Press 2 for Money Deposit");
			System.out.println("Press 3 for Fund Transfer");
			System.out.println("Press 4 to check Account Status");
			System.out.println("Press 0 for Exit.");
			choice=sc.nextInt();
			switch(choice) {
				case 1:System.out.println("Enter account number from which amount is to be withdrawn: ");
							accountNumber=sc.nextInt();
							System.out.println("Enter pin number: ");
							pinNumber=sc.nextInt();
							System.out.println("Enter amount to be withdrawn:");
							amount=sc.nextInt();
							try{
								bankingService.withdrawAmount(accountNumber, amount, pinNumber);
								System.out.println("Account details after withdrawal: "+bankingService.getAccountDetails(accountNumber));
							}catch(AccountNotFoundException | InvalidPinNumberException | AccountBlockedException | InvalidAmountException
									| InsufficientAmountException e) {
								System.out.println("Error: "+e.getMessage());
							}
							break;
			   case 2:System.out.println("Enter account number from which amount is to be deposited: ");
							accountNumber=sc.nextInt();
							System.out.println("Enter amount to be deposited:");
							amount=sc.nextInt();
							try {
								bankingService.depositAmount(accountNumber, amount);
								System.out.println("Account details after depositing: "+bankingService.getAccountDetails(accountNumber));
							} catch (AccountNotFoundException | BankingServicesDownException | AccountBlockedException e) {
								System.out.println("Error: "+e.getMessage());
							}
							break;
			   case 3:System.out.println("Enter the account number from which money will be transferrred: ");
							int accountNoFrom=sc.nextInt();
							System.out.println("Enter the Pin Number: ");
							pinNumber=sc.nextInt();
							System.out.println("Enter the account number where money will be transferred: ");
							int accountNoTo=sc.nextInt();
							System.out.println("Enter amount: ");
							float transferAmount=sc.nextInt();
							boolean b = false;
							try {
								b = bankingService.fundTransfer(accountNoTo, accountNoFrom, transferAmount, pinNumber);
							} catch (InsufficientAmountException | AccountNotFoundException | InvalidPinNumberException |
						BankingServicesDownException | AccountBlockedException e) {
								System.out.println("Error: "+e.getMessage());
							}
							if(b) {
								System.out.println("Transaction Successful.");
							}break;
			   case 4:System.out.println("Enter the Account Number: ");
						   accountNumber=sc.nextInt();
				try {
					System.out.println("Account Details: "+bankingService.getAccountDetails(accountNumber));
				} catch (AccountNotFoundException | BankingServicesDownException e) {
					System.out.println("Error: "+e.getMessage());
				}break;
			   case 0:System.out.println("Thank You! Please come again.");
			   				System.exit(0);
			   default:System.out.println("Invalide Choice. Try again.");break;
			   
				}
		}while(choice!=0);
	}
}