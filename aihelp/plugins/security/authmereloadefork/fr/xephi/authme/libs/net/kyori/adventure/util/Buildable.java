package fr.xephi.authme.libs.net.kyori.adventure.util;

import fr.xephi.authme.libs.net.kyori.adventure.builder.AbstractBuilder;
import java.util.function.Consumer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Buildable<R, B extends Buildable.Builder<R>> {
   /** @deprecated */
   @Deprecated
   @Contract(
      mutates = "param1"
   )
   @NotNull
   static <R extends Buildable<R, B>, B extends Buildable.Builder<R>> R configureAndBuild(@NotNull final B builder, @Nullable final Consumer<? super B> consumer) {
      return (Buildable)AbstractBuilder.configureAndBuild(builder, consumer);
   }

   @Contract(
      value = "-> new",
      pure = true
   )
   @NotNull
   B toBuilder();

   /** @deprecated */
   @Deprecated
   public interface Builder<R> extends AbstractBuilder<R> {
      @Contract(
         value = "-> new",
         pure = true
      )
      @NotNull
      R build();
   }
}
