package ac.grim.grimac.predictionengine;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.fastutil.objects.ObjectIterator;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.LastInstance;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityRideable;
import ac.grim.grimac.utils.data.packetentity.PacketEntityStrider;
import ac.grim.grimac.utils.lists.EvictingQueue;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.BoundingBoxSize;
import ac.grim.grimac.utils.nmsutil.ReachUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class UncertaintyHandler {
   private final GrimPlayer player;
   public final EvictingQueue<Double> pistonX = new EvictingQueue(5);
   public final EvictingQueue<Double> pistonY = new EvictingQueue(5);
   public final EvictingQueue<Double> pistonZ = new EvictingQueue(5);
   public boolean isStepMovement;
   public HashSet<BlockFace> slimePistonBounces;
   public double xNegativeUncertainty = 0.0D;
   public double xPositiveUncertainty = 0.0D;
   public double zNegativeUncertainty = 0.0D;
   public double zPositiveUncertainty = 0.0D;
   public double yNegativeUncertainty = 0.0D;
   public double yPositiveUncertainty = 0.0D;
   public double thisTickSlimeBlockUncertainty = 0.0D;
   public double nextTickSlimeBlockUncertainty = 0.0D;
   public boolean onGroundUncertain = false;
   public boolean lastPacketWasGroundPacket = false;
   public boolean isSteppingOnSlime = false;
   public boolean isSteppingOnIce = false;
   public boolean isSteppingOnHoney = false;
   public boolean wasSteppingOnBouncyBlock = false;
   public boolean isSteppingOnBouncyBlock = false;
   public boolean isSteppingNearBubbleColumn = false;
   public boolean isSteppingNearScaffolding = false;
   public boolean isSteppingNearShulker = false;
   public boolean isNearGlitchyBlock = false;
   public boolean isOrWasNearGlitchyBlock = false;
   public boolean claimingLeftStuckSpeed = false;
   public boolean lastMovementWasZeroPointZeroThree = false;
   public boolean lastMovementWasUnknown003VectorReset = false;
   public boolean wasZeroPointThreeVertically = false;
   public final EvictingQueue<Integer> collidingEntities = new EvictingQueue(3);
   public final EvictingQueue<Integer> riptideEntities = new EvictingQueue(3);
   public final List<Integer> fishingRodPulls = new ArrayList();
   public SimpleCollisionBox fireworksBox = null;
   public SimpleCollisionBox fishingRodPullBox = null;
   public final LastInstance lastFlyingTicks;
   public final LastInstance lastFlyingStatusChange;
   public final LastInstance lastUnderwaterFlyingHack;
   public final LastInstance lastStuckSpeedMultiplier;
   public final LastInstance lastHardCollidingLerpingEntity;
   public final LastInstance lastThirtyMillionHardBorder;
   public final LastInstance lastTeleportTicks;
   public final LastInstance lastPointThree;
   public final LastInstance stuckOnEdge;
   public final LastInstance lastStuckNorth;
   public final LastInstance lastStuckSouth;
   public final LastInstance lastStuckWest;
   public final LastInstance lastStuckEast;
   public final LastInstance lastVehicleSwitch;
   public double lastHorizontalOffset = 0.0D;
   public double lastVerticalOffset = 0.0D;

   public UncertaintyHandler(GrimPlayer player) {
      this.player = player;
      this.lastFlyingTicks = new LastInstance(player);
      this.lastFlyingStatusChange = new LastInstance(player);
      this.lastUnderwaterFlyingHack = new LastInstance(player);
      this.lastStuckSpeedMultiplier = new LastInstance(player);
      this.lastHardCollidingLerpingEntity = new LastInstance(player);
      this.lastThirtyMillionHardBorder = new LastInstance(player);
      this.lastTeleportTicks = new LastInstance(player);
      this.lastPointThree = new LastInstance(player);
      this.stuckOnEdge = new LastInstance(player);
      this.lastStuckNorth = new LastInstance(player);
      this.lastStuckSouth = new LastInstance(player);
      this.lastStuckWest = new LastInstance(player);
      this.lastStuckEast = new LastInstance(player);
      this.lastVehicleSwitch = new LastInstance(player);
      this.tick();
      this.riptideEntities.add(0);
      this.collidingEntities.add(0);
   }

   public void tick() {
      this.pistonX.add(0.0D);
      this.pistonY.add(0.0D);
      this.pistonZ.add(0.0D);
      this.isStepMovement = false;
      this.isSteppingNearShulker = false;
      this.wasSteppingOnBouncyBlock = this.isSteppingOnBouncyBlock;
      this.isSteppingOnSlime = false;
      this.isSteppingOnBouncyBlock = false;
      this.isSteppingOnIce = false;
      this.isSteppingOnHoney = false;
      this.isSteppingNearBubbleColumn = false;
      this.isSteppingNearScaffolding = false;
      this.slimePistonBounces = new HashSet();
      this.tickFireworksBox();
   }

   public boolean wasAffectedByStuckSpeed() {
      return this.lastStuckSpeedMultiplier.hasOccurredSince(5);
   }

   public void tickFireworksBox() {
      this.fishingRodPullBox = this.fishingRodPulls.isEmpty() ? null : new SimpleCollisionBox();
      this.fireworksBox = null;
      Iterator var1 = this.fishingRodPulls.iterator();

      while(var1.hasNext()) {
         int owner = (Integer)var1.next();
         PacketEntity entity = this.player.compensatedEntities.getEntity(owner);
         if (entity != null) {
            SimpleCollisionBox entityBox = entity.getPossibleCollisionBoxes();
            float scale = (float)entity.getAttributeValue(Attributes.SCALE);
            float width = BoundingBoxSize.getWidth(this.player, entity) * scale;
            float height = BoundingBoxSize.getHeight(this.player, entity) * scale;
            entityBox.maxY -= (double)height;
            entityBox.expand((double)(-width / 2.0F), 0.0D, (double)(-width / 2.0F));
            Vector3dm maxLocation = new Vector3dm(entityBox.maxX, entityBox.maxY, entityBox.maxZ);
            Vector3dm minLocation = new Vector3dm(entityBox.minX, entityBox.minY, entityBox.minZ);
            Vector3dm diff = minLocation.subtract(this.player.lastX, this.player.lastY + 1.4400000000000002D, this.player.lastZ).multiply(0.1D);
            this.fishingRodPullBox.minX = Math.min(0.0D, diff.getX());
            this.fishingRodPullBox.minY = Math.min(0.0D, diff.getY());
            this.fishingRodPullBox.minZ = Math.min(0.0D, diff.getZ());
            diff = maxLocation.subtract(this.player.lastX, this.player.lastY + 1.4400000000000002D, this.player.lastZ).multiply(0.1D);
            this.fishingRodPullBox.maxX = Math.max(0.0D, diff.getX());
            this.fishingRodPullBox.maxY = Math.max(0.0D, diff.getY());
            this.fishingRodPullBox.maxZ = Math.max(0.0D, diff.getZ());
         }
      }

      this.fishingRodPulls.clear();
      int maxFireworks = this.player.fireworks.getMaxFireworksAppliedPossible() * 2;
      if (maxFireworks > 0 && (this.player.isGliding || this.player.wasGliding)) {
         this.fireworksBox = new SimpleCollisionBox();
         Vector3dm currentLook = ReachUtils.getLook(this.player, this.player.yaw, this.player.pitch);
         Vector3dm lastLook = ReachUtils.getLook(this.player, this.player.lastYaw, this.player.lastPitch);
         double antiTickSkipping = this.player.isPointThree() ? 0.0D : 0.05D;
         double minX = Math.min(-antiTickSkipping, currentLook.getX()) + Math.min(-antiTickSkipping, lastLook.getX());
         double minY = Math.min(-antiTickSkipping, currentLook.getY()) + Math.min(-antiTickSkipping, lastLook.getY());
         double minZ = Math.min(-antiTickSkipping, currentLook.getZ()) + Math.min(-antiTickSkipping, lastLook.getZ());
         double maxX = Math.max(antiTickSkipping, currentLook.getX()) + Math.max(antiTickSkipping, lastLook.getX());
         double maxY = Math.max(antiTickSkipping, currentLook.getY()) + Math.max(antiTickSkipping, lastLook.getY());
         double maxZ = Math.max(antiTickSkipping, currentLook.getZ()) + Math.max(antiTickSkipping, lastLook.getZ());
         minX *= 1.7D;
         minY *= 1.7D;
         minZ *= 1.7D;
         maxX *= 1.7D;
         maxY *= 1.7D;
         maxZ *= 1.7D;
         minX = Math.max(-1.7D, minX);
         minY = Math.max(-1.7D, minY);
         minZ = Math.max(-1.7D, minZ);
         maxX = Math.min(1.7D, maxX);
         maxY = Math.min(1.7D, maxY);
         maxZ = Math.min(1.7D, maxZ);
         this.fireworksBox = new SimpleCollisionBox(minX, minY, minZ, maxX, maxY, maxZ);
      }
   }

   public double getOffsetHorizontal(VectorData data) {
      double threshold = this.player.getMovementThreshold();
      boolean newVectorPointThree = this.player.couldSkipTick && data.isKnockback() && !data.isSetbackKb(this.player);
      boolean explicit003 = data.isZeroPointZeroThree() || this.lastMovementWasZeroPointZeroThree;
      boolean either003 = newVectorPointThree || explicit003;
      double pointThree = !newVectorPointThree && !this.lastMovementWasUnknown003VectorReset ? 0.0D : threshold;
      if (explicit003) {
         pointThree = 0.546D * threshold * 2.0D + threshold;
      }

      if (either003 && (this.influencedByBouncyBlock() || this.isSteppingOnHoney)) {
         pointThree = 0.7280000000000001D * threshold * 2.0D + threshold;
      }

      if (either003 && this.isSteppingOnIce) {
         pointThree = 0.8999900000000001D * threshold * 2.0D + threshold;
      }

      if (pointThree > threshold) {
         pointThree *= 0.8999900000000001D;
      }

      if (either003 && (this.player.lastOnGround || this.player.isFlying)) {
         pointThree = 0.91D * threshold * 2.0D + threshold;
      }

      if (either003 && (this.player.isGliding || this.player.wasGliding)) {
         pointThree = 0.99D * threshold * 2.0D + threshold;
      }

      if (this.player.uncertaintyHandler.claimingLeftStuckSpeed) {
         pointThree = 0.15D;
      }

      return pointThree;
   }

   public boolean influencedByBouncyBlock() {
      return this.isSteppingOnBouncyBlock || this.wasSteppingOnBouncyBlock;
   }

   public double getVerticalOffset(VectorData data) {
      if (this.player.uncertaintyHandler.claimingLeftStuckSpeed) {
         return 0.06D;
      } else if (this.player.uncertaintyHandler.wasSteppingOnBouncyBlock && (this.player.wasTouchingWater || this.player.wasTouchingLava)) {
         return 0.06D;
      } else if (this.lastFlyingTicks.hasOccurredSince(5) && Math.abs(data.vector.getY()) < 4.5D * (double)this.player.flySpeed - 0.25D) {
         return 0.06D;
      } else {
         double pointThree = this.player.getMovementThreshold();
         if (data.isTrident()) {
            return pointThree * 2.0D;
         } else if (this.player.couldSkipTick && (data.isKnockback() || this.player.isClimbing) && !data.isZeroPointZeroThree()) {
            return pointThree;
         } else if (this.player.pointThreeEstimator.controlsVerticalMovement() && (data.isZeroPointZeroThree() || this.lastMovementWasZeroPointZeroThree)) {
            return pointThree * 2.0D;
         } else {
            return !this.wasZeroPointThreeVertically && !this.player.uncertaintyHandler.onGroundUncertain && !this.player.uncertaintyHandler.lastPacketWasGroundPacket ? 0.0D : pointThree;
         }
      }
   }

   public double reduceOffset(double offset) {
      if (this.player.uncertaintyHandler.lastHardCollidingLerpingEntity.hasOccurredSince(3)) {
         --offset;
      }

      if (this.player.uncertaintyHandler.isOrWasNearGlitchyBlock) {
         offset -= 0.25D;
      }

      if (this.player.uncertaintyHandler.wasAffectedByStuckSpeed() && (!this.player.isPointThree() || this.player.inVehicle())) {
         offset -= 0.01D;
      }

      if (this.player.uncertaintyHandler.influencedByBouncyBlock() && (!this.player.isPointThree() || this.player.inVehicle())) {
         offset -= 0.03D;
      }

      PacketEntity var4 = this.player.compensatedEntities.self.getRiding();
      if (var4 instanceof PacketEntityRideable) {
         PacketEntityRideable vehicle = (PacketEntityRideable)var4;
         if (vehicle.currentBoostTime < vehicle.boostTimeMax + 20) {
            offset -= 0.01D;
         }
      }

      return Math.max(0.0D, offset);
   }

   public void checkForHardCollision() {
      if (this.hasHardCollision()) {
         this.player.uncertaintyHandler.lastHardCollidingLerpingEntity.reset();
      }

   }

   private boolean hasHardCollision() {
      SimpleCollisionBox expandedBB = this.player.boundingBox.copy().expand(1.0D);
      return this.isSteppingNearShulker || this.regularHardCollision(expandedBB) || this.striderCollision(expandedBB) || this.boatCollision(expandedBB);
   }

   private boolean regularHardCollision(SimpleCollisionBox expandedBB) {
      PacketEntity riding = this.player.compensatedEntities.self.getRiding();
      ObjectIterator var3 = this.player.compensatedEntities.entityMap.values().iterator();

      PacketEntity entity;
      do {
         do {
            if (!var3.hasNext()) {
               return false;
            }

            entity = (PacketEntity)var3.next();
         } while(!entity.isBoat && entity.type != EntityTypes.SHULKER && !entity.isHappyGhast);
      } while(entity == riding || !entity.getPossibleCollisionBoxes().isIntersected(expandedBB));

      return true;
   }

   private boolean striderCollision(SimpleCollisionBox expandedBB) {
      if (this.player.compensatedEntities.self.getRiding() instanceof PacketEntityStrider) {
         ObjectIterator var2 = this.player.compensatedEntities.entityMap.values().iterator();

         while(var2.hasNext()) {
            PacketEntity entity = (PacketEntity)var2.next();
            if (entity.type == EntityTypes.STRIDER && entity != this.player.compensatedEntities.self.getRiding() && !entity.hasPassenger(entity) && entity.getPossibleCollisionBoxes().isIntersected(expandedBB)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean boatCollision(SimpleCollisionBox expandedBB) {
      PacketEntity riding = this.player.compensatedEntities.self.getRiding();
      if (riding != null && riding.isBoat) {
         ObjectIterator var3 = this.player.compensatedEntities.entityMap.values().iterator();

         PacketEntity entity;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            entity = (PacketEntity)var3.next();
         } while(entity == riding || !entity.isPushable() || riding.hasPassenger(entity) || !entity.getPossibleCollisionBoxes().isIntersected(expandedBB));

         return true;
      } else {
         return false;
      }
   }
}
