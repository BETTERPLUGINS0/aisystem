/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Art
 */
package me.zombie_striker.qav.util.xseries.art;

import org.bukkit.Art;

public abstract class BukkitArt {
    public abstract int getBlockWidth();

    public abstract int getBlockHeight();

    public abstract String getKey();

    public abstract int getId();

    public abstract Art object();

    public int hashCode() {
        return this.object().hashCode();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (object instanceof BukkitArt) {
            return this.object().equals((Object)((BukkitArt)object).object());
        }
        return this.object().equals(object);
    }

    public String toString() {
        return this.getClass().getSimpleName() + '(' + this.object().toString() + ')';
    }
}

