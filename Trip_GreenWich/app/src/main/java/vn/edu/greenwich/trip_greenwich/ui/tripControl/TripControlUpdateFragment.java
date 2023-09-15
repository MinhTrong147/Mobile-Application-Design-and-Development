package vn.edu.greenwich.trip_greenwich.ui.tripControl;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import vn.edu.greenwich.trip_greenwich.R;

import vn.edu.greenwich.trip_greenwich.models.TripControl;

public class TripControlUpdateFragment extends Fragment implements TripControlRegisterFragment.FragmentListener {
    public static final String ARG_PARAM_TRIP_MANAGER= "tripControl";

    public TripControlUpdateFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_control_update, container, false);

        if (getArguments() != null) {
            TripControl tripControl = (TripControl) getArguments().getSerializable(ARG_PARAM_TRIP_MANAGER);

            Bundle bundle = new Bundle();
            bundle.putSerializable(TripControlRegisterFragment.ARG_PARAM_TRIP_CONTROL, tripControl);

            // Send arguments (tripControl info) to TripControlRegisterFragment.
            getChildFragmentManager().getFragments().get(0).setArguments(bundle);
        }

        return view;
    }

    @Override
    public void sendFromTripControlRegisterFragment(long status) {
        switch ((int) status) {
            case 0:
                Toast.makeText(getContext(), R.string.notification_update_fail, Toast.LENGTH_SHORT).show();
                return;

            default:
                Toast.makeText(getContext(), R.string.notification_update_success, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigateUp();
        }
    }
}