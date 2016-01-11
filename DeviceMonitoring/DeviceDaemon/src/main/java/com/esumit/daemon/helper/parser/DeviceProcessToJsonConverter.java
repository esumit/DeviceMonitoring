package com.esumit.daemon.helper.parser;

import com.esumit.daemon.device.DeviceInfo;
import com.esumit.daemon.device.deviceinfo.DeviceAddressInfo;
import com.esumit.daemon.device.deviceinfo.DeviceDateTimeInfo;
import com.esumit.daemon.device.deviceinfo.DeviceOSDetails;
import com.esumit.daemon.device.deviceinfo.deviceprocessinfo.DeviceProcess;
import com.esumit.daemon.device.deviceinfo.deviceprocessinfo.DeviceProcessList;
import com.esumit.daemon.helper.DeviceConfig;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by eSumit on 10/27/15.
 * Purpose of this function is to
 *
 * 1.Fetch Process List as a LinkedList,that contains set of nodes(process) and each node
 * represent a single process e.g. its process id, parent id, its process name, whether it has
 * child nodes or not, process type etc.
 *
 * 2. Parse this LinkedList so that all nodes would be in a tree order e.g parent process, then its child process.
 *
 * 3. Prepare a DeviceInfo object , Add other details into : DeviceDateTimeInfo,DeviceOSDetails , DeviceAddressInfo
 *
 * 4. Finally convert this whole information into a json e.g. DeviceInfo.json
 *
 * 5. Return this DeviceInfo.json to caller.
 *
 * To know more about Parser go to DeviceProcessListParser.class
 */
public class DeviceProcessToJsonConverter {

    private static final Logger logger = LoggerFactory.getLogger(DeviceProcessToJsonConverter.class);

    public String getDeviceProcessJsonFile() {

        String workingDir = System.getProperty("user.dir");

        DeviceConfig deviceConfigInstance = DeviceConfig.getInstance();
        String jsonFileName = workingDir + "/" + deviceConfigInstance.getDeviceConfigProperty("file.deviceinfo");


        DeviceProcessListParser deviceProcessListParser = new DeviceProcessListParser();
        LinkedList<DeviceProcess> listDeviceProcess = deviceProcessListParser.parseDeviceProcessList();
        DeviceProcessList deviceProcessList = new DeviceProcessList();
        deviceProcessList.makeProcessListTree(listDeviceProcess);

        DeviceAddressInfo deviceAddressInfo = new DeviceAddressInfo();
        deviceAddressInfo.setDeviceAddressInfoDetails();

        DeviceDateTimeInfo deviceDateTimeInfo = new DeviceDateTimeInfo();

        deviceDateTimeInfo.setDeviceDateTimeInfoDeatils();

        DeviceOSDetails deviceOSDetails = new DeviceOSDetails();

        deviceOSDetails.setDeviceOSInfoDetails();

        DeviceInfo deviceInfo = new DeviceInfo();

        deviceInfo.setProcessInfo(deviceProcessList);
        deviceInfo.setDeviceAddressInfo(deviceAddressInfo);
        deviceInfo.setDeviceDateTimeInfo(deviceDateTimeInfo);
        deviceInfo.setDeviceOSDetails(deviceOSDetails);

        ObjectMapper mapper = new ObjectMapper();

        try {
            //Convert object to JSON string and save into file directly
            mapper.writeValue(new File(jsonFileName), deviceInfo);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonFileName;

    }


}
