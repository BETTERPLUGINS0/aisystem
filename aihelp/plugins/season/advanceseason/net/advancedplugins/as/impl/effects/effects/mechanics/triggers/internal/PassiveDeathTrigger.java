/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class PassiveDeathTrigger
extends AdvancedTrigger {
    public PassiveDeathTrigger() {
        super("PASSIVE_DEATH");
        this.setComboEnabled(true);
        this.setDescription("Activates when Player dies due to non-combat related reason");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPassiveDeath(EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getCause().name().contains("ATTACK") || entityDamageEvent.getCause().name().contains("SONIC_BOOM")) {
            return;
        }
        Object object = entityDamageEvent.getEntity();
        if (!(object instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)object;
        if (entityDamageEvent.getCause().name().contains("EXPLOSION") && livingEntity.getKiller() != null) {
            return;
        }
        if (livingEntity.getHealth() - entityDamageEvent.getFinalDamage() > 0.0) {
            return;
        }
        if (entityDamageEvent.getEntity().hasMetadata("ae_ignore")) {
            return;
        }
        if (MinecraftVersion.getVersionNumber() >= 1130) {
            object = livingEntity.getEquipment().getItemInMainHand();
            ItemStack object2 = livingEntity.getEquipment().getItemInOffHand();
            if (object.getType().name().equalsIgnoreCase("TOTEM_OF_UNDYING") || object2.getType().name().equalsIgnoreCase("TOTEM_OF_UNDYING")) {
                return;
            }
        }
        for (StackItem stackItem : GetAllRollItems.get(livingEntity)) {
            this.executionBuilder().setVictim(livingEntity).setAttackerMain(false).setEvent((Event)entityDamageEvent).setStackItem(stackItem).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

