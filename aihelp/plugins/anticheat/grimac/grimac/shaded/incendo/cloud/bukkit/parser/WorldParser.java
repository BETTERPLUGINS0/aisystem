package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCaptionKeys;
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
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class WorldParser<C> implements ArgumentParser<C, World>, BlockingSuggestionProvider.Strings<C> {
   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, World> worldParser() {
      return ParserDescriptor.of(new WorldParser(), (Class)World.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, World> worldComponent() {
      return CommandComponent.builder().parser(worldParser());
   }

   @NonNull
   public ArgumentParseResult<World> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      String input = commandInput.readString();
      World world = Bukkit.getWorld(input);
      return world == null ? ArgumentParseResult.failure(new WorldParser.WorldParseException(input, commandContext)) : ArgumentParseResult.success(world);
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      return (Iterable)Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
   }

   public static final class WorldParseException extends ParserException {
      private final String input;

      public WorldParseException(@NonNull final String input, @NonNull final CommandContext<?> context) {
         super(WorldParser.class, context, BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_WORLD, CaptionVariable.of("input", input));
         this.input = input;
      }

      @NonNull
      public String input() {
         return this.input;
      }
   }
}
