package ac.grim.grimac.shaded.incendo.cloud.caption;

import java.util.Map;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value.Immutable;

@API(
   status = Status.STABLE
)
@Immutable
public abstract class ConstantCaptionProvider<C> implements CaptionProvider<C> {
   @NonNull
   public abstract Map<Caption, String> captions();

   @Nullable
   public final String provide(@NonNull final Caption caption, @NonNull final C recipient) {
      return (String)this.captions().get(caption);
   }
}
