package advancedplugins.pm2.cv.packet.outgoing;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.packet.PacketWrapper;

@VersionSensible
public class TeleportEntityPacketWrapper extends PacketWrapper {
   public final int entityId;
   public final double x;
   public final double y;
   public final double z;
   public final byte yRot;
   public final byte xRot;
   public final boolean onGround;

   public TeleportEntityPacketWrapper(final int entityId, final double x, final double y, final double z, final byte yRot, final byte xRot, final boolean onGround) {
      this.entityId = var1;
      this.x = var2;
      this.y = var4;
      this.z = var6;
      this.yRot = var8;
      this.xRot = var9;
      this.onGround = var10;
   }

   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof TeleportEntityPacketWrapper)) {
         return false;
      } else {
         TeleportEntityPacketWrapper var2 = (TeleportEntityPacketWrapper)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.entityId != var2.entityId) {
            return false;
         } else if (Double.compare(this.x, var2.x) != 0) {
            return false;
         } else if (Double.compare(this.y, var2.y) != 0) {
            return false;
         } else if (Double.compare(this.z, var2.z) != 0) {
            return false;
         } else if (this.yRot != var2.yRot) {
            return false;
         } else if (this.xRot != var2.xRot) {
            return false;
         } else {
            return this.onGround == var2.onGround;
         }
      }
   }

   protected boolean canEqual(final Object other) {
      return var1 instanceof TeleportEntityPacketWrapper;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var9 = var2 * 59 + this.entityId;
      long var3 = Double.doubleToLongBits(this.x);
      var9 = var9 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.y);
      var9 = var9 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.z);
      var9 = var9 * 59 + (int)(var7 >>> 32 ^ var7);
      var9 = var9 * 59 + this.yRot;
      var9 = var9 * 59 + this.xRot;
      var9 = var9 * 59 + (this.onGround ? 79 : 97);
      return var9;
   }
}
