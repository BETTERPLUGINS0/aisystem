/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FlyEffect
extends AdvancedEffect {
    private final Map<UUID, Long> flyDuration = new HashMap<UUID, Long>();

    public FlyEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "FLY", "Toggle player's flight", "%e:[TICKS]");
        this.addArgument(0, Long.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        try {
            return this.parse(executionTask, livingEntity, stringArray);
        } catch (Exception exception) {
            return true;
        }
    }

    private boolean parse(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        Player player = (Player)livingEntity;
        if (!executionTask.getBuilder().isPermanent() || stringArray.length != 0) {
            if (stringArray.length >= 1) {
                if (executionTask.getBuilder().isPermanent() && executionTask.getBuilder().isRemoved()) {
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    return true;
                }
                long l = Long.parseLong(stringArray[0]) / 20L * 1000L;
                if (player.isFlying()) {
                    long l2 = this.flyDuration.getOrDefault(player.getUniqueId(), 0L);
                    if (l2 > System.currentTimeMillis()) {
                        return true;
                    }
                } else {
                    this.flyDuration.put(player.getUniqueId(), System.currentTimeMillis() + l);
                }
                player.setAllowFlight(!player.getAllowFlight());
                player.setFlying(player.getAllowFlight());
                SchedulerUtils.runTaskLater(() -> {
                    this.flyDuration.remove(player.getUniqueId());
                    player.setFlying(false);
                    player.setAllowFlight(false);
                }, l / 50L);
                return true;
            }
            player.setAllowFlight(!player.getAllowFlight());
            player.setFlying(player.getAllowFlight());
            return true;
        }
        player.setAllowFlight(!executionTask.getBuilder().isRemoved());
        player.setFlying(!executionTask.getBuilder().isRemoved());
        return true;
    }
}

