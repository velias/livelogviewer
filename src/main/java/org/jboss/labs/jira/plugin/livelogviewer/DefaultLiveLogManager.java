package org.jboss.labs.jira.plugin.livelogviewer;

import java.util.Enumeration;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;

/**
 * @author Brock Janiczak
 * @author Vlastimil Elias
 */
public class DefaultLiveLogManager implements LiveLogManager, DisposableBean {

  private static final Logger log = LogManager.getLogger(DefaultLiveLogManager.class);

  private static final String APPENDER_NAME = "LiveLogPluginAppender-" + System.currentTimeMillis();
  private MemoryLogger appender;

  public DefaultLiveLogManager() {
    log.info("DefaultLiveLogManager is starting ....");
    int count = 0;
    if (appender == null) {
      appender = new MemoryLogger();
      appender.setName(APPENDER_NAME);
      BasicConfigurator.configure(appender);
      Enumeration<?> currentLoggers = LogManager.getRootLogger().getLoggerRepository().getCurrentLoggers();
      while (currentLoggers.hasMoreElements()) {
        Logger logger = (Logger) currentLoggers.nextElement();
        if (!logger.getAdditivity()) {
          logger.addAppender(appender);
          count++;
        }
      }
    }
    log.info("DefaultLiveLogManager started, registered for " + count + " loggers.");
  }

  @Override
  public MemoryLogger getAppender() {
    return appender;
  }

  @Override
  public void destroy() {
    if (appender != null) {
      Enumeration<?> currentLoggers = LogManager.getRootLogger().getLoggerRepository().getCurrentLoggers();
      while (currentLoggers.hasMoreElements()) {
        Logger logger = (Logger) currentLoggers.nextElement();
        logger.removeAppender(appender);
      }
      appender.close();
      appender = null;
    }
    log.info("DefaultLiveLogManager is stopped.");
  }
}
