/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.enums;

public enum DriveUp {
    SLABS,
    BLOCKS,
    BOTH;


    public boolean isSlabs() {
        return this.equals((Object)SLABS);
    }

    public boolean isBlocks() {
        return this.equals((Object)BLOCKS);
    }

    public boolean isBoth() {
        return this.equals((Object)BOTH);
    }
}

