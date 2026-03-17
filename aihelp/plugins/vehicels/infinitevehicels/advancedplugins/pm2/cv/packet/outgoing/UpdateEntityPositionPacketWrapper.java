package advancedplugins.pm2.cv.packet.outgoing;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.packet.PacketWrapper;

@VersionSensible
public class UpdateEntityPositionPacketWrapper extends PacketWrapper {
   public final int entityId;
   public final short deltaX;
   public final short deltaY;
   public final short deltaZ;
   public final boolean onGround;

   public UpdateEntityPositionPacketWrapper(final int entityId, final short deltaX, final short deltaY, final short deltaZ, final boolean onGround) {
      this.entityId = var1;
      this.deltaX = var2;
      this.deltaY = var3;
      this.deltaZ = var4;
      this.onGround = var5;
   }

   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof UpdateEntityPositionPacketWrapper)) {
         return false;
      } else {
         UpdateEntityPositionPacketWrapper var2 = (UpdateEntityPositionPacketWrapper)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.entityId != var2.entityId) {
            return false;
         } else if (this.deltaX != var2.deltaX) {
            return false;
         } else if (this.deltaY != var2.deltaY) {
            return false;
         } else if (this.deltaZ != var2.deltaZ) {
            return false;
         } else {
            return this.onGround == var2.onGround;
         }
      }
   }

   protected boolean canEqual(final Object other) {
      return var1 instanceof UpdateEntityPositionPacketWrapper;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.entityId;
      var3 = var3 * 59 + this.deltaX;
      var3 = var3 * 59 + this.deltaY;
      var3 = var3 * 59 + this.deltaZ;
      var3 = var3 * 59 + (this.onGround ? 79 : 97);
      return var3;
   }
}
