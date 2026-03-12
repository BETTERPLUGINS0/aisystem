package ac.grim.grimac.shaded.incendo.cloud.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.CommandTree;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContextFactory;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.execution.ExecutionCoordinator;
import ac.grim.grimac.shaded.incendo.cloud.services.State;
import ac.grim.grimac.shaded.incendo.cloud.setting.ManagerSetting;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public final class DelegatingSuggestionFactory<C, S extends Suggestion> implements SuggestionFactory<C, S> {
   private final List<S> singleEmptySuggestion;
   private final CommandManager<C> commandManager;
   private final CommandTree<C> commandTree;
   private final CommandContextFactory<C> contextFactory;
   private final ExecutionCoordinator<C> executionCoordinator;
   private final SuggestionMapper<S> mapper;

   public DelegatingSuggestionFactory(@NonNull final CommandManager<C> commandManager, @NonNull final CommandTree<C> commandTree, @NonNull final CommandContextFactory<C> contextFactory, @NonNull final ExecutionCoordinator<C> executionCoordinator, @NonNull final SuggestionMapper<S> mapper) {
      this.commandManager = commandManager;
      this.commandTree = commandTree;
      this.contextFactory = contextFactory;
      this.executionCoordinator = executionCoordinator;
      this.mapper = mapper;
      this.singleEmptySuggestion = Collections.singletonList(mapper.map(Suggestion.suggestion("")));
   }

   @NonNull
   public CompletableFuture<Suggestions<C, S>> suggest(@NonNull final CommandContext<C> context, @NonNull final String input) {
      return this.suggestFromTree(context, input);
   }

   @NonNull
   public CompletableFuture<Suggestions<C, S>> suggest(@NonNull final C sender, @NonNull final String input) {
      return this.suggest(this.contextFactory.create(true, sender), input);
   }

   @NonNull
   public <S2 extends Suggestion> SuggestionFactory<C, S2> mapped(@NonNull final SuggestionMapper<S2> mapper) {
      return new DelegatingSuggestionFactory(this.commandManager, this.commandTree, this.contextFactory, this.executionCoordinator, this.mapper.then(mapper));
   }

   private CompletableFuture<Suggestions<C, S>> suggestFromTree(final CommandContext<C> context, final String input) {
      CommandInput commandInput = CommandInput.of(input);
      context.store((String)"__raw_input__", commandInput.copy());
      if (this.commandManager.preprocessContext(context, commandInput) != State.ACCEPTED) {
         return this.commandManager.settings().get(ManagerSetting.FORCE_SUGGESTION) ? CompletableFuture.completedFuture(Suggestions.create(context, this.singleEmptySuggestion, commandInput)) : CompletableFuture.completedFuture(Suggestions.create(context, Collections.emptyList(), commandInput));
      } else {
         return this.executionCoordinator.coordinateSuggestions(this.commandTree, context, commandInput, this.mapper).thenApply((suggestions) -> {
            return this.commandManager.settings().get(ManagerSetting.FORCE_SUGGESTION) && suggestions.list().isEmpty() ? Suggestions.create(suggestions.commandContext(), this.singleEmptySuggestion, commandInput) : suggestions;
         });
      }
   }
}
