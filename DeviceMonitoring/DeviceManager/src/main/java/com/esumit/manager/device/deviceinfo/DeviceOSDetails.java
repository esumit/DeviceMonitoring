package com.esumit.manager.device.deviceinfo;

/**
 * Created by eSumit on 10/27/15.
 *
 * It gets the Operating System details of the intended device.
 */
public class DeviceOSDetails {

    private String osName;
    private String osVersion;
    private String osArchitecture;

    public DeviceOSDetails() {
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getOsArchitecture() {
        return osArchitecture;
    }

    public void setOsArchitecture(String osArchitecture) {
        this.osArchitecture = osArchitecture;
    }



    public void setDeviceOSInfoDetails() {

        this.osName = System.getProperty("os.name");
        this.osVersion = System.getProperty("os.version");
        this.osArchitecture= System.getProperty("os.arch");
    }
}
