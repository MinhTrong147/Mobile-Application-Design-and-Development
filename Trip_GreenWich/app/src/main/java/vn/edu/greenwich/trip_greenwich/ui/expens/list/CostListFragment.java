package vn.edu.greenwich.trip_greenwich.ui.expens.list;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import vn.edu.greenwich.trip_greenwich.R;
import vn.edu.greenwich.trip_greenwich.database.TripCostDAO;
import vn.edu.greenwich.trip_greenwich.models.Cost;

public class CostListFragment extends Fragment {
    public static final String ARG_PARAM_TRIP_CONTROL_ID = "trip_control_id";

    protected ArrayList<Cost> _costList = new ArrayList<>();

    protected TripCostDAO _db;
    protected TextView fmCostListEmptyNotice;
    protected RecyclerView fmCostListRecylerView;

    public CostListFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        _db = new TripCostDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cost_list, container, false);

        if (getArguments() != null) {
            Cost cost = new Cost();
            cost.setTripId(getArguments().getLong(ARG_PARAM_TRIP_CONTROL_ID));

            _costList = _db.getCostList(cost, null, false);
        }

        fmCostListRecylerView = view.findViewById(R.id.fmCostListRecylerView);
        fmCostListEmptyNotice = view.findViewById(R.id.fmCostListEmptyNotice);

        LinearLayoutManager linearLayoutControl = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutControl.getOrientation());

        fmCostListRecylerView.addItemDecoration(dividerItemDecoration);
        fmCostListRecylerView.setAdapter(new CostAdapter(_costList));
        fmCostListRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Show "No Cost." message.
        fmCostListEmptyNotice.setVisibility(_costList.isEmpty() ? View.VISIBLE : View.GONE);

        return view;
    }
}