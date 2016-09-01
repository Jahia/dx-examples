#Example Filter
This example provides how to create an Jahia Action and register the action for use.

##Tech Stack
- Jahia DX

##Usage
Install module and enable for site.  The filter is currently set up to be part of the filter chain for all node types.  Refer to Jahia filter resource on additional configurations.

##Java

###ExampleFilter
This filter adds a `Example Filter executed` to the end of each rendered content in preview or live mode.

####Resources
- hhttps://www.jahia.com/community/extend/developers-techwiki/rendering/filters

####Troubleshooting
Navigate to `http://<host>:<port>/modules/tools/renderFilters.jsp` to view all registered filters.  The user is able to see the filter order as well as enable and disable filters. 