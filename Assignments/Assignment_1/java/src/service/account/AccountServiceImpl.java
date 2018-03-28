package service.account;

import model.Account;
import model.User;
import repository.EntityNotFoundException;
import repository.account.AccountRepository;

import java.util.List;

public class AccountServiceImpl implements AccountService{

    private final AccountRepository repository;

    public AccountServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Account> findAll() {
        return repository.findAll();
    }

    @Override
    public Account findById(Long id) throws EntityNotFoundException {
        return repository.findById(id);
    }

    @Override
    public boolean save(Account account) {
        return repository.save(account);
    }

    @Override
    public boolean transferMoney(Long accountId1, Long accountId2, Long moneyAmount) throws EntityNotFoundException {
        Account account1 = repository.findById(accountId1);
        Account account2 = repository.findById(accountId2);

        account1.setAmountOfMoney(account1.getAmountOfMoney()-moneyAmount);
        account2.setAmountOfMoney(account2.getAmountOfMoney()+moneyAmount);

        return repository.update(account1)&&repository.update(account2);
    }

    @Override
    public boolean payUtilityBill(long moneyAmount, Account account) {
        account.setAmountOfMoney(account.getAmountOfMoney()-moneyAmount);
        return repository.update(account);
    }
}
