package advancedplugins.pm2.cv.api.vehicle.input;

public class PlayerSteerInput {
   public final int sideways;
   public final int forward;
   public final boolean jump;
   public final boolean unmount;
   public final boolean keepAlive;

   public PlayerSteerInput(int var1, int var2, boolean var3, boolean var4) {
      this.sideways = var1;
      this.forward = var2;
      this.jump = var3;
      this.unmount = var4;
      this.keepAlive = var1 == 0 && var2 == 0 && !var3 && !var4;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         PlayerSteerInput var2 = (PlayerSteerInput)var1;
         if (this.sideways != var2.sideways) {
            return false;
         } else if (this.forward != var2.forward) {
            return false;
         } else if (this.jump != var2.jump) {
            return false;
         } else if (this.unmount != var2.unmount) {
            return false;
         } else {
            return this.keepAlive == var2.keepAlive;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.sideways;
      var1 = 31 * var1 + this.forward;
      var1 = 31 * var1 + (this.jump ? 1 : 0);
      var1 = 31 * var1 + (this.unmount ? 1 : 0);
      var1 = 31 * var1 + (this.keepAlive ? 1 : 0);
      return var1;
   }
}
