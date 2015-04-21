/*
 * @(#) Geocode.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.OSRef;

/**
 * This class provides an immutable geocode that represents coordinates.
 *
 * @author Chris Wareham
 */
public final class Geocode implements Externalizable {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The WGS84 latitude of the geocode.
     */
    private double latitude;
    /**
     * The WGS84 longitude of the geocode.
     */
    private double longitude;
    /**
     * The Ordnance Survey easting of the geocode.
     */
    private double x;
    /**
     * The Ordnance Survey northing of the geocode.
     */
    private double y;

    /**
     * Constructs a new instance of the Geocode class.
     *
     * @param latLng the WGS84 latitude and longitude
     * @param osRef the x and y Ordnance Survey easting and northing
     */
    private Geocode(final LatLng latLng, final OSRef osRef) {
        latitude = latLng.getLat();
        longitude = latLng.getLng();
        x = osRef.getEasting();
        y = osRef.getNorthing();
    }

    /**
     * Calculate a geocode from WGS84 latitude and longitude.
     *
     * @param latitude the WGS84 latitude
     * @param longitude the WGS84 longitude
     * @return the geocode
     */
    public static Geocode fromLatLng(final double latitude, final double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        OSRef osRef = latLng.toOSRef();
        return new Geocode(latLng, osRef);
    }

    /**
     * Calculate a geocode from Ordnance Survey easting and northing.
     *
     * @param x the Ordnance Survey easting
     * @param y the Ordnance Survey northing
     * @return the geocode
     */
    public static Geocode fromOsRef(final double x, final double y) {
        OSRef osRef = new OSRef(x, y);
        LatLng latLng = osRef.toLatLng();
        latLng.toWGS84();
        return new Geocode(latLng, osRef);
    }

    /**
     * Get the WGS84 latitude of the geocode.
     *
     * @return the WGS84 latitude of the geocode
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Get the WGS84 longitude of the geocode.
     *
     * @return the WGS84 longitude of the geocode
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Get the Ordnance Survey easting of the geocode.
     *
     * @return the Ordnance Survey easting of the geocode
     */
    public double getX() {
        return x;
    }

    /**
     * Get the Ordnance Survey northing of the geocode.
     *
     * @return the Ordnance Survey northing of the geocode
     */
    public double getY() {
        return y;
    }

    /**
     * Compares the specified object with this geocode.
     *
     * @param o the object to compare
     * @return true if the object is equal to this geocode
     */
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Geocode)) {
            return false;
        }
        Geocode g = (Geocode) o;
        return x == g.x && y == g.y;
    }

    /**
     * Returns the hash code of this geocode.
     *
     * @return the hash code of this geocode
     */
    @Override
    public int hashCode() {
        return ((int) x) ^ ((int) y);
    }

    /**
     * Returns the string representation of this geocode.
     *
     * @return the string representation of this geocode
     */
    @Override
    public String toString() {
        return x + "x" + y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readExternal(final ObjectInput in) throws IOException {
        latitude = in.readDouble();
        longitude = in.readDouble();
        x = in.readDouble();
        y = in.readDouble();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeDouble(x);
        out.writeDouble(y);
    }
}
