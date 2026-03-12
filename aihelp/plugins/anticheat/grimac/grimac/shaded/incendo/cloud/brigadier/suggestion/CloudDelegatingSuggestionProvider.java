package ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL,
   since = "2.0.0"
)
public final class CloudDelegatingSuggestionProvider<C, S> implements SuggestionProvider<S> {
   private final BrigadierSuggestionFactory<C, S> brigadierSuggestionFactory;
   private final CommandNode<C> node;

   public CloudDelegatingSuggestionProvider(@NonNull final BrigadierSuggestionFactory<C, S> suggestionFactory, @NonNull final CommandNode<C> node) {
      this.brigadierSuggestionFactory = suggestionFactory;
      this.node = node;
   }

   @NonNull
   public CompletableFuture<Suggestions> getSuggestions(@NonNull final CommandContext<S> context, @NonNull final SuggestionsBuilder builder) throws CommandSyntaxException {
      return this.brigadierSuggestionFactory.buildSuggestions(context, this.node.parent(), builder);
   }
}
