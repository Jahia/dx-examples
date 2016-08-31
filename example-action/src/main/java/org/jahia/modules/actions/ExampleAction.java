package org.jahia.modules.actions;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by smomin on 8/31/16.
 */
public class ExampleAction extends Action {

    /**
     * This method handles all method requests and returns the appropriate Action Result.
     *
     * Redirect to the same page url with a 200 status code.  The path will not have an extention, the action executor will
     * handle adding the extension.
     * <pre>
     * {@code
     * return new ActionResult(200, renderContext.getMainResource().getNode().getPath());
     * }
     * </pre>
     *
     * If redirect requires a query param, use the response sendRedirect option and use the standard ActionResult.OK.
     * final String redirectUrl = resource.getNode().getPath() + ".html?key=value";
     * <pre>
     * {@code
     * renderContext.getResponse().sendRedirect(redirectUrl);
     * }
     * </pre>
     *
     * If JSON response is expected, use the below code to return a JSON in ActionResult.
     * <pre>
     * {@code
     * final Map<String, String> mapToReturn = new HashMap();
     * mapToReturn.put("key1", "value1");
     * mapToReturn.put("key2", "value2");
     * return new ActionResult(200, null, new JSONObject(mapToReturn));
     * }
     * </pre>
     * @param req request object
     * @param renderContext object hold information on the session, workspace, etc
     * @param resource object that is auto mapped based on the url used.
     * @param session oject to manage JCR data
     * @param parameters
     * @param urlResolver
     * @return
     * @throws Exception
     */
    @Override
    public ActionResult doExecute(final HttpServletRequest req,
                                  final RenderContext renderContext,
                                  final Resource resource,
                                  final JCRSessionWrapper session,
                                  final Map<String, List<String>> parameters,
                                  final URLResolver urlResolver)
            throws Exception {
        final HttpServletResponse response = renderContext.getResponse();

		// The below code is to demostrate a successful visiable response.
        response.getWriter().write("Hello Example Action!  The node path is " + resource.getNode().getPath());

        // No redirect, will only return a 200
        return ActionResult.OK;
    }
}
