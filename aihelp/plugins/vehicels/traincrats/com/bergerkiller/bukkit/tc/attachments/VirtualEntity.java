package com.bergerkiller.bukkit.tc.attachments;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.AttachmentControllerMember;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.ClientboundMoveMinecartPacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityDestroyHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityMetadataHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityTeleportHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityVelocityHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLivingHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityHandle.PacketPlayOutEntityLookHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityHandle.PacketPlayOutRelEntityMoveHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityHandle.PacketPlayOutRelEntityMoveLookHandle;
import com.bergerkiller.generated.net.minecraft.server.level.EntityTrackerEntryStateHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.vehicle.minecart.NewMinecartBehaviorHandle.LerpStepHandle;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VirtualEntity extends VirtualSpawnableObject {
   public static final double PLAYER_SIT_BUTT_EYE_HEIGHT = 1.0D;
   public static final double PLAYER_STANDING_EYE_HEIGHT = 1.62D;
   public static final double PLAYER_SIT_CHICKEN_BUTT_OFFSET = -0.62D;
   /** @deprecated */
   @Deprecated
   public static final double ARMORSTAND_BUTT_OFFSET = 0.27D;
   private final int entityId;
   private final UUID entityUUID;
   protected DataWatcher metaData;
   private double posX;
   private double posY;
   private double posZ;
   private boolean posSet;
   private final Vector liveAbsPos;
   private final Vector syncAbsPos;
   private final Vector velSyncAbsPos;
   private float liveYaw;
   private float livePitch;
   private float syncYaw;
   private float syncPitch;
   private double liveVel;
   private double syncVel;
   private int lastLiveVelTick;
   private Vector relativePos;
   private VirtualEntity.ByViewerPositionAdjustment byViewerPositionAdjustment;
   private EntityType entityType;
   private boolean entityTypeIsMinecart;
   private boolean respawnOnPitchFlip;
   private int rotateCtr;
   private VirtualEntity.SyncMode syncMode;
   private boolean minecartInterpolation;
   private boolean useParentMetadata;
   private Vector yawPitchRoll;

   public VirtualEntity(AttachmentManager manager) {
      this(manager, EntityUtil.getUniqueEntityId(), UUID.randomUUID());
   }

   public VirtualEntity(AttachmentManager manager, int entityId, UUID entityUUID) {
      super(manager);
      this.lastLiveVelTick = -1;
      this.relativePos = new Vector();
      this.byViewerPositionAdjustment = null;
      this.entityType = EntityType.CHICKEN;
      this.entityTypeIsMinecart = false;
      this.respawnOnPitchFlip = false;
      this.rotateCtr = 0;
      this.syncMode = VirtualEntity.SyncMode.NORMAL;
      this.minecartInterpolation = false;
      this.useParentMetadata = false;
      this.yawPitchRoll = new Vector(0.0D, 0.0D, 0.0D);
      this.entityId = entityId;
      this.entityUUID = entityUUID;
      this.metaData = new DataWatcher();
      this.liveAbsPos = new Vector();
      this.syncAbsPos = new Vector(Double.NaN, Double.NaN, Double.NaN);
      this.velSyncAbsPos = new Vector(Double.NaN, Double.NaN, Double.NaN);
      this.syncVel = 0.0D;
      this.posX = this.posY = this.posZ = 0.0D;
      this.posSet = false;
   }

   public DataWatcher getMetaData() {
      return this.metaData;
   }

   public UUID getEntityUUID() {
      return this.entityUUID;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public boolean containsEntityId(int entityId) {
      return entityId == this.entityId;
   }

   public double getPosX() {
      return this.liveAbsPos.getX();
   }

   public double getPosY() {
      return this.liveAbsPos.getY();
   }

   public double getPosZ() {
      return this.liveAbsPos.getZ();
   }

   public Vector getPos() {
      return this.liveAbsPos;
   }

   public boolean isMountable() {
      return VirtualEntity.VehicleMountRegistry.isMountable(this.entityType);
   }

   public double getMountOffset() {
      return VirtualEntity.VehicleMountRegistry.getOffset(this.entityType);
   }

   public boolean syncPositionIfMounted() {
      return VirtualEntity.VehicleMountRegistry.syncPositionIfMounted(this.entityType);
   }

   public void setRespawnOnPitchFlip(boolean respawn) {
      this.respawnOnPitchFlip = respawn;
   }

   public void setPosition(Vector position) {
      this.posX = position.getX();
      this.posY = position.getY();
      this.posZ = position.getZ();
      this.posSet = true;
   }

   public Vector getRelativeOffset() {
      return this.relativePos;
   }

   public void setRelativeOffset(Vector offset) {
      MathUtil.setVector(this.relativePos, offset);
   }

   public void setRelativeOffset(double dx, double dy, double dz) {
      MathUtil.setVector(this.relativePos, dx, dy, dz);
   }

   public void addRelativeOffset(Vector offset) {
      this.relativePos.add(offset);
   }

   public void addRelativeOffset(double dx, double dy, double dz) {
      MathUtil.addToVector(this.relativePos, dx, dy, dz);
   }

   public void setByViewerPositionAdjustment(VirtualEntity.ByViewerPositionAdjustment adjustment) {
      this.byViewerPositionAdjustment = adjustment;
   }

   public void setSyncMode(VirtualEntity.SyncMode mode) {
      this.syncMode = mode;
      if (mode == VirtualEntity.SyncMode.SEAT) {
         this.livePitch = this.syncPitch = 0.0F;
      }

   }

   public void setUseMinecartInterpolation(boolean use) {
      this.minecartInterpolation = use;
   }

   public Vector getYawPitchRoll() {
      return this.yawPitchRoll;
   }

   public Vector getSyncPos() {
      return this.syncAbsPos;
   }

   public float getLiveYaw() {
      return this.liveYaw;
   }

   public float getLivePitch() {
      return this.livePitch;
   }

   public float getSyncYaw() {
      return this.syncYaw;
   }

   public float getSyncPitch() {
      return this.syncPitch;
   }

   public void updatePosition(Matrix4x4 transform) {
      Quaternion rotation = transform.getRotation();
      Vector f = rotation.forwardVector();
      double yaw;
      double pitch;
      if (this.hasPitch()) {
         Vector u = rotation.upVector();
         double yawmode_factor_start = 0.9D;
         double yawmode_factor_end = 0.99D;
         double yawmode_factor = (Math.abs(f.getY()) - 0.9D) / 0.010000000000000009D;
         boolean isFrontSideDown = f.getY() < 0.0D;
         if (u.getY() < 0.0D) {
            pitch = 180.0D + (double)MathUtil.getLookAtPitch(f.getX(), -f.getY(), f.getZ());
            f.multiply(-1.0D);
         } else {
            pitch = (double)MathUtil.getLookAtPitch(f.getX(), f.getY(), f.getZ());
         }

         if (isFrontSideDown) {
            u.multiply(-1.0D);
         }

         if (yawmode_factor <= 0.0D) {
            yaw = (double)MathUtil.getLookAtYaw(-f.getZ(), f.getX());
         } else if (yawmode_factor >= 1.0D) {
            yaw = (double)MathUtil.getLookAtYaw(u.getZ(), -u.getX());
         } else {
            double ax = yawmode_factor * u.getZ() + (1.0D - yawmode_factor) * -f.getZ();
            double az = yawmode_factor * -u.getX() + (1.0D - yawmode_factor) * f.getX();
            yaw = (double)MathUtil.getLookAtYaw(ax, az);
         }
      } else {
         yaw = (double)MathUtil.getLookAtYaw(-f.getZ(), f.getX());
         pitch = 0.0D;
      }

      this.updatePosition(transform, new Vector(pitch, yaw, 0.0D));
   }

   public void updatePosition(Matrix4x4 transform, Vector yawPitchRoll) {
      if (this.posSet) {
         Vector v = new Vector(this.posX, this.posY, this.posZ);
         transform.transformPoint(v);
         this.updatePosition(v, yawPitchRoll);
      } else {
         this.updatePosition(transform.toVector(), yawPitchRoll);
      }

   }

   public void updatePosition(Vector position, Vector yawPitchRoll) {
      MathUtil.setVector(this.liveAbsPos, position);
      this.liveAbsPos.add(this.relativePos);
      this.yawPitchRoll = yawPitchRoll;
      this.liveYaw = (float)this.yawPitchRoll.getY();
      if (this.syncMode != VirtualEntity.SyncMode.SEAT && this.hasPitch()) {
         this.livePitch = (float)this.yawPitchRoll.getX();
      } else {
         this.livePitch = 0.0F;
      }

      if (this.entityTypeIsMinecart) {
         this.liveYaw -= 90.0F;
      }

      if (Double.isNaN(this.syncAbsPos.getX())) {
         this.syncPositionSilent();
      }

      this.liveVel = this.calcNewVelocity();
   }

   private double calcNewVelocity() {
      if (this.entityTypeIsMinecart && this.manager instanceof AttachmentControllerMember) {
         MinecartMember<?> member = ((AttachmentControllerMember)this.manager).getMember();
         if (member.hasInitializedGroup() && member.getGroup().getProperties().isSoundEnabled() && !member.isDerailed()) {
            int serverTicks = CommonUtil.getServerTicks();
            int elapsedTicks = serverTicks - this.lastLiveVelTick;
            this.lastLiveVelTick = serverTicks;
            if (elapsedTicks == 0) {
               return this.liveVel;
            } else {
               double newLiveVel = this.liveVel;
               if (elapsedTicks <= 20 && !Double.isNaN(this.velSyncAbsPos.getX())) {
                  newLiveVel = this.liveAbsPos.distance(this.velSyncAbsPos);
                  newLiveVel /= (double)elapsedTicks;
               }

               MathUtil.setVector(this.velSyncAbsPos, this.liveAbsPos);
               if (newLiveVel > 1.0D) {
                  newLiveVel = 1.0D;
               }

               if (newLiveVel < 0.001D) {
                  newLiveVel = 0.0D;
               }

               return newLiveVel;
            }
         } else {
            return 0.0D;
         }
      } else {
         return 0.0D;
      }
   }

   public void setEntityType(EntityType entityType) {
      this.entityType = entityType;
      this.entityTypeIsMinecart = isMinecart(entityType);
   }

   public EntityType getEntityType() {
      return this.entityType;
   }

   public boolean isMinecart() {
      return this.entityTypeIsMinecart;
   }

   public boolean isExperimentalMinecart() {
      return this.isMinecart() && this.manager != null && this.manager.getWorldFeatures().MINECART_IMPROVEMENTS;
   }

   public void setUseParentMetadata(boolean use) {
      this.useParentMetadata = use;
   }

   /** @deprecated */
   @Deprecated
   public void addViewerWithoutSpawning(Player viewer) {
      super.addViewerWithoutSpawning(viewer);
   }

   public void addViewerWithoutSpawning(AttachmentViewer viewer) {
      super.addViewerWithoutSpawning(viewer);
   }

   public boolean hasViewers() {
      return super.hasViewers();
   }

   /** @deprecated */
   @Deprecated
   public boolean isViewer(Player viewer) {
      return super.isViewer(viewer);
   }

   public boolean isViewer(AttachmentViewer viewer) {
      return super.isViewer(viewer);
   }

   /** @deprecated */
   @Deprecated
   public final void spawn(Player viewer, Vector motion) {
      super.spawn(viewer, motion);
   }

   public final void spawn(AttachmentViewer viewer, Vector motion) {
      super.spawn(viewer, motion);
   }

   protected void sendSpawnPackets(AttachmentViewer viewer, Vector motion) {
      Vector spawnPos = this.syncAbsPos.clone();
      if (this.byViewerPositionAdjustment != null) {
         this.byViewerPositionAdjustment.adjust(viewer, spawnPos);
      }

      spawnPos.subtract(motion);
      if (this.isLivingEntity()) {
         PacketPlayOutSpawnEntityLivingHandle spawnPacket = PacketPlayOutSpawnEntityLivingHandle.createNew();
         spawnPacket.setEntityId(this.entityId);
         spawnPacket.setEntityUUID(this.entityUUID);
         spawnPacket.setEntityType(this.entityType);
         spawnPacket.setPosX(spawnPos.getX());
         spawnPacket.setPosY(spawnPos.getY());
         spawnPacket.setPosZ(spawnPos.getZ());
         spawnPacket.setMotX(motion.getX());
         spawnPacket.setMotY(motion.getY());
         spawnPacket.setMotZ(motion.getZ());
         spawnPacket.setYaw(this.syncYaw);
         spawnPacket.setPitch(this.syncPitch);
         spawnPacket.setHeadYaw(this.syncMode == VirtualEntity.SyncMode.ITEM ? 0.0F : this.syncYaw);
         viewer.sendEntityLivingSpawnPacket(spawnPacket, this.getUsedMeta());
      } else {
         PacketPlayOutSpawnEntityHandle spawnPacket = PacketPlayOutSpawnEntityHandle.createNew();
         spawnPacket.setEntityId(this.entityId);
         spawnPacket.setEntityUUID(this.entityUUID);
         spawnPacket.setEntityType(this.entityType);
         spawnPacket.setPosX(spawnPos.getX());
         spawnPacket.setPosY(spawnPos.getY());
         spawnPacket.setPosZ(spawnPos.getZ());
         spawnPacket.setMotX(motion.getX());
         spawnPacket.setMotY(motion.getY());
         spawnPacket.setMotZ(motion.getZ());
         if (this.isExperimentalMinecart()) {
            spawnPacket.setYaw(180.0F - this.syncYaw);
            spawnPacket.setPitch(this.syncPitch);
         } else {
            spawnPacket.setYaw(this.syncYaw);
            spawnPacket.setPitch(this.syncPitch);
         }

         viewer.send((PacketHandle)spawnPacket);
         PacketPlayOutEntityMetadataHandle metaPacket = PacketPlayOutEntityMetadataHandle.createNew(this.entityId, this.getUsedMeta(), true);
         viewer.send(metaPacket.toCommonPacket());
      }

      if (this.syncMode == VirtualEntity.SyncMode.SEAT) {
         PacketPlayOutRelEntityMoveLookHandle movePacket = PacketPlayOutRelEntityMoveLookHandle.createNew(this.entityId, motion.getX(), motion.getY(), motion.getZ(), this.syncYaw, this.syncPitch, false);
         viewer.send((PacketHandle)movePacket);
      } else if (motion.lengthSquared() > 0.001D) {
         CommonPacket movePacket = PacketType.OUT_ENTITY_MOVE.newInstance(this.entityId, motion.getX(), motion.getY(), motion.getZ(), false);
         viewer.send(movePacket);
      }

      if (this.syncVel > 0.0D) {
         viewer.send((PacketHandle)PacketPlayOutEntityVelocityHandle.createNew(this.entityId, this.syncVel, 0.0D, 0.0D));
      }

   }

   protected void applyGlowing(ChatColor color) {
      this.getMetaData().setFlag(EntityHandle.DATA_FLAGS, 64, color != null);
      this.syncMetadata();
   }

   protected void applyGlowColorForViewer(AttachmentViewer viewer, ChatColor color) {
      viewer.updateGlowColor(this.entityUUID, color);
   }

   public void syncMetadata() {
      DataWatcher metaData = this.getUsedMeta();
      if (metaData.isChanged()) {
         this.broadcast((PacketHandle)PacketPlayOutEntityMetadataHandle.createNew(this.entityId, metaData, false));
      }

   }

   public void syncPositionSilent() {
      MathUtil.setVector(this.syncAbsPos, this.liveAbsPos);
      this.syncYaw = this.liveYaw;
      this.syncPitch = this.livePitch;
      this.syncVel = this.liveVel;
   }

   public void syncPosition(boolean absolute) {
      if (!this.hasViewers()) {
         this.syncPositionSilent();
      } else {
         if (Math.abs(this.liveVel - this.syncVel) > 0.01D || this.syncVel > 0.0D && this.liveVel == 0.0D) {
            this.syncVel = this.liveVel;
            this.broadcast((PacketHandle)PacketPlayOutEntityVelocityHandle.createNew(this.entityId, this.syncVel, 0.0D, 0.0D));
         }

         this.syncMetadata();
         double dx = this.liveAbsPos.getX() - this.syncAbsPos.getX();
         double dy = this.liveAbsPos.getY() - this.syncAbsPos.getY();
         double dz = this.liveAbsPos.getZ() - this.syncAbsPos.getZ();
         double abs_delta = Math.max(Math.max(Math.abs(dx), Math.abs(dy)), Math.abs(dz));
         boolean largeChange = abs_delta > 8.0D;
         boolean moved;
         if (this.isExperimentalMinecart()) {
            moved = MathUtil.getAngleDifference(this.syncPitch, 180.0F) < 90.0F || MathUtil.getAngleDifference(this.livePitch, 180.0F) < 90.0F;
            List<LerpStepHandle> steps = new ArrayList(2);
            if (moved) {
               steps.add(LerpStepHandle.createNew(this.syncAbsPos, new Vector(), 180.0F - this.syncYaw, this.syncPitch, 0.0F));
            }

            steps.add(LerpStepHandle.createNew(this.liveAbsPos, new Vector(dx, dy, dz), 180.0F - this.liveYaw, this.livePitch, 1.0F));
            ClientboundMoveMinecartPacketHandle p = ClientboundMoveMinecartPacketHandle.createNew(this.entityId, steps);
            this.broadcast((PacketHandle)p);
            MathUtil.setVector(this.syncAbsPos, this.liveAbsPos);
            this.syncYaw = this.liveYaw;
            this.syncPitch = this.livePitch;
         } else if (this.respawnOnPitchFlip && this.syncPitch != this.livePitch && Util.isProtocolRotationGlitched(this.syncPitch, this.livePitch)) {
            this.forAllViewers(this::sendDestroyPacketsWithoutVMC);
            this.syncPositionSilent();
            Iterator var18 = this.getViewers().iterator();

            while(var18.hasNext()) {
               AttachmentViewer viewer = (AttachmentViewer)var18.next();
               this.sendSpawnPackets(viewer, largeChange ? new Vector() : new Vector(dx, dy, dz));
            }

         } else if (!absolute && !largeChange) {
            moved = abs_delta >= 2.44140625E-4D;
            boolean rotatedNow = EntityTrackerEntryStateHandle.hasProtocolRotationChanged(this.liveYaw, this.syncYaw) || EntityTrackerEntryStateHandle.hasProtocolRotationChanged(this.livePitch, this.syncPitch);
            boolean rotated = false;
            if (rotatedNow) {
               this.forceSyncRotation();
               rotated = true;
            } else if (this.rotateCtr > 0) {
               --this.rotateCtr;
               rotated = true;
            }

            if (rotatedNow) {
               this.refreshHeadRotation();
            }

            if (this.minecartInterpolation) {
               double FACTOR = 0.6D;
               dx *= 0.6D;
               dy *= 0.6D;
               dz *= 0.6D;
            }

            if (moved && rotated) {
               PacketPlayOutRelEntityMoveLookHandle packet = PacketPlayOutRelEntityMoveLookHandle.createNew(this.entityId, dx, dy, dz, this.liveYaw, this.livePitch, false);
               this.syncYaw = packet.getYaw();
               this.syncPitch = packet.getPitch();
               MathUtil.addToVector(this.syncAbsPos, packet.getDeltaX(), packet.getDeltaY(), packet.getDeltaZ());
               this.broadcast((PacketHandle)packet);
            } else if (moved) {
               PacketPlayOutRelEntityMoveHandle packet = PacketPlayOutRelEntityMoveHandle.createNew(this.entityId, dx, dy, dz, false);
               MathUtil.addToVector(this.syncAbsPos, packet.getDeltaX(), packet.getDeltaY(), packet.getDeltaZ());
               this.broadcast((PacketHandle)packet);
            } else if (rotated) {
               Iterator var25 = this.getViewers().iterator();

               while(var25.hasNext()) {
                  AttachmentViewer viewer = (AttachmentViewer)var25.next();
                  if (viewer.evaluateGameVersion(">=", "1.15")) {
                     PacketPlayOutRelEntityMoveLookHandle packet = PacketPlayOutRelEntityMoveLookHandle.createNew(this.entityId, 0.0D, 0.0D, 0.0D, this.liveYaw, this.livePitch, false);
                     viewer.send((PacketHandle)packet);
                     this.syncYaw = packet.getYaw();
                     this.syncPitch = packet.getPitch();
                  } else {
                     PacketPlayOutEntityLookHandle packet = PacketPlayOutEntityLookHandle.createNew(this.entityId, this.liveYaw, this.livePitch, false);
                     viewer.send((PacketHandle)packet);
                     this.syncYaw = packet.getYaw();
                     this.syncPitch = packet.getPitch();
                  }
               }
            }

         } else {
            if (this.byViewerPositionAdjustment != null) {
               Vector pos = new Vector();
               Iterator var12 = this.getViewers().iterator();

               while(var12.hasNext()) {
                  AttachmentViewer viewer = (AttachmentViewer)var12.next();
                  MathUtil.setVector(pos, this.liveAbsPos);
                  this.byViewerPositionAdjustment.adjust(viewer, pos);
                  viewer.send((PacketHandle)PacketPlayOutEntityTeleportHandle.createNew(this.entityId, pos.getX(), pos.getY(), pos.getZ(), this.liveYaw, this.livePitch, false));
               }
            } else {
               this.broadcast((PacketHandle)PacketPlayOutEntityTeleportHandle.createNew(this.entityId, this.liveAbsPos.getX(), this.liveAbsPos.getY(), this.liveAbsPos.getZ(), this.liveYaw, this.livePitch, false));
            }

            this.syncPositionSilent();
            this.refreshHeadRotation();
         }
      }
   }

   public void forceSyncRotation() {
      this.rotateCtr = 14;
   }

   private void refreshHeadRotation() {
      if (this.syncMode.isNormal() && this.isLivingEntity()) {
         CommonPacket packet = PacketType.OUT_ENTITY_HEAD_ROTATION.newInstance();
         packet.write(PacketType.OUT_ENTITY_HEAD_ROTATION.entityId, this.entityId);
         packet.write(PacketType.OUT_ENTITY_HEAD_ROTATION.headYaw, this.liveYaw);
         this.broadcast(packet);
      }

   }

   public static boolean isLivingEntity(EntityType entityType) {
      Class<?> entityClass = entityType.getEntityClass();
      return entityClass != null && LivingEntity.class.isAssignableFrom(entityClass);
   }

   private boolean isLivingEntity() {
      return isLivingEntity(this.entityType);
   }

   public void respawnForAll(Vector motion) {
      this.forAllViewers(this::sendDestroyPacketsWithoutVMC);
      this.syncPosition(true);
      this.forAllViewers((v) -> {
         this.sendSpawnPackets(v, motion);
      });
   }

   public void destroyForAll() {
      super.destroyForAll();
   }

   /** @deprecated */
   @Deprecated
   public void destroy(Player viewer) {
      super.destroy(viewer);
   }

   public void destroy(AttachmentViewer viewer) {
      super.destroy(viewer);
   }

   protected void sendDestroyPackets(AttachmentViewer viewer) {
      this.sendDestroyPacketsWithoutVMC(viewer);
      viewer.getVehicleMountController().remove(this.entityId);
   }

   private void sendDestroyPacketsWithoutVMC(AttachmentViewer viewer) {
      if (this.syncVel > 0.0D) {
         viewer.send(PacketType.OUT_ENTITY_VELOCITY.newInstance(this.entityId, new Vector()));
      }

      PacketPlayOutEntityDestroyHandle destroyPacket = PacketPlayOutEntityDestroyHandle.createNewSingle(this.entityId);
      viewer.send((PacketHandle)destroyPacket);
   }

   public void broadcast(CommonPacket packet) {
      super.broadcast(packet);
   }

   public void broadcast(PacketHandle packet) {
      super.broadcast(packet);
   }

   private DataWatcher getUsedMeta() {
      return this.useParentMetadata && this.manager instanceof AttachmentControllerMember ? ((CommonMinecart)((AttachmentControllerMember)this.manager).getMember().getEntity()).getMetaData() : this.metaData;
   }

   private boolean hasPitch() {
      return this.entityTypeIsMinecart || this.isLivingEntity();
   }

   public static boolean isMinecart(EntityType entityType) {
      switch(entityType) {
      case MINECART:
      case MINECART_CHEST:
      case MINECART_FURNACE:
      case MINECART_TNT:
      case MINECART_COMMAND:
      case MINECART_MOB_SPAWNER:
      case MINECART_HOPPER:
         return true;
      default:
         return false;
      }
   }

   @FunctionalInterface
   public interface ByViewerPositionAdjustment {
      void adjust(AttachmentViewer var1, Vector var2);
   }

   public static enum SyncMode {
      ITEM(false),
      NORMAL(true),
      SEAT(false);

      private final boolean _normal;

      private SyncMode(boolean normal) {
         this._normal = normal;
      }

      public boolean isNormal() {
         return this._normal;
      }

      // $FF: synthetic method
      private static VirtualEntity.SyncMode[] $values() {
         return new VirtualEntity.SyncMode[]{ITEM, NORMAL, SEAT};
      }
   }

   private static class VehicleMountRegistry {
      private static final Map<EntityType, Double> _lookup = new EnumMap(EntityType.class);
      private static final Set<EntityType> _unmountable = EnumSet.noneOf(EntityType.class);
      private static final Set<EntityType> _noPositionSyncIfMounted = EnumSet.noneOf(EntityType.class);
      private static final Double DEFAULT_OFFSET = 1.0D;

      public static double getOffset(EntityType type) {
         return (Double)_lookup.getOrDefault(type, DEFAULT_OFFSET);
      }

      public static boolean isMountable(EntityType type) {
         return !_unmountable.contains(type);
      }

      public static boolean syncPositionIfMounted(EntityType type) {
         return !_noPositionSyncIfMounted.contains(type);
      }

      private static void register(Predicate<EntityType> condition, double offset) {
         register(condition, offset, true, true);
      }

      private static void register(Predicate<EntityType> condition, double offset, boolean mountable, boolean syncPosition) {
         Stream.of(EntityType.values()).filter(condition).forEachOrdered((type) -> {
            _lookup.put(type, offset);
            if (!mountable) {
               _unmountable.add(type);
            }

            if (!syncPosition) {
               _noPositionSyncIfMounted.add(type);
            }

         });
      }

      private static void register(String name, double offset) {
         register(name, offset, true, true);
      }

      private static void register(String name, double offset, boolean mountable, boolean syncPosition) {
         try {
            EntityType type = EntityType.valueOf(name);
            _lookup.put(type, offset);
            if (!mountable) {
               _unmountable.add(type);
            }

            if (!syncPosition) {
               _noPositionSyncIfMounted.add(type);
            }
         } catch (IllegalArgumentException var6) {
         }

      }

      static {
         register("AXOLOTL", 0.59D);
         register("BAT", 1.0D);
         register("BEE", 0.7D);
         register("BLAZE", 1.6D);
         register("BOAT", 0.2D, false, true);
         register("CAT", 0.8D);
         register("CAVE_SPIDER", 0.5D);
         register("CHICKEN", 0.62D);
         register("COD", 0.5D);
         register("COW", 1.3D);
         register("CREEPER", 1.55D);
         register("DOLPHIN", 0.75D);
         register("DONKEY", 1.15D);
         register("DROWNED", 1.75D);
         register("ENDERMAN", 2.45D);
         register("ENDERMITE", 0.5D);
         register("ENDER_DRAGON", 3.4D, false, true);
         register("EVOKER", 1.75D);
         register("FALLING_BLOCK", 1.0D);
         register("FOX", 0.8D);
         register("GHAST", 4.0D, false, true);
         register("GIANT", 12.0D, false, true);
         register("GLOW_SQUID", 0.9D);
         register("GOAT", 1.25D);
         register("GUARDIAN", 0.92D);
         register("HOGLIN", 1.5D);
         register("HORSE", 1.4D, false, true);
         register("HUSK", 1.75D);
         register("ILLUSIONER", 1.75D);
         register("IRON_GOLEM", 2.3D);
         register("LEASH_HITCH", 0.97D);
         register("LLAMA", 1.37D, false, true);
         register((e) -> {
            return e.name().contains("MINECART");
         }, 0.27D);
         register("MULE", 1.22D);
         register("MUSHROOM_COW", 1.3D);
         register("OCELOT", 0.8D);
         register("PANDA", 1.2D);
         register("PARROT", 0.96D);
         register("PHANTOM", 0.67D);
         register("PIG", 0.965D);
         register("PIGLIN", 2.05D);
         register("PIGLIN_BRUTE", 1.75D);
         register("PILLAGER", 1.75D);
         register("POLAR_BEAR", 1.305D);
         register("PRIMED_TNT", 1.0D);
         register("PUFFERFISH", 0.55D);
         register("RABBIT", 0.63D);
         register("RAVAGER", 2.4D);
         register("SALMON", 0.58D);
         register("SHEEP", 1.25D);
         register("SHULKER", 1.0D, false, false);
         register("SHULKER_BULLET", 0.52D);
         register("SILVERFISH", 0.51D);
         register("SKELETON", 1.75D);
         register("SKELETON_HORSE", 1.3D, false, true);
         register("SMALL_FIREBALL", 0.52D);
         register("SNOWMAN", 1.67D);
         register("SPIDER", 0.73D);
         register("SQUID", 0.88D);
         register("STRAY", 1.775D);
         register("STRIDER", 1.79D, false, true);
         register("TRADER_LLAMA", 1.36D);
         register("TURTLE", 0.58D);
         register("VEX", 0.88D);
         register("VILLAGER", 1.75D);
         register("VINDICATOR", 1.75D);
         register("WANDERING_TRADER", 1.75D);
         register("WITCH", 1.75D);
         register("WITHER", 3.5D, false, true);
         register("WITHER_SKELETON", 2.07D);
         register("WITHER_SKULL", 0.52D);
         register("WOLF", 0.92D);
         register("ZOGLIN", 1.53D);
         register("ZOMBIE", 1.75D);
         register("ZOMBIE_HORSE", 1.47D);
         register("ZOMBIE_VILLAGER", 1.75D);
         register("ZOMBIFIED_PIGLIN", 1.75D);
         register("ZOMBIE_VILLAGER", 1.75D);
      }
   }
}
