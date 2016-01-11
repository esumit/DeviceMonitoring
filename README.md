# DeviceMonitoring
DeviceMonitor audits running applications in a device (e.g. ubuntu desktop, android device, mac machine) with the help of DeviceManager who manage devices. DeviceManager get the audit information from the DeviceDaemon(running inside the device, responsible to collect the running application information in a specified time interval). If Device-Monitor finds any suspicious activity on that device, then it alerts to the authorized user of that device.

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
