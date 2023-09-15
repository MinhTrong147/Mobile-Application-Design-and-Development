package vn.edu.greenwich.trip_greenwich.ui.tripControl.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import vn.edu.greenwich.trip_greenwich.R;
import vn.edu.greenwich.trip_greenwich.models.TripControl;
import vn.edu.greenwich.trip_greenwich.ui.tripControl.TripControlDetailFragment;

public class TripControlAdapter extends RecyclerView.Adapter<TripControlAdapter.ViewHolder> implements Filterable {
    protected ArrayList<TripControl> _originalList;
    protected ArrayList<TripControl> _filteredList;
    protected TripControlAdapter.ItemFilter _itemFilter = new TripControlAdapter.ItemFilter();

    public TripControlAdapter(ArrayList<TripControl> list) {
        _originalList = list;
        _filteredList = list;
    }

    public void updateList(ArrayList<TripControl> list) {
        _originalList = list;
        _filteredList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_trip_control, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TripControl tripControl = _filteredList.get(position);

        String riskYes = holder.itemView.getResources().getString(R.string.label_risk_trip_yes);
        String riskNo = holder.itemView.getResources().getString(R.string.label_risk_trip_no);


        holder.listItemTripControlName.setText(tripControl.getName());
        holder.listItemTripControlDestination.setText(tripControl.getDestination());
        holder.listItemTripControlStartDate.setText(tripControl.getStartDate());
        holder.listItemTripControlRisk.setText(tripControl.getRiskTrip() == 1 ? riskYes : riskNo);
    }

    @Override
    public int getItemCount() {
        return _filteredList == null ? 0 : _filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return _itemFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout listItemTripControl;
        protected TextView listItemTripControlName,  listItemTripControlStartDate, listItemTripControlRisk, listItemTripControlDestination;

        public ViewHolder(View itemView) {
            super(itemView);

            listItemTripControlName = itemView.findViewById(R.id.listItemTripControlName);
            listItemTripControlStartDate = itemView.findViewById(R.id.listItemTripControlStartDate);
            listItemTripControlRisk = itemView.findViewById(R.id.listItemTripControlRisk);
            listItemTripControlDestination = itemView.findViewById(R.id.listItemTripControlDestination);

            listItemTripControl = itemView.findViewById(R.id.listItemTripControl);
            listItemTripControl.setOnClickListener(v -> showDetail(v));
        }

        protected void showDetail(View view) {
            TripControl tripControl = _filteredList.get(getAdapterPosition());

            Bundle bundle = new Bundle();
            bundle.putSerializable(TripControlDetailFragment.ARG_PARAM_TRIP_CONTROL, tripControl);

            Navigation.findNavController(view).navigate(R.id.tripControlDetailFragment, bundle);
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<TripControl> list = _originalList;
            final ArrayList<TripControl> nlist = new ArrayList<>(list.size());

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            for (TripControl tripControl : list) {
                String filterableString = tripControl.toString();
                if (filterableString.toLowerCase().contains(filterString)) nlist.add(tripControl);
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _filteredList = (ArrayList<TripControl>) results.values;
            notifyDataSetChanged();
        }
    }
}