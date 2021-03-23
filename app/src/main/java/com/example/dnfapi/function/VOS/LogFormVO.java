package com.example.dnfapi.function.VOS;

public class LogFormVO {
    public String logKinds;
    public String logTitle;

    public String getLogTitle() {
        return logTitle;
    }

    public void setLogTitle(String logTitle) {
        this.logTitle = logTitle;
    }

    public String logContent;
    public String date;

    public LogFormVO(String logKinds, String logTitle, String logContent, String date) {
        this.logKinds = logKinds;
        this.logTitle = logTitle;
        this.logContent = logContent;
        this.date = date;
    }

    public String getLogKinds() {
        return logKinds;
    }

    public void setLogKinds(String logKinds) {
        this.logKinds = logKinds;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
