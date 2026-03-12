package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option;

import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option.value.ValueType;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

final class OptionImpl<V> implements Option<V> {
   private final String id;
   private final ValueType<V> type;
   @Nullable
   private final V defaultValue;

   OptionImpl(final String id, final ValueType<V> type, @Nullable final V defaultValue) {
      this.id = id;
      this.type = type;
      this.defaultValue = defaultValue;
   }

   public String id() {
      return this.id;
   }

   public ValueType<V> valueType() {
      return this.type;
   }

   @Nullable
   public V defaultValue() {
      return this.defaultValue;
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         OptionImpl<?> that = (OptionImpl)other;
         return Objects.equals(this.id, that.id) && Objects.equals(this.type, that.type);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.id, this.type});
   }

   public String toString() {
      return this.getClass().getSimpleName() + "{id=" + this.id + ",type=" + this.type + ",defaultValue=" + this.defaultValue + '}';
   }
}
