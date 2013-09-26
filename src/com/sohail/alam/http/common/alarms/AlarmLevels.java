/*
 * Copyright 2013 The Http Server & Proxy
 *
 *  The File Watch Service Project licenses this file to you under the Apache License, version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at:
 *
 *               http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.sohail.alam.http.common.alarms;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 24/9/13
 * Time: 1:08 AM
 */
public interface AlarmLevels {
    // ITU Perceived Severity      syslog SEVERITY (Name)
    public static final int CRITICAL = 1; //(Alert)
    public static final int MAJOR = 2; //(Critical)
    public static final int MINOR = 3; //(Error)
    public static final int WARNING = 4; //(Warning)
    public static final int INDETERMINATE = 5; //(Notice)
    public static final int CLEARED = 5; //(Notice)
}