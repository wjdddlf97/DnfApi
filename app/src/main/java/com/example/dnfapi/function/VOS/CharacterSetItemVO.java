package com.example.dnfapi.function.VOS;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CharacterSetItemVO {
    @SerializedName("itemId")
    private String itemId;
    @SerializedName("itemName")
    private String itemName;
    @SerializedName("slotName")
    private String slotName;
    @SerializedName("itemRarity")
    private String itemRarity;
    @SerializedName("setItemName")
    private String setItemName;
    @SerializedName("reinforce")
    private int reinforce;
    @SerializedName("itemGradeName")
    private String itemGradeName;
    @SerializedName("enchant")
    private Object enchant;
    @SerializedName("amplificationName")
    private String amplificationName;
    @SerializedName("refine")
    private int refine;
    @SerializedName("sirocoInfo")
    private Object sirocoInfo;
    @SerializedName("upgradeInfo")
    private Object upgradeInfo;

    public Object getEnchant() {
        return enchant;
    }

    public void setEnchant(Object enchant) {
        this.enchant = enchant;
    }

    public Object getSirocoInfo() {
        return sirocoInfo;
    }

    public void setSirocoInfo(Object sirocoInfo) {
        this.sirocoInfo = sirocoInfo;
    }

    public Object getUpgradeInfo() {
        return upgradeInfo;
    }

    public void setUpgradeInfo(Object upgradeInfo) {
        this.upgradeInfo = upgradeInfo;
    }

    public Object getMythologyInfo() {
        return mythologyInfo;
    }

    public void setMythologyInfo(Object mythologyInfo) {
        this.mythologyInfo = mythologyInfo;
    }

    @SerializedName("mythologyInfo")
    private Object mythologyInfo;


    public String getItemId() {
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

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public String getItemRarity() {
        return itemRarity;
    }

    public void setItemRarity(String itemRarity) {
        this.itemRarity = itemRarity;
    }

    public String getSetItemName() {
        return setItemName;
    }

    public void setSetItemName(String setItemName) {
        this.setItemName = setItemName;
    }

    public int getReinforce() {
        return reinforce;
    }

    public void setReinforce(int reinforce) {
        this.reinforce = reinforce;
    }

    public String getItemGradeName() {
        return itemGradeName;
    }

    public void setItemGradeName(String itemGradeName) {
        this.itemGradeName = itemGradeName;
    }

    public String getAmplificationName() {
        return amplificationName;
    }

    public void setAmplificationName(String amplificationName) {
        this.amplificationName = amplificationName;
    }

    public int getRefine() {
        return refine;
    }

    public void setRefine(int refine) {
        this.refine = refine;
    }
}
