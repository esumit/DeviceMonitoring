package com.esumit.manager.device.deviceinfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by eSumit on 10/27/15.
 *
 * * It gets the Date Time details of the intended device.
 */
public class DeviceDateTimeInfo {
    private String dateTime;
    private String timezone;

    public DeviceDateTimeInfo() {
    }

    public void setDeviceDateTimeInfoDeatils() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date date = new Date();
        String strDate = formatter.format(date);

        this.setDateTime(strDate);
        TimeZone tz = TimeZone.getDefault();

        String timeZone = tz.getID() + tz.getDisplayName();
        this.setTimezone(timeZone);
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
