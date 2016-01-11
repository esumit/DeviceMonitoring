package com.esumit.daemon.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by eSumit on 10/27/15.
 *
 * Its a singleton class, its purpose to help DaemonServer for config related stuff.
 */
public class DeviceConfig {

    private static final DeviceConfig deviceConfig = new DeviceConfig();

    private DeviceConfig() {
    }

    private String deviceConfigFile;


    public static DeviceConfig getInstance() {
        return deviceConfig;
    }

    public String getDeviceConfigProperty(String propertyName) {
        Properties prop = new Properties();
        InputStream input = null;

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        input = classloader.getResourceAsStream("config.properties");

        // load a properties file
        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(propertyName);
    }

    public String getDeviceConfigFile() {
        return deviceConfigFile;
    }

    public void setDeviceConfigFile(String deviceConfigFile) {
        this.deviceConfigFile = deviceConfigFile;
    }


}
