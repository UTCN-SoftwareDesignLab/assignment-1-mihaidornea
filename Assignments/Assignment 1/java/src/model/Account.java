package model;

import java.util.Date;

public class Account {

    private Long id;
    private Long identificationNumber;
    private String type;
    private Long amountOfMoney;
    private Date dateOfCreation;
    private Long client_id;

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmountOfMoney(Long acountOfMoney) {
        this.amountOfMoney = acountOfMoney;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public void setIdentificationNumber(Long identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setClient_id(Long client_id) {
        this.client_id = client_id;
    }

    public Long getId() {
        return id;
    }

    public Long getIdentificationNumber() {
        return identificationNumber;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public Long getAmountOfMoney() {
        return amountOfMoney;
    }

    public String getType() {
        return type;
    }

    public Long getClient_id() {
        return client_id;
    }
}
