/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Art
 */
package me.zombie_striker.qav.util.xseries.art;

import me.zombie_striker.qav.util.xseries.art.BukkitArt;
import org.bukkit.Art;

class NewArt
extends BukkitArt {
    private final Art art;

    public NewArt(Object object) {
        this.art = (Art)object;
    }

    @Override
    public int getBlockWidth() {
        return this.art.getBlockWidth();
    }

    @Override
    public int getBlockHeight() {
        return this.art.getBlockHeight();
    }

    @Override
    public String getKey() {
        return this.art.getKey().getKey();
    }

    @Override
    public int getId() {
        return this.art.getId();
    }

    @Override
    public Art object() {
        return this.art;
    }
}

