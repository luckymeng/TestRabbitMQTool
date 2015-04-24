package com.msx.rabbitmq.message;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msx.rabbitmq.message.conf.MessageConfUtil;
import com.rabbitmq.client.Connection;

public class MultipleSendThread implements Runnable {

  private Logger logger = LoggerFactory.getLogger(MultipleSendThread.class);
  private String content;
  private String messageTTL;
  private int sendThreadCount;
  Map<String, String> userMap = null;

  public MultipleSendThread(String content, String messageTTL,int sendThreadCount) {
    this.content = content;
    this.messageTTL = messageTTL;
    this.sendThreadCount = sendThreadCount;
  }

  public void run() {
    for (int i = 1; i < (sendThreadCount + 1); i++) {
      Thread t = new Thread(new SendThread(content, messageTTL, "SendThread" + i));
      t.start();
    }

  }

  class SendThread implements Runnable {

    private String content;
    private Connection conn;
    private String threadName;
    private LSMSDataClient lsms;
    private String messageTTL;
    long startTime = System.currentTimeMillis();
    private int messageCount;

    public SendThread(String content, String messageTTL, String threadName) {
      this.content = content;
      this.messageTTL = messageTTL;
      this.threadName = threadName;
    }

    public void run() {
      MessageManager msg = null;
      boolean toStop = true;
      String exchange = MessageConfUtil.getRabbitmqExchangeName();
      try {
        msg = new MessageManager();
      } catch (Exception e) {
        logger.debug("[" + threadName + "]  new MessageManager() error!");
        logger.error("error", e);
      }
      conn = msg.getConnection();
      while (toStop) {
        lsms = LSMSDataClient.getInstance();
        String routingKey = (String) lsms.executeMessage.poll();
        if (routingKey != null) {
          // logger.debug("["+threadName+"] routingKey = "+routingKey);
          try {
            if (routingKey != null) {
              msg.sendMessageByRoutingKeyNoClosed(conn, exchange, routingKey, content, messageTTL);
              messageCount++;
              // logger.debug("["+threadName+"] Send message to rabbitmq routingkey = "
              // +routingKey);
            }
          } catch (Exception e) {
            logger.debug("[" + threadName + "] Send message to rabbitmq error!");
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
      int sendSpeed = (int)Math.round((double)messageCount*((double)1000/((double)endTime - (double)startTime)));
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[");
      stringBuilder.append(threadName);
      stringBuilder.append("] SendThread is finished!cost time is ");
      stringBuilder.append((endTime - startTime));
      stringBuilder.append("ms ");
      stringBuilder.append(" send message count is ");
      stringBuilder.append(messageCount);
      stringBuilder.append(" send message speed is ");
      stringBuilder.append(sendSpeed);
      stringBuilder.append("/s");
      logger.debug(stringBuilder.toString());
      LSMSDataClient.getInstance().getCount(String.valueOf(sendSpeed));
      System.out.println(stringBuilder.toString());
    }

  }

}
