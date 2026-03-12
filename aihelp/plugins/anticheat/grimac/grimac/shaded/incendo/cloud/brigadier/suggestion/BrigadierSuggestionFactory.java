package ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierCommand;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionFactory;
import ac.grim.grimac.shaded.incendo.cloud.type.tuple.Pair;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL,
   since = "2.0.0"
)
public final class BrigadierSuggestionFactory<C, S> {
   private final CloudBrigadierManager<C, S> cloudBrigadierManager;
   private final CommandManager<C> commandManager;
   private final SuggestionFactory<C, ? extends TooltipSuggestion> suggestionFactory;

   public BrigadierSuggestionFactory(@NonNull final CloudBrigadierManager<C, S> cloudBrigadierManager, @NonNull final CommandManager<C> commandManager, @NonNull final SuggestionFactory<C, ? extends TooltipSuggestion> suggestionFactory) {
      this.cloudBrigadierManager = cloudBrigadierManager;
      this.commandManager = commandManager;
      this.suggestionFactory = suggestionFactory;
   }

   @NonNull
   public CompletableFuture<Suggestions> buildSuggestions(@NonNull final CommandContext<S> senderContext, @Nullable final CommandNode<C> parentNode, @NonNull final SuggestionsBuilder builder) {
      C cloudSender = this.cloudBrigadierManager.senderMapper().map(senderContext.getSource());
      ac.grim.grimac.shaded.incendo.cloud.context.CommandContext<C> commandContext = new ac.grim.grimac.shaded.incendo.cloud.context.CommandContext(true, cloudSender, this.commandManager);
      commandContext.store("_cloud_brigadier_native_sender", senderContext.getSource());
      String command = builder.getInput().substring(((StringRange)((Pair)CloudBrigadierCommand.parsedNodes(senderContext.getLastChild()).get(0)).second()).getStart());
      String leading = command.split(" ")[0];
      if (leading.contains(":")) {
         command = command.substring(leading.split(":")[0].length() + 1);
      }

      return this.suggestionFactory.suggest(commandContext.sender(), command).thenApply((suggestionsResult) -> {
         List<TooltipSuggestion> suggestions = new ArrayList(suggestionsResult.list());
         if (parentNode != null) {
            Set<String> siblingLiterals = (Set)parentNode.children().stream().map(CommandNode::component).filter(Objects::nonNull).filter((c) -> {
               return c.type() == CommandComponent.ComponentType.LITERAL;
            }).flatMap((commandComponent) -> {
               return commandComponent.aliases().stream();
            }).collect(Collectors.toSet());
            suggestions.removeIf((suggestionx) -> {
               return siblingLiterals.contains(suggestionx.suggestion());
            });
         }

         int trimmed = builder.getInput().length() - suggestionsResult.commandInput().length();
         int rawOffset = suggestionsResult.commandInput().cursor();
         SuggestionsBuilder suggestionsBuilder = builder.createOffset(rawOffset + trimmed);
         Iterator var7 = suggestions.iterator();

         while(var7.hasNext()) {
            TooltipSuggestion suggestion = (TooltipSuggestion)var7.next();

            try {
               suggestionsBuilder.suggest(Integer.parseInt(suggestion.suggestion()), suggestion.tooltip());
            } catch (NumberFormatException var10) {
               suggestionsBuilder.suggest(suggestion.suggestion(), suggestion.tooltip());
            }
         }

         return suggestionsBuilder.build();
      });
   }
}
