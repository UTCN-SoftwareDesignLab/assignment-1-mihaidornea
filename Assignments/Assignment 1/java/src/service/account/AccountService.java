package service.account;

import model.Account;
import model.User;
import repository.EntityNotFoundException;

import java.util.List;

public interface AccountService {

    List<Account> findAll();

    Account findById(Long id) throws EntityNotFoundException;

    boolean save(Account account);

    boolean transferMoney(Long accountId1, Long accountId2, Long moneyAmount) throws EntityNotFoundException;

    boolean payUtilityBill(long moneyAmount, Account account);


}
