package com.esumit.manager.helper;


import com.esumit.manager.device.deviceinfo.DeviceInfoDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * Created by eSumit on 10/27/15.
 *
 * This DBHelper class does the following things :
 *
 * 1.connectToDB - based on the specified mongoDB IP,DB Name
 *
 * 2.insertDeviceInfoToDB - insert DeviceInfoDB records into the database
 *
 * 3.closeConnection - close db connection
 */
public class DBHelper {

    private static final Logger logger = LoggerFactory.getLogger(DBHelper.class);
    private DeviceConfig deviceConfigInstance = DeviceConfig.getInstance();

    private MongoClient mongoClient;
    private DB db;

    public void connectToDB() {

        try {
            this.mongoClient = new MongoClient(deviceConfigInstance.getDeviceConfigProperty("mongodb.ip"));
            this.db = mongoClient.getDB(deviceConfigInstance.getDeviceConfigProperty("mongodb.db.name"));
            logger.info("Connect to database successfully");
        } catch (UnknownHostException e) {
            logger.info(e.getMessage());
        }
    }

    public void insertDeviceInfoToDB(DeviceInfoDB deviceInfoDB)
    {
        ObjectMapper mapper = new ObjectMapper();
        //Convert object to JSON string
        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(deviceInfoDB);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        BasicDBObject obj = (BasicDBObject) JSON.parse(jsonInString);

        DBCollection dbCollection = db.getCollection("deviceinfo");
        dbCollection.save(obj);

        logger.info("dbCollection.count() + " + dbCollection.count());
    }

    public void closeConnection()
    {
        mongoClient.close();

    }
}
