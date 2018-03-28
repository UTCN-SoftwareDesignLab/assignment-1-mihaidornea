package model;

public class Client {

    private Long id;
    private String name;
    private Long identityNumber;
    private Long personalNumericalCode;
    private String address;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Long getIdentityNumber() {
        return identityNumber;
    }

    public Long getPersonalNumericalCode() {
        return personalNumericalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdentityNumber(Long identityNumber) {
        this.identityNumber = identityNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPersonalNumericalCode(Long personalNumericalCode) {
        this.personalNumericalCode = personalNumericalCode;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
