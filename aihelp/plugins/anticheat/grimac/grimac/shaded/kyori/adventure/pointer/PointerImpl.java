package ac.grim.grimac.shaded.kyori.adventure.pointer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;

final class PointerImpl<T> implements Pointer<T> {
   private final Class<T> type;
   private final Key key;

   PointerImpl(final Class<T> type, final Key key) {
      this.type = type;
      this.key = key;
   }

   @NotNull
   public Class<T> type() {
      return this.type;
   }

   @NotNull
   public Key key() {
      return this.key;
   }

   public String toString() {
      return Internals.toString(this);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         PointerImpl<?> that = (PointerImpl)other;
         return this.type.equals(that.type) && this.key.equals(that.key);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.type.hashCode();
      result = 31 * result + this.key.hashCode();
      return result;
   }
}
