/*
 * @(#) DefaultRequestProcessor.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.chriswareham.di.ComponentException;
import net.chriswareham.di.ComponentFactory;
import net.chriswareham.di.LifecycleComponent;

/**
 * This class implements methods that handle requests in the MVC framework.
 *
 * @author Chris Wareham
 */
public class DefaultRequestProcessor implements RequestProcessor, LifecycleComponent {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultRequestProcessor.class);
    /**
     * The pattern for splitting a path.
     */
    private static final Pattern PATH_PATTERN = Pattern.compile("/");

    /**
     * The component factory.
     */
    private ComponentFactory componentFactory;
    /**
     * The multi-part request handler.
     */
    private MultiPartRequestHandler multiPartRequestHandler;
    /**
     * The list of action interceptors.
     */
    private List<ActionInterceptor> actionInterceptors = new ArrayList<>();
    /**
     * The tree of action mappings.
     */
    private ActionMapping actions = new ActionMapping();
    /**
     * The map of view mappings.
     */
    private Map<String, View> views = new HashMap<>();

    /**
     * Set the component factory.
     *
     * @param cf the component factory
     */
    public void setComponentFactory(final ComponentFactory cf) {
        componentFactory = cf;
    }

    /**
     * Initialise the request processor.
     *
     * @throws ComponentException if an error occurs
     */
    @Override
    public void start() throws ComponentException {
        int actionInterceptorsCount = 0;
        int actionsCount = 0;
        int viewsCount = 0;

        for (String componentName : componentFactory.getComponentNames()) {

            // process an action interceptor component

            if (componentFactory.isComponent(componentName, ActionInterceptor.class)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("start(): processing action interceptor:[" + componentName + "]");
                }

                ActionInterceptor actionInterceptor = componentFactory.getComponent(componentName, ActionInterceptor.class);
                actionInterceptors.add(actionInterceptor);

                ++actionInterceptorsCount;
            }

            // process an action component

            if (componentFactory.isComponent(componentName, Action.class)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("start(): processing action:[" + componentName + "]");
                }

                Action action = componentFactory.getComponent(componentName, Action.class);

                ActionMapping mapping = actions;

                StringBuilder path = new StringBuilder();
                String[] pathComponents = PATH_PATTERN.split(action.getPath());
                for (int i = 1; i < pathComponents.length; ++i) {
                    path.append('/');
                    path.append(pathComponents[i]);

                    ActionMapping childMapping = mapping.getChild(pathComponents[i]);
                    if (childMapping == null) {
                        childMapping = new ActionMapping(pathComponents[i], path.toString());
                        mapping.addChild(childMapping);
                    }
                    mapping = childMapping;
                }

                mapping.setAction(action);

                ++actionsCount;
            }

            // process a view component

            if (componentFactory.isComponent(componentName, View.class)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("start(): processing view:[" + componentName + "]");
                }

                View view = componentFactory.getComponent(componentName, View.class);
                views.put(componentName, view);

                ++viewsCount;
            }

            // process a multi-part request handler

            if (componentFactory.isComponent(componentName, MultiPartRequestHandler.class)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("start(): processing multi-part request handler:[" + componentName + "]");
                }

                multiPartRequestHandler = componentFactory.getComponent(componentName, MultiPartRequestHandler.class);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("start(): action interceptors:[" + actionInterceptorsCount + "] actions:[" + actionsCount + "] views:[" + viewsCount + "]");
        }
    }

    /**
     * Shut down the request processor.
     */
    @Override
    public void stop() {
        multiPartRequestHandler = null;
        actionInterceptors.clear();
        actions.removeChildren();
        views.clear();
    }

    /**
     * Process a request.
     *
     * Examines the path to the web resource and determines whether a
     * corresponding class exists in the action mappings. If a mapping is found
     * then the {@link Action#action(ActionRequest) action} method is called to
     * process the request and return a response which includes an optional view
     * name and any data model. If a view name is specified, and a corresponding
     * entry exists in the view mappings, then the appropriate
     * {@link View#render(Map,HttpServletRequest,HttpServletResponse,ServletContext) render}
     * method is called. If a view name is specified, but a corresponding entry
     * does not exist in the view mappings, then a 404 "Not Found" error is
     * returned.
     *
     * If an action mapping is not found, but a corresponding entry exists in
     * the view mappings, then the appropriate
     * {@link View#render(Map,HttpServletRequest,HttpServletResponse,ServletContext) render}
     * method is called. This is useful for requests that do not require any
     * processing by an action class.
     *
     * If an action and view class are not found then a 404 "Not Found" error
     * is returned.
     *
     * @see ActionInterceptor
     * @see Action
     * @see View
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param context the servlet context
     * @throws IOException if an input or output error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void process(final HttpServletRequest request, final HttpServletResponse response, final ServletContext context) throws IOException, ServletException {
        String path = request.getServletPath();

        long timestamp = 0;

        if (LOGGER.isDebugEnabled()) {
            timestamp = System.currentTimeMillis();
            LOGGER.debug("process(): path:[" + path + "]");
        }

        ActionMapping mapping = locateMapping(path);

        if (mapping.isAction()) {
            try {
                String actionPath = mapping.getPath();

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("process(): action:[" + actionPath + "] found for path:[" + path + "]");
                }

                // The servlet API is not type-safe
                @SuppressWarnings("unchecked") Map<String, String[]> parameters = request.getParameterMap();
                Map<String, String[]> headers = getHeaderMap(request);

                MutableActionRequest actionRequest = new DefaultActionRequest();
                actionRequest.setRemoteAddr(request.getRemoteAddr());
                actionRequest.setLocale(request.getLocale());
                actionRequest.setPath(actionPath);
                actionRequest.setPathInfo(path.substring(actionPath.length()));
                actionRequest.addHeaders(headers);
                actionRequest.addParameters(parameters);
                actionRequest.setCookies(request.getCookies());
                if (multiPartRequestHandler != null && multiPartRequestHandler.isMultiPartRequest(request)) {
                    multiPartRequestHandler.parseRequest(request, actionRequest);
                }

                ActionInterceptorChain actionInterceptorchain = new DefaultActionInterceptorChain(actionInterceptors, mapping.getAction());
                ActionResponse actionResponse = actionInterceptorchain.intercept(actionRequest);

                for (Cookie cookie : actionResponse.getCookies().values()) {
                    response.addCookie(cookie);
                }

                String viewName = actionResponse.getViewName();

                if (views.containsKey(viewName)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("process(): view found for name:[" + viewName + "]");
                    }

                    View view = views.get(viewName);
                    view.render(actionResponse.getModels(), request, response, context);
                } else if (viewName != null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (RequestException exception) {
                response.sendError(exception.getError());
            } catch (RuntimeException | ActionException exception) {
                throw new ServletException("Unable to process request for path " + path, exception);
            }
        } else if (views.containsKey(path)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("process(): view found for path:[" + path + "]");
            }

            View view = views.get(path);
            view.render(Collections.<String, Object>emptyMap(), request, response, context);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("process(): no action or view found for path:[" + path + "]");
            }

            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("process(): path:[" + path + "] elapsed:[" + (System.currentTimeMillis() - timestamp) + "ms]");
        }
    }

    /**
     * Locate the most appropriate action mapping for a request path.
     *
     * @param path the request path
     * @return the most appropriate action mapping for the request path
     */
    private ActionMapping locateMapping(final String path) {
        ActionMapping mapping = actions.isAction() ? actions : null;
        ActionMapping parent = actions;

        String[] pathComponents = PATH_PATTERN.split(path);
        for (int i = 1; i < pathComponents.length; ++i) {
            if (!parent.isChild(pathComponents[i])) {
                break;
            }
            parent = parent.getChild(pathComponents[i]);
            mapping = parent.isAction() ? parent : mapping;
        }

        return mapping;
    }

    /**
     * Return a map of headers for a request.
     *
     * @param request the request
     * @return the map of headers
     */
    private static Map<String, String[]> getHeaderMap(final HttpServletRequest request) {
        Map<String, String[]> headers = new HashMap<>();

        // The servlet API is not type-safe
        @SuppressWarnings("unchecked") Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            headers.put(name, getHeaderValues(request, name));
        }

        return headers;
    }

    /**
     * Return an array of headers.
     *
     * @param request the request
     * @param name the header name
     * @return the array of headers
     */
    private static String[] getHeaderValues(final HttpServletRequest request, final String name) {
        List<String> list = new ArrayList<>();

        // The servlet API is not type-safe
        @SuppressWarnings("unchecked") Enumeration<String> values = request.getHeaders(name);
        while (values.hasMoreElements()) {
            list.add(values.nextElement());
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * This class stores a node in the actions mapping tree.
     */
    private static class ActionMapping {
        /**
         * The name of the node.
         */
        private String name;
        /**
         * The path of the node.
         */
        private String path;
        /**
         * The action of the node.
         */
        private Action action;
        /**
         * The children of the node.
         */
        private Map<String, ActionMapping> mappings;

        /**
         * Create a new root node.
         */
        ActionMapping() {
            this("/", "/");
        }

        /**
         * Create a new node.
         *
         * @param n the name of the node
         * @param p the path of the node
         */
        ActionMapping(final String n, final String p) {
            name = n;
            path = p;
        }

        /**
         * Get the path of the node.
         *
         * @return the path of the node
         */
        public String getPath() {
            return path;
        }

        /**
         * Return whether the node has an action.
         *
         * @return whether the node has an action
         */
        public boolean isAction() {
            return action != null;
        }

        /**
         * Get the action of the node.
         *
         * @return the action of the node
         */
        public Action getAction() {
            return action;
        }

        /**
         * Set the action of the node.
         *
         * @param a the action of the node
         */
        public void setAction(final Action a) {
            action = a;
        }

        /**
         * Return whether the node has any children.
         *
         * @return whether the node has any children
         */
        public boolean isParent() {
            return mappings != null && !mappings.isEmpty();
        }

        /**
         * Return whether the node has a child.
         *
         * @param n the name of the child node
         * @return whether the node has the child
         */
        public boolean isChild(final String n) {
            return mappings != null ? mappings.containsKey(n) : false;
        }

        /**
         * Get a child node.
         *
         * @param n the name of the child node
         * @return the child node, or null if it does not exist
         */
        public ActionMapping getChild(final String n) {
            return mappings != null ? mappings.get(n) : null;
        }

        /**
         * Add a child node.
         *
         * @param child the child node to add
         */
        public void addChild(final ActionMapping child) {
            if (mappings == null) {
                mappings = new HashMap<>();
            }
            mappings.put(child.name, child);
        }

        /**
         * Remove the child nodes.
         */
        public void removeChildren() {
            if (mappings != null) {
                mappings.clear();
            }
        }
    }
}
