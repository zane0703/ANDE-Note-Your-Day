package sg.LIZ.assignment1;

import java.util.GregorianCalendar;

public  class Key {
    public static int currentYear;
    public static byte currentMonth;
    public static byte currentDay;
    public final static String HTTP_METHOD_POST="POST";
    public final static String HTTP_METHOD_GET="GET";
    public final static String HTTP_METHOD_PUT="PUT";
    public final static String HTTP_METHOD_DELETE="DELETE";
    public final static String HTTP_CONTENT_TYPE="Content-Type";
    public final static String HTTP_AUTHORIZATION = "Authorization";
    public final static String FORM_URL_ENCODED="application/x-www-form-urlencoded";
    public final static String KEY_FK_USER_ID="fkUserId";
    public final static String KEY_START="start";
    public final static String KEY_END="END";
    public final static String KEY_TOKEN = "token";
    public final static String KEY_ID = "id";
    public final static String KEY_DAY = "day";
    public final static String KEY_MONTH= "month";
    public final static String KEY_YEAR = "year";
    public final static String KEY_START_HOURS ="startHours";
    public final static String KEY_START_MINUTES="startMinutes";
    public final static String KEY_END_HOURS = "endHours";
    public final static String KEY_END_MINUTES = "endMinutes";
    public final static String KEY_ALL_DAY ="allDay";
    public final static String KEY_TITLE = "title";
    public final static String KEY_DESCRIPTION = "description";
    public final static String KEY_VENUE="venue";
    public final static String KEY_IMAGE="image";
    public final static String DATABASE_NAME="assignment";
    static {
        GregorianCalendar gregorianCalendar =new GregorianCalendar();
        currentYear =  gregorianCalendar.get(GregorianCalendar.YEAR);
        currentMonth = (byte) gregorianCalendar.get(GregorianCalendar.MONTH);
        currentDay = (byte) gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH);

    }
}
