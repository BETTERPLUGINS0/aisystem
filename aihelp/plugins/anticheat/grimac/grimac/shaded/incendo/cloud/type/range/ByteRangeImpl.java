package ac.grim.grimac.shaded.incendo.cloud.type.range;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.immutables.value.Generated;

@ParametersAreNonnullByDefault
@CheckReturnValue
@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
@Generated(
   from = "ByteRange",
   generator = "Immutables"
)
@Immutable
final class ByteRangeImpl implements ByteRange {
   private final byte minByte;
   private final byte maxByte;

   private ByteRangeImpl(byte minByte, byte maxByte) {
      this.minByte = minByte;
      this.maxByte = maxByte;
   }

   public byte minByte() {
      return this.minByte;
   }

   public byte maxByte() {
      return this.maxByte;
   }

   public final ByteRangeImpl withMinByte(byte value) {
      return this.minByte == value ? this : new ByteRangeImpl(value, this.maxByte);
   }

   public final ByteRangeImpl withMaxByte(byte value) {
      return this.maxByte == value ? this : new ByteRangeImpl(this.minByte, value);
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof ByteRangeImpl && this.equalTo(0, (ByteRangeImpl)another);
      }
   }

   private boolean equalTo(int synthetic, ByteRangeImpl another) {
      return this.minByte == another.minByte && this.maxByte == another.maxByte;
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + Byte.hashCode(this.minByte);
      h += (h << 5) + Byte.hashCode(this.maxByte);
      return h;
   }

   public String toString() {
      return "ByteRange{minByte=" + this.minByte + ", maxByte=" + this.maxByte + "}";
   }

   public static ByteRangeImpl of(byte minByte, byte maxByte) {
      return new ByteRangeImpl(minByte, maxByte);
   }

   public static ByteRangeImpl copyOf(ByteRange instance) {
      return instance instanceof ByteRangeImpl ? (ByteRangeImpl)instance : of(instance.minByte(), instance.maxByte());
   }
}
