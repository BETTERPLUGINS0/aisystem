/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DefenseMobProjectileTrigger
extends AdvancedTrigger {
    public DefenseMobProjectileTrigger() {
        super("DEFENSE_MOB_PROJECTILE");
        this.setDescription("Activates when Player/Mob gets hit by projectile from mob");
        this.setComboEnabled(true);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onProjectileHitEvent(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Entity entity = entityDamageByEntityEvent.getDamager();
        if (!(entity instanceof Projectile)) {
            return;
        }
        Projectile projectile = (Projectile)entity;
        entity = projectile.getShooter();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entity;
        if (entity instanceof Player) {
            return;
        }
        Entity entity2 = entityDamageByEntityEvent.getEntity();
        if (!(entity2 instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity2 = (LivingEntity)entity2;
        if (entityDamageByEntityEvent.getEntity().hasMetadata("ae_ignore")) {
            return;
        }
        if (entityDamageByEntityEvent.getCause().equals((Object)EntityDamageEvent.DamageCause.THORNS)) {
            return;
        }
        if (livingEntity2.getHealth() - entityDamageByEntityEvent.getFinalDamage() <= 0.0) {
            return;
        }
        this.addCombo(livingEntity2.getUniqueId());
        boolean bl = projectile.getLocation().getY() - livingEntity2.getLocation().getY() > 1.45;
        for (StackItem stackItem : GetAllRollItems.getMainItems(livingEntity2)) {
            this.executionBuilder().setAttacker(livingEntity).setVictim(livingEntity2).setStackItem(stackItem).setAttackerMain(false).processVariables("%is headshot%;" + bl, "%projectile type%;" + (projectile instanceof Arrow ? "arrow" : "trident")).setAttackerMain(false).setEvent((Event)entityDamageByEntityEvent).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

