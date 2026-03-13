/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

public class StruckTrigger
extends AdvancedTrigger {
    public StruckTrigger() {
        super("STRUCK");
        this.setDescription("Activates when entity gets struck by lightning");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onDamage(EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.LIGHTNING) {
            return;
        }
        if (!(entityDamageEvent.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (entityDamageEvent.getEntity().hasMetadata("ae_ignore")) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entityDamageEvent.getEntity();
        for (StackItem stackItem : GetAllRollItems.getMainItems(livingEntity)) {
            this.executionBuilder().setVictim(livingEntity).setAttackerMain(false).setEvent((Event)entityDamageEvent).setStackItem(stackItem).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

