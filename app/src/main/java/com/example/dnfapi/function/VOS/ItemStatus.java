package com.example.dnfapi.function.VOS;

import com.example.dnfapi.function.VOS.CharacterListStatVO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemStatus {
    @SerializedName("status")
    public List<CharacterListStatVO> status;
}
