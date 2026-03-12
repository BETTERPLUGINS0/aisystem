package ac.grim.grimac.shaded.incendo.cloud.help.result;

import ac.grim.grimac.shaded.incendo.cloud.help.HelpQuery;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@API(
   status = Status.STABLE
)
@Immutable
public interface MultipleCommandResult<C> extends HelpQueryResult<C> {
   @NonNull
   static <C> MultipleCommandResult<C> of(@NonNull final HelpQuery<C> query, @NonNull final String longestPath, @NonNull final List<String> childSuggestions) {
      return MultipleCommandResultImpl.of(query, longestPath, childSuggestions);
   }

   @NonNull
   HelpQuery<C> query();

   @NonNull
   String longestPath();

   @NonNull
   List<String> childSuggestions();
}
