package advancedplugins.pm2.cv.packet.incoming;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.packet.PacketWrapper;

@VersionSensible
public class SetPlayerRotationPacketWrapper extends PacketWrapper {
   public final float yRot;
   public final float xRot;
   public final boolean onGround;

   public SetPlayerRotationPacketWrapper(final float yRot, final float xRot, final boolean onGround) {
      this.yRot = var1;
      this.xRot = var2;
      this.onGround = var3;
   }

   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof SetPlayerRotationPacketWrapper)) {
         return false;
      } else {
         SetPlayerRotationPacketWrapper var2 = (SetPlayerRotationPacketWrapper)var1;
         if (!var2.canEqual(this)) {
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
      return var1 instanceof SetPlayerRotationPacketWrapper;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + Float.floatToIntBits(this.yRot);
      var3 = var3 * 59 + Float.floatToIntBits(this.xRot);
      var3 = var3 * 59 + (this.onGround ? 79 : 97);
      return var3;
   }
}
