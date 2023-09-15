package vn.edu.greenwich.trip_greenwich.ui.tripControl;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomappbar.BottomAppBar;
import vn.edu.greenwich.trip_greenwich.R;
import vn.edu.greenwich.trip_greenwich.database.TripCostDAO;
import vn.edu.greenwich.trip_greenwich.models.Cost;
import vn.edu.greenwich.trip_greenwich.models.TripControl;
import vn.edu.greenwich.trip_greenwich.ui.dialog.DeleteConfirmFragment;
import vn.edu.greenwich.trip_greenwich.ui.expens.CostCreateFragment;
import vn.edu.greenwich.trip_greenwich.ui.expens.list.CostListFragment;

public class TripControlDetailFragment extends Fragment
        implements DeleteConfirmFragment.FragmentListener, CostCreateFragment.FragmentListener {
    public static final String ARG_PARAM_TRIP_CONTROL = "tripControl";

    protected TripCostDAO _db;
    protected TripControl _trip;
    protected Button fmTripControlDetailCostButton;
    protected BottomAppBar fmTripControlDetailBottomAppBar;
    protected FragmentContainerView fmTripControlDetailCostList;
    protected TextView fmTripControlDetailName, fmTripControlDetailStartDate, fmTripControlDetailRisk,fmTripControlDetailCurrentLocation, fmTripControlDetailDestination, fmTripControlDetailDescription, fmTripControlDetailAmount;

    public TripControlDetailFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new TripCostDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_control_detail, container, false);

        fmTripControlDetailName = view.findViewById(R.id.fmTripControlDetailName);
        fmTripControlDetailStartDate = view.findViewById(R.id.fmTripControlDetailStartDate);
        fmTripControlDetailCurrentLocation = view.findViewById(R.id.fmTripControlDetailCurrentLocation);
        fmTripControlDetailDestination = view.findViewById(R.id.fmTripControlDetailDestination);
        fmTripControlDetailDescription = view.findViewById(R.id.fmTripControlDetailDescription);
        fmTripControlDetailRisk = view.findViewById(R.id.fmTripControlDetailRisk);
        fmTripControlDetailAmount = view.findViewById(R.id.fmTripControlDetailAmount);
        fmTripControlDetailBottomAppBar = view.findViewById(R.id.fmTripControlDetailBottomAppBar);
        fmTripControlDetailCostButton = view.findViewById(R.id.fmTripControlDetailExpensesButton);
        fmTripControlDetailCostList = view.findViewById(R.id.fmTripControlDetailCostList);

        fmTripControlDetailBottomAppBar.setOnMenuItemClickListener(item -> menuItemSelected(item));
        fmTripControlDetailCostButton.setOnClickListener(v -> showAddCostFragment());


        showDetails();
        showCostList();

        return view;
    }

    protected void showDetails() {
        String name =  getString(R.string.error_not_found);
        String currentLocation =  getString(R.string.error_not_found);
        String destination =  getString(R.string.error_not_found);
        String description =  getString(R.string.error_not_found);
        String startDate =  getString(R.string.error_not_found);
        String currentAmount = getString(R.string.error_not_found);
        String risk = getString(R.string.error_not_found);


        if (getArguments() != null) {
            long tripcontrolid;
            System.out.println(getArguments().get("tripControl"));
            if(getArguments().getLong("tripControl") != 0){
                tripcontrolid = getArguments().getLong("tripControl");

            }else{
                _trip = (TripControl) getArguments().getSerializable(ARG_PARAM_TRIP_CONTROL);
                tripcontrolid = _trip.getId();

            }
            // Retrieve data from Database.
            _trip = _db.getTripControlById(tripcontrolid);
            name = _trip.getName();
            currentLocation = _trip.getCurrentLocation();
            destination = _trip.getDestination();
            description = _trip.getDescription();
            startDate = _trip.getStartDate();
            currentAmount = String.valueOf(_trip.getCurrentAmount());
            risk = _trip.getRiskTrip() == 1 ? getString(R.string.label_risk_trip_yes) : getString(R.string.label_risk_trip_no);
        }
        fmTripControlDetailName.setText(name);
        fmTripControlDetailCurrentLocation.setText(currentLocation);
        fmTripControlDetailDestination.setText(destination);
        fmTripControlDetailDescription.setText(description);
        fmTripControlDetailAmount.setText(currentAmount+ "EUR");
        fmTripControlDetailStartDate.setText(startDate);
        fmTripControlDetailRisk.setText(risk);
    }

    protected void showCostList() {
        if (getArguments() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(CostListFragment.ARG_PARAM_TRIP_CONTROL_ID, _trip.getId());

            // Send arguments (tripControl id) to CostListFragment.
            getChildFragmentManager().getFragments().get(0).setArguments(bundle);
        }
    }

    protected boolean menuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tripControlUpdateFragment:
                showUpdateFragment();
                return true;

            case R.id.tripControlDeleteFragment:
                showDeleteConfirmFragment();
                return true;
        }

        return true;
    }

    protected void showUpdateFragment() {
        Bundle bundle = null;

        if (_trip != null) {
            bundle = new Bundle();
            bundle.putSerializable(TripControlUpdateFragment.ARG_PARAM_TRIP_MANAGER, _trip);
        }

        Navigation.findNavController(getView()).navigate(R.id.tripControlUpdateFragment, bundle);
    }

    protected void showDeleteConfirmFragment() {
        new DeleteConfirmFragment(getString(R.string.notification_delete_confirm)).show(getChildFragmentManager(), null);
    }

    protected void showAddCostFragment() {
        new CostCreateFragment(_trip.getId()).show(getChildFragmentManager(), null);
    }

    @Override
    public void sendFromDeleteConfirmFragment(int status) {
        if (status == 1 && _trip != null ) {
            long numOfDeletedRows = _db.deleteTripControl(_trip.getId());

            if (numOfDeletedRows > 0) {
                Toast.makeText(getContext(), R.string.notification_delete_success, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigateUp();
                return;

            }
        }
        Toast.makeText(getContext(), R.string.notification_delete_fail, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void sendFromCostCreateFragment(Cost cost) {
        if (cost != null) {
            cost.setTripId(_trip.getId());

            long id = _db.insertExpenses(cost);

            Toast.makeText(getContext(), id == -1 ? R.string.notification_create_fail : R.string.notification_create_success, Toast.LENGTH_SHORT).show();

            reloadCostList();
        }
    }

    protected void reloadCostList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CostListFragment.ARG_PARAM_TRIP_CONTROL_ID, _trip.getId());

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fmTripControlDetailCostList, CostListFragment.class, bundle)
                .commit();
    }


}