package com.yurjinia.user.utils;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class TimeZoneUtils {
    private static final String DEFAULT_TIME_ZONE = "UTC";

    // Приватный конструктор, чтобы предотвратить создание экземпляра
    private TimeZoneUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Преобразование Instant (UTC) в локальное время пользователя.
     *
     * @param utcTime   Время в формате Instant (UTC).
     * @param timeZone  Часовой пояс пользователя (например, "Europe/Berlin").
     * @return Локальное время в формате LocalDateTime.
     */
    public static LocalDateTime convertToLocalTime(Instant utcTime, String timeZone) {
        ZoneId zoneId = ZoneId.of(timeZone != null ? timeZone : DEFAULT_TIME_ZONE);
        return LocalDateTime.ofInstant(utcTime, zoneId);
    }

    /**
     * Преобразование локального времени в Instant (UTC).
     *
     * @param localDateTime Локальное время.
     * @param timeZone      Часовой пояс пользователя.
     * @return Время в формате Instant (UTC).
     */
    public static Instant convertToUTC(LocalDateTime localDateTime, String timeZone) {
        ZoneId zoneId = ZoneId.of(timeZone != null ? timeZone : DEFAULT_TIME_ZONE);
        return localDateTime.atZone(zoneId).toInstant();
    }

    /**
     * Форматирование даты в строку для отображения.
     *
     * @param localDateTime Локальное время.
     * @param format        Формат отображения (например, "yyyy-MM-dd HH:mm:ss").
     * @return Отформатированная строка с датой.
     */
    public static String formatDate(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    /**
     * Получить текущее время в UTC.
     *
     * @return Время в формате Instant (UTC).
     */
    public static Instant getCurrentUTC() {
        return Instant.now();
    }
}
