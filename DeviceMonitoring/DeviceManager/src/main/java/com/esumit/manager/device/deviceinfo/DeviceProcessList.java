package com.esumit.manager.device.deviceinfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by eSumit on 10/27/15.
 * This class has methods to manage the process node information received in a linked list, it does the
 * following :
 *
 * 1. It prepare a Tree from the received Nodes in a Process Linked List, by calling the method :makeProcessListTree
 *
 * 2. walkListPrint prints the Process Nodes in a parent/child order.
 *
 * 3. setProcessTypeToGhost randomly set some process to GHOST.
 *
 * 4. walkListPrint helps to insertListNodeInProcessListTree to traverse the child of a parent process

 */
public class DeviceProcessList {

    private static final Logger logger = LoggerFactory.getLogger(DeviceProcessList.class);

    private DeviceProcess initProcess;

    public DeviceProcessList() {
        this.initProcess = null;
    }


    public void makeProcessListTree(LinkedList<DeviceProcess> deviceProcessList) {
        ListIterator<DeviceProcess> listIterator = deviceProcessList.listIterator();
        while (listIterator.hasNext()) {
            insertListNodeInProcessListTree(listIterator.next());
        }
    }

    public void insertListNodeInProcessListTree(DeviceProcess processParam) {
        // if processPraram's ID equal to Zero and DeviceProcess ID is 1
        // Make it as initProcess so now initProcess is not Null

        if (getInitProcess() == null) {
            initProcess = new DeviceProcess();
            initProcess.setChilds(null);
            initProcess.setParentid(0);
            initProcess.setProcessid(0);
        }

        DeviceProcess newNode = new DeviceProcess();

        newNode.setProcessid(processParam.getProcessid());
        newNode.setParentid(processParam.getParentid());
        newNode.setProcessName(processParam.getProcessName());

        // If Node to be the Child of Root Node

        if ((initProcess.getProcessid() == processParam.getParentid())) {
            if (initProcess.getChilds() == null) {
                LinkedList<DeviceProcess> addChilds = new LinkedList<DeviceProcess>();
                addChilds.add(newNode);
                initProcess.setChilds(addChilds);
                initProcess.setHaveChild(true);
            } else {
                LinkedList<DeviceProcess> addChilds = initProcess.getChilds();
                addChilds.add(newNode);
            }
        }


        DeviceProcess retNode = walkList(processParam, initProcess.getChilds());

        if ((retNode.getProcessid() == processParam.getParentid())) {
            if (retNode.getChilds() == null) {
                LinkedList<DeviceProcess> addChilds = new LinkedList<DeviceProcess>();
                addChilds.add(newNode);
                retNode.setChilds(addChilds);
                retNode.setHaveChild(true);
            } else {
                if (setProcessTypeToGhost()) {
                    newNode.setDeviceProcessSeverity(DeviceProcessSeverity.ALERT);
                    newNode.setDeviceProcessType(DeviceProcessType.GHOST);
                }
                LinkedList<DeviceProcess> addChilds = retNode.getChilds();
                addChilds.add(newNode);

            }
        }

    }

    public void walkListPrint(DeviceProcess processParam, int spaceCnt) {
        LinkedList<DeviceProcess> childLists = processParam.getChilds();
        DeviceProcess tempProcess = null;


        if ((processParam.getParentid() == 0) && (processParam.getProcessid() == 0)) {
            System.out.println(processParam.getProcessid());
        }

        ++spaceCnt;
        ListIterator<DeviceProcess> listIterator = childLists.listIterator();

        while (listIterator.hasNext()) {
            tempProcess = listIterator.next();

            for (int i = 0; i < spaceCnt; i++) {
                System.out.print("-B-");
            }
            System.out.println(tempProcess.getProcessid());

            if (tempProcess.isHaveChild()) {
                walkListPrint(tempProcess, spaceCnt);
            }

        }

    }

    public DeviceProcess walkList(DeviceProcess processParam, LinkedList<DeviceProcess> childList) {
        DeviceProcess tempProcess = null;


        ListIterator<DeviceProcess> listIterator = childList.listIterator();

        while (listIterator.hasNext()) {
            tempProcess = listIterator.next();

            if ((tempProcess.getProcessid() == processParam.getParentid()) ||
                         (tempProcess.getParentid() == processParam.getParentid())) {
                return tempProcess;
            }

            if (tempProcess.isHaveChild()) {
                tempProcess = walkList(processParam, tempProcess.getChilds());

                if ((tempProcess.getProcessid() == processParam.getParentid()) ||
                        (tempProcess.getParentid() == processParam.getParentid())) {
                    return tempProcess;
                }
            }
        }

        return tempProcess;
    }

    public Boolean setProcessTypeToGhost() {
        //note a single Random object is reused here
        Random randomGenerator = new Random();

        int randomInt = randomGenerator.nextInt(100);

        if (randomInt < 10) {
            //logger.info("True : Generated Ghost : " + randomInt);
            return true;
        } else {
            // logger.info("False : No Ghost Generated : " + randomInt);
            return false;
        }

    }

    public DeviceProcess getInitProcess() {
        return initProcess;
    }

    public void setInitProcess(DeviceProcess initProcess) {
        this.initProcess = initProcess;
    }

}
