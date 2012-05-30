'Live Log Viewer' JIRA plugin
==============================

This is plugin for Atlassian JIRA which allows realtime view of the server log messages.
Plugin is based on "Live Logging Plugin" 0.9.2 version source codes available at https://marketplace.atlassian.com/plugins/com.atlassian.log with BSD license.

Installation
-------------
This plugin is Type 2, so can be installed and upgraded directly from JIRA Aaministration GUI (over UPM) 

Build
-------------
You need Apache Maven 2.2 and JDK 1.6 installed, then run next command in project root directory

mvn clean package

Plugin .jar file is then placed in /target subfolder


Changelog
-------------

1.0 - 2012-05-30 - changes against original "Live Logging Plugin" 0.9.2
- upgrade to JIRA 5 (User class change, related permission checking change)
- plugin type changed to Type 2 so now can be installed and upgraded over UPM
- in memory log messages store capacity increased from 500 to 1000
- FATAL, ERROR and WARN levels now use separate in memory log messages store with capacity 500, so longer history is available when you filter to these levels
- Log Viewer page - patched error in javascript in LiveLogViewer.vm so refresh starts correctly now when page is shown first time
- Log Viewer page - log messages ordering selector added 
- Log Viewer page - Refresh button added
- Log Viewer page - Exception stacktrace is now written directly to the "Message" column, not to "Category" column on new row. Font is smaller for it.
- Log Viewer page - page title added
- Log Viewer page - filter form submit by Enter key disabled to prevent unintentional page refresh which resets filters settings
- Log Viewer page - increased font size for log message lines to be readable
- added Atlassian maven repo into pom.xml so plugin can be build without any specific maven configuration
- packages changed not to be from com.atlassian domain

- structure of source codes and resources and atlassian-plugin.xml changed to be like standard JIRA plugin
- Log Viewer page now uses standard JIRA admin page template
- Log Viewer page - messages can be filtered as "Exact" or "At least" over levels
- Log Viewer page - message list refresh rate can be changed 