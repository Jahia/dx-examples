package org.jahia.modules.example.log;


import org.apache.log4j.xml.DOMConfigurator;
import org.jahia.settings.SettingsBean;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.FactoryConfigurationError;
import java.net.URL;

public class LogAdditionalConfigurationLoaderActivator implements BundleActivator {
	/**
	 * The key to find in the jahia.*.properties files
	 */
	private static final String CONFIG_LOG_FOLDER_URL_KEY = "logging.altLogFile.url";

	private static final Logger logger = LoggerFactory.getLogger(LogAdditionalConfigurationLoaderActivator.class);

	private BundleContext context;

	public void loadAdditionalConfiguration() {
		// Retrieve the path where to look for the additional log4j XML configuration file
		String configLogFolderUrl = SettingsBean.getInstance().getPropertiesFile().getProperty(CONFIG_LOG_FOLDER_URL_KEY);

		if (configLogFolderUrl == null || "".equals(configLogFolderUrl)) {
			logger.warn("Could not load the additional Log4j configuration, the key 'logging.altLogFile.url' is not " +
					"specified within configurations file");
			return;
		}

		final URL confsURL = this.getClass().getResource(SettingsBean.getInstance().getPropertiesFile().getProperty(CONFIG_LOG_FOLDER_URL_KEY));

		if (confsURL == null) {
			logger.warn("Could not load the additional Log4j configuration, configLogFolder URL not specified");
			return;
		}

		try {
			DOMConfigurator.configure(confsURL);
		} catch (FactoryConfigurationError fce) {
			logger.error("Could not load the additional Log4j configuration", fce);
			return;
		}
		logger.info("Sucessfully load additional Log4j configuration");
	}


	@Override
	public void start(BundleContext bundleContext) throws Exception {
		if (this.context == null) {
			this.context = bundleContext;
		}
		this.loadAdditionalConfiguration();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}
