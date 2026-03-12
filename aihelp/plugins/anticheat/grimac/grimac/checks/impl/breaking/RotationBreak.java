package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.Ray;
import ac.grim.grimac.utils.nmsutil.ReachUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@CheckData(
   name = "RotationBreak",
   experimental = true
)
public class RotationBreak extends Check implements BlockBreakCheck {
   private double flagBuffer = 0.0D;
   private boolean ignorePost = false;

   public RotationBreak(GrimPlayer player) {
      super(player);
   }

   public void onBlockBreak(BlockBreak blockBreak) {
      if (this.player.cameraEntity.isSelf()) {
         if (!this.player.inVehicle()) {
            if (blockBreak.action != DiggingAction.CANCELLED_DIGGING) {
               if (this.flagBuffer > 0.0D && !this.didRayTraceHit(blockBreak)) {
                  this.ignorePost = true;
                  if (this.flagAndAlert("pre-flying, action=" + String.valueOf(blockBreak.action)) && this.shouldModifyPackets()) {
                     blockBreak.cancel();
                  }
               }

            }
         }
      }
   }

   public void onPostFlyingBlockBreak(BlockBreak blockBreak) {
      if (this.player.cameraEntity.isSelf()) {
         if (!this.player.inVehicle()) {
            if (blockBreak.action != DiggingAction.CANCELLED_DIGGING) {
               if (this.ignorePost) {
                  this.ignorePost = false;
               } else {
                  if (this.didRayTraceHit(blockBreak)) {
                     this.flagBuffer = Math.max(0.0D, this.flagBuffer - 0.1D);
                  } else {
                     this.flagBuffer = 1.0D;
                     this.flagAndAlert("post-flying, action=" + String.valueOf(blockBreak.action));
                  }

               }
            }
         }
      }
   }

   private boolean didRayTraceHit(BlockBreak blockBreak) {
      SimpleCollisionBox box = new SimpleCollisionBox(blockBreak.position);
      double[] possibleEyeHeights = this.player.getPossibleEyeHeights();
      double minEyeHeight = Double.MAX_VALUE;
      double maxEyeHeight = Double.MIN_VALUE;
      double[] var8 = possibleEyeHeights;
      int var9 = possibleEyeHeights.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         double height = var8[var10];
         minEyeHeight = Math.min(minEyeHeight, height);
         maxEyeHeight = Math.max(maxEyeHeight, height);
      }

      SimpleCollisionBox eyePositions = new SimpleCollisionBox(this.player.x, this.player.y + minEyeHeight, this.player.z, this.player.x, this.player.y + maxEyeHeight, this.player.z);
      eyePositions.expand(this.player.getMovementThreshold());
      if (eyePositions.isIntersected(box)) {
         return true;
      } else {
         List<Vector3f> possibleLookDirs = new ArrayList(Arrays.asList(new Vector3f(this.player.lastYaw, this.player.pitch, 0.0F), new Vector3f(this.player.yaw, this.player.pitch, 0.0F)));
         if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
            ((List)possibleLookDirs).add(new Vector3f(this.player.lastYaw, this.player.lastPitch, 0.0F));
         }

         if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
            possibleLookDirs = Collections.singletonList(new Vector3f(this.player.yaw, this.player.pitch, 0.0F));
         }

         double distance = this.player.compensatedEntities.self.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
         double[] var12 = possibleEyeHeights;
         int var13 = possibleEyeHeights.length;

         for(int var14 = 0; var14 < var13; ++var14) {
            double d = var12[var14];
            Iterator var17 = ((List)possibleLookDirs).iterator();

            while(var17.hasNext()) {
               Vector3f lookDir = (Vector3f)var17.next();
               Vector3d starting = new Vector3d(this.player.x, this.player.y + d, this.player.z);
               Ray trace = new Ray(this.player, starting.getX(), starting.getY(), starting.getZ(), lookDir.getX(), lookDir.getY());
               Pair<Vector3dm, BlockFace> intercept = ReachUtils.calculateIntercept(box, trace.getOrigin(), trace.getPointAtDistance(distance));
               if (intercept.first() != null) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}
