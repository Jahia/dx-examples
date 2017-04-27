import org.jahia.api.Constants
import org.jahia.services.content.JCRPropertyWrapper
import org.jahia.tools.patches.LoggerWrapper
import org.jahia.services.content.JCRCallback
import org.jahia.services.content.JCRNodeWrapper
import org.jahia.services.content.JCRSessionWrapper
import org.jahia.services.content.JCRTemplate

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

/**
 * Script to use for removing a property on existing content, before removing the property in the definition
 */

final LoggerWrapper logger = log

final String nodeTypeName = "ins:myComponent"
final String propertyName = "propertyStringWithI18NToRemove"

JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, new JCRCallback() {
    @Override
    Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

        final String stmt = "SELECT * FROM [" + nodeTypeName + "] WHERE ISDESCENDANTNODE('/sites') AND [" +
                propertyName + "] IS NOT NULL"
        final NodeIterator iteratorSites = session.getWorkspace().getQueryManager().createQuery(stmt, Query
                .JCR_SQL2)
                .execute().getNodes()
        while (iteratorSites.hasNext()) {
            JCRNodeWrapper node = iteratorSites.nextNode() as JCRNodeWrapper

            JCRPropertyWrapper property = node.getProperty(propertyName)
            property.remove()
        }

        session.save()

        return null
    }
})