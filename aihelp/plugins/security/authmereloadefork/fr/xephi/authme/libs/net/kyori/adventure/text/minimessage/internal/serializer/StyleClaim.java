package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer;

import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StyleClaim<V> {
   @NotNull
   static <T> StyleClaim<T> claim(@NotNull final String claimKey, @NotNull final Function<Style, T> lens, @NotNull final BiConsumer<T, TokenEmitter> emitable) {
      return claim(claimKey, lens, ($) -> {
         return true;
      }, emitable);
   }

   @NotNull
   static <T> StyleClaim<T> claim(@NotNull final String claimKey, @NotNull final Function<Style, T> lens, @NotNull final Predicate<T> filter, @NotNull final BiConsumer<T, TokenEmitter> emitable) {
      return new StyleClaimImpl((String)Objects.requireNonNull(claimKey, "claimKey"), (Function)Objects.requireNonNull(lens, "lens"), (Predicate)Objects.requireNonNull(filter, "filter"), (BiConsumer)Objects.requireNonNull(emitable, "emitable"));
   }

   @NotNull
   String claimKey();

   @Nullable
   Emitable apply(@NotNull final Style style);
}
