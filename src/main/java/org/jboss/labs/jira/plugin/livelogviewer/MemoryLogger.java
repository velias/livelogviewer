package org.jboss.labs.jira.plugin.livelogviewer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.CyclicBuffer;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author Brock Janiczak
 * @author Vlastimil Elias
 */
public class MemoryLogger extends AppenderSkeleton {

  private static final Logger log = LogManager.getLogger(MemoryLogger.class);

  private CyclicBuffer entries = new CyclicBuffer(1000);
  private CyclicBuffer entries_fatal = new CyclicBuffer(500);
  private CyclicBuffer entries_error = new CyclicBuffer(500);
  private CyclicBuffer entries_warn = new CyclicBuffer(500);

  @Override
  protected synchronized void append(LoggingEvent loggingEvent) {
    try {
      entries.add(loggingEvent);
      if (loggingEvent.getLevel().isGreaterOrEqual(Level.FATAL)) {
        entries_fatal.add(loggingEvent);
      }
      if (loggingEvent.getLevel().isGreaterOrEqual(Level.ERROR)) {
        entries_error.add(loggingEvent);
      }
      if (loggingEvent.getLevel().isGreaterOrEqual(Level.WARN)) {
        entries_warn.add(loggingEvent);
      }
    } catch (Error e) {
      // nothing to do, just to be sure no error will be thrown from here
    }
  }

  @Override
  public void close() {
    entries.resize(1);
    entries_fatal.resize(1);
    entries_error.resize(1);
    entries_warn.resize(1);
  }

  @Override
  public boolean requiresLayout() {
    return false;
  }

  int cnt = 0;

  public synchronized List<LoggingEvent> getEventsBetween(String level, long startTime, long endTime) {

    // TODO remove
    switch (cnt % 3) {
    case 0:
      log.error("Test error", new Exception());
    case 1:
      log.fatal("Test fatal", new Exception());
    case 2:
      log.warn("Test warn", new Exception());
    }
    cnt++;

    List<LoggingEvent> matches = new ArrayList<LoggingEvent>();

    CyclicBuffer set = entries;
    if ("FATAL".equalsIgnoreCase(level))
      set = entries_fatal;
    if ("ERROR".equalsIgnoreCase(level))
      set = entries_error;
    if ("WARN".equalsIgnoreCase(level))
      set = entries_warn;

    log.debug("getEventsBetween level=" + level + " start=" + startTime + " end=" + endTime + " numOfAvailableEntries="
        + set.length());

    for (int i = 0; i < set.length(); i++) {
      LoggingEvent event = (LoggingEvent) set.get(i);
      if ((startTime != -1 && event.timeStamp < startTime) || (endTime != -1 && event.timeStamp > endTime)) {
        continue;
      }
      matches.add(event);
    }

    return matches;
  }

}
