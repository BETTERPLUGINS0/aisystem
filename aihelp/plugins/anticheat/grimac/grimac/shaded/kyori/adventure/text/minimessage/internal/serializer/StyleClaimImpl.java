package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

class StyleClaimImpl<V> implements StyleClaim<V> {
   private final String claimKey;
   private final Function<Style, V> lens;
   private final Predicate<V> filter;
   private final BiConsumer<V, TokenEmitter> emitable;

   StyleClaimImpl(final String claimKey, final Function<Style, V> lens, final Predicate<V> filter, final BiConsumer<V, TokenEmitter> emitable) {
      this.claimKey = claimKey;
      this.lens = lens;
      this.filter = filter;
      this.emitable = emitable;
   }

   @NotNull
   public String claimKey() {
      return this.claimKey;
   }

   @Nullable
   public Emitable apply(@NotNull final Style style) {
      V element = this.lens.apply(style);
      return element != null && this.filter.test(element) ? (emitter) -> {
         this.emitable.accept(element, emitter);
      } : null;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.claimKey});
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof StyleClaimImpl)) {
         return false;
      } else {
         StyleClaimImpl<?> that = (StyleClaimImpl)other;
         return Objects.equals(this.claimKey, that.claimKey);
      }
   }
}
