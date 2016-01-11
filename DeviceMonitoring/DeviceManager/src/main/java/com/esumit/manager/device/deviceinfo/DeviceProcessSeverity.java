package com.esumit.manager.device.deviceinfo;

/**
 * Created by eSumit on 10/27/15.
 *
 * Its just to add dummy Severity type of a process.
 */
public enum DeviceProcessSeverity {
    NORMAL,        /** Everything is Normal */
    LOW_RISK,      /** Its Normal, but this process may cause problems ~1% Predictablity */
    MEDIUM_RISK,   /** Its Normal, but this process may cause problems ~10% Predictablity */
    WARNING,       /** Its Normal, but this process may cause problems ~15% Predictablity */
    ALERT,         /** Its not Normal, This process will cause problems ~35% Predictablity */
    DANGER         /** Its not Normal : This DeviceProcess giving problems now - Take immediate actions */
}
