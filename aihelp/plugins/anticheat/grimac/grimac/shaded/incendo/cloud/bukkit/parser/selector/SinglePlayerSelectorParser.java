package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.SinglePlayerSelector;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.PlayerParser;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import java.util.concurrent.CompletableFuture;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class SinglePlayerSelectorParser<C> extends SelectorUtils.PlayerSelectorParser<C, SinglePlayerSelector> {
   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, SinglePlayerSelector> singlePlayerSelectorParser() {
      return ParserDescriptor.of(new SinglePlayerSelectorParser(), (Class)SinglePlayerSelector.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, SinglePlayerSelector> singlePlayerSelectorComponent() {
      return CommandComponent.builder().parser(singlePlayerSelectorParser());
   }

   public SinglePlayerSelectorParser() {
      super(true);
   }

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public SinglePlayerSelector mapResult(@NonNull final String input, @NonNull final SelectorUtils.EntitySelectorWrapper wrapper) {
      final Player player = wrapper.singlePlayer();
      return new SinglePlayerSelector() {
         @NonNull
         public Player single() {
            return player;
         }

         @NonNull
         public String inputString() {
            return input;
         }
      };
   }

   protected CompletableFuture<ArgumentParseResult<SinglePlayerSelector>> legacyParse(final CommandContext<C> commandContext, final CommandInput commandInput) {
      String input = commandInput.peekString();
      final Player player = Bukkit.getPlayer(input);
      if (player == null) {
         return CompletableFuture.completedFuture(ArgumentParseResult.failure(new PlayerParser.PlayerParseException(input, commandContext)));
      } else {
         final String pop = commandInput.readString();
         return ArgumentParseResult.successFuture(new SinglePlayerSelector() {
            @NonNull
            public Player single() {
               return player;
            }

            @NonNull
            public String inputString() {
               return pop;
            }
         });
      }
   }
}
