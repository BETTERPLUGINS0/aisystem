/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package net.advancedplugins.seasons.handlers.bedrock;

import net.advancedplugins.seasons.handlers.bedrock.BedrockHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BedrockListeners
implements Listener {
    private final BedrockHandler bedrockHandler;

    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        if (playerJoinEvent.getPlayer().getName().startsWith(".")) {
            this.bedrockHandler.getBedrockPlayers().add(playerJoinEvent.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        this.bedrockHandler.getBedrockPlayers().remove(playerQuitEvent.getPlayer().getUniqueId());
    }

    public BedrockListeners(BedrockHandler bedrockHandler) {
        this.bedrockHandler = bedrockHandler;
    }
}

