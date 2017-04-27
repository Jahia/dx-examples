import org.jahia.api.Constants
import org.jahia.services.content.JCRPropertyWrapper
import org.jahia.services.content.JCRCallback
import org.jahia.services.content.JCRNodeWrapper
import org.jahia.services.content.JCRSessionWrapper
import org.jahia.services.content.JCRTemplate

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

/**
 * The following groovy will query content where Editors/Contributors have override the cache expiration and will
 * then remove this override
 */

JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, new JCRCallback() {
    @Override
    Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

        final String stmt = "SELECT * FROM [jmix:cache] WHERE ISDESCENDANTNODE('/sites') AND [j:expiration] IS NOT NULL"
        final NodeIterator nodeIterator = session.getWorkspace().getQueryManager().createQuery(stmt, Query
                .JCR_SQL2)
                .execute().getNodes()
        while (nodeIterator.hasNext()) {
            JCRNodeWrapper node = nodeIterator.nextNode() as JCRNodeWrapper
            log.info("Remove property 'j:expiration' on node : " + node.getPath())

            // Remove the property j:expiration
            JCRPropertyWrapper expirationProperty = node.getProperty("j:expiration")
            expirationProperty.remove()


            // If the mixin was only here for the property j:expiration, as we removed the property, we also
            // remove the no more used mixin
            JCRPropertyWrapper userProperty = node.getProperty("j:perUser")
            if (!userProperty.getBoolean()) {
                log.info("Also removing mixin 'jmix:cache'")
                node.removeMixin("jmix:cache");
            }
        }

        session.save()

        return null
    }
})