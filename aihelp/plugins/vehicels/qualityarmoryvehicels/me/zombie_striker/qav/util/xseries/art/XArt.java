/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Art
 */
package me.zombie_striker.qav.util.xseries.art;

import me.zombie_striker.qav.util.xseries.art.BukkitArt;
import me.zombie_striker.qav.util.xseries.art.NewArt;
import me.zombie_striker.qav.util.xseries.art.OldArt;
import org.bukkit.Art;

public class XArt {
    private static final boolean USE_INTERFACE = Art.class.isInterface();

    private XArt() {
    }

    public static BukkitArt of(Art art) {
        if (USE_INTERFACE) {
            return new NewArt(art);
        }
        return new OldArt(art);
    }
}

