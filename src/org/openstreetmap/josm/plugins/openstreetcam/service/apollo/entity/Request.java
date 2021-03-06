/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo.entity;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Contribution;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class Request {

    private final Detection detection;
    private final Contribution contribution;


    public Request(final Detection detection, final Contribution contribution) {
        this.detection = detection;
        this.contribution = contribution;
    }

    public Detection getDetection() {
        return detection;
    }

    public Contribution getContribution() {
        return contribution;
    }
}