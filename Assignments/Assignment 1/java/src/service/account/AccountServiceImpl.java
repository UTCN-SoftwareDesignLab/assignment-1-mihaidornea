package service.account;

import model.Account;
import model.User;
import model.builder.AccountBuilder;
import model.validation.AccountValidator;
import model.validation.Notification;
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
    public Notification<Account> findById(Long id) throws EntityNotFoundException {
        return repository.findById(id);
    }

    @Override
    public Notification<Boolean> update(Account account) {
        Notification<Boolean> notification = new Notification<>();
        AccountValidator accountValidator = new AccountValidator(account);
        boolean goodAccount = accountValidator.validate();
        if (!goodAccount){
            accountValidator.getErrors().forEach(notification::addError);
            notification.setResult(Boolean.FALSE);
        } else {
            notification.setResult(repository.update(account));
        }
       return notification;

    }

    @Override
    public Notification<Boolean> save(Account account) {
        AccountValidator accountValidator = new AccountValidator(account);
        boolean goodAccount = accountValidator.validate();
        Notification<Boolean> notification = new Notification<>();
        if (!goodAccount){
            accountValidator.getErrors().forEach(notification::addError);
            notification.setResult(Boolean.FALSE);
        } else {
            notification.setResult(repository.save(account));
        }
        return notification;
    }

    @Override
    public void removeAccount(Account account) {
        repository.removeAccount(account);
    }

    @Override
    public Notification<Boolean> transferMoney(Long accountId1, Long accountId2, Long moneyAmount) throws EntityNotFoundException {
        Notification<Account> notification1 = new Notification<>();
        Notification<Account> notification2 = new Notification<>();
        Notification<Boolean> returnNotification = new Notification<>();

        notification1 = repository.findByIdentificationNumber(accountId1);
        notification2 = repository.findByIdentificationNumber(accountId2);

        Account account1;
        Account account2;

        if (!notification1.hasErrors())
            account1 = repository.findByIdentificationNumber(accountId1).getResult();
        else {
            returnNotification.setResult(Boolean.FALSE);
            returnNotification.addError("First ID is Invalid");
            return returnNotification;
        }

        if (!notification2.hasErrors())
            account2 = repository.findByIdentificationNumber(accountId2).getResult();
        else {
            returnNotification.setResult(Boolean.FALSE);
            returnNotification.addError("Second ID is Invalid");
            return returnNotification;
        }

        account1.setAmountOfMoney(account1.getAmountOfMoney()-moneyAmount);
        account2.setAmountOfMoney(account2.getAmountOfMoney()+moneyAmount);

        if(repository.update(account1)&&repository.update(account2)){
            returnNotification.setResult(Boolean.TRUE);
            return returnNotification;
        }
        else {
            returnNotification.setResult(Boolean.FALSE);
            returnNotification.addError("Failed to update the accounts");
            return returnNotification;
        }
    }

    @Override
    public Notification<Account> findByIdentificationNumber(Long identificationNumber) throws EntityNotFoundException{
        return repository.findByIdentificationNumber(identificationNumber);
    }

    @Override
    public boolean payUtilityBill(long moneyAmount, Account account) {
        account.setAmountOfMoney(account.getAmountOfMoney()-moneyAmount);
        return repository.update(account);
    }
}
