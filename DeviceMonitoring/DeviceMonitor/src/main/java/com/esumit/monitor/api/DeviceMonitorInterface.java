package com.esumit.monitor.api;



import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;



/**
 * Created by eSumit on 10/28/15.
 *
 * This REST GET API as  http://localhost:8080/device-monitor/device/getdeviceinfo  called by DeviceMonitor.js
 * via a AJAX Request from devicemonitor.js.
 *
 * Here is the flow :
 *
 * // Assume that MongoDB running on background  at localhost:27017
 *
 * 1. User open the browser and type : http://localhost:8080, It shows Device-Monitor.html
 *
 * 2. Device-Monitor.html shows two button 'Start' and 'Stop'
 *
 * 3. Click to 'Start'
 *
 * 4. It will fetch most recent DeviceInfo record from DeviceInfo Database of MongoDB and Display.
 */
@Path("/device")
public interface DeviceMonitorInterface {

    @GET
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Path("/getdeviceinfo")
    Response getDeviceInfo();
}
