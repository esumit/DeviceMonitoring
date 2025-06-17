package com.esumit.daemon.device.deviceinfo.deviceprocessinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.deploy.Environment;

import java.util.LinkedList;

/**
 * Created by eSumit on 10/27/15.
 *
 * It represent as an object of one process. It keeps, following information of each process running in a
 * specific device e.g. process id, parent id, process name, if its with a child, process type, its severity, and
 * list of childs.
 */
public class DeviceProcess {

    private int processid;
    private int parentid;
    private String processName;
    private Boolean haveChild;
    private DeviceProcessType deviceProcessType;
    private DeviceProcessSeverity deviceProcessSeverity;
    private LinkedList<DeviceProcess> childs;
    private ObjectMapper objectMapper;
    protected Environment environment;
    private boolean isHarmful = false;

    public DeviceProcess() {
        this.processid = 0;
        this.parentid = 0;
        this.childs = null;
        this.haveChild = false;
        this.processName = null;
        this.deviceProcessType = DeviceProcessType.RUNNING;
        this.deviceProcessSeverity = DeviceProcessSeverity.NORMAL;
    }


    public int getProcessid() {
        return processid;
    }

    public void setProcessid(int processid) {
        this.processid = processid;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Boolean isHaveChild() {
        return haveChild;
    }

    public void setHaveChild(Boolean haveChild) {
        this.haveChild = haveChild;
    }

    public DeviceProcessType getDeviceProcessType() {
        return deviceProcessType;
    }

    public void setDeviceProcessType(DeviceProcessType deviceProcessType) {
        this.deviceProcessType = deviceProcessType;
    }


    public DeviceProcessSeverity getDeviceProcessSeverity() {
        return deviceProcessSeverity;
    }

    public void setDeviceProcessSeverity(DeviceProcessSeverity deviceProcessSeverity) {
        this.deviceProcessSeverity = deviceProcessSeverity;
    }

    public LinkedList<DeviceProcess> getChilds() {
        return childs;
    }

    public void setChilds(LinkedList<DeviceProcess> childs) {
        this.childs = childs;
    }

    public boolean isHarmful() {
        return isHarmful;
    }

    public void setHarmful(boolean harmful) {
        isHarmful = harmful;
    }
}

