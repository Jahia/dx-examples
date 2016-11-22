#Example Externalize log4j config
The goal of this module is to show how to load additional XML log4j configuration file

##Usage
In your Jahia properties file (/digital-factory-config/jahia/jahia.properties, define the key "logging.altLogFile
.url" and specify as its value the path where to get the additional configuration file
e.g.: logging.altLogFile.url=/org/jahia/modules/example/log/config/additionalLog4j.xml

Deploy your module

##How it is working
The module defines an BundleActivator class. When the module is started, the activator dynamically load the
additional configuration file if it is found