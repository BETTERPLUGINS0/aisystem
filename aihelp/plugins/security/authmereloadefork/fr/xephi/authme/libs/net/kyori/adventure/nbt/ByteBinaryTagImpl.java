package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Debug.Renderer(
   text = "\"0x\" + Integer.toString(this.value, 16)",
   hasChildren = "false"
)
final class ByteBinaryTagImpl extends AbstractBinaryTag implements ByteBinaryTag {
   private final byte value;

   ByteBinaryTagImpl(final byte value) {
      this.value = value;
   }

   public byte value() {
      return this.value;
   }

   public byte byteValue() {
      return this.value;
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
      return (short)this.value;
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         ByteBinaryTagImpl that = (ByteBinaryTagImpl)other;
         return this.value == that.value;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Byte.hashCode(this.value);
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("value", this.value));
   }
}
