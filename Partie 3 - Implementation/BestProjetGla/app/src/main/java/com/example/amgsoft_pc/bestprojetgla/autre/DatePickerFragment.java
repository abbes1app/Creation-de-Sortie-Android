package com.example.amgsoft_pc.bestprojetgla.autre;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private int year, month, day;
    private TextView dateView;

    public DatePickerFragment() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setTextView(TextView dateView) {
        this.dateView = dateView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        dateView.setText(dateToString(year, month, day));
    }

    public String getToday() {
        return dateToString(year, month, day);
    }

    private String dateToString(int annee, int mois, int jours) {
        return Sortie.dateToString(jours, mois + 1, annee);
    }
}