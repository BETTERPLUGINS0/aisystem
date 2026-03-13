/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.betterstructures.content;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.betterstructures.content.BSPackage;
import com.magmaguy.magmacore.nightbreak.NightbreakContentRefresher;
import java.util.ArrayList;
import org.bukkit.plugin.java.JavaPlugin;

public class BSPackageRefresher {
    private static final long REFRESH_COOLDOWN_MS = 300000L;
    private static long lastRefresh = 0L;

    private BSPackageRefresher() {
    }

    public static void refreshContentAndAccess() {
        long now = System.currentTimeMillis();
        if (now - lastRefresh < 300000L) {
            return;
        }
        lastRefresh = now;
        NightbreakContentRefresher.refreshAsync((JavaPlugin)MetadataHandler.PLUGIN, new ArrayList<BSPackage>(BSPackage.getBsPackages().values()), bspPackage -> true, outdated -> {});
    }

    public static void reset() {
        lastRefresh = 0L;
    }
}

