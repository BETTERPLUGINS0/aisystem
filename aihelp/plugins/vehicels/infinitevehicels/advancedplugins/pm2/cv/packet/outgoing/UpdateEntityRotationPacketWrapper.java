package advancedplugins.pm2.cv.packet.outgoing;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.packet.PacketWrapper;

@VersionSensible
public class UpdateEntityRotationPacketWrapper extends PacketWrapper {
   public final int entityId;
   public final byte yRot;
   public final byte xRot;
   public final boolean onGround;

   public UpdateEntityRotationPacketWrapper(final int entityId, final byte yRot, final byte xRot, final boolean onGround) {
      this.entityId = var1;
      this.yRot = var2;
      this.xRot = var3;
      this.onGround = var4;
   }

   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof UpdateEntityRotationPacketWrapper)) {
         return false;
      } else {
         UpdateEntityRotationPacketWrapper var2 = (UpdateEntityRotationPacketWrapper)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.entityId != var2.entityId) {
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
      return var1 instanceof UpdateEntityRotationPacketWrapper;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.entityId;
      var3 = var3 * 59 + this.yRot;
      var3 = var3 * 59 + this.xRot;
      var3 = var3 * 59 + (this.onGround ? 79 : 97);
      return var3;
   }
}
