/*
 * @(#) CsvView.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.chriswareham.util.CsvWriter;

/**
 * This class provides a view that renders models as comma separated values.
 *
 * @param <T> the type of objects to serialise
 * @author Chris Wareham
 */
public class CsvView<T> implements View {
    /**
     * The content type.
     */
    private static final String CONTENT_TYPE = "text/csv";
    /**
     * The character encoding.
     */
    private static final String CHARACTER_ENCODING = "UTF-8";
    /**
     * The content disposition header.
     */
    private static final String CONTENT_DISPOSITION_HEADER = "Content-disposition";
    /**
     * The content disposition prefix.
     */
    private static final String CONTENT_DISPOSITION_PREFIX = "attachment; filename=";
    /**
     * The date format.
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * The CSV serialiser.
     */
    private CsvSerialiser<T> serialiser;
    /**
     * Whether to print a date header.
     */
    private boolean dateHeader;
    /**
     * The name of the model to serialise data for.
     */
    private String dataModel;
    /**
     * The name of the model with the filename of the serialised data.
     */
    private String filenameModel;

    /**
     * Set the CSV serialiser.
     *
     * @param s the CSV serialiser
     */
    public void setSerialiser(final CsvSerialiser<T> s) {
        serialiser = s;
    }

    /**
     * Set whether to print a date header.
     *
     * @param dh whether to print a date header
     */
    public void setDateHeader(final boolean dh) {
        dateHeader = dh;
    }

    /**
     * Set the name of the model to serialise data for.
     *
     * @param dm the name of the model to serialise data fore
     */
    public void setDataModel(final String dm) {
        dataModel = dm;
    }

    /**
     * Set the name of the model with the filename of the serialised data.
     *
     * @param fm the name of the model with the filename of the serialised data
     */
    public void setFilenameModel(final String fm) {
        filenameModel = fm;
    }

    /**
     * Render models as comma separated values.
     *
     * @param models the models
     * @param request the client request
     * @param response the servlet response
     * @param context the servlet context
     * @throws IOException if an input or output error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void render(final Map<String, Object> models, final HttpServletRequest request, final HttpServletResponse response, final ServletContext context) throws IOException, ServletException {
        try {
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(CHARACTER_ENCODING);
            response.setHeader(CONTENT_DISPOSITION_HEADER, CONTENT_DISPOSITION_PREFIX + models.get(filenameModel));


            CsvWriter writer = new CsvWriter(response.getWriter());

            if (dateHeader) {
                DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                writer.writeLine(Collections.singletonList(dateFormat.format(new Date())));
                writer.writeLine(Collections.<String>emptyList());
            }

            writer.writeLine(serialiser.getHeaders());

            Object model = models.get(dataModel);
            if (model instanceof Collection<?>) {
                @SuppressWarnings("unchecked") Collection<T> list = (Collection<T>) model;
                for (T item : list) {
                    writer.writeLine(serialiser.serialise(item));
                }
            } else {
                @SuppressWarnings("unchecked") T item = (T) model;
                writer.writeLine(serialiser.serialise(item));
            }

            writer.flush();
        } catch (IOException exception) {
            // assume the client aborted the request (the following line just keeps Checkstyle happy)
            return;
        }
    }
}
