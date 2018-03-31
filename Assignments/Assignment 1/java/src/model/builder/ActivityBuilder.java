package model.builder;

import model.Activity;

import java.sql.Date;

public class ActivityBuilder {

    private Activity activity;

    public ActivityBuilder(){
        activity = new Activity();
    }

    public ActivityBuilder setUserId(Long userId){
        activity.setUserId(userId);
        return this;
    }

    public ActivityBuilder setAction(String action){
        activity.setAction(action);
        return this;
    }

    public ActivityBuilder setDate(Date date){
        activity.setDateOfExecution(date);
        return this;
    }

    public Activity build(){
        return activity;
    }

}
