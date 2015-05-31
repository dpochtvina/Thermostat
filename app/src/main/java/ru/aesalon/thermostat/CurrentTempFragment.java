package ru.aesalon.thermostat;

/**
 * Created by Nikita on 31.05.2015.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;
import com.rey.material.widget.Switch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A placeholder fragment containing a simple view.
 */
public class CurrentTempFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static int timeBoost = 500;
    public static int smooth = 100;
    TextView tvCl;
    TextView tvSeekBarSt;
    int prevSeekBarProgress=0;
    TextView tvCancel;
    TextView tvConfirm;
    Switch switchVacation;

    TextView tvCurTemp;
    ImageButton btnMinus;
    ImageButton btnPlus;
    ProgressBar progressBar;
    SeekBar seekBar;
    TextClock textClock;
    private boolean mLongClickPlus;
    GregorianCalendar gregorianCalendar = new GregorianCalendar();
    Thread tempChange;

    private final SimpleDateFormat _sdfWatchTime = new SimpleDateFormat("EEEE HH:mm:ss");
    private TextView _tvTime;

    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CurrentTempFragment newInstance(int sectionNumber) {
        CurrentTempFragment fragment = new CurrentTempFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CurrentTempFragment() {
    }

    @Override
    public void onStart(){
        super.onStart();
        TimeZone timezone = TimeZone.getDefault();
       gregorianCalendar = new GregorianCalendar(timezone);

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep((int)(1000/timeBoost));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String hour = gregorianCalendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0"+gregorianCalendar.get(Calendar.HOUR_OF_DAY) : gregorianCalendar.get(Calendar.HOUR_OF_DAY)+"";
                                String minute = gregorianCalendar.get(Calendar.MINUTE) < 10 ? "0"+gregorianCalendar.get(Calendar.MINUTE) : gregorianCalendar.get(Calendar.MINUTE)+"";
                                String second = gregorianCalendar.get(Calendar.SECOND) < 10 ? "0"+gregorianCalendar.get(Calendar.SECOND) : gregorianCalendar.get(Calendar.SECOND)+"";
                                _tvTime.setText(" "+gregorianCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
                                +hour + ":"
                                        + minute +":"+
                                second);
                                gregorianCalendar.add(Calendar.SECOND, timeBoost/smooth);
                             }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
        tempChange = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted() ) {
                        Thread.sleep((int)(50));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if( MainActivity.desiredTemp!=MainActivity.currentTemp) {
                                    if (MainActivity.currentTemp < MainActivity.desiredTemp) {
                                        MainActivity.currentTemp++;
                                    } else {
                                        MainActivity.currentTemp--;
                                    }
                                    int dig = MainActivity.currentTemp / 10 + 5;
                                    int dig2 = MainActivity.currentTemp % 10;
                                    tvCurTemp.setText(dig + "." + dig2 + " \u00b0C");
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                                if (MainActivity.isIsDay){
                                    MainActivity.desiredTemp = MainActivity.dayTemp;

                                } else {
                                    MainActivity.desiredTemp = MainActivity.nightTemp;
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        tempChange.start();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cur_temp, container, false);
        tvSeekBarSt = (TextView) rootView.findViewById(R.id.textViewSeekBarStatus);
        tvCancel = (TextView) rootView.findViewById(R.id.textViewCancel);
        tvCl = (TextView) rootView.findViewById(R.id.tvCl);
        tvConfirm = (TextView) rootView.findViewById(R.id.textViewConfirm);
        btnMinus = (ImageButton) rootView.findViewById(R.id.buttonMinus);
        btnPlus = (ImageButton) rootView.findViewById(R.id.buttonPlus);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        seekBar =(SeekBar) rootView.findViewById(R.id.seekBar);
        _tvTime = (TextView)rootView.findViewById(R.id.tvCl);
        tvCurTemp = (TextView) rootView.findViewById(R.id.tvCurTemp);
        switchVacation = (Switch) rootView.findViewById(R.id.switchVacation);

        int dig = MainActivity.currentTemp / 10 + 5;
        int dig2 = MainActivity.currentTemp % 10;
        tvCurTemp.setText(dig + "." + dig2 + " \u00b0C");

        switchVacation.setChecked(MainActivity.vacation);
        switchVacation.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch aSwitch, boolean b) {
                MainActivity.vacation = b;
                ((MainActivity)getActivity()).saveVacation();
                ((MainActivity)getActivity()).loadVacation();

            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(seekBar.getProgress() + 1);
            }
        });
        btnPlus.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                mLongClickPlus = true;
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        int timeSleep = 200;
                        while (mLongClickPlus) {
                            seekBar.setProgress(seekBar.getProgress() + 1);
                            try {
                                Thread.sleep(timeSleep);
                                if (timeSleep > 30)
                                    timeSleep -= 30;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                }.execute();
                return true;
            }
        });
        btnPlus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mLongClickPlus = false;
                }
                return false;
            }
        });
        btnMinus.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                mLongClickPlus = true;
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        int timeSleep = 200;
                        while (mLongClickPlus) {
                            seekBar.setProgress(seekBar.getProgress() - 1);
                            try {
                                Thread.sleep(timeSleep);
                                if (timeSleep > 30)
                                    timeSleep -= 30;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                }.execute();
                return true;
            }
        });
        btnMinus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mLongClickPlus = false;
                }
                return false;
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(seekBar.getProgress() - 1);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                tvCancel.setVisibility(View.VISIBLE);
                tvConfirm.setVisibility(View.VISIBLE);
                btnMinus.setVisibility(View.VISIBLE);
                btnPlus.setVisibility(View.VISIBLE);
                if (tvCancel.getVisibility() == View.INVISIBLE) {
                    tvCancel.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
                    tvConfirm.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
                    btnMinus.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
                    btnPlus.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                prevSeekBarProgress = seekBar.getProgress();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                int dig = progress / 10 + 5;
                int dig2 = progress % 10;
                tvSeekBarSt.setText("Change to " + dig + "." + dig2 + " \u00b0C");
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlus.setVisibility(View.INVISIBLE);
                btnMinus.setVisibility(View.INVISIBLE);
                tvCancel.setVisibility(View.INVISIBLE);
                tvConfirm.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                seekBar.setProgress(prevSeekBarProgress);

            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity.desiredTemp=seekBar.getProgress();
                btnPlus.setVisibility(View.INVISIBLE);
                btnMinus.setVisibility(View.INVISIBLE);
                tvCancel.setVisibility(View.INVISIBLE);
                tvConfirm.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                //TODO:initiate temperature changing
            }
        });
        return rootView;
    }

}



