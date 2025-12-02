package team.upao.dev.common.utils;


import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

public class ExpirationUtils {
    public static boolean isExpirationAfterNow(String expirationTimestamp) {
        if (expirationTimestamp == null) return false;
        String s = expirationTimestamp;
        if (s.isEmpty()) return false;

        try {
            long parsed = Long.parseLong(s);
            Instant expiration = (s.length() >= 13) ? Instant.ofEpochMilli(parsed) : Instant.ofEpochSecond(parsed);
            return expiration.isAfter(Instant.now());
        } catch (NumberFormatException ignored) { }

        try {
            Instant i = Instant.parse(s);
            return i.isAfter(Instant.now());
        } catch (DateTimeParseException ex1) {
            try {
                Instant i = OffsetDateTime.parse(s).toInstant();
                return i.isAfter(Instant.now());
            } catch (DateTimeParseException ex2) {
                return false;
            }
        }
    }

    public static long computeExpirationEpochSeconds(String expirationDateStr) {
        Instant fallback = Instant.ofEpochMilli(System.currentTimeMillis() + 24L * 60 * 60 * 1000);
        if (expirationDateStr == null) return fallback.getEpochSecond();
        String s = expirationDateStr.trim();
        if (s.isEmpty()) return fallback.getEpochSecond();

        try {
            long parsed = Long.parseLong(s);
            if (s.length() >= 13) {
                return Instant.ofEpochMilli(parsed).getEpochSecond();
            } else {
                return Instant.ofEpochSecond(parsed).getEpochSecond();
            }
        } catch (NumberFormatException ignored) { }

        try {
            Instant i = Instant.parse(s);
            return i.getEpochSecond();
        } catch (DateTimeParseException ex1) {
            try {
                Instant i = OffsetDateTime.parse(s).toInstant();
                return i.getEpochSecond();
            } catch (DateTimeParseException ex2) {
                return fallback.getEpochSecond();
            }
        }
    }
}
