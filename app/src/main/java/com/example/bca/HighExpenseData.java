package com.example.bca;

import java.util.Map;

public class HighExpenseData {
    private Integer HEID;
    private String userIDfk;
    private String date;
    private String time;
    private Map<String, Float> hespent;
    private Integer numberOfExpense;
    private Float totalExpense;

    public Integer getHEID() {
        return HEID;
    }

    public void setHEID(Integer HEID) {
        this.HEID = HEID;
    }

    public String getUserIDfk() {
        return userIDfk;
    }

    public void setUserIDfk(String userIDfk) {
        this.userIDfk = userIDfk;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Map<String, Float> getHespent() {
        return hespent;
    }

    public void setHespent(Map<String, Float> hespent) {
        this.hespent = hespent;
    }

    public Integer getNumberOfExpense() {
        return numberOfExpense;
    }

    public void setNumberOfExpense(Integer numberOfExpense) {
        this.numberOfExpense = numberOfExpense;
    }

    public Float getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Float totalExpense) {
        this.totalExpense = totalExpense;
    }
}