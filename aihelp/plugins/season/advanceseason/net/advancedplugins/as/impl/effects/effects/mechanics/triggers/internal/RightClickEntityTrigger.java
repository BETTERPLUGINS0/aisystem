/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerInteractAtEntityEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class RightClickEntityTrigger
extends AdvancedTrigger {
    public RightClickEntityTrigger() {
        super("RIGHT_CLICK_ENTITY");
        this.setDescription("Activates when player right clicks an alive entity.");
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerInteract(@NotNull PlayerInteractAtEntityEvent playerInteractAtEntityEvent) {
        if (playerInteractAtEntityEvent.isCancelled()) {
            return;
        }
        if (!ASManager.getInstance().getConfig().getBoolean("settings.right-click-triggers-on-off-hand", false) && playerInteractAtEntityEvent.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        if (!(playerInteractAtEntityEvent.getRightClicked() instanceof LivingEntity)) {
            return;
        }
        Player player = playerInteractAtEntityEvent.getPlayer();
        LivingEntity livingEntity = (LivingEntity)playerInteractAtEntityEvent.getRightClicked();
        for (StackItem stackItem : GetAllRollItems.getMainItems((LivingEntity)player)) {
            this.executionBuilder().setAttacker((LivingEntity)player).setVictim(livingEntity).setStackItem(stackItem).setAttackerMain(true).setEvent((Event)playerInteractAtEntityEvent).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

