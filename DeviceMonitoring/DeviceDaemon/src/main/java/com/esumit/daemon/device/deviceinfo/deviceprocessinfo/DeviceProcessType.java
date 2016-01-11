package com.esumit.daemon.device.deviceinfo.deviceprocessinfo;

/**
 * Created by eSumit on 10/27/15.
 *
 * Its a dummy class just to define the type of process. At present the default type of process is 'RUNNING',
 * and for demo purpose the harmful process is GHOST.
 */
public enum DeviceProcessType {

    /**
     * Default : Running DeviceProcess
     */
    RUNNING,
    /**
     * Init DeviceProcess
     */
    INIT,
    /**
     * Daemon DeviceProcess
     */
    DAEMON,
    /**
     * Zombie DeviceProcess
     */
    ZOMBIE,
    /**
     * Orphan DeviceProcess
     */
    ORPHAN,
    /**
     * Unauthorized DeviceProcess
     */
    GHOST,
}
