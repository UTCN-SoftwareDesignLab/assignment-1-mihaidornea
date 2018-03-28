package controller;

import java.util.Date;

public class AccountFields {

    private Long clientId;
    private Long identificationNumber;
    private String Type;
    private Long money;
    private Date date;

    public Long getIdentificationNumber() {
        return identificationNumber;
    }

    public String getType() {
        return Type;
    }

    public Date getDate() {
        return date;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getMoney() {
        return money;
    }

    public void setIdentificationNumber(Long identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMoney(Long money) {
        this.money = money;
    }
}