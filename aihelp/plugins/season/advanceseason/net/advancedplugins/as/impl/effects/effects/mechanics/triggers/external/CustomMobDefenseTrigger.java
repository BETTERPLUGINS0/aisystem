/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.advancedplugins.mobs.impl.customMobs.events.CustomEntityPunchEvent
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.external;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import net.advancedplugins.mobs.impl.customMobs.events.CustomEntityPunchEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;

public class CustomMobDefenseTrigger
extends AdvancedTrigger {
    public CustomMobDefenseTrigger() {
        super("CUSTOM_MOB_DEFENSE");
        this.setDescription("Activates when custom mob gets hit by a player");
    }

    @EventHandler
    public void onHit(CustomEntityPunchEvent customEntityPunchEvent) {
        if (customEntityPunchEvent.getCustomEntity().getEntityMeta().getBaseEntity().hasMetadata("ae_ignore")) {
            return;
        }
        if (!(customEntityPunchEvent.getAttacker() instanceof Player)) {
            return;
        }
        LivingEntity livingEntity = customEntityPunchEvent.getCustomEntity().getEntityMeta().getBaseEntity();
        Player player = (Player)customEntityPunchEvent.getAttacker();
        if (livingEntity.getHealth() - customEntityPunchEvent.getFinalDamage() <= 0.0) {
            return;
        }
        this.resetCombo(livingEntity.getUniqueId());
        if (customEntityPunchEvent.getCause().name().equalsIgnoreCase("CUSTOM") && livingEntity.hasMetadata("mcmmo_rupture")) {
            return;
        }
        if (livingEntity instanceof Player && ((Player)livingEntity).isBlocking() && customEntityPunchEvent.getFinalDamage() <= 0.0) {
            return;
        }
        for (StackItem stackItem : GetAllRollItems.getMainItems((LivingEntity)player)) {
            this.executionBuilder().setAttacker((LivingEntity)player).setVictim(livingEntity).setAttackerMain(false).setEvent((Event)customEntityPunchEvent).setStackItem(stackItem).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

