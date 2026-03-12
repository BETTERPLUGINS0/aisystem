package ac.grim.grimac.predictionengine;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.VectorData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;

public class SneakingEstimator extends Check implements PostPredictionCheck {
   private SimpleCollisionBox sneakingPotentialHiddenVelocity = new SimpleCollisionBox();
   private List<VectorData> possible = new ArrayList();

   public SneakingEstimator(GrimPlayer player) {
      super(player);
   }

   public void storePossibleVelocities(List<VectorData> possible) {
      this.possible = possible;
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (predictionComplete.isChecked()) {
         double trueFriction = this.player.lastOnGround ? (double)this.player.friction * 0.91D : 0.91D;
         if (this.player.wasTouchingLava) {
            trueFriction = 0.5D;
         }

         if (this.player.wasTouchingWater) {
            trueFriction = 0.96D;
         }

         if (this.player.isGliding) {
            trueFriction = 0.99D;
         }

         if (!this.player.uncertaintyHandler.stuckOnEdge.hasOccurredSince(0)) {
            this.sneakingPotentialHiddenVelocity = new SimpleCollisionBox();
         } else {
            Iterator var4 = this.possible.iterator();

            while(true) {
               VectorData data;
               do {
                  do {
                     do {
                        do {
                           if (!var4.hasNext()) {
                              SimpleCollisionBox var10000 = this.sneakingPotentialHiddenVelocity;
                              var10000.minX *= trueFriction;
                              var10000 = this.sneakingPotentialHiddenVelocity;
                              var10000.minZ *= trueFriction;
                              var10000 = this.sneakingPotentialHiddenVelocity;
                              var10000.maxX *= trueFriction;
                              var10000 = this.sneakingPotentialHiddenVelocity;
                              var10000.maxZ *= trueFriction;
                              this.sneakingPotentialHiddenVelocity.minX = Math.min(-0.15D, this.sneakingPotentialHiddenVelocity.minX);
                              this.sneakingPotentialHiddenVelocity.minZ = Math.min(-0.15D, this.sneakingPotentialHiddenVelocity.minZ);
                              this.sneakingPotentialHiddenVelocity.maxX = Math.max(0.15D, this.sneakingPotentialHiddenVelocity.maxX);
                              this.sneakingPotentialHiddenVelocity.maxZ = Math.max(0.15D, this.sneakingPotentialHiddenVelocity.maxZ);
                              if (!this.player.uncertaintyHandler.lastStuckEast.hasOccurredSince(0)) {
                                 this.sneakingPotentialHiddenVelocity.maxX = 0.0D;
                              }

                              if (!this.player.uncertaintyHandler.lastStuckWest.hasOccurredSince(0)) {
                                 this.sneakingPotentialHiddenVelocity.minX = 0.0D;
                              }

                              if (!this.player.uncertaintyHandler.lastStuckNorth.hasOccurredSince(0)) {
                                 this.sneakingPotentialHiddenVelocity.minZ = 0.0D;
                              }

                              if (!this.player.uncertaintyHandler.lastStuckSouth.hasOccurredSince(0)) {
                                 this.sneakingPotentialHiddenVelocity.maxZ = 0.0D;
                              }

                              return;
                           }

                           data = (VectorData)var4.next();
                        } while(data.isJump() != this.player.predictedVelocity.isJump());
                     } while(data.isKnockback() != this.player.predictedVelocity.isKnockback());
                  } while(data.isExplosion() != this.player.predictedVelocity.isExplosion());

                  if (this.player.uncertaintyHandler.lastStuckWest.hasOccurredSince(0) || this.player.uncertaintyHandler.lastStuckNorth.hasOccurredSince(0)) {
                     this.sneakingPotentialHiddenVelocity.minX = Math.min(this.sneakingPotentialHiddenVelocity.minX, data.vector.getX());
                     this.sneakingPotentialHiddenVelocity.minZ = Math.min(this.sneakingPotentialHiddenVelocity.minZ, data.vector.getZ());
                  }
               } while(!this.player.uncertaintyHandler.lastStuckEast.hasOccurredSince(0) && !this.player.uncertaintyHandler.lastStuckSouth.hasOccurredSince(0));

               this.sneakingPotentialHiddenVelocity.maxX = Math.max(this.sneakingPotentialHiddenVelocity.maxX, data.vector.getX());
               this.sneakingPotentialHiddenVelocity.maxZ = Math.max(this.sneakingPotentialHiddenVelocity.maxZ, data.vector.getZ());
            }
         }
      }
   }

   @Generated
   public SimpleCollisionBox getSneakingPotentialHiddenVelocity() {
      return this.sneakingPotentialHiddenVelocity;
   }
}
