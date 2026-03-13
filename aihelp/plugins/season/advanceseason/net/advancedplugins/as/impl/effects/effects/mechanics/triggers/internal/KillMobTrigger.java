/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDeathEvent
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillMobTrigger
extends AdvancedTrigger {
    public KillMobTrigger() {
        super("KILL_MOB");
        this.setDescription("Activates when any Entity kills another Entity");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onMobDeath(EntityDeathEvent entityDeathEvent) {
        Arrow arrow;
        if (entityDeathEvent.getEntity() instanceof Player) {
            return;
        }
        if (entityDeathEvent.getEntity().hasMetadata("ae_ignore")) {
            return;
        }
        EntityDamageEvent entityDamageEvent = entityDeathEvent.getEntity().getLastDamageCause();
        if (!(entityDamageEvent instanceof EntityDamageByEntityEvent)) {
            return;
        }
        Entity entity = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager();
        if (!(entity instanceof LivingEntity)) {
            if (entity instanceof Arrow) {
                arrow = (Arrow)entity;
                if (!(arrow.getShooter() instanceof LivingEntity)) {
                    return;
                }
            } else {
                return;
            }
        }
        arrow = entity instanceof Arrow ? (LivingEntity)((Arrow)entity).getShooter() : (LivingEntity)entity;
        LivingEntity livingEntity = entityDeathEvent.getEntity();
        if (arrow == null) {
            return;
        }
        if (arrow.isDead() || !arrow.isValid()) {
            return;
        }
        for (StackItem stackItem : GetAllRollItems.getMainItems((LivingEntity)arrow)) {
            this.executionBuilder().setAttacker((LivingEntity)arrow).setVictim(livingEntity).processVariables("%exp%;" + entityDeathEvent.getDroppedExp()).setAttackerMain(true).setEvent((Event)entityDeathEvent).setStackItem(stackItem).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

