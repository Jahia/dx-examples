#Example Site Settings
The goal of this module is to set the foundation of standardizing the Site Settings experience.

#Tech Stack
- Jahia DX
- Angular
- Jquery
- Underscore

#Usage
Deploy this module and enable it for a site.  Go to edit mode and site settings tab to administer the example site settings.

#Components

##Settings
This component is used as a type on the node that will save the site settings to the JCR.

##Manage Settings
This component is used to display the settings experience that site admins will interact with.  This component view will provide the necessary HTML markup and load necessary client side libraries and styling.

###Files: resources > exnt_manageSettings > html
- manageSettings.jsp

##Template
This page template will provide the necessary HTML markup with Angular annotations and load the necessary client side libraries and styling.  In addition, the template will have build a JavaScript object with necessary urls and context to be used by Angular.  Once the template is ready, designers can create a Site setting template in Studio.  The designer will place the Manage Settings component in this template.

###Files: resources > jnt_template > html
- template.example.settings.jsp

##Angular

##Settings Directive
The settings directive provides a fragment HTML template and a javascript that is used to bind the settings Angular object to the markup.

###Files: resources > javascript > example > settings > directives > settings
- ma-settings.html
- ma-settings.js

##Setting Service
The settings service provides the retrieve and storing of the settings.

###Files: resources > javascript > example > settings > services
- ma-settings-services.js

##i18n Directive and Service
The i18n service and directive is used by the Settings directive to retrieve proper i18n value based on the user's locale.  Refer to the page template jsp on how the locale information injected.  The i18n services uses the i18n javascript generated during build time of this module.  The plugin will be found in the POM.xml for this module.  In addition, the `ServiceFilter` will place the correct i18n javascript file in the page context to be loaded when the page is served to the user.

###Files: resources > javascript > example > settings > directives
- i18n.js

##Resources
- http://www.learn-angular.org/

##Java

###Settings
This is an enhanced POJO with methods to load, store, and remove the settings.  The load and store of the site settings is performed on the edit workspace.  This approach does not provide a publish separation of settings changes.  If publish workflow is required, then loading of settings needs to be workspace aware.

###SettingsAction
This actions is the endpoint used for the settings posted for storage for the site.  The `settingsActionUrl` is set in the `template.example.settings.jsp` which is set to use the preview base path.

####Resource
- https://www.jahia.com/community/extend/developers-techwiki/content-manipulation/actions

###SettingsChangedListener
This System listener listens for settings changed event.  It loops through the `affectedSites` list in the changed event and starts loading the site settings.

####Resources
- https://www.jahia.com/community/extend/developers-techwiki/events-rules-jobs/system-events

###SettingsConstants
This hold constants that are used by Services and Actions.

###SettingsFilter
This Jahia Filter set the request attributes for the Site Settings

####Resources
- https://www.jahia.com/community/extend/developers-techwiki/rendering/filters

###SettingsListener
This JCR listener listens for node and property changes that is restricted to the `exnt:settings` node type.  This listener will build a setting changed event and publish to the System listener, `SettingsChangedListener`.
 
####Resource
 - https://www.jahia.com/community/extend/developers-techwiki/events-rules-jobs/jcr-events

###SettingsService
This service is normall injected into other object to manage site settings.  The site settings are loaded and placed in `settingsBySiteKeyMap` variable.  This will allow quicker access to aready loaded site settings.  With this approach, listeners are needed to update this map when new site settings are saved to the JCR.  In addition to loading the site settings, this service loads all the i18n.js resources into `supportedLocales` set.  This set is used by the `SettingsFilter` to place the correct i18n JS path.

###Spring
This spring file is used to declare spring objects that registered with the platform.  Once registered, the Spring objects can be injected into other object for use.
###Files: /resources > META-INF > spring
- example-site-settings.xml