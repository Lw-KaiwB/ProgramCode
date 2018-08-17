package com.kb.criminalitent.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.kb.criminalitent.R;
import com.kb.criminalitent.view.CustomTimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "com.kb.criminaliten.extra_date";
    private static final String EXTRA_DATE_ARG = "extra_date_agr";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(EXTRA_DATE_ARG);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_date, null);
        final DatePicker datePicker = view.findViewById(R.id.date_picker);
        datePicker.init(year, month, day, null);
        final CustomTimePicker timePicker = view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hour);
            timePicker.setMinute(min);
        } else {
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(min);
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.date_picker_title)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int mYear = datePicker.getYear();
                        int mMonth = datePicker.getMonth();
                        int mDay = datePicker.getDayOfMonth();
                        int mHour;
                        int mMin;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            mHour = timePicker.getHour();
                            mMin = timePicker.getMinute();
                        } else {
                            mHour = timePicker.getChildCount();
                            mMin = timePicker.getCurrentMinute();
                        }

                        Date mDate = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMin).getTime();
                        setResult(Activity.RESULT_OK, mDate);
                    }
                })
                .create();
    }

    public static DatePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DATE_ARG, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
