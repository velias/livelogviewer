package org.jboss.labs.jira.plugin.livelogviewer;

import java.util.regex.Pattern;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author Brock Janiczak
 */
public class NoNSXMLLayout extends Layout {

  @Override
  public void activateOptions() {
  }

  @Override
  public String format(LoggingEvent event) {
    StringBuffer sb = new StringBuffer();
    sb.append("<event logger=\"").append(event.getLoggerName()).append("\"");
    sb.append(" timestamp=\"").append(event.timeStamp).append("\"");
    sb.append(" level=\"").append(event.getLevel().toString()).append("\"");
    sb.append(" thread=\"").append(event.getThreadName()).append("\"");
    sb.append(">");
    sb.append("<message><![CDATA[");
    Transform.appendEscapingCDATA(sb, sanitizeXmlChars(event.getRenderedMessage()));
    sb.append("]]>");
    sb.append("</message>");
    if (event.getThrowableInformation() != null) {
      sb.append("<throwable>");
      sb.append("<![CDATA[");
      String[] throwableStrRep = event.getThrowableStrRep();
      for (int i = 0; i < throwableStrRep.length; i++) {
        sb.append(throwableStrRep[i]).append("\n");
      }
      sb.append("]]>");
      sb.append("</throwable>");
    }
    sb.append("</event>");

    return sb.toString();
  }

  public static String sanitizeXmlChars(String xml) {
      if (xml == null || ("".equals(xml))) return "";
      // ref : http://www.w3.org/TR/REC-xml/#charsets
      // jdk 7
      Pattern xmlInvalidChars =
        Pattern.compile(
           "[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD\\x{10000}-\\x{10FFFF}]"
        
          );
      return xmlInvalidChars.matcher(xml).replaceAll(".");
    }
  
  @Override
  public boolean ignoresThrowable() {
    return false;
  }

}
