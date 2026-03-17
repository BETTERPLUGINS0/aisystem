package com.bergerkiller.bukkit.tc.commands.parsers;

import com.bergerkiller.bukkit.common.cloud.CloudLocalizedException;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandInput;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParseResult;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParser;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ParserDescriptor;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.BlockingSuggestionProvider.Strings;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.properties.standard.type.ChunkLoadOptions;
import java.util.Optional;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ChunkLoadOptionsModeParser implements ArgumentParser<CommandSender, ChunkLoadOptions.Mode>, Strings<CommandSender> {
   public static ParserDescriptor<CommandSender, ChunkLoadOptions.Mode> chunkLoadOptionsModeParser() {
      return ParserDescriptor.of(new ChunkLoadOptionsModeParser(), ChunkLoadOptions.Mode.class);
   }

   @NonNull
   public ArgumentParseResult<ChunkLoadOptions.Mode> parse(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput commandInput) {
      Optional<ChunkLoadOptions.Mode> parsed = ChunkLoadOptions.Mode.fromName(commandInput.peekString());
      if (!parsed.isPresent()) {
         return ArgumentParseResult.failure(new CloudLocalizedException(commandContext, Localization.COMMAND_INPUT_CHUNK_LOADING_MODE_INVALID, new String[]{commandInput.peekString()}));
      } else {
         commandInput.readString();
         return ArgumentParseResult.success((ChunkLoadOptions.Mode)parsed.get());
      }
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput input) {
      return ChunkLoadOptions.Mode.getAllNames();
   }
}
