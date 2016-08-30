package org.jahia.modules.example.settings;

import org.apache.commons.lang.StringUtils;
import org.jahia.data.templates.JahiaTemplatesPackage;
import org.jahia.services.content.JCRCallback;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.JCRTemplate;
import org.jahia.services.content.decorator.JCRSiteNode;
import org.jahia.services.sites.JahiaSitesService;
import org.jahia.services.templates.JahiaModuleAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This service handles management of settings node for a site.
 */
public class SettingsService implements InitializingBean, JahiaModuleAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsService.class);
    private static final SettingsService instance = new SettingsService();
    private Map<String, Settings> settingsBySiteKeyMap = new HashMap<>();
    private String resourceBundleName;
    private Set<String> supportedLocales = Collections.emptySet();

    /**
     *
     */
    private SettingsService() {
        super();
    }

    /**
     *
     * @return
     */
    public static SettingsService getInstance() {
        return instance;
    }

    /**
     * @param siteKey
     * @throws RepositoryException
     */
    public void loadSettings(final String siteKey) throws RepositoryException {
        JCRTemplate.getInstance().doExecuteWithSystemSession(new JCRCallback<Object>() {

            /**
             * This method loads all the site settings or a site settings.
             *
             * @param session
             * @return
             * @throws RepositoryException
             */
            @Override
            public Object doInJCR(final JCRSessionWrapper session) throws RepositoryException {
                // If no site key, then clear settings in the map and reload the settings for all the sites.
                if (siteKey == null) {
                    settingsBySiteKeyMap.clear();
                    for (final JCRSiteNode siteNode : JahiaSitesService.getInstance().getSitesNodeList(session)) {
                        loadSettings(siteNode);
                    }
                }
                // If site keys is available, then only update the settings for that particular site.
                else {
                    settingsBySiteKeyMap.remove(siteKey);
                    if (session.nodeExists("/sites/" + siteKey)) {
                        loadSettings(JahiaSitesService.getInstance().getSiteByKey(siteKey, session));
                    }
                }
                return null;
            }

            /**
             * This method loads the settings from the JCR.
             *
             * @param siteNode
             * @throws RepositoryException
             */
            private void loadSettings(final JCRSiteNode siteNode) throws RepositoryException {
                boolean loaded;
                try {
                    // Loads the settings for that site key.
                    final Settings settings = new Settings(siteNode.getSiteKey());
                    loaded = settings.load();
                    // If settings are loaded, then set the settings with the site key in the map.
                    if (loaded) {
                        settingsBySiteKeyMap.put(siteNode.getSiteKey(), settings);
                    }
                } catch (Exception e) {
                    LOGGER.error("Error while loading settings from "
                            + siteNode.getPath() + "/"
                            + SettingsConstants.SETTINGS_NODE_NAME, e);
                }
            }
        });
    }

    /**
     * This method sets the settings values in the JCR.
     *
     * @param siteKey
     * @param setting1
     * @return
     * @throws IOException
     */
    public Settings setSettings(final String siteKey,
                                final String setting1) throws IOException {
        final Settings settings = new Settings(siteKey);
        settings.setSetting1(setting1);

        // refresh and save settings
        settings.store();

        // Add settings and site key to the map.
        settingsBySiteKeyMap.put(siteKey, settings);
        return settings;
    }

    public Settings getSettings(final String siteKey) {
        return settingsBySiteKeyMap.get(siteKey);
    }

    /**
     * This method gets the resource bundle for the module.
     *
     * @param jahiaTemplatesPackage
     */
    @Override
    public void setJahiaModule(final JahiaTemplatesPackage jahiaTemplatesPackage) {
        final org.springframework.core.io.Resource[] resources;
        final String resourceBundleName = jahiaTemplatesPackage.getResourceBundleName();
        if (resourceBundleName != null) {
            // Get resource bundle and create a
            this.resourceBundleName = StringUtils.substringAfterLast(resourceBundleName, ".") + "-i18n";

            // Get the resources from the "javascript/i18n" folder relative to the module.
            resources = jahiaTemplatesPackage.getResources("javascript/i18n");
            supportedLocales = new HashSet<>();

            // Loop through the resource found
            for (final org.springframework.core.io.Resource resource : resources) {
                final String resourceFilename = resource.getFilename();

                // If the resource file name matches with the resource bundle name, then parse the file name to get the
                // locale and add to the support locales set.
                if (resourceFilename.startsWith(this.resourceBundleName)) {
                    final String locale = StringUtils.substringBetween(resourceFilename, this.resourceBundleName, ".js");
                    supportedLocales.add(locale.length() > 0 ? StringUtils.substringAfter(locale, "_") : StringUtils.EMPTY);
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public Set<String> getSupportedLocales() {
        return supportedLocales;
    }

    /**
     *
     * @return
     */
    public String getResourceBundleName() {
        return resourceBundleName;
    }

    /**
     * This method reloads all the site settings in the platform.
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        loadSettings(null);
    }
}
