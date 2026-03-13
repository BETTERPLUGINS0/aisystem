/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.InventoryHolder
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;

public class RightClickTrigger
extends AdvancedTrigger {
    public RightClickTrigger() {
        super("RIGHT_CLICK");
        this.setDescription("Activates when player right clicks anything.");
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_AIR && playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK && playerInteractEvent.isCancelled()) {
            return;
        }
        if (!ASManager.getInstance().getConfig().getBoolean("settings.right-click-triggers-on-off-hand", false) && playerInteractEvent.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        Block block = playerInteractEvent.getClickedBlock();
        if (block != null && block.getState() instanceof InventoryHolder) {
            return;
        }
        for (StackItem stackItem : GetAllRollItems.getMainItems((LivingEntity)player)) {
            this.executionBuilder().setAttacker((LivingEntity)player).setAttackerMain(true).setEvent((Event)playerInteractEvent).setStackItem(stackItem).processVariables("%block type%;" + ASManager.getBlockMaterial(block)).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).setBlock(block).buildAndExecute();
        }
    }
}

