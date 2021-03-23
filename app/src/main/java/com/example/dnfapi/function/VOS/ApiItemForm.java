package com.example.dnfapi.function.VOS;

import com.example.dnfapi.function.VOS.ItemInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiItemForm {
    @SerializedName("rows")
    public List<ItemInfo> rows;
}
