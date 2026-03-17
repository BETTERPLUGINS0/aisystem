/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.gui.slot;

public record Slot(int row, int column) {
    public static Slot of(int n, int n2) {
        return new Slot(n, n2);
    }
}

