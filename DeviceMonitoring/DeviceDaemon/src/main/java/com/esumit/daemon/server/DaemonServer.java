package com.esumit.daemon.server;

import com.esumit.daemon.helper.DeviceConfig;
import com.esumit.daemon.worker.DaemonWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * DaemonServer is the main class of DeviceDaemon Module. It calls DeviceConfig instance to configure dameon
 * specific configs,and then it create serverSocket and listen to receive a request.
 * Once it receive a request, It creates a DaemonWorker thread.
 * Next : Go to DameonWorker.class to know what that does.
 */

public class DaemonServer {

    private static final Logger logger = LoggerFactory.getLogger(DaemonServer.class);

    public static void main(String[] args) {

        DeviceConfig deviceConfigInstance = DeviceConfig.getInstance();
        int serverPort = Integer.parseInt(deviceConfigInstance.getDeviceConfigProperty("server.port"));

        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);

            logger.info("Listening...");

            while (true) {
                Socket newSocket = serverSocket.accept();
                logger.info("Connected");
                new Thread(new DaemonWorker(newSocket)).start();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);

        }
    }

}
