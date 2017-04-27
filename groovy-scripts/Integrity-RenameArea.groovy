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
 * Use this script when you rename an area withing a page/content template, and want this area to be renamed
 * as well in already existing content
 */

final LoggerWrapper logger = log

final String nodeTypeName = "jnt:contentList"
final String siteKey = "mySiteKey"
final String oldName = "areawhichwillchangename"
final String newName = "arearenamed"

JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, new JCRCallback() {
    @Override
    Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

        final String stmt = "SELECT * FROM [" + nodeTypeName + "] WHERE ISDESCENDANTNODE('/sites/" + siteKey + "') " +
                "AND" +
                " NAME(['"+ nodeTypeName + "'])='"+ oldName +"'"
        final NodeIterator iteratorSites = session.getWorkspace().getQueryManager().createQuery(stmt, Query
                .JCR_SQL2)
                .execute().getNodes()
        while (iteratorSites.hasNext()) {
            JCRNodeWrapper node = iteratorSites.nextNode() as JCRNodeWrapper
            node.rename(newName);
        }

        session.save()

        return null
    }
})