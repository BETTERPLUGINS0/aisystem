package com.bergerkiller.bukkit.tc.commands.parsers;

import com.bergerkiller.bukkit.common.cloud.CloudLocalizedException;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandInput;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParseResult;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParser;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ParserDescriptor;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.BlockingSuggestionProvider.Strings;
import com.bergerkiller.bukkit.tc.Direction;
import com.bergerkiller.bukkit.tc.Localization;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public class DirectionParser implements ArgumentParser<CommandSender, Direction>, Strings<CommandSender> {
   public static ParserDescriptor<CommandSender, Direction> directionParser() {
      return ParserDescriptor.of(new DirectionParser(), Direction.class);
   }

   @NonNull
   public ArgumentParseResult<Direction> parse(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput commandInput) {
      Direction result = Direction.parse(commandInput.peekString());
      if (result == Direction.NONE) {
         return ArgumentParseResult.failure(new CloudLocalizedException(commandContext, Localization.COMMAND_INPUT_DIRECTION_INVALID, new String[]{commandInput.peekString()}));
      } else {
         commandInput.readString();
         return ArgumentParseResult.success(result);
      }
   }

   @NonNull
   public List<String> stringSuggestions(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput commandInput) {
      String input = commandInput.lastRemainingToken();
      List<String> recommended = Arrays.asList("north", "east", "south", "west", "up", "down", "left", "right", "forward", "backward", "continue", "reverse");
      return recommended.stream().anyMatch((s) -> {
         return s.startsWith(input);
      }) ? recommended : (List)Stream.of(Direction.values()).filter((s) -> {
         return s != Direction.NONE;
      }).flatMap((s) -> {
         return Stream.of(s.aliases());
      }).collect(Collectors.toList());
   }
}
