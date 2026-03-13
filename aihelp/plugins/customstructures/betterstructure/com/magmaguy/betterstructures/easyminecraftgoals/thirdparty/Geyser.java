/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.geysermc.geyser.api.GeyserApi
 *  org.geysermc.geyser.api.connection.GeyserConnection
 */
package com.magmaguy.betterstructures.easyminecraftgoals.thirdparty;

import org.bukkit.entity.Player;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.connection.GeyserConnection;

class Geyser {
    private Geyser() {
    }

    static boolean isBedrock(Player player) {
        GeyserConnection geyserConnection = GeyserApi.api().connectionByUuid(player.getUniqueId());
        return geyserConnection != null;
    }
}

