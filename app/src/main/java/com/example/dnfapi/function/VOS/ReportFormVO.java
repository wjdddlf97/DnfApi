package com.example.dnfapi.function.VOS;

public class ReportFormVO {
    public String reportKinds;
    public String reportTitle;
    public String reportContent;
    public String reportType;
    public String reportedMemberId;
    public String reportMemberId;

    public ReportFormVO(String reportKinds, String reportTitle, String reportContent, String reportType, String reportedMemberId, String reportMemberId) {
        this.reportKinds = reportKinds;
        this.reportTitle = reportTitle;
        this.reportContent = reportContent;
        this.reportType = reportType;
        this.reportedMemberId = reportedMemberId;
        this.reportMemberId = reportMemberId;
    }

    public String getReportKinds() {
        return reportKinds;
    }

    public void setReportKinds(String reportKinds) {
        this.reportKinds = reportKinds;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportedMemberId() {
        return reportedMemberId;
    }

    public void setReportedMemberId(String reportedMemberId) {
        this.reportedMemberId = reportedMemberId;
    }

    public String getReportMemberId() {
        return reportMemberId;
    }

    public void setReportMemberId(String reportMemberId) {
        this.reportMemberId = reportMemberId;
    }
}
