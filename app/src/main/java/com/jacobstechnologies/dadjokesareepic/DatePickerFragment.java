package com.jacobstechnologies.dadjokesareepic;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.sql.Date;
import java.util.Calendar;

import static com.jacobstechnologies.dadjokesareepic.MainActivity.sharedPreferences;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 20);
        c.set(1973, Calendar.MAY, Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);


    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        sharedPreferences =  getActivity().getSharedPreferences(getString(R.string.sharedPrefs), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("monthOfBday", month);
        editor.putInt("dayOfBday", day);
        editor.putInt("yearOfBday", year);
        editor.commit();
        MainActivity.SetAlarmForOccasion(getActivity(), month, day);
        SettingsActiv.resetShit();
    }

}
