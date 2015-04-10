/*
 * @(#) MonthsTag.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc.tags;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.LoopTagSupport;

/**
 * This class provides a tag for iterating over months.
 *
 * @author Chris Wareham
 */
public class MonthsTag extends LoopTagSupport {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The months iterator.
     */
    private Iterator<Date> iterator;
    /**
     * The starting date to iterate the months from.
     */
    private Date start;
    /**
     * The number of months to iterate over.
     */
    private int count = 12;

    /**
     * Set the starting date to iterate the months from.
     *
     * @param d the starting date to iterate the months from
     */
    public void setStart(final Date d) {
        start = d;
    }

    /**
     * Set the number of months to iterate over.
     *
     * @param c the number of months to iterate over
     */
    public void setCount(final int c) {
        count = c;
    }

    /**
     * Get whether there are more months to iterate over.
     *
     * @return whether there are more months to iterate over
     * @throws JspTagException if an error occurs
     */
    @Override
    protected boolean hasNext() throws JspTagException {
        return iterator.hasNext();
    }

    /**
     * Get the next month in the iteration.
     *
     * @return the next month in the iteration
     * @throws JspTagException if an error occurs
     */
    @Override
    protected Object next() throws JspTagException {
        return iterator.next();
    }

    /**
     * Prepare tag invocation.
     *
     * @throws JspTagException if an error occurs
     */
    @Override
    protected void prepare() throws JspTagException {
        if (count > 0) {
            List<Date> months = new ArrayList<>(count);

            Calendar calendar = Calendar.getInstance();

            if (start != null) {
                calendar.setTime(start);
            }

            for (int i = 0; i < count; ++i) {
                months.add(calendar.getTime());
                calendar.add(Calendar.MONTH, 1);
            }

            iterator = months.iterator();
        }
    }
}
