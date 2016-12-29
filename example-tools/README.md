#Example Tools
The goal of this module is to show how to add a new page to the Jahia's Tools.

##Usage
Once the module is deployed, you can directly access the new page using such URL :
http://localhost:8080/modules/tools/findDeactivatedCache.jsp

##Note
Unfortunately it is not yet possible to add a new JSP to the /tools menu.
If you add a new page there, you will have to directly access it knowing its name.

##How-to add a new page
Basically you need :
- a module having for parent "jahia-bundles" (see pom.xml)
- your module needs to be defined as a OSGi fragment of the "tools" bundle (see pom.xml)
- just add your JSP(s) in /src/main/resources folder