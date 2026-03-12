package ac.grim.grimac.shaded.incendo.cloud.caption;

import java.util.function.Function;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.STABLE
)
public interface CaptionProvider<C> {
   @NonNull
   static <C> ImmutableConstantCaptionProvider.Builder<C> constantProvider() {
      return ImmutableConstantCaptionProvider.builder();
   }

   @NonNull
   static <C> CaptionProvider<C> constantProvider(@NonNull final Caption caption, @NonNull final String value) {
      return constantProvider().putCaption(caption, value).build();
   }

   @NonNull
   static <C> CaptionProvider<C> forCaption(@NonNull final Caption caption, @NonNull final Function<C, String> provider) {
      return (key, recipient) -> {
         return key.equals(caption) ? (String)provider.apply(recipient) : null;
      };
   }

   @Nullable
   String provide(@NonNull Caption caption, @NonNull C recipient);
}
