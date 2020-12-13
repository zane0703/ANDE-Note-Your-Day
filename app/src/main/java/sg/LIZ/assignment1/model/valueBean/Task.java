package sg.LIZ.assignment1.model.valueBean;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
public final class Task {
    public final int ID;
    public final byte DAY;
    public final byte MONTH;
    public final int YEAR;
    public final byte START_HOURS;
    public final byte START_MINUTES;
    public final byte END_HOURS;
    public final byte END_MINUTES;
    public final boolean ALL_DAY;
    public final String TITLE;
    public final String DESCRIPTION;
    public final String VENUE;

    public Task(@IntRange(from = 0) int id, byte day, byte month, @IntRange(from = -1) int year, byte startHours, byte startMinutes, byte endHours, byte endMinutes, boolean allDay, String title, @Nullable String description,@Nullable  String venue) {
        this.ID = id;
        this.DAY = day;
        this.MONTH = month;
        this.YEAR = year;
        this.START_HOURS = startHours;
        this.START_MINUTES = startMinutes;
        this.END_HOURS = endHours;
        this.END_MINUTES = endMinutes;
        this.ALL_DAY = allDay;
        this.TITLE = title;
        this.DESCRIPTION = description;
        this.VENUE = venue;
    }

    public Task(byte day, byte month, int year, byte startHours, byte startMinutes, byte endHours, byte endMinutes, boolean allDay, String title, String description, String venue) {
        this(0, day, month, year, startHours, startMinutes, endHours, endMinutes, allDay, title, description, venue);
    }

    public Task(@IntRange(from = 0)  int id, String title,byte startHours, byte startMinutes, byte endHours, byte endMinutes, boolean allDay) {
        this(id, (byte)-1, (byte)-1, (byte)-1, startHours, startMinutes, endHours, endMinutes, allDay, title, null, null);
    }
}
