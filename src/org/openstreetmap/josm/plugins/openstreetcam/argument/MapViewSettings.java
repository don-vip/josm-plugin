/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;


/**
 * Defines the user configurable map view settings attributes.
 *
 * @author beataj
 * @version $Revision$
 */
public class MapViewSettings {

    private final Integer photoZoom;
    private final boolean manualSwitchFlag;


    /**
     * Builds a new object with the given arguments.
     *
     * @param photoZoom the zoom level from which photo locations are displayed
     * @param manualSwitchFlag specifies if the map view data is changed manually or automatically based on current zoom
     */
    public MapViewSettings(final Integer photoZoom, final boolean manualSwitchFlag) {
        this.photoZoom = photoZoom;
        this.manualSwitchFlag = manualSwitchFlag;
    }

    public Integer getPhotoZoom() {
        return photoZoom;
    }

    public boolean isManualSwitchFlag() {
        return manualSwitchFlag;
    }
}