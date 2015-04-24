package com.msx.rabbitmq.message;

import java.io.IOException;
import java.util.List;
import java.util.Random;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msx.rabbitmq.message.conf.MessageConfUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MessageManager {

  final static private Logger logger = LoggerFactory.getLogger(MessageManager.class);
  final static private String EXCHANGE_NAME = "aquabo_messageManager_default_exchange";
  final static private String EXCHANGE_TYPE = "direct";
  private ConnectionFactory factory;
  private Address[] addrArr = null;
  
  public MessageManager() throws Exception{
    this.initialize();
  }

  public void initialize() throws Exception {
   factory = new ConnectionFactory();
   factory.setPassword(MessageConfUtil.getRabbitmqPwd());
    factory.setUsername(MessageConfUtil.getRabbitmqUsername());
    //factory.setVirtualHost(MessageConfUtil.getRabbitmqVirtualhost());
    String rabbitmqHost = MessageConfUtil.getRabbitmqHost();
    int rabbitmqPort = MessageConfUtil.getRabbitmqPort();
    String[] hostArray = null;
    if (rabbitmqHost != null && rabbitmqHost.trim().length() > 0) {
      hostArray = rabbitmqHost.split(";");
      addrArr = new Address[hostArray.length];
      for (int i = 0; i < hostArray.length; i++) {
       // addrArr = new Address[hostArray.length];
        Address tempAddr = new Address(hostArray[i], rabbitmqPort);
        addrArr[i] = tempAddr;
       // logger.debug("{}:{}", "get rabbitmq hostArray,host[" + i + "]", addrArr[i]);
      }
    }
  }
  
  public Connection getConnection(){
    Connection conn = null;
     try {
       Address[] temp = new Address[1];
       Random rand = new Random();
       int i = rand.nextInt(addrArr.length);
       if(i>1){
       temp[0]=addrArr[i];
       addrArr[i]=addrArr[0];
       addrArr[0]=temp[0];
       }
       conn = factory.newConnection(addrArr);
      } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return conn;
   }


  public void finalize(Connection conn) {
    if (conn != null && conn.isOpen()) {
      try {
        conn.close();
        conn = null;
      } catch (IOException e) {
        logger.error("", e);
      }
    }
  }
  
  @SuppressWarnings("deprecation")
  public void sendMessageByRoutingKeyNoClosed(Connection conn,String exchange, String routingKey, String message, String messageTTL)
      {
      Channel channel = null;
      try {
        channel = conn.createChannel();
      // channel.queueDeclare("", true, false, false, null);
      AMQP.BasicProperties properties = new AMQP.BasicProperties();
      if (messageTTL != null && !messageTTL.equalsIgnoreCase("")) {
        if(Integer.parseInt(messageTTL)<0){
          properties.setExpiration("0");
        }
        else{
          properties.setExpiration(messageTTL);
        }
      }
      else{
        properties.setExpiration("0");
      }
      channel.basicPublish(exchange, routingKey,
          properties, message.getBytes("utf-8"));
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        try {
          if (channel != null && channel.isOpen()) {
            channel.close();
          }
        } catch (IOException e) {
          //
        }
      }
  }

  @SuppressWarnings("deprecation")
  public void sendMessageByQueueName(String queueName, String message, String messageTTL) throws Exception {
    Channel channel = null;
    Connection connection = null;
    try {
      connection = factory.newConnection(addrArr);
      channel = connection.createChannel();
      logger.debug("MessageManager send message[{}] to {}", message, queueName);
      channel.queueDeclare(queueName, true, false, false, null);
      AMQP.BasicProperties properties = new AMQP.BasicProperties();
      if (messageTTL != null && !messageTTL.equalsIgnoreCase("")) {
        if(messageTTL.equalsIgnoreCase("-1")||Integer.parseInt(messageTTL)<0){
        }
        else{
          properties.setExpiration(messageTTL);
        }
      }
      else {
        properties.setExpiration("0");
      }
      channel.basicPublish("", queueName,
          properties, message.getBytes("utf-8"));
      logger.debug("MessageManager sent message[{}] to {}", message, queueName);
    } finally {
      try {
        if (channel != null && channel.isOpen()) {
          channel.close();
        }
      } catch (IOException e) {
        //
      }
      try {
        if (connection != null && connection.isOpen()) {
          connection.close();
          connection = null;
        }
      } catch (IOException e) {}
    }
  }

  @SuppressWarnings("deprecation")
  public void sendMessageByRoutingKey(String exchange, String routingKey, String message, String messageTTL)
      throws Exception {
    Channel channel = null;
    Connection connection = null;
    try {
      connection = factory.newConnection(addrArr);
      channel = connection.createChannel();
      //logger.debug("MessageManager send message[{}] to {}", message, routingKey);
      // channel.queueDeclare("", true, false, false, null);
      AMQP.BasicProperties properties = new AMQP.BasicProperties();
      if (messageTTL != null && !messageTTL.equalsIgnoreCase("")) {
        if(messageTTL.equalsIgnoreCase("-1")||Integer.parseInt(messageTTL)<0){
        }
        else{
          properties.setExpiration(messageTTL);
        }
      }
      else{
        properties.setExpiration("0");
      }
      channel.basicPublish(exchange, routingKey,
          properties, message.getBytes("utf-8"));
      //logger.debug("MessageManager sent message[{}] to {}", message, routingKey);
    } finally {
      try {
        if (channel != null && channel.isOpen()) {
          channel.close();
        }
      } catch (IOException e) {
        //
      }
      try {
        if (connection != null && connection.isOpen()) {
          connection.close();
          connection = null;
        }
      } catch (IOException e) {}
    }
  }
  
  @SuppressWarnings("deprecation")
  public void sendMessageByQueueList(List<String> queue, String message, String messageTTL) throws Exception {
    Channel channel = null;
    Connection connection = null;
    try {
      connection = factory.newConnection(addrArr);
      channel = connection.createChannel();
      logger.debug("MessageManager send message[{}] to {}", message, queue.size());
      for(int i = 0;i < queue.size(); i++){
        channel.queueDeclare(queue.get(i), true, false, false, null);
        
      AMQP.BasicProperties properties = new AMQP.BasicProperties();
      if (messageTTL != null && !messageTTL.equalsIgnoreCase("")) {
        properties.setExpiration(messageTTL);
      }
      else {
        properties.setExpiration("0");
      }
      channel.basicPublish("", queue.get(i),
          properties, message.getBytes("utf-8"));
      logger.debug("MessageManager sent message[{}] to {}", message, queue.get(i));
      }
    } finally {
      try {
        if (channel != null && channel.isOpen()) {
          channel.close();
        }
      } catch (IOException e) {
        //
      }
      try {
        if (connection != null && connection.isOpen()) {
          connection.close();
          connection = null;
        }
      } catch (IOException e) {}
    }
  }

  public void deleteQueue(String queueName) throws Exception {
    Channel channel = null;
    Connection connection = null;
    connection = factory.newConnection(addrArr);
    channel = connection.createChannel();
    try {
      channel.queueDelete(queueName);
    } catch (IOException e) {
      logger.error("", e);
    } catch (Throwable t) {
      logger.error("", t);
    } finally {
      try {
        if (channel != null && channel.isOpen()) {
          channel.close();
        }
      } catch (IOException e) {

      }
      try {
        if (connection != null && connection.isOpen()) {
          connection.close();
          connection = null;
        }
      } catch (IOException e) {

      }
    }

  }
  
  public void createQueueAndBind(Connection connection,String queueName, String exchangeName, String exchangeType,String routingKey) throws Exception {
    Channel channel = null;
    try {
      channel = connection.createChannel();
      String newExchangType=null;
      String newExchangName=null;
      if(exchangeName!=null&&!exchangeName.equalsIgnoreCase("")){
        newExchangName=exchangeName;
      }
      else{
        newExchangName=EXCHANGE_NAME;
      }
      if(exchangeType!=null&&!exchangeType.equalsIgnoreCase("")){
        newExchangType=exchangeType;
      }
      else{
        newExchangType=EXCHANGE_TYPE;
      }
      channel.queueDeclare(queueName, true, false, false, null);
      channel.exchangeDeclare(newExchangName,newExchangType, true);
      channel.queueBind(queueName, exchangeName, routingKey);
    } catch (IOException e) {
      logger.error("", e);
    } catch (Throwable t) {
      logger.error("", t);
    } finally {
      try {
        if (channel != null && channel.isOpen()) {
          channel.close();
        }
      } catch (IOException e) {

      }
    }

  }

  public void createQueue(String queueName) throws Exception {
    Channel channel = null;
    Connection connection = null;
    try {
      connection = factory.newConnection(addrArr);
      channel = connection.createChannel();
      channel.queueDeclare(queueName, true, false, false, null);
    } catch (IOException e) {
      logger.error("", e);
    } catch (Throwable t) {
      logger.error("", t);
    } finally {
      try {
        if (channel != null && channel.isOpen()) {
          channel.close();
        }
      } catch (IOException e) {

      }
      try {
        if (connection != null && connection.isOpen()) {
          connection.close();
          connection = null;
        }
      } catch (IOException e) {

      }
    }

  }

  public void bindQueue(String queueName, String exchangeName, String exchangeType,String routingKey) throws Exception {
    Channel channel = null;
    Connection connection = null;
    connection = factory.newConnection(addrArr);
    channel = connection.createChannel();
    String newExchangType=null;
    String newExchangName=null;
    if(exchangeName!=null&&!exchangeName.equalsIgnoreCase("")){
      newExchangName=exchangeName;
    }
    else{
      newExchangName=EXCHANGE_NAME;
    }
    if(exchangeType!=null&&!exchangeType.equalsIgnoreCase("")){
      newExchangType=exchangeType;
    }
    else{
      newExchangType=EXCHANGE_TYPE;
    }
    try {
      channel.exchangeDeclare(newExchangName,newExchangType, true);
      channel.queueBind(queueName, exchangeName, routingKey);
    } catch (IOException e) {
      logger.error("", e);
    } catch (Throwable t) {
      logger.error("", t);
    } finally {
      try {
        if (channel != null && channel.isOpen()) {
          channel.close();
        }
      } catch (IOException e) {

      }
      try {
        if (connection != null && connection.isOpen()) {
          connection.close();
          connection = null;
        }
      } catch (IOException e) {

      }
    }

  }
  
}
