package vn.edu.greenwich.trip_greenwich.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

import vn.edu.greenwich.trip_greenwich.models.Cost;
import vn.edu.greenwich.trip_greenwich.models.TripControl;

public class TripCostDAO {
    protected TripCostDbHelper tripCostDbHelper;
    protected SQLiteDatabase dbWrite, dbRead;

    public TripCostDAO(Context context) {
        tripCostDbHelper = new TripCostDbHelper(context);

        dbRead = tripCostDbHelper.getReadableDatabase();
        dbWrite = tripCostDbHelper.getWritableDatabase();
    }

    public void close() {
        dbRead.close();
        dbWrite.close();
    }

    public void reset() {
        tripCostDbHelper.onUpgrade(dbWrite, 0, 0);
    }

    // TripControl.

    public long insertTripControl(TripControl tripControl) {
        ContentValues values = getTripControlValues(tripControl);

        return dbWrite.insert(TripControlEntry.TABLE_NAME, null, values);
    }

    public ArrayList<TripControl> getTripControlList(TripControl tripControl, String orderByColumn, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;

        if (null != tripControl) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();

            if (tripControl.getName() != null && !tripControl.getName().trim().isEmpty()) {
                selection += " AND " + TripControlEntry.COL_NAME + " LIKE ?";
                conditionList.add("%" + tripControl.getName() + "%");
            }
            if (tripControl.getCurrentLocation() != null && !tripControl.getCurrentLocation().trim().isEmpty()) {
                selection += " AND " + TripControlEntry.COL_CURRENT_LOCATION + " LIKE ?";
                conditionList.add("%" + tripControl.getCurrentLocation() + "%");
            }
            if (tripControl.getDestination() != null && !tripControl.getDestination().trim().isEmpty()) {
                selection += " AND " + TripControlEntry.COL_DESTINATION + " LIKE ?";
                conditionList.add("%" + tripControl.getDestination() + "%");
            }

            if (tripControl.getCurrentAmount() != -1 ) {
                selection += " AND " + TripControlEntry.COL_CURRENT_AMOUNT + " = ?";
                conditionList.add(String.valueOf(tripControl.getCurrentAmount()));
            }

            if (tripControl.getDescription() != null && !tripControl.getDescription().trim().isEmpty()) {
                selection += " AND " + TripControlEntry.COL_DESCRIPTION + " LIKE ?";
                conditionList.add("%" + tripControl.getDescription() + "%");
            }

            if (tripControl.getStartDate() != null && !tripControl.getStartDate().trim().isEmpty()) {
                selection += " AND " + TripControlEntry.COL_START_DATE + " = ?";
                conditionList.add(tripControl.getStartDate());
            }

            if (tripControl.getRiskTrip() != -1) {
                selection += " AND " + TripControlEntry.COL_RISK_TRIP + " = ?";
                conditionList.add(String.valueOf(tripControl.getRiskTrip()));
            }


            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }

            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }

        return getTripControlFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    public TripControl getTripControlById(long id) {
        String selection = TripControlEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getTripControlFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }

    public long updateTripControl(TripControl tripControl) {
        ContentValues values = getTripControlValues(tripControl);

        String selection = TripControlEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(tripControl.getId())};

        return dbWrite.update(TripControlEntry.TABLE_NAME, values, selection, selectionArgs);
    }


    public long deleteTripControl(long id) {
        String selection = TripControlEntry.COL_ID + " = ?";
        String query ="DELETE FROM "+ CostEntry.TABLE_NAME+ " WHERE "+ CostEntry.COL_TRIP_CONTROL_ID+" = "+id;
        String[] selectionArgs = {String.valueOf(id)};

        dbWrite.execSQL(query);

        return dbWrite.delete(TripControlEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected String getOrderByString(String orderByColumn, boolean isDesc) {
        if (orderByColumn == null || orderByColumn.trim().isEmpty())
            return null;

        if (isDesc)
            return orderByColumn.trim() + " DESC";

        return orderByColumn.trim();
    }

    protected ContentValues getTripControlValues(TripControl tripControl) {
        ContentValues values = new ContentValues();

        values.put(TripControlEntry.COL_NAME, tripControl.getName());
        values.put(TripControlEntry.COL_CURRENT_LOCATION, tripControl.getCurrentLocation());
        values.put(TripControlEntry.COL_DESTINATION, tripControl.getDestination());
        values.put(TripControlEntry.COL_CURRENT_AMOUNT, tripControl.getCurrentAmount());
        values.put(TripControlEntry.COL_DESCRIPTION, tripControl.getDescription());
        values.put(TripControlEntry.COL_START_DATE, tripControl.getStartDate());
        values.put(TripControlEntry.COL_RISK_TRIP, tripControl.getRiskTrip());

        return values;
    }

    protected ArrayList<TripControl> getTripControlFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<TripControl> list = new ArrayList<>();

        Cursor cursor = dbRead.query(TripControlEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            TripControl tripControlItem = new TripControl();

            tripControlItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(TripControlEntry.COL_ID)));
            tripControlItem.setName(cursor.getString(cursor.getColumnIndexOrThrow(TripControlEntry.COL_NAME)));
            tripControlItem.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(TripControlEntry.COL_CURRENT_LOCATION)));
            tripControlItem.setCurrentLocation(cursor.getString(cursor.getColumnIndexOrThrow(TripControlEntry.COL_DESTINATION)));
            tripControlItem.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TripControlEntry.COL_DESCRIPTION)));
            tripControlItem.setRiskTrip(cursor.getInt(cursor.getColumnIndexOrThrow(TripControlEntry.COL_RISK_TRIP)));
            tripControlItem.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(TripControlEntry.COL_START_DATE)));
            tripControlItem.setCurrentAmount(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(TripControlEntry.COL_CURRENT_AMOUNT))) );

            list.add(tripControlItem);
        }

        cursor.close();

        return list;
    }

    // Cost.

    public long insertExpenses(Cost cost) {
        ContentValues values = getExpensesValues(cost);

        return dbWrite.insert(CostEntry.TABLE_NAME, null, values);
    }
    public void updateDb(String s){

        dbWrite.execSQL(s);
    }
    public ArrayList<Cost> getCostList(Cost cost, String orderByColumn, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;

        if (cost != null) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();

            if (cost.getContent() != null && !cost.getContent().trim().isEmpty()) {
                selection += " AND " + CostEntry.COL_CONTENT + " LIKE ?";
                conditionList.add("%" + cost.getContent() + "%");
            }

            if (cost.getAmount() != -1 ) {
                selection += " AND " + CostEntry.COL_AMOUNT + " LIKE ?";
                conditionList.add("%" + cost.getAmount() + "%");
            }

            if (cost.getDate() != null && !cost.getDate().trim().isEmpty()) {
                selection += " AND " + CostEntry.COL_DATE + " = ?";
                conditionList.add(cost.getDate());
            }

            if (cost.getTime() != null && !cost.getTime().trim().isEmpty()) {
                selection += " AND " + CostEntry.COL_TIME + " = ?";
                conditionList.add(cost.getTime());
            }

            if (cost.getType() != null && !cost.getType().trim().isEmpty()) {
                selection += " AND " + CostEntry.COL_TYPE + " = ?";
                conditionList.add(cost.getType());
            }


            if (cost.getTripId() != -1) {
                selection += " AND " + CostEntry.COL_TRIP_CONTROL_ID + " = ?";
                conditionList.add(String.valueOf(cost.getTripId()));
            }

            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }

            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }

        return getExpensesFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    public Cost getExpensesById(long id) {
        String selection = CostEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getExpensesFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }

    public long updateExpenses(Cost cost) {
        ContentValues values = getExpensesValues(cost);

        String selection = CostEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(cost.getId())};

        return dbWrite.update(CostEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public long deleteExpenses(long id) {
        String selection = CostEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return dbWrite.delete(CostEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected ContentValues getExpensesValues(Cost cost) {
        ContentValues values = new ContentValues();
        values.put(CostEntry.COL_CONTENT, cost.getContent());
        values.put(CostEntry.COL_DATE, cost.getDate());
        values.put(CostEntry.COL_TIME, cost.getTime());
        values.put(CostEntry.COL_AMOUNT, cost.getAmount());
        values.put(CostEntry.COL_TYPE, cost.getType());
        values.put(CostEntry.COL_TRIP_CONTROL_ID, cost.getTripId());

        return values;
    }

    protected ArrayList<Cost> getExpensesFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Cost> list = new ArrayList<>();

        Cursor cursor = dbRead.query(CostEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            Cost costItem = new Cost();

            costItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(CostEntry.COL_ID)));
            costItem.setContent(cursor.getString(cursor.getColumnIndexOrThrow(CostEntry.COL_CONTENT)));
            costItem.setDate(cursor.getString(cursor.getColumnIndexOrThrow(CostEntry.COL_DATE)));
            costItem.setTime(cursor.getString(cursor.getColumnIndexOrThrow(CostEntry.COL_TIME)));
            costItem.setAmount(cursor.getLong(cursor.getColumnIndexOrThrow(CostEntry.COL_AMOUNT)));
            costItem.setType(cursor.getString(cursor.getColumnIndexOrThrow(CostEntry.COL_TYPE)));
            costItem.setTripId(cursor.getLong(cursor.getColumnIndexOrThrow(CostEntry.COL_TRIP_CONTROL_ID)));

            list.add(costItem);
        }

        cursor.close();

        return list;
    }
}