/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.external;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import net.advancedplugins.seasons.event.TemperatureEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;

public class TemperatureEventTrigger
extends AdvancedTrigger {
    public TemperatureEventTrigger() {
        super("TEMPERATURE_EVENT");
    }

    public void trigger(Player player, String string, Event event) {
        for (StackItem stackItem : GetAllRollItems.getMainItems((LivingEntity)player)) {
            this.executionBuilder().setAttacker((LivingEntity)player).setAttackerMain(true).setEvent(event).setStackItem(stackItem).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).processVariables("%event_name%;" + string).buildAndExecute();
        }
    }

    @EventHandler
    public void onTemp(TemperatureEvent temperatureEvent) {
        this.trigger(temperatureEvent.getPlayer(), temperatureEvent.eventName(), (Event)temperatureEvent);
    }
}

