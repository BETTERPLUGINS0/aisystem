package com.bergerkiller.bukkit.tc.commands.parsers;

import com.bergerkiller.bukkit.common.cloud.CloudLocalizedException;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandInput;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParseResult;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParser;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.BlockingSuggestionProvider.Strings;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.mountiplex.MountiplexUtil;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public class AccelerationParser implements ArgumentParser<CommandSender, Double>, Strings<CommandSender> {
   public static final String NAME = "acceleration";
   private final boolean _greedy;

   public AccelerationParser(boolean greedy) {
      this._greedy = greedy;
   }

   @NonNull
   public ArgumentParseResult<Double> parse(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput commandInput) {
      String input = this._greedy ? commandInput.remainingInput() : commandInput.peekString();
      double result;
      if (input.equalsIgnoreCase("nan")) {
         result = Double.NaN;
      } else {
         result = Util.parseAcceleration(input, Double.NaN);
         if (Double.isNaN(result)) {
            return ArgumentParseResult.failure(new CloudLocalizedException(commandContext, Localization.COMMAND_INPUT_ACCELERATION_INVALID, new String[]{input}));
         }
      }

      if (this._greedy) {
         commandInput.cursor(commandInput.length());
      } else {
         commandInput.readString();
      }

      return ArgumentParseResult.success(result);
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput commandInput) {
      String input = commandInput.lastRemainingToken();
      if (input.isEmpty()) {
         return (Iterable)Stream.concat(MountiplexUtil.toStream("-"), IntStream.range(0, 10).mapToObj(Integer::toString)).collect(Collectors.toList());
      } else {
         char lastChar = input.charAt(input.length() - 1);
         if (lastChar != '-' && lastChar != '.' && lastChar != ',') {
            if (Character.isDigit(lastChar)) {
               Stream<String> suggestions = unitStream();
               if (!input.contains(".") && !input.contains(",")) {
                  suggestions = Stream.concat(suggestions, MountiplexUtil.toStream("."));
               }

               suggestions = Stream.concat(suggestions, IntStream.range(0, 10).mapToObj(Integer::toString));
               return (Iterable)suggestions.map((s) -> {
                  return input + s;
               }).collect(Collectors.toList());
            } else {
               String unitPrefix = getUnitPrefix(input);
               String value = input.substring(0, input.length() - unitPrefix.length());
               return (Iterable)unitStream().filter((u) -> {
                  return u.startsWith(unitPrefix);
               }).map((u) -> {
                  return value + u;
               }).collect(Collectors.toList());
            }
         } else {
            return (Iterable)IntStream.range(0, 10).mapToObj(Integer::toString).map((s) -> {
               return input + s;
            }).collect(Collectors.toList());
         }
      }
   }

   private static String getUnitPrefix(String input) {
      for(int i = 0; i < input.length(); ++i) {
         char c = input.charAt(i);
         if (c != '-' && c != '.' && c != ',' && c != ' ' && !Character.isDigit(c)) {
            return input.substring(i);
         }
      }

      return "";
   }

   private static Stream<String> unitStream() {
      return Stream.of("G", "m/s/s", "km/h/s", "mi/h/s", "mph/s", "ft/s/s");
   }
}
