package ru.aesalon.thermostat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Nikita on 18.04.2015.
 */
public class FragmentDayNight extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private TextView tvTemp;
    private TextView tvNightTemp;
    private TextView tvCancel;
    private TextView tvCancelNight;
    private TextView tvConfirm;
    private TextView tvConfirmNight;
    private SeekBar seekBarDay;
    private SeekBar seekBarNight;
    private ImageButton btnIncrTempDay;
    private ImageButton btnDecrTempDay;
    private ImageButton btnIncrTempNight;
    private ImageButton btnDecrTempNight;

    private boolean mLongClickPlus;
    private static int prevSeekBarProgressDay;
    private static int prevSeekBarProgressNight;
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentDayNight newInstance(int sectionNumber) {
        FragmentDayNight fragment = new FragmentDayNight();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentDayNight() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_daynight, container, false);
        tvTemp=(TextView) rootView.findViewById(R.id.tvTemp);
        tvNightTemp=(TextView) rootView.findViewById(R.id.tvNightTemp);
        tvCancel=(TextView) rootView.findViewById(R.id.tvCancel);
        tvConfirm=(TextView) rootView.findViewById(R.id.tvConfirm);
        tvCancelNight=(TextView) rootView.findViewById(R.id.tvCancelNight);
        tvConfirmNight=(TextView) rootView.findViewById(R.id.tvConfirmNight);
        seekBarDay = (SeekBar)rootView.findViewById(R.id.seekBarDay);
        seekBarNight=(SeekBar)rootView.findViewById(R.id.seekBarNight);
        btnDecrTempDay=(ImageButton)rootView.findViewById(R.id.btnReduceTempDay);
        btnDecrTempNight=(ImageButton)rootView.findViewById(R.id.btnReduceTemp);
        btnIncrTempDay=(ImageButton)rootView.findViewById(R.id.btnIncrTempDay);
        btnIncrTempNight=(ImageButton)rootView.findViewById(R.id.btnIncrTemp);

        tvCancel.setVisibility(View.INVISIBLE);
        tvConfirm.setVisibility(View.INVISIBLE);
        btnDecrTempDay.setVisibility(View.INVISIBLE);
        btnIncrTempDay.setVisibility(View.INVISIBLE);
        tvCancelNight.setVisibility(View.INVISIBLE);
        tvConfirmNight.setVisibility(View.INVISIBLE);
        btnDecrTempNight.setVisibility(View.INVISIBLE);
        btnIncrTempNight.setVisibility(View.INVISIBLE);

        seekBarDay.setProgress(MainActivity.dayTemp);
        seekBarNight.setProgress(MainActivity.nightTemp);
        int dig = seekBarDay.getProgress() / 10 + 5;
        int dig2 = seekBarDay.getProgress()  % 10;
        tvTemp.setText("" + dig + "." + dig2 + " \u00b0C");
        dig = seekBarNight.getProgress()  / 10 + 5;
        dig2 = seekBarNight.getProgress()  % 10;
        tvNightTemp.setText("" + dig + "." + dig2 + " \u00b0C");

        seekBarDay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                tvCancel.setVisibility(View.VISIBLE);
                tvConfirm.setVisibility(View.VISIBLE);
                btnDecrTempDay.setVisibility(View.VISIBLE);
                btnIncrTempDay.setVisibility(View.VISIBLE);
                if (tvCancel.getVisibility() == View.INVISIBLE) {
                    tvCancel.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
                    tvConfirm.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
                    btnDecrTempDay.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
                    btnIncrTempDay.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                prevSeekBarProgressDay = seekBar.getProgress();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                int dig = progress / 10 + 5;
                int dig2 = progress % 10;
                tvTemp.setText("" + dig + "." + dig2 + " \u00b0C");
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnIncrTempDay.setVisibility(View.INVISIBLE);
                btnDecrTempDay.setVisibility(View.INVISIBLE);
                tvCancel.setVisibility(View.INVISIBLE);
                tvConfirm.setVisibility(View.INVISIBLE);
                seekBarDay.setProgress(prevSeekBarProgressDay);

            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.dayTemp = seekBarDay.getProgress();
                ((MainActivity) getActivity()).saveTemp();
                btnIncrTempDay.setVisibility(View.INVISIBLE);
                btnDecrTempDay.setVisibility(View.INVISIBLE);
                tvCancel.setVisibility(View.INVISIBLE);
                tvConfirm.setVisibility(View.INVISIBLE);
                //TODO:initiate temperature changing
            }
        });
        btnIncrTempDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarDay.setProgress(seekBarDay.getProgress() + 1);
            }
        });
        btnIncrTempDay.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                mLongClickPlus = true;
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        int timeSleep = 200;
                        while (mLongClickPlus) {
                            seekBarDay.setProgress(seekBarDay.getProgress() + 1);
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
        btnIncrTempDay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mLongClickPlus = false;
                }
                return false;
            }
        });
        btnDecrTempDay.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                mLongClickPlus = true;
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        int timeSleep = 200;
                        while (mLongClickPlus) {
                            seekBarDay.setProgress(seekBarDay.getProgress() - 1);
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
        btnDecrTempDay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mLongClickPlus = false;
                }
                return false;
            }
        });
        btnDecrTempDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarDay.setProgress(seekBarDay.getProgress() - 1);
            }
        });








        seekBarNight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                tvCancelNight.setVisibility(View.VISIBLE);
                tvConfirmNight.setVisibility(View.VISIBLE);
                btnDecrTempNight.setVisibility(View.VISIBLE);
                btnIncrTempNight.setVisibility(View.VISIBLE);
                if (tvCancelNight.getVisibility() == View.INVISIBLE) {
                    tvCancelNight.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
                    tvConfirmNight.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
                    btnDecrTempNight.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
                    btnIncrTempNight.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                prevSeekBarProgressNight = seekBarNight.getProgress();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                int dig = progress / 10 + 5;
                int dig2 = progress % 10;
                tvNightTemp.setText("" + dig + "." + dig2 + " \u00b0C");
            }
        });

        tvCancelNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnIncrTempNight.setVisibility(View.INVISIBLE);
                btnDecrTempNight.setVisibility(View.INVISIBLE);
                tvCancelNight.setVisibility(View.INVISIBLE);
                tvConfirmNight.setVisibility(View.INVISIBLE);
                seekBarNight.setProgress(prevSeekBarProgressNight);

            }
        });

        tvConfirmNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.nightTemp = seekBarNight.getProgress();
                        ((MainActivity) getActivity()).saveTemp();
                btnIncrTempNight.setVisibility(View.INVISIBLE);
                btnDecrTempNight.setVisibility(View.INVISIBLE);
                tvCancelNight.setVisibility(View.INVISIBLE);
                tvConfirmNight.setVisibility(View.INVISIBLE);
                //TODO:initiate temperature changing
            }
        });
        btnIncrTempNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarNight.setProgress(seekBarNight.getProgress() + 1);
            }
        });
        btnIncrTempNight.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                mLongClickPlus = true;
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        int timeSleep = 200;
                        while (mLongClickPlus) {
                            seekBarNight.setProgress(seekBarNight.getProgress() + 1);
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
        btnIncrTempNight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mLongClickPlus = false;
                }
                return false;
            }
        });
        btnDecrTempNight.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                mLongClickPlus = true;
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        int timeSleep = 200;
                        while (mLongClickPlus) {
                            seekBarNight.setProgress(seekBarNight.getProgress() - 1);
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
        btnDecrTempNight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mLongClickPlus = false;
                }
                return false;
            }
        });
        btnDecrTempNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarNight.setProgress(seekBarNight.getProgress() - 1);
            }
        });
        return rootView;
    }

}