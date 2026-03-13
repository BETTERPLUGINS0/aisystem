/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerFishEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class CatchFishTrigger
extends AdvancedTrigger {
    public CatchFishTrigger() {
        super("CATCH_FISH");
        this.setDescription("Activates when player catches a fish");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerFish(PlayerFishEvent playerFishEvent) {
        if (!playerFishEvent.getState().name().startsWith("CAUGHT")) {
            return;
        }
        if (!(playerFishEvent.getCaught() instanceof Item)) {
            return;
        }
        Player player = playerFishEvent.getPlayer();
        if (player.isDead() || !player.isValid()) {
            return;
        }
        if (playerFishEvent.getCaught() instanceof Item) {
            if (playerFishEvent.getCaught().getTicksLived() > 0) {
                return;
            }
            if (!(playerFishEvent.getCaught() instanceof Item)) {
                return;
            }
        }
        for (StackItem stackItem : GetAllRollItems.getItemsInHands((LivingEntity)player)) {
            if (stackItem.i == null || stackItem.i.getType() != Material.FISHING_ROD) continue;
            this.executionBuilder().setAttacker((LivingEntity)playerFishEvent.getPlayer()).setAttackerMain(true).setVictim(playerFishEvent.getCaught() instanceof LivingEntity ? (LivingEntity)playerFishEvent.getCaught() : null).setEvent((Event)playerFishEvent).setStackItem(stackItem).addDrops(playerFishEvent.getHook().getLocation(), playerFishEvent.getCaught() instanceof Item ? ((Item)playerFishEvent.getCaught()).getItemStack() : new ItemStack(Material.AIR)).processVariables("%exp%;" + playerFishEvent.getExpToDrop(), "%caught%;" + String.valueOf(playerFishEvent.getCaught() instanceof Item ? ((Item)playerFishEvent.getCaught()).getItemStack().getType() : (playerFishEvent.getCaught() != null ? playerFishEvent.getCaught().getType() : "null"))).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).setBlock(playerFishEvent.getCaught().getLocation().getBlock()).buildAndExecute();
        }
    }
}

