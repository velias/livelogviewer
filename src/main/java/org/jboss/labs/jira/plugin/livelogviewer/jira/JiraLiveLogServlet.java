package org.jboss.labs.jira.plugin.livelogviewer.jira;

import javax.servlet.http.HttpServletRequest;

import org.jboss.labs.jira.plugin.livelogviewer.LiveLogServlet;

import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.user.util.UserManager;

/**
 * @author Brock Janiczak
 * @author Vlastimil Elias
 */
public class JiraLiveLogServlet extends LiveLogServlet {
    private static final long serialVersionUID = 1L;

    private GlobalPermissionManager getGlobalPermissionManager;
    private UserManager userManager;

    @Override
    protected boolean currentUserHasAccess(HttpServletRequest req) {
        return getGlobalPermissionManager.hasPermission(GlobalPermissionKey.ADMINISTER, userManager.getUserByName(req.getRemoteUser()));
    }

    public void setGetGlobalPermissionManager(GlobalPermissionManager getGlobalPermissionManager) {
        this.getGlobalPermissionManager = getGlobalPermissionManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
