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
 *
 * @author beataj
 * @version $Revision$
 */
public class CacheSettings {

    private final int memoryCount;
    private final int diskCount;
    private final int prevNextCount;
    private final int nearbyCount;


    public CacheSettings(final int memoryCount, final int diskCount, final int prevNextCount, final int nearbyCount) {
        this.memoryCount = memoryCount;
        this.diskCount = diskCount;
        this.prevNextCount = prevNextCount;
        this.nearbyCount = nearbyCount;
    }


    public int getMemoryCount() {
        return memoryCount;
    }

    public int getDiskCount() {
        return diskCount;
    }

    public int getPrevNextCount() {
        return prevNextCount;
    }

    public int getNearbyCount() {
        return nearbyCount;
    }
}