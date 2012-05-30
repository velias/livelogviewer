package org.jboss.labs.jira.plugin.livelogviewer.jira;

import javax.servlet.http.HttpServletRequest;

import org.jboss.labs.jira.plugin.livelogviewer.LiveLogServlet;

import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.user.util.UserManager;

/**
 * @author Brock Janiczak
 * @author Vlastimil Elias
 */
public class JiraLiveLogServlet extends LiveLogServlet {
  private static final long serialVersionUID = 1L;

  private PermissionManager permissionManager;
  private UserManager userManager;

  @Override
  protected boolean currentUserHasAccess(HttpServletRequest req) {
    return permissionManager.hasPermission(Permissions.ADMINISTER, userManager.getUser(req.getRemoteUser()));
  }

  /**
   * @param permissionManager the permissionManager to set
   */
  public void setPermissionManager(PermissionManager permissionManager) {
    this.permissionManager = permissionManager;
  }

  /**
   * @param userManager the userManager to set
   */
  public void setUserManager(UserManager userManager) {
    this.userManager = userManager;
  }
}
