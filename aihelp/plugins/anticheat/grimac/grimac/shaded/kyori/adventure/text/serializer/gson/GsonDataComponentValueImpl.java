package ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.text.event.DataComponentValue;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import java.util.Objects;
import java.util.stream.Stream;

class GsonDataComponentValueImpl implements GsonDataComponentValue {
   private final JsonElement element;

   GsonDataComponentValueImpl(@NotNull final JsonElement element) {
      this.element = element;
   }

   @NotNull
   public JsonElement element() {
      return this.element;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("element", (Object)this.element));
   }

   public String toString() {
      return Internals.toString(this);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         GsonDataComponentValueImpl that = (GsonDataComponentValueImpl)other;
         return Objects.equals(this.element, that.element);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.element);
   }

   static final class RemovedGsonComponentValueImpl extends GsonDataComponentValueImpl implements DataComponentValue.Removed {
      static final GsonDataComponentValueImpl.RemovedGsonComponentValueImpl INSTANCE = new GsonDataComponentValueImpl.RemovedGsonComponentValueImpl();

      private RemovedGsonComponentValueImpl() {
         super(JsonNull.INSTANCE);
      }
   }
}
