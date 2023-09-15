package vn.edu.greenwich.trip_greenwich.ui.tripControl.list;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import vn.edu.greenwich.trip_greenwich.R;
import vn.edu.greenwich.trip_greenwich.models.TripControl;
import vn.edu.greenwich.trip_greenwich.ui.tripControl.TripControlSearchFragment;
import vn.edu.greenwich.trip_greenwich.database.TripCostDAO;

public class TripControlListFragment extends Fragment implements TripControlSearchFragment.FragmentListener {
    protected ArrayList<TripControl> tripControlList = new ArrayList<>();

    protected TripCostDAO _db;
    protected EditText fmTripControlListFilter;
    protected TripControlAdapter tripControlAdapter;
    protected TextView fmTripControlListEmptyNotice;
    protected RecyclerView fmTripControlListRecyclerView;
    protected ImageButton fmTripControlListButtonSearch, fmTripControlListButtonResetSearch;

    public TripControlListFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        _db = new TripCostDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_control_list, container, false);

        fmTripControlListRecyclerView = view.findViewById(R.id.fmTripControlListRecylerView);
        fmTripControlListEmptyNotice = view.findViewById(R.id.fmTripControlListEmptyNotice);

        fmTripControlListButtonResetSearch = view.findViewById(R.id.fmTripControlListButtonResetSearch);
        fmTripControlListButtonResetSearch.setOnClickListener(v -> resetSearch());

        fmTripControlListButtonSearch = view.findViewById(R.id.fmTripControlListButtonSearch);
        fmTripControlListButtonSearch.setOnClickListener(v -> showSearchDialog());

        fmTripControlListFilter = view.findViewById(R.id.fmTripControlListFilter);
        fmTripControlListFilter.addTextChangedListener(filter());

        LinearLayoutManager linearLayoutControl = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutControl.getOrientation());

        tripControlAdapter = new TripControlAdapter(tripControlList);

        fmTripControlListRecyclerView.addItemDecoration(dividerItemDecoration);
        fmTripControlListRecyclerView.setAdapter(tripControlAdapter);
        fmTripControlListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        reloadList(null);
    }

    protected void reloadList(TripControl tripControl) {
        tripControlList = _db.getTripControlList(tripControl, null, false);
        tripControlAdapter.updateList(tripControlList);

        // Show "No TripControl." message.
        fmTripControlListEmptyNotice.setVisibility(tripControlList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    protected TextWatcher filter() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                tripControlAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    protected void resetSearch() {
        fmTripControlListFilter.setText("");
        reloadList(null);
    }

    protected void showSearchDialog() {
        new TripControlSearchFragment().show(getChildFragmentManager(), null);
    }

    @Override
    public void sendFromTripControlSearchFragment(TripControl tripControl) {
        if (!tripControl.isEmpty()) {
            reloadList(tripControl);
            return;
        }

        reloadList(null);
    }
}