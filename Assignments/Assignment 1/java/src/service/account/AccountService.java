package service.account;

import model.Account;
import model.User;
import model.validation.Notification;
import repository.EntityNotFoundException;

import java.util.List;

public interface AccountService {

    List<Account> findAll();

    Notification<Account> findById(Long id) throws EntityNotFoundException;

    Notification<Boolean> save(Account account);

    Notification<Boolean> transferMoney(Long accountId1, Long accountId2, Long moneyAmount) throws EntityNotFoundException;

    void removeAccount(Account account);

    Notification<Boolean> update (Account account);

    Notification<Account> findByIdentificationNumber(Long identificationNumber) throws EntityNotFoundException;

    boolean payUtilityBill(long moneyAmount, Account account2);


}
