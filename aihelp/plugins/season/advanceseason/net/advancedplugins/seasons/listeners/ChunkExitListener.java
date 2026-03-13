/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package net.advancedplugins.seasons.listeners;

import net.advancedplugins.seasons.Core;
import org.bukkit.Chunk;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ChunkExitListener
implements Listener {
    public void onWalk(PlayerMoveEvent playerMoveEvent) {
        if (this.sameChunk(playerMoveEvent.getFrom().getChunk(), playerMoveEvent.getTo().getChunk())) {
            return;
        }
        if (playerMoveEvent.getPlayer().isFlying()) {
            return;
        }
        if (!Core.getWorldHandler().isWorldEnabled(playerMoveEvent.getPlayer().getWorld().getName())) {
            return;
        }
        Core.getBiomesHandler().getRenderHandler().refreshForPlayer(playerMoveEvent.getPlayer(), false);
    }

    private boolean sameChunk(Chunk chunk, Chunk chunk2) {
        return chunk.getX() == chunk2.getX() && chunk.getZ() == chunk2.getZ();
    }
}

