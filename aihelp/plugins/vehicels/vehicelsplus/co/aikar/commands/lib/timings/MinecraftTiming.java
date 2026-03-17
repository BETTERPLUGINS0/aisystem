/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  co.aikar.timings.Timing
 *  co.aikar.timings.Timings
 *  org.bukkit.plugin.Plugin
 */
package co.aikar.commands.lib.timings;

import co.aikar.commands.lib.timings.MCTiming;
import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import org.bukkit.plugin.Plugin;

class MinecraftTiming
extends MCTiming {
    private final Timing timing;

    MinecraftTiming(Plugin plugin, String string, MCTiming mCTiming) {
        this.timing = Timings.of((Plugin)plugin, (String)string, mCTiming instanceof MinecraftTiming ? ((MinecraftTiming)mCTiming).timing : null);
    }

    @Override
    public MCTiming startTiming() {
        this.timing.startTimingIfSync();
        return this;
    }

    @Override
    public void stopTiming() {
        this.timing.stopTimingIfSync();
    }
}

