#Example Action
This example provides how to create an Jahia Action and register the action for use.

##Tech Stack
- Jahia DX

##Usage
Install module and enable for site.  Drag Example Action Submitter onto a page.  Click on link to execute action.

##Component

###Example Action Submitter
This component just provides an url so the Jahia Action can be executed.
####Files: resources > exnt_actionSubmitter > html
- actionSubmitter.jsp

##Java

###ExampleAction
This action provides a template to execute any custom logic.  In the comments, there are example results that can be returned to an end user.

####Resources
- https://www.jahia.com/community/extend/developers-techwiki/content-manipulation/actions

####Troubleshooting
Navigate to `http://<host>:<port>/modules/tools/actions.jsp` to view all registered actions.  If you action is not in the list, there might be a problem in the spring configuration of the aciton.