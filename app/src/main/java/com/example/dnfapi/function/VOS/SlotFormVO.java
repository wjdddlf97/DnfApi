package com.example.dnfapi.function.VOS;

import com.google.gson.annotations.SerializedName;

public class SlotFormVO {
    @SerializedName("slotId")
    private String slotId;
    @SerializedName("slotName")
    private String slotName;

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }
}
