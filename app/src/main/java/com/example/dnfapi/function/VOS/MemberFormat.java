package com.example.dnfapi.function.VOS;

public class MemberFormat {
    public String memberName;
    public String subTitle;

    public MemberFormat(String memberName, String subTitle) {
        this.memberName = memberName;
        this.subTitle = subTitle;
    }


    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
