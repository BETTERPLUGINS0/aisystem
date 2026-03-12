package ac.grim.grimac.shaded.incendo.cloud.help.result;

import ac.grim.grimac.shaded.incendo.cloud.help.HelpQuery;
import java.util.Iterator;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

@API(
   status = Status.STABLE
)
@Immutable
public interface IndexCommandResult<C> extends HelpQueryResult<C>, Iterable<CommandEntry<C>> {
   @NonNull
   static <C> IndexCommandResult<C> of(@NonNull final HelpQuery<C> query, @NonNull final List<CommandEntry<C>> entries) {
      return IndexCommandResultImpl.of(query, entries);
   }

   @NonNull
   HelpQuery<C> query();

   @NonNull
   List<CommandEntry<C>> entries();

   @Parameter(false)
   default boolean isEmpty() {
      return this.entries().isEmpty();
   }

   @Parameter(false)
   @NonNull
   default Iterator<CommandEntry<C>> iterator() {
      return this.entries().iterator();
   }
}
