package ru.aesalon.thermostat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Nikita on 31.05.2015 .
 */
public class DataController {

    private static int timeValue(Time t) {
        return t.hour * 60 + t.minute;
    }

    public static void addElement(Vector<Interval> vec, Interval value) {
        boolean[] used = new boolean[24 * 60 + 1];
        for (int i = 0; i < used.length; ++i)
            used[i] = false;

        for (Interval i : vec) {
            int start = timeValue(i.tm1);
            int end = timeValue(i.tm2);
            for (int j = start; j <= end; ++j)
                used[j] = true;
        }

        int start = timeValue(value.tm1);
        int end = timeValue(value.tm2);
        for (int j = start; j <= end; ++j)
            used[j] = true;

        int i = 0;
        vec.clear();
        while (i < used.length) {
            if (used[i]) {
                int start_time = i;
                while (used[i]) i++;
                int end_time = i - 1;
                Time startTime = new Time();
                Time endTime = new Time();
                startTime.hour = start_time / 60;
                startTime.minute = start_time % 60;
                endTime.hour = end_time / 60;
                endTime.minute = end_time % 60;
                vec.add(new Interval(startTime, endTime));
            } else {
                i++;
            }
        }
        /*int index_start = 0;
        int index_end = 0;

        while (index_start < vec.size() && timeValue(vec.elementAt(index_start).tm2) < timeValue(value.tm1))
            index_start++;
        while (index_end < vec.size() && timeValue(vec.elementAt(index_end).tm2) < timeValue(value.tm2))
            index_end++;
        if (index_start == index_end) {
            if (index_start < vec.size() && )
            vec.add(index_start, value);
        } else if (index_start < index_end) {
//            int end_time = Math.max(timeValue(value.tm2), timeValue(vec.elementAt(index_end).tm2));
            int end_time = timeValue(value.tm2);
            if (index_end < vec.size() && timeValue(vec.elementAt(index_end).tm2) > end_time)
                end_time = timeValue(vec.elementAt(index_end).tm2);
            while (index_start != index_end) {
                vec.remove(index_start);
                index_end--;
            }
            value.tm2.hour = end_time / 60;
            value.tm2.minute = end_time % 60;
            vec.add(index_start, value);*/

    }

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


    public static DataController getInstance(Activity activity) {
        if (dataController != null) {
            return dataController;
        } else {
            dataController = new DataController(activity);

            return dataController;
        }
    }

