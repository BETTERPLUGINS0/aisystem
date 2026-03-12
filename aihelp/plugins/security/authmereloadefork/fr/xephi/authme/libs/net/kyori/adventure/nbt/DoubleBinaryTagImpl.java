package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Debug.Renderer(
   text = "String.valueOf(this.value) + \"d\"",
   hasChildren = "false"
)
final class DoubleBinaryTagImpl extends AbstractBinaryTag implements DoubleBinaryTag {
   private final double value;

   DoubleBinaryTagImpl(final double value) {
      this.value = value;
   }

   public double value() {
      return this.value;
   }

   public byte byteValue() {
      return (byte)(ShadyPines.floor(this.value) & 255);
   }

   public double doubleValue() {
      return this.value;
   }

   public float floatValue() {
      return (float)this.value;
   }

   public int intValue() {
      return ShadyPines.floor(this.value);
   }

   public long longValue() {
      return (long)Math.floor(this.value);
   }

   public short shortValue() {
      return (short)(ShadyPines.floor(this.value) & '\uffff');
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         DoubleBinaryTagImpl that = (DoubleBinaryTagImpl)other;
         return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(that.value);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Double.hashCode(this.value);
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("value", this.value));
   }
}
