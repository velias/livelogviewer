package com.atlassian.jira.web.action.admin.livelogviewer;

import com.atlassian.jira.web.action.JiraWebActionSupport;

public class LiveLogViewer extends JiraWebActionSupport {

  /**
   * Action used to show plugin page in JIRA administration GUI.
   * 
   * @author Lukas Koranda
   */
  private static final long serialVersionUID = 1L;

  protected String getRedirectPage() {
    return "LiveLogViewer.jspa";
  }

}
