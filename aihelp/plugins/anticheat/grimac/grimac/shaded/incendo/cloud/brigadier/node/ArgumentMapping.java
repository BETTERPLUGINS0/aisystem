package ac.grim.grimac.shaded.incendo.cloud.brigadier.node;

import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.SuggestionsType;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value.Immutable;

@API(
   status = Status.INTERNAL,
   since = "2.0.0"
)
@Immutable
interface ArgumentMapping<S> {
   @NonNull
   ArgumentType<?> argumentType();

   @NonNull
   default SuggestionsType suggestionsType() {
      return SuggestionsType.BRIGADIER_SUGGESTIONS;
   }

   @Nullable
   SuggestionProvider<S> suggestionProvider();
}
