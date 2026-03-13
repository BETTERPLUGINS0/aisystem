/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.time;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.LongRange;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.function.FailableBiConsumer;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableRunnable;
import org.apache.commons.lang3.math.NumberUtils;

public class DurationUtils {
    static final LongRange LONG_TO_INT_RANGE = LongRange.of(NumberUtils.LONG_INT_MIN_VALUE, NumberUtils.LONG_INT_MAX_VALUE);

    public static <T extends Throwable> void accept(FailableBiConsumer<Long, Integer, T> failableBiConsumer, Duration duration) {
        if (failableBiConsumer != null && duration != null) {
            failableBiConsumer.accept(duration.toMillis(), DurationUtils.getNanosOfMilli(duration));
        }
    }

    @Deprecated
    public static int getNanosOfMiili(Duration duration) {
        return DurationUtils.getNanosOfMilli(duration);
    }

    public static int getNanosOfMilli(Duration duration) {
        return DurationUtils.zeroIfNull(duration).getNano() % 1000000;
    }

    public static boolean isPositive(Duration duration) {
        return !duration.isNegative() && !duration.isZero();
    }

    private static <E extends Throwable> Instant now(FailableConsumer<Instant, E> failableConsumer) {
        Instant instant = Instant.now();
        failableConsumer.accept(instant);
        return instant;
    }

    public static <E extends Throwable> Duration of(FailableConsumer<Instant, E> failableConsumer) {
        return DurationUtils.since(DurationUtils.now(failableConsumer::accept));
    }

    public static <E extends Throwable> Duration of(FailableRunnable<E> failableRunnable) {
        return DurationUtils.of((Instant instant) -> failableRunnable.run());
    }

    public static Duration since(Temporal temporal) {
        return Duration.between(temporal, Instant.now());
    }

    static ChronoUnit toChronoUnit(TimeUnit timeUnit) {
        switch (Objects.requireNonNull(timeUnit)) {
            case NANOSECONDS: {
                return ChronoUnit.NANOS;
            }
            case MICROSECONDS: {
                return ChronoUnit.MICROS;
            }
            case MILLISECONDS: {
                return ChronoUnit.MILLIS;
            }
            case SECONDS: {
                return ChronoUnit.SECONDS;
            }
            case MINUTES: {
                return ChronoUnit.MINUTES;
            }
            case HOURS: {
                return ChronoUnit.HOURS;
            }
            case DAYS: {
                return ChronoUnit.DAYS;
            }
        }
        throw new IllegalArgumentException(timeUnit.toString());
    }

    public static Duration toDuration(long l, TimeUnit timeUnit) {
        return Duration.of(l, DurationUtils.toChronoUnit(timeUnit));
    }

    public static int toMillisInt(Duration duration) {
        Objects.requireNonNull(duration, "duration");
        return LONG_TO_INT_RANGE.fit(duration.toMillis()).intValue();
    }

    public static Duration zeroIfNull(Duration duration) {
        return ObjectUtils.defaultIfNull(duration, Duration.ZERO);
    }

    @Deprecated
    public DurationUtils() {
    }
}

