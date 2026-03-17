package advancedplugins.pm2.cv.api.vehicle.input;

public class PlayerInput {
   private final boolean leftClick;
   private final boolean rightClick;
   private final boolean swapHotkey;
   private final boolean crouch;
   private final boolean jump;
   private final boolean keepAlive;

   public PlayerInput(boolean var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      this.leftClick = var1;
      this.rightClick = var2;
      this.swapHotkey = var3;
      this.jump = var4;
      this.crouch = var5;
      this.keepAlive = !var1 && !var2 && !var3;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         PlayerInput var2 = (PlayerInput)var1;
         if (this.leftClick != var2.leftClick) {
            return false;
         } else if (this.rightClick != var2.rightClick) {
            return false;
         } else if (this.crouch != var2.crouch) {
            return false;
         } else if (this.jump != var2.jump) {
            return false;
         } else {
            return this.swapHotkey == var2.swapHotkey;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.leftClick ? 1 : 0;
      var1 = 31 * var1 + (this.rightClick ? 1 : 0);
      var1 = 31 * var1 + (this.swapHotkey ? 1 : 0);
      return var1;
   }

   public boolean wasPressed(PlayerInput.InputType var1) {
      boolean var10000;
      switch(var1.ordinal()) {
      case 0:
         var10000 = this.leftClick;
         break;
      case 1:
         var10000 = this.rightClick;
         break;
      case 2:
         var10000 = this.swapHotkey;
         break;
      case 3:
         var10000 = this.crouch;
         break;
      case 4:
         var10000 = this.jump;
         break;
      case 5:
         var10000 = false;
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   public boolean isLeftClick() {
      return this.leftClick;
   }

   public boolean isRightClick() {
      return this.rightClick;
   }

   public boolean isSwapHotkey() {
      return this.swapHotkey;
   }

   public boolean isCrouch() {
      return this.crouch;
   }

   public boolean isJump() {
      return this.jump;
   }

   public boolean isKeepAlive() {
      return this.keepAlive;
   }

   public static enum InputType {
      LEFT_CLICK,
      RIGHT_CLICK,
      SWAP_OFFHAND,
      CROUCH,
      JUMP,
      NONE;

      // $FF: synthetic method
      private static PlayerInput.InputType[] $values() {
         return new PlayerInput.InputType[]{LEFT_CLICK, RIGHT_CLICK, SWAP_OFFHAND, CROUCH, JUMP, NONE};
      }
   }
}
