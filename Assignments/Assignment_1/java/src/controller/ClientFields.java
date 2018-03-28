package controller;

public class ClientFields {
    private String name;
    private Long identificationNumber;
    private Long personalCode;
    private String address;
    private Long moneyToPay;

    public Long getIdentificationNumber() {
        return identificationNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public Long getPersonalCode() {
        return personalCode;
    }

    public void setIdentificationNumber(Long identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPersonalCode(Long personalCode) {
        this.personalCode = personalCode;
    }

    public void setMoneyToPay(Long moneyToPay) {
        this.moneyToPay = moneyToPay;
    }

    public Long getMoneyToPay() {
        return moneyToPay;
    }
}