package ac.grim.grimac.shaded.incendo.cloud.description;

import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@API(
   status = Status.STABLE
)
@Immutable
public interface Description {
   Description EMPTY = DescriptionImpl.of("");

   @NonNull
   static Description empty() {
      return EMPTY;
   }

   @NonNull
   static Description of(@NonNull final String string) {
      return (Description)(((String)Objects.requireNonNull(string, "string")).isEmpty() ? empty() : DescriptionImpl.of(string));
   }

   @NonNull
   static Description description(@NonNull final String string) {
      return of(string);
   }

   @NonNull
   String textDescription();

   default boolean isEmpty() {
      return this.textDescription().isEmpty();
   }
}
