/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityToggleGlideEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;

public class ElytraFlyTrigger
extends AdvancedTrigger {
    public ElytraFlyTrigger() {
        super("ELYTRA_FLY");
        this.setDescription("Activates when entity starts flying");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onFly(EntityToggleGlideEvent entityToggleGlideEvent) {
        if (!entityToggleGlideEvent.isGliding()) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entityToggleGlideEvent.getEntity();
        ItemStack itemStack = livingEntity.getEquipment().getChestplate();
        this.executionBuilder().setAttacker(livingEntity).setAttackerMain(true).setEvent((Event)entityToggleGlideEvent).setItemType(RollItemType.CHESTPLATE).setItem(itemStack).buildAndExecute();
    }
}

