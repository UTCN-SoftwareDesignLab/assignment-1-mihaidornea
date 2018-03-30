package controller;

import model.Role;

public class UserFieldsBuilder {

    private UserFields userFields;

    public UserFieldsBuilder(){
        this.userFields = new UserFields();
    }

    public UserFieldsBuilder setUsername(String username){
        userFields.setUsername(username);
        return this;
    }

    public UserFieldsBuilder setPassword(String password){
        userFields.setPassword(password);
        return this;
    }

    public UserFieldsBuilder setRole(Role role){
        userFields.setRole(role);
        return this;
    }

    public UserFieldsBuilder setId(Long id){
        userFields.setId(id);
        return this;
    }

    public UserFields build(){
        return this.userFields;
    }
}
