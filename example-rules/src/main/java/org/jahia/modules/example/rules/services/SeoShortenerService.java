package org.jahia.modules.example.rules.services;

import org.jahia.services.content.JCRContentUtils;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.decorator.JCRSiteNode;
import org.jahia.services.content.rules.AddedNodeFact;
import org.jahia.services.seo.VanityUrl;
import org.jahia.services.seo.jcr.NonUniqueUrlMappingException;
import org.jahia.services.seo.jcr.VanityUrlService;
import org.slf4j.Logger;

import javax.jcr.ItemNotFoundException;
import javax.jcr.RepositoryException;
import java.util.Locale;

public class SeoShortenerService {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(SeoShortenerService.class);

	private VanityUrlService vanityUrlService;

	public void setVanityUrlService(VanityUrlService vanityUrlService) {
		this.vanityUrlService = vanityUrlService;
	}

	/**
	 * Add vanity URL for the specified node on the given locale
	 *
	 * @param node the node for which we need to add the vanity URL
	 * @param siteKey siteKey on which the node is
	 * @param urlToTry the VanityURL to try to create. If such a VanityURL is already existing, we increment an index
	 *                    on it (e.g. "url-1", "url-2", etc)
	 * @param locale the locale for which we need to create the vanity URL
	 * @throws RepositoryException
	 */
	private void addMapping(JCRNodeWrapper node, String siteKey, String urlToTry, String locale) throws
			RepositoryException {
		logger.debug("Adding mapping for node: " + node.getPath() + " | For locale: " + locale);
		String newUrl = urlToTry;
		int index = 0;
		while (true) {
			try {
				vanityUrlService.saveVanityUrlMapping(node, new VanityUrl(newUrl, siteKey, locale, true,
						true));
				break;
			} catch (NonUniqueUrlMappingException ex) {
				newUrl = urlToTry + "-" + (++index);
			}
		}
	}

	/**
	 * Add a vanity URL for the specified node by removing the whole path and replacing it with "/news/nodename"
	 *
	 * @param node the node which has been created
	 * @throws RepositoryException
	 */
	public void addMappingForAllSiteLocales(AddedNodeFact node) throws RepositoryException {
		logger.debug("Adding mapping for node: " + node.getNode().getPath() + " | For all locales of the site");
		JCRNodeWrapper newNode = node.getNode();
		String urlToTry;

		JCRSiteNode siteNode = newNode.getResolveSite();
		String siteKey = siteNode.getSiteKey();
		for (Locale locale : siteNode.getLanguagesAsLocales()) {
			try {
				urlToTry = "/news/" + JCRContentUtils.generateNodeName(newNode.getI18N(locale).getProperty("jcr:title")
						.getString());
				addMapping(newNode, siteKey, urlToTry, locale.toString());
			}
			catch (ItemNotFoundException ex) {
				logger.debug("The property name has not been translated in the following locale: " + locale
						.getDisplayLanguage());
			}
		}
	}
}
