package com.example.dnfapi.Chat;

public class ChatData {
    public String msg;
    public String nickName;

    public ChatData(){}

    public ChatData(String msg, String nickName) {
        this.msg = msg;
        this.nickName = nickName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
