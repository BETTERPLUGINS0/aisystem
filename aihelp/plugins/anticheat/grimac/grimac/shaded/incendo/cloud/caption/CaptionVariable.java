package ac.grim.grimac.shaded.incendo.cloud.caption;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@API(
   status = Status.STABLE
)
@Immutable
public interface CaptionVariable {
   @NonNull
   static CaptionVariable of(@NonNull final String key, @NonNull final String value) {
      return CaptionVariableImpl.of(key, value);
   }

   @NonNull
   String key();

   @NonNull
   String value();
}
