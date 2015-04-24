package com.msx.rabbitmq.message;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msx.rabbitmq.message.MessageManager;
import com.msx.rabbitmq.message.conf.MessageConfUtil;

public class RestSendMessage {

  private Logger logger = LoggerFactory.getLogger(RestSendMessage.class);

  public String sendMessage(String content,String queueCount,String sendThreadCount,String messageTTL) {
    long startTime = System.currentTimeMillis();
    String result;
    String defaultExchangeName = "etv.exchange";
    String message = "";
    MessageManager msg = null;
      String exchange = MessageConfUtil.getRabbitmqExchangeName();
      if (exchange == null || exchange.equalsIgnoreCase("")) {
        exchange = defaultExchangeName;
      }
      try {
        msg = new MessageManager();
      } catch (Exception e) {
        logger.error("error", e);
        result = "RabbitMQ connection is error,plesae check rabbitmq configuration!";
        return result;
      }
      String perfix = MessageConfUtil.getRoutingKeyPrefix();
      String suffix = MessageConfUtil.getRoutingKeySuffix();
      if (perfix == null) {
        perfix = "";
      }
      if (suffix == null) {
        suffix = "";
      }
      if (messageTTL == null || messageTTL.equalsIgnoreCase("")) {
        messageTTL = "0";
      }
      try {
        LSMSDataClient.getInstance().getUserQueue(Integer.parseInt(queueCount));
    } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      logger.debug("[RestSendMessage] messagettl = " + messageTTL);
      message = content;
      MultipleSendThread ttt = new MultipleSendThread(message, messageTTL,Integer.parseInt(sendThreadCount));
      Thread tt = new Thread(ttt);
      tt.start();
    logger.debug("[RestSendMessage] Message is sent to binding queues!");
    result = "ok";
    return result;
  }
  
  public String createAndBind(String queueCount,String createThreadCount) {
    String result =null;
    try {
        LSMSDataClient.getInstance().getUserQueue(Integer.parseInt(queueCount));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Thread t = new Thread(new CreateThread(Integer.parseInt(createThreadCount)));
    t.start();
    result = "OK";
    logger.debug("[RestSendMessage] create And Bind is finished!");
    return result;
  }
  
  public String recieve(String recieveThreadCount,String secondsToStop) {
    String result =null;
    try {
        LSMSDataClient.getInstance().getUserQueue(Integer.parseInt(recieveThreadCount));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Thread t = new Thread(new RecieveThread(Integer.parseInt(recieveThreadCount),Integer.parseInt(secondsToStop)));
    t.start();
    result = "OK";
    logger.debug("[RestSendMessage] starting listening finished!");
    return result;
  }
  

}
