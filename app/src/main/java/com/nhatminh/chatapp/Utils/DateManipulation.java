package com.nhatminh.chatapp.Utils;

import android.widget.Toast;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManipulation {

    static final String DATE_FORMAT = "dd/MM/yyyy";
    static SimpleDateFormat dateFormat;
    static SimpleDateFormat timestampFormat = new SimpleDateFormat("EEE, HH:mm");




    public DateManipulation() {
        dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setLenient(false);

    }

    public boolean isValidDate(String date){


        try {
            dateFormat.parse(date.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public Date stringToDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }

    public String dateToString(Date date) {
        return dateFormat.format(date);
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public String timestampToString(Timestamp timestamp){
        return timestampFormat.format(timestamp);
    }

}
