package advancedplugins.pm2.cv.packet.incoming;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerSteerInput;
import advancedplugins.pm2.cv.packet.PacketWrapper;
import org.jetbrains.annotations.NotNull;

@VersionSensible
public class PlayerInputPacketWrapper extends PacketWrapper {
   public final float sideways;
   public final float forward;
   public final boolean jump;
   public final boolean unmount;

   public boolean isKeepAlive() {
      return this.sideways == 0.0F && this.forward == 0.0F && !this.jump && !this.unmount;
   }

   @NotNull
   public PlayerSteerInput toSteerInput() {
      return new PlayerSteerInput(this.sideways != 0.0F ? (this.sideways < 0.0F ? -1 : 1) : 0, this.forward != 0.0F ? (this.forward < 0.0F ? -1 : 1) : 0, this.jump, this.unmount);
   }

   public PlayerInputPacketWrapper(final float sideways, final float forward, final boolean jump, final boolean unmount) {
      this.sideways = var1;
      this.forward = var2;
      this.jump = var3;
      this.unmount = var4;
   }

   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof PlayerInputPacketWrapper)) {
         return false;
      } else {
         PlayerInputPacketWrapper var2 = (PlayerInputPacketWrapper)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Float.compare(this.sideways, var2.sideways) != 0) {
            return false;
         } else if (Float.compare(this.forward, var2.forward) != 0) {
            return false;
         } else if (this.jump != var2.jump) {
            return false;
         } else {
            return this.unmount == var2.unmount;
         }
      }
   }

   protected boolean canEqual(final Object other) {
      return var1 instanceof PlayerInputPacketWrapper;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + Float.floatToIntBits(this.sideways);
      var3 = var3 * 59 + Float.floatToIntBits(this.forward);
      var3 = var3 * 59 + (this.jump ? 79 : 97);
      var3 = var3 * 59 + (this.unmount ? 79 : 97);
      return var3;
   }
}
