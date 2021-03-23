package com.example.dnfapi.function.VOS;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class ItemInfo {
    @SerializedName("itemId")
    private String itemId;
    @SerializedName("itemName")
    private String itemName;

    @SerializedName("averagePrice")
    private int averagePrice;
    @SerializedName("unitPrice")
    private int unitPrice;


    @SerializedName("itemRarity")
    private String itemRarity;
    @SerializedName("itemTypeDetail")
    private String itemTypeDetail;
    @SerializedName("itemExplain")
    private String itemExplain;
    @SerializedName("itemExplainDetail")
    private String itemExplainDetail;
    @SerializedName("itemFlavorText")
    private String itemFlavorText;
    @SerializedName("setItemId")
    private String setItemId;

    public String getSetItemId() {
        return setItemId;
    }

    public void setSetItemId(String setItemId) {
        this.setItemId = setItemId;
    }

    @SerializedName("setItemName")
    private String setItemName;



    //카드일 경우
    @SerializedName("cardInfo")
    public Object cardInfo;

    public Object getCardInfo() {
        return cardInfo.toString();
    }

    public String getItemRarity() {
        return itemRarity;
    }

    public void setItemRarity(String itemRarity) {
        this.itemRarity = itemRarity;
    }

    public String getItemTypeDetail() {
        return itemTypeDetail;
    }

    public void setItemTypeDetail(String itemTypeDetail) {
        this.itemTypeDetail = itemTypeDetail;
    }

    public String getItemExplainDetail() {
        return itemExplainDetail;
    }

    public void setItemExplainDetail(String itemExplainDetail) {
        this.itemExplainDetail = itemExplainDetail;
    }

    public String getItemFlavorText() {
        return itemFlavorText;
    }

    public void setItemFlavorText(String itemFlavorText) {
        this.itemFlavorText = itemFlavorText;
    }

    public String getSetItemName() {
        return setItemName;
    }

    public void setSetItemName(String setItemName) {
        this.setItemName = setItemName;
    }

    public String getItemExplain() {
        return itemExplain;
    }

    public void setItemExplain(String itemExplain) {
        this.itemExplain = itemExplain;
    }



    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(int averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getItemId() {
        Log.d("wfwfwfw", itemId);
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
