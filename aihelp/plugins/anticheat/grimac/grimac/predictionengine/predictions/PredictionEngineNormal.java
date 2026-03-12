package ac.grim.grimac.predictionengine.predictions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.JumpPower;
import java.util.HashSet;
import java.util.Iterator;
import java.util.OptionalInt;
import java.util.Set;

public class PredictionEngineNormal extends PredictionEngine {
   public static void staticVectorEndOfTick(GrimPlayer player, Vector3dm vector) {
      double adjustedY = vector.getY();
      OptionalInt levitation = player.compensatedEntities.getPotionLevelForPlayer(PotionTypes.LEVITATION);
      if (levitation.isPresent()) {
         adjustedY += (0.05D * (double)(levitation.getAsInt() + 1) - vector.getY()) * 0.2D;
         player.fallDistance = 0.0D;
      } else if (player.hasGravity) {
         adjustedY -= player.gravity;
      }

      vector.setX(vector.getX() * (double)player.friction);
      vector.setY(adjustedY * 0.9800000190734863D);
      vector.setZ(vector.getZ() * (double)player.friction);
   }

   public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
      if (!player.supportsEndTick() || player.packetStateData.knownInput.jump()) {
         VectorData vector;
         Vector3dm jump;
         for(Iterator var3 = (new HashSet(existingVelocities)).iterator(); var3.hasNext(); existingVelocities.add(vector.returnNewModified(jump, VectorData.VectorType.Jump))) {
            vector = (VectorData)var3.next();
            jump = vector.vector.clone();
            if (!player.isFlying) {
               OptionalInt jumpBoost = player.compensatedEntities.getPotionLevelForPlayer(PotionTypes.JUMP_BOOST);
               if ((jumpBoost.isEmpty() || jumpBoost.getAsInt() >= 0) && player.onGround || !player.lastOnGround) {
                  return;
               }

               JumpPower.jumpFromGround(player, jump);
            } else {
               jump.add(0.0D, (double)(player.flySpeed * 3.0F), 0.0D);
               if (!player.wasFlying) {
                  Vector3dm edgeCaseJump = jump.clone();
                  JumpPower.jumpFromGround(player, edgeCaseJump);
                  existingVelocities.add(vector.returnNewModified(edgeCaseJump, VectorData.VectorType.Jump));
               }
            }
         }

      }
   }

   public void endOfTick(GrimPlayer player, double delta) {
      super.endOfTick(player, delta);
      boolean walkingOnPowderSnow = false;
      if (!player.inVehicle() && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17) && player.compensatedWorld.getBlockType(player.x, player.y, player.z) == StateTypes.POWDER_SNOW) {
         ItemStack boots = player.inventory.getBoots();
         walkingOnPowderSnow = boots != null && boots.getType() == ItemTypes.LEATHER_BOOTS;
      }

      player.isClimbing = Collisions.onClimbable(player, player.x, player.y, player.z);
      if (player.lastWasClimbing == 0.0D && (player.pointThreeEstimator.isNearClimbable() || player.isClimbing) && (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) || !Collisions.isEmpty(player, player.boundingBox.copy().expand(player.clientVelocity.getX(), 0.0D, player.clientVelocity.getZ()).expand(0.5D, -1.0E-7D, 0.5D))) || walkingOnPowderSnow) {
         Vector3dm ladderVelocity = player.clientVelocity.clone().setY(0.2D);
         staticVectorEndOfTick(player, ladderVelocity);
         player.lastWasClimbing = ladderVelocity.getY();
      }

      Iterator var8 = player.getPossibleVelocitiesMinusKnockback().iterator();

      while(var8.hasNext()) {
         VectorData vector = (VectorData)var8.next();
         staticVectorEndOfTick(player, vector.vector);
      }

   }

   public Vector3dm handleOnClimbable(Vector3dm vector, GrimPlayer player) {
      if (player.isClimbing) {
         player.fallDistance = 0.0D;
         vector.setX(GrimMath.clamp(vector.getX(), -0.15000000596046448D, 0.15000000596046448D));
         vector.setZ(GrimMath.clamp(vector.getZ(), -0.15000000596046448D, 0.15000000596046448D));
         vector.setY(Math.max(vector.getY(), -0.15000000596046448D));
         if (vector.getY() < 0.0D && player.compensatedWorld.getBlockType(player.lastX, player.lastY, player.lastZ) != StateTypes.SCAFFOLDING && player.isSneaking && !player.isFlying) {
            vector.setY(0.0D);
         }
      }

      return vector;
   }
}
