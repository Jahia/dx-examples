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
 * Used to delete a given area (jnt:contentList) on every pages of a given site, only in default workspace
 */

final LoggerWrapper logger = log

final String nodeTypeName = "jnt:contentList"

// Site key
final String siteKey = "mySiteKey"
// Name of the area to delete
final String nameOfTheArea = "areatodelete"

JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, new JCRCallback() {
    @Override
    Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

        final String stmt = "SELECT * FROM [" + nodeTypeName + "] WHERE ISDESCENDANTNODE('/sites/" + siteKey + "') " +
                "AND" +
                " NAME(['"+ nodeTypeName + "'])='"+ nameOfTheArea +"'"
        final NodeIterator iteratorSites = session.getWorkspace().getQueryManager().createQuery(stmt, Query
                .JCR_SQL2)
                .execute().getNodes()
        while (iteratorSites.hasNext()) {
            JCRNodeWrapper node = iteratorSites.nextNode() as JCRNodeWrapper
            node.remove();
        }

        session.save()

        return null
    }
})