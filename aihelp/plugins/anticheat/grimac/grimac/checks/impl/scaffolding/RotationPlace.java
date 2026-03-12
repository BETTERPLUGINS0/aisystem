package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
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
   name = "RotationPlace",
   description = "Placed a block while not looking at it"
)
public class RotationPlace extends BlockPlaceCheck {
   private double flagBuffer = 0.0D;
   private boolean ignorePost = false;

   public RotationPlace(GrimPlayer player) {
      super(player);
   }

   public void onBlockPlace(BlockPlace place) {
      if (place.material != StateTypes.SCAFFOLDING) {
         if (this.player.cameraEntity.isSelf()) {
            if (!this.player.inVehicle()) {
               if (this.flagBuffer > 0.0D && !this.didRayTraceHit(place)) {
                  this.ignorePost = true;
                  if (this.flagAndAlert("pre-flying") && this.shouldModifyPackets() && this.shouldCancel()) {
                     place.resync();
                  }
               }

            }
         }
      }
   }

   public void onPostFlyingBlockPlace(BlockPlace place) {
      if (place.material != StateTypes.SCAFFOLDING) {
         if (this.player.cameraEntity.isSelf()) {
            if (!this.player.inVehicle()) {
               if (this.ignorePost) {
                  this.ignorePost = false;
               } else {
                  boolean hit = this.didRayTraceHit(place);
                  if (!hit) {
                     this.flagBuffer = 1.0D;
                     this.flagAndAlert("post-flying");
                  } else {
                     this.flagBuffer = Math.max(0.0D, this.flagBuffer - 0.1D);
                  }

               }
            }
         }
      }
   }

   private boolean didRayTraceHit(BlockPlace place) {
      SimpleCollisionBox box = new SimpleCollisionBox(place.position);
      List<Vector3f> possibleLookDirs = new ArrayList(Arrays.asList(new Vector3f(this.player.yaw, this.player.pitch, 0.0F), new Vector3f(this.player.lastYaw, this.player.pitch, 0.0F)));
      double[] possibleEyeHeights = this.player.getPossibleEyeHeights();
      double minEyeHeight = Double.MAX_VALUE;
      double maxEyeHeight = Double.MIN_VALUE;
      double[] var9 = possibleEyeHeights;
      int var10 = possibleEyeHeights.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         double height = var9[var11];
         minEyeHeight = Math.min(minEyeHeight, height);
         maxEyeHeight = Math.max(maxEyeHeight, height);
      }

      SimpleCollisionBox eyePositions = new SimpleCollisionBox(this.player.x, this.player.y + minEyeHeight, this.player.z, this.player.x, this.player.y + maxEyeHeight, this.player.z);
      eyePositions.expand(this.player.getMovementThreshold());
      if (eyePositions.isIntersected(box)) {
         return true;
      } else {
         if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
            ((List)possibleLookDirs).add(new Vector3f(this.player.lastYaw, this.player.lastPitch, 0.0F));
         }

         if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
            possibleLookDirs = Collections.singletonList(new Vector3f(this.player.yaw, this.player.pitch, 0.0F));
         }

         double distance = this.player.compensatedEntities.self.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
         double[] var24 = possibleEyeHeights;
         int var13 = possibleEyeHeights.length;

         for(int var14 = 0; var14 < var13; ++var14) {
            double d = var24[var14];
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
