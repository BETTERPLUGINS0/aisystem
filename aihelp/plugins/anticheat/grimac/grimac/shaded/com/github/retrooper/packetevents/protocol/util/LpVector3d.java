package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
public final class LpVector3d {
   private static final double ABS_MAX_VALUE = Double.longBitsToDouble(4760304806130089984L);
   private static final double ABS_MIN_VALUE = Double.longBitsToDouble(4539628699284145152L);

   private LpVector3d() {
   }

   public static Vector3d read(PacketWrapper<?> wrapper) {
      int b0 = wrapper.readUnsignedByte();
      if (b0 == 0) {
         return Vector3d.zero();
      } else {
         long bits = (long)b0 | (long)wrapper.readUnsignedByte() << 8 | wrapper.readUnsignedInt() << 16;
         long max = (long)b0 & 3L;
         if (((long)b0 & 4L) != 0L) {
            max |= ((long)wrapper.readVarInt() & 4294967295L) << 2;
         }

         return new Vector3d(unpack(bits >> 3) * (double)max, unpack(bits >> 18) * (double)max, unpack(bits >> 33) * (double)max);
      }
   }

   public static void write(PacketWrapper<?> wrapper, Vector3d vector) {
      double x = sanitize(vector.x);
      double y = sanitize(vector.y);
      double z = sanitize(vector.z);
      double max = MathUtil.absMax(x, MathUtil.absMax(y, z));
      if (max < ABS_MIN_VALUE) {
         wrapper.writeByte(0);
      } else {
         long maxLong = MathUtil.ceilLong(max);
         boolean large = (maxLong & 3L) != maxLong;
         long mul = large ? maxLong & 3L | 4L : maxLong;
         long packedX = pack(x / (double)maxLong) << 3;
         long packedY = pack(y / (double)maxLong) << 18;
         long packedZ = pack(z / (double)maxLong) << 33;
         long bits = mul | packedX | packedY | packedZ;
         wrapper.writeShortLE((short)((int)bits));
         wrapper.writeInt((int)(bits >> 16));
         if (large) {
            wrapper.writeVarInt((int)(maxLong >> 2));
         }

      }
   }

   private static double sanitize(double comp) {
      return !Double.isNaN(comp) ? MathUtil.clamp(comp, -ABS_MAX_VALUE, ABS_MAX_VALUE) : 0.0D;
   }

   private static long pack(double comp) {
      return Math.round((comp * 0.5D + 0.5D) * 32766.0D);
   }

   private static double unpack(long bits) {
      return Math.min((double)(bits & 32767L), 32766.0D) * 2.0D / 32766.0D - 1.0D;
   }
}
