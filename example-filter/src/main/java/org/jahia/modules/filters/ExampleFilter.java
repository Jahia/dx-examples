package org.jahia.modules.filters;

import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;

/**
 * Created by smomin on 8/31/16.
 */
public class ExampleFilter extends AbstractFilter {

    /**
     * This method is best used to inject additional content before the html generation is executed.  The content
     * is placed in the request object that can be accessed on lower filters or the view.
     *
     * @param renderContext
     * @param resource
     * @param chain
     * @return
     * @throws Exception
     */
    @Override
    public String prepare(final RenderContext renderContext,
                          final Resource resource,
                          final RenderChain chain) throws Exception {

        // Return a non null value will stop the filter chain and move on to the execution process of the filters.
        return super.prepare(renderContext, resource, chain);
    }

    /**
     * This method is the post manipulation of the processed HTML.
     *
     * @param previousOut
     * @param renderContext
     * @param resource
     * @param chain
     * @return
     * @throws Exception
     */
    @Override
    public String execute(final String previousOut,
                          final RenderContext renderContext,
                          final Resource resource,
                          final RenderChain chain) throws Exception {
        final StringBuilder builder = new StringBuilder();
        builder.append(previousOut);
        builder.append("Example Filter is executed");

        return super.execute(builder.toString(), renderContext, resource, chain);
    }
}
