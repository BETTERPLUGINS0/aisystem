/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.MinecraftVersion;

public class NbtApiException
extends RuntimeException {
    private static final long serialVersionUID = -993309714559452334L;
    public static Boolean confirmedBroken = null;

    public NbtApiException() {
    }

    public NbtApiException(String string, Throwable throwable) {
        super(NbtApiException.generateMessage(string), throwable);
    }

    public NbtApiException(String string) {
        super(NbtApiException.generateMessage(string));
    }

    public NbtApiException(Throwable throwable) {
        super(NbtApiException.generateMessage(throwable == null ? null : throwable.toString()), throwable);
    }

    private static String generateMessage(String string) {
        if (string == null) {
            return null;
        }
        if (confirmedBroken == null) {
            return "[?][" + MinecraftVersion.getNBTAPIVersion() + "]" + string;
        }
        if (!confirmedBroken.booleanValue()) {
            return "[Selfchecked][" + MinecraftVersion.getNBTAPIVersion() + "]" + string;
        }
        return "[" + (Object)((Object)MinecraftVersion.getVersion()) + "][" + MinecraftVersion.getNBTAPIVersion() + "]There were errors detected during the server self-check! Please, make sure that NBT-API is up to date. Error message: " + string;
    }
}

