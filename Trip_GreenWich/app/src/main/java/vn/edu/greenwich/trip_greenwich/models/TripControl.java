package vn.edu.greenwich.trip_greenwich.models;

import java.io.Serializable;

public class TripControl implements Serializable {
    protected long _id;
    protected String _name;
    protected String _current_location;
    protected String _destination;
    protected String _description;
    protected String _startDate;
    protected int _riskTrip;
    protected long _current_amount;

    public TripControl() {
        _id = -1;
        _name = null;
        _current_location = null;
        _destination = null;
        _description = null;
        _startDate = null;
        _riskTrip = -1;
        _current_amount = 0;
    }


    public TripControl(long id, String name, String current_location, String destination, String description, String startDate, int riskTrip, long current_amount) {
        _id = id;
        _name = name;
        _current_location = current_location;
        _destination = destination;
        _description = description;
        _startDate = startDate;
        _riskTrip = riskTrip;
        _current_amount = current_amount;
    }


    public long getId() { return _id; }
    public void setId(long id) {
        _id = id;
    }

    public String getName() { return _name;}
    public void setName(String name) { _name = name;}

    public String getCurrentLocation() { return _current_location;}
    public void setCurrentLocation(String _current_location) { this._current_location = _current_location;}

    public String getDestination() { return _destination;}
    public void setDestination(String destination) { _destination = destination;}

    public String getDescription() { return _description; }
    public void setDescription(String description) { _description = description; }

    public long getCurrentAmount() {
        return _current_amount;
    }
    public void setCurrentAmount(long _current_amount) {
        this._current_amount = _current_amount;
    }

    public String getStartDate() {
        return _startDate;
    }
    public void setStartDate(String startDate) {
        _startDate = startDate;
    }

    public int getRiskTrip() {
        return _riskTrip;
    }
    public void setRiskTrip(int riskTrip) {
        _riskTrip = riskTrip;
    }

    public boolean isEmpty() {
        if (-1 == _id && null == _name && null == _current_location && null == _destination && null == _description && null == _startDate && -1 == _riskTrip)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "[" + _startDate + "] " + _name + _current_location + _destination + _description;
    }

}