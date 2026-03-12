package ac.grim.grimac.manager.init.start;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.UncertaintyHandler;
import ac.grim.grimac.predictionengine.predictions.PredictionEngine;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.fastutil.objects.Object2IntMap;
import ac.grim.grimac.shaded.fastutil.objects.Object2IntOpenHashMap;
import ac.grim.grimac.shaded.fastutil.objects.ObjectIterator;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.lists.EvictingQueue;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public final class SuperDebug extends Check implements PostPredictionCheck {
   private static final StringBuilder[] flags = new StringBuilder[256];
   private final Object2IntMap<StringBuilder> continuedDebug = new Object2IntOpenHashMap();
   private final List<VectorData> predicted = new EvictingQueue(60);
   private final List<Vector3dm> actually = new EvictingQueue(60);
   private final List<SuperDebug.Location> locations = new EvictingQueue(60);
   private final List<Vector3dm> startTickClientVel = new EvictingQueue(60);
   private final List<Vector3dm> baseTickAddition = new EvictingQueue(60);
   private final List<Vector3dm> baseTickWater = new EvictingQueue(60);

   public SuperDebug(GrimPlayer player) {
      super(player);
   }

   public static StringBuilder getFlag(int identifier) {
      --identifier;
      return identifier < flags.length && identifier >= 0 ? flags[identifier] : null;
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (predictionComplete.isChecked()) {
         SuperDebug.Location location = new SuperDebug.Location(this.player.x, this.player.y, this.player.z, this.player.yaw, this.player.pitch, this.player.platformPlayer == null ? "null" : this.player.platformPlayer.getWorld().getName());
         ObjectIterator it = this.continuedDebug.object2IntEntrySet().iterator();

         while(it.hasNext()) {
            Entry<StringBuilder, Integer> debug = (Entry)it.next();
            this.appendDebug((StringBuilder)debug.getKey(), this.player.predictedVelocity, this.player.actualMovement, location, this.player.startTickClientVel, this.player.baseTickAddition, this.player.baseTickWaterPushing);
            debug.setValue((Integer)debug.getValue() - 1);
            if ((Integer)debug.getValue() <= 0) {
               it.remove();
            }
         }

         this.predicted.add(this.player.predictedVelocity);
         this.actually.add(this.player.actualMovement);
         this.locations.add(location);
         this.startTickClientVel.add(this.player.startTickClientVel);
         this.baseTickAddition.add(this.player.baseTickAddition);
         this.baseTickWater.add(this.player.baseTickWaterPushing);
         if (predictionComplete.getIdentifier() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Grim Version: ").append(GrimAPI.INSTANCE.getExternalAPI().getGrimVersion());
            sb.append("\n");
            sb.append("Player Name: ");
            sb.append(this.player.user.getName());
            sb.append("\nClient Version: ");
            sb.append(this.player.getClientVersion().getReleaseName());
            sb.append("\nClient Brand: ");
            sb.append(this.player.getBrand());
            sb.append("\nServer Version: ");
            sb.append(PacketEvents.getAPI().getServerManager().getVersion().getReleaseName());
            sb.append("\nPing: ");
            sb.append(this.player.getTransactionPing());
            sb.append("ms\n\n");

            for(int i = 0; i < this.predicted.size(); ++i) {
               VectorData predict = (VectorData)this.predicted.get(i);
               Vector3dm actual = (Vector3dm)this.actually.get(i);
               SuperDebug.Location loc = (SuperDebug.Location)this.locations.get(i);
               Vector3dm startTickVel = (Vector3dm)this.startTickClientVel.get(i);
               Vector3dm addition = (Vector3dm)this.baseTickAddition.get(i);
               Vector3dm water = (Vector3dm)this.baseTickWater.get(i);
               this.appendDebug(sb, predict, actual, loc, startTickVel, addition, water);
            }

            UncertaintyHandler uncertaintyHandler = this.player.uncertaintyHandler;
            sb.append("XNeg: ");
            sb.append(uncertaintyHandler.xNegativeUncertainty);
            sb.append("\nXPos: ");
            sb.append(uncertaintyHandler.xPositiveUncertainty);
            sb.append("\nYNeg: ");
            sb.append(uncertaintyHandler.yNegativeUncertainty);
            sb.append("\nYPos: ");
            sb.append(uncertaintyHandler.yPositiveUncertainty);
            sb.append("\nZNeg: ");
            sb.append(uncertaintyHandler.zNegativeUncertainty);
            sb.append("\nZPos: ");
            sb.append(uncertaintyHandler.zPositiveUncertainty);
            sb.append("\nStuck: ");
            sb.append(uncertaintyHandler.stuckOnEdge.hasOccurredSince(1));
            sb.append("\n\n0.03: ");
            sb.append(uncertaintyHandler.lastMovementWasZeroPointZeroThree);
            sb.append("\n0.03 reset: ");
            sb.append(uncertaintyHandler.lastMovementWasUnknown003VectorReset);
            sb.append("\n0.03 vertical: ");
            sb.append(uncertaintyHandler.wasZeroPointThreeVertically);
            sb.append("\n\nIs gliding: ");
            sb.append(this.player.isGliding);
            sb.append("\nIs swimming: ");
            sb.append(this.player.isSwimming);
            sb.append("\nIs on ground: ");
            sb.append(this.player.onGround);
            sb.append("\nClient claims ground: ");
            sb.append(this.player.clientClaimsLastOnGround);
            sb.append("\nLast on ground: ");
            sb.append(this.player.lastOnGround);
            sb.append("\nWater: ");
            sb.append(this.player.wasTouchingWater);
            sb.append("\nLava: ");
            sb.append(this.player.wasTouchingLava);
            sb.append("\nVehicle: ");
            sb.append(this.player.inVehicle());
            sb.append("\n\n");
            sb.append("Bounding box: ");
            sb.append("minX=");
            sb.append(this.player.boundingBox.minX);
            sb.append(", minY=");
            sb.append(this.player.boundingBox.minY);
            sb.append(", minZ=");
            sb.append(this.player.boundingBox.minZ);
            sb.append(", maxX=");
            sb.append(this.player.boundingBox.maxX);
            sb.append(", maxY=");
            sb.append(this.player.boundingBox.maxY);
            sb.append(", maxZ=");
            sb.append(this.player.boundingBox.maxZ);
            sb.append('}');
            sb.append("\n");
            int maxLength = 0;
            int maxPosLength = 0;

            int y;
            int z;
            int x;
            WrappedBlockState block;
            for(y = GrimMath.floor(this.player.boundingBox.minY) - 2; y <= GrimMath.ceil(this.player.boundingBox.maxY) + 2; ++y) {
               for(z = GrimMath.floor(this.player.boundingBox.minZ) - 2; z <= GrimMath.ceil(this.player.boundingBox.maxZ) + 2; ++z) {
                  maxPosLength = (int)Math.max((double)maxPosLength, Math.ceil(Math.log10((double)Math.abs(z))));

                  for(x = GrimMath.floor(this.player.boundingBox.minX) - 2; x <= GrimMath.ceil(this.player.boundingBox.maxX) + 2; ++x) {
                     maxPosLength = (int)Math.max((double)maxPosLength, Math.ceil(Math.log10((double)Math.abs(x))));
                     block = this.player.compensatedWorld.getBlock(x, y, z);
                     maxLength = Math.max(block.toString().replace("minecraft:", "").length(), maxLength);
                  }
               }
            }

            maxPosLength += 4;
            ++maxLength;

            for(y = GrimMath.ceil(this.player.boundingBox.maxY) + 2; y >= GrimMath.floor(this.player.boundingBox.minY) - 2; --y) {
               sb.append("y: ");
               sb.append(y);
               sb.append("\n");
               sb.append(String.format("%-" + maxPosLength + "s", "x: "));

               for(z = GrimMath.floor(this.player.boundingBox.minX) - 2; z <= GrimMath.ceil(this.player.boundingBox.maxX) + 2; ++z) {
                  sb.append(String.format("%-" + maxLength + "s", z));
               }

               sb.append("\n");

               for(z = GrimMath.floor(this.player.boundingBox.minZ) - 2; z <= GrimMath.ceil(this.player.boundingBox.maxZ) + 2; ++z) {
                  sb.append(String.format("%-" + maxPosLength + "s", "z: " + z + " "));

                  for(x = GrimMath.floor(this.player.boundingBox.minX) - 2; x <= GrimMath.ceil(this.player.boundingBox.maxX) + 2; ++x) {
                     block = this.player.compensatedWorld.getBlock(x, y, z);
                     sb.append(String.format("%-" + maxLength + "s", block.toString().replace("minecraft:", "")));
                  }

                  sb.append("\n");
               }

               sb.append("\n\n\n");
            }

            flags[predictionComplete.getIdentifier() - 1] = sb;
            this.continuedDebug.put(sb, 40);
         }
      }
   }

   private void appendDebug(StringBuilder sb, VectorData predict, Vector3dm actual, SuperDebug.Location location, Vector3dm startTick, Vector3dm addition, Vector3dm water) {
      if (predict.isZeroPointZeroThree()) {
         sb.append("Movement threshold/tick skipping\n");
      }

      if (predict.isAttackSlow()) {
         sb.append("* 0.6 horizontal attack slowdown\n");
      }

      if (predict.isKnockback()) {
         if (this.player.firstBreadKB != null) {
            sb.append("First bread knockback: ").append(this.player.firstBreadKB.vector).append("\n");
         }

         if (this.player.likelyKB != null) {
            sb.append("Second bread knockback: ").append(this.player.likelyKB.vector).append("\n");
         }
      }

      if (predict.isExplosion()) {
         if (this.player.firstBreadExplosion != null) {
            sb.append("First bread explosion: ").append(this.player.firstBreadExplosion.vector).append("\n");
         }

         if (this.player.likelyExplosions != null) {
            sb.append("Second bread explosion: ").append(this.player.likelyExplosions.vector).append("\n");
         }
      }

      if (predict.isTrident()) {
         sb.append("Trident\n");
      }

      if (predict.isSwimHop()) {
         sb.append("Swim hop\n");
      }

      if (predict.isJump()) {
         sb.append("Jump\n");
      }

      Set<VectorData> set = new HashSet(Collections.singletonList(new VectorData(startTick.clone(), VectorData.VectorType.BestVelPicked)));
      (new PredictionEngine()).applyMovementThreshold(this.player, set);
      Vector3dm trueStartVel = ((VectorData)set.toArray()[0]).vector;
      Vector3dm clientMovement = this.getPlayerMathMovement(this.player, actual.clone().subtract(trueStartVel), location.xRot);
      Vector3dm simulatedMovement = this.getPlayerMathMovement(this.player, predict.vector.clone().subtract(trueStartVel), location.xRot);
      Vector3dm offset = actual.clone().subtract(predict.vector);
      trueStartVel.add(addition);
      trueStartVel.add(water);
      sb.append("Simulated: ");
      sb.append(predict.vector.toString());
      sb.append("\nActually:  ");
      sb.append(actual);
      sb.append("\nOffset Vector: ");
      sb.append(offset);
      sb.append("\nOffset: ");
      sb.append(offset.length());
      sb.append("\nLocation:  ");
      sb.append(location);
      sb.append("\nInitial velocity: ");
      sb.append(startTick);
      if (addition.lengthSquared() > 0.0D) {
         sb.append("\nInitial vel addition: ");
         sb.append(addition);
      }

      if (water.lengthSquared() > 0.0D) {
         sb.append("\nWater vel addition: ");
         sb.append(water);
      }

      sb.append("\nClient input:    ");
      sb.append(clientMovement);
      sb.append(" length: ");
      sb.append(clientMovement.length());
      sb.append("\nSimulated input: ");
      sb.append(simulatedMovement);
      sb.append(" length: ");
      sb.append(simulatedMovement.length());
      sb.append("\n\n");
   }

   private Vector3dm getPlayerMathMovement(GrimPlayer player, Vector3dm wantedMovement, float f2) {
      float f3 = player.trigHandler.sin(f2 * 0.017453292F);
      float f4 = player.trigHandler.cos(f2 * 0.017453292F);
      float bestTheoreticalX = (float)((double)f3 * wantedMovement.getZ() + (double)f4 * wantedMovement.getX()) / (f3 * f3 + f4 * f4);
      float bestTheoreticalZ = (float)((double)(-f3) * wantedMovement.getX() + (double)f4 * wantedMovement.getZ()) / (f3 * f3 + f4 * f4);
      return new Vector3dm(bestTheoreticalX, 0.0F, bestTheoreticalZ);
   }

   private static record Location(double x, double y, double z, float xRot, float yRot, String world) {
      private Location(double x, double y, double z, float xRot, float yRot, String world) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.xRot = xRot;
         this.yRot = yRot;
         this.world = world;
      }

      @NotNull
      public String toString() {
         return "x: " + this.x + " y: " + this.y + " z: " + this.z + " xRot: " + this.xRot + " yRot: " + this.yRot + " world: " + this.world;
      }

      public double x() {
         return this.x;
      }

      public double y() {
         return this.y;
      }

      public double z() {
         return this.z;
      }

      public float xRot() {
         return this.xRot;
      }

      public float yRot() {
         return this.yRot;
      }

      public String world() {
         return this.world;
      }
   }
}
