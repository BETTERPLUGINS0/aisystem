package ac.grim.grimac.shaded.kyori.adventure.builder;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

@FunctionalInterface
public interface AbstractBuilder<R> {
   @Contract(
      mutates = "param1"
   )
   @NotNull
   static <R, B extends AbstractBuilder<R>> R configureAndBuild(@NotNull final B builder, @Nullable final Consumer<? super B> consumer) {
      if (consumer != null) {
         consumer.accept(builder);
      }

      return builder.build();
   }

   @Contract(
      value = "-> new",
      pure = true
   )
   @NotNull
   R build();
}
