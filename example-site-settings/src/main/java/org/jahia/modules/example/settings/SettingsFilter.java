/**
 * ==========================================================================================
 * =                            JAHIA'S ENTERPRISE DISTRIBUTION                             =
 * ==========================================================================================
 *
 *                                  http://www.jahia.com
 *
 * JAHIA'S ENTERPRISE DISTRIBUTIONS LICENSING - IMPORTANT INFORMATION
 * ==========================================================================================
 *
 *     Copyright (C) 2002-2016 Jahia Solutions Group. All rights reserved.
 *
 *     This file is part of a Jahia's Enterprise Distribution.
 *
 *     Jahia's Enterprise Distributions must be used in accordance with the terms
 *     contained in the Jahia Solutions Group Terms & Conditions as well as
 *     the Jahia Sustainable Enterprise License (JSEL).
 *
 *     For questions regarding licensing, support, production usage...
 *     please contact our team at sales@jahia.com or go to http://www.jahia.com/license.
 *
 * ==========================================================================================
 */
package org.jahia.modules.example.settings;

import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This filter prepares the request and set the js file into the request.
 * @author smomin
 */
public class SettingsFilter extends AbstractFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsFilter.class);
    private SettingsService settingsService;

    /**
     * This method prepares the request and sets the i18n js.
     *
     * @param renderContext
     * @param resource
     * @param chain
     * @return
     * @throws Exception
     */
    @Override
    public String prepare(final RenderContext renderContext,
                          final Resource resource,
                          final RenderChain chain) throws Exception {
        // Get the site settings for the site.
        final Settings settings = settingsService.getSettings(renderContext.getSite().getSiteKey());
        // If the settings are available, then set the has setting to true.
        renderContext.getRequest().setAttribute(SettingsConstants.HAS_SETTINGS, settings != null);

        final String language = renderContext.getUILocale().getLanguage();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("langugage is {}", language);
        }
        // Set the i18n JS variable with the i18n JS file path.
        if (settingsService.getResourceBundleName() != null) {
            renderContext.getRequest().setAttribute(SettingsConstants.I18N_JAVA_SCRIPT_FILE,
                    "i18n/" + settingsService.getResourceBundleName() +
                    (settingsService.getSupportedLocales().contains(language) ? ("_" + language) : "") + ".js");
        }
        return null;
    }

    public void setSettingsService(final SettingsService settingsService) {
        this.settingsService = settingsService;
    }
}
