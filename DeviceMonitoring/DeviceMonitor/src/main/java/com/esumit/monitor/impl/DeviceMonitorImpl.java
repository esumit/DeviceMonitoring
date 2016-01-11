package com.esumit.monitor.impl;

import com.esumit.monitor.api.DeviceMonitorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.ws.rs.core.Response;


/**
 * Created by eSumit on 10/28/15.
 *
 * This is the implementation of REST GET Request of http://localhost:8080/device-monitor/device/getdeviceinfo
 */
@Service("monitorservice")
@EnableAuthentication
public class DeviceMonitorImpl implements DeviceMonitorInterface {

    @Autowired
    private GetDeviceInfoHandler deviceInfoHandler;

    public Response getDeviceInfo( )
    {
        return deviceInfoHandler.getDeviceInfo();
    }


}
