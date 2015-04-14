/*
 * @(#) ComponentParametersHandler.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import org.apache.log4j.Logger;

import net.chriswareham.util.Strings;

/**
 * This class provides a handler for parsing component parameters.
 *
 * @author Chris Wareham
 */
public class ComponentParametersHandler extends DefaultHandler {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ComponentParametersHandler.class);
    /**
     * The default instantiation.
     */
    private static final String DEFAULT_INSTANTIATION = "IMMEDIATE";
    /**
     * The default constructor value type.
     */
    private static final String DEFAULT_TYPE = "string";
    /**
     * The constructor value types.
     */
    private static final Map<String, Class<?>> TYPES;

    static {
        Map<String, Class<?>> types = new HashMap<>();
        types.put("string", String.class);
        types.put("boolean", boolean.class);
        types.put("int", int.class);
        types.put("long", long.class);
        types.put("float", float.class);
        types.put("double", double.class);
        TYPES = Collections.unmodifiableMap(types);
    }

    /**
     * The resource resolver.
     */
    private ResourceResolver resourceResolver;
    /**
     * The component resource name.
     */
    private String componentResource;
    /**
     * The component parameters.
     */
    private Map<String, ComponentParameters> componentParameters;
    /**
     * The component parameters currently being parsed.
     */
    private ComponentParameters parameters;
    /**
     * The list currently being parsed.
     */
    private String list;
    /**
     * The map currently being parsed.
     */
    private String map;
    /**
     * The document locator.
     */
    private Locator documentLocator;

    /**
     * Set the resource resolver.
     *
     * @param rr the resource resolver
     */
    public void setResourceResolver(final ResourceResolver rr) {
        resourceResolver = rr;
    }

    /**
     * Set the component resource name.
     *
     * @param cr the component resource name
     */
    public void setComponentResource(final String cr) {
        componentResource = cr;
    }

    /**
     * Set the component parameters.
     *
     * @param cp the component parameters
     */
    public void setComponentParameters(final Map<String, ComponentParameters> cp) {
        componentParameters = cp;
    }

    /**
     * Parse component parameters.
     *
     * @throws SAXException if an error occurs
     */
    public void parse() throws SAXException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("parse(): parsing component resource:[" + componentResource + "]");
        }

        try {
            InputSource source = new InputSource(resourceResolver.getResource(componentResource));
            source.setSystemId(resourceResolver.getResourcePath(componentResource));

            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setEntityResolver((final String publicId, final String systemId) -> new InputSource(new StringReader("")));
            reader.setContentHandler(this);
            reader.parse(source);
        } catch (SAXParseException exception) {
            throw new SAXException(resourceResolver.getResourcePath(componentResource) + ":" + exception.getLineNumber() + ": " + exception.getMessage());
        } catch (IOException exception) {
            throw new SAXException(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDocumentLocator(final Locator locator) {
        documentLocator = locator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        switch (localName) {
        case "components":
            break;
        case "include":
            handleInclude(attributes);
            break;
        case "component":
            handleComponent(attributes);
            break;
        case "constructor-ref":
            handleConstructorReference(attributes);
            break;
        case "constructor-value":
            handleConstructorValue(attributes);
            break;
        case "property":
            handleProperty(attributes);
            break;
        case "list":
            handleList(attributes);
            break;
        case "item":
            handleItem(attributes);
            break;
        case "map":
            handleMap(attributes);
            break;
        case "entry":
            handleEntry(attributes);
            break;
        default:
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + localName + "' element is unsupported");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        switch (localName) {
        case "component":
            parameters = null;
            break;
        case "list":
            list = null;
            break;
        case "map":
            map = null;
            break;
        }
    }

    /**
     * Handle an include element.
     *
     * @param attributes the element attributes
     * @throws SAXException if an error is found
     */
    private void handleInclude(final Attributes attributes) throws SAXException {
        String path = attributes.getValue("path");

        if (Strings.isNullOrEmpty(path)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the 'path' attribute must be specified for an include");
        }

        ComponentParametersHandler handler = new ComponentParametersHandler();
        handler.setResourceResolver(resourceResolver);
        handler.setComponentResource(path);
        handler.setComponentParameters(componentParameters);
        handler.parse();
    }

    /**
     * Handle a component element.
     *
     * @param attributes the element attributes
     * @throws SAXException if an error is found
     */
    private void handleComponent(final Attributes attributes) throws SAXException {
        String id = attributes.getValue("id");

        if (Strings.isNullOrEmpty(id)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the 'id' attribute must be specified for a component");
        }

        if (componentParameters.containsKey(id)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + id + "' component has already been defined");
        }

        String klass = attributes.getValue("class");

        if (Strings.isNullOrEmpty(klass)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the 'class' attribute must be specified for a component");
        }

        Class<?> type = null;

        try {
            type = Class.forName(klass);
        } catch (ClassNotFoundException exception) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + klass + "' class cannot be found");
        }

        String instantiation = attributes.getValue("instantiation");
        instantiation = instantiation != null ? instantiation.toUpperCase() : DEFAULT_INSTANTIATION;

        try {
            ComponentInstantiation.valueOf(instantiation);
        } catch (IllegalArgumentException exception) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + instantiation + "' instantiation is unsupported");
        }

        String startup = attributes.getValue("startup");
        String shutdown = attributes.getValue("shutdown");

        parameters = new ComponentParameters();
        parameters.setId(id);
        parameters.setType(type);
        parameters.setInstantiation(ComponentInstantiation.valueOf(instantiation));
        parameters.setStartup(startup);
        parameters.setShutdown(shutdown);

        componentParameters.put(id, parameters);
    }

    /**
     * Handle a constructor reference element.
     *
     * @param attributes the element attributes
     * @throws SAXException if an error is found
     */
    private void handleConstructorReference(final Attributes attributes) throws SAXException {
        String refid = attributes.getValue("refid");

        if (refid == null) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the constructor reference must specify a 'refid' attribute");
        }

        if (!componentParameters.containsKey(refid)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + refid + "' component has not been defined");
        }

        parameters.addConstructorArg(new ConstructorReference(componentParameters.get(refid).getType(), refid));
    }

    /**
     * Handle a constructor value element.
     *
     * @param attributes the element attributes
     * @throws SAXException if an error is found
     */
    private void handleConstructorValue(final Attributes attributes) throws SAXException {
        String type = attributes.getValue("type");
        String value = attributes.getValue("value");

        if (value == null) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the constructor value must specify a 'value' attribute");
        }

        if (type != null && !TYPES.containsKey(type)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the constructor value type '" + type + "' is unsupported");
        }

        Class<?> c = type != null ? TYPES.get(type) : TYPES.get(DEFAULT_TYPE);

        parameters.addConstructorArg(new ConstructorValue(c, ComponentUtils.valueOf(c, value)));
    }

    /**
     * Handle a property element.
     *
     * @param attributes the element attributes
     * @throws SAXException if an error is found
     */
    private void handleProperty(final Attributes attributes) throws SAXException {
        String name = attributes.getValue("name");

        if (Strings.isNullOrEmpty(name)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the 'name' attribute must be specified for a property");
        }

        if (parameters.containsParameter(name)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + name + "' parameter has already been defined");
        }

        String refid = attributes.getValue("refid");
        String value = attributes.getValue("value");

        if (refid == null && value == null) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + name + "' property must specify a 'refid' or 'value' attribute");
        }

        if (refid != null && value != null) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + name + "' property must not specify both 'refid' and 'value' attributes");
        }

        if (refid != null && !componentParameters.containsKey(refid)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + refid + "' component has not been defined");
        }

        if (refid != null) {
            parameters.addReference(name, refid);
        } else {
            parameters.addProperty(name, value);
        }
    }

    /**
     * Handle a list element.
     *
     * @param attributes the element attributes
     * @throws SAXException if an error is found
     */
    private void handleList(final Attributes attributes) throws SAXException {
        String name = attributes.getValue("name");

        if (Strings.isNullOrEmpty(name)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the 'name' attribute must be specified for a list");
        }

        if (parameters.containsParameter(name)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + name + "' parameter has already been defined");
        }

        list = name;
    }

    /**
     * Handle an item element.
     *
     * @param attributes the element attributes
     * @throws SAXException if an error is found
     */
    private void handleItem(final Attributes attributes) throws SAXException {
        if (list == null) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the 'item' element must only be specified within a list");
        }

        String refid = attributes.getValue("refid");
        String value = attributes.getValue("value");

        if (refid == null && value == null) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + list + "' item must specify a 'refid' or 'value' attribute");
        }

        if (refid != null && value != null) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + list + "' item must not specify both 'refid' and 'value' attributes");
        }

        if (refid != null && !componentParameters.containsKey(refid)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + refid + "' component has not been defined");
        }

        if (refid != null) {
            parameters.addListReference(list, refid);
        } else {
            parameters.addListProperty(list, value);
        }
    }

    /**
     * Handle a map element.
     *
     * @param attributes the element attributes
     * @throws SAXException if an error is found
     */
    private void handleMap(final Attributes attributes) throws SAXException {
        String name = attributes.getValue("name");

        if (Strings.isNullOrEmpty(name)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the 'name' attribute must be specified for a map");
        }

        if (parameters.containsParameter(name)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + name + "' parameter has already been defined");
        }

        map = name;
    }

    /**
     * Handle an entry element.
     *
     * @param attributes the element attributes
     * @throws SAXException if an error is found
     */
    private void handleEntry(final Attributes attributes) throws SAXException {
        if (map == null) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the 'entry' element must only be specified within a map");
        }

        String key = attributes.getValue("key");

        if (Strings.isNullOrEmpty(key)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + map + "' entry must specify a 'key' attribute");
        }

        String refid = attributes.getValue("refid");
        String value = attributes.getValue("value");

        if (refid == null && value == null) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + map + "' entry must specify a 'refid' or 'value' attribute");
        }

        if (refid != null && value != null) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + map + "' entry must not specify both 'refid' and 'value' attributes");
        }

        if (refid != null && !componentParameters.containsKey(refid)) {
            throw new SAXException(documentLocator.getSystemId() + ":" + documentLocator.getLineNumber() + ": the '" + refid + "' component has not been defined");
        }

        if (refid != null) {
            parameters.addMapReference(map, key, refid);
        } else {
            parameters.addMapProperty(map, key, value);
        }
    }
}
