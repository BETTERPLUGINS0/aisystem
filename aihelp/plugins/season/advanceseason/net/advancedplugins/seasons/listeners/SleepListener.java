/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.GameRule
 *  org.bukkit.World
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerBedEnterEvent
 *  org.bukkit.event.player.PlayerBedEnterEvent$BedEnterResult
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.advancedplugins.seasons.listeners;

import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.seasons.Core;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SleepListener
implements Listener {
    @EventHandler
    public void onSleep(PlayerBedEnterEvent playerBedEnterEvent) {
        if (playerBedEnterEvent.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) {
            return;
        }
        final World world = playerBedEnterEvent.getPlayer().getWorld();
        if (!this.allPlayersSleeping(world, 1)) {
            return;
        }
        new BukkitRunnable(){

            public void run() {
                if (SleepListener.this.allPlayersSleeping(world, 0)) {
                    Core.getCalendarHandler().getWorldTimeController().sleepWorld(world);
                }
            }
        }.runTaskLater((Plugin)ASManager.getInstance(), 100L);
    }

    private boolean allPlayersSleeping(World world, int n) {
        long l;
        double d;
        Integer n2 = (Integer)world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
        if (n2 == null) {
            n2 = 100;
        }
        return (d = (double)(l = world.getPlayers().stream().filter(LivingEntity::isSleeping).count() + (long)n) / (double)this.getPlayersInWorld(world) * 100.0) >= (double)n2.intValue();
    }

    private int getPlayersInWorld(World world) {
        return (int)world.getPlayers().stream().filter(player -> !player.getWorld().getName().equalsIgnoreCase(world.getName())).count();
    }
}

