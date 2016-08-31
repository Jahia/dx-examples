package org.jahia.modules.example.settings;

import org.jahia.services.content.JCRContentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * This Application Listener listens for any settings changes.
 */
public class SettingsChangedListener implements ApplicationListener<ApplicationEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsChangedListener.class);
    private SettingsService settingsService;

    /**
     * The method loops through the affected sites and loads the setting for each site.
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(final ApplicationEvent event) {
        if (event instanceof ExampleSettingsChangedEvent) {
            try {
                // Loop through the affected sites and load settings.
                for (final String siteKey : ((ExampleSettingsChangedEvent) event).getAffectedSites()) {
                    settingsService.loadSettings(siteKey);
                }
            } catch (RepositoryException e) {
                LOGGER.error("unable to load settings", e);
            }
        }
    }

    /**
     * @param settingsService
     */
    public void setSettingsService(final SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    /**
     * The event sets the list of affected sites to be later handle by the settings changed listener.
     */
    public static class ExampleSettingsChangedEvent extends ApplicationEvent {
        private static Logger LOGGER = LoggerFactory.getLogger(ExampleSettingsChangedEvent.class);
        private List<String> affectedSites;

        /**
         * This method loops through the events and add the affected sites.
         *
         * @param events
         */
        public ExampleSettingsChangedEvent(final EventIterator events) {
            super(events);
            affectedSites = new ArrayList<>();
            // Loop the events
            while (events.hasNext()) {
                final Event event = events.nextEvent();
                try {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("event type is {}", event.getType());
                        LOGGER.debug("event path is {}", event.getPath());
                        LOGGER.debug("event user id is {}", event.getUserID());
                    }
                    // Get the site key from the event path and add to the affected sites list.
                    affectedSites.add(JCRContentUtils.getSiteKey(event.getPath()));
                } catch (RepositoryException e) {
                    LOGGER.warn("error while getting event", e);
                }
            }
        }

        /**
         * @return
         */
        public List<String> getAffectedSites() {
            return affectedSites;
        }
    }
}
