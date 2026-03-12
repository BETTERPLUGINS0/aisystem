package ac.grim.grimac.shaded.incendo.cloud.caption;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class DelegatingCaptionProvider<C> implements CaptionProvider<C> {
   @NonNull
   public abstract CaptionProvider<C> delegate();

   @Nullable
   public final String provide(@NonNull final Caption caption, @NonNull final C recipient) {
      return this.delegate().provide(caption, recipient);
   }
}
