package com.esumit.manager;

import com.esumit.manager.device.deviceinfo.DeviceInfo;
import com.esumit.manager.helper.DBHelper;
import com.esumit.manager.helper.DeviceConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;

/**
 * Created by eSumit on 10/27/15.
 *
 * DeviceManager class connect with respective devices, at this time only with one device and gets the DeviceInfo
 * in json format. Once it receive Json file it converts into a DeviceInfo Object, then it prepares a DeviceInfoDB
 * Object.
 *
 * DeviceInfo Object contains : Device Process Info, Device Address Info, Device Time Info etc
 *
 * DeviceInfoDB Object contains : DeviceInfo and Date/Time info when this record would be inserted to DB
 *
 * After preparing it to the DeviceInfoDB object, it converts it to json file, and then store it to the DeviceInfo database
 * of MongoDB.
 */
public class DeviceManager {
    private static final Logger logger = LoggerFactory.getLogger(DeviceManager.class);


    // Main method
    public static void main(String[] args) {

        DeviceConfig deviceConfigInstance = DeviceConfig.getInstance();
        String serverIP = deviceConfigInstance.getDeviceConfigProperty("service.ip");
        int portNo = Integer.parseInt(deviceConfigInstance.getDeviceConfigProperty("server.port"));
        int fileMaxSize =  Integer.parseInt(deviceConfigInstance.getDeviceConfigProperty("file.maxsize"));
        String workingDir = System.getProperty("user.dir");
        String fileName = workingDir + "/" + deviceConfigInstance.getDeviceConfigProperty("file.name");

        DeviceManagerFunctions deviceManagerFunctions = new DeviceManagerFunctions();

        do {

            try {
                if(deviceManagerFunctions.getDeviceInfoFileFromDeviceDaemon(serverIP,portNo,fileMaxSize,fileName))
                {

                    ObjectMapper mapper = new ObjectMapper();
                    DeviceInfo deviceInfo = new DeviceInfo();


                    try {
                        deviceInfo = mapper.readValue(new File(fileName), DeviceInfo.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                 deviceManagerFunctions.storeDeviceInfoToDB(deviceInfo);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                Thread.sleep(60000);  //1000 milliseconds is one second.
                logger.info("60000 lapsed ! getting the file again");
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } while(true);

    }

}
