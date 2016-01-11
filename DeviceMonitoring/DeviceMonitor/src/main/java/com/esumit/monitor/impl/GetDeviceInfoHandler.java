package com.esumit.monitor.impl;

import com.mongodb.*;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.UnknownHostException;

/**
 * Created by eSumit on 10/28/15.
 *
 * REST GET Request handled by the GetDeviceInfoHandler, which in turn call getDeviceInfo. It connects to the mongoDB,
 * retrive the record and return to the caller.
 */
@Service
public class GetDeviceInfoHandler {

    public Response getDeviceInfo( )
    {

        AnnotationConfigWebApplicationContext
        // To connect to mongodb server
        MongoClient mongoClient = null;

        try {
            mongoClient = new MongoClient("localhost", 27017);

        }catch (UnknownHostException e) {
            e.printStackTrace();
        }
            // Now connect to your databases
            DB db = mongoClient.getDB("DeviceInfo");
            DBCollection dbCollection = db.getCollection("deviceinfo");
            DBCursor singleElement = dbCollection.find().limit(1).sort(new BasicDBObject("_id", -1));


            // DBCursor cursor = db.dbCollection.find().sort({x:1});

            String jsonString = singleElement.next().toString();
            mongoClient.close();

            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();

    }
}
