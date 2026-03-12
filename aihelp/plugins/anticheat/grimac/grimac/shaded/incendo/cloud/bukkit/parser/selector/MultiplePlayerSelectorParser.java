package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.MultiplePlayerSelector;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.PlayerParser;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class MultiplePlayerSelectorParser<C> extends SelectorUtils.PlayerSelectorParser<C, MultiplePlayerSelector> {
   private final boolean allowEmpty;

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, MultiplePlayerSelector> multiplePlayerSelectorParser() {
      return multiplePlayerSelectorParser(true);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, MultiplePlayerSelector> multiplePlayerSelectorParser(final boolean allowEmpty) {
      return ParserDescriptor.of(new MultiplePlayerSelectorParser(allowEmpty), (Class)MultiplePlayerSelector.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, MultiplePlayerSelector> multiplePlayerSelectorComponent() {
      return CommandComponent.builder().parser(multiplePlayerSelectorParser());
   }

   @API(
      status = Status.STABLE,
      since = "1.8.0"
   )
   public MultiplePlayerSelectorParser(final boolean allowEmpty) {
      super(false);
      this.allowEmpty = allowEmpty;
   }

   public MultiplePlayerSelectorParser() {
      this(true);
   }

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public MultiplePlayerSelector mapResult(@NonNull final String input, @NonNull final SelectorUtils.EntitySelectorWrapper wrapper) {
      final List<Player> players = wrapper.players();
      if (players.isEmpty() && !this.allowEmpty) {
         (new SelectorUtils.SelectorParser.Thrower(NO_PLAYERS_EXCEPTION_TYPE.get())).throwIt();
      }

      return new MultiplePlayerSelector() {
         @NonNull
         public String inputString() {
            return input;
         }

         @NonNull
         public Collection<Player> values() {
            return Collections.unmodifiableCollection(players);
         }
      };
   }

   protected CompletableFuture<ArgumentParseResult<MultiplePlayerSelector>> legacyParse(final CommandContext<C> commandContext, final CommandInput commandInput) {
      String input = commandInput.peekString();
      final Player player = Bukkit.getPlayer(input);
      if (player == null) {
         return CompletableFuture.completedFuture(ArgumentParseResult.failure(new PlayerParser.PlayerParseException(input, commandContext)));
      } else {
         final String pop = commandInput.readString();
         return ArgumentParseResult.successFuture(new MultiplePlayerSelector() {
            @NonNull
            public String inputString() {
               return pop;
            }

            @NonNull
            public Collection<Player> values() {
               return Collections.singletonList(player);
            }
         });
      }
   }
}
