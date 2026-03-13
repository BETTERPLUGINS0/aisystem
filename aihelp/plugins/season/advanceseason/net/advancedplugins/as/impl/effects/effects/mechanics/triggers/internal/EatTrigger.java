/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerItemConsumeEvent
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
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class EatTrigger
extends AdvancedTrigger {
    public EatTrigger() {
        super("EAT");
        this.setDescription("Activates when player eats");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onConsume(PlayerItemConsumeEvent playerItemConsumeEvent) {
        Player player = playerItemConsumeEvent.getPlayer();
        if (player.isDead() || !player.isValid()) {
            return;
        }
        for (StackItem stackItem : GetAllRollItems.getMainItems((LivingEntity)player)) {
            this.executionBuilder().setAttacker((LivingEntity)player).setAttackerMain(true).processVariables("%food type%;" + ASManager.getMaterial(playerItemConsumeEvent.getItem())).setEvent((Event)playerItemConsumeEvent).setStackItem(stackItem).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

