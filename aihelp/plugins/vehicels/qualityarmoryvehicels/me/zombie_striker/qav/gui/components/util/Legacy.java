/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.gui.components.util;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class Legacy {
    public static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();

    private Legacy() {
        throw new UnsupportedOperationException("Class should not be instantiated!");
    }
}

