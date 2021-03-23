package com.example.dnfapi.function;

import com.google.gson.annotations.SerializedName;

public class ItemSirocoInfoOptionsDetail {
    @SerializedName("explain")
    private String explain;
    @SerializedName("explainDetail")
    private String explainDetail;
    @SerializedName("buffExplain")
    private String buffExplain;
    @SerializedName("buffExplainDetail")
    private String buffExplainDetail;

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getExplainDetail() {
        return explainDetail;
    }

    public void setExplainDetail(String explainDetail) {
        this.explainDetail = explainDetail;
    }

    public String getBuffExplain() {
        return buffExplain;
    }

    public void setBuffExplain(String buffExplain) {
        this.buffExplain = buffExplain;
    }

    public String getBuffExplainDetail() {
        return buffExplainDetail;
    }

    public void setBuffExplainDetail(String buffExplainDetail) {
        this.buffExplainDetail = buffExplainDetail;
    }
}
