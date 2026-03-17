package com.bergerkiller.bukkit.tc.commands.suggestions;

import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandInput;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.BlockingSuggestionProvider.Strings;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorHandler;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorHandlerConditionOption;
import java.util.Collections;
import java.util.stream.Collectors;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TrainListFilterSuggestionProvider implements Strings<CommandSender> {
   @NonNull
   public Iterable<String> stringSuggestions(@NonNull CommandContext<CommandSender> context, @NonNull CommandInput input) {
      CommandSender sender = (CommandSender)context.sender();
      if (input.remainingInput().startsWith("@train[")) {
         TrainCarts plugin = (TrainCarts)context.inject(TrainCarts.class).get();
         SelectorHandler handler = plugin.getSelectorHandlerRegistry().find("train");
         return (Iterable)(handler == null ? Collections.singletonList("@train[]") : (Iterable)handler.options(sender, "train", Collections.emptyList()).stream().map(SelectorHandlerConditionOption::name).map((s) -> {
            return "@train[" + s + "=";
         }).collect(Collectors.toList()));
      } else {
         return Collections.singletonList("@train[");
      }
   }
}
