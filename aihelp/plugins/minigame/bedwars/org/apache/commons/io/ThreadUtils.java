/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io;

import java.time.Duration;
import java.time.Instant;

public final class ThreadUtils {
    private static int getNanosOfMilli(Duration duration) {
        return duration.getNano() % 1000000;
    }

    public static void sleep(Duration duration) throws InterruptedException {
        Instant finishInstant = Instant.now().plus(duration);
        Duration remainingDuration = duration;
        do {
            Thread.sleep(remainingDuration.toMillis(), ThreadUtils.getNanosOfMilli(remainingDuration));
        } while (!(remainingDuration = Duration.between(Instant.now(), finishInstant)).isNegative());
    }
}

