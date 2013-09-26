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
 * Time: 1:09 AM
 */

/**
 * Example 1 - Mandatory Alarm Information
 * <p/>
 * <pre>
 * &lt;165&gt;1 2003-10-11T22:14:15.003Z mymachine.example.com
 * evntslog - ID47 [exampleSDID@32473 iut="3" eventSource=
 * "Application" eventID="1011"][alarms resource="su root"
 * probableCause="unauthorizedAccessAttempt"
 * perceivedSeverity="major"]
 * BOMAn application event log entry...
 * </pre>
 * <p/>
 * In this example, extended from [RFC5424], the VERSION is 1 and the
 * Facility has the value of 4.  The severity is 2.  The message was
 * created on 11 October 2003 at 10:14:15pm UTC, 3 milliseconds into the
 * next second.  The message originated from a host that identifies
 * itself as "mymachine.example.com".  The APP-NAME is "evntslog" and
 * the PROCID is unknown.  The MSGID is "ID47".  We have included both
 * the structured data from the original example, a single element with
 * the value "[exampleSDID@32473 iut="3" eventSource="Application"
 * eventID="1011"]", and a new element with the alarms information
 * defined in this memo.  The alarms SD-ID contains the mandatory SD-
 * PARAMS of resource, probableCause, and preceivedSeverity.  The MSG
 * itself is "An application event log entry..."  The BOM at the
 * beginning of the MSG indicates UTF-8 encoding.
 * <p/>
 * Example 2 - Additional Alarm Information
 * <p/>
 * <pre>
 * &lt;165&gt;1 2004-11-10T20:15:15.003Z mymachine.example.com
 * evntslog - ID48 [alarms resource="interface 42"
 * probableCause="unauthorizedAccessAttempt"
 * perceivedSeverity="major"
 * eventType="communicationsAlarm"
 * resourceURI="snmp://example.com//1.3.6.1.2.1.2.2.1.1.42"]
 * </pre>
 * <p/>
 * In this example, we include two optional alarms fields: eventType and
 * resourceURI.
 */
public class AlarmStructuredData {
    /**
     * The unique ID for the alarms
     */
    public String id;
    /**
     * MANDATORY
     * This item uniquely identifies the resource under alarms
     * within the scope of a network element
     */
    public String resourceUnderAlarm;
    /**
     * MANDATORY
     * This parameter is the mnemonic associated with the
     * IANAItuProbableCause object defined within [RFC3877] and any
     * subsequent extensions defined by IANA
     */
    public String probableCause;
    /**
     * OPTIONAL
     * This parameter is the mnemonic associated with the
     * IANAItuEventType object defined within [RFC3877] and any subsequent
     * extensions defined by IANA
     */
    public String eventType;
    /**
     * MANDATORY
     * Similar to the definition of perceived severity in
     * [X.736] and [RFC3877], this object can take the following values:
     * cleared, indeterminate, critical, major, minor, warning
     */
    public String perceivedSeverity;
    /**
     * OPTIONAL
     * Similar to the definition of perceived severity
     * in [X.733] and [RFC3877], this object can take the following values:
     * moreSevere, noChange, lessSevere
     */
    public String trendIndication;
    /**
     * OPTIONAL
     * This item uniquely identifies the resource under alarms.
     * The value of this field MUST conform to the URI definition in
     * [RFC3986] and its updates.  In the case of an SNMP resource, the
     * syntax in [RFC4088] MUST be used and "resourceURI" must point to the
     * same resource as alarmActiveResourceId [RFC3877] for this alarms.
     */
    public String resourceURI;
}