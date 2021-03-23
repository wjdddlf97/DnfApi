package com.example.dnfapi.function.VOS;

import com.example.dnfapi.function.VOS.CharacterListVO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiForm {
    @SerializedName("rows")
    public List<CharacterListVO> rows;

    public String toString(){
        return "" + rows.get(0).getJobGrowName() + "";
    }

    public String getServerId(){ return rows.get(0).getServerId(); }
    public String getCharacterId(){ return rows.get(0).getCharacterId(); }
    public String getCharacterName(){ return rows.get(0).getCharacterName(); }
    public int getLevel(){ return rows.get(0).getLevel(); }
    public String getJobId(){ return rows.get(0).getJobId(); }
    public String getJobGrowId(){ return rows.get(0).getJobGrowId(); }
    public String getJobName(){ return rows.get(0).getJobName(); }
    public String getJobGrowName(){ return rows.get(0).getJobGrowName(); }


}

