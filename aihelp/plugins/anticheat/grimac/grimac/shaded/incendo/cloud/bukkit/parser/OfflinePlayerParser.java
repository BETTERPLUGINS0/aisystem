package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCaptionKeys;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCommandContextKeys;
import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionVariable;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.ParserException;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.BlockingSuggestionProvider;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class OfflinePlayerParser<C> implements ArgumentParser<C, OfflinePlayer>, BlockingSuggestionProvider.Strings<C> {
   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, OfflinePlayer> offlinePlayerParser() {
      return ParserDescriptor.of(new OfflinePlayerParser(), (Class)OfflinePlayer.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, OfflinePlayer> offlinePlayerComponent() {
      return CommandComponent.builder().parser(offlinePlayerParser());
   }

   @NonNull
   public ArgumentParseResult<OfflinePlayer> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      String input = commandInput.readString();
      if (input.length() > 16) {
         return ArgumentParseResult.failure(new OfflinePlayerParser.OfflinePlayerParseException(input, commandContext));
      } else {
         OfflinePlayer player;
         try {
            player = Bukkit.getOfflinePlayer(input);
         } catch (Exception var6) {
            return ArgumentParseResult.failure(new OfflinePlayerParser.OfflinePlayerParseException(input, commandContext));
         }

         return ArgumentParseResult.success(player);
      }
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      CommandSender bukkit = (CommandSender)commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
      return (Iterable)Bukkit.getOnlinePlayers().stream().filter((player) -> {
         return !(bukkit instanceof Player) || ((Player)bukkit).canSee(player);
      }).map(OfflinePlayer::getName).collect(Collectors.toList());
   }

   public static final class OfflinePlayerParseException extends ParserException {
      private final String input;

      public OfflinePlayerParseException(@NonNull final String input, @NonNull final CommandContext<?> context) {
         super(OfflinePlayerParser.class, context, BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_OFFLINEPLAYER, CaptionVariable.of("input", input));
         this.input = input;
      }

      @NonNull
      public String input() {
         return this.input;
      }
   }
}
