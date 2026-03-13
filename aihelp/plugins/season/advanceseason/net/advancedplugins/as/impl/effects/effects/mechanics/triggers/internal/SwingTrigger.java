/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SwingTrigger
extends AdvancedTrigger {
    public SwingTrigger() {
        super("SWING");
        this.setDescription("Activates when player swings held item.");
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        Location location;
        String string;
        if (playerInteractEvent.getItem() == null) {
            return;
        }
        if (playerInteractEvent.getAction() != Action.LEFT_CLICK_BLOCK && playerInteractEvent.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }
        if (MinecraftVersion.getVersionNumber() >= 190 && playerInteractEvent.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (playerInteractEvent.getClickedBlock() == null) {
            string = "AIR";
            location = player.getLocation();
        } else {
            string = playerInteractEvent.getClickedBlock().getType().name();
            location = playerInteractEvent.getClickedBlock().getLocation().add(0.5, 0.5, 0.5);
        }
        this.executionBuilder().setAttacker((LivingEntity)player).setAttackerMain(true).setBlock(location.getBlock()).processVariables("%block type%;" + string, "%block location%;" + location.getX() + "|" + location.getY() + "|" + location.getZ()).setEvent((Event)playerInteractEvent).setItemType(RollItemType.HAND).setItem(itemStack).buildAndExecute();
    }
}

