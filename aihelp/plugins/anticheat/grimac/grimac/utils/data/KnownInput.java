package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;

public record KnownInput(boolean forward, boolean backward, boolean left, boolean right, boolean jump, boolean shift, boolean sprint) {
   public static final KnownInput DEFAULT = new KnownInput(false, false, false, false, false, false, false);

   public KnownInput(boolean forward, boolean backward, boolean left, boolean right, boolean jump, boolean shift, boolean sprint) {
      this.forward = forward;
      this.backward = backward;
      this.left = left;
      this.right = right;
      this.jump = jump;
      this.shift = shift;
      this.sprint = sprint;
   }

   @Contract(
      pure = true
   )
   public boolean moving() {
      return this.forward || this.backward || this.left || this.right || this.jump;
   }

   public boolean forward() {
      return this.forward;
   }

   public boolean backward() {
      return this.backward;
   }

   public boolean left() {
      return this.left;
   }

   public boolean right() {
      return this.right;
   }

   public boolean jump() {
      return this.jump;
   }

   public boolean shift() {
      return this.shift;
   }

   public boolean sprint() {
      return this.sprint;
   }
}
