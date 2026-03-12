package ac.grim.grimac.manager;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsN;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.platform.api.entity.GrimEntity;
import ac.grim.grimac.platform.api.world.PlatformWorld;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngine;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineElytra;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineNormal;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineWater;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.teleport.RelativeFlag;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerAttachEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.chunks.Column;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.data.SetBackData;
import ac.grim.grimac.utils.data.TeleportAcceptData;
import ac.grim.grimac.utils.data.TeleportData;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.VelocityData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Location;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.math.VectorUtils;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import ac.grim.grimac.utils.nmsutil.ReachUtils;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Generated;

public class SetbackTeleportUtil extends Check implements PostPredictionCheck {
   public final ConcurrentLinkedQueue<TeleportData> pendingTeleports = new ConcurrentLinkedQueue();
   private final Random random = new Random();
   public boolean hasAcceptedSpawnTeleport = false;
   public boolean blockOffsets = false;
   public SetbackTeleportUtil.SetbackPosWithVector lastKnownGoodPosition;
   public boolean isSendingSetback = false;
   public int cheatVehicleInterpolationDelay = 0;
   private SetBackData requiredSetBack = null;
   private long lastWorldResync = 0L;

   public SetbackTeleportUtil(GrimPlayer player) {
      super(player);
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      Vector3dm afterTickFriction = this.player.clientVelocity.clone();
      if (predictionComplete.getData().getSetback() != null) {
         if (this.cheatVehicleInterpolationDelay > 0) {
            this.cheatVehicleInterpolationDelay = 10;
         }

         this.lastKnownGoodPosition = new SetbackTeleportUtil.SetbackPosWithVector(new Vector3d(this.player.x, this.player.y, this.player.z), afterTickFriction);
      } else if (this.requiredSetBack == null || this.requiredSetBack.isComplete()) {
         --this.cheatVehicleInterpolationDelay;
         this.lastKnownGoodPosition = new SetbackTeleportUtil.SetbackPosWithVector(new Vector3d(this.player.x, this.player.y, this.player.z), afterTickFriction);
      }

      if (this.requiredSetBack != null) {
         this.requiredSetBack.tick();
      }

   }

   public void executeForceResync() {
      if (this.player.gamemode != GameMode.SPECTATOR && !this.player.disableGrim) {
         if (this.lastKnownGoodPosition != null) {
            this.blockMovementsUntilResync(true, true);
         }
      }
   }

   public void executeNonSimulatingForceResync() {
      if (this.player.gamemode != GameMode.SPECTATOR && !this.player.disableGrim) {
         if (this.lastKnownGoodPosition != null) {
            this.blockMovementsUntilResync(false, true);
         }
      }
   }

   public void executeNonSimulatingSetback() {
      if (this.player.gamemode != GameMode.SPECTATOR && !this.player.disableGrim) {
         if (this.lastKnownGoodPosition != null) {
            this.blockMovementsUntilResync(false, false);
         }
      }
   }

   public boolean executeViolationSetback() {
      if (this.isExempt()) {
         return false;
      } else {
         this.blockMovementsUntilResync(true, false);
         return true;
      }
   }

   private boolean isExempt() {
      if (this.lastKnownGoodPosition == null) {
         return true;
      } else if (this.player.disableGrim) {
         return true;
      } else {
         return this.player.platformPlayer != null && this.player.noSetbackPermission;
      }
   }

   private void simulateFriction(Vector3dm vector) {
      if (this.player.wasTouchingWater) {
         PredictionEngineWater.staticVectorEndOfTick(this.player, vector, 0.8F, this.player.gravity, true);
      } else if (this.player.wasTouchingLava) {
         vector.multiply(0.5D);
         if (this.player.hasGravity) {
            vector.add(0.0D, -this.player.gravity / 4.0D, 0.0D);
         }
      } else if (this.player.isGliding) {
         PredictionEngineElytra.getElytraMovement(this.player, vector, ReachUtils.getLook(this.player, this.player.yaw, this.player.pitch)).multiply(this.player.stuckSpeedMultiplier).multiply(0.9900000095367432D, 0.9800000190734863D, 0.9900000095367432D);
         vector.setY(vector.getY() - 0.05D);
      } else {
         PredictionEngineNormal.staticVectorEndOfTick(this.player, vector);
      }

      vector.multiply(this.player.stuckSpeedMultiplier);
      (new PredictionEngine()).applyMovementThreshold(this.player, new HashSet(Collections.singletonList(new VectorData(vector, VectorData.VectorType.BestVelPicked))));
   }

