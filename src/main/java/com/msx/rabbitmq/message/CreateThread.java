package com.msx.rabbitmq.message;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msx.rabbitmq.message.conf.MessageConfUtil;
import com.rabbitmq.client.Connection;


public class CreateThread implements Runnable{
  
  private Logger logger = LoggerFactory.getLogger(CreateThread.class);
  private int createThreadCount;
  private String threadName;
  
  public CreateThread(int createThreadCount){
    this.createThreadCount = createThreadCount;
    
  }

  public void run() {
    for (int i = 1; i < (createThreadCount + 1); i++) {
      threadName = "CreateThread" + i;
      Thread t = new Thread(new DoCreateThread(threadName));
      t.start();
    }

  }

  class DoCreateThread implements Runnable {
    
    private int createQueueCount;

    public DoCreateThread(String threadName){
      this.testThread=threadName;
    }

    private Connection conn;
    private String testThread;
    private LSMSDataClient lsms;
    long startTime = System.currentTimeMillis();


    public void run() {
      MessageManager msg = null;
      boolean toStop = true;
      String exchange = MessageConfUtil.getRabbitmqExchangeName();
      try {
        msg = new MessageManager();
      } catch (Exception e) {
        logger.debug("[" + testThread + "]  new MessageManager() error!");
        logger.error("error", e);
      }
      conn = msg.getConnection();
      while (toStop) {
        lsms = LSMSDataClient.getInstance();
        String routingKey = (String) lsms.executeMessage.poll();
        String queueName = routingKey+"STB";
        if (routingKey != null) {
          try {
            if (routingKey != null) {
              msg.createQueueAndBind(conn,queueName,exchange, "direct", routingKey);
              createQueueCount++;
            }
          } catch (Exception e) {
            logger.debug("[" + testThread + "] Send message to rabbitmq error!");
            logger.error("error", e);
          }
        }
        else {
          toStop = false;
        }
      }
      if(!toStop){
        msg.finalize(conn);
      }
      long endTime = System.currentTimeMillis();
      SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
      String time = format.format(endTime - startTime);
      logger.debug("[" + testThread + "] CreateThread is finished!cost time is " + (endTime - startTime)+" ms"+" this thread create queues number is "+createQueueCount);
    }

  }

}
