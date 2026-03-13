/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package com.andrei1058.bedwars.money.internal;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.configuration.MoneyConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class MoneyPerMinuteTask {
    private final int money = MoneyConfig.money.getInt("money-rewards.per-minute");
    private BukkitTask task;

    public MoneyPerMinuteTask(Arena arena) {
        if (this.money < 1) {
            return;
        }
        this.task = Bukkit.getScheduler().runTaskTimer((Plugin)BedWars.plugin, () -> {
            if (null == arena) {
                this.cancel();
                return;
            }
            for (Player p : arena.getPlayers()) {
                BedWars.getEconomy().giveMoney(p, this.money);
                p.sendMessage(Language.getMsg(p, Messages.MONEY_REWARD_PER_MINUTE).replace("{money}", String.valueOf(this.money)));
            }
        }, 1200L, 1200L);
    }

    public void cancel() {
        if (this.task != null) {
            this.task.cancel();
        }
    }
}

