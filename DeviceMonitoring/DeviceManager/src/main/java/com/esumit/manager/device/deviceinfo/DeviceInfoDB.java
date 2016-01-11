package com.esumit.manager.device.deviceinfo;

/**
 * Created by eSumit on 10/25/15.
 *
 * It contains Device info and additional date information.
 */
public class DeviceInfoDB {

    DeviceInfo deviceInfo;
    String date;  /** time when deviceInfoDB record got inserted into MongoDB */

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

}
