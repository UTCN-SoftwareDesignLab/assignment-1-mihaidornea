package model;

import java.util.ArrayList;
import java.util.List;

public class Report {

    private List<Activity> activities;

    public Report(){
        activities = new ArrayList<>();
    }

    public void addActivities(Activity activity){
        activities.add(activity);
    }

    public String toString(){
        String reports = new String();
        for (Activity a : activities){
            reports = reports.concat(a.getAction() + "  " + a.getDateOfExecution().toString() + "\n");
        }
        return reports;
    }

    public List<Activity> getActivities() {
        return activities;
    }
}
