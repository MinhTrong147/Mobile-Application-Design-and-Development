package vn.edu.greenwich.trip_greenwich.database;

public class CostEntry {
    public static final String TABLE_NAME = "expens";
    public static final String COL_ID = "id";
    public static final String COL_CONTENT = "content";
    public static final String COL_DATE = "date";
    public static final String COL_TIME = "time";
    public static final String COL_TYPE = "type";
    public static final String COL_AMOUNT = "amount";
    public static final String COL_TRIP_CONTROL_ID = "Trip_id";

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY," +
                    COL_CONTENT + " TEXT NOT NULL," +
                    COL_AMOUNT + " INTEGER NOT NULL," +
                    COL_DATE + " TEXT NOT NULL," +
                    COL_TIME + " TEXT NOT NULL," +
                    COL_TYPE + " INTEGER NOT NULL," +
                    COL_TRIP_CONTROL_ID + " INTEGER NOT NULL," +
                    "FOREIGN KEY(" + COL_TRIP_CONTROL_ID + ") " +
                    "REFERENCES " + TripControlEntry.TABLE_NAME + "(" + TripControlEntry.COL_ID + "))";

    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}