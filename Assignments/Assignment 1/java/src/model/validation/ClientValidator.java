package model.validation;

import model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientValidator {

    private static final int IDENTITY_NUMBER_LENGTH = 1000;
    private static final int PERSONAL_CODE_NUMBER_LENGTH = 1000;

    private final Client client;
    private final List<String> errors;

    public List<String> getErrors() { return errors; }

    public ClientValidator(Client client){
        this.client = client;
        errors = new ArrayList<>();
    }

    public boolean validate() {
        validateIdentityNumber();
        validateName();
        validatePersonalCode();
        return errors.isEmpty();
    }

    public void validateIdentityNumber(){
        if (client.getIdentityNumber() < IDENTITY_NUMBER_LENGTH){
            errors.add("Identification number Invalid");
        }
    }

    public void validatePersonalCode(){
        if (client.getPersonalNumericalCode() < PERSONAL_CODE_NUMBER_LENGTH){
            errors.add("Personal code number invalid");
        }
    }

    public void validateName(){
        if (containsDigit(client.getName()) || containsSpecialCharacter(client.getName()))
            errors.add("Invalid Name!");
    }


    private boolean containsSpecialCharacter(String s) {
        if (s == null || s.trim().isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile("^A-Za-z0-9");
        Matcher m = p.matcher(s);
        return m.find();
    }

    private boolean containsDigit(String s){
        if (s != null && !s.isEmpty()){
            for (char c : s.toCharArray()){
                if(Character.isDigit(c)){
                    return true;
                }
            }
        }
        return false;
    }
}
