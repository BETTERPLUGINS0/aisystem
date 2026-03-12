package ac.grim.grimac.utils.enums;

public enum Pose {
   STANDING(0.6F, 1.8F, 1.62F),
   FALL_FLYING(0.6F, 0.6F, 0.4F),
   SLEEPING(0.2F, 0.2F, 0.2F),
   SWIMMING(0.6F, 0.6F, 0.4F),
   SPIN_ATTACK(0.6F, 0.6F, 0.4F),
   CROUCHING(0.6F, 1.5F, 1.27F),
   DYING(0.2F, 0.2F, 0.2F),
   NINE_CROUCHING(0.6F, 1.65F, 1.54F),
   LONG_JUMPING(0.6F, 1.8F, 1.54F);

   public final float width;
   public final float height;
   public final float eyeHeight;

   private Pose(float param3, float param4, float param5) {
      this.width = width;
      this.height = height;
      this.eyeHeight = eyeHeight;
   }

   // $FF: synthetic method
   private static Pose[] $values() {
      return new Pose[]{STANDING, FALL_FLYING, SLEEPING, SWIMMING, SPIN_ATTACK, CROUCHING, DYING, NINE_CROUCHING, LONG_JUMPING};
   }
}
