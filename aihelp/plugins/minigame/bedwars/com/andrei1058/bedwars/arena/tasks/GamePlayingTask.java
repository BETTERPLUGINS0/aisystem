/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitTask
 */
package com.andrei1058.bedwars.arena.tasks;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.player.PlayerInvisibilityPotionEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.tasks.PlayingTask;
import com.andrei1058.bedwars.arena.Arena;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class GamePlayingTask
implements Runnable,
PlayingTask {
    private Arena arena;
    private BukkitTask task;
    private int beds_destroy_countdown;
    private int dragon_spawn_countdown;
    private int game_end_countdown;

    public GamePlayingTask(Arena arena) {
        this.arena = arena;
        this.beds_destroy_countdown = BedWars.config.getInt("countdowns.next-event-beds-destroy");
        this.dragon_spawn_countdown = BedWars.config.getInt("countdowns.next-event-dragon-spawn");
        this.game_end_countdown = BedWars.config.getInt("countdowns.next-event-game-end");
        this.task = Bukkit.getScheduler().runTaskTimer((Plugin)BedWars.plugin, (Runnable)this, 0L, 20L);
    }

    @Override
    public Arena getArena() {
        return this.arena;
    }

    @Override
    public BukkitTask getBukkitTask() {
        return this.task;
    }

    @Override
    public int getTask() {
        return this.task.getTaskId();
    }

    @Override
    public int getBedsDestroyCountdown() {
        return this.beds_destroy_countdown;
    }

    @Override
    public int getDragonSpawnCountdown() {
        return this.dragon_spawn_countdown;
    }

    @Override
    public int getGameEndCountdown() {
        return this.game_end_countdown;
    }

    @Override
    public void run() {
        switch (this.getArena().getNextEvent()) {
            case EMERALD_GENERATOR_TIER_II: 
            case EMERALD_GENERATOR_TIER_III: 
            case DIAMOND_GENERATOR_TIER_II: 
            case DIAMOND_GENERATOR_TIER_III: {
                if (this.getArena().upgradeDiamondsCount > 0) {
                    --this.getArena().upgradeDiamondsCount;
                    if (this.getArena().upgradeDiamondsCount == 0) {
                        this.getArena().updateNextEvent();
                    }
                }
                if (this.getArena().upgradeEmeraldsCount <= 0) break;
                --this.getArena().upgradeEmeraldsCount;
                if (this.getArena().upgradeEmeraldsCount != 0) break;
                this.getArena().updateNextEvent();
                break;
            }
            case BEDS_DESTROY: {
                --this.beds_destroy_countdown;
                if (this.getBedsDestroyCountdown() != 0) break;
                for (Player player : this.getArena().getPlayers()) {
                    BedWars.nms.sendTitle(player, Language.getMsg(player, Messages.NEXT_EVENT_TITLE_ANNOUNCE_BEDS_DESTROYED), Language.getMsg(player, Messages.NEXT_EVENT_SUBTITLE_ANNOUNCE_BEDS_DESTROYED), 0, 40, 10);
                    player.sendMessage(Language.getMsg(player, Messages.NEXT_EVENT_CHAT_ANNOUNCE_BEDS_DESTROYED));
                }
                for (Player player : this.getArena().getSpectators()) {
                    BedWars.nms.sendTitle(player, Language.getMsg(player, Messages.NEXT_EVENT_TITLE_ANNOUNCE_BEDS_DESTROYED), Language.getMsg(player, Messages.NEXT_EVENT_SUBTITLE_ANNOUNCE_BEDS_DESTROYED), 0, 40, 10);
                    player.sendMessage(Language.getMsg(player, Messages.NEXT_EVENT_CHAT_ANNOUNCE_BEDS_DESTROYED));
                }
                for (ITeam iTeam : this.getArena().getTeams()) {
                    iTeam.setBedDestroyed(true);
                }
                this.getArena().updateNextEvent();
                break;
            }
            case ENDER_DRAGON: {
                --this.dragon_spawn_countdown;
                if (this.getDragonSpawnCountdown() != 0) break;
                for (Player player : this.getArena().getPlayers()) {
                    BedWars.nms.sendTitle(player, Language.getMsg(player, Messages.NEXT_EVENT_TITLE_ANNOUNCE_SUDDEN_DEATH), Language.getMsg(player, Messages.NEXT_EVENT_SUBTITLE_ANNOUNCE_SUDDEN_DEATH), 0, 40, 10);
                    for (ITeam iTeam : this.getArena().getTeams()) {
                        if (iTeam.getMembers().isEmpty()) continue;
                        player.sendMessage(Language.getMsg(player, Messages.NEXT_EVENT_CHAT_ANNOUNCE_SUDDEN_DEATH).replace("{TeamDragons}", String.valueOf(iTeam.getDragons())).replace("{TeamColor}", iTeam.getColor().chat().toString()).replace("{TeamName}", iTeam.getDisplayName(Language.getPlayerLanguage(player))));
                    }
                }
                for (Player player : this.getArena().getSpectators()) {
                    BedWars.nms.sendTitle(player, Language.getMsg(player, Messages.NEXT_EVENT_TITLE_ANNOUNCE_SUDDEN_DEATH), Language.getMsg(player, Messages.NEXT_EVENT_SUBTITLE_ANNOUNCE_SUDDEN_DEATH), 0, 40, 10);
                    for (ITeam iTeam : this.getArena().getTeams()) {
                        if (iTeam.getMembers().isEmpty()) continue;
                        player.sendMessage(Language.getMsg(player, Messages.NEXT_EVENT_CHAT_ANNOUNCE_SUDDEN_DEATH).replace("{TeamDragons}", String.valueOf(iTeam.getDragons())).replace("{TeamColor}", iTeam.getColor().chat().toString()).replace("{TeamName}", iTeam.getDisplayName(Language.getPlayerLanguage(player))));
                    }
                }
                this.getArena().updateNextEvent();
                for (ITeam iTeam : this.arena.getTeams()) {
                    for (IGenerator iGenerator : iTeam.getGenerators()) {
                        Location l = iGenerator.getLocation();
                        for (int y = 0; y < 20; ++y) {
                            l.clone().subtract(0.0, (double)y, 0.0).getBlock().setType(Material.AIR);
                        }
                    }
                }
                for (ITeam iTeam : this.getArena().getTeams()) {
                    if (iTeam.getMembers().isEmpty()) continue;
                    for (int x = 0; x < iTeam.getDragons(); ++x) {
                        BedWars.nms.spawnDragon(this.getArena().getConfig().getArenaLoc("waiting.Loc").add(0.0, 10.0, 0.0), iTeam);
                    }
                }
                break;
            }
            case GAME_END: {
                --this.game_end_countdown;
                if (this.getGameEndCountdown() != 0) break;
                this.getArena().checkWinner();
                this.getArena().changeStatus(GameState.restarting);
            }
        }
        int distance = 0;
        for (ITeam t : this.getArena().getTeams()) {
            if (t.getSize() > 1) {
                for (Player p : t.getMembers()) {
                    for (Player p2 : t.getMembers()) {
                        if (p2 == p) continue;
                        if (distance == 0) {
                            distance = (int)p.getLocation().distance(p2.getLocation());
                            continue;
                        }
                        if ((int)p.getLocation().distance(p2.getLocation()) >= distance) continue;
                        distance = (int)p.getLocation().distance(p2.getLocation());
                    }
                    BedWars.nms.playAction(p, Language.getMsg(p, Messages.FORMATTING_ACTION_BAR_TRACKING).replace("{team}", String.valueOf(t.getColor().chat()) + t.getDisplayName(Language.getPlayerLanguage(p))).replace("{distance}", t.getColor().chat().toString() + distance).replace("&", "\u00a7"));
                }
            }
            for (IGenerator o : t.getGenerators()) {
                o.spawn();
            }
        }
        boolean bl = false;
        for (Player player : this.getArena().getPlayers()) {
            int n;
            if (Arena.afkCheck.get(player.getUniqueId()) == null) {
                Arena.afkCheck.put(player.getUniqueId(), n);
                continue;
            }
            n = Arena.afkCheck.get(player.getUniqueId());
            Arena.afkCheck.replace(player.getUniqueId(), ++n);
            if (n != 45) continue;
            BedWars.getAPI().getAFKUtil().setPlayerAFK(player, true);
        }
        if (!this.getArena().getRespawnSessions().isEmpty()) {
            for (Map.Entry entry : this.getArena().getRespawnSessions().entrySet()) {
                if ((Integer)entry.getValue() <= 0) {
                    IArena a = Arena.getArenaByPlayer((Player)entry.getKey());
                    if (a == null) {
                        this.getArena().getRespawnSessions().remove(entry.getKey());
                        continue;
                    }
                    ITeam t = a.getTeam((Player)entry.getKey());
                    if (t == null) {
                        a.addSpectator((Player)entry.getKey(), true, null);
                        continue;
                    }
                    t.respawnMember((Player)entry.getKey());
                    ((Player)entry.getKey()).setAllowFlight(false);
                    ((Player)entry.getKey()).setFlying(false);
                    continue;
                }
                BedWars.nms.sendTitle((Player)entry.getKey(), Language.getMsg((Player)entry.getKey(), Messages.PLAYER_DIE_RESPAWN_TITLE).replace("{time}", String.valueOf(entry.getValue())), Language.getMsg((Player)entry.getKey(), Messages.PLAYER_DIE_RESPAWN_SUBTITLE).replace("{time}", String.valueOf(entry.getValue())), 0, 30, 10);
                ((Player)entry.getKey()).sendMessage(Language.getMsg((Player)entry.getKey(), Messages.PLAYER_DIE_RESPAWN_CHAT).replace("{time}", String.valueOf(entry.getValue())));
                this.getArena().getRespawnSessions().replace((Player)entry.getKey(), (Integer)entry.getValue() - 1);
            }
        }
        if (!this.getArena().getShowTime().isEmpty()) {
            for (Map.Entry entry : this.getArena().getShowTime().entrySet()) {
                if ((Integer)entry.getValue() <= 0) {
                    for (Player p : ((Player)entry.getKey()).getWorld().getPlayers()) {
                        BedWars.nms.showArmor((Player)entry.getKey(), p);
                    }
                    ((Player)entry.getKey()).removePotionEffect(PotionEffectType.INVISIBILITY);
                    this.getArena().getShowTime().remove(entry.getKey());
                    Bukkit.getPluginManager().callEvent((Event)new PlayerInvisibilityPotionEvent(PlayerInvisibilityPotionEvent.Type.REMOVED, this.getArena().getTeam((Player)entry.getKey()), (Player)entry.getKey(), this.getArena()));
                    continue;
                }
                this.getArena().getShowTime().replace((Player)entry.getKey(), (Integer)entry.getValue() - 1);
            }
        }
        for (IGenerator iGenerator : this.getArena().getOreGenerators()) {
            iGenerator.spawn();
        }
    }

    @Override
    public void cancel() {
        this.task.cancel();
    }
}

