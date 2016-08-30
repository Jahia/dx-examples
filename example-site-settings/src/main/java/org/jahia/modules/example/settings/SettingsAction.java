/**
 * ==========================================================================================
 * =                            JAHIA'S ENTERPRISE DISTRIBUTION                             =
 * ==========================================================================================
 * <p>
 * http://www.jahia.com
 * <p>
 * JAHIA'S ENTERPRISE DISTRIBUTIONS LICENSING - IMPORTANT INFORMATION
 * ==========================================================================================
 * <p>
 * Copyright (C) 2002-2016 Jahia Solutions Group. All rights reserved.
 * <p>
 * This file is part of a Jahia's Enterprise Distribution.
 * <p>
 * Jahia's Enterprise Distributions must be used in accordance with the terms
 * contained in the Jahia Solutions Group Terms & Conditions as well as
 * the Jahia Sustainable Enterprise License (JSEL).
 * <p>
 * For questions regarding licensing, support, production usage...
 * please contact our team at sales@jahia.com or go to http://www.jahia.com/license.
 * <p>
 * ==========================================================================================
 */
package org.jahia.modules.example.settings;

import com.google.common.io.CharStreams;
import org.apache.commons.lang.StringUtils;
import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class SettingsAction extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsAction.class);
    private SettingsService settingsService;

    /**
     * This method handles saving.
     *
     * @param request
     * @param renderContext
     * @param resource
     * @param session
     * @param parameters
     * @param urlResolver
     * @return
     * @throws Exception
     */
    @Override
    public ActionResult doExecute(final HttpServletRequest request,
                                  final RenderContext renderContext,
                                  final Resource resource,
                                  final JCRSessionWrapper session,
                                  final Map<String, List<String>> parameters,
                                  final URLResolver urlResolver) throws Exception {
        try {
            // Get the request chars and set to response text.
            final String responseText = CharStreams.toString(request.getReader());
            final JSONObject settings;
            final Settings serverSettings;
            final String siteKey = renderContext.getSite().getSiteKey();

            // if payload has content, it means an update.
            if (StringUtils.isNotEmpty(responseText)) {
                // Create JSONObject from responseText.
                settings = new JSONObject(responseText);

                // Get the settings from the JCR.
                final Settings oldSettings = settingsService.getSettings(siteKey);

                // Check against the settings from the JCR with the new setting property that has been posted.
                final Boolean enabled = getSettingOrDefault(settings, SettingsConstants.ENABLED,
                        (oldSettings != null && oldSettings.getEnabled()));
                final String settings1 = getSettingOrDefault(settings, SettingsConstants.SETTING_1,
                        (oldSettings != null ? oldSettings.getSetting1() : ""));

                // If service is enabled, then set the settings in the JCR.
                if (enabled) {
                    serverSettings = settingsService.setSettings(siteKey, settings1);
                } else {
                    serverSettings = null;
                }
            } else {
                serverSettings = settingsService.getSettings(siteKey);
            }

            // Create a JSON object to be returned as a response.
            final JSONObject resp = new JSONObject();
            if (serverSettings != null) {
                resp.put(SettingsConstants.ENABLED, serverSettings.getEnabled());
                resp.put(SettingsConstants.SETTING_1, serverSettings.getSetting1());
            }

            // If no configuration set, then set a variable noConf to true.  This value can be used to display a
            // message to the end user.
            resp.put("noConf", serverSettings == null);
            return new ActionResult(HttpServletResponse.SC_OK, null, resp);
        } catch (Exception e) {
            final JSONObject error = new JSONObject();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("error while saving settings", e);
            }
            // Set the error type and message as a response.
            error.put("error", e.getMessage());
            error.put("type", e.getClass().getSimpleName());
            return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null, error);
        }
    }

    /**
     * This method returns the value of the property if exists, otherwise return default value.
     *
     * @param settings
     * @param propertyName
     * @param defaultValue
     * @param <T>
     * @return
     * @throws JSONException
     */
    private <T> T getSettingOrDefault(final JSONObject settings,
                                      final String propertyName,
                                      final T defaultValue) throws JSONException {
        return settings.has(propertyName) ? (T) settings.get(propertyName) : defaultValue;
    }

    /**
     *
     * @param settingsService
     */
    public void setSettingsService(final SettingsService settingsService) {
        this.settingsService = settingsService;
    }
}
