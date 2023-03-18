package com.example.yourvet.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Date;
public class WorkDay {
    private String start_time;
    private String end_time;

    public WorkDay(String start_time, String end_time) {
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public WorkDay() {
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
    public ArrayList<WorkDay> getNonBusyIntervals(int time, ArrayList<WorkDay> busy_intervals) throws ParseException {
        ArrayList<WorkDay> non_busy_intervals = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        Date start_time = formatter.parse(this.start_time);
        Date end_time = formatter.parse(this.end_time);
        long time_in_millis = time * 60 * 1000;
        for (long t = start_time.getTime(); t + time_in_millis <= end_time.getTime(); t += time_in_millis) {
            Date interval_start = new Date(t);
            Date interval_end = new Date(t + time_in_millis);
            boolean is_busy = false;
            for (WorkDay busy_interval : busy_intervals) {
                Date busy_start = formatter.parse(busy_interval.start_time);
                Date busy_end = formatter.parse(busy_interval.end_time);
                if (interval_start.before(busy_end) && busy_start.before(interval_end)) {
                    is_busy = true;
                    break;
                }
            }
            if (!is_busy) {
                non_busy_intervals.add(new WorkDay(formatter.format(interval_start), formatter.format(interval_end)));
            }
        }
        return non_busy_intervals;
    }

    @Override
    public String toString() {
        return "WorkDay{" +
                "start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                '}';
    }
    public ArrayList<String> getSubIntervals(int t) {
        // Extract the start and end times from the interval object
        String startTime = this.getStart_time();
        String endTime =this.getEnd_time();

        // Parse the start and end times into hours and minutes
        int startHour = Integer.parseInt(startTime.substring(0, 2));
        int startMinute = Integer.parseInt(startTime.substring(3, 5));
        int endHour = Integer.parseInt(endTime.substring(0, 2));
        int endMinute = Integer.parseInt(endTime.substring(3, 5));

        // If the end time is before the start time, add 12 hours to the end time
        if (endHour < startHour || (endHour == startHour && endMinute < startMinute)) {
            endHour += 12;
        }

        // Calculate the total number of minutes in the interval
        int totalMinutes = (endHour * 60 + endMinute) - (startHour * 60 + startMinute);

        // Calculate the number of subintervals
        int numSubIntervals = totalMinutes / t;

        // Create an ArrayList to hold the subintervals
        ArrayList<String> subIntervals = new ArrayList<String>();

        // Initialize the start time to the beginning of the interval
        int currentHour = startHour;
        int currentMinute = startMinute;

        // Loop through the subintervals and calculate their start and end times
        for (int j = 0; j < numSubIntervals; j++) {
            // Calculate the end time of the subinterval
            int subIntervalEndMinute = currentMinute + t;
            int subIntervalEndHour = currentHour + subIntervalEndMinute / 60;
            subIntervalEndMinute %= 60;

            // If the subinterval crosses over from AM to PM or vice versa, adjust the end hour
            if ((currentHour < 12 && subIntervalEndHour >= 12) || (currentHour >= 12 && subIntervalEndHour < 12)) {
                subIntervalEndHour = (subIntervalEndHour + 12) % 24;
            }
            // Format the start and end times of the subinterval as strings
            String subIntervalStartTime = String.format("%02d:%02d %s", currentHour % 12, currentMinute, currentHour < 12 ? "AM" : "PM");
            String subIntervalEndTime = String.format("%02d:%02d %s", subIntervalEndHour % 12, subIntervalEndMinute, subIntervalEndHour < 12 ? "AM" : "PM");
            // Add the subinterval to the ArrayList
            subIntervals.add(subIntervalStartTime + " - " + subIntervalEndTime);

            // Update the current time to the start of the next subinterval
            currentMinute += t;
            currentHour += currentMinute / 60;
            currentMinute %= 60;
        }

        return subIntervals;
    }
}
