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
 * Remove every node of a given nodetype
 */

final LoggerWrapper logger = log

final String nodeTypeName = "ins:myComponent"

JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, new JCRCallback() {
    @Override
    Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

        final String stmt = "SELECT * FROM [" + nodeTypeName + "] WHERE ISDESCENDANTNODE('/sites')"
        final NodeIterator iteratorSites = session.getWorkspace().getQueryManager().createQuery(stmt, Query
                .JCR_SQL2)
                .execute().getNodes()
        while (iteratorSites.hasNext()) {
            JCRNodeWrapper node = iteratorSites.nextNode() as JCRNodeWrapper

            node.remove()
        }

        session.save()

        return null
    }
})