/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityShootBowEvent
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;

public class BowFireTrigger
extends AdvancedTrigger {
    public BowFireTrigger() {
        super("BOW_FIRE");
        this.setDescription("Activates when bow is fired");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onShoot(EntityShootBowEvent entityShootBowEvent) {
        LivingEntity livingEntity = entityShootBowEvent.getEntity();
        this.executionBuilder().setAttacker(livingEntity).setAttackerMain(true).setEvent((Event)entityShootBowEvent).setItemType(RollItemType.getFromEquipment(entityShootBowEvent.getHand())).setItem(entityShootBowEvent.getBow()).processVariables("%force%;" + entityShootBowEvent.getForce()).buildAndExecute();
    }
}

