/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils;

import nl.mtvehicles.core.infrastructure.libs.nbtapi.NbtApiException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.MinecraftVersion;

public class CheckUtil {
    private CheckUtil() {
    }

    public static void assertAvailable(MinecraftVersion version) {
        if (!MinecraftVersion.isAtLeastVersion(version)) {
            throw new NbtApiException("This Method is only avaliable for the version " + version.name() + " and above!");
        }
    }
}

