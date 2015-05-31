package ru.aesalon.thermostat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Nikita on 31.05.2015.
 */
public class DataController {

    private static DataController dataController;
    private Activity activity;
    private Vector<Interval> intervals_mon;
    private Vector<Interval> intervals_tue;
    private Vector<Interval> intervals_wed;
    private Vector<Interval> intervals_thu;
    private Vector<Interval> intervals_fri;
    private Vector<Interval> intervals_sat;
    private Vector<Interval> intervals_sun;

    SharedPreferences sharedPref;
    public static String tagIntervals_mon = "intervals_monday";
    public static String tagIntervals_tue = "intervals_tuesday";
    public static String tagIntervals_wed = "intervals_wednesday";
    public static String tagIntervals_thu = "intervals_thursday";
    public static String tagIntervals_fri = "intervals_friday";
    public static String tagIntervals_sat = "intervals_saturday";
    public static String tagIntervals_sun = "intervals_sunday";


    public static DataController getInstance(Activity activity){
        if(dataController!=null){
            return dataController;
        } else {
            dataController = new DataController(activity);
            return dataController;
        }
    }

    private DataController(Activity activity){
        this.activity= activity;

        Context context = activity;
        sharedPref = context.getSharedPreferences(
                activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE);



    }

    public void updateChangesMon(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_mon, Interval.convertToSet(intervals_mon));
        editor.commit();
    }

    public void addIntervalMon(Time tm1, Time tm2){
        if (intervals_mon==null){
            intervals_mon = new Vector<>();
        }
        intervals_mon.add(new Interval(tm1, tm2));
    }

    public void updateChangesWed(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_wed, Interval.convertToSet(intervals_wed));
        editor.commit();
    }

    public void addIntervalWed(Time tm1, Time tm2){
        if (intervals_wed==null){
            intervals_wed = new Vector<>();
        }
        intervals_wed.add(new Interval(tm1, tm2));
    }

    public void updateChangesThu(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_thu, Interval.convertToSet(intervals_thu));
        editor.commit();
    }

    public void addIntervalThu(Time tm1, Time tm2){
        if (intervals_thu==null){
            intervals_thu = new Vector<>();
        }
        intervals_thu.add(new Interval(tm1, tm2));
    }

    public void updateChangesFri(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_fri, Interval.convertToSet(intervals_fri));
        editor.commit();
    }

    public void addIntervalFri(Time tm1, Time tm2){
        if (intervals_fri==null){
            intervals_fri = new Vector<>();
        }
        intervals_fri.add(new Interval(tm1, tm2));
    }

    public void updateChangesSat(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_sat, Interval.convertToSet(intervals_sat));
        editor.commit();
    }

    public void addIntervalSat(Time tm1, Time tm2){
        if (intervals_sat==null){
            intervals_sat = new Vector<>();
        }
        intervals_sat.add(new Interval(tm1, tm2));
    }

    public void updateChangesSun(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_sun, Interval.convertToSet(intervals_sun));
        editor.commit();
    }

    public void addIntervalSun(Time tm1, Time tm2){
        if (intervals_sun==null){
            intervals_sun = new Vector<>();
        }
        intervals_sun.add(new Interval(tm1, tm2));
    }

    public void updateChangesTue(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_tue, Interval.convertToSet(intervals_tue));
        editor.commit();
    }

    public void addIntervalTue(Time tm1, Time tm2){
        if (intervals_tue==null){
            intervals_tue = new Vector<>();
        }
        intervals_tue.add(new Interval(tm1, tm2));
    }



    public Vector<Interval> getIntervals(int i){
        loadSaved(i);
        return intervals_mon;
    }

    public void loadSaved(int day){
        intervals_mon = new Vector<>();
        Set<String> def = new HashSet<>();
        switch(day) {
            case 0: {
                def =  sharedPref.getStringSet(tagIntervals_mon, def);
                intervals_mon = Interval.convertToInterval(def);
                break;
            }
            case 1: {
                def =  sharedPref.getStringSet(tagIntervals_tue, def);
                intervals_tue = Interval.convertToInterval(def);
                break;
            }
            case 2: {
                def =  sharedPref.getStringSet(tagIntervals_wed, def);
                intervals_wed = Interval.convertToInterval(def);
                break;
            }
            case 3: {
                def =  sharedPref.getStringSet(tagIntervals_thu, def);
                intervals_thu = Interval.convertToInterval(def);
                break;
            }
            case 4: {
                def =  sharedPref.getStringSet(tagIntervals_fri, def);
                intervals_fri = Interval.convertToInterval(def);
                break;
            }
            case 5: {
                def =  sharedPref.getStringSet(tagIntervals_sat, def);
                intervals_sat = Interval.convertToInterval(def);
                break;
            }
            case 6: {
                def =  sharedPref.getStringSet(tagIntervals_sun, def);
                intervals_sun = Interval.convertToInterval(def);
                break;
            }
        }
    }






    public static class Interval{
        public Time tm1, tm2;
        public Interval(Time tm1, Time tm2){
            this.tm1 = tm1;
            this.tm2 = tm2;
        }
        public static Set<String> convertToSet(Vector<Interval>intervals){
            Set<String>convert = new HashSet<String>();
            for(Interval in:intervals){
                convert.add(in.toString());
            }
            return convert;
        }

        public static Vector<Interval> convertToInterval(Set<String> sets){
            Vector<Interval> convert = new Vector<>();
            for(String in:sets){
                Time time1 = new Time();
                Time time2 = new Time();
                time1.set(0,Integer.parseInt(in.substring(3,5)), Integer.parseInt(in.substring(0,2)), 0, 0, 0);
                time2.set(0,Integer.parseInt(in.substring(11,13)), Integer.parseInt(in.substring(8,10)), 0, 0, 0);
                convert.add(new Interval(time1,time2));
            }
            return convert;
        }

        @Override
        public String toString(){
            return (tm1.hour < 10 ? "0"+tm1.hour : tm1.hour).toString()
                    +":"
                    +(tm1.minute < 10 ? "0"+tm1.minute : tm1.minute).toString()
                    +" - "
                    +(tm2.hour < 10 ? "0"+tm2.hour : tm2.hour).toString()+":"
                    +(tm2.minute < 10 ? "0"+tm2.minute : tm2.minute).toString();
        }

    }

}
