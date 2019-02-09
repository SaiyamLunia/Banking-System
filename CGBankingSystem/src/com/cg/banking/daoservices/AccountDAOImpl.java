package com.cg.banking.daoservices;
import java.util.ArrayList;
import java.util.List;
import com.cg.banking.beans.Account;
import com.cg.banking.util.BankingDBUtil;
public class AccountDAOImpl implements AccountDAO {
	@Override
	public Account save(Account account) {
		account.setAccountNo(BankingDBUtil.getACCOUNT_NUMBER());
		account.setPinNumber(BankingDBUtil.getPIN_NUMBER());
		BankingDBUtil.customer.put(account.getAccountNo(), account);
		return account;
	}
	@Override
	public boolean update(Account account) {
		if(BankingDBUtil.customer.containsKey(account.getAccountNo())) {
			BankingDBUtil.customer.put(account.getAccountNo(), account);
			return true;
		}
		return false;
	}
	@Override
	public Account findOne(int accountNo) {
		return BankingDBUtil.customer.get(accountNo);
	}
	@Override
	public List<Account> findAll() {
		return new ArrayList<>(BankingDBUtil.customer.values());
	}
}
