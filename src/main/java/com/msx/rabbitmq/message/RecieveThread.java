package com.msx.rabbitmq.message;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;


public class RecieveThread implements Runnable{

  private Logger logger = LoggerFactory.getLogger(RecieveThread.class);
  Map<String, String> userMap = null;
  ConnectionFactory connFac = new ConnectionFactory() ;
  private Connection conn;
  private int recieveThreadCount;
  private int secondsToStop;
  
  public RecieveThread(int recieveThreadCount,int secondsToStop){
    this.recieveThreadCount=recieveThreadCount;
    this.secondsToStop=secondsToStop;
  }

  public void run() {
    try {
      LSMSDataClient lsms = LSMSDataClient.getInstance();
      MessageManager msg = new MessageManager();
      conn = msg.getConnection();
      logger.debug("Listening queues count is " +lsms.executeMessage.size());
    for (int i = 1; i < (recieveThreadCount + 1); i++) {
      if(i%100==0){
          conn = msg.getConnection();
      }
      Thread t = new Thread(new testRecieveThread(conn,secondsToStop));
      t.start();
    }
    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  class testRecieveThread implements Runnable {

    
    private Connection conn;
    private LSMSDataClient lsms;
    private int secondsToStop;
    private Channel channel;
  
    public testRecieveThread(Connection conn,int secondsToStop){
      this.conn=conn;
      this.secondsToStop = secondsToStop;
    }
    
    public void run() {
      int temp;
      final long startTime = System.currentTimeMillis();
      long breakTime = 0;
      MessageManager msg = null;
      boolean toStop = true;
      boolean timeToStop = true;
      long stopTime = secondsToStop*1000;
      while (toStop) {
        lsms = LSMSDataClient.getInstance();
        String routingKey = (String) lsms.executeMessage.poll();
        String queueName = routingKey+"STB";
        if (routingKey != null) {
          try {
            if (routingKey != null) {
              channel = conn.createChannel();
              QueueingConsumer consumer = new QueueingConsumer(channel);
              channel.basicConsume(queueName, true, consumer);
              while(timeToStop){
                  Delivery delivery = consumer.nextDelivery();
                  String messageContent = new String(delivery.getBody());  
                  breakTime = System.currentTimeMillis();
                  long timeQuantum = breakTime-startTime;
                  if(timeQuantum>=stopTime){
                    timeToStop = false;
                  }
              }
            }
            channel.close();
          } catch (Exception e) {
            logger.error("error", e);
          }
        }
        else {
          toStop = false;
        }
      }
      if(!toStop){
       // msg.finalize(conn);
      }
    }

  }

}
