package vn.edu.greenwich.trip_greenwich.ui.tripControl;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import vn.edu.greenwich.trip_greenwich.R;
import vn.edu.greenwich.trip_greenwich.database.TripCostDAO;
import vn.edu.greenwich.trip_greenwich.models.TripControl;

public class TripControlRegisterConfirmFragment extends DialogFragment {
    protected TripCostDAO _db;
    protected TripControl _tripControl;
    protected Button fmTripControlRegisterConfirmButtonConfirm, fmTripControlRegisterConfirmButtonCancel;
    protected TextView fmTripControlRegisterConfirmName, fmTripControlRegisterConfirmStartDate, fmTripControlRegisterConfirmRisk,fmTripControlRegisterConfirmCurrentLocation, fmTripControlRegisterConfirmDestination, fmTripControlRegisterConfirmDescription;

    public TripControlRegisterConfirmFragment() {
        _tripControl = new TripControl();
    }

    public TripControlRegisterConfirmFragment(TripControl tripControl) {
        _tripControl = tripControl;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new TripCostDAO(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_control_register_confirm, container, false);

        String name = getString(R.string.error_no_info);
        String startDate = getString(R.string.error_no_info);
        String riskTrip = getString(R.string.error_no_info);
        String CurrentLocation =getString(R.string.error_no_info);
        String Destination =getString(R.string.error_no_info);
        String Description =getString(R.string.error_no_info);

        fmTripControlRegisterConfirmName = view.findViewById(R.id.fmTripControlRegisterConfirmName);
        fmTripControlRegisterConfirmStartDate = view.findViewById(R.id.fmTripControlRegisterConfirmStartDate);
        fmTripControlRegisterConfirmRisk = view.findViewById(R.id.fmTripControlRegisterConfirmRisk);
        fmTripControlRegisterConfirmButtonCancel = view.findViewById(R.id.fmTripControlRegisterConfirmButtonCancel);
        fmTripControlRegisterConfirmButtonConfirm = view.findViewById(R.id.fmTripControlRegisterConfirmButtonConfirm);
        fmTripControlRegisterConfirmDestination =view.findViewById(R.id.fmTripControlRegisterConfirmDestination);
        fmTripControlRegisterConfirmCurrentLocation =view.findViewById(R.id.fmTripControlRegisterConfirmCurrentLocation);
        fmTripControlRegisterConfirmDescription = view.findViewById(R.id.fmTripControlRegisterConfirmDescription);

        if (_tripControl.getRiskTrip() != -1) {
            riskTrip = _tripControl.getRiskTrip() == 1 ? getString(R.string.label_risk_trip_yes) : getString(R.string.label_risk_trip_no);
        }

        if (_tripControl.getName() != null && !_tripControl.getName().trim().isEmpty()) {
            name = _tripControl.getName();
        }


        if (_tripControl.getStartDate() != null && !_tripControl.getStartDate().trim().isEmpty()) {
            startDate = _tripControl.getStartDate();
        }
        if (_tripControl.getCurrentLocation() != null && !_tripControl.getCurrentLocation().trim().isEmpty()) {
            CurrentLocation  = _tripControl.getCurrentLocation();
        }
        if (_tripControl.getDescription() != null && !_tripControl.getDescription().trim().isEmpty()) {
            Description  = _tripControl.getDescription();
        }
        if (_tripControl.getDestination() != null && !_tripControl.getDestination().trim().isEmpty()) {
            Destination = _tripControl.getDestination();
        }


        fmTripControlRegisterConfirmName.setText(name);
        fmTripControlRegisterConfirmStartDate.setText(startDate);
        fmTripControlRegisterConfirmRisk.setText(riskTrip);
        fmTripControlRegisterConfirmCurrentLocation.setText(CurrentLocation);
        fmTripControlRegisterConfirmDestination.setText(Destination);
        fmTripControlRegisterConfirmDescription.setText(Description);
        fmTripControlRegisterConfirmButtonCancel.setOnClickListener(v -> dismiss());
        fmTripControlRegisterConfirmButtonConfirm.setOnClickListener(v -> confirm());

        return view;
    }

    protected void confirm() {
        long status = _db.insertTripControl(_tripControl);

        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromTripControlRegisterConfirmFragment(status);

        dismiss();
    }

    public interface FragmentListener {
        void sendFromTripControlRegisterConfirmFragment(long status);
    }
}