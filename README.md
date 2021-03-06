# DeviceMonitoring
DeviceMonitoring is a conceptual full stack web application which audits running processes in a computing device (e.g. ubuntu desktop, android device, mac machine). 

It has been divided in four parts :

1. DeviceMonitor - Presentation part, UI have html,css, js code 

2. DeviceDaemon -  An auditorrunning in a device. It responsible to collect the processes information in a specified time interval

3. DeviceManager - It fetches audit information from the DeviceDaemon. If Device-Monitor finds any suspicious activity on that device, then it alerts the red flag on the UI

4. DB - Mongo DB used to store the persistance

![alt tag](http://media.hiringlibrary.com.s3.amazonaws.com/wp-content/uploads/17045321/How-DeviceMonitor-Works.png)


# Flow of Device Monitoring

DeviceDaemon: – Device Daemon is a TCP server, it waits for DeviceInfo request to receive. Once It receive the request, It calls to DeviceWorker(a new thread) which in turn call DaemonHelper. That DaemonHelper executes a perl script or ‘ps axo ppid,pid,command’ to get the details info about the ‘process’ running in that device. It parses the received ‘process information’ and converts into a Linked List of process.


 
This LinkedList of process processed by the DeviceProcessToJsonConverter. It parses the received list of nodes, put into right parent/child tree order and then converts into a json file DeviceInfo.json. It also adds additional information into this file e.g. Device Time, Address,OS Details.
Once the DeviceInfo.json got prepared then DeviceWorker send this DeviceInfo.json to DeviceManager and terminate its thread.

 

DeviceManager: DeviceManager runs TCP client in a thread. In every minute (time is configurable), it sends a DeviceInfo request to DeviceDameon. That DeviceDameon receives the request and return the DeviceInfo.json to DeviceManager. DeviceManager receives DeviceInfo.json file, then it maps DeviceInfo.json to a DeviceInfo Object and adds additional information e.g. time it received the info and produce DeviceInfoDB Object. After that it converts DeviceInfoDB Object into Json and then insert it to MongDB’s collection -name -‘DeviceInfo’.

Device-Monitor Web Service: This Web Service runs inside the Catalina container, deployed as a REST based web service. The URL of this GET is: http://localhost:8080/device-monitor/device/getdeviceinfo. Internally it call the getDeviceInfo API, which in turn access the DeviceInfo MongoDB database ,fetches the latest DeviceInfo Record and then return to caller as a JSON response.

Device-Monitor Display: It contains following things :

Device-Monitor.html – On call to http://localhost:8080 , browser fetches this file and display on the browser with ‘Start’ and ‘Stop’ Button.

Devicemonitor.js: On click to start, it sends getDeviceInfo GET request (on every 60 seconds) to the webservice( as explained above) and receives the DeviceInfo.json file. It parses this file and display the information on Browser.


# How overall flow will work ?

[Assumption : Java/Maven/MongoDB installed on the machine]

 // Run MongoDB 

 Step-1 : Start MongoDB Server, Create a Database name ‘DeviceInfo’ and collection deviceInfo.

 // Run DeviceDameon 

Step-2 : Go to DeviceDaemon folder – Type : mvn clean install, and then go to its target folder and Type : java -jar device-daemon.jar

(to configure IP and port, go to its config.properties file)

// Run DeviceManager

Step-3 : Go to DeviceManager folder – Type : mvn clean install, and then go to its target folder and Type : java -jar device-manager.jar

(to configure IP and port, go to its config.properties file)

Now Device Manager getting the DeviceInfo.json from DeviceDaemon and inserting into the DeviceInfo database of MongoDB.

// Once both are running, then run the WebService

Step-4 : Go to DeviceMonitor folder – Type : mvn clean install, and then go to its target folder and Type : mvn tomcat7:run

(to configure IP and port, go to its config.properties file)

![alt tag](http://media.hiringlibrary.com.s3.amazonaws.com/wp-content/uploads/17045133/All-Four-Process-are-Running.png)

// Open the browser
Step-5 : Open the browser e.g. chrome or safari , type : http://localhost:8080/ 
5.1 It will show Device-Monitor.html as below :

![alt tag](https://github.com/esumit/DeviceMonitoring/blob/master/Images/DeviceMonitoring_MainPage.png)

5.2 Click ‘Start’ button – It will start fetching the records from database and change the Stop button to ‘Stop it If you want’ 

![alt tag](https://github.com/esumit/DeviceMonitoring/blob/master/Images/DeviceMonitoringWithDetails.png)

5.3 Scroll Down to See the Device’s Process details:

A process shows red if it is harmful for the system (dummy at this time, randomly picked some process as harmful process). 

A process shows green if its normal process.

![alt tag](https://github.com/esumit/DeviceMonitoring/blob/master/Images/DeviceMonitor_DeviceStatus.png)


Code Structure

![alt tag](https://github.com/esumit/DeviceMonitoring/blob/master/Images/DeviceMonitor_CodeStructure.png)

Technology Used:

OS  Used : Mac OS X,10.10.1, x86_64
Languages : Java , JavaScript, CSS,JavaScript
Framework/Server : Apache CXF, Spring, Tomcat Container
Database : MongoDB





