package model.validation;

import model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountValidator {

    private static final int MIN_IDENTIFICATION_NUMBER_LENGTH = 100000;
    private static final int MIN_AMOUNT_OF_MONEY = 0;

    private final Account account;
    private final List<String> errors;

    public AccountValidator(Account account){
        this.account = account;
        errors = new ArrayList<>();
    }

    public List<String> getErrors(){
        return errors;
    }

    public boolean validate(){
        validateAmountOfMoney(account.getAmountOfMoney());
        validateIdentificationNumber(account.getIdentificationNumber());
        return errors.isEmpty();
    }

    private void validateIdentificationNumber(Long identificationNumber){
        if (account.getIdentificationNumber() < MIN_IDENTIFICATION_NUMBER_LENGTH){
            errors.add("Identification number too short!");
        }
    }

    private void validateAmountOfMoney(Long ammountOfMoney){
        if (account.getAmountOfMoney() < MIN_AMOUNT_OF_MONEY){
            errors.add("Ammount of money must be positive!");
        }
    }

}
