package com.esumit.daemon.device;

import com.esumit.daemon.device.deviceinfo.DeviceAddressInfo;
import com.esumit.daemon.device.deviceinfo.DeviceDateTimeInfo;
import com.esumit.daemon.device.deviceinfo.DeviceOSDetails;
import com.esumit.daemon.device.deviceinfo.deviceprocessinfo.DeviceProcessList;


/**
 * Created by eSumit on 10/27/15.
 *
 * It contains all the Device related information which would be added into the DeviceInfo.json file
 * and later it would be sent to the DeviceManager on request.
 */
public class DeviceInfo {

    private DeviceOSDetails deviceOSDetails;
    private DeviceAddressInfo deviceAddressInfo;
    private DeviceDateTimeInfo deviceDateTimeInfo;
    private DeviceProcessList processInfo;

    public DeviceInfo() {
    }

    public DeviceOSDetails getDeviceOSDetails() {
        return deviceOSDetails;
    }

    public void setDeviceOSDetails(DeviceOSDetails deviceOSDetails) {
        this.deviceOSDetails = deviceOSDetails;
    }

    public DeviceAddressInfo getDeviceAddressInfo() {
        return deviceAddressInfo;
    }

    public void setDeviceAddressInfo(DeviceAddressInfo deviceAddressInfo) {
        this.deviceAddressInfo = deviceAddressInfo;
    }

    public DeviceDateTimeInfo getDeviceDateTimeInfo() {
        return deviceDateTimeInfo;
    }

    public void setDeviceDateTimeInfo(DeviceDateTimeInfo deviceDateTimeInfo) {
        this.deviceDateTimeInfo = deviceDateTimeInfo;
    }

    public DeviceProcessList getProcessInfo() {
        return processInfo;
    }

    public void setProcessInfo(DeviceProcessList processInfo) {
        this.processInfo = processInfo;
    }


}

