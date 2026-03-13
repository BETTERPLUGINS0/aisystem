/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinTrigger
extends AdvancedTrigger {
    public JoinTrigger() {
        super("JOIN");
        this.setDescription("Activates when player joins the server");
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        if (playerJoinEvent.getPlayer().isDead() || !playerJoinEvent.getPlayer().isValid()) {
            return;
        }
        for (StackItem stackItem : GetAllRollItems.getAllItems((LivingEntity)playerJoinEvent.getPlayer())) {
            this.executionBuilder().setAttacker((LivingEntity)playerJoinEvent.getPlayer()).processVariables("%first join%;" + !playerJoinEvent.getPlayer().hasPlayedBefore()).setAttackerMain(true).setEvent((Event)playerJoinEvent).setStackItem(stackItem).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

