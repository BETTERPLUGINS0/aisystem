package ac.grim.grimac.utils.math;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import lombok.Generated;

public class TrigHandler {
   private final GrimPlayer player;
   private double buffer = 0.0D;
   private boolean isVanillaMath = true;

   public TrigHandler(GrimPlayer player) {
      this.player = player;
   }

   public void toggleShitMath() {
      this.isVanillaMath = !this.isVanillaMath;
   }

   public Vector3dm getVanillaMathMovement(Vector3dm wantedMovement, float f, float f2) {
      float f3 = VanillaMath.sin(GrimMath.radians(f2));
      float f4 = VanillaMath.cos(GrimMath.radians(f2));
      float bestTheoreticalX = (float)((double)f3 * wantedMovement.getZ() + (double)f4 * wantedMovement.getX()) / (f3 * f3 + f4 * f4) / f;
      float bestTheoreticalZ = (float)((double)(-f3) * wantedMovement.getX() + (double)f4 * wantedMovement.getZ()) / (f3 * f3 + f4 * f4) / f;
      return new Vector3dm(bestTheoreticalX, 0.0F, bestTheoreticalZ);
   }

   public Vector3dm getShitMathMovement(Vector3dm wantedMovement, float f, float f2) {
      float f3 = this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8) ? OptifineFastMath.sin(GrimMath.radians(f2)) : LegacyFastMath.sin(GrimMath.radians(f2));
      float f4 = this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8) ? OptifineFastMath.cos(GrimMath.radians(f2)) : LegacyFastMath.cos(GrimMath.radians(f2));
      float bestTheoreticalX = (float)((double)f3 * wantedMovement.getZ() + (double)f4 * wantedMovement.getX()) / (f3 * f3 + f4 * f4) / f;
      float bestTheoreticalZ = (float)((double)(-f3) * wantedMovement.getX() + (double)f4 * wantedMovement.getZ()) / (f3 * f3 + f4 * f4) / f;
      return new Vector3dm(bestTheoreticalX, 0.0F, bestTheoreticalZ);
   }

   public void setOffset(double offset) {
      if (offset != 0.0D && !(offset > 0.001D)) {
         if (offset > 1.0E-5D) {
            Vector3dm trueMovement = this.player.actualMovement.clone().subtract(this.player.startTickClientVel);
            Vector3dm correctMath = this.getVanillaMathMovement(trueMovement, 0.1F, this.player.yaw);
            Vector3dm fastMath = this.getShitMathMovement(trueMovement, 0.1F, this.player.yaw);
            correctMath = new Vector3dm(Math.abs(correctMath.getX()), 0.0D, Math.abs(correctMath.getZ()));
            fastMath = new Vector3dm(Math.abs(fastMath.getX()), 0.0D, Math.abs(fastMath.getZ()));
            double minCorrectHorizontal = Math.min(correctMath.getX(), correctMath.getZ());
            minCorrectHorizontal = Math.min(minCorrectHorizontal, Math.abs(correctMath.getX() - correctMath.getZ()));
            double minFastMathHorizontal = Math.min(fastMath.getX(), fastMath.getZ());
            minFastMathHorizontal = Math.min(minFastMathHorizontal, Math.abs(fastMath.getX() - fastMath.getZ()));
            boolean newVanilla = minCorrectHorizontal < minFastMathHorizontal;
            this.buffer += newVanilla != this.isVanillaMath ? 1.0D : -0.25D;
            if (this.buffer > 5.0D) {
               this.buffer = 0.0D;
               this.isVanillaMath = !this.isVanillaMath;
            }

            this.buffer = Math.max(0.0D, this.buffer);
         }

      }
   }

   public float sin(float value) {
      return this.isVanillaMath ? VanillaMath.sin(value) : (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8) ? OptifineFastMath.sin(value) : LegacyFastMath.sin(value));
   }

   public float cos(float value) {
      return this.isVanillaMath ? VanillaMath.cos(value) : (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8) ? OptifineFastMath.cos(value) : LegacyFastMath.cos(value));
   }

   @Generated
   public boolean isVanillaMath() {
      return this.isVanillaMath;
   }
}
