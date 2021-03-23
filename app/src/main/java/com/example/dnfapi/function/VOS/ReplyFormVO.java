package com.example.dnfapi.function.VOS;

public class ReplyFormVO {

    public String replyContent;
    public String writeDate;
    public String writer;
    public String writerId;



    public ReplyFormVO(){

    }

    public ReplyFormVO(String replyContent, String writeDate, String writer, String writerId) {
        this.replyContent = replyContent;
        this.writeDate = writeDate;
        this.writer = writer;
        this.writerId = writerId;

    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
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
