package com.bergerkiller.bukkit.tc.properties.api;

import com.bergerkiller.bukkit.tc.commands.selector.SelectorCondition;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface IPropertySelectorCondition {
   boolean matches(CommandSender var1, TrainProperties var2, SelectorCondition var3);
}
