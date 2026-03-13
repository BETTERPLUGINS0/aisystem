/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
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
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandTrigger
extends AdvancedTrigger {
    public CommandTrigger() {
        super("COMMAND");
        this.setDescription("Activates when command is used");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onCommand(PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        Player player = playerCommandPreprocessEvent.getPlayer();
        for (StackItem stackItem : GetAllRollItems.getMainItems((LivingEntity)player)) {
            this.executionBuilder().setVictim((LivingEntity)player).setAttackerMain(false).setEvent((Event)playerCommandPreprocessEvent).setStackItem(stackItem).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

