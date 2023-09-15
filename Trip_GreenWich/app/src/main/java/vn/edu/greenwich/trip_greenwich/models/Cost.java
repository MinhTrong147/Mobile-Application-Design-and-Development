package vn.edu.greenwich.trip_greenwich.models;

import java.io.Serializable;

public class Cost implements Serializable {
    protected long _id;
    protected String _content;
    protected long _amount;
    protected String _date;
    protected String _time;
    protected String _type;
    protected long _TripId;

    public Cost() {
        _id = -1;
        _content = null;
        _amount = -1;
        _date = null;
        _time = null;
        _type = null;
        _TripId = -1;
    }

    public Cost(long id, String content, String date, String time, String type, long amount, long TripId) {
        _id = id;
        _content = content;
        _date = date;
        _time = time;
        _type = type;
        _amount = amount;
        _TripId = TripId;
    }

    public long getId() {
        return _id;
    }
    public void setId(long id) {
        _id = id;
    }

    public String getContent() {
        return _content;
    }
    public void setContent(String content) {
        _content = content;
    }

    public String getDate() {
        return _date;
    }
    public void setDate(String date) {
        _date = date;
    }

    public String getTime() {
        return _time;
    }
    public void setTime(String time) {
        _time = time;
    }

    public String getType() {
        return _type;
    }
    public void setType(String type) {
        _type = type;
    }

    public long getAmount() {
        return _amount;
    }

    public void setAmount(long _amount) {
        this._amount = _amount;
    }

    public long getTripId() {
        return _TripId;
    }
    public void setTripId(long TripId) {
        _TripId = TripId;
    }

    public boolean isEmpty() {
        if (-1 == _id && null == _content && -1 == _amount && null == _date && null == _time && null == _type  && -1 == _TripId)
            return true;

        return false;
    }

    @Override
    public String toString() { return "[" + _type + "][" + _date + "] " + _content ;}
}