package com.example.dnfapi.function.VOS;

import com.google.gson.annotations.SerializedName;

public class CharacterDetailListVO {

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
    @SerializedName("adventureName")
    private String adventureName;
    @SerializedName("guildId")
    private String guildId;
    @SerializedName("guildName")
    private String guildName;

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

    public String getAdventureName() {
        return adventureName;
    }

    public void setAdventureName(String adventureName) {
        this.adventureName = adventureName;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }
}
