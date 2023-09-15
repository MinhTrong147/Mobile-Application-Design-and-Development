package vn.edu.greenwich.trip_greenwich.ui.tripControl;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import vn.edu.greenwich.trip_greenwich.R;
import vn.edu.greenwich.trip_greenwich.models.TripControl;
import vn.edu.greenwich.trip_greenwich.ui.dialog.CalendarFragment;

public class TripControlSearchFragment extends DialogFragment implements CalendarFragment.FragmentListener {
    protected EditText fmTripControlSearchDate, fmTripControlSearchName;
    protected Button fmTripControlSearchButtonCancel, fmTripControlSearchButtonSearch;

    public TripControlSearchFragment() {}

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
        View view = inflater.inflate(R.layout.fragment_trip_control_search, container, false);

        fmTripControlSearchDate = view.findViewById(R.id.fmTripControlSearchDate);
        fmTripControlSearchName = view.findViewById(R.id.fmTripControlSearchName);
        fmTripControlSearchButtonCancel = view.findViewById(R.id.fmTripControlSearchButtonCancel);
        fmTripControlSearchButtonSearch = view.findViewById(R.id.fmTripControlSearchButtonSearch);

        fmTripControlSearchButtonSearch.setOnClickListener(v -> search());
        fmTripControlSearchButtonCancel.setOnClickListener(v -> dismiss());
        fmTripControlSearchDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));

        return view;
    }

    protected void search() {
        TripControl _tripControl = new TripControl();

        String date = fmTripControlSearchDate.getText().toString();
        String name = fmTripControlSearchName.getText().toString();

        if (date != null && !date.trim().isEmpty())
            _tripControl.setStartDate(date);

        if (name != null && !name.trim().isEmpty())
            _tripControl.setName(name);

        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromTripControlSearchFragment(_tripControl);
        System.out.println(name);

        dismiss();
    }

    protected boolean showCalendar(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new CalendarFragment().show(getChildFragmentManager(), null);
        }

        return false;
    }

    @Override
    public void sendFromCalendarFragment(String date) {
        fmTripControlSearchDate.setText(date);
    }

    public interface FragmentListener {
        void sendFromTripControlSearchFragment(TripControl tripControl);
    }
}