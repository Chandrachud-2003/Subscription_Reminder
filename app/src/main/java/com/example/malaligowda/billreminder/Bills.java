package com.example.malaligowda.billreminder;

public class Bills {
    private int _id;
    private String _name;
    private int day;
    private int month;
    private int year;
    private int amt;

    public Bills(String name) {
        this._name = name;
    }

    public String get_name() {
        return _name;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public int get_id() {
        return _id;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getAmt() {
        return amt;
    }
}
