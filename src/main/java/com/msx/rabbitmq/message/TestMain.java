package com.msx.rabbitmq.message;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMain {

  private int createQueueCount;
  private int createThreadCount;
  private int recieveThreadCount;
  private int secondsToStop;
  private String messageContent;
  private int sendQueueCount;
  private int sendThreadCount;
  private int messageTTL;
  private int firstChoice;
  private static Logger logger = LoggerFactory.getLogger(TestMain.class);
  
  public static void main(String args[]){
    TestMain tm = new TestMain();
    RestSendMessage rsm = new RestSendMessage();
    System.out.println("Create queues please push 1..");
    System.out.println("listening queues please push 2..");
    System.out.println("Sending message to queues please push 3..");
    Scanner scan = new Scanner(System.in);
    Integer firstParam = scan.nextInt();
    tm.setFirstChoice((int) firstParam);
    if(tm.getFirstChoice()==1){
      System.out.println("please input create queue count.");
      Scanner scan1 = new Scanner(System.in);
      Integer secondParam = scan1.nextInt();
      tm.setCreateQueueCount((int)secondParam);
      System.out.println("please input create thread count.");
      Scanner scan2 = new Scanner(System.in);
      Integer thirdParam = scan2.nextInt();
      tm.setCreateThreadCount((int)thirdParam);
      String result = rsm.createAndBind(String.valueOf(tm.getCreateQueueCount()),String.valueOf(tm.getCreateThreadCount()));
      if(result.equalsIgnoreCase("OK")){
        System.out.println("create action is finished!");
      }
    }
    if(tm.getFirstChoice()==2){
      System.out.println("please input listening queue count.");
      Scanner scan3 = new Scanner(System.in);
      Integer fourParam = scan3.nextInt();
      tm.setRecieveThreadCount((int)fourParam);
      String result = rsm.recieve(String.valueOf(tm.getRecieveThreadCount()), "100000");
      if(result.equalsIgnoreCase("OK")){
        System.out.println("listening action is finished!");
      }
    }
    if(tm.getFirstChoice()==3){
      System.out.println("please input sending queue count.");
      Scanner scan4 = new Scanner(System.in);
      Integer fiveParam = scan4.nextInt();
      tm.setSendQueueCount((int)fiveParam);
      System.out.println("please input sending thread count.");
      Scanner scan5 = new Scanner(System.in);
      Integer sixParam = scan5.nextInt();
      tm.setSendThreadCount((int)sixParam);
      System.out.println("please input message time to live (/ms).");
      Scanner scan6 = new Scanner(System.in);
      Integer sevenParam = scan6.nextInt();
      tm.setMessageTTL((int)sevenParam);
      System.out.println("please input message  content.");
      Scanner scan7 = new Scanner(System.in);
      String eightParam = scan7.nextLine();
      tm.setMessageContent(eightParam);
      logger.debug("Starting threads to send message.");
      logger.debug(" messageContent = " + tm.getMessageContent());
      String result = rsm.sendMessage(tm.getMessageContent(), String.valueOf(tm.getSendQueueCount()), String.valueOf(tm.getSendThreadCount()),String.valueOf(tm.getMessageTTL()));
      boolean stop = true;
      LSMSDataClient lsms = LSMSDataClient.getInstance();
      while(stop){
        if(lsms.countSendSpeed.size()==(int)sixParam){
          stop=false;
        }
      }
      int countAll = 0;
      while (!stop) {
        String threadSpeed = (String) lsms.countSendSpeed.poll();
        if (threadSpeed != null) {
          int countOne = Integer.parseInt(threadSpeed);
          countAll+=countOne;
        }
        else {
          stop = true;
        }
      }
      String out = "sending message action is finished! All threads sending speed is "+countAll+"/s";
      System.out.println(out);
      logger.debug(out);
    }
  }


  
  public int getCreateQueueCount() {
    return createQueueCount;
  }


  
  public void setCreateQueueCount(int createQueueCount) {
    this.createQueueCount = createQueueCount;
  }


  
  public int getCreateThreadCount() {
    return createThreadCount;
  }


  
  public void setCreateThreadCount(int createThreadCount) {
    this.createThreadCount = createThreadCount;
  }


  
  public int getRecieveThreadCount() {
    return recieveThreadCount;
  }


  
  public void setRecieveThreadCount(int recieveThreadCount) {
    this.recieveThreadCount = recieveThreadCount;
  }


  
  public int getSecondsToStop() {
    return secondsToStop;
  }


  
  public void setSecondsToStop(int secondsToStop) {
    this.secondsToStop = secondsToStop;
  }


  
  public String getMessageContent() {
    return messageContent;
  }


  
  public void setMessageContent(String messageContent) {
    this.messageContent = messageContent;
  }


  
  public int getSendQueueCount() {
    return sendQueueCount;
  }


  
  public void setSendQueueCount(int sendQueueCount) {
    this.sendQueueCount = sendQueueCount;
  }


  
  public int getSendThreadCount() {
    return sendThreadCount;
  }


  
  public void setSendThreadCount(int sendThreadCount) {
    this.sendThreadCount = sendThreadCount;
  }


  
  public int getMessageTTL() {
    return messageTTL;
  }


  
  public void setMessageTTL(int messageTTL) {
    this.messageTTL = messageTTL;
  }


  
  public int getFirstChoice() {
    return firstChoice;
  }


  
  public void setFirstChoice(int firstChoice) {
    this.firstChoice = firstChoice;
  }

  
  
}
