package com.example.bca;

import java.util.Map;

public class FairShareData {
    private String userIDfk;
    private Integer FSID;
    private String date;
    private String time;
    private Map<String, Float> controInfo;
    private Integer totalContributors;
    private Float totalExpense;
    private Float perHead;

    public Integer getFSID() {
        return FSID;
    }

    public void setFSID(Integer FSID) {
        this.FSID = FSID;
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

    public Map<String, Float> getControInfo() {
        return controInfo;
    }

    public void setControInfo(Map<String, Float> controInfo) {
        this.controInfo = controInfo;
    }

    public Integer getTotalContributors() {
        return totalContributors;
    }

    public void setTotalContributors(Integer totalContributors) {
        this.totalContributors = totalContributors;
    }

    public Float getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Float totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Float getPerHead() {
        return perHead;
    }

    public void setPerHead(Float perHead) {
        this.perHead = perHead;
    }
}