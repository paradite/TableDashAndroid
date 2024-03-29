package thack.ac.tabledash;

import android.nfc.FormatException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Class to provide general useful methods
 * Created by paradite on 17/8/14.
 */
public class Helper {

    private static final byte[] HEX_CHAR_TABLE = {(byte) '0', (byte) '1',
            (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
            (byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
            (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F'};

    /**
     * Get Hex string from raw byte array
     * @param raw   Raw byte array
     * @param len   Length of array
     * @return  Hex String
     */
    static String getHexString(byte[] raw, int len) {
                byte[] hex = new byte[2 * len];
                int index = 0;
                int pos = 0;

                for (byte b : raw) {
                    if (pos >= len)
                        break;

                    pos++;
                    int v = b & 0xFF;
                    hex[index++] = HEX_CHAR_TABLE[v >>> 4];
                    hex[index++] = HEX_CHAR_TABLE[v & 0xF];
        //            Log.e("getHexString", "raw byte: " + b + " v: " + v + " converted: " + HEX_CHAR_TABLE[v >>> 4] + " & " + HEX_CHAR_TABLE[v & 0xF]);
                }

                return new String(hex);
            }

    /**
     * Get the displayable date from week and year
     * @param week week of year
     * @param year year
     * @return Date string for displaying
     */
    public static String getDate(int week, int year) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Calendar cal = Calendar.getInstance();
//        Log.i(TAG, sdf.format(cal.getTime()));
        cal.set(Calendar.YEAR, year);
//        Log.i(TAG, sdf.format(cal.getTime()));
        cal.set(Calendar.WEEK_OF_YEAR, week);
//        Log.i(TAG, sdf.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, 1);
//        Log.i(TAG, sdf.format(cal.getTime()));
        String start = sdf.format(cal.getTime());
        cal.set(Calendar.DAY_OF_WEEK, 7);
//        Log.i(TAG, sdf.format(cal.getTime()));
        String end = sdf.format(cal.getTime());
        return start + " to " + end;
    }

    /**
     * Parse the date from server to String for local database
     *
     * @param date_received date received from server
     * @return Date for storing in local database
     */
    public static Date parseDateFromString(String date_received) {
        //2014-06-28 14:56:59
        SimpleDateFormat dateFormat_received = new SimpleDateFormat(
                "yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat_received.parse(date_received);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String parseDateToString(Date date_received) {
        //2014-06-28 14:56:59
        SimpleDateFormat dateFormat_received = new SimpleDateFormat(
                "yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault());
        String date_string = null;
        date_string = dateFormat_received.format(date_received);
        return date_string;
    }

    /**
     * Check if eating is finished
     *
     * @param time_ending Date from the preference of the app
     * @return true if finished
     */
    public static Boolean checkIfFinished(Date time_ending) {
        Date date_current = new Date();
        long seconds = (date_current.getTime() - time_ending.getTime()) / 1000;
//        Log.e("Check, ", "time diff in seconds: " + seconds);
//        Current time > Ending time => Finished eating session
        return seconds > 0;
    }

    public static Boolean checkIfAlmostEnd(Date time_notification) {
        Date date_current = new Date();
        return (date_current.getTime() - time_notification.getTime()) > 0;
    }

}
