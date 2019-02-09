package com.cg.banking.util;
import java.util.HashMap;
import com.cg.banking.beans.Account;
public class BankingDBUtil {
	public static HashMap<Integer,Account> customer=new HashMap<>();
	public static int ACCOUNT_NUMBER=5000;
	public static int TRANSACTION_NUMBER=100;
	public static int getACCOUNT_NUMBER() {
		return ++ACCOUNT_NUMBER;
	}
	public static int getTRANSACTION_NUMBER() {
		return ++TRANSACTION_NUMBER;
	}
	public static int getPIN_NUMBER() {
		return (int) (Math.random()*10000);
	}
}