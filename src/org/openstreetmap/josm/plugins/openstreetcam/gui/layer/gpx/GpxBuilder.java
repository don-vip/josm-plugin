/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer.gpx;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openstreetmap.josm.data.gpx.GpxData;
import org.openstreetmap.josm.data.gpx.GpxTrack;
import org.openstreetmap.josm.data.gpx.ImmutableGpxTrack;
import org.openstreetmap.josm.data.gpx.WayPoint;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;


/**
 * Converts business entities to GPX format.
 *
 * @author beataj
 * @version $Revision$
 */
final class GpxBuilder {

    private static final String EXTENSIONS = "extensions";
    private static final String HEADING = "heading";
    private static final String TIME = "time";
    private static final String UTC = "UTC";
    private static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm'Z'";

    private GpxBuilder() {}


    /**
     * Builds a GPX structure for the sequence business entity. A sequence represents the GPS track associated with the
     * selected photo location.
     *
     * @param sequence a {@code Sequence} object
     * @return a {@code GpxData} object
     */
    static GpxData buildSequenceGpx(final Sequence sequence) {
        final Collection<Collection<WayPoint>> trk = new ArrayList<>();
        final List<WayPoint> wayPoints = new ArrayList<>();
        for (final Photo photo : sequence.getPhotos()) {
            wayPoints.add(createWayPoint(photo));
        }
        trk.add(wayPoints);
        final Map<String, Object> trkAttr = new HashMap<>();
        trkAttr.put(EXTENSIONS, HEADING);
        final GpxTrack gpsTrack = new ImmutableGpxTrack(trk, trkAttr);
        final GpxData gpxData = new GpxData();
        gpxData.addTrack(gpsTrack);
        return gpxData;
    }


    private static WayPoint createWayPoint(final Photo photo) {
        final WayPoint wayPoint = new WayPoint(photo.getLocation());
        final LocalDateTime dateTime =
                LocalDateTime.parse(photo.getShotDate(), DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT));
        final ZonedDateTime utcZoned = dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(UTC));
        wayPoint.put(TIME, DateTimeFormatter.ofPattern(UTC_DATE_FORMAT).format(utcZoned));
        if (photo.getHeading() != null) {
            wayPoint.addExtension(HEADING, Double.toString(photo.getHeading()));
        }
        return wayPoint;
    }
}