package com.example.bca;

import java.util.Map;

public class DailyExpenseData {
    private Integer DailyExpenseID;
    private String userIDfk;
    private String date;
    private String time;
    private Map<String, Float> despent;
    private Float totalExpense;
    private Integer numberOfExpense;

    public String getUserIDfk() {
        return userIDfk;
    }

    public void setUserIDfk(String userIDfk) {
        this.userIDfk = userIDfk;
    }

    public Integer getDailyExpenseID() {
        return DailyExpenseID;
    }

    public void setDailyExpenseID(Integer dailyExpenseID) {
        DailyExpenseID = dailyExpenseID;
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

    public Map<String, Float> getDespent() {
        return despent;
    }

    public void setDespent(Map<String, Float> despent) {
        this.despent = despent;
    }

    public Float getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Float totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Integer getNumberOfExpense() {
        return numberOfExpense;
    }

    public void setNumberOfExpense(Integer numberOfExpense) {
        this.numberOfExpense = numberOfExpense;
    }
}