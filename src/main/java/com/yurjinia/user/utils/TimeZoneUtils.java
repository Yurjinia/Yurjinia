package com.yurjinia.user.utils;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class TimeZoneUtils {

    private static final String DEFAULT_TIME_ZONE = "UTC";

    private TimeZoneUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Converts an Instant (UTC) to the user's local time.
     *
     * @param utcTime   The time in Instant (UTC) format.
     * @param timeZone  The user's time zone (e.g., "Europe/Berlin").
     * @return The local time in LocalDateTime format.
     */
    public static LocalDateTime convertToLocalTime(Instant utcTime, String timeZone) {
        ZoneId zoneId = ZoneId.of(timeZone != null ? timeZone : DEFAULT_TIME_ZONE);
        return LocalDateTime.ofInstant(utcTime, zoneId);
    }
}
