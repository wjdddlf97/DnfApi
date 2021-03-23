package com.example.dnfapi.function.VOS;

public class BoardFormVO {

    public String title;
    public String boardContent;
    public String writeDate;
    public String writer;
    public String writerId;

    public BoardFormVO(String title, String boardContent, String writeDate, String writer, String writerId) {
        this.title = title;
        this.boardContent = boardContent;
        this.writeDate = writeDate;
        this.writer = writer;
        this.writerId = writerId;
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
}
