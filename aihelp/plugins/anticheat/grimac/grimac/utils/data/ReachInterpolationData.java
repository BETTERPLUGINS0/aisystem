package ac.grim.grimac.utils.data;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.NoCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;

public class ReachInterpolationData {
   private final SimpleCollisionBox targetLocation;
   private final GrimPlayer player;
   private final PacketEntity entity;
   public SimpleCollisionBox startingLocation;
   private int interpolationStepsLowBound = 0;
   private int interpolationStepsHighBound = 0;
   private int interpolationSteps = 1;
   private boolean expandNonRelative = false;

   public ReachInterpolationData(GrimPlayer player, SimpleCollisionBox startingLocation, TrackedPosition position, PacketEntity entity) {
      boolean unreliableTicking = !player.inVehicle() && player.canSkipTicks();
      this.startingLocation = startingLocation;
      Vector3d pos = position.getPos();
      this.targetLocation = new SimpleCollisionBox(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z, false);
      this.player = player;
      this.entity = entity;
      if (player.getClientVersion().isOlderThan(ClientVersion.V_1_9) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
         this.targetLocation.expand(0.03125D);
      }

      if (entity.isBoat) {
         this.interpolationSteps = 10;
      } else if (entity.isMinecart) {
         this.interpolationSteps = 5;
      } else if (entity.type == EntityTypes.SHULKER) {
         this.interpolationSteps = 1;
      } else if (entity.isLivingEntity) {
         this.interpolationSteps = 3;
      } else {
         this.interpolationSteps = 1;
      }

      if (unreliableTicking) {
         this.interpolationStepsHighBound = this.getInterpolationSteps();
      }

   }

   public ReachInterpolationData(GrimPlayer player, SimpleCollisionBox finishedLoc, PacketEntity entity) {
      this.startingLocation = finishedLoc;
      this.targetLocation = finishedLoc;
      this.entity = entity;
      this.player = player;
   }

   public static SimpleCollisionBox combineCollisionBox(SimpleCollisionBox one, SimpleCollisionBox two) {
      double minX = Math.min(one.minX, two.minX);
      double maxX = Math.max(one.maxX, two.maxX);
      double minY = Math.min(one.minY, two.minY);
      double maxY = Math.max(one.maxY, two.maxY);
      double minZ = Math.min(one.minZ, two.minZ);
      double maxZ = Math.max(one.maxZ, two.maxZ);
      return new SimpleCollisionBox(minX, minY, minZ, maxX, maxY, maxZ);
   }

   public static CollisionBox getOverlapHitbox(CollisionBox b1, CollisionBox b2) {
      if (b1 != NoCollisionBox.INSTANCE && b2 != NoCollisionBox.INSTANCE) {
         if (b1 instanceof SimpleCollisionBox && b2 instanceof SimpleCollisionBox) {
            SimpleCollisionBox box1 = (SimpleCollisionBox)b1;
            SimpleCollisionBox box2 = (SimpleCollisionBox)b2;
            double overlapMinX = Math.max(box1.minX, box2.minX);
            double overlapMaxX = Math.min(box1.maxX, box2.maxX);
            double overlapMinY = Math.max(box1.minY, box2.minY);
            double overlapMaxY = Math.min(box1.maxY, box2.maxY);
            double overlapMinZ = Math.max(box1.minZ, box2.minZ);
            double overlapMaxZ = Math.min(box1.maxZ, box2.maxZ);
            return (CollisionBox)(!(overlapMinX > overlapMaxX) && !(overlapMinY > overlapMaxY) && !(overlapMinZ > overlapMaxZ) ? new SimpleCollisionBox(overlapMinX, overlapMinY, overlapMinZ, overlapMaxX, overlapMaxY, overlapMaxZ) : NoCollisionBox.INSTANCE);
         } else {
            throw new IllegalArgumentException("Both b1 and b2 must be SimpleCollisionBox instances");
         }
      } else {
         return NoCollisionBox.INSTANCE;
      }
   }

   private int getInterpolationSteps() {
      return this.interpolationSteps;
   }

   public SimpleCollisionBox getPossibleLocationCombined() {
      int interpSteps = this.getInterpolationSteps();
      double stepMinX = (this.targetLocation.minX - this.startingLocation.minX) / (double)interpSteps;
      double stepMaxX = (this.targetLocation.maxX - this.startingLocation.maxX) / (double)interpSteps;
      double stepMinY = (this.targetLocation.minY - this.startingLocation.minY) / (double)interpSteps;
      double stepMaxY = (this.targetLocation.maxY - this.startingLocation.maxY) / (double)interpSteps;
      double stepMinZ = (this.targetLocation.minZ - this.startingLocation.minZ) / (double)interpSteps;
      double stepMaxZ = (this.targetLocation.maxZ - this.startingLocation.maxZ) / (double)interpSteps;
      SimpleCollisionBox minimumInterpLocation = new SimpleCollisionBox(this.startingLocation.minX + (double)this.interpolationStepsLowBound * stepMinX, this.startingLocation.minY + (double)this.interpolationStepsLowBound * stepMinY, this.startingLocation.minZ + (double)this.interpolationStepsLowBound * stepMinZ, this.startingLocation.maxX + (double)this.interpolationStepsLowBound * stepMaxX, this.startingLocation.maxY + (double)this.interpolationStepsLowBound * stepMaxY, this.startingLocation.maxZ + (double)this.interpolationStepsLowBound * stepMaxZ);

      for(int step = this.interpolationStepsLowBound + 1; step <= this.interpolationStepsHighBound; ++step) {
         minimumInterpLocation = combineCollisionBox(minimumInterpLocation, new SimpleCollisionBox(this.startingLocation.minX + (double)step * stepMinX, this.startingLocation.minY + (double)step * stepMinY, this.startingLocation.minZ + (double)step * stepMinZ, this.startingLocation.maxX + (double)step * stepMaxX, this.startingLocation.maxY + (double)step * stepMaxY, this.startingLocation.maxZ + (double)step * stepMaxZ));
      }

      return minimumInterpLocation;
   }

   public SimpleCollisionBox getPossibleHitboxCombined() {
      SimpleCollisionBox minimumInterpLocation = this.getPossibleLocationCombined();
      if (this.expandNonRelative) {
         minimumInterpLocation.expand(0.03125D, 0.015625D, 0.03125D);
      }

      GetBoundingBox.expandBoundingBoxByEntityDimensions(minimumInterpLocation, this.player, this.entity);
      return minimumInterpLocation;
   }

   public void updatePossibleStartingLocation(SimpleCollisionBox possibleLocationCombined) {
      this.startingLocation = combineCollisionBox(this.startingLocation, possibleLocationCombined);
   }

   public void tickMovement(boolean incrementLowBound, boolean tickingReliably) {
      if (!tickingReliably) {
         this.interpolationStepsHighBound = this.getInterpolationSteps();
      }

      if (incrementLowBound) {
         this.interpolationStepsLowBound = Math.min(this.interpolationStepsLowBound + 1, this.getInterpolationSteps());
      }

      this.interpolationStepsHighBound = Math.min(this.interpolationStepsHighBound + 1, this.getInterpolationSteps());
   }

   public String toString() {
      String var10000 = String.valueOf(this.targetLocation);
      return "ReachInterpolationData{targetLocation=" + var10000 + ", startingLocation=" + String.valueOf(this.startingLocation) + ", interpolationStepsLowBound=" + this.interpolationStepsLowBound + ", interpolationStepsHighBound=" + this.interpolationStepsHighBound + "}";
   }

   public void expandNonRelative() {
      this.expandNonRelative = true;
   }
}
