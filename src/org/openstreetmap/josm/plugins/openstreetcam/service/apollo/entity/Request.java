/*
 *  Copyright 2017 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
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