package com.msx.rabbitmq.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msx.rabbitmq.message.conf.MessageConfUtil;

public class LSMSDataClient  {

  private Logger logger = LoggerFactory.getLogger(LSMSDataClient.class);
  public static LinkedBlockingQueue executeMessage = null;
  public static LinkedBlockingQueue countSendSpeed = null;
  private static LSMSDataClient instance;

  public LSMSDataClient() {
    executeMessage = new LinkedBlockingQueue();
    countSendSpeed = new LinkedBlockingQueue();
  }


  public static LSMSDataClient getInstance()
  {
    if (instance == null)
      try
      {
        instance = new LSMSDataClient();
      } catch (Exception e)
      {} catch (Throwable te)
      {}
    return instance;
  }

  public void getCount(String count){
      try {
        countSendSpeed.put(count);
      } catch (InterruptedException e) {
       
      }
  }

  public void getUserQueue(int ii) throws IOException, InterruptedException {
    String prefix = MessageConfUtil.getRoutingKeyPrefix();
    String suffix = MessageConfUtil.getRoutingKeySuffix();
    int temp=ii;
    if (prefix == null) {
      prefix = "";
    }
    if (suffix == null) {
      suffix = "";
    }
    for (int i = 1000000; i < (1000000+ii); i++) {
      executeMessage.put(prefix + i + suffix);
    }
  }
  
  public void getUserQueue() throws IOException, InterruptedException {
    String prefix = MessageConfUtil.getRoutingKeyPrefix();
    String suffix = MessageConfUtil.getRoutingKeySuffix();
    if (prefix == null) {
      prefix = "";
    }
    if (suffix == null) {
      suffix = "";
    }
    for (int i = 1000000; i < (1000000)+MessageConfUtil.getTestQueueNumber(); i++) {
      executeMessage.put(prefix + i + suffix);
    }
  }


}
