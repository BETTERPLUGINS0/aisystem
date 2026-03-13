/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Trident
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.effects.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class TridentShootHandler
implements Listener {
    private static final Map<UUID, ItemStack> savedTridents = new HashMap<UUID, ItemStack>();

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onShoot(ProjectileLaunchEvent projectileLaunchEvent) {
        if (!(projectileLaunchEvent.getEntity() instanceof Trident)) {
            return;
        }
        if (!(projectileLaunchEvent.getEntity().getShooter() instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)projectileLaunchEvent.getEntity().getShooter();
        savedTridents.put(livingEntity.getUniqueId(), livingEntity.getEquipment().getItemInHand().clone());
    }

    public static ItemStack getItem(UUID uUID) {
        return savedTridents.get(uUID);
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onLang(ProjectileHitEvent projectileHitEvent) {
        if (projectileHitEvent.getEntity().getType() != EntityType.TRIDENT) {
            return;
        }
        if (!(projectileHitEvent.getEntity().getShooter() instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)projectileHitEvent.getEntity().getShooter();
        SchedulerUtils.runTaskLater(() -> savedTridents.remove(livingEntity.getUniqueId()));
    }
}

