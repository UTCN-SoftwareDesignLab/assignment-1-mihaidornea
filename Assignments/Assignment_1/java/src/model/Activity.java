package model;

import java.util.Date;

public class Activity {

    private Long userId;
    private String action;
    private Date dateOfExecution;

    public void setAction(String action) {
        this.action = action;
    }

    public void setDateOfExecution(Date dateOfExecution) {
        this.dateOfExecution = dateOfExecution;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getDateOfExecution() {
        return dateOfExecution;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }
}
