package nuce.tatv.noteeverything.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormat {
    public String Format(String date) throws ParseException {
        SimpleDateFormat formatIn = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatOut = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formatIn.parse(date));
        String newDate = formatOut.format(calendar.getTime());
        return newDate;
    }
}
