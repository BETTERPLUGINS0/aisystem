package ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import com.mojang.brigadier.Message;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value.Immutable;

@API(
   status = Status.STABLE,
   since = "2.0.0"
)
@Immutable
public interface TooltipSuggestion extends Suggestion {
   @NonNull
   static TooltipSuggestion suggestion(@NonNull final String suggestion, @Nullable final Message tooltip) {
      return TooltipSuggestionImpl.of(suggestion, tooltip);
   }

   @NonNull
   static TooltipSuggestion tooltipSuggestion(@NonNull final Suggestion suggestion) {
      return suggestion instanceof TooltipSuggestion ? (TooltipSuggestion)suggestion : suggestion(suggestion.suggestion(), (Message)null);
   }

   @NonNull
   String suggestion();

   @Nullable
   Message tooltip();

   @NonNull
   TooltipSuggestion withSuggestion(@NonNull String suggestion);
}
