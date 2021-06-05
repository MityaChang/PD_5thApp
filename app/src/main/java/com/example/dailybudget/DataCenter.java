package com.example.dailybudget;

public class DataCenter {

    String title;
    String description;
    String budget;
    String id;
    String date;


    public DataCenter(){}
    public DataCenter(String title, String description, String budget, String id, String date) {
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.id = id;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
