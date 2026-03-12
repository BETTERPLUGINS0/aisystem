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
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PlayerParser<C> implements ArgumentParser<C, Player>, BlockingSuggestionProvider<C> {
   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, Player> playerParser() {
      return ParserDescriptor.of(new PlayerParser(), (Class)Player.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, Player> playerComponent() {
      return CommandComponent.builder().parser(playerParser());
   }

   @NonNull
   public ArgumentParseResult<Player> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      String input = commandInput.readString();
      Player player = Bukkit.getPlayer(input);
      return player == null ? ArgumentParseResult.failure(new PlayerParser.PlayerParseException(input, commandContext)) : ArgumentParseResult.success(player);
   }

   @NonNull
   public Iterable<Suggestion> suggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      CommandSender bukkit = (CommandSender)commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
      return (Iterable)Bukkit.getOnlinePlayers().stream().filter((player) -> {
         return !(bukkit instanceof Player) || ((Player)bukkit).canSee(player);
      }).map(OfflinePlayer::getName).map(Suggestion::suggestion).collect(Collectors.toList());
   }

   public static final class PlayerParseException extends ParserException {
      private final String input;

      public PlayerParseException(@NonNull final String input, @NonNull final CommandContext<?> context) {
         super(PlayerParser.class, context, BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_PLAYER, CaptionVariable.of("input", input));
         this.input = input;
      }

      @NonNull
      public String input() {
         return this.input;
      }
   }
}
