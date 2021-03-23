package com.example.dnfapi.function.VOS;

import com.google.gson.annotations.SerializedName;

public class CharacterListVO {
    @SerializedName("serverId")
    private String serverId;
    @SerializedName("characterId")
    private String characterId;
    @SerializedName("characterName")
    private String characterName;
    @SerializedName("level")
    private int level;
    @SerializedName("jobId")
    private String jobId;
    @SerializedName("jobGrowId")
    private String jobGrowId;
    @SerializedName("jobName")
    private String jobName;
    @SerializedName("jobGrowName")
    private String jobGrowName;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

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

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobGrowId() {
        return jobGrowId;
    }

    public void setJobGrowId(String jobGrowId) {
        this.jobGrowId = jobGrowId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGrowName() {
        return jobGrowName;
    }

    public void setJobGrowName(String jobGrowName) {
        this.jobGrowName = jobGrowName;
    }

    public CharacterListVO(){}

    public CharacterListVO(String serverId, String characterId, String characterName, int level, String jobId, String jobGrowId, String jobName, String jobGrowName) {
        this.serverId = serverId;
        this.characterId = characterId;
        this.characterName = characterName;
        this.level = level;
        this.jobId = jobId;
        this.jobGrowId = jobGrowId;
        this.jobName = jobName;
        this.jobGrowName = jobGrowName;
    }
}
