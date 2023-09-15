package vn.edu.greenwich.trip_greenwich;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Date;
import vn.edu.greenwich.trip_greenwich.database.BackupEntry;
import vn.edu.greenwich.trip_greenwich.database.TripCostDAO;
import vn.edu.greenwich.trip_greenwich.models.Backup;
import vn.edu.greenwich.trip_greenwich.models.Cost;
import vn.edu.greenwich.trip_greenwich.models.TripControl;

public class SettingActivity extends AppCompatActivity {
    protected TripCostDAO _db;
    protected Button settingBackup, settingResetDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(R.string.label_setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _db = new TripCostDAO(this);

        settingBackup = findViewById(R.id.settingBackup);
        settingResetDatabase = findViewById(R.id.settingResetDatabase);

        settingBackup.setOnClickListener(v -> backup());
        settingResetDatabase.setOnClickListener(v -> resetDatabase());
    }

    protected void backup() {
        ArrayList<TripControl> tripControls = _db.getTripControlList(null, null, false);
        ArrayList<Cost> expens = _db.getCostList(null, null, false);

        if (null != tripControls && 0 < tripControls.size() && null != expens && 0 < expens.size()) {
            String deviceName = Build.MANUFACTURER
                    + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                    + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
            String userId = " nt6651u ";
            Backup backup = new Backup(new Date(), deviceName, userId, tripControls, expens);

            FirebaseFirestore.getInstance().collection(BackupEntry.TABLE_NAME)
                    .add(backup)
                    .addOnSuccessListener(document -> {
                        Toast.makeText(this, R.string.notification_backup_success, Toast.LENGTH_SHORT).show();
                        Log.d(getResources().getString(R.string.label_backup_firestore), document.getId());
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, R.string.notification_backup_fail, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    });
        } else {
            Toast.makeText(this, R.string.error_empty_list, Toast.LENGTH_SHORT).show();
        }
    }

    protected void resetDatabase() {
        _db.reset();

        Toast.makeText(this, R.string.label_reset_database, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}