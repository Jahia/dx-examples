package org.jahia.modules.example.settings;

import org.apache.commons.lang.StringUtils;
import org.jahia.api.Constants;
import org.jahia.services.content.JCRCallback;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.JCRTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.io.IOException;

/**
 * This is an enhanced bean object with properties and methods to manage loading, storing, and removing the settings
 * from the JCR.
 * Created by smomin on 8/18/16.
 */
public class Settings {
    private static final Logger LOGGER = LoggerFactory.getLogger(Settings.class);

    private String siteKey;
    private boolean enabled;
    private String setting1;
    /**
     * @param siteKey
     * @throws IOException
     */
    public Settings(final String siteKey) throws IOException {
        this.siteKey = siteKey;
        this.enabled = true;
    }

    /**
     * This method removes the settings node for the site.
     */
    public void remove() {
        try {
            // store settings
            JCRTemplate.getInstance().doExecuteWithSystemSession(new JCRCallback<Boolean>() {
                public Boolean doInJCR(final JCRSessionWrapper session) throws RepositoryException {
                    // Retrieve settings node from the JCR.
                    final JCRNodeWrapper settingsNode = session
                            .getNode("/sites/" + siteKey + "/" + SettingsConstants.SETTINGS_NODE_NAME);

                    // Remove the settings node and save.
                    settingsNode.remove();
                    session.save();
                    return Boolean.TRUE;
                }
            });
        } catch (RepositoryException e) {
            LOGGER.error("Error deleting context server settings into the repository.", e);
        }
    }

    /**
     * This method retrieves the setting node for the site and sets the properties on this bean object.
     *
     * @return
     */
    public boolean load() {
        try {
            // read default settings
            return JCRTemplate.getInstance()
                    .doExecuteWithSystemSessionAsUser(null, Constants.EDIT_WORKSPACE, null, new JCRCallback<Boolean>() {
                public Boolean doInJCR(final JCRSessionWrapper session) throws RepositoryException {
                    // Check if site node exists, then check if the settings node exists on site.
                    if (session.nodeExists("/sites/" + siteKey)
                            && session.nodeExists("/sites/" + siteKey + "/" + SettingsConstants.SETTINGS_NODE_NAME)) {
                        // Retrieve settings node from the JCR.
                        final JCRNodeWrapper settingsNode = session
                                .getNode("/sites/" + siteKey + "/" + SettingsConstants.SETTINGS_NODE_NAME);

                        if (settingsNode.hasProperty(SettingsConstants.SETTINGS_EX_SETTING_1)) {
                            setting1 = settingsNode
                                    .getProperty(SettingsConstants.SETTINGS_EX_SETTING_1).getString();
                        }
                        return Boolean.TRUE;
                    }
                    return Boolean.FALSE;
                }
            });
        } catch (RepositoryException e) {
            LOGGER.error("Error reading settings from the repository.", e);
        }
        return false;
    }

    /**
     * This method creates a setting node or retrieves the setting node if exists for this site.
     * The properties are persisted in the JCR.
     */
    public void store() {
        try {
            // store default props
            JCRTemplate.getInstance()
                    .doExecuteWithSystemSessionAsUser(null, Constants.EDIT_WORKSPACE, null, new JCRCallback<Boolean>() {
                public Boolean doInJCR(final JCRSessionWrapper session) throws RepositoryException {
                    final JCRNodeWrapper settingsNode;

                    // Check if the settings node exists as a child of the site node.  If exists, retrieve the node.
                    if (session.nodeExists("/sites/" + siteKey + "/" + SettingsConstants.SETTINGS_NODE_NAME)) {
                        settingsNode = session.getNode("/sites/" + siteKey + "/" + SettingsConstants.SETTINGS_NODE_NAME);
                    }
                    // Otherwise, add the node as a child to the site node.
                    else {
                        settingsNode = session.getNode("/sites/" + siteKey).addNode(SettingsConstants.SETTINGS_NODE_NAME,
                                SettingsConstants.SETTINGS_NODE_TYPE);
                    }

                    // Set the property and return boolean if the node needs to be updated.
                    boolean doSave = setProperty(settingsNode, SettingsConstants.SETTINGS_EX_SETTING_1,
                            getSetting1());

                    if (doSave) {
                        session.save();
                    }
                    return Boolean.TRUE;
                }
            });
        } catch (RepositoryException e) {
            LOGGER.error("Error storing settings into the repository.", e);
        }
    }

    /**
     * This method sets or removes the property value.
     *
     * @param node
     * @param propertyName
     * @param value
     * @return
     * @throws RepositoryException
     */
    private boolean setProperty(final JCRNodeWrapper node,
                                final String propertyName,
                                final String value) throws RepositoryException {
        // Check if property does not exists and value is not empty.
        // or check if node has the property and the property value is not the same as the value paramater
        if ((!node.hasProperty(propertyName) && StringUtils.isNotEmpty(value))
                || (node.hasProperty(propertyName)
                && !StringUtils.equals(node.getProperty(propertyName).getString(), value))) {
            // If value is empty, remove the property from the JCR node.
            if (StringUtils.isEmpty(value)) {
                node.getProperty(propertyName).remove();
            }
            // Set the node property and value.
            else {
                node.setProperty(propertyName, value);
            }
            return true;
        }
        return false;
    }

    public String getSetting1() {
        return setting1;
    }

    public void setSetting1(final String setting1) {
        this.setting1 = setting1;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
