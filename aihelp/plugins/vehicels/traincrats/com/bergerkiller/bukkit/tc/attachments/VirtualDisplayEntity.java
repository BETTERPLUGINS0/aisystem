package com.bergerkiller.bukkit.tc.attachments;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.math.Vector3;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.Brightness;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher.Prototype;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityDestroyHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityMetadataHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityTeleportHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLivingHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityHandle.PacketPlayOutRelEntityMoveHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.DisplayHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.decoration.EntityArmorStandHandle;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.function.IntFunction;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

public abstract class VirtualDisplayEntity extends VirtualSpawnableObject {
   public static final EntityType BLOCK_DISPLAY_ENTITY_TYPE = (EntityType)LogicUtil.tryMake(() -> {
      return EntityType.valueOf("BLOCK_DISPLAY");
   }, (Object)null);
   public static final EntityType ITEM_DISPLAY_ENTITY_TYPE = (EntityType)LogicUtil.tryMake(() -> {
      return EntityType.valueOf("ITEM_DISPLAY");
   }, (Object)null);
   public static final EntityType TEXT_DISPLAY_ENTITY_TYPE = (EntityType)LogicUtil.tryMake(() -> {
      return EntityType.valueOf("TEXT_DISPLAY");
   }, (Object)null);
   public static final EntityType INTERACTION_ENTITY_TYPE = (EntityType)LogicUtil.tryMake(() -> {
      return EntityType.valueOf("INTERACTION");
   }, (Object)null);
   public static final double BBOX_FACT = 1.41421356274619D;
   public static final DataWatcher ARMORSTAND_MOUNT_METADATA = new DataWatcher();
   public static final Prototype BASE_DISPLAY_METADATA;
   private final int mountEntityId;
   private final int displayEntityId;
   private final UUID displayEntityUUID;
   private final EntityType entityType;
   private final Vector syncPos;
   private final Vector livePos;
   private final Quaternion liveRot;
   protected final DataWatcher metadata;
   protected final Vector scale;
   private Brightness brightness;

   public VirtualDisplayEntity(AttachmentManager manager, EntityType entityType) {
      this(manager, entityType, BASE_DISPLAY_METADATA.create());
   }

   public VirtualDisplayEntity(AttachmentManager manager, EntityType entityType, DataWatcher metadata) {
      super(manager);
      this.mountEntityId = EntityUtil.getUniqueEntityId();
      this.displayEntityId = EntityUtil.getUniqueEntityId();
      this.displayEntityUUID = UUID.randomUUID();
      this.entityType = entityType;
      this.metadata = metadata;
      this.syncPos = new Vector(Double.NaN, Double.NaN, Double.NaN);
      this.livePos = new Vector(Double.NaN, Double.NaN, Double.NaN);
      this.liveRot = new Quaternion();
      this.scale = new Vector(1.0D, 1.0D, 1.0D);
      this.brightness = Brightness.UNSET;
   }

   public DataWatcher getMetadata() {
      return this.metadata;
   }

   protected Vector computeTranslation(Quaternion rotation) {
      return new Vector();
   }

   public boolean containsEntityId(int entityId) {
      return entityId == this.mountEntityId || entityId == this.displayEntityId;
   }

   public Vector getScale() {
      return this.scale;
   }

   public void setScale(Vector3 scale) {
      if (this.scale.getX() != scale.x || this.scale.getY() != scale.y || this.scale.getZ() != scale.z) {
         MathUtil.setVector(this.scale, scale.x, scale.y, scale.z);
         this.onScaleUpdated();
      }

   }

   public void setScale(Vector scale) {
      if (this.scale.getX() != scale.getX() || this.scale.getY() != scale.getY() || this.scale.getZ() != scale.getZ()) {
         MathUtil.setVector(this.scale, scale);
         this.onScaleUpdated();
      }

   }

   protected void onScaleUpdated() {
      this.metadata.set(DisplayHandle.DATA_SCALE, this.scale);
   }

   public void setBrightness(Brightness brightness) {
      if (!this.brightness.equals(brightness)) {
         this.brightness = brightness;
         this.metadata.set(DisplayHandle.DATA_BRIGHTNESS_OVERRIDE, brightness);
      }

   }

   public void updatePosition(Matrix4x4 transform) {
      MathUtil.setVector(this.livePos, transform.toVector());
      this.liveRot.setTo(transform.getRotation());
      this.onRotationUpdated(this.liveRot);
      if (Double.isNaN(this.syncPos.getX())) {
         MathUtil.setVector(this.syncPos, this.livePos);
         this.syncPosition(true);
      }

   }

