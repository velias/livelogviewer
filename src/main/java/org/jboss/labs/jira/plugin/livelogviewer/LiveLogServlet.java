package org.jboss.labs.jira.plugin.livelogviewer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author Brock Janiczak
 * @author Vlastimil Elias
 */
public abstract class LiveLogServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private LiveLogManager manager;

  private static final long TICK = 1000L * 5L;

  public void setLiveLogManager(LiveLogManager manager) {
    this.manager = manager;
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);

  }

  protected abstract boolean currentUserHasAccess(HttpServletRequest req);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    if (!currentUserHasAccess(req)) {
      resp.setStatus(HttpURLConnection.HTTP_FORBIDDEN);
      return;
    }
    long since = -1;
    long before = -1;

    String fromString = req.getParameter("from");
    if (fromString != null && fromString.length() > 0) {
      try {
        since = Long.parseLong(fromString);
        since -= since % TICK;
      } catch (NumberFormatException e) {
      }
    }

    String toString = req.getParameter("to");
    if (toString != null && toString.length() > 0) {
      try {
        before = Long.parseLong(toString);
        before -= since % TICK;
      } catch (NumberFormatException e) {
      }
    }

    if (before == -1) {
      long now = System.currentTimeMillis();
      before = now - (now % TICK);
    }

    String level = req.getParameter("level");

    resp.setContentType("application/xml");
    resp.setHeader("Cache-Control", "no-cache, must-revalidate");
    resp.setHeader("Expires", "Mon, 28 May 2012 01:00:00 GMT");
    PrintWriter writer = resp.getWriter();
    writer.write("<?xml version='1.0' encoding='UTF-8'?>\n");
    writer.write("<events>");
    Layout layout = new NoNSXMLLayout();

    List<LoggingEvent> events = manager.getAppender().getEventsBetween(level, since, before);
    for (LoggingEvent event : events) {
      writer.write(layout.format(event));
    }
    writer.write("</events>");

  }
}
