package org.jboss.labs.jira.plugin.livelogviewer.jira;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.webresource.api.assembler.PageBuilderService;

/**
 * Action used to show plugin page in JIRA administration GUI.
 * 
 * @author Lukas Koranda
 * @author Vlastimil Elias (velias at redhat dot com)
 */
public class LiveLogViewerAction extends JiraWebActionSupport {
    
    private static final Logger log = LogManager.getLogger(LiveLogViewerAction.class);

    private PageBuilderService pageBuilderService;

    private static final long serialVersionUID = 1L;

    @Override
    protected String doExecute() throws Exception {
        log.debug("doExecute called");
        return doDefault();
    }
    
    @Override
    public String doDefault() throws Exception {
        log.debug("doDefault called");
        pageBuilderService.assembler().resources().requireWebResource("org.jboss.labs.jira.plugin.livelogviewer:LiveLogResources").requireWebResource("com.atlassian.auiplugin:aui-progress-indicator");
        return "success";
    }

    public PageBuilderService getPageBuilderService() {
        return pageBuilderService;
    }

    public void setPageBuilderService(PageBuilderService pageBuilderService) {
        this.pageBuilderService = pageBuilderService;
    }

}
