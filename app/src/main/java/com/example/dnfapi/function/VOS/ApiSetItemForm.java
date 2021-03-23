package com.example.dnfapi.function.VOS;

import com.example.dnfapi.function.VOS.CharacterSetItemVO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiSetItemForm {
    //무기 , 칭호 , 상의 , 머리어깨 , 하의 , 신발 , 허리 , 목걸이 , 팔찌 , 반지
    //보조장비 , 마법석 , 귀걸이


    @SerializedName("equipment")
    public List<CharacterSetItemVO> equipment;
}
