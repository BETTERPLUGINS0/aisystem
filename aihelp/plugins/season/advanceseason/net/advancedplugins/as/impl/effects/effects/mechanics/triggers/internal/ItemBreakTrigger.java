/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerItemBreakEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

public class ItemBreakTrigger
extends AdvancedTrigger {
    public ItemBreakTrigger() {
        super("ITEM_BREAK");
        this.setDescription("Activates when player breaks an item");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onBreak(PlayerItemBreakEvent playerItemBreakEvent) {
        Player player = playerItemBreakEvent.getPlayer();
        if (player.isDead() || !player.isValid()) {
            return;
        }
        ItemStack itemStack = playerItemBreakEvent.getBrokenItem().clone();
        RollItemType rollItemType = RollItemType.getHand(player, itemStack);
        this.executionBuilder().setVictim((LivingEntity)player).setAttacker((LivingEntity)player).setAttackerMain(false).processVariables("%maximum durability%;" + itemStack.getType().getMaxDurability()).setEvent((Event)playerItemBreakEvent).setItemType(rollItemType).setItem(itemStack).buildAndExecute();
    }
}

