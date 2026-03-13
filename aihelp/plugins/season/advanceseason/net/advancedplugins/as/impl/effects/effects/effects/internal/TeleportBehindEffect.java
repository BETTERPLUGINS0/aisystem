/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportBehindEffect
extends AdvancedEffect {
    public TeleportBehindEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "TELEPORT_BEHIND", "Teleport behind other entity", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        LivingEntity livingEntity2 = this.getOtherEntity(livingEntity, executionTask);
        Location location = livingEntity2.getLocation();
        float f = location.getYaw() + 90.0f;
        if (f < 0.0f) {
            f += 360.0f;
        }
        double d = Math.cos(Math.toRadians(f));
        double d2 = Math.sin(Math.toRadians(f));
        Location location2 = new Location(location.getWorld(), location.getX() - d, location.getY(), location.getZ() - d2, location.getYaw(), location.getPitch());
        if (!ASManager.isAir(location2.getBlock())) {
            return true;
        }
        livingEntity.teleport(location2, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        return true;
    }
}

