/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package com.andrei1058.bedwars.levels.internal;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.events.player.PlayerXpGainEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.configuration.LevelsConfig;
import com.andrei1058.bedwars.levels.internal.PlayerLevel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class PerMinuteTask {
    private final int xp = LevelsConfig.levels.getInt("xp-rewards.per-minute");
    private BukkitTask task;

    public PerMinuteTask(Arena arena) {
        if (this.xp < 1) {
            return;
        }
        this.task = Bukkit.getScheduler().runTaskTimer((Plugin)BedWars.plugin, () -> {
            for (Player p : arena.getPlayers()) {
                PlayerLevel.getLevelByPlayer(p.getUniqueId()).addXp(this.xp, PlayerXpGainEvent.XpSource.PER_MINUTE);
                p.sendMessage(Language.getMsg(p, Messages.XP_REWARD_PER_MINUTE).replace("{xp}", String.valueOf(this.xp)));
            }
        }, 1200L, 1200L);
    }

    public void cancel() {
        if (this.task != null) {
            this.task.cancel();
        }
    }
}

