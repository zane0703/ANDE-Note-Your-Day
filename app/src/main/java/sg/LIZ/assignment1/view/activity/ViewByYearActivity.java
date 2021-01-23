package sg.LIZ.assignment1.view.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Arrays;
import sg.LIZ.assignment1.R;

public class ViewByYearActivity extends AppCompatActivity {
    private final GregorianCalendar gregorianCalendar = new GregorianCalendar();
    private int selectedMonth = gregorianCalendar.get(Calendar.MONTH);
    private byte currentMonth = (byte) selectedMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_view_by_year);
    }

}
