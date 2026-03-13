/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package com.magmaguy.magmacore.instance;

import com.magmaguy.magmacore.events.MatchLeaveEvent;
import com.magmaguy.magmacore.instance.MatchInstance;
import com.magmaguy.magmacore.util.AttributeManager;
import com.magmaguy.magmacore.util.Logger;
import java.util.HashMap;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class MatchPlayer {
    public static HashMap<UUID, MatchPlayer> matchPlayers = new HashMap();
    private final Player player;
    private final Location previousLocation;
    private final GameMode previousGameMode;
    private final MatchInstance matchInstance;
    private final Location fallbackLocation;
    private int lives;
    private MatchPlayerType matchPlayerType;
    private DeathLocation deathLocation = null;

    public MatchPlayer(Player player, Location previousLocation, Location fallbackLocation, GameMode previousGameMode, MatchInstance matchInstance, int lives, MatchPlayerType matchPlayerType) {
        this.player = player;
        this.previousLocation = previousLocation;
        this.fallbackLocation = fallbackLocation;
        this.previousGameMode = previousGameMode;
        this.matchInstance = matchInstance;
        this.lives = lives;
        this.matchPlayerType = matchPlayerType;
        matchPlayers.put(player.getUniqueId(), this);
    }

    public static MatchPlayer getMatchPlayer(Player player) {
        return matchPlayers.get(player.getUniqueId());
    }

    public void createDeathLocation() {
        this.deathLocation = new DeathLocation(this.player.getLocation().getBlock());
        this.player.setGameMode(GameMode.SPECTATOR);
    }

    public void revive() {
        this.teleport(this.deathLocation.block.getLocation());
        this.deathLocation.block.setType(Material.AIR, false);
        this.deathLocation = null;
        this.player.setGameMode(GameMode.SURVIVAL);
    }

    public void sendMessage(String message) {
        Logger.sendSimpleMessage((CommandSender)this.player, message);
    }

    public void sendTitle(String title, String subtitle) {
        Logger.sendTitle(this.player, title, subtitle);
    }

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Logger.sendTitle(this.player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public void removeMatchPlayer() {
        if (this.previousGameMode != null) {
            this.player.setGameMode(this.previousGameMode);
        }
        this.player.setHealth(AttributeManager.getAttributeBaseValue((LivingEntity)this.player, "generic_max_health"));
        if (this.matchInstance.getMatchInstanceConfiguration().getExitLocation() != null) {
            this.teleport(this.matchInstance.getMatchInstanceConfiguration().getExitLocation());
        } else if (this.previousLocation != null && this.previousLocation.getWorld() != null) {
            this.teleport(this.previousLocation);
        } else {
            this.teleport(this.fallbackLocation);
        }
        Bukkit.getPluginManager().callEvent((Event)new MatchLeaveEvent(this.matchInstance, this));
        if (this.matchInstance.isPlayer(this)) {
            this.matchInstance.players.remove(this);
        }
        if (this.matchInstance.isSpectator(this)) {
            this.matchInstance.spectators.remove(this);
        }
        this.matchInstance.postPlayerRemovalCheck(this);
    }

    public void teleport(Location location) {
        MatchInstance.MatchInstanceEvents.teleportBypass = true;
        this.player.teleport(location);
        MatchInstance.MatchInstanceEvents.teleportBypass = false;
    }

    @Generated
    public Player getPlayer() {
        return this.player;
    }

    @Generated
    public Location getPreviousLocation() {
        return this.previousLocation;
    }

    @Generated
    public GameMode getPreviousGameMode() {
        return this.previousGameMode;
    }

    @Generated
    public MatchInstance getMatchInstance() {
        return this.matchInstance;
    }

    @Generated
    public Location getFallbackLocation() {
        return this.fallbackLocation;
    }

    @Generated
    public int getLives() {
        return this.lives;
    }

    @Generated
    public void setLives(int lives) {
        this.lives = lives;
    }

    @Generated
    public MatchPlayerType getMatchPlayerType() {
        return this.matchPlayerType;
    }

    @Generated
    public void setMatchPlayerType(MatchPlayerType matchPlayerType) {
        this.matchPlayerType = matchPlayerType;
    }

    @Generated
    public DeathLocation getDeathLocation() {
        return this.deathLocation;
    }

    private record DeathLocation(Block block) {
    }

    public static enum MatchPlayerType {
        PLAYER,
        SPECTATOR;

    }

    public static class MatchPlayerEvents
    implements Listener {
        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            MatchPlayer matchPlayer = matchPlayers.get(event.getPlayer().getUniqueId());
            if (matchPlayer == null) {
                return;
            }
            matchPlayer.removeMatchPlayer();
        }
    }
}

