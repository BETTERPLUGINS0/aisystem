/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Trident
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.persistence.PersistentDataHolder
 *  org.bukkit.projectiles.ProjectileSource
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import net.advancedplugins.as.impl.effects.effects.settings.SettingValues;
import net.advancedplugins.as.impl.utils.pdc.PDCHandler;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.projectiles.ProjectileSource;

public class ArrowHitTrigger
extends AdvancedTrigger {
    public ArrowHitTrigger() {
        super("ARROW_HIT");
        this.setDescription("Activates when arrow hits an entity");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onHit(ProjectileHitEvent projectileHitEvent) {
        ProjectileSource projectileSource = projectileHitEvent.getEntity().getShooter();
        if (!(projectileSource instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)projectileSource;
        LivingEntity livingEntity2 = projectileSource = projectileHitEvent.getHitEntity() instanceof LivingEntity ? (LivingEntity)projectileHitEvent.getHitEntity() : null;
        if (projectileSource == null) {
            return;
        }
        if (SettingValues.isBowFullPower() && !PDCHandler.getBoolean((PersistentDataHolder)projectileHitEvent.getEntity(), "projectile-max-force") && !(projectileHitEvent.getEntity() instanceof Trident)) {
            return;
        }
        Location location = projectileSource != null ? projectileSource.getLocation() : (projectileHitEvent.getHitBlock() != null ? projectileHitEvent.getHitBlock().getLocation() : projectileHitEvent.getEntity().getLocation());
        for (StackItem stackItem : GetAllRollItems.getMainItems(livingEntity)) {
            this.executionBuilder().setAttacker(livingEntity).processVariables("%hit location%;" + (location.getX() + 0.5) + "|" + (location.getY() + 0.5) + "|" + (location.getZ() + 0.5), "%hit location x%;" + (location.getX() + 0.5), "%hit location y%;" + (location.getY() + 0.5), "%hit location z%;" + (location.getZ() + 0.5), "%block type%;" + (projectileHitEvent.getHitBlock() != null ? projectileHitEvent.getHitBlock().getType().name() : "null")).setVictim((LivingEntity)projectileSource).setAttackerMain(true).setStackItem(stackItem).setEvent((Event)projectileHitEvent).setBlock(location.getBlock()).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

