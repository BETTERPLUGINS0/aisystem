/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.andrei1058.bedwars.support.paper;

import com.andrei1058.bedwars.BedWars;
import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class TeleportManager {
    public static void teleport(Entity entity, Location location) {
        TeleportManager.teleportC(entity, location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public static void teleportC(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause) {
        if (BedWars.isPaper && BedWars.config.getBoolean("performance-settings.paper-features")) {
            PaperLib.teleportAsync(entity, location, cause);
            return;
        }
        entity.teleport(location, cause);
    }
}

