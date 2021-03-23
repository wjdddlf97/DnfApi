package com.example.dnfapi.function.VOS;

import com.example.dnfapi.function.VOS.CharacterListStatVO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiStatForm {

    @SerializedName("characterName")
    private String characterName;
    @SerializedName("level")
    private int level;
    @SerializedName("jobGrowName")
    private String jobGrowName;
    @SerializedName("status")
    public List<CharacterListStatVO> status;

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getJobGrowName() {
        return jobGrowName;
    }

    public void setJobGrowName(String jobGrowName) {
        this.jobGrowName = jobGrowName;
    }


}
