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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

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
    private static final Set<String> harmfulProcessNames = new HashSet<>();

    static {
        try (InputStream is = DeviceProcessToJsonConverter.class.getClassLoader().getResourceAsStream("harmful-processes.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            if (is == null) {
                logger.error("Cannot find harmful-processes.txt");
            } else {
                harmfulProcessNames.addAll(reader.lines().map(String::trim).filter(line -> !line.isEmpty()).collect(Collectors.toSet()));
                logger.info("Loaded " + harmfulProcessNames.size() + " harmful process names.");
            }
        } catch (IOException | NullPointerException e) {
            logger.error("Error loading harmful-processes.txt", e);
        }
    }

    public String getDeviceProcessJsonFile() {

        String workingDir = System.getProperty("user.dir");

        DeviceConfig deviceConfigInstance = DeviceConfig.getInstance();
        String jsonFileName = workingDir + "/" + deviceConfigInstance.getDeviceConfigProperty("file.deviceinfo");


        DeviceProcessListParser deviceProcessListParser = new DeviceProcessListParser();
        LinkedList<DeviceProcess> listDeviceProcess = deviceProcessListParser.parseDeviceProcessList();

        if (listDeviceProcess != null) {
            for (DeviceProcess process : listDeviceProcess) {
                if (process.getProcessName() != null) {
                    String command = process.getProcessName().toLowerCase();
                    boolean foundHarmful = false; // Flag to check if current process is harmful
                    for (String harmfulName : harmfulProcessNames) {
                        if (command.contains(harmfulName.toLowerCase())) {
                            process.setHarmful(true);
                            process.setDeviceProcessSeverity(com.esumit.daemon.device.deviceinfo.deviceprocessinfo.DeviceProcessSeverity.DANGER);
                            foundHarmful = true;
                            break;
                        }
                    }
                    // If not found in harmful list, its severity remains NORMAL (default from constructor)
                }
            }
        }

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
