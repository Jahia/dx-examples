package org.jahia.modules.newCustomWorkflows;

import java.util.List;

import org.jahia.services.workflow.jbpm.custom.AbstractWorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomWorkItemHandler extends AbstractWorkItemHandler implements WorkItemHandler {

	private transient static Logger logger = LoggerFactory.getLogger(CustomWorkItemHandler.class);
	
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager workItemManager) {
		logger.debug("CustomWorkItemHandler has been canceled");
		
		workItemManager.abortWorkItem(workItem.getId());
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager workItemManager) {
		logger.debug("CustomWorkItemHandler has been called");

		// Retrieving input data
		List<String> nodeIds = (List<String>) workItem.getParameter("nodeIds");
		String workspace = (String) workItem.getParameter("workspace");
		
		Boolean result = false;
		
		/*
		 * Write business code here and eventually modify the result
		 * (call to webservices, checking data trying to be published, ...)
		 */
		//result = true;

		// Setting output data
		workItem.getResults().put("valid", result);
		workItemManager.completeWorkItem(workItem.getId(), null);
	}

}
