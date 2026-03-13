/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AddWalkSpeedEffect
extends AdvancedEffect {
    private final Map<UUID, Long> speedDuration = new HashMap<UUID, Long>();
    private final Map<UUID, Integer> removalTasks = new HashMap<UUID, Integer>();

    public AddWalkSpeedEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "ADD_WALK_SPEED", "Adds to the players walk speed (from -1 to 1) without applying potion", "%e:<SPEED>:[TICKS]");
        this.addArgument(0, Float.class);
        this.addArgument(1, Long.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (!(livingEntity instanceof Player)) {
            return false;
        }
        if (stringArray.length < 1) {
            return false;
        }
        Player player = (Player)livingEntity;
        float f = Float.parseFloat(stringArray[0]);
        if (executionTask.getBuilder().isPermanent() && stringArray.length == 1) {
            if (executionTask.getBuilder().isRemoved()) {
                player.setWalkSpeed(0.2f);
            } else {
                this.addSpeed(player, f);
            }
        } else {
            if (stringArray.length >= 2) {
                if (executionTask.getBuilder().isPermanent() && executionTask.getBuilder().isRemoved()) {
                    player.setWalkSpeed(0.2f);
                    return true;
                }
                long l = (long)ASManager.parseInt(stringArray[1]) / 20L * 1000L;
                this.speedDuration.put(player.getUniqueId(), System.currentTimeMillis() + l);
                UUID uUID = player.getUniqueId();
                if (this.removalTasks.containsKey(uUID)) {
                    int n = this.removalTasks.remove(uUID);
                    Bukkit.getScheduler().cancelTask(n);
                }
                this.removalTasks.put(uUID, SchedulerUtils.runTaskLater(() -> {
                    this.speedDuration.remove(uUID);
                    player.setWalkSpeed(0.2f);
                    this.removalTasks.remove(uUID);
                }, l / 50L));
                this.addSpeed(player, f);
                return true;
            }
            this.addSpeed(player, f);
            return true;
        }
        return true;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        UUID uUID = player.getUniqueId();
        if (this.speedDuration.containsKey(uUID)) {
            this.speedDuration.remove(uUID);
            player.setWalkSpeed(0.2f);
        }
        if (this.removalTasks.containsKey(uUID)) {
            int n = this.removalTasks.remove(uUID);
            Bukkit.getScheduler().cancelTask(n);
        }
    }

    public void addSpeed(Player player, float f) {
        float f2 = player.getWalkSpeed();
        float f3 = Math.max(-1.0f, Math.min(1.0f, f2 + f));
        player.setWalkSpeed(f3);
    }
}

