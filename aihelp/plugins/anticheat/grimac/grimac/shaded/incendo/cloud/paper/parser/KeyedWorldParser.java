package ac.grim.grimac.shaded.incendo.cloud.paper.parser;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.WorldParser;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class KeyedWorldParser<C> implements ArgumentParser<C, World>, SuggestionProvider<C> {
   private final ArgumentParser<C, World> parser;

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, World> keyedWorldParser() {
      return ParserDescriptor.of(new KeyedWorldParser(), (Class)World.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, World> keyedWorldComponent() {
      return CommandComponent.builder().parser(keyedWorldParser());
   }

   public KeyedWorldParser() {
      Class<?> keyed = CraftBukkitReflection.findClass("org.bukkit.Keyed");
      if (keyed != null && keyed.isAssignableFrom(World.class)) {
         this.parser = null;
      } else {
         this.parser = new WorldParser();
      }

   }

   @NonNull
   public ArgumentParseResult<World> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      if (this.parser != null) {
         return this.parser.parse(commandContext, commandInput);
      } else {
         String input = commandInput.readString();
         NamespacedKey key = NamespacedKey.fromString(input);
         if (key == null) {
            return ArgumentParseResult.failure(new WorldParser.WorldParseException(input, commandContext));
         } else {
            World world = Bukkit.getWorld(key);
            return world == null ? ArgumentParseResult.failure(new WorldParser.WorldParseException(input, commandContext)) : ArgumentParseResult.success(world);
         }
      }
   }

   @NonNull
   public CompletableFuture<? extends Iterable<? extends Suggestion>> suggestionsFuture(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      if (this.parser != null) {
         return this.parser.suggestionProvider().suggestionsFuture(commandContext, input);
      } else {
         List<World> worlds = Bukkit.getWorlds();
         List<Suggestion> completions = new ArrayList(worlds.size() * 2);

         NamespacedKey key;
         for(Iterator var5 = worlds.iterator(); var5.hasNext(); completions.add(Suggestion.suggestion(key.getNamespace() + ':' + key.getKey()))) {
            World world = (World)var5.next();
            key = world.getKey();
            if (input.hasRemainingInput() && key.getNamespace().equals("minecraft")) {
               completions.add(Suggestion.suggestion(key.getKey()));
            }
         }

         return CompletableFuture.completedFuture(completions);
      }
   }
}
