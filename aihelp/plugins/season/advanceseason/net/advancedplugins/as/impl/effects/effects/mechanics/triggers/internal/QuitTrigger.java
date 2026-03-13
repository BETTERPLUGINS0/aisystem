/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitTrigger
extends AdvancedTrigger {
    public QuitTrigger() {
        super("QUIT");
        this.setDescription("Activates when a player leaves");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        for (StackItem stackItem : GetAllRollItems.getMainItems((LivingEntity)player)) {
            this.executionBuilder().setAttacker((LivingEntity)player).setAttackerMain(true).setStackItem(stackItem).setEvent((Event)playerQuitEvent).setBlock(playerQuitEvent.getPlayer().getLocation().getBlock()).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

