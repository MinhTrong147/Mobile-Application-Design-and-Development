package vn.edu.greenwich.trip_greenwich.ui.tripControl;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.switchmaterial.SwitchMaterial;
import vn.edu.greenwich.trip_greenwich.R;
import vn.edu.greenwich.trip_greenwich.database.TripCostDAO;
import vn.edu.greenwich.trip_greenwich.models.TripControl;
import vn.edu.greenwich.trip_greenwich.ui.dialog.CalendarFragment;

public class TripControlRegisterFragment extends Fragment
        implements TripControlRegisterConfirmFragment.FragmentListener, CalendarFragment.FragmentListener {
    public static final String ARG_PARAM_TRIP_CONTROL = "tripControl";

    protected EditText fmTripControlRegisterName, fmTripControlRegisterCurrentLocation,fmTripControlRegisterDestination, fmTripControlRegisterDescription, fmTripControlRegisterStartDate;
    protected LinearLayout fmTripControlRegisterLinearLayout;
    protected SwitchMaterial fmTripControlRegisterRisk;
    protected TextView fmTripControlRegisterError;
    protected Button fmTripControlRegisterButton;
    protected TripCostDAO _db;

    public TripControlRegisterFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new TripCostDAO(getContext());
    }
    long current_amount = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_control_register, container, false);

        fmTripControlRegisterError = view.findViewById(R.id.fmTripControlRegisterError);
        fmTripControlRegisterName = view.findViewById(R.id.fmTripControlRegisterName);
        fmTripControlRegisterCurrentLocation = view.findViewById(R.id.fmTripControlRegisterCurrentLocation);
        fmTripControlRegisterDestination = view.findViewById(R.id.fmTripControlRegisterDestination);
        fmTripControlRegisterDescription = view.findViewById(R.id.fmTripControlRegisterDescription);
        fmTripControlRegisterStartDate = view.findViewById(R.id.fmTripControlRegisterStartDate);
        fmTripControlRegisterRisk = view.findViewById(R.id.fmTripControlRegisterRisk);
        fmTripControlRegisterButton = view.findViewById(R.id.fmTripControlRegisterButton);
        fmTripControlRegisterLinearLayout = view.findViewById(R.id.fmTripControlRegisterLinearLayout);

        // Show Calendar for choosing a date.
        fmTripControlRegisterStartDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));

        // Update current tripControl.
        if (getArguments() != null) {
            TripControl tripControl = (TripControl) getArguments().getSerializable(ARG_PARAM_TRIP_CONTROL);

            fmTripControlRegisterName.setText(tripControl.getName());
            fmTripControlRegisterCurrentLocation.setText(tripControl.getCurrentLocation());
            fmTripControlRegisterDestination.setText(tripControl.getDestination());
            fmTripControlRegisterDescription.setText(tripControl.getDescription());
            fmTripControlRegisterStartDate.setText(tripControl.getStartDate());
            fmTripControlRegisterRisk.setChecked(tripControl.getRiskTrip() == 1 ? true : false);
            current_amount = tripControl.getCurrentAmount();
            fmTripControlRegisterButton.setText(R.string.label_update);
            fmTripControlRegisterButton.setOnClickListener(v -> update(tripControl.getId()));

            return view;
        }

        // Create new tripControl.
        fmTripControlRegisterButton.setOnClickListener(v -> register());

        return view;
    }

    protected void register() {
        if (isValidForm()) {
            TripControl tripControl = getTripControlFromInput(-1);

            new TripControlRegisterConfirmFragment(tripControl).show(getChildFragmentManager(), null);

            return;
        }

    }

    protected void update(long id) {
        if (isValidForm()) {
            TripControl tripControl = getTripControlFromInput(id);

            long status = _db.updateTripControl(tripControl);

            FragmentListener listener = (FragmentListener) getParentFragment();
            listener.sendFromTripControlRegisterFragment(status);

            return;
        }

    }

    protected boolean showCalendar(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new CalendarFragment().show(getChildFragmentManager(), null);
        }

        return false;
    }

    protected TripControl getTripControlFromInput(long id) {
        String name = fmTripControlRegisterName.getText().toString();
        String current_location = fmTripControlRegisterCurrentLocation.getText().toString();
        String destination = fmTripControlRegisterDestination.getText().toString();
        String description = fmTripControlRegisterDescription.getText().toString();
        String startDate = fmTripControlRegisterStartDate.getText().toString();
        int owner = fmTripControlRegisterRisk.isChecked() ? 1 : 0;

        return new TripControl(id, name, current_location, destination, description, startDate, owner,current_amount);
    }

    protected boolean isValidForm() {
        boolean isValid = true;

        String error = "";
        String name = fmTripControlRegisterName.getText().toString();
        String startDate = fmTripControlRegisterStartDate.getText().toString();
        String CurrentLocation = fmTripControlRegisterCurrentLocation.getText().toString();
        String Destination = fmTripControlRegisterDestination.getText().toString();
        String Description = fmTripControlRegisterDescription.getText().toString();

        if (name == null || name.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_name) + "\n";
            isValid = false;
        }
        if (CurrentLocation == null || CurrentLocation.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_currentLocation) + "\n";
            isValid = false;
        }
        if (Destination == null || Destination.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_destination) + "\n";
            isValid = false;
        }

        if (startDate == null || startDate.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_start_date) + "\n";
            isValid = false;
        }

        fmTripControlRegisterError.setText(error);

        return isValid;
    }


    @Override
    public void sendFromTripControlRegisterConfirmFragment(long status) {
        switch ((int) status) {
            case -1:
                Toast.makeText(getContext(), R.string.notification_create_fail, Toast.LENGTH_SHORT).show();
                return;

            default:
                Toast.makeText(getContext(), R.string.notification_create_success, Toast.LENGTH_SHORT).show();

                fmTripControlRegisterName.setText("");
                fmTripControlRegisterStartDate.setText("");
                fmTripControlRegisterCurrentLocation.setText("");
                fmTripControlRegisterDescription.setText("");
                fmTripControlRegisterDestination.setText("");
                fmTripControlRegisterName.requestFocus();
        }
    }

    @Override
    public void sendFromCalendarFragment(String date) {
        fmTripControlRegisterStartDate.setText(date);
    }



    public interface FragmentListener {
        void sendFromTripControlRegisterFragment(long status);
    }
}