    private DataController(Activity activity) {
        this.activity = activity;
        intervals_mon = new Vector<>();
        intervals_tue = new Vector<>();
        intervals_wed = new Vector<>();
        intervals_thu = new Vector<>();
        intervals_fri = new Vector<>();
        intervals_sat = new Vector<>();
        intervals_sun = new Vector<>();
        Context context = activity;
        sharedPref = context.getSharedPreferences(
                activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        for (int i = 0; i < 7; i++) {
            loadSaved(i);
        }


    }

    public boolean isDay(int day, Time tm) {
        day -= 2;
        if (day == -1) {
            day = 6;
        }
        switch (day) {
            case 0:
                return searchInterval(tm, intervals_mon);
            case 1:
                return searchInterval(tm, intervals_tue);
            case 2:
                return searchInterval(tm, intervals_wed);
            case 3:
                return searchInterval(tm, intervals_thu);
            case 4:
                return searchInterval(tm, intervals_fri);
            case 5:
                return searchInterval(tm, intervals_sat);
            case 6:
                return searchInterval(tm, intervals_sun);

        }

        return false;
    }

    private boolean searchInterval(Time tm, Vector<Interval> intervals) {
        for (Interval inter : intervals) {
            int compare = tm.hour * 60 + tm.minute;
            int low = inter.tm1.hour * 60 + inter.tm1.minute;
            int high = inter.tm2.hour * 60 + inter.tm2.minute;
            if (low < compare && compare < high) {
                return true;
            }
        }
        return false;
    }

    public void updateChangesMon() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_mon, Interval.convertToSet(intervals_mon));
        editor.commit();
    }

    public void DELETE(Interval interval) {
        if (intervals_mon != null) {
            intervals_mon.remove(interval);
            updateChangesMon();
        }
        if (intervals_tue != null) {
            intervals_tue.remove(interval);
            updateChangesTue();
        }
        if (intervals_wed != null) {
            intervals_wed.remove(interval);
            updateChangesWed();
        }
        if (intervals_thu != null) {
            intervals_thu.remove(interval);
            updateChangesThu();
        }
        if (intervals_fri != null) {
            intervals_fri.remove(interval);
            updateChangesFri();
        }
        if (intervals_sat != null) {
            intervals_sat.remove(interval);
            updateChangesSat();
        }
        if (intervals_sun != null) {
            intervals_sun.remove(interval);
            updateChangesSun();
        }
    }

    public void deleteDay(int day) {
        switch (day) {
            case 0:
                intervals_mon = new Vector<Interval>();
                updateChangesMon();
                break;
            case 1:
                intervals_tue = new Vector<Interval>();
                updateChangesTue();
                break;
            case 2:
                intervals_wed = new Vector<Interval>();
                updateChangesWed();
                break;
            case 3:
                intervals_thu = new Vector<Interval>();
                updateChangesThu();
                break;
            case 4:
                intervals_fri = new Vector<Interval>();
                updateChangesFri();
                break;
            case 5:
                intervals_sat = new Vector<Interval>();
                updateChangesSat();
                break;
            case 6:
                intervals_sun = new Vector<Interval>();
                updateChangesSun();
                break;


        }
    }

    public void addIntervalMon(Time tm1, Time tm2) {
        if (intervals_mon == null) {
            intervals_mon = new Vector<>();
        }
        addElement(intervals_mon, new Interval(tm1, tm2));
//        intervals_mon.add(new Interval(tm1, tm2));
    }

    public void updateChangesWed() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_wed, Interval.convertToSet(intervals_wed));
        editor.commit();
    }

    public void addIntervalWed(Time tm1, Time tm2) {
        if (intervals_wed == null) {
            intervals_wed = new Vector<>();
        }
        addElement(intervals_wed, new Interval(tm1, tm2));
//        intervals_wed.add(new Interval(tm1, tm2));
    }

    public void updateChangesThu() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_thu, Interval.convertToSet(intervals_thu));
        editor.commit();
    }

    public void addIntervalThu(Time tm1, Time tm2) {
        if (intervals_thu == null) {
            intervals_thu = new Vector<>();
        }
        addElement(intervals_thu, new Interval(tm1, tm2));
//        intervals_thu.add(new Interval(tm1, tm2));
    }

    public void updateChangesFri() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_fri, Interval.convertToSet(intervals_fri));
        editor.commit();
    }

    public void addIntervalFri(Time tm1, Time tm2) {
        if (intervals_fri == null) {
            intervals_fri = new Vector<>();
        }
        addElement(intervals_fri, new Interval(tm1, tm2));
//        intervals_fri.add(new Interval(tm1, tm2));
    }

    public void updateChangesSat() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_sat, Interval.convertToSet(intervals_sat));
        editor.commit();
    }

    public void addIntervalSat(Time tm1, Time tm2) {
        if (intervals_sat == null) {
            intervals_sat = new Vector<>();
        }
        addElement(intervals_sat, new Interval(tm1, tm2));
//        intervals_sat.add(new Interval(tm1, tm2));
    }

    public void updateChangesSun() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_sun, Interval.convertToSet(intervals_sun));
        editor.commit();
    }

    public void addIntervalSun(Time tm1, Time tm2) {
        if (intervals_sun == null) {
            intervals_sun = new Vector<>();
        }
        addElement(intervals_sun, new Interval(tm1, tm2));
//        intervals_sun.add(new Interval(tm1, tm2));
    }

    public void updateChangesTue() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(tagIntervals_tue, Interval.convertToSet(intervals_tue));
        editor.commit();
    }

    public void addIntervalTue(Time tm1, Time tm2) {
        if (intervals_tue == null) {
            intervals_tue = new Vector<>();
        }
        addElement(intervals_tue, new Interval(tm1, tm2));
//        intervals_tue.add(new Interval(tm1, tm2));
    }


    public Vector<Interval> getIntervals(int i) {
        loadSaved(i);
        switch (i) {
            case 0:
                return intervals_mon;
            case 1:
                return intervals_tue;
            case 2:
                return intervals_wed;
            case 3:
                return intervals_thu;
            case 4:
                return intervals_fri;
            case 5:
                return intervals_sat;
            case 6:
                return intervals_sun;
        }
        return intervals_mon;
    }

    public void loadSaved(int day) {

        Set<String> def = new HashSet<>();
        switch (day) {
            case 0: {
                def = sharedPref.getStringSet(tagIntervals_mon, def);
                intervals_mon = Interval.convertToInterval(def);
                break;
            }
            case 1: {
                def = sharedPref.getStringSet(tagIntervals_tue, def);
                intervals_tue = Interval.convertToInterval(def);
                break;
            }
            case 2: {
                def = sharedPref.getStringSet(tagIntervals_wed, def);
                intervals_wed = Interval.convertToInterval(def);
                break;
            }
            case 3: {
                def = sharedPref.getStringSet(tagIntervals_thu, def);
                intervals_thu = Interval.convertToInterval(def);
                break;
            }
            case 4: {
                def = sharedPref.getStringSet(tagIntervals_fri, def);
                intervals_fri = Interval.convertToInterval(def);
                break;
            }
            case 5: {
                def = sharedPref.getStringSet(tagIntervals_sat, def);
                intervals_sat = Interval.convertToInterval(def);
                break;
            }
            case 6: {
                def = sharedPref.getStringSet(tagIntervals_sun, def);
                intervals_sun = Interval.convertToInterval(def);
                break;
            }
        }
    }


    public static class Interval {
        public Time tm1, tm2;

        public Interval(Time tm1, Time tm2) {
            this.tm1 = tm1;
            this.tm2 = tm2;
        }

        public static Set<String> convertToSet(Vector<Interval> intervals) {
            Set<String> convert = new LinkedHashSet<String>();
            for (Interval in : intervals) {
                convert.add(in.toString());
            }
            return convert;
        }

        public static Vector<Interval> convertToInterval(Set<String> sets) {
            Vector<Interval> convert = new Vector<>();
            for (String in : sets) {
                Time time1 = new Time();
                Time time2 = new Time();
                time1.set(0, Integer.parseInt(in.substring(3, 5)), Integer.parseInt(in.substring(0, 2)), 0, 0, 0);
                time2.set(0, Integer.parseInt(in.substring(11, 13)), Integer.parseInt(in.substring(8, 10)), 0, 0, 0);
                addElement(convert, new Interval(time1, time2));
//                convert.add(new Interval(time1, time2));
            }
            return convert;
        }

        @Override
        public String toString() {
            return (tm1.hour < 10 ? "0" + tm1.hour : tm1.hour).toString()
                    + ":"
                    + (tm1.minute < 10 ? "0" + tm1.minute : tm1.minute).toString()
                    + " - "
                    + (tm2.hour < 10 ? "0" + tm2.hour : tm2.hour).toString() + ":"
                    + (tm2.minute < 10 ? "0" + tm2.minute : tm2.minute).toString();
        }

    }

}
