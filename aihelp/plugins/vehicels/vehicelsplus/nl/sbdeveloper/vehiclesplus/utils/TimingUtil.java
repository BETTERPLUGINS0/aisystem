/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimingUtil {
    private static final Map<String, TimingUtil> timers = new HashMap<String, TimingUtil>();
    private static Map<UUID, Long> inTimer = new HashMap<UUID, Long>();

    private TimingUtil() {
    }

    public static boolean isInTimer(String string2, int n, UUID uUID) {
        TimingUtil timingUtil = timers.computeIfAbsent(string2, string -> new TimingUtil());
        if (!inTimer.containsKey(uUID)) {
            inTimer.put(uUID, System.currentTimeMillis());
            return false;
        }
        return inTimer.get(uUID) - System.currentTimeMillis() * 1000L < (long)n;
    }
}

