package com.example.dnfapi.function.VOS;

public class Report_DirectFormVO {
    public String name;
    public String userId;
    public String date;
    public String isEnd;
    public Report_DirectFormVO() {
    }

    public Report_DirectFormVO(String name, String userId, String date,String isEnd) {
        this.name = name;
        this.userId = userId;
        this.date = date;
        this.isEnd = isEnd;
    }

    public String getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }




}
