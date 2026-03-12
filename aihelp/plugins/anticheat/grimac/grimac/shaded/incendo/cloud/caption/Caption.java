package ac.grim.grimac.shaded.incendo.cloud.caption;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@API(
   status = Status.STABLE
)
@Immutable
public interface Caption {
   @NonNull
   static Caption of(@NonNull final String key) {
      return CaptionImpl.of(key);
   }

   @NonNull
   String key();
}
