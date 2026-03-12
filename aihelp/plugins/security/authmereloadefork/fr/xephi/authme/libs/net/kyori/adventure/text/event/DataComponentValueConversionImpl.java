package fr.xephi.authme.libs.net.kyori.adventure.text.event;

import fr.xephi.authme.libs.net.kyori.adventure.internal.Internals;
import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

final class DataComponentValueConversionImpl<I, O> implements DataComponentValueConverterRegistry.Conversion<I, O> {
   private final Class<I> source;
   private final Class<O> destination;
   private final BiFunction<Key, I, O> conversion;

   DataComponentValueConversionImpl(@NotNull final Class<I> source, @NotNull final Class<O> destination, @NotNull final BiFunction<Key, I, O> conversion) {
      this.source = source;
      this.destination = destination;
      this.conversion = conversion;
   }

   @NotNull
   public Class<I> source() {
      return this.source;
   }

   @NotNull
   public Class<O> destination() {
      return this.destination;
   }

   @NotNull
   public O convert(@NotNull final Key key, @NotNull final I input) {
      return this.conversion.apply((Key)Objects.requireNonNull(key, "key"), Objects.requireNonNull(input, "input"));
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("source", (Object)this.source), ExaminableProperty.of("destination", (Object)this.destination), ExaminableProperty.of("conversion", (Object)this.conversion));
   }

   public String toString() {
      return Internals.toString(this);
   }

   public boolean equals(final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         DataComponentValueConversionImpl<?, ?> that = (DataComponentValueConversionImpl)other;
         return Objects.equals(this.source, that.source) && Objects.equals(this.destination, that.destination) && Objects.equals(this.conversion, that.conversion);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.source, this.destination, this.conversion});
   }
}
