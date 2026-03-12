package fr.xephi.authme.libs.net.kyori.adventure.key;

import fr.xephi.authme.libs.net.kyori.examination.Examinable;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import fr.xephi.authme.libs.net.kyori.examination.string.StringExaminer;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class KeyedValueImpl<T> implements Examinable, KeyedValue<T> {
   private final Key key;
   private final T value;

   KeyedValueImpl(final Key key, final T value) {
      this.key = key;
      this.value = value;
   }

   @NotNull
   public Key key() {
      return this.key;
   }

   @NotNull
   public T value() {
      return this.value;
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         KeyedValueImpl<?> that = (KeyedValueImpl)other;
         return this.key.equals(that.key) && this.value.equals(that.value);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.key.hashCode();
      result = 31 * result + this.value.hashCode();
      return result;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("key", (Object)this.key), ExaminableProperty.of("value", this.value));
   }

   public String toString() {
      return (String)this.examine(StringExaminer.simpleEscaping());
   }
}
