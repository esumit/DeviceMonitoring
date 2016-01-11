package com.esumit.daemon.helper.parser;

import com.esumit.daemon.device.deviceinfo.deviceprocessinfo.DeviceProcess;
import com.esumit.daemon.helper.DeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by eSumit on 10/27/15.
 *
 * This class executes the 'ps axo ppid,pid,command' and then read the output of it line by line. It ignores the
 * first line which is with PPID etc information, and split each line in three parts - parent id, process id and
 * process name. it continue to read all lines and prepare a node of each line. then each node inserted into
 * a linked list and returned to the caller.
 */
public class DeviceProcessListParser {

    private static final Logger logger = LoggerFactory.getLogger(DeviceProcessListParser.class);

    public LinkedList<DeviceProcess> parseDeviceProcessList() {
        String line;
        String workingDir = System.getProperty("user.dir");
        DeviceConfig deviceConfigInstance = DeviceConfig.getInstance();
        String processFileName = workingDir + "/" + deviceConfigInstance.getDeviceConfigProperty("file.processlist");

        LinkedList<DeviceProcess> listDeviceProcess = new LinkedList<DeviceProcess>();

        try {
            // Creating variable myFile to store the file stated by FILE_TO_SEND
            Process p = Runtime.getRuntime().exec("ps axo ppid,pid,command");

            FileOutputStream outputStream = new FileOutputStream(new File(processFileName));


            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));


            while ((line = input.readLine()) != null) {

                String[] splitProcessLine = line.trim().split("\\s+");

                if (splitProcessLine[0].equalsIgnoreCase("PPID")) {
                    continue;
                }

                DeviceProcess deviceProcess = new DeviceProcess();
                deviceProcess.setParentid(Integer.parseInt(splitProcessLine[0]));
                deviceProcess.setProcessid(Integer.parseInt(splitProcessLine[1]));
                deviceProcess.setProcessName(splitProcessLine[2]);
                listDeviceProcess.add(deviceProcess);
                outputStream.write(line.getBytes());
                outputStream.write("\n\r".getBytes());
            }
            input.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return listDeviceProcess;

    }

}
