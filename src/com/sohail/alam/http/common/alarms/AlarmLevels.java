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