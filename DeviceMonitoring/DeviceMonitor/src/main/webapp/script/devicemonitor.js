
/*
  This files calls the getdeviceinfo GET request on every 60 seconds ( or the specified timer) from 
  http://localhost:8080/device-monitor/device/getdeviceinfo, parses the received
  DevicesInfo.json to prepare the value in HTML and display on the Device-Monitor.html web page. 

  Sumit Arora : 28th-Oct-2015

*/

var deviceMonitorStartStop = 0;


function deviceStartStatusHandler ( )
{
    // Start the timer
    document.getElementById("buttontype2").value = "Stop it If you want";

    onStartClick();
    deviceMonitorStartStop = setInterval ( "onStartClick()", 60000 );
  
}

function deviceStopStatusHandler ( )
{
   document.getElementById("buttontype2").value = "Stop";
   //document.getElementById("section3").innerHTML = "Stopped";
   clearInterval (deviceMonitorStartStop);
   
}


function onStartClick()
{

  
$.ajax({
  type:'GET',
  url: 'http://localhost:8080/device-monitor/device/getdeviceinfo',
  dataType: 'json',
  success: function(data) {
      
      var alertFlag = false;
      var json = JSON.parse(data);

      var todayDateAndTime = Date() + "<br>";
      var deviceDetailsHeading = "<hr>" + "<h1 id=h1tag > Device Details </h1> "
      var htmlDeviceInfo=  "<h2> Device Information</h2>"
      var deviceInfoValue = "IP Address: " + json.deviceInfo.deviceAddressInfo.ipAddress + "<br> " + "Hostname : " +json.deviceInfo.deviceAddressInfo.hostName + "<br> " + "Mac Address: " + json.deviceInfo.deviceAddressInfo.macAddress + "<br>";

      var deviceDateTime= "*Date/Time -( When information sent by the device) <br>"
      var deviceDateTimeInfo = "Date/Time: " + json.deviceInfo.deviceDateTimeInfo.dateTime + "<br> " + "Timezone: " +json.deviceInfo.deviceDateTimeInfo.timezone + "<br> ";
       
      var dbDateTime= "*Date/Time -( When information got recorded) <br>"
      var dbInfoValue = "Date/Time: " + json.date ;

    
      var deviceOSValue = "<br> OS Name: " + json.deviceInfo.deviceOSDetails.osName + "<br>" + "OS Version: " + json.deviceInfo.deviceOSDetails.osVersion + "<br>" + "OS Arch: " + json.deviceInfo.deviceOSDetails.osArchitecture + "<br> <hr>";

      var deviceMonitoringStatus = "<h1 id=h1tag > Device Status :: <img src='img/green.png' style='width:25px;height:25px;'> </h1>"

      var htmlValue = todayDateAndTime + deviceDetailsHeading + htmlDeviceInfo + deviceInfoValue + deviceDateTime + deviceDateTimeInfo + dbDateTime + dbInfoValue + deviceOSValue;

      var deviceProcessInfo = {};

      deviceProcessInfo = json.deviceInfo.processInfo.initProcess;
      var spaceCnt = 0;

      var processHTMLValue = "Init Process";

      walkListPrint(deviceProcessInfo,0);

      function walkListPrint(deviceProcessInfo,spaceCnt)
      {
        var childLists = [];
        childLists = deviceProcessInfo.childs;
        
        var tempProcess = null;


        if((deviceProcessInfo.parentid == 0) && (deviceProcessInfo.processid == 0))
        {
            processHTMLValue = processHTMLValue + deviceProcessInfo.processid + "   " + deviceProcessInfo.processName  + "<br>";
        }

        ++spaceCnt;
        var listIterator = childLists.length;
        var k = 0;

        for(k=0; k< listIterator ;k++)
        {
            tempProcess = childLists[k];
            var i =0;
             for(i=0;i<spaceCnt;i++) {
                
                if(tempProcess.deviceProcessType == "GHOST" && tempProcess.deviceProcessSeverity == "ALERT")
                {
                  processHTMLValue  = processHTMLValue + "<img src='img/red.png' style='width:25px;height:25px;'>" ;
                  alertFlag = true;
                } else
                {
                processHTMLValue  = processHTMLValue + "<img src='img/green.png' style='width:25px;height:25px;'>" ;
              }
            }

             processHTMLValue  = processHTMLValue + tempProcess.processid + "   " + tempProcess.processName + "<br>" ;

            if(tempProcess.haveChild)
            {
                walkListPrint(tempProcess,spaceCnt);
            }
        }
    }


     var deviceMonitoringGreenStatus = "<h1 id=h1tag > Device Status :: <img src='img/green.png' style='width:25px;height:25px;'> </h1>";

     var deviceMonitoringRedStatus = "<h1 id=h1tag > Device Status :: <img src='img/red.png' style='width:25px;height:25px;'> </h1>";

     if(alertFlag)
     {
      
        htmlValue = htmlValue + deviceMonitoringRedStatus;

     }else
     {

        htmlValue = htmlValue + deviceMonitoringGreenStatus;

     }

     document.getElementById("section3").innerHTML = htmlValue + processHTMLValue + "<hr>";
    },
    error: function(){
        $('#output ul').append('<li>Error');
    }
 });


}
   
function  onStopClick()
{
    document.getElementById("section3").innerHTML = "Stopped";

}  
