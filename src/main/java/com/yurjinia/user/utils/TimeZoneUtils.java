package com.yurjinia.user.utils;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@UtilityClass
public final class TimeZoneUtils {

    private static final String DEFAULT_TIME_ZONE = "UTC";

    /**
     * Converts an Instant (UTC) to the user's local time.
     *
     * @param utcTime  The time in Instant (UTC) format.
     * @param timeZone The user's time zone (e.g., "Europe/Berlin").
     * @return The local time in LocalDateTime format.
     */
    public static LocalDateTime toLocalDateTime(Instant utcTime, String timeZone) {
        ZoneId zoneId = ZoneId.of(timeZone != null ? timeZone : DEFAULT_TIME_ZONE);
        return LocalDateTime.ofInstant(utcTime, zoneId);
    }

}
