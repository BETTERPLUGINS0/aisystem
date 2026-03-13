/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.md_5.bungee.api.ChatMessageType
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.magmaguy.magmacore.instance;

import com.magmaguy.magmacore.MagmaCore;
import com.magmaguy.magmacore.events.MatchDestroyEvent;
import com.magmaguy.magmacore.events.MatchInstantiateEvent;
import com.magmaguy.magmacore.events.MatchJoinEvent;
import com.magmaguy.magmacore.instance.MatchInstanceConfiguration;
import com.magmaguy.magmacore.instance.MatchInstanceInterface;
import com.magmaguy.magmacore.instance.MatchPlayer;
import com.magmaguy.magmacore.util.Logger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import lombok.Generated;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class MatchInstance
implements MatchInstanceInterface {
    protected static final HashSet<MatchInstance> instances = new HashSet();
    private final MatchInstanceConfiguration matchInstanceConfiguration;
    protected HashSet<MatchPlayer> players = new HashSet();
    protected HashSet<MatchPlayer> spectators = new HashSet();
    protected InstanceState state = InstanceState.WAITING;
    protected String permission = null;
    private TickTask tick = null;

    public MatchInstance(MatchInstanceConfiguration matchInstanceConfiguration) {
        this.matchInstanceConfiguration = matchInstanceConfiguration;
        instances.add(this);
    }

    public static void shutdown() {
        HashSet<MatchInstance> cloneInstance = new HashSet<MatchInstance>(instances);
        cloneInstance.forEach(MatchInstance::destroyMatch);
        instances.clear();
    }

    public MatchInstantiateEvent start() {
        MatchInstantiateEvent matchInstantiateEvent = new MatchInstantiateEvent(this);
        if (matchInstantiateEvent.isCancelled()) {
            return matchInstantiateEvent;
        }
        this.tick = new TickTask();
        this.tick.runTaskTimer((Plugin)MagmaCore.getInstance().getRequestingPlugin(), 0L, 1L);
        this.countdownMatch();
        return matchInstantiateEvent;
    }

    public boolean addNewPlayer(Player player) {
        MatchJoinEvent event = new MatchJoinEvent(this, player);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return false;
        }
        if (this.getMatchInstanceConfiguration().getMatchGamemode() != null) {
            player.setGameMode(this.getMatchInstanceConfiguration().getMatchGamemode());
        }
        if (this.getMatchInstanceConfiguration().getDungeonPermission() != null && !player.hasPermission(this.getMatchInstanceConfiguration().getDungeonPermission())) {
            player.sendMessage(this.matchInstanceConfiguration.getFailedToJoinOngoingMatchAsPlayerNoPermission());
            return false;
        }
        if (!this.state.equals((Object)InstanceState.WAITING)) {
            event.setCancelled(true);
            player.sendMessage(this.matchInstanceConfiguration.getFailedToJoinOngoingMatchAsPlayerMessage());
            return false;
        }
        if (this.players.size() + 1 > this.matchInstanceConfiguration.getMaxPlayers()) {
            player.sendMessage(this.matchInstanceConfiguration.getFailedToJoinOngoingMatchAsPlayerInstanceIsFull());
            return false;
        }
        MatchPlayer matchPlayer = new MatchPlayer(player, player.getLocation(), this.matchInstanceConfiguration.getFallbackLocation(), player.getGameMode(), this, this.getMatchInstanceConfiguration().getLives(), MatchPlayer.MatchPlayerType.PLAYER);
        this.players.add(matchPlayer);
        return this.initializeNewPlayerOrSpectator(matchPlayer, this.players);
    }

    public boolean addNewPlayer(MatchPlayer matchPlayer) {
        Player player = matchPlayer.getPlayer();
        MatchJoinEvent event = new MatchJoinEvent(this, player);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return false;
        }
        if (this.getMatchInstanceConfiguration().getMatchGamemode() != null) {
            player.setGameMode(this.getMatchInstanceConfiguration().getMatchGamemode());
        }
        if (this.getMatchInstanceConfiguration().getDungeonPermission() != null && !player.hasPermission(this.getMatchInstanceConfiguration().getDungeonPermission())) {
            player.sendMessage(this.matchInstanceConfiguration.getFailedToJoinOngoingMatchAsPlayerNoPermission());
            return false;
        }
        if (!this.state.equals((Object)InstanceState.WAITING)) {
            event.setCancelled(true);
            player.sendMessage(this.matchInstanceConfiguration.getFailedToJoinOngoingMatchAsPlayerMessage());
            return false;
        }
        if (this.players.size() + 1 > this.matchInstanceConfiguration.getMaxPlayers()) {
            player.sendMessage(this.matchInstanceConfiguration.getFailedToJoinOngoingMatchAsPlayerInstanceIsFull());
            return false;
        }
        this.players.add(matchPlayer);
        return this.initializeNewPlayerOrSpectator(matchPlayer, this.players);
    }

    public boolean addNewSpectator(Player player) {
        if (!this.getMatchInstanceConfiguration().isSpectatable()) {
            return false;
        }
        MatchJoinEvent event = new MatchJoinEvent(this, player);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return false;
        }
        if (this.getMatchInstanceConfiguration().getDungeonPermission() != null && !player.hasPermission(this.getMatchInstanceConfiguration().getDungeonPermission())) {
            player.sendMessage(this.matchInstanceConfiguration.getFailedToJoinOngoingMatchAsPlayerNoPermission());
            return false;
        }
        MatchPlayer matchPlayer = new MatchPlayer(player, player.getLocation(), this.matchInstanceConfiguration.getFallbackLocation(), player.getGameMode(), this, this.getMatchInstanceConfiguration().getLives(), MatchPlayer.MatchPlayerType.SPECTATOR);
        this.spectators.add(matchPlayer);
        return this.initializeNewPlayerOrSpectator(matchPlayer, this.spectators);
    }

    private boolean initializeNewPlayerOrSpectator(final MatchPlayer matchPlayer, HashSet<MatchPlayer> playerOrSpectatorSet) {
        playerOrSpectatorSet.add(matchPlayer);
        matchPlayer.sendMessage(this.matchInstanceConfiguration.getMatchJoinAsPlayerMessage().replace("$count", "" + this.matchInstanceConfiguration.getMinPlayers()));
        matchPlayer.sendTitle(this.matchInstanceConfiguration.getMatchJoinAsPlayerTitle(), this.matchInstanceConfiguration.getMatchStartingSubtitle(), 60, 180, 60);
        new BukkitRunnable(){

            public void run() {
                if (MatchInstance.this.matchInstanceConfiguration.getLobbyLocation() != null && MatchInstance.this.state.equals((Object)InstanceState.WAITING)) {
                    matchPlayer.teleport(MatchInstance.this.matchInstanceConfiguration.getLobbyLocation());
                } else if (MatchInstance.this.matchInstanceConfiguration.getStartLocation() != null) {
                    matchPlayer.teleport(MatchInstance.this.matchInstanceConfiguration.getStartLocation());
                }
            }
        }.runTaskLater((Plugin)MagmaCore.getInstance().getRequestingPlugin(), 1L);
        return true;
    }

    public void postPlayerRemovalCheck(MatchPlayer matchPlayer) {
        if (this.players.isEmpty()) {
            this.endMatch();
        }
    }

    public void playerDeath(MatchPlayer matchPlayer) {
        matchPlayer.setLives(matchPlayer.getLives() - 1);
        if (matchPlayer.getLives() <= 0) {
            matchPlayer.removeMatchPlayer();
        }
    }

    public void makeSpectator(MatchPlayer matchPlayer) {
        if (!this.matchInstanceConfiguration.isSpectatable()) {
            return;
        }
        this.players.remove(matchPlayer);
        this.spectators.add(matchPlayer);
    }

    public boolean isPlayer(MatchPlayer matchPlayer) {
        return this.players.contains(matchPlayer);
    }

    public boolean isSpectator(MatchPlayer matchPlayer) {
        return this.spectators.contains(matchPlayer);
    }

    public void removeSpectator(MatchPlayer matchPlayer) {
        matchPlayer.removeMatchPlayer();
        this.spectators.remove(matchPlayer);
    }

    public void countdownMatch() {
        if (this.state != InstanceState.WAITING) {
            return;
        }
        if (this.players.size() < this.matchInstanceConfiguration.getMinPlayers()) {
            this.announceChat(this.matchInstanceConfiguration.getMatchFailedToStartNotEnoughPlayersMessage().replace("$amount", "" + this.matchInstanceConfiguration.getMinPlayers()));
            return;
        }
        this.state = InstanceState.STARTING;
        new CountdownTask().runTaskTimer((Plugin)MagmaCore.getInstance().getRequestingPlugin(), 0L, 20L);
    }

    private void playerWatchdog() {
        ((HashSet)this.players.clone()).forEach(matchPlayer -> {
            if (!matchPlayer.getPlayer().isOnline()) {
                matchPlayer.removeMatchPlayer();
            }
            if (!this.isInRegion(matchPlayer.getPlayer().getLocation())) {
                MatchInstanceEvents.teleportBypass = true;
                matchPlayer.teleport(this.matchInstanceConfiguration.getStartLocation());
            }
        });
    }

    private void spectatorWatchdog() {
        ((HashSet)this.spectators.clone()).forEach(matchPlayer -> {
            if (!matchPlayer.getPlayer().isOnline()) {
                this.removeSpectator((MatchPlayer)matchPlayer);
            }
            if (!this.isInRegion(matchPlayer.getPlayer().getLocation())) {
                MatchInstanceEvents.teleportBypass = true;
                matchPlayer.teleport(this.matchInstanceConfiguration.getStartLocation());
            }
        });
    }

    private void intruderWatchdog() {
        if (this.state != InstanceState.ONGOING) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            MatchPlayer matchPlayer = MatchPlayer.getMatchPlayer(player);
            if (matchPlayer != null && matchPlayer.getMatchInstance().equals(this) || !this.isInRegion(player.getLocation())) continue;
            this.kickPlayerOut(player);
        }
    }

    private void kickPlayerOut(Player player) {
        if (player.isOp()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent)new TextComponent("You are intruding on a match, but won't get kicked you because you're an OP!"));
            return;
        }
        MatchPlayer matchPlayer = MatchPlayer.getMatchPlayer(player);
        if (matchPlayer != null) {
            matchPlayer.removeMatchPlayer();
        }
    }

    public void announceChat(String message) {
        this.getAllParticipants().forEach(matchPlayer -> matchPlayer.sendMessage(message));
    }

    public void announceTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.getAllParticipants().forEach(matchPlayer -> matchPlayer.sendTitle(title, subtitle, fadeIn, stay, fadeOut));
    }

    protected List<MatchPlayer> getAllParticipants() {
        ArrayList<MatchPlayer> matchPlayers = new ArrayList<MatchPlayer>();
        matchPlayers.addAll(this.players);
        matchPlayers.addAll(this.spectators);
        return matchPlayers;
    }

    private void startMessage(int counter, MatchPlayer matchPlayer) {
        matchPlayer.getPlayer().sendTitle(this.matchInstanceConfiguration.getMatchStartingTitle().replace("$count", "" + (3 - counter)), this.matchInstanceConfiguration.getMatchStartingSubtitle().replace("$count", "" + (3 - counter)), 0, 20, 0);
    }

    @Override
    public abstract boolean isInRegion(Location var1);

    protected void finishStarting() {
        this.state = InstanceState.ONGOING;
        this.players.forEach(matchPlayer -> {
            if (this.matchInstanceConfiguration.getStartLocation() != null) {
                matchPlayer.teleport(this.matchInstanceConfiguration.getStartLocation());
            }
        });
    }

    public void victory() {
        this.state = InstanceState.COMPLETED_VICTORY;
        this.endMatch();
    }

    public void defeat() {
        this.state = InstanceState.COMPLETED_DEFEAT;
        this.endMatch();
    }

    protected void endMatch() {
        if (this.state != InstanceState.COMPLETED_VICTORY && this.state != InstanceState.COMPLETED_DEFEAT) {
            this.state = InstanceState.COMPLETED;
        }
        Logger.debug("MATCH ENDED");
        this.destroyMatch();
    }

    protected void destroyMatch() {
        this.state = InstanceState.WAITING;
        List<MatchPlayer> copy = this.getAllParticipants();
        copy.forEach(MatchPlayer::removeMatchPlayer);
        this.players.clear();
        this.spectators.clear();
        if (this.tick != null && !this.tick.isCancelled()) {
            this.tick.cancel();
        }
        Bukkit.getPluginManager().callEvent((Event)new MatchDestroyEvent(this));
    }

    @Generated
    public static HashSet<MatchInstance> getInstances() {
        return instances;
    }

    @Generated
    public MatchInstanceConfiguration getMatchInstanceConfiguration() {
        return this.matchInstanceConfiguration;
    }

    @Generated
    public HashSet<MatchPlayer> getPlayers() {
        return this.players;
    }

    @Generated
    public InstanceState getState() {
        return this.state;
    }

    @Generated
    public String getPermission() {
        return this.permission;
    }

    public static enum InstanceState {
        WAITING,
        STARTING,
        ONGOING,
        COMPLETED,
        COMPLETED_VICTORY,
        COMPLETED_DEFEAT;

    }

    private class TickTask
    extends BukkitRunnable {
        private TickTask() {
        }

        public void run() {
            MatchInstance.this.playerWatchdog();
            MatchInstance.this.spectatorWatchdog();
            MatchInstance.this.intruderWatchdog();
        }
    }

    private class CountdownTask
    extends BukkitRunnable {
        int counter = 0;

        private CountdownTask() {
        }

        public void run() {
            if (MatchInstance.this.players.size() < MatchInstance.this.matchInstanceConfiguration.getMinPlayers()) {
                this.cancel();
                MatchInstance.this.endMatch();
                return;
            }
            ++this.counter;
            MatchInstance.this.players.forEach(matchPlayer -> MatchInstance.this.startMessage(this.counter, (MatchPlayer)matchPlayer));
            MatchInstance.this.spectators.forEach(matchPlayer -> MatchInstance.this.startMessage(this.counter, (MatchPlayer)matchPlayer));
            if (this.counter >= 3) {
                MatchInstance.this.finishStarting();
                this.cancel();
            }
        }
    }

    public static class MatchInstanceEvents
    implements Listener {
        public static boolean teleportBypass = false;

        @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
        public void onPlayerDamage(EntityDamageEvent event) {
            if (!event.getEntityType().equals((Object)EntityType.PLAYER)) {
                return;
            }
            MatchPlayer matchPlayer = MatchPlayer.getMatchPlayer((Player)event.getEntity());
            if (matchPlayer == null) {
                return;
            }
            if (event.getFinalDamage() < matchPlayer.getPlayer().getHealth()) {
                return;
            }
            if (matchPlayer.getMatchInstance().state != InstanceState.ONGOING) {
                matchPlayer.removeMatchPlayer();
            }
            event.setCancelled(true);
            matchPlayer.getMatchInstance().playerDeath(matchPlayer);
        }
    }
}

