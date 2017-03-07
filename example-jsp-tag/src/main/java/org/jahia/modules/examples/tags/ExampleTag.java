package org.jahia.modules.examples.tags;

import org.jahia.taglibs.AbstractJahiaTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import java.io.IOException;

public class ExampleTag extends AbstractJahiaTag {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleTag.class);
    private String name;

    /**
     * This simple tag says hello to a person.
     * @return
     * @throws JspException
     */
    public int doEndTag() throws JspException {
        try {
            pageContext.getResponse().getWriter().append("Hello " + name);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return super.doEndTag();
    }

    /**
     * This will set the name passed from the tag.
     *
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }
}
