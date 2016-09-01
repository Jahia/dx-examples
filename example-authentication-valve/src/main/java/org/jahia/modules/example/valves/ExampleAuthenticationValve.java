package org.jahia.modules.example.valves;

import org.apache.commons.lang.StringUtils;
import org.jahia.bin.Login;
import org.jahia.params.valves.AuthValveContext;
import org.jahia.params.valves.AutoRegisteredBaseAuthValve;
import org.jahia.params.valves.LoginEngineAuthValveImpl;
import org.jahia.pipelines.PipelineException;
import org.jahia.pipelines.valves.ValveContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by smomin on 9/1/16.
 */
public class ExampleAuthenticationValve extends AutoRegisteredBaseAuthValve {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleAuthenticationValve.class);
    private static final String CMS_PREFIX = "/cms";
    private static final String REDIRECT = "redirect";
    private static final String SITE = "site";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    /**
     * This method handles the login process.  If sucessful, the user is placed in session.
     * @param context
     * @param valveContext
     * @throws PipelineException
     */
    @Override
    public void invoke(final Object context,
                       final ValveContext valveContext) throws PipelineException {
        final AuthValveContext authContext = (AuthValveContext) context;
        final HttpServletRequest request = authContext.getRequest();
        final boolean isLoginProcess = CMS_PREFIX.equals(request.getServletPath())
                && (Login.getMapping()).equals(request.getPathInfo());

        // Since Authentication Valve is executed on all requests.  We only want to execute the authenticaation
        // if its a login request.
        if (isLoginProcess) {
            final HttpServletResponse response = authContext.getResponse();
            final String site = request.getParameter(SITE);
            final String redirect = request.getParameter(REDIRECT);
            final String username = request.getParameter(USERNAME);
            final String password = request.getParameter(PASSWORD);

            LOGGER.debug("uid " + username);
            // If username is not empty, continue with the authenitcation
            if (StringUtils.isNotEmpty(username)) {
                // Get JCR user node using username and site
//                final JCRUserNode jahiaUserNode = ServicesRegistry.getInstance().getJahiaUserManagerService()
//                        .lookupUser(username, site);
//                if (jahiaUserNode != null) {
//                    // Verify is the password with the user's account.
//                    if (jahiaUserNode.verifyPassword(password)) {
//                        // If the acount is not locked, place user in the session.
//                        if (!jahiaUserNode.isAccountLocked()) {
//                            final JahiaUser jahiaUser = jahiaUserNode.getJahiaUser();
//                            if (jahiaUser.isAccountLocked()) {
//                                LOGGER.info("Login failed. Account is locked for user " + username);
//                                return;
//                            }
//                            authContext.getSessionFactory().setCurrentUser(jahiaUser);
//                        } else {
//                            // Failure message placed in Request object for later processing.
//                            LOGGER.warn("Login failed: account for user " + jahiaUserNode.getName() + " is locked.");
//                            request.setAttribute(LoginEngineAuthValveImpl.VALVE_RESULT,
//                                    LoginEngineAuthValveImpl.ACCOUNT_LOCKED);
//                        }
//                    } else {
//                        // Failure message placed in Request object for later processing.
//                        LOGGER.warn("Login failed: user " + jahiaUserNode.getName() + " provided bad password.");
//                        request.setAttribute(LoginEngineAuthValveImpl.VALVE_RESULT,
//                                LoginEngineAuthValveImpl.BAD_PASSWORD);
//                    }
//                } else {
//                    // Failure message placed in Request object for later processing.
//                    if (LOGGER.isDebugEnabled()) {
//                        LOGGER.debug("Login failed. Unknown username " + username + ".");
//                    }
//                    request.setAttribute(LoginEngineAuthValveImpl.VALVE_RESULT,
//                            LoginEngineAuthValveImpl.UNKNOWN_USER);
//                }
            }
            request.setAttribute(LoginEngineAuthValveImpl.VALVE_RESULT,
                    LoginEngineAuthValveImpl.OK);
        }
        valveContext.invokeNext(context);
    }
}
