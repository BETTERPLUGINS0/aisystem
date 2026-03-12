package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import lombok.Generated;

public final class TrackedPosition {
   private static final double MODERN_COORDINATE_SCALE = 4096.0D;
   private static final double LEGACY_COORDINATE_SCALE = 32.0D;
   private final double scale = 4096.0D;
   private Vector3d pos = new Vector3d();

   public static long pack(double value, double scale) {
      return Math.round(value * scale);
   }

   public static double packLegacy(double value, double scale) {
      return Math.floor(value * scale);
   }

   private double unpack(long value) {
      return (double)value / this.scale;
   }

   private double unpackLegacy(double value) {
      return value / this.scale;
   }

   public Vector3d withDelta(long x, long y, long z) {
      if (x == 0L && y == 0L && z == 0L) {
         return this.pos;
      } else {
         double d = x == 0L ? this.pos.x : this.unpack(pack(this.pos.x, this.scale) + x);
         double e = y == 0L ? this.pos.y : this.unpack(pack(this.pos.y, this.scale) + y);
         double f = z == 0L ? this.pos.z : this.unpack(pack(this.pos.z, this.scale) + z);
         return new Vector3d(d, e, f);
      }
   }

   public Vector3d withDeltaLegacy(double x, double y, double z) {
      double d = this.unpackLegacy(packLegacy(this.pos.x, this.scale) + x);
      double e = this.unpackLegacy(packLegacy(this.pos.y, this.scale) + y);
      double f = this.unpackLegacy(packLegacy(this.pos.z, this.scale) + z);
      return new Vector3d(d, e, f);
   }

   @Generated
   public double getScale() {
      return this.scale;
   }

   @Generated
   public Vector3d getPos() {
      return this.pos;
   }

   @Generated
   public void setPos(Vector3d pos) {
      this.pos = pos;
   }
}