   protected void onRotationUpdated(Quaternion rotation) {
   }

   protected void sendSpawnPackets(AttachmentViewer viewer, Vector motion) {
      boolean canInterpolate = viewer.supportsDisplayEntityLocationInterpolation();
      if (!canInterpolate) {
         PacketPlayOutSpawnEntityLivingHandle spawnPacket = PacketPlayOutSpawnEntityLivingHandle.createNew();
         spawnPacket.setEntityId(this.mountEntityId);
         spawnPacket.setEntityUUID(UUID.randomUUID());
         spawnPacket.setEntityType(EntityType.ARMOR_STAND);
         spawnPacket.setPosX(this.syncPos.getX() - motion.getX());
         spawnPacket.setPosY(this.syncPos.getY() - motion.getY());
         spawnPacket.setPosZ(this.syncPos.getZ() - motion.getZ());
         spawnPacket.setMotX(motion.getX());
         spawnPacket.setMotY(motion.getY());
         spawnPacket.setMotZ(motion.getZ());
         spawnPacket.setYaw(0.0F);
         spawnPacket.setPitch(0.0F);
         spawnPacket.setHeadYaw(0.0F);
         viewer.sendEntityLivingSpawnPacket(spawnPacket, ARMORSTAND_MOUNT_METADATA);
      }

      PacketPlayOutSpawnEntityHandle spawnPacket = PacketPlayOutSpawnEntityHandle.createNew();
      spawnPacket.setEntityId(this.displayEntityId);
      spawnPacket.setEntityUUID(this.displayEntityUUID);
      spawnPacket.setEntityType(this.entityType);
      spawnPacket.setPosX(this.syncPos.getX() - motion.getX());
      spawnPacket.setPosY(this.syncPos.getY() - motion.getY());
      spawnPacket.setPosZ(this.syncPos.getZ() - motion.getZ());
      spawnPacket.setMotX(motion.getX());
      spawnPacket.setMotY(motion.getY());
      spawnPacket.setMotZ(motion.getZ());
      spawnPacket.setYaw(0.0F);
      spawnPacket.setPitch(0.0F);
      viewer.send((PacketHandle)spawnPacket);
      viewer.send((PacketHandle)PacketPlayOutEntityMetadataHandle.createNew(this.displayEntityId, this.metadata, true));
      if (!canInterpolate) {
         viewer.getVehicleMountController().mount(this.mountEntityId, this.displayEntityId);
      }

   }

   protected void sendDestroyPackets(AttachmentViewer viewer) {
      if (viewer.supportsDisplayEntityLocationInterpolation()) {
         viewer.send((PacketHandle)PacketPlayOutEntityDestroyHandle.createNewSingle(this.displayEntityId));
         viewer.getVehicleMountController().remove(this.displayEntityId);
      } else {
         viewer.send((PacketHandle)PacketPlayOutEntityDestroyHandle.createNewMultiple(new int[]{this.displayEntityId, this.mountEntityId}));
         viewer.getVehicleMountController().remove(this.displayEntityId);
         viewer.getVehicleMountController().remove(this.mountEntityId);
      }

   }

   protected void applyGlowing(ChatColor color) {
      this.metadata.setFlag(EntityHandle.DATA_FLAGS, 64, color != null);
      this.syncMeta();
   }

   protected void applyGlowColorForViewer(AttachmentViewer viewer, ChatColor color) {
      viewer.updateGlowColor(this.displayEntityUUID, color);
   }

   public void setUseMinecartInterpolation(boolean use) {
      this.metadata.set(DisplayHandle.DATA_INTERPOLATION_DURATION, use ? 5 : 3);
   }

   public void syncPosition(boolean absolute) {
      this.metadata.forceSet(DisplayHandle.DATA_TRANSLATION, this.computeTranslation(this.liveRot));
      this.metadata.forceSet(DisplayHandle.DATA_LEFT_ROTATION, this.liveRot);
      this.metadata.forceSet(DisplayHandle.DATA_INTERPOLATION_START_DELTA_TICKS, 0);
      double dx;
      double dy;
      double dz;
      if (!absolute) {
         dx = this.livePos.getX() - this.syncPos.getX();
         dy = this.livePos.getY() - this.syncPos.getY();
         dz = this.livePos.getZ() - this.syncPos.getZ();
         double abs_delta = Math.max(Math.max(Math.abs(dx), Math.abs(dy)), Math.abs(dz));
         absolute = abs_delta > 8.0D;
      } else {
         dx = 0.0D;
         dy = 0.0D;
         dz = 0.0D;
      }

      if (absolute) {
         MathUtil.setVector(this.syncPos, this.livePos);
         this.syncPositionLogic((id) -> {
            return PacketPlayOutEntityTeleportHandle.createNew(id, this.syncPos.getX(), this.syncPos.getY(), this.syncPos.getZ(), 0.0F, 0.0F, false);
         });
      } else {
         PacketPlayOutRelEntityMoveHandle packet = (PacketPlayOutRelEntityMoveHandle)this.syncPositionLogicAlwaysCreate((id) -> {
            return PacketPlayOutRelEntityMoveHandle.createNew(id, dx, dy, dz, false);
         });
         MathUtil.addToVector(this.syncPos, packet.getDeltaX(), packet.getDeltaY(), packet.getDeltaZ());
      }

      this.syncMeta();
   }

