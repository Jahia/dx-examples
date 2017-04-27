import org.jahia.api.Constants
import org.jahia.tools.patches.LoggerWrapper
import org.jahia.services.content.JCRCallback
import org.jahia.services.content.JCRNodeWrapper
import org.jahia.services.content.JCRSessionWrapper
import org.jahia.services.content.JCRTemplate

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

/**
 * Retrieve every not filled property of a given nodetype, and set this property with a default value
 * Script to use before setting a mandatory constraint on an already existing property
 */

final LoggerWrapper logger = log

final String nodeTypeName = "ins:myComponent"
final String propertyName = "propertyWhichWillBecomeMandatory"
final String defaultValue = "Default value : please replace me"

JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, new JCRCallback() {
    @Override
    Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

        final String stmt = "SELECT * FROM [" + nodeTypeName + "] WHERE ISDESCENDANTNODE('/sites') AND [" +
                propertyName + "] IS NULL"
        final NodeIterator iteratorSites = session.getWorkspace().getQueryManager().createQuery(stmt, Query
                .JCR_SQL2)
                .execute().getNodes()
        while (iteratorSites.hasNext()) {
            JCRNodeWrapper node = iteratorSites.nextNode() as JCRNodeWrapper
            logger.info("Node missing mandatory '" + propertyName +"' property : " + node.getPath())
            node.setProperty(propertyName, defaultValue)
        }

        session.save()

        return null
    }
})