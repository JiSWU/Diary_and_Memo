package com.example.jh.memoproject.List_Grid_view;

public class DailyListViewItem {

    private int seq;
    private String memo;
    private String year_month;
    private String day ;
    private String week ;
    private String time;


    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getYear_month(){
        return year_month;
    }

    public void setYear_month(String year_month){
        this.year_month = year_month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String month) {
        this.week = month;
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

