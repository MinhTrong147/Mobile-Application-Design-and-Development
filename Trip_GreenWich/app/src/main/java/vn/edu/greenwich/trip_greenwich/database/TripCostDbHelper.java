package vn.edu.greenwich.trip_greenwich.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class TripCostDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TripCost";
    public static final int DATABASE_VERSION = 1;

    public TripCostDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TripControlEntry.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(CostEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(CostEntry.SQL_DELETE_TABLE);
        sqLiteDatabase.execSQL(TripControlEntry.SQL_DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void updateRS (SQLiteDatabase sqLiteDatabase,String s){
        sqLiteDatabase.execSQL(s);
    }

}