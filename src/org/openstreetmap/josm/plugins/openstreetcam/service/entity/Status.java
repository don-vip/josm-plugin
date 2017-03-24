/*
 *  Copyright 2016 Telenav, Inc.
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
package org.openstreetmap.josm.plugins.openstreetcam.service.entity;


/**
 * Defines the attributes of the status.
 *
 * @author Beata
 * @version $Revision$
 */
public class Status {

    // setters are not required,since GSON sets the fields directly using reflection.
    private final Integer apiCode;
    private final String apiMessage;
    private final Integer httpCode;
    private final String httpMessage;


    public Status(final Integer apiCode, final String apiMessage, final Integer httpCode, final String httpMessage) {
        this.apiCode = apiCode;
        this.apiMessage = apiMessage;
        this.httpCode = httpCode;
        this.httpMessage = httpMessage;
    }

    public Integer getApiCode() {
        return apiCode;
    }

    public String getApiMessage() {
        return apiMessage;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public String getHttpMessage() {
        return httpMessage;
    }
}