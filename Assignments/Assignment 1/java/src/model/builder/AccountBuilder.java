package model.builder;

import model.Account;

import java.util.Date;

public class AccountBuilder {

    private Account account;

    public AccountBuilder(){
        account = new Account();
    }

    public AccountBuilder setIdentificationNumber(Long identNr){
        account.setIdentificationNumber(identNr);
        return this;
    }

    public AccountBuilder setType(String type){
        account.setType(type);
        return this;
    }

    public AccountBuilder setAmountOfMoney(Long amount){
        account.setAmountOfMoney(amount);
        return this;
    }

    public AccountBuilder setDateOfCreation(Date date){
        account.setDateOfCreation(date);
        return this;
    }

    public AccountBuilder setClientId(Long id){
        account.setClient_id(id);
        return this;
    }

    public AccountBuilder setId(Long id){
        account.setId(id);
        return this;
    }

    public Account build(){
        return account;
    }

}
