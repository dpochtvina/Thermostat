package ru.aesalon.thermostat;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.drawable.RippleDrawable;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.SnackBar;
import com.rey.material.widget.Spinner;
import com.rey.material.widget.Button;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Nikita on 18.04.2015.
 */
public class FragmentIntervals extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    ArrayAdapter<CharSequence> adapter;
    protected static final int REFRESH = 0;
    private HashMap<String, Vector<DataController.Interval>>storage;
    private Spinner spn_label;
    Button bt_time_light;
    int nowday = 0;
    String tagNow;
    FloatingActionButton btn;
    Time t1 = null;
    Time t2 = null;
    private Handler _hRedraw;
    DataController controller;
    @InjectView(R.id.intervals_table) protected TableLayout interTable;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public void REALLOADDAY(int numDayWeek)
    {
        nowday = numDayWeek;
        loadDay(numDayWeek);
    }

    @Override
    public void onStart(){
        super.onStart();
        storage = new HashMap<>();
        controller = DataController.getInstance(getActivity());

        //TODO:Test Remove!

/*        if (storage==null || storage.get(DataController.tagIntervals_mon)==null) {
            loadDay(0);

        }*/
        REALLOADDAY(nowday);
        //addRows(storage.get(DataController.tagIntervals_mon));
    }
    //from 0- Monday, 6 - Sunday
    public void loadDay(int numDayWeek){
        nowday = numDayWeek;
        switch(numDayWeek){
            case 0:{
                storage.put(DataController.tagIntervals_mon, controller.getIntervals(numDayWeek));
                if(storage.get(DataController.tagIntervals_mon)==null) {
                    storage.put(DataController.tagIntervals_mon, new Vector<DataController.Interval>());
                }
                break;
            }
            case 1:{
                storage.put(DataController.tagIntervals_tue, controller.getIntervals(numDayWeek));
                if(storage.get(DataController.tagIntervals_tue)==null) {
                    storage.put(DataController.tagIntervals_tue, new Vector<DataController.Interval>());
                }
                break;
            }
            case 2:{
                storage.put(DataController.tagIntervals_wed, controller.getIntervals(numDayWeek));
                if(storage.get(DataController.tagIntervals_wed)==null) {
                    storage.put(DataController.tagIntervals_wed, new Vector<DataController.Interval>());
                }
                break;
            }
            case 3:{
                storage.put(DataController.tagIntervals_thu, controller.getIntervals(numDayWeek));
                if(storage.get(DataController.tagIntervals_thu)==null) {
                    storage.put(DataController.tagIntervals_thu, new Vector<DataController.Interval>());
                }
                break;
            }
            case 4:{
                storage.put(DataController.tagIntervals_fri, controller.getIntervals(numDayWeek));
                if(storage.get(DataController.tagIntervals_fri)==null) {
                    storage.put(DataController.tagIntervals_fri, new Vector<DataController.Interval>());
                }
                break;
            }
            case 5:{
                storage.put(DataController.tagIntervals_sat, controller.getIntervals(numDayWeek));
                if(storage.get(DataController.tagIntervals_sat)==null) {
                    storage.put(DataController.tagIntervals_sat, new Vector<DataController.Interval>());
                }
                break;
            }
            case 6:{
                storage.put(DataController.tagIntervals_sun, controller.getIntervals(numDayWeek));
                if(storage.get(DataController.tagIntervals_sun)==null) {
                    storage.put(DataController.tagIntervals_sun, new Vector<DataController.Interval>());
                }
                break;
            }
        }
        switch(numDayWeek){
            case 0:
            {
                addRows(storage.get(DataController.tagIntervals_mon));
                tagNow = DataController.tagIntervals_mon;
                break;
            }
            case 1:
            {
                addRows(storage.get(DataController.tagIntervals_tue));
                tagNow = DataController.tagIntervals_tue;
                break;
            }
            case 2:
            {
                addRows(storage.get(DataController.tagIntervals_wed));
                tagNow = DataController.tagIntervals_wed;
                break;
            }
            case 3:
            {
                addRows(storage.get(DataController.tagIntervals_thu));
                tagNow = DataController.tagIntervals_thu;
                break;
            }
            case 4:
            {
                addRows(storage.get(DataController.tagIntervals_fri));
                tagNow = DataController.tagIntervals_fri;
                break;
            }
            case 5:
            {
                addRows(storage.get(DataController.tagIntervals_sat));
                tagNow = DataController.tagIntervals_sat;
                break;
            }
            case 6:
            {
                addRows(storage.get(DataController.tagIntervals_sun));
                tagNow = DataController.tagIntervals_sun;
                break;
            }
        }
    }
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentIntervals newInstance(int sectionNumber) {
        FragmentIntervals fragment = new FragmentIntervals();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentIntervals() {
    }

    public void dialogShow(final View v) {
        Dialog.Builder builder = null;
        switch (v.getId()) {
            case R.id.button_bt_float_color:
                builder = new TimePickerDialog.Builder(6, 0) {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        TimePickerDialog dialog = (TimePickerDialog) fragment.getDialog();

                        super.onPositiveActionClicked(fragment);
                        if (t1==null) {
                            t1 = new Time();
                            Toast.makeText(getActivity(), "Start time " + dialog.getFormattedTime(SimpleDateFormat.getTimeInstance()), Toast.LENGTH_SHORT).show();
                            t1.set(0, getMinute(), getHour(), 0, 0, 0);
                            dialogShow(v);
                        } else {
                            if (t2 == null){
                                t2 = new Time();
                                Toast.makeText(getActivity(), "Finish time " + dialog.getFormattedTime(SimpleDateFormat.getTimeInstance()), Toast.LENGTH_SHORT).show();
                                t2.set(0, getMinute(), getHour(), 0, 0, 0);

                                int low= t1.hour*60+t1.minute;
                                int high = t2.hour*60+t2.minute;
                                if (low>=high){
                                    t1 = null;
                                    t2 = null;
                                    Toast.makeText(getActivity(), "Incorrect times! The first time should be lower then the last one.", Toast.LENGTH_LONG).show();
                                } else {
                                    nowday = spn_label.getSelectedItemPosition();
                                    switch (spn_label.getSelectedItemPosition()) {
                                        case 0:

                                            controller.addIntervalMon(t1, t2);
                                            controller.updateChangesMon();
                                            break;
                                        case 1:
                                            controller.addIntervalTue(t1, t2);
                                            controller.updateChangesTue();
                                            break;
                                        case 2:
                                            controller.addIntervalWed(t1, t2);
                                            controller.updateChangesWed();
                                            break;
                                        case 3:
                                            controller.addIntervalThu(t1, t2);
                                            controller.updateChangesThu();
                                            break;
                                        case 4:
                                            controller.addIntervalFri(t1, t2);
                                            controller.updateChangesFri();
                                            break;
                                        case 5:
                                            controller.addIntervalSat(t1, t2);
                                            controller.updateChangesSat();
                                            break;
                                        case 6:
                                            controller.addIntervalSun(t1, t2);
                                            controller.updateChangesSun();
                                            break;

                                    }
                                    REALLOADDAY(nowday);
                                }
                                _hRedraw.sendEmptyMessage(REFRESH);;
                                t1 = null;
                                t2 = null;
                            }
                        }

                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        t1 = null;
                        t2 = null;
                        Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.positiveAction("OK")
                        .negativeAction("CANCEL");
                break;
        }
        DialogFragment fragment = DialogFragment.newInstance(builder);
//        if (builder != null) {
//            builder.getDialog().setTitle(t1 == null ? " 3" : " 4");
//        }
        fragment.show(getFragmentManager(), null);
    }
    private void redrawEverything(){
        interTable.invalidate();
        interTable.refreshDrawableState();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_intervals, container, false);
        ButterKnife.inject(getActivity());
        FloatingActionButton bt_time_light = (FloatingActionButton)rootView.findViewById(R.id.button_bt_float_color);


        interTable = (TableLayout)rootView.findViewById(R.id.intervals_table);
        spn_label = (Spinner)rootView.findViewById(R.id.spinner_label);
        _hRedraw=new Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                switch (msg.what) {
                    case REFRESH:
                        redrawEverything();
                        break;
                }
            }
        };




        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.days_array, R.layout.row_spn);
        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        spn_label.setAdapter(adapter);

        spn_label.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner spinner, View view, int i, long l) {

                loadDay(i);


                switch(i){
                    case 0:
                    {
                        addRows(storage.get(DataController.tagIntervals_mon));
                        break;
                    }
                    case 1:
                    {
                        addRows(storage.get(DataController.tagIntervals_tue));
                        break;
                    }
                    case 2:
                    {
                        addRows(storage.get(DataController.tagIntervals_wed));
                        break;
                    }
                    case 3:
                    {
                        addRows(storage.get(DataController.tagIntervals_thu));
                        break;
                    }
                    case 4:
                    {
                        addRows(storage.get(DataController.tagIntervals_fri));
                        break;
                    }
                    case 5:
                    {
                        addRows(storage.get(DataController.tagIntervals_sat));
                        break;
                    }
                    case 6:
                    {
                        addRows(storage.get(DataController.tagIntervals_sun));
                        break;
                    }
                }
            }
        });


        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(v instanceof FloatingActionButton){
                    dialogShow(v);
//                    dialogShow(v);
                }

                System.out.println(v + " " + ((RippleDrawable)v.getBackground()).getDelayClickType());

            }
        };
        btn = (FloatingActionButton)rootView.findViewById(R.id.button_bt_float_color);
        btn.setOnClickListener(listener);

        return rootView;
    }


    private void addRows(Vector<DataController.Interval>intervals){
        if(intervals.size()>=5)
            btn.setVisibility(View.INVISIBLE);
        else
            btn.setVisibility(View.VISIBLE);
        interTable.removeAllViews();
        for(final DataController.Interval inter:intervals){
            final DataController.Interval interval = inter;
            LayoutInflater li=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View tableRow = li.inflate(R.layout.intervals_row, null);
            TextView tv = (TextView)tableRow.findViewById(R.id.interbals_row_timeTV);
            tv.setText(inter.toString());
            Button delete = (Button)tableRow.findViewById(R.id.intervals_row_button);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new AlertDialog.Builder(FragmentIntervals.this.getActivity()).setMessage(interval.toString()).show();
                    controller.DELETE(interval);
                    REALLOADDAY(nowday);
                }
            });

            interTable.addView(tableRow);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.intervals_row_button){
        }
    }
}