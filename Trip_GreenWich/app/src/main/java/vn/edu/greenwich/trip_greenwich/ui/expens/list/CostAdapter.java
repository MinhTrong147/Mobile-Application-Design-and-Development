package vn.edu.greenwich.trip_greenwich.ui.expens.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import vn.edu.greenwich.trip_greenwich.R;
import vn.edu.greenwich.trip_greenwich.database.CostEntry;
import vn.edu.greenwich.trip_greenwich.database.TripCostDAO;
import vn.edu.greenwich.trip_greenwich.database.TripControlEntry;
import vn.edu.greenwich.trip_greenwich.models.Cost;

public class CostAdapter extends RecyclerView.Adapter<CostAdapter.ViewHolder> implements Filterable {
    protected ArrayList<Cost> _originalList;
    protected ArrayList<Cost> _filteredList;
    protected Button listItemCostDelete;
    protected TripCostDAO _db;
    protected CostAdapter.ItemFilter _itemFilter = new CostAdapter.ItemFilter();

    public CostAdapter(ArrayList<Cost> list) {
        _originalList = list;
        _filteredList = list;
    }

    public void updateList(ArrayList<Cost> list) {
        _originalList = list;
        _filteredList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_cost, parent, false);
        listItemCostDelete = view.findViewById(R.id.listItemCostDelete);
        _db = new TripCostDAO(view.getContext());

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cost cost = _filteredList.get(position);

        holder.listItemCostDate.setText(cost.getDate());
        holder.listItemCostTime.setText(cost.getTime());
        holder.listItemCostAmount.setText(String.valueOf(cost.getAmount())+ "EUR");
        holder.listItemCostType.setText(cost.getType());
        holder.listItemCostContent.setText(cost.getContent());
        listItemCostDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query ="DELETE FROM "+ CostEntry.TABLE_NAME+ " WHERE "+ CostEntry.COL_ID+" = "+ cost.getId();
                String queryUpdate ="UPDATE "+ TripControlEntry.TABLE_NAME+" SET "+ TripControlEntry.COL_CURRENT_AMOUNT+" = "+ TripControlEntry.COL_CURRENT_AMOUNT+ " - " + cost.getAmount()+ " WHERE "+ TripControlEntry.COL_ID+" = "+ cost.getTripId();

                _db.updateDb(query);
                _db.updateDb(queryUpdate);

            }
        });

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
            protected TextView listItemCostDate, listItemCostTime, listItemCostType, listItemCostContent,
                    listItemCostAmount;
            public ViewHolder(View itemView) {
                super(itemView);

                listItemCostDate = itemView.findViewById(R.id.listItemCostDate);
                listItemCostTime = itemView.findViewById(R.id.listItemCostTime);
                listItemCostType = itemView.findViewById(R.id.listItemCostType);
                listItemCostAmount = itemView.findViewById(R.id.listItemCostAmount);
                listItemCostContent = itemView.findViewById(R.id.listItemCostContent);

            }
        }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<Cost> list = _originalList;
            final ArrayList<Cost> nlist = new ArrayList<>(list.size());

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            for (Cost cost : list) {
                String filterableString = cost.toString();

                if (filterableString.toLowerCase().contains(filterString))
                    nlist.add(cost);
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _filteredList = (ArrayList<Cost>) results.values;
            notifyDataSetChanged();
        }
    }
}