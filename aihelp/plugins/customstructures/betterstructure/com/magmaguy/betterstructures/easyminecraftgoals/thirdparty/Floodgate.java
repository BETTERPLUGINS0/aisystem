/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.geysermc.floodgate.api.FloodgateApi
 */
package com.magmaguy.betterstructures.easyminecraftgoals.thirdparty;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

class Floodgate {
    private Floodgate() {
    }

    static boolean isBedrock(Player player) {
        return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
    }
}

