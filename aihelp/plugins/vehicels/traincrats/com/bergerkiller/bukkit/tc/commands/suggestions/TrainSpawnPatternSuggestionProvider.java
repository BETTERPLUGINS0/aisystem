package com.bergerkiller.bukkit.tc.commands.suggestions;

import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandInput;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.BlockingSuggestionProvider.Strings;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableGroup;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TrainSpawnPatternSuggestionProvider implements Strings<CommandSender> {
   @NonNull
   public Iterable<String> stringSuggestions(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput commandInput) {
      String input = commandInput.lastRemainingToken();
      TrainCarts plugin = (TrainCarts)commandContext.inject(TrainCarts.class).get();
      if (!input.isEmpty() && !Character.isDigit(input.charAt(input.length() - 1))) {
         int nameStart = 0;

         for(int i = input.length() - 1; i >= 0; --i) {
            if (Character.isDigit(input.charAt(i))) {
               nameStart = i + 1;
               break;
            }
         }

         String prefix = input.substring(0, nameStart);
         String typedName = input.substring(nameStart);
         List<String> filtered = (List)plugin.getSavedTrains().getNames().stream().filter((n) -> {
            return n.length() > typedName.length() && n.startsWith(typedName);
         }).map((n) -> {
            return prefix + n;
         }).collect(Collectors.toList());
         if (filtered.isEmpty()) {
            Stream<String> result = Stream.of(SpawnableGroup.VanillaCartType.values()).map(SpawnableGroup.VanillaCartType::toString);
            result = Stream.concat(result, IntStream.range(0, 10).mapToObj(Integer::toString));
            result = result.map((n) -> {
               return input + n;
            });
            return (Iterable)result.collect(Collectors.toList());
         } else {
            return filtered;
         }
      } else {
         Stream<String> result = plugin.getSavedTrains().getNames().stream();
         result = Stream.concat(result, Stream.of(SpawnableGroup.VanillaCartType.values()).map(SpawnableGroup.VanillaCartType::toString));
         result = Stream.concat(result, IntStream.range(0, 10).mapToObj(Integer::toString));
         if (!input.isEmpty()) {
            result = result.map((name) -> {
               return input + name;
            });
         }

         return (Iterable)result.collect(Collectors.toList());
      }
   }
}
