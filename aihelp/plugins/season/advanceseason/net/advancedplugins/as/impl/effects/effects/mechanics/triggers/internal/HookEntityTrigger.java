/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerFishEvent
 *  org.bukkit.event.player.PlayerFishEvent$State
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class HookEntityTrigger
extends AdvancedTrigger {
    public HookEntityTrigger() {
        super("HOOK_ENTITY");
        this.setDescription("Activates when player hooks an entity with fishing rod");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerFish(PlayerFishEvent playerFishEvent) {
        if (!playerFishEvent.getState().equals((Object)PlayerFishEvent.State.CAUGHT_ENTITY)) {
            return;
        }
        Player player = playerFishEvent.getPlayer();
        if (player.isDead() || !player.isValid()) {
            return;
        }
        if (playerFishEvent.getCaught() == null) {
            return;
        }
        if (!(playerFishEvent.getCaught() instanceof LivingEntity)) {
            return;
        }
        if (playerFishEvent.getCaught() instanceof Item) {
            return;
        }
        if (playerFishEvent.getCaught() instanceof ArmorStand) {
            return;
        }
        for (StackItem stackItem : GetAllRollItems.getItemsInHands((LivingEntity)player)) {
            if (stackItem.i == null || stackItem.i.getType() != Material.FISHING_ROD) continue;
            this.executionBuilder().setAttacker((LivingEntity)playerFishEvent.getPlayer()).setAttackerMain(true).setStackItem(stackItem).setVictim((LivingEntity)playerFishEvent.getCaught()).setEvent((Event)playerFishEvent).addDrops(playerFishEvent.getHook().getLocation(), new ItemStack(Material.AIR)).processVariables("%exp%;" + playerFishEvent.getExpToDrop(), "%caught%;" + String.valueOf(playerFishEvent.getCaught() != null ? playerFishEvent.getCaught().getType() : "null")).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

