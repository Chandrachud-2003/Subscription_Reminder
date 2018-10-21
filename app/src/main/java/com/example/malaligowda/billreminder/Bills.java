package com.example.malaligowda.billreminder;

public class Bills {
    private int _id;
    private String _name;
    private String _date;
    private String amt;
    private String _type;
    private String _currency;
    private String _interval;
    private boolean _notify;


    public Bills(String name) {
        this._name = name;
    }

    public String get_name() {
        return _name;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public boolean get_notify() {
        return _notify;
    }

    public void set_notify(boolean _notify) {
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

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String getAmt() {
        return amt;
    }
}
