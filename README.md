# DeviceMonitoring
DeviceMonitoring is a comprehensive full-stack web application designed to monitor and audit running processes across various computing devices, 
including Ubuntu desktops, Android devices, and Mac machines. 

![alt tag](https://github.com/esumit/DeviceMonitoring/blob/master/Images/device-monitor-context.png)

The project is divided into four components:


1. DeviceMonitor: This is the presentation layer that consists of the user interface, built using HTML, CSS, and JavaScript. It displays the monitored data for the user's convenience. 

2. DeviceDaemon: This component is an auditor running on each monitored device. It is responsible for gathering information about the device's running processes at specified time intervals.

3. DeviceManager - This component communicates with DeviceDaemon to retrieve the audit data. If DeviceMonitor detects any suspicious activities on a device, it raises a red flag in the user interface to alert the user.

4. DB - MongoDB is utilized for storing persistent data related to the application, including the monitored processes and their respective information

![alt tag](https://github.com/esumit/DeviceMonitoring/blob/master/Images/How-DeviceMonitor-Works.png)


# Flow of Device Monitoring

**DeviceDaemon**: The Device Daemon is a TCP server that waits for incoming DeviceInfo requests. Upon receiving a request, it initiates a DeviceWorker (a new thread) which in turn calls the DaemonHelper. The DaemonHelper executes a Perl script or runs the command 'ps axo ppid,pid,command' to obtain detailed information about the processes running on the device. This information is parsed and converted into a linked list of processes.

The DeviceProcessToJsonConverter processes this LinkedList of processes. It organizes the received list of nodes into the correct parent/child tree structure and converts it into a JSON file, DeviceInfo.json. The converter also adds additional information to this file, such as device time, address, and OS details. Once the DeviceInfo.json is ready, the DeviceWorker sends it to the DeviceManager and terminates its thread.

**DeviceManager**: The DeviceManager runs a TCP client in a separate thread. At a configurable time interval (default is every minute), it sends a DeviceInfo request to the DeviceDaemon. The DeviceDaemon responds with the DeviceInfo.json file, which the DeviceManager then maps to a DeviceInfo object. Additional information, such as the time the information was received, is added to produce a DeviceInfoDB object. This object is converted into JSON format and inserted into the MongoDB collection named 'DeviceInfo'.

**Device-Monitor Web Service**: This web service runs within the Catalina container and is deployed as a REST-based web service. The URL for this GET request is: http://localhost:8080/device-monitor/device/getdeviceinfo. Internally, it calls the getDeviceInfo API, which accesses the DeviceInfo MongoDB database, fetches the latest DeviceInfo record, and returns it to the caller as a JSON response.
Device-Monitor Display: It contains following things :

**Device-Monitor Display**: The display component includes the following elements:

- Device-Monitor.html: When the user accesses http://localhost:8080, the browser retrieves this file and displays it with 'Start' and 'Stop' buttons.
- Devicemonitor.js: When the user clicks the 'Start' button, the script sends a getDeviceInfo GET request (every 60 seconds) to the web service (as described above) and receives the DeviceInfo.json file. The script parses this file and displays the information in the browser.


# Overall Workflow Guide

[Assumption : Java/Maven/MongoDB installed on the machine]

![alt tag](https://github.com/esumit/DeviceMonitoring/blob/master/Images/device-monitor-workflow.png)

##### Step-1 Run MongoDB 

- Start the MongoDB server.
- Create a database named 'DeviceInfo' and a collection called 'deviceInfo'.

##### Step-2 Run DeviceDameon

- Navigate to the DeviceDaemon folder.
- Type mvn clean install, then go to the target folder and type java -jar device-daemon.jar.
(To configure IP and port, edit the config.properties file.)


##### Step-3 Run DeviceManager
- Navigate to the DeviceManager folder.
- Type mvn clean install, then go to the target folder and type java -jar device-manager.jar.
(To configure IP and port, edit the config.properties file.)
- Now, DeviceManager retrieves the DeviceInfo.json from DeviceDaemon and inserts it into the DeviceInfo MongoDB database.


#### Step-4 - Run the WebService

- Navigate to the DeviceMonitor folder.
- Type mvn clean install, then go to the target folder and type mvn tomcat7:run.
(To configure IP and port, edit the config.properties file.)

![alt tag](https://github.com/esumit/DeviceMonitoring/blob/master/Images/All-Four-Process-are-Running.png)

#### Step-5 - Open the browser
- Open a browser (e.g., Chrome or Safari) and type http://localhost:8080/.
- This will display the Device-Monitor.html page.
  - ![alt tag](https://github.com/esumit/DeviceMonitoring/blob/master/Images/DeviceMonitoring_MainPage.png)
- Click the 'Start' button to begin fetching records from the database. The button text will change to 'Stop it If you want'.
  - https://github.com/esumit/DeviceMonitoring/blob/master/Images/DeviceMonitoringWithDetails.png
- Scroll down to see the device's process details. Processes are color-coded:
  - Red indicates a harmful process (currently dummy data; some processes are randomly selected as harmful).
    - ![alt tag](https://github.com/esumit/DeviceMonitoring/blob/master/Images/DeviceMonitor_DeviceStatus.png)
  - Green indicates a normal process.
    - ![alt tag](https://github.com/esumit/DeviceMonitoring/blob/master/Images/DeviceMonitor_DeviceStatus.png)



#### Code Structure

![alt tag](https://github.com/esumit/DeviceMonitoring/blob/master/Images/DeviceMonitor_CodeStructure.png)

#### Technology Used:

OS  Used : Mac OS X,10.10.1, x86_64
Languages : Java , JavaScript, CSS,JavaScript
Framework/Server : Apache CXF, Spring, Tomcat Container
Database : MongoDB





