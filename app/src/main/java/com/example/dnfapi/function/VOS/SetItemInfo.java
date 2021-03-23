package com.example.dnfapi.function.VOS;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SetItemInfo {
    @SerializedName("setItemId")
    private String setItemId;
    @SerializedName("setItemName")
    private String setItemName;

    @SerializedName("setItems")
    public List<CharacterSetItemVO> setItems;
}
