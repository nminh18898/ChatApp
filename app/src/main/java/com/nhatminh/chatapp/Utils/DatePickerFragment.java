package com.nhatminh.chatapp.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    final int USER_MIN_AGES = 18;
    Calendar mCalendar;
    int mYear;
    int mMonth;
    int mDay;

    OnDateSelectedListener mCallback;


    public DatePickerFragment() {

        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR) - USER_MIN_AGES;
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnDateSelectedListener)
        {
            mCallback = (OnDateSelectedListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()+"must implement OnSelectedDateListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if(getActivity() == null) {
            return null;
        }


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month;
        mDay = dayOfMonth;

        mCalendar.set(year, month, dayOfMonth);
        mCallback.selectedDate(mCalendar.getTime());

    }


    @Override
    public void onDetach() {
        super.onDetach();
        if(mCallback != null)
        {
            mCallback = null;
        }
    }

    public interface OnDateSelectedListener{
         void selectedDate (Date selectedDate);
    }
}