   private void blockMovementsUntilResync(boolean simulateNextTickPosition, boolean isResync) {
      if (this.requiredSetBack != null) {
         if (this.player.platformPlayer == null || !this.player.noSetbackPermission) {
            this.requiredSetBack.setPlugin(false);
            if (!this.isPendingSetback()) {
               if (System.currentTimeMillis() - this.lastWorldResync > 5000L) {
                  this.player.resyncPositions(this.player.boundingBox.copy().expand(1.0D));
                  this.lastWorldResync = System.currentTimeMillis();
               }

               Vector3dm clientVel = this.lastKnownGoodPosition.vector.clone();
               Pair<VelocityData, Vector3dm> futureKb = this.player.checkManager.getKnockbackHandler().getFutureKnockback();
               VelocityData futureExplosion = this.player.checkManager.getExplosionHandler().getFutureExplosion();
               if (futureKb.first() != null && !((VelocityData)futureKb.first()).isSetback) {
                  clientVel = (Vector3dm)futureKb.second();
               }

               if (futureExplosion != null && (futureKb.first() == null || ((VelocityData)futureKb.first()).transaction < futureExplosion.transaction && !((VelocityData)futureKb.first()).isSetback)) {
                  clientVel.add(futureExplosion.vector);
               }

               Vector3d position = this.lastKnownGoodPosition.pos;
               SimpleCollisionBox oldBB = this.player.boundingBox;
               this.player.boundingBox = GetBoundingBox.getPlayerBoundingBox(this.player, position.getX(), position.getY(), position.getZ());
               if (simulateNextTickPosition) {
                  Vector3dm collide = Collisions.collide(this.player, clientVel.getX(), clientVel.getY(), clientVel.getZ());
                  position = position.withX(position.getX() + collide.getX());
                  position = position.withY(position.getY() + collide.getY());
                  if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_9)) {
                     position = position.withY(position.getY() + 1.0E-7D);
                  }

                  position = position.withZ(position.getZ() + collide.getZ());
                  if (clientVel.getX() != collide.getX()) {
                     clientVel.setX(0);
                  }

                  if (clientVel.getY() != collide.getY()) {
                     clientVel.setY(0);
                  }

                  if (clientVel.getZ() != collide.getZ()) {
                     clientVel.setZ(0);
                  }

                  this.simulateFriction(clientVel);
               }

               this.player.boundingBox = oldBB;
               if (!this.hasAcceptedSpawnTeleport || this.player.isFlying) {
                  clientVel = null;
               }

               if (isResync) {
                  this.blockOffsets = true;
               }

               SetBackData data = new SetBackData(new TeleportData(position, (Vector3d)null, RelativeFlag.YAW.or(RelativeFlag.PITCH), this.player.lastTransactionSent.get(), 0), this.player.yaw, this.player.pitch, clientVel, this.player.inVehicle(), false);
               this.sendSetback(data);
            }
         }
      }
   }

   private void sendSetback(SetBackData data) {
      this.isSendingSetback = true;
      Vector3d position = data.getTeleportData().getLocation();

      try {
         if (this.player.inVehicle()) {
            int vehicleId = this.player.getRidingVehicleId();
            if (this.player.compensatedEntities.serverPlayerVehicle != null) {
               if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
                  this.player.user.sendPacket((PacketWrapper)(new WrapperPlayServerSetPassengers(vehicleId, new int[2])));
               } else {
                  this.player.user.sendPacket((PacketWrapper)(new WrapperPlayServerAttachEntity(vehicleId, -1, false)));
               }

               this.player.user.sendPacket((PacketWrapper)(new WrapperPlayServerEntityTeleport(vehicleId, new Vector3d(position.getX(), position.getY(), position.getZ()), this.player.yaw % 360.0F, 0.0F, false)));
               this.player.getSetbackTeleportUtil().cheatVehicleInterpolationDelay = Integer.MAX_VALUE;
               GrimAPI.INSTANCE.getScheduler().getEntityScheduler().execute(this.player.platformPlayer, GrimAPI.INSTANCE.getGrimPlugin(), () -> {
                  if (this.player.platformPlayer != null) {
                     GrimEntity vehicle = this.player.platformPlayer.getVehicle();
                     if (vehicle != null) {
                        vehicle.eject();
                     }
                  }

               }, (Runnable)null, 0L);
            }
         }

         double y = position.getY();
         if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            ++y;
         }

         this.player.sendTransaction();
         int teleportId = this.random.nextInt() | Integer.MIN_VALUE;
         data.setPlugin(false);
         data.getTeleportData().setTeleportId(teleportId);
         data.getTeleportData().setTransaction(this.player.lastTransactionSent.get());
         this.addSentTeleport(new Location((PlatformWorld)null, position.getX(), y, position.getZ(), this.player.yaw % 360.0F, this.player.pitch % 360.0F), (Vector3d)null, data.getTeleportData().getTransaction(), RelativeFlag.YAW.or(RelativeFlag.PITCH), false, teleportId);
         this.requiredSetBack = data;
         PacketEvents.getAPI().getProtocolManager().sendPacketSilently(this.player.user.getChannel(), (PacketWrapper)(new WrapperPlayServerPlayerPositionAndLook(position.getX(), position.getY(), position.getZ(), 0.0F, 0.0F, data.getTeleportData().getFlags().getMask(), teleportId, false)));
         this.player.sendTransaction();
         if (data.getVelocity() != null && data.getVelocity().lengthSquared() > 0.0D) {
            this.player.user.sendPacket((PacketWrapper)(new WrapperPlayServerEntityVelocity(this.player.entityID, new Vector3d(data.getVelocity().getX(), data.getVelocity().getY(), data.getVelocity().getZ()))));
         }
      } finally {
         this.isSendingSetback = false;
      }

   }

   public TeleportAcceptData checkTeleportQueue(double x, double y, double z) {
      TeleportAcceptData teleportData = new TeleportAcceptData();

      TeleportData teleportPos;
      while((teleportPos = (TeleportData)this.pendingTeleports.peek()) != null) {
         double trueTeleportX = (teleportPos.isRelativeX() ? this.player.x : 0.0D) + teleportPos.getLocation().getX();
         double trueTeleportY = (teleportPos.isRelativeY() ? this.player.y : 0.0D) + teleportPos.getLocation().getY();
         double trueTeleportZ = (teleportPos.isRelativeZ() ? this.player.z : 0.0D) + teleportPos.getLocation().getZ();
         Vector3d clamped = VectorUtils.clampVector(new Vector3d(trueTeleportX, trueTeleportY, trueTeleportZ));
         double threshold = teleportPos.isRelativePos() ? this.player.getMovementThreshold() : 0.0D;
         boolean closeEnoughY = Math.abs(clamped.getY() - y) <= 1.0E-7D + threshold;
         if (this.player.lastTransactionReceived.get() == teleportPos.getTransaction() && Math.abs(clamped.getX() - x) <= threshold && closeEnoughY && Math.abs(clamped.getZ() - z) <= threshold) {
            this.pendingTeleports.poll();
            this.hasAcceptedSpawnTeleport = true;
            this.blockOffsets = false;
            if (this.requiredSetBack != null && this.requiredSetBack.getTeleportData().getTransaction() == teleportPos.getTransaction()) {
               teleportData.setSetback(this.requiredSetBack);
               this.requiredSetBack.setComplete(true);
            }

            teleportData.setTeleportData(teleportPos);
            teleportData.setTeleport(true);
            break;
         }

         if (this.player.lastTransactionReceived.get() <= teleportPos.getTransaction()) {
            break;
         }

         ((BadPacketsN)this.player.checkManager.getCheck(BadPacketsN.class)).flagAndAlert();
         this.pendingTeleports.poll();
         this.requiredSetBack.setPlugin(false);
         if (this.pendingTeleports.isEmpty()) {
            this.sendSetback(this.requiredSetBack);
         }
      }

      return teleportData;
   }

   public boolean checkVehicleTeleportQueue(double x, double y, double z) {
      int lastTransaction = this.player.lastTransactionReceived.get();

      while(true) {
         Pair<Integer, Vector3d> teleportPos = (Pair)this.player.vehicleData.vehicleTeleports.peek();
         if (teleportPos == null || lastTransaction < (Integer)teleportPos.first()) {
            break;
         }

         Vector3d position = (Vector3d)teleportPos.second();
         if (position.getX() == x && position.getY() == y && position.getZ() == z) {
            this.player.vehicleData.vehicleTeleports.poll();
            return true;
         }

         if (lastTransaction <= (Integer)teleportPos.first() + 1) {
            break;
         }

         this.player.vehicleData.vehicleTeleports.poll();
      }

      return false;
   }

   public boolean shouldBlockMovement() {
      return this.insideUnloadedChunk() || this.blockOffsets || this.requiredSetBack != null && !this.requiredSetBack.isComplete();
   }

   private boolean isPendingSetback() {
      if (this.requiredSetBack != null && (this.requiredSetBack.getTeleportData().isRelativeX() || this.requiredSetBack.getTeleportData().isRelativeY() || this.requiredSetBack.getTeleportData().isRelativeZ())) {
         return false;
      } else {
         return this.requiredSetBack != null && !this.requiredSetBack.isComplete();
      }
   }

   public boolean insideUnloadedChunk() {
      Column column = this.player.compensatedWorld.getChunk(GrimMath.floor(this.player.x) >> 4, GrimMath.floor(this.player.z) >> 4);
      return !this.player.disableGrim && (column == null || column.transaction() >= this.player.lastTransactionReceived.get() || !this.player.getSetbackTeleportUtil().hasAcceptedSpawnTeleport);
   }

   public void addSentTeleport(Location position, @Nullable Vector3d velocity, int transaction, RelativeFlag flags, boolean plugin, int teleportId) {
      if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_21_2)) {
         velocity = null;
      }

      TeleportData data = new TeleportData(new Vector3d(position.getX(), position.getY(), position.getZ()), velocity, flags, transaction, teleportId);
      this.pendingTeleports.add(data);
      Vector3d safePosition = new Vector3d(position.getX(), position.getY(), position.getZ());
      if (flags.has(RelativeFlag.X)) {
         safePosition = safePosition.withX(safePosition.getX() + this.lastKnownGoodPosition.pos.getX());
      }

      if (flags.has(RelativeFlag.Y)) {
         safePosition = safePosition.withY(safePosition.getY() + this.lastKnownGoodPosition.pos.getY());
      }

      if (flags.has(RelativeFlag.Z)) {
         safePosition = safePosition.withZ(safePosition.getZ() + this.lastKnownGoodPosition.pos.getZ());
      }

      data = new TeleportData(safePosition, velocity, RelativeFlag.YAW.or(RelativeFlag.PITCH), transaction, teleportId);
      this.requiredSetBack = new SetBackData(data, this.player.yaw, this.player.pitch, (Vector3dm)null, false, plugin);
      this.lastKnownGoodPosition = new SetbackTeleportUtil.SetbackPosWithVector(safePosition, new Vector3dm());
   }

   @Generated
   public SetBackData getRequiredSetBack() {
      return this.requiredSetBack;
   }

   public static class SetbackPosWithVector {
      private final Vector3d pos;
      private Vector3dm vector;

      @Generated
      public SetbackPosWithVector(Vector3d pos, Vector3dm vector) {
         this.pos = pos;
         this.vector = vector;
      }

      @Generated
      public Vector3d getPos() {
         return this.pos;
      }

      @Generated
      public Vector3dm getVector() {
         return this.vector;
      }

      @Generated
      public void setVector(Vector3dm vector) {
         this.vector = vector;
      }
   }
}
