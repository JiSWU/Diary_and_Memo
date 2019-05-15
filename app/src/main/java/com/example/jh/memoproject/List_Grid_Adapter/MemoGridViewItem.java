package com.example.jh.memoproject.List_Grid_Adapter;

public class MemoGridViewItem {

    private int seq;
    private String date ;
    private String title ;
    private String memo;
    private String time;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time= time;
    }
}
