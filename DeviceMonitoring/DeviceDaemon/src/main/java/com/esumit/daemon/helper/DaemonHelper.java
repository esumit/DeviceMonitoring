package com.esumit.daemon.helper;


import com.esumit.daemon.helper.parser.DeviceProcessToJsonConverter;

/**
 * Created by eSumit on 10/27/15.
 *
 * This provides helpers to Daemon Servers e.g. convert process list to json file.
 */
public class DaemonHelper {

    private DeviceProcessToJsonConverter deviceProcessToJsonConverter;

    public DaemonHelper() {
        this.deviceProcessToJsonConverter = new DeviceProcessToJsonConverter();

    }

    public DeviceProcessToJsonConverter getDeviceProcessToJsonConverter() {
        return deviceProcessToJsonConverter;
    }

    public void setDeviceProcessToJsonConverter(DeviceProcessToJsonConverter deviceProcessToJsonConverter) {
        this.deviceProcessToJsonConverter = deviceProcessToJsonConverter;
    }

}
