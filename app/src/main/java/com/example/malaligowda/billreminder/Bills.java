package com.example.malaligowda.billreminder;

public class Bills {
    private int _id;
    private String _name;
    private String _day;
    private String amt;
    private String _type;
    private String _currency;
    private String _interval;
    private String _notify;
    private String _notifyDays;


    public Bills(String name) {
        this._name = name;
    }

    public String get_name() {
        return _name;
    }

//
    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_notify() {
        return _notify;
    }

    public void set_notify(String _notify) {
        this._notify = _notify;
    }

    public String get_currency() {
        return _currency;
    }

    public String get_interval() {
        return _interval;
    }

    public void set_interval(String _interval) {
        this._interval = _interval;
    }

    public String get_notifyDays() {
        return _notifyDays;
    }

    public void set_notifyDays(String _notifyDays) {
        this._notifyDays = _notifyDays;
    }

    public void set_currency(String _currency) {
        this._currency = _currency;
    }



    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_day(String _day) {

        this._day = _day;
    }

    public String get_day() {

        return _day;
    }

    public String getAmt() {
        return amt;
    }
}
