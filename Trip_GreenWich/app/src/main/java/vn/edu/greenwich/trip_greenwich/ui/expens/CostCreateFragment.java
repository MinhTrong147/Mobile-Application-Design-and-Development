package vn.edu.greenwich.trip_greenwich.ui.expens;


import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.content.Context;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;


import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import vn.edu.greenwich.trip_greenwich.R;
import vn.edu.greenwich.trip_greenwich.database.TripCostDAO;
import vn.edu.greenwich.trip_greenwich.database.TripControlEntry;
import vn.edu.greenwich.trip_greenwich.models.Cost;
import vn.edu.greenwich.trip_greenwich.models.TripControl;
import vn.edu.greenwich.trip_greenwich.ui.dialog.DatePickerFragment;
import vn.edu.greenwich.trip_greenwich.ui.dialog.TimePickerFragment;
import vn.edu.greenwich.trip_greenwich.ui.tripControl.TripControlDetailFragment;

public class CostCreateFragment extends DialogFragment
        implements DatePickerFragment.FragmentListener, TimePickerFragment.FragmentListener {
    protected long _tripcontrolId;
    protected TripCostDAO _db;
    FragmentManager fragmentControl;
    TripControlDetailFragment frg = new TripControlDetailFragment();

    protected EditText fmCostCreateDate, fmCostCreateTime,fmCostCreateAmount, fmCostCreateContent;
    protected Button fmCostCreateButtonCancel, fmCostCreateButtonAdd;
    protected Spinner fmCostCreateType;
    protected TextView current_amount;

    public CostCreateFragment() {
        _tripcontrolId = -1;
    }

    public CostCreateFragment(long tripcontrolId) {
        _tripcontrolId = tripcontrolId;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new TripCostDAO(getContext());
        fragmentControl =getActivity().getSupportFragmentManager();
    }

    @Override
    public void sendFromDatePickerFragment(String date) {
        fmCostCreateDate.setText(date);
    }

    @Override
    public void sendFromTimePickerFragment(String time) {
        fmCostCreateTime.setText(time);
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cost_create, container, false);

        fmCostCreateDate = view.findViewById(R.id.fmCostCreateDate);
        fmCostCreateTime = view.findViewById(R.id.fmCostCreateTime);
        fmCostCreateAmount = view.findViewById(R.id.fmCostCreateAmount);
        fmCostCreateContent = view.findViewById(R.id.fmCostCreateContent);
        fmCostCreateButtonCancel = view.findViewById(R.id.fmCostCreateButtonCancel);
        fmCostCreateButtonAdd = view.findViewById(R.id.fmCostCreateButtonAdd);
        fmCostCreateType = view.findViewById(R.id.fmCostCreateType);
        fmCostCreateButtonCancel.setOnClickListener(v -> dismiss());
        fmCostCreateButtonAdd.setOnClickListener(v -> createCost());
        current_amount = view.findViewById(R.id.fmTripControlDetailAmount);

        fmCostCreateDate.setOnTouchListener((v, motionEvent) -> showDateDialog(motionEvent));
        fmCostCreateTime.setOnTouchListener((v, motionEvent) -> showTimeDialog(motionEvent));

        setTypeSpinner();

        return view;
    }

    protected void setTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.expenses_type,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fmCostCreateType.setAdapter(adapter);
    }

    protected boolean showDateDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new DatePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }

        return false;
    }

    protected boolean showTimeDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new TimePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }

        return false;
    }

    protected void createCost() {
        Cost cost = new Cost();
        TripControl tripControl = new TripControl();

        tripControl.setCurrentAmount(Long.parseLong(fmCostCreateAmount.getText().toString()));
        String query = "UPDATE "+ TripControlEntry.TABLE_NAME +" SET "+ TripControlEntry.COL_CURRENT_AMOUNT+ " = "+ TripControlEntry.COL_CURRENT_AMOUNT+"+ "+String.valueOf(fmCostCreateAmount.getText().toString())+" WHERE id= "+_tripcontrolId+";";
        _db.updateDb(query);

        cost.setType(fmCostCreateType.getSelectedItem().toString());
        cost.setTime(fmCostCreateTime.getText().toString());
        cost.setAmount(Long.parseLong(fmCostCreateAmount.getText().toString()));
        cost.setDate(fmCostCreateDate.getText().toString());
        cost.setContent(fmCostCreateContent.getText().toString());
        Bundle args = new Bundle();
        args.putLong("tripControl",_tripcontrolId);
        frg.setArguments(args);
        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromCostCreateFragment(cost);
        fragmentControl.findFragmentById(R.id.frg_trip_detail);

        fragmentControl.beginTransaction().replace(R.id.frg_trip_detail,frg).detach(frg).attach(frg).commit();

        dismiss();
    }



    public interface FragmentListener {
        void sendFromCostCreateFragment(Cost cost);
    }
}