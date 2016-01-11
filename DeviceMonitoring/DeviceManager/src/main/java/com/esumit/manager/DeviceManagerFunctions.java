package com.esumit.manager;

import com.esumit.manager.device.deviceinfo.DeviceInfo;
import com.esumit.manager.device.deviceinfo.DeviceInfoDB;
import com.esumit.manager.helper.DBHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by eSumit on 10/27/15.
 * <p>
 * DeviceManager Functions perform following work items :
 * <p>
 * Step-1 : Connect to the Device Daemon
 * <p>
 * Step-2 : Request the DeviceInfo.json
 * <p>
 * Step-3 : Store this DeviceInfo to DB
 * <p>
 * getDeviceInfoFileFromDeviceDaemon get the file from DeviceDaemon (running inside an intended device).
 * <p>
 * storeDeviceInfoToDB - Store this information to MongoDB Database name : DeviceInfo
 */
public class DeviceManagerFunctions {

    private static final Logger logger = LoggerFactory.getLogger(DeviceManagerFunctions.class);

    public Boolean storeDeviceInfoToDB(DeviceInfo deviceInfo) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        String strDate = formatter.format(date);

        DeviceInfoDB deviceInfoDB = new DeviceInfoDB();
        deviceInfoDB.setDeviceInfo(deviceInfo);
        deviceInfoDB.setDate(strDate);

        DBHelper dbHelper = new DBHelper();

        dbHelper.connectToDB();
        dbHelper.insertDeviceInfoToDB(deviceInfoDB);
        dbHelper.closeConnection();

        return true;

    }

    public Boolean getDeviceInfoFileFromDeviceDaemon(String serverIP, int portNo, int fileMaxSize, String fileName) throws IOException {
        InputStream input = null;

        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;

        try {

            // Step-1 : Connect to the Device Daemon

            // Step-2 : Request the DeviceInfo.json

            // Step-3 : Store this DeviceInfo to DB

            sock = new Socket(serverIP, portNo);
            byte[] mybytearray = new byte[fileMaxSize];

            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(fileName);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray, 0, mybytearray.length);

            current = bytesRead;

            do {

                bytesRead = is.read(mybytearray, current,
                        (mybytearray.length - current));
                if (bytesRead >= 0)
                    current += bytesRead;

            } while (bytesRead > -1);

            bos.write(mybytearray, 0, current);
            bos.flush();
            fos.flush();

            logger.info("File " + fileName + " downloaded ("
                    + current + " bytes read)");
            return true;
        } catch (IOException ex) {
            logger.info(ex.getMessage());
        } finally {

            if (fos != null)
                fos.close();

            if (bos != null)
                bos.close();

            if (sock != null)
                sock.close();

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.info(e.getMessage());
                }
            }
        }
        return false;
    }
}
