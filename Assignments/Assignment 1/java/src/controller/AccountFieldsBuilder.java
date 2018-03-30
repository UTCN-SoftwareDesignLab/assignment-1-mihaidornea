package controller;

public class AccountFieldsBuilder {

    private AccountFields accountFields;

    public AccountFieldsBuilder() {

        this.accountFields = new AccountFields();

    }

    public AccountFieldsBuilder setClientId(Long clientId){
        accountFields.setClientId(clientId);
        return this;
    }

    public AccountFieldsBuilder setIdentificationNumber (Long identificationNumber){
        accountFields.setIdentificationNumber(identificationNumber);
        return this;
    }

    public AccountFieldsBuilder setSecondIdentificationNumber (Long identificationNumber){
        accountFields.setSecondIdentificationNumber(identificationNumber);
        return this;
    }

    public AccountFieldsBuilder setType (String type){
        accountFields.setType(type);
        return this;
    }

    public AccountFieldsBuilder setMoney(Long money){
        accountFields.setMoney(money);
        return this;
    }

    public AccountFields build(){
        return accountFields;
    }
}
