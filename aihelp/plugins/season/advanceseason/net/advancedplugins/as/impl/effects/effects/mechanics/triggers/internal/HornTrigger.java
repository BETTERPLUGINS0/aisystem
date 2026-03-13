/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HornTrigger
extends AdvancedTrigger {
    public HornTrigger() {
        super("HORN");
        this.setDescription("Activates when horn is blown (used)");
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onGoatHornSound(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.useItemInHand() == Event.Result.DENY) {
            return;
        }
        if (!playerInteractEvent.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK) && !playerInteractEvent.getAction().equals((Object)Action.RIGHT_CLICK_AIR)) {
            return;
        }
        ItemStack itemStack = playerInteractEvent.getItem();
        Player player = playerInteractEvent.getPlayer();
        Material material = Material.valueOf((String)"GOAT_HORN");
        if (itemStack == null || itemStack.getType() != material || player.hasCooldown(material)) {
            return;
        }
        RollItemType rollItemType = RollItemType.getHand(player, itemStack);
        this.executionBuilder().setVictim((LivingEntity)player).setAttacker((LivingEntity)player).setAttackerMain(false).setEvent((Event)playerInteractEvent).setItemType(rollItemType).setItem(itemStack).buildAndExecute();
    }
}

