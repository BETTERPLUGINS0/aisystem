package advancedplugins.pm2.cv.packet.incoming;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.packet.PacketWrapper;

@VersionSensible
public class SetPlayerPositionRotationPacketWrapper extends PacketWrapper {
   public final double x;
   public final double y;
   public final double z;
   public final float yRot;
   public final float xRot;
   public final boolean onGround;

   public SetPlayerPositionRotationPacketWrapper(final double x, final double y, final double z, final float yRot, final float xRot, final boolean onGround) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.yRot = var7;
      this.xRot = var8;
      this.onGround = var9;
   }

   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof SetPlayerPositionRotationPacketWrapper)) {
         return false;
      } else {
         SetPlayerPositionRotationPacketWrapper var2 = (SetPlayerPositionRotationPacketWrapper)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.x, var2.x) != 0) {
            return false;
         } else if (Double.compare(this.y, var2.y) != 0) {
            return false;
         } else if (Double.compare(this.z, var2.z) != 0) {
            return false;
         } else if (Float.compare(this.yRot, var2.yRot) != 0) {
            return false;
         } else if (Float.compare(this.xRot, var2.xRot) != 0) {
            return false;
         } else {
            return this.onGround == var2.onGround;
         }
      }
   }

   protected boolean canEqual(final Object other) {
      return var1 instanceof SetPlayerPositionRotationPacketWrapper;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.x);
      int var9 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.y);
      var9 = var9 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.z);
      var9 = var9 * 59 + (int)(var7 >>> 32 ^ var7);
      var9 = var9 * 59 + Float.floatToIntBits(this.yRot);
      var9 = var9 * 59 + Float.floatToIntBits(this.xRot);
      var9 = var9 * 59 + (this.onGround ? 79 : 97);
      return var9;
   }
}
