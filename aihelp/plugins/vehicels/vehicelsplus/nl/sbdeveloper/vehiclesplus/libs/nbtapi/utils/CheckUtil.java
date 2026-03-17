/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils;

import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.MinecraftVersion;

public class CheckUtil {
    private CheckUtil() {
    }

    public static void assertAvailable(MinecraftVersion minecraftVersion) {
        if (!MinecraftVersion.isAtLeastVersion(minecraftVersion)) {
            throw new NbtApiException("This Method is only avaliable for the version " + minecraftVersion.name() + " and above!");
        }
    }
}

