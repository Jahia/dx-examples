import org.apache.jackrabbit.core.value.InternalValue
import org.apache.jackrabbit.spi.commons.nodetype.constraint.ValueConstraint
import org.apache.jackrabbit.spi.commons.value.QValueValue
import org.jahia.api.Constants
import org.jahia.services.content.nodetypes.ExtendedPropertyDefinition
import org.jahia.tools.patches.LoggerWrapper
import org.jahia.services.content.JCRCallback
import org.jahia.services.content.JCRNodeWrapper
import org.jahia.services.content.JCRSessionWrapper
import org.jahia.services.content.JCRTemplate

import javax.jcr.NodeIterator
import javax.jcr.PropertyType
import javax.jcr.RepositoryException
import javax.jcr.Value
import javax.jcr.nodetype.ConstraintViolationException
import javax.jcr.query.Query

/**
 * Use this script after adding a new property constraint (regex, range) on a property, in order to detect and log
 * properties which are not compliant with this new constraint and need to be modified by editors
 */

final LoggerWrapper logger = log

final String nodeTypeName = "ins:myComponent"
final String propertyName = "propertyWhichWillHaveARegexConstraint"

JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, new JCRCallback() {
    @Override
    Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

        final String stmt = "SELECT * FROM [" + nodeTypeName + "] WHERE ISDESCENDANTNODE('/sites')"
        final NodeIterator iteratorSites = session.getWorkspace().getQueryManager().createQuery(stmt, Query
                .JCR_SQL2)
                .execute().getNodes()
        ExtendedPropertyDefinition propertyDefinition = null
        ValueConstraint[] constraints = null

        while (iteratorSites.hasNext()) {
            JCRNodeWrapper node = iteratorSites.nextNode() as JCRNodeWrapper

            if (propertyDefinition == null) {
                propertyDefinition = node.getApplicablePropertyDefinition(propertyName)
                constraints = propertyDefinition.getValueConstraintObjects()
            }

            InternalValue[] internalValues = null

            // Retrieve value or values
            if (!propertyDefinition.isMultiple()) {
                Value value = node.getProperty(propertyName).getValue()
                InternalValue internalValue = null
                if (value.getType() != PropertyType.BINARY && !((value.getType() == PropertyType.PATH || value.getType() == PropertyType.NAME) && !(value instanceof QValueValue))) {
                    internalValue = InternalValue.create(value, null, null)
                }
                if (internalValue != null) {
                    internalValues = new InternalValue[1]
                    internalValues[0] = internalValue
                }
            } else {
                Value[] values = node.getProperty(propertyName).getValues()
                List<InternalValue> list = new ArrayList<InternalValue>()
                for (Value value : values) {
                    if (value != null) {
                        // perform type conversion as necessary and create InternalValue
                        // from (converted) Value
                        InternalValue internalValue = null
                        if (value.getType() != PropertyType.BINARY
                                && !((value.getType() == PropertyType.PATH || value.getType() == PropertyType.NAME) && !(value instanceof QValueValue))) {
                            internalValue = InternalValue.create(value, null, null)
                        }
                        list.add(internalValue)
                    }
                }
                if (!list.isEmpty()) {
                    internalValues = list.toArray(new InternalValue[list.size()])
                }
            }


            // Check constraints
            if (internalValues != null && internalValues.length > 0) {
                for (InternalValue iValue : internalValues) {
                    // constraints are OR-ed together
                    boolean satisfied = false;
                    for (ValueConstraint constraint : constraints) {
                        try {
                            constraint.check(iValue)
                            satisfied = true
                            break
                        } catch (ConstraintViolationException e) {
                            logger.info("ConstraintViolation on node : " + node.getPath() + " | " + e.message)
                            break
                        }
                    }
                    if (!satisfied) {
                        break
                    }
                }
            }
        }

        return null;
    }
})