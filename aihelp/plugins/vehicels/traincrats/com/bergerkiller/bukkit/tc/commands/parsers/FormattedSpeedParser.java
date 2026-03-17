package com.bergerkiller.bukkit.tc.commands.parsers;

import com.bergerkiller.bukkit.common.cloud.CloudLocalizedException;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandInput;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParseResult;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParser;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ParserDescriptor;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.BlockingSuggestionProvider.Strings;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.utils.FormattedSpeed;
import com.bergerkiller.mountiplex.MountiplexUtil;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public class FormattedSpeedParser implements ArgumentParser<CommandSender, FormattedSpeed>, Strings<CommandSender> {
   private final boolean _greedy;

   public FormattedSpeedParser(boolean greedy) {
      this._greedy = greedy;
   }

   public static ParserDescriptor<CommandSender, FormattedSpeed> formattedSpeedParser(boolean greedy) {
      return ParserDescriptor.of(new FormattedSpeedParser(greedy), FormattedSpeed.class);
   }

   @NonNull
   public ArgumentParseResult<FormattedSpeed> parse(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput commandInput) {
      String input = this._greedy ? commandInput.remainingInput() : commandInput.peekString();
      FormattedSpeed result;
      if (input.equalsIgnoreCase("nan")) {
         result = FormattedSpeed.of(Double.NaN);
      } else {
         result = FormattedSpeed.parse(input, (FormattedSpeed)null);
         if (result == null) {
            return ArgumentParseResult.failure(new CloudLocalizedException(commandContext, Localization.COMMAND_INPUT_SPEED_INVALID, new String[]{input}));
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
   public List<String> stringSuggestions(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput commandInput) {
      String input = commandInput.lastRemainingToken();
      if (input.isEmpty()) {
         return (List)Stream.concat(Stream.of("-", "+"), IntStream.range(0, 10).mapToObj(Integer::toString)).collect(Collectors.toList());
      } else {
         char lastChar = input.charAt(input.length() - 1);
         if (lastChar != '-' && lastChar != '.' && lastChar != ',') {
            if (Character.isDigit(lastChar)) {
               Stream<String> suggestions = unitStream();
               if (!input.contains(".") && !input.contains(",")) {
                  suggestions = Stream.concat(suggestions, MountiplexUtil.toStream("."));
               }

               suggestions = Stream.concat(suggestions, IntStream.range(0, 10).mapToObj(Integer::toString));
               return (List)suggestions.map((s) -> {
                  return input + s;
               }).collect(Collectors.toList());
            } else {
               String unitPrefix = getUnitPrefix(input);
               String value = input.substring(0, input.length() - unitPrefix.length());
               return (List)unitStream().filter((u) -> {
                  return u.startsWith(unitPrefix);
               }).map((u) -> {
                  return value + u;
               }).collect(Collectors.toList());
            }
         } else {
            return (List)IntStream.range(0, 10).mapToObj(Integer::toString).map((s) -> {
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
      return Stream.of("m/s", "km/h", "mi/h", "mph", "kmh", "ft/s");
   }
}
