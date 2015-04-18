package ru.aesalon.thermostat;

import java.util.Locale;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position){
                case 0:
                    return CurrentTempFragment.newInstance(position + 1);
                case 1:
                    return FragmentDayNight.newInstance(0);
                case 2:
                    return FragmentIntervals.newInstance(0);

            }
            return CurrentTempFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CurrentTempFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        TextView tvSeekBarSt;
        TextView tvCancel;
        TextView tvConfirm;
        ImageButton btnMinus;
        ImageButton btnPlus;
        ProgressBar progressBar;
        SeekBar seekBar;
        private boolean mLongClickPlus;

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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_cur_temp, container, false);
            tvSeekBarSt = (TextView) rootView.findViewById(R.id.textViewSeekBarStatus);
            tvCancel = (TextView) rootView.findViewById(R.id.textViewCancel);
            tvConfirm = (TextView) rootView.findViewById(R.id.textViewConfirm);
            btnMinus = (ImageButton) rootView.findViewById(R.id.buttonMinus);
            btnPlus = (ImageButton) rootView.findViewById(R.id.buttonPlus);
            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            seekBar =(SeekBar) rootView.findViewById(R.id.seekBar);

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
                    // TODO Auto-generated method stub
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
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // TODO Auto-generated method stub
                    int dig = progress / 10 + 5;
                    int dig2 = progress % 10;
                    tvSeekBarSt.setText("Change to " + dig + "." + dig2 + " °C");
                }
            });

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: disable changes, make btnPlus, btnMinus and confirm and cancel buttons invisible
                }
            });
            tvConfirm.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
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



}
