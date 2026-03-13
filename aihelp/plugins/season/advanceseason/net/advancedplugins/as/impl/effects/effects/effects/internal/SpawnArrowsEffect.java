/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.ItemFrame
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.util.Vector
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class SpawnArrowsEffect
extends AdvancedEffect {
    private final Cache<Arrow, Boolean> arrowCache = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.SECONDS).build();

    public SpawnArrowsEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "SPAWN_ARROWS", "Spawn flood of arrows from above", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        return this.executeEffect(executionTask, livingEntity.getLocation().add(0.0, 1.0, 0.0), stringArray);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        Block block = location.getBlock().getRelative(0, 5, 0);
        for (int i = -3; i <= 3; ++i) {
            for (int j = -3; j <= 3; ++j) {
                Block block2 = block.getRelative(i, 0, j);
                Arrow arrow = block2.getWorld().spawnArrow(block2.getLocation(), new Vector(0, -10, 0), 0.2f, 0.0f);
                this.arrowCache.put(arrow, true);
            }
        }
        return true;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.LOW)
    public void stopArrowsBreakBlock(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Entity entity = entityDamageByEntityEvent.getDamager();
        if (!(entity instanceof Arrow)) {
            return;
        }
        Arrow arrow = (Arrow)entity;
        if (!(entityDamageByEntityEvent.getEntity() instanceof ArmorStand) && !(entityDamageByEntityEvent.getEntity() instanceof ItemFrame)) {
            return;
        }
        if (this.arrowCache.getIfPresent(arrow) != null) {
            this.arrowCache.invalidate(arrow);
            entityDamageByEntityEvent.setCancelled(true);
        }
    }
}

