package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.Debug;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;

@Debug.Renderer(
   text = "String.valueOf(this.value) + \"i\"",
   hasChildren = "false"
)
final class IntBinaryTagImpl extends AbstractBinaryTag implements IntBinaryTag {
   private final int value;

   IntBinaryTagImpl(final int value) {
      this.value = value;
   }

   public int value() {
      return this.value;
   }

   public byte byteValue() {
      return (byte)(this.value & 255);
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public float floatValue() {
      return (float)this.value;
   }

   public int intValue() {
      return this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public short shortValue() {
      return (short)(this.value & '\uffff');
   }

   @NotNull
   public Number numberValue() {
      return this.value;
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         IntBinaryTagImpl that = (IntBinaryTagImpl)other;
         return this.value == that.value;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Integer.hashCode(this.value);
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("value", this.value));
   }
}
