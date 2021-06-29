package com.example.dnfapi.function.VOS;

public class BoardFormVO {

    public String title;
    public String boardContent;
    public String writeDate;
    public String writer;
    public String writerId;
    public String type;
    public String classType;

    public BoardFormVO(String title, String boardContent, String writeDate, String writer, String writerId,String type) {
        this.title = title;
        this.boardContent = boardContent;
        this.writeDate = writeDate;
        this.writer = writer;
        this.writerId = writerId;
        this.type = type;
        this.classType = "";
    }

    public BoardFormVO(String title, String boardContent, String writeDate, String writer, String writerId,String type, String classType) {
        this.title = title;
        this.boardContent = boardContent;
        this.writeDate = writeDate;
        this.writer = writer;
        this.writerId = writerId;
        this.type = type;
        this.classType = classType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
