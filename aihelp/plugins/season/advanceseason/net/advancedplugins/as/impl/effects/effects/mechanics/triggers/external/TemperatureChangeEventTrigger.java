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
import net.advancedplugins.seasons.event.TemperatureChangeEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;

public class TemperatureChangeEventTrigger
extends AdvancedTrigger {
    public TemperatureChangeEventTrigger() {
        super("TEMPERATURE_CHANGE");
    }

    public void trigger(Player player, int n, int n2, int n3, Event event) {
        for (StackItem stackItem : GetAllRollItems.getMainItems((LivingEntity)player)) {
            this.executionBuilder().setAttacker((LivingEntity)player).setAttackerMain(true).setEvent(event).setStackItem(stackItem).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).processVariables("%old_temp%;" + n, "%delta_temp%;" + n2, "%current_temp%;" + n3).buildAndExecute();
        }
    }

    @EventHandler
    public void onTempChange(TemperatureChangeEvent temperatureChangeEvent) {
        this.trigger(temperatureChangeEvent.getPlayer(), temperatureChangeEvent.oldTemp(), Math.abs(temperatureChangeEvent.oldTemp() - temperatureChangeEvent.newTemp()), temperatureChangeEvent.newTemp(), (Event)temperatureChangeEvent);
    }
}

