/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.spigotmc.CustomTimingsHandler
 */
package co.aikar.commands.lib.timings;

import co.aikar.commands.lib.timings.MCTiming;
import org.bukkit.Bukkit;
import org.spigotmc.CustomTimingsHandler;

class SpigotTiming
extends MCTiming {
    private final CustomTimingsHandler timing;

    SpigotTiming(String string) {
        this.timing = new CustomTimingsHandler(string);
    }

    @Override
    public MCTiming startTiming() {
        if (Bukkit.isPrimaryThread()) {
            this.timing.startTiming();
        }
        return this;
    }

    @Override
    public void stopTiming() {
        if (Bukkit.isPrimaryThread()) {
            this.timing.stopTiming();
        }
    }
}

