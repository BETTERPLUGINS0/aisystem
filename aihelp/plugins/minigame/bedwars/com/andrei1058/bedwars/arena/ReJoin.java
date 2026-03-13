/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.arena;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.tasks.ReJoinTask;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.lobbysocket.ArenaSocket;
import com.andrei1058.bedwars.shop.ShopCache;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReJoin {
    private UUID player;
    private IArena arena;
    private ITeam bwt;
    private ReJoinTask task = null;
    private final ArrayList<ShopCache.CachedItem> permanentsAndNonDowngradables = new ArrayList();
    private static final List<ReJoin> reJoinList = new ArrayList<ReJoin>();

    public ReJoin(Player player, IArena arena, ITeam bwt, List<ShopCache.CachedItem> cachedArmor) {
        ReJoin rj = ReJoin.getPlayer(player);
        if (rj != null) {
            rj.destroy(true);
        }
        if (bwt == null) {
            return;
        }
        if (bwt.isBedDestroyed()) {
            return;
        }
        this.bwt = bwt;
        this.player = player.getUniqueId();
        this.arena = arena;
        reJoinList.add(this);
        BedWars.debug("Created ReJoin for " + player.getName() + " " + String.valueOf(player.getUniqueId()) + " at " + arena.getArenaName());
        if (bwt.getMembers().isEmpty()) {
            this.task = new ReJoinTask(arena, bwt);
        }
        this.permanentsAndNonDowngradables.addAll(cachedArmor);
        if (BedWars.autoscale) {
            JsonObject json = new JsonObject();
            json.addProperty("type", "RC");
            json.addProperty("uuid", player.getUniqueId().toString());
            json.addProperty("arena_id", arena.getWorldName());
            json.addProperty("server", BedWars.config.getString("bungee-settings.server-id"));
            ArenaSocket.sendMessage(json.toString());
        }
    }

    public static boolean exists(@NotNull Player pl) {
        BedWars.debug("ReJoin exists check " + String.valueOf(pl.getUniqueId()));
        for (ReJoin rj : ReJoin.getReJoinList()) {
            BedWars.debug("ReJoin exists check list scroll: " + rj.getPl().toString());
            if (!rj.getPl().equals(pl.getUniqueId())) continue;
            return true;
        }
        return false;
    }

    @Nullable
    public static ReJoin getPlayer(@NotNull Player player) {
        BedWars.debug("ReJoin getPlayer " + String.valueOf(player.getUniqueId()));
        for (ReJoin rj : ReJoin.getReJoinList()) {
            if (!rj.getPl().equals(player.getUniqueId())) continue;
            return rj;
        }
        return null;
    }

    public boolean canReJoin() {
        BedWars.debug("ReJoin canReJoin  check.");
        if (this.arena == null) {
            BedWars.debug("ReJoin canReJoin arena is null " + this.player.toString());
            this.destroy(true);
            return false;
        }
        if (this.arena.getStatus() == GameState.restarting) {
            BedWars.debug("ReJoin canReJoin status is restarting " + this.player.toString());
            this.destroy(true);
            return false;
        }
        if (this.bwt == null) {
            BedWars.debug("ReJoin canReJoin bwt is null " + this.player.toString());
            this.destroy(true);
            return false;
        }
        if (this.bwt.isBedDestroyed()) {
            BedWars.debug("ReJoin canReJoin bed is destroyed " + this.player.toString());
            this.destroy(false);
            return false;
        }
        return true;
    }

    public boolean reJoin(Player player) {
        Sounds.playSound("rejoin-allowed", player);
        player.sendMessage(Language.getMsg(player, Messages.REJOIN_ALLOWED).replace("{arena}", this.getArena().getDisplayName()));
        if (player.getGameMode() != GameMode.SURVIVAL) {
            Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
                player.setGameMode(GameMode.SURVIVAL);
                player.setAllowFlight(true);
                player.setFlying(true);
            }, 20L);
        }
        return this.arena.reJoin(player);
    }

    public void destroy(boolean destroyTeam) {
        BedWars.debug("ReJoin destroy for " + this.player.toString());
        reJoinList.remove(this);
        JsonObject json = new JsonObject();
        json.addProperty("type", "RD");
        json.addProperty("uuid", this.player.toString());
        json.addProperty("server", BedWars.config.getString("bungee-settings.server-id"));
        ArenaSocket.sendMessage(json.toString());
        if (this.bwt != null && destroyTeam && this.bwt.getMembers().isEmpty()) {
            this.bwt.setBedDestroyed(true);
            if (this.bwt != null) {
                for (Player p2 : this.arena.getPlayers()) {
                    p2.sendMessage(Language.getMsg(p2, Messages.TEAM_ELIMINATED_CHAT).replace("{TeamColor}", this.bwt.getColor().chat().toString()).replace("{TeamName}", this.bwt.getDisplayName(Language.getPlayerLanguage(p2))));
                }
                for (Player p2 : this.arena.getSpectators()) {
                    p2.sendMessage(Language.getMsg(p2, Messages.TEAM_ELIMINATED_CHAT).replace("{TeamColor}", this.bwt.getColor().chat().toString()).replace("{TeamName}", this.bwt.getDisplayName(Language.getPlayerLanguage(p2))));
                }
            }
            this.arena.checkWinner();
        }
    }

    public UUID getPlayer() {
        return this.player;
    }

    public ITeam getBwt() {
        return this.bwt;
    }

    public IArena getArena() {
        return this.arena;
    }

    public ReJoinTask getTask() {
        return this.task;
    }

    public UUID getPl() {
        return this.player;
    }

    public List<ShopCache.CachedItem> getPermanentsAndNonDowngradables() {
        return this.permanentsAndNonDowngradables;
    }

    public static List<ReJoin> getReJoinList() {
        return Collections.unmodifiableList(reJoinList);
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof ReJoin)) {
            return false;
        }
        ReJoin reJoin = (ReJoin)o;
        return reJoin.getPl().equals(this.getPl());
    }
}

