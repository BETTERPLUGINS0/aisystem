/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class AttackMobTrigger
extends AdvancedTrigger {
    public AttackMobTrigger() {
        super("ATTACK_MOB");
        this.setDescription("Activates when player/mob hits a mob");
        this.setComboEnabled(true);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void playerHitMob(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (entityDamageByEntityEvent.getEntity() instanceof Player) {
            return;
        }
        if (entityDamageByEntityEvent.getEntity().hasMetadata("ae_ignore")) {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof LivingEntity)) {
            return;
        }
        if (entityDamageByEntityEvent.getEntityType() == EntityType.ARMOR_STAND) {
            return;
        }
        if (entityDamageByEntityEvent.getCause().equals((Object)EntityDamageEvent.DamageCause.THORNS)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entityDamageByEntityEvent.getDamager();
        LivingEntity livingEntity2 = (LivingEntity)entityDamageByEntityEvent.getEntity();
        if (entityDamageByEntityEvent.getCause().name().equalsIgnoreCase("CUSTOM") && livingEntity2.hasMetadata("mcmmo_rupture")) {
            return;
        }
        this.addCombo(livingEntity.getUniqueId());
        for (StackItem stackItem : GetAllRollItems.getMainItems(livingEntity)) {
            if (stackItem.getRollItemType() == RollItemType.OFFHAND) continue;
            this.executionBuilder().setAttacker(livingEntity).setVictim(livingEntity2).setAttackerMain(true).setStackItem(stackItem).setEvent((Event)entityDamageByEntityEvent).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

