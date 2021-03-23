package com.example.dnfapi.function.VOS;

public class DailyGradeFormVO {
    public String dailyGrade;
    public String dailyGradePercent;

    public DailyGradeFormVO(String dailyGrade, String dailyGradePercent) {
        this.dailyGrade = dailyGrade;
        this.dailyGradePercent = dailyGradePercent;
    }

    public String getDailyGrade() {
        return dailyGrade;
    }

    public void setDailyGrade(String dailyGrade) {
        this.dailyGrade = dailyGrade;
    }

    public String getDailyGradePercent() {
        return dailyGradePercent;
    }

    public void setDailyGradePercent(String dailyGradePercent) {
        this.dailyGradePercent = dailyGradePercent;
    }
}
