package com.example.dnfapi.function.VOS;

public class BoardListView {
    public String boardName;
    public String boardWriter;
    public String writeDate;
    public String boardWriterId;
    public String boardType;

    public String getBoardWriterId() {
        return boardWriterId;
    }

    public void setBoardWriterId(String boardWriterId) {
        this.boardWriterId = boardWriterId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardWriter() {
        return boardWriter;
    }

    public void setBoardWriter(String boardWriter) {
        this.boardWriter = boardWriter;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }
}
