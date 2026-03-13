/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.destroystokyo.paper.event.player.PlayerJumpEvent
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class JumpTrigger
extends AdvancedTrigger {
    public JumpTrigger() {
        super("JUMP");
        this.setDescription("Activates when player jump");
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onJump(PlayerJumpEvent playerJumpEvent) {
        if (playerJumpEvent.getPlayer().isDead() || !playerJumpEvent.getPlayer().isValid()) {
            return;
        }
        for (StackItem stackItem : GetAllRollItems.getMainItems((LivingEntity)playerJumpEvent.getPlayer())) {
            this.executionBuilder().setAttacker((LivingEntity)playerJumpEvent.getPlayer()).setAttackerMain(true).setEvent((Event)playerJumpEvent).setStackItem(stackItem).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

