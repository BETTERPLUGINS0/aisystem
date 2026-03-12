package ac.grim.grimac.checks.impl.aim.processor;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.RotationCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.RotationUpdate;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.lists.RunningMode;
import ac.grim.grimac.utils.math.GrimMath;

public class AimProcessor extends Check implements RotationCheck {
   private static final int SIGNIFICANT_SAMPLES_THRESHOLD = 15;
   private static final int TOTAL_SAMPLES_THRESHOLD = 80;
   public double sensitivityX;
   public double sensitivityY;
   public double divisorX;
   public double divisorY;
   public double modeX;
   public double modeY;
   public double deltaDotsX;
   public double deltaDotsY;
   private final RunningMode xRotMode = new RunningMode(80);
   private final RunningMode yRotMode = new RunningMode(80);
   private float lastXRot;
   private float lastYRot;

   public AimProcessor(GrimPlayer playerData) {
      super(playerData);
   }

   public static double convertToSensitivity(double var13) {
      double var11 = var13 / 0.15000000596046448D / 8.0D;
      double var9 = Math.cbrt(var11);
      return (var9 - 0.20000000298023224D) / 0.6000000238418579D;
   }

   public void process(RotationUpdate rotationUpdate) {
      rotationUpdate.setProcessor(this);
      float deltaXRot = rotationUpdate.getDeltaXRotABS();
      this.divisorX = GrimMath.gcd((double)deltaXRot, (double)this.lastXRot);
      if (deltaXRot > 0.0F && deltaXRot < 5.0F && this.divisorX > GrimMath.MINIMUM_DIVISOR) {
         this.xRotMode.add(this.divisorX);
         this.lastXRot = deltaXRot;
      }

      float deltaYRot = rotationUpdate.getDeltaYRotABS();
      this.divisorY = GrimMath.gcd((double)deltaYRot, (double)this.lastYRot);
      if (deltaYRot > 0.0F && deltaYRot < 5.0F && this.divisorY > GrimMath.MINIMUM_DIVISOR) {
         this.yRotMode.add(this.divisorY);
         this.lastYRot = deltaYRot;
      }

      Pair modeY;
      if (this.xRotMode.size() > 15) {
         modeY = this.xRotMode.getMode();
         if ((Integer)modeY.second() > 15) {
            this.modeX = (Double)modeY.first();
            this.sensitivityX = convertToSensitivity(this.modeX);
         }
      }

      if (this.yRotMode.size() > 15) {
         modeY = this.yRotMode.getMode();
         if ((Integer)modeY.second() > 15) {
            this.modeY = (Double)modeY.first();
            this.sensitivityY = convertToSensitivity(this.modeY);
         }
      }

      this.deltaDotsX = (double)deltaXRot / this.modeX;
      this.deltaDotsY = (double)deltaYRot / this.modeY;
   }
}
