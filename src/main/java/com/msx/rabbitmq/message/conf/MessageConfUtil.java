package com.msx.rabbitmq.message.conf;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class MessageConfUtil {

  private static Configuration MessageConf = null;

  private final static String PREFIX = "configuration/";
  private final static String SUFFIX = ".properties";
  private final static String AQUABO_PATH = PREFIX + "RabbitMQ" + SUFFIX;

  // Lsms rest url
  public static final String MULTIAPLL_MESSAGE_SOURCE_LSMS_ALL_USER_URL = "multiapll.message.source.lsms_all_user.url";
  public static final String MULTIAPP_COMMON_MESSAGE_BATCH_LSMS_ALL_USER_ROUTINGKEY_PREFIX = "multiapp.common.message.batch.lsms_all_user.routingkey_prefix";
  public static final String MULTIAPP_COMMON_MESSAGE_BATCH_LSMS_ALL_USER_ROUTINGKEY_SUFFIX = "multiapp.common.message.batch.lsms_all_user.routingkey_suffix";
  public static final String MULTIAPP_COMMON_MESSAGE_BATCH_LSMS_ALL_USER_EXPIRATION = "multiapp.common.message.batch.lsms_all_user.expiration";
  public static final String MULTIAPP_COMMON_MESSAGE_USERNAME = "multiapp.common.message.username";
  public static final String MULTIAPP_COMMON_MESSAGE_PWD = "multiapp.common.message.pwd";
  public static final String MULTIAPP_COMMON_MESSAGE_HOST = "multiapp.common.message.host";
  public static final String MULTIAPP_COMMON_MESSAGE_PORT = "multiapp.common.message.port";
  public static final String MULTIAPP_COMMON_MESSAGE_BATCH_LSMS_ALL_USER_EXCHANGE = "multiapp.common.message.batch.lsms_all_user.exchange";
  public static final String MULTIAPP_COMMON_MESSAGE_VIRTUALHOST = "multiapp.common.message.virtualhost";
  public static final String MULTIAPP_COMMON_MESSAGE_BATCH_SEND_THREAD_NUMBER = "multiapp.common.message.batch.send_thread_number";
  public static final String MULTIAPP_COMMON_MESSAGE_BATCH_TEST_QUEUE_NUMBER = "multiapp.common.message.batch.test_queue_number";
  public static final String MULTIAPP_COMMON_MESSAGE_BATCH_RECIEVE_THREAD_NUMBER = "multiapp.common.message.batch.recieve_thread_number";

  public final static String getAquaBOPath() {
    return AQUABO_PATH;
  }

  public final static Configuration getAquaBOConfiguration() throws Exception {
    if (MessageConf == null) {
      PropertiesConfiguration conf = null;
      try {
        conf = new PropertiesConfiguration(AQUABO_PATH);
      } catch (ConfigurationException e) {
        throw new Exception("Fail to initialize AquaBO configuration",e);
      }
      MessageConf = conf;
    }
    return MessageConf;
  }

  public static String getUserUrl() {
    String result = null;
    try {
      result = getAquaBOConfiguration().getString(
          MULTIAPLL_MESSAGE_SOURCE_LSMS_ALL_USER_URL);
    } catch (Exception e) {
      result = null;
    }
    return result;
  }

  public static String getRoutingKeyPrefix() {
    String result = null;
    try {
      result = getAquaBOConfiguration().getString(
          MULTIAPP_COMMON_MESSAGE_BATCH_LSMS_ALL_USER_ROUTINGKEY_PREFIX);
    } catch (Exception e) {
      result = null;
    }
    return result;
  }

  public static String getRoutingKeySuffix() {
    String result = null;
    try {
      result = getAquaBOConfiguration().getString(
          MULTIAPP_COMMON_MESSAGE_BATCH_LSMS_ALL_USER_ROUTINGKEY_SUFFIX);
    } catch (Exception e) {
      result = null;
    }
    return result;
  }

  public static String getMultiappMessageExpiration() {
    String result = null;
    try {
      result = getAquaBOConfiguration().getString(
          MULTIAPP_COMMON_MESSAGE_BATCH_LSMS_ALL_USER_EXPIRATION);
    } catch (Exception e) {
      result = null;
    }
    return result;
  }

  public static String getRabbitmqUsername() {
    String result = null;
    try {
      result = getAquaBOConfiguration().getString(
          MULTIAPP_COMMON_MESSAGE_USERNAME);
    } catch (Exception e) {
      result = null;
    }
    return result;
  }

  public static String getRabbitmqPwd() {
    String result = null;
    try {
      result = getAquaBOConfiguration().getString(
          MULTIAPP_COMMON_MESSAGE_PWD);
    } catch (Exception e) {
      result = null;
    }
    return result;
  }

  public static String getRabbitmqHost() {
    String result = null;
    try {
      result = getAquaBOConfiguration().getString(
          MULTIAPP_COMMON_MESSAGE_HOST);
    } catch (Exception e) {
      result = null;
    }
    return result;
  }

  public static int getRabbitmqPort() {
    int result = 0;
    try {
      result = getAquaBOConfiguration().getInt(
          MULTIAPP_COMMON_MESSAGE_PORT);
    } catch (Exception e) {
      result = 0;
    }
    return result;
  }

  public static String getRabbitmqExchangeName() {
    String result = null;
    try {
      result = getAquaBOConfiguration().getString(
          MULTIAPP_COMMON_MESSAGE_BATCH_LSMS_ALL_USER_EXCHANGE);
    } catch (Exception e) {
      result = null;
    }
    return result;
  }

  public static String getRabbitmqVirtualhost() {
    String result = null;
    try {
      result = getAquaBOConfiguration().getString(
          MULTIAPP_COMMON_MESSAGE_VIRTUALHOST);
    } catch (Exception e) {
      result = MULTIAPP_COMMON_MESSAGE_VIRTUALHOST;
    }
    return result;
  }

  public static int getSendThreadNumber() {
    int result = 3;
    try {
      result = getAquaBOConfiguration().getInt(
          MULTIAPP_COMMON_MESSAGE_BATCH_SEND_THREAD_NUMBER);
    } catch (Exception e) {
      result = 3;
    }
    return result;
  }

  public static int getTestQueueNumber() {
    int result = 3;
    try {
      result = getAquaBOConfiguration().getInt(
          MULTIAPP_COMMON_MESSAGE_BATCH_TEST_QUEUE_NUMBER);
    } catch (Exception e) {
      result = 3;
    }
    return result;
  }
  

  public static int getRecieveThreadNumber() {
    int result = 3;
    try {
      result = getAquaBOConfiguration().getInt(
          MULTIAPP_COMMON_MESSAGE_BATCH_RECIEVE_THREAD_NUMBER);
    } catch (Exception e) {
      result = 3;
    }
    return result;
  }

}
