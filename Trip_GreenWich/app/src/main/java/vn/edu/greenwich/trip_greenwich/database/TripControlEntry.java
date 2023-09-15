package vn.edu.greenwich.trip_greenwich.database;

public class TripControlEntry {
    public static final String TABLE_NAME = "tripControl";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_CURRENT_LOCATION= "current_location";
    public static final String COL_DESTINATION = "destination";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_CURRENT_AMOUNT = "current_amount";
    public static final String COL_START_DATE = "start_date";
    public static final String COL_RISK_TRIP = "risk_trip";

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY," +
                    COL_NAME + " TEXT NOT NULL," +
                    COL_CURRENT_LOCATION+ " TEXT NOT NULL," +
                    COL_DESTINATION + " TEXT NOT NULL," +
                    COL_CURRENT_AMOUNT + " INTEGER NOT NULL," +
                    COL_DESCRIPTION + " TEXT NOT NULL," +
                    COL_START_DATE + " TEXT NOT NULL," +
                    COL_RISK_TRIP + " INTEGER NULL)";

    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}