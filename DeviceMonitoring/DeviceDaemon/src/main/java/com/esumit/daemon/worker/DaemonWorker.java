package com.esumit.daemon.worker;

import com.esumit.daemon.helper.DaemonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;


/**
 * Created by eSumit on 10/27/15.
 *
 * Daemon Worker thread extends the Thread class to implement run interface. It calls the instance of DaemonHelper
 * instance, which in turn call the method getDeviceProcessToJsonConverter of DeviceProcessToJsonConverter.
 * This DeviceProcessToJsonConverter return the DeviceInfo.Json file from this method getDeviceProcessJsonFile.
 *
 * How this method works : getDeviceProcessJsonFile , Go to DeviceProcessToJsonConverter.class
 *
 */
public class DaemonWorker extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(DaemonWorker.class);

    private Socket csocket;

    public Socket getCsocket() {
        return csocket;
    }

    public void setCsocket(Socket csocket) {
        this.csocket = csocket;
    }

    public DaemonWorker(Socket csocket) {
        this.csocket = csocket;
    }


    public void run() {

        String workingDir = System.getProperty("user.dir");
        DaemonHelper daemonHelper = new DaemonHelper();

        try {

            String deviceProcessFileName = daemonHelper.getDeviceProcessToJsonConverter().getDeviceProcessJsonFile();

            File deviceInfoFile = new File(deviceProcessFileName);


            byte[] myByteArray = new byte[(int) deviceInfoFile.length()];

            FileInputStream fis = new FileInputStream(deviceInfoFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

            bis.read(myByteArray, 0, myByteArray.length);

            OutputStream os = csocket.getOutputStream();

            logger.info("Sending " + workingDir + "/" + deviceProcessFileName + "("
                    + myByteArray.length + " bytes)");

            os.write(myByteArray, 0, myByteArray.length);

            os.flush();
            bis.close();
            os.close();
            csocket.close();

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }
}
