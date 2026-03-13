/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageModifier
 *  org.bukkit.inventory.EntityEquipment
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;

public class ShieldBlockTrigger
extends AdvancedTrigger {
    public ShieldBlockTrigger() {
        super("SHIELD_BLOCK");
        this.setDescription("Activates when player blocks with a shield");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void playerHitPlayer(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (entityDamageByEntityEvent.getEntity().hasMetadata("ae_ignore")) {
            return;
        }
        if (entityDamageByEntityEvent.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) == 0.0) {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entityDamageByEntityEvent.getDamager();
        LivingEntity livingEntity2 = (LivingEntity)entityDamageByEntityEvent.getEntity();
        if (livingEntity2.getHealth() - entityDamageByEntityEvent.getFinalDamage() <= 0.0) {
            return;
        }
        EntityEquipment entityEquipment = livingEntity2.getEquipment();
        if (entityEquipment == null) {
            return;
        }
        if (entityEquipment.getItemInMainHand().getType().name().contains("SHIELD") && entityEquipment.getItemInOffHand().getType().name().contains("SHIELD")) {
            StackItem stackItem = new StackItem(entityEquipment.getItemInMainHand(), RollItemType.HAND);
            this.executionBuilder().setAttacker(livingEntity).setVictim(livingEntity2).setStackItem(stackItem).setAttackerMain(false).setEvent((Event)entityDamageByEntityEvent).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
            return;
        }
        for (StackItem stackItem : GetAllRollItems.getMainItems(livingEntity2)) {
            this.executionBuilder().setAttacker(livingEntity).setVictim(livingEntity2).setStackItem(stackItem).setAttackerMain(false).setEvent((Event)entityDamageByEntityEvent).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

