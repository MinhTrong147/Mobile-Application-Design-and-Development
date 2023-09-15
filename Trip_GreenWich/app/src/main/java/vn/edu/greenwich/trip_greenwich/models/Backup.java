package vn.edu.greenwich.trip_greenwich.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Backup implements Serializable {
    protected Date _date;
    protected String _deviceName;
    protected ArrayList<TripControl> _tripControls;
    protected ArrayList<Cost> _expens;
    protected String _userId;

    public Backup(Date date, String deviceName, String userId, ArrayList<TripControl> tripControls, ArrayList<Cost> expens) {
        _date = date;
        _deviceName = deviceName;
        _tripControls = tripControls;
        _expens = expens;
        _userId = userId;
    }

    public void setDate(Date date) {
        _date = date;
    }

    public Date getDate() {
        return _date;
    }

    public void setDeviceName(String deviceName) {
        _deviceName = deviceName;
    }

    public String getDeviceName() {
        return _deviceName;
    }

    public void setUserId(String userId) {_userId = userId;}

    public String getUserId() {
        return _userId;
    }

    public void setResidents(ArrayList<TripControl> tripControls) {
        _tripControls = tripControls;
    }

    public ArrayList<TripControl> getResidents() {
        return _tripControls;
    }

    public void setRequests(ArrayList<Cost> expens) {
        _expens = expens;
    }

    public ArrayList<Cost> getRequests() {
        return _expens;
    }
}