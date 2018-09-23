package com.example.amgsoft_pc.bestprojetgla.autre;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private int heures, minutes;
    private TextView timeView;

    public TimePickerFragment() {
        final Calendar c = Calendar.getInstance();
        heures = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);
    }

    public void setTime(int heures, int minutes) {
        this.heures = heures;
        this.minutes = minutes;
    }

    public void setTextView(TextView timeView) {
        this.timeView = timeView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, heures, minutes, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int heures, int minutes) {
        this.heures = heures;
        this.minutes = minutes;

        timeView.setText(timeToText(heures, minutes));
    }

    public String getNow() {
        return timeToText(heures, minutes);
    }

    private String timeToText(int heure, int minutes) {
        return Sortie.heureToString(heure, minutes);
    }
}