   private <T extends PacketHandle> T syncPositionLogicAlwaysCreate(IntFunction<T> packetCreator) {
      return (PacketHandle)this.syncPositionLogic(packetCreator).orElseGet(() -> {
         return (PacketHandle)packetCreator.apply(this.displayEntityId);
      });
   }

   private <T extends PacketHandle> Optional<T> syncPositionLogic(IntFunction<T> packetCreator) {
      T packetForNewClients = null;
      T packetForOldClients = null;
      Iterator var4 = this.getViewers().iterator();

      while(var4.hasNext()) {
         AttachmentViewer viewer = (AttachmentViewer)var4.next();
         if (viewer.supportsDisplayEntityLocationInterpolation()) {
            if (packetForNewClients == null) {
               packetForNewClients = (PacketHandle)packetCreator.apply(this.displayEntityId);
            }

            viewer.send(packetForNewClients);
         } else {
            if (packetForOldClients == null) {
               packetForOldClients = (PacketHandle)packetCreator.apply(this.mountEntityId);
            }

            viewer.send(packetForOldClients);
         }
      }

      if (packetForNewClients != null) {
         return Optional.of(packetForNewClients);
      } else if (packetForOldClients != null) {
         return Optional.of(packetForOldClients);
      } else {
         return Optional.empty();
      }
   }

   protected void syncMeta() {
      this.broadcast(PacketPlayOutEntityMetadataHandle.createNew(this.displayEntityId, this.metadata, false));
   }

   public static Brightness loadBrightnessFromConfig(ConfigurationNode config) {
      ConfigurationNode brightnessConfig = config.getNodeIfExists("brightness");
      return brightnessConfig != null ? Brightness.blockAndSkyLight((Integer)brightnessConfig.get("block", 0), (Integer)brightnessConfig.get("sky", 0)) : Brightness.UNSET;
   }

   public static void saveBrightnessToConfig(ConfigurationNode config, Brightness brightness) {
      if (brightness == Brightness.UNSET) {
         config.remove("brightness");
      } else {
         ConfigurationNode brightnessConfig = config.getNode("brightness");
         brightnessConfig.set("block", brightness.blockLight());
         brightnessConfig.set("sky", brightness.skyLight());
      }

   }

   static {
      ARMORSTAND_MOUNT_METADATA.set(EntityHandle.DATA_NO_GRAVITY, true);
      ARMORSTAND_MOUNT_METADATA.set(EntityHandle.DATA_FLAGS, -96);
      ARMORSTAND_MOUNT_METADATA.set(EntityArmorStandHandle.DATA_ARMORSTAND_FLAGS, (byte)25);
      BASE_DISPLAY_METADATA = Prototype.build().setClientDefault(DisplayHandle.DATA_INTERPOLATION_DURATION, 0).set(DisplayHandle.DATA_INTERPOLATION_DURATION, 3).set(DisplayHandle.DATA_POS_ROT_INTERPOLATION_DURATION, 3).setClientDefault(DisplayHandle.DATA_INTERPOLATION_START_DELTA_TICKS, 0).setClientDefault(DisplayHandle.DATA_SCALE, new Vector(1, 1, 1)).setClientDefault(DisplayHandle.DATA_TRANSLATION, new Vector()).setClientDefault(DisplayHandle.DATA_LEFT_ROTATION, new Quaternion()).setClientDefault(DisplayHandle.DATA_RIGHT_ROTATION, new Quaternion()).setClientDefault(DisplayHandle.DATA_BRIGHTNESS_OVERRIDE, Brightness.UNSET).setClientDefault(DisplayHandle.DATA_WIDTH, 0.0F).setClientDefault(DisplayHandle.DATA_HEIGHT, 0.0F).create();
   }
}
