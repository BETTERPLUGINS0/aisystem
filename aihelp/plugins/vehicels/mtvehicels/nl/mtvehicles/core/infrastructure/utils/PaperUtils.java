/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.mtvehicles.core.infrastructure.utils;

import lombok.Generated;

public final class PaperUtils {
    public static final boolean isRunningPaper = PaperUtils.hasClass("com.destroystokyo.paper.PaperConfig") || PaperUtils.hasClass("io.papermc.paper.configuration.Configuration");

    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Generated
    private PaperUtils() {
    }
}

