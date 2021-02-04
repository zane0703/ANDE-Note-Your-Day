package sg.LIZ.assignment1.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.GregorianCalendar;

import sg.LIZ.assignment1.Key;

public class ChangeDate extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GregorianCalendar gregorianCalendar =new GregorianCalendar();
        Key.currentYear =  gregorianCalendar.get(GregorianCalendar.YEAR);
        Key.currentMonth = (byte) gregorianCalendar.get(GregorianCalendar.MONTH);
        Key.currentDay = (byte) gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH);

    }
}
