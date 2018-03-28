package controller;

public class ClientFieldsBuilder{

    ClientFields clientFields;

    public ClientFieldsBuilder(){
        clientFields = new ClientFields();
    }

    public ClientFieldsBuilder setName(String name){
        clientFields.setName(name);
        return this;
    }

    public ClientFieldsBuilder setAddress(String address){
        clientFields.setName(address);
        return this;
    }

    public ClientFieldsBuilder setIdentificationNumber(Long identificationNumber){
        clientFields.setIdentificationNumber(identificationNumber);
        return this;
    }

    public ClientFieldsBuilder setPersonalCodeNumber(Long personalCodeNumber){
        clientFields.setPersonalCode(personalCodeNumber);
        return this;
    }

    public ClientFieldsBuilder setMoneyToPay(Long money){
        clientFields.setMoneyToPay(money);
        return this;
    }

    public ClientFields build(){
        return clientFields;
    }

}

