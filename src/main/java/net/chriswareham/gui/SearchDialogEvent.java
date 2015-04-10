/*
 * @(#) SearchDialogEvent.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.gui;

import java.util.Collections;
import java.util.EventObject;
import java.util.List;

/**
 * This class provides an event that describes a table search.
 *
 * @author Chris Wareham
 */
public class SearchDialogEvent extends EventObject {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The search string.
     */
    private String searchString;
    /**
     * The table columns to search.
     */
    private List<Integer> searchColumns;
    /**
     * Whether to search for an exact match.
     */
    private boolean exactMatch;

    /**
     * Construct an instance of the event that describes a table search.
     *
     * @param source the object that generated this event
     * @param string the search string
     * @param columns the table columns to search
     * @param exact whether to search for an exact match
     */
    public SearchDialogEvent(final Object source, final String string, final List<Integer> columns, final boolean exact) {
        super(source);
        searchString = string;
        searchColumns = columns;
        exactMatch = exact;
    }

    /**
     * Get the search string.
     *
     * @return the search string
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * Get the table columns to search.
     *
     * @return the table columns to search
     */
    public List<Integer> getSearchColumns() {
        return Collections.unmodifiableList(searchColumns);
    }

    /**
     * Get whether to search for an exact match.
     *
     * @return whether to search for an exact match
     */
    public boolean isExactMatch() {
        return exactMatch;
    }
}
