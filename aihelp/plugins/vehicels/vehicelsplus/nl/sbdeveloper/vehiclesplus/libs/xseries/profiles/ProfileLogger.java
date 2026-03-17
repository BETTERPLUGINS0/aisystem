/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.profiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ProfileLogger {
    public static final Logger LOGGER = LogManager.getLogger((String)"XSkull");

    public static void debug(String string, Object ... objectArray) {
        LOGGER.debug(string, objectArray);
    }
}

