/*
 * @(#) RandomComparator.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.util.Comparator;
import java.util.Random;

/**
 * This class provides a comparator for sorting objects in a random order.
 *
 * @author Chris Wareham
 */
public class RandomComparator implements Comparator<Object> {
    /**
     * The random number generator.
     */
    private final Random random = new Random();

    /**
     * Sort two objects randomly.
     *
     * @param o1 the first object to sort
     * @param o2 the second object to sort
     * @return a negative integer, zero, or a positive integer indicating
     * whether the first object sorts less than, equal to, or greater than
     * the second object
     */
    @Override
    public int compare(final Object o1, final Object o2) {
        int i1 = random.nextInt();
        int i2 = random.nextInt();
        return i1 < i2 ? -1 : i1 > i2 ? 1 : 0;
    }
}
