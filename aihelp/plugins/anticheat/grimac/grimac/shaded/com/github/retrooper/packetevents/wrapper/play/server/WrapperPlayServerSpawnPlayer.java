package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityMetadataProvider;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Location;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WrapperPlayServerSpawnPlayer extends PacketWrapper<WrapperPlayServerSpawnPlayer> {
   private static final float ROTATION_DIVISOR = 0.7111111F;
   private int entityID;
   private UUID uuid;
   private Vector3d position;
   private float yaw;
   private float pitch;
   /** @deprecated */
   @Deprecated
   private ItemType item;
   private List<EntityData<?>> entityMetadata;

   public WrapperPlayServerSpawnPlayer(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSpawnPlayer(int entityId, UUID uuid, Location location, EntityMetadataProvider metadata) {
      this(entityId, uuid, location.getPosition(), location.getYaw(), location.getPitch(), metadata.entityData(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()));
   }

   public WrapperPlayServerSpawnPlayer(int entityId, UUID uuid, Location location, List<EntityData<?>> entityMetadata) {
      this(entityId, uuid, location.getPosition(), location.getYaw(), location.getPitch(), entityMetadata);
   }

   public WrapperPlayServerSpawnPlayer(int entityId, UUID uuid, Location location, EntityData<?>... entityMetadata) {
      this(entityId, uuid, location.getPosition(), location.getYaw(), location.getPitch(), Arrays.asList(entityMetadata));
   }

   public WrapperPlayServerSpawnPlayer(int entityID, UUID uuid, Vector3d position, float yaw, float pitch, List<EntityData<?>> entityMetadata) {
      super((PacketTypeCommon)PacketType.Play.Server.SPAWN_PLAYER);
      this.entityID = entityID;
      this.uuid = uuid;
      this.position = position;
      this.yaw = yaw;
      this.pitch = pitch;
      this.entityMetadata = entityMetadata;
      this.item = ItemTypes.AIR;
   }

   public WrapperPlayServerSpawnPlayer(int entityID, UUID uuid, Vector3d position, float yaw, float pitch, EntityMetadataProvider metadata) {
      this(entityID, uuid, position, yaw, pitch, metadata.entityData(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()));
   }

   public void read() {
      this.entityID = this.readVarInt();
      this.uuid = this.readUUID();
      boolean v1_9 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9);
      if (v1_9) {
         this.position = new Vector3d(this.readDouble(), this.readDouble(), this.readDouble());
      } else {
         this.position = new Vector3d((double)this.readInt() / 32.0D, (double)this.readInt() / 32.0D, (double)this.readInt() / 32.0D);
      }

      this.yaw = (float)this.readByte() / 0.7111111F;
      this.pitch = (float)this.readByte() / 0.7111111F;
      if (!v1_9) {
         this.item = ItemTypes.getById(this.serverVersion.toClientVersion(), this.readShort());
      } else {
         this.item = ItemTypes.AIR;
      }

      if (this.serverVersion.isOlderThan(ServerVersion.V_1_15)) {
         this.entityMetadata = this.readEntityMetadata();
      } else {
         this.entityMetadata = new ArrayList();
      }

   }

   public void write() {
      this.writeVarInt(this.entityID);
      this.writeUUID(this.uuid);
      boolean v1_9 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9);
      if (v1_9) {
         this.writeDouble(this.position.getX());
         this.writeDouble(this.position.getY());
         this.writeDouble(this.position.getZ());
      } else {
         this.writeInt(MathUtil.floor(this.position.getX() * 32.0D));
         this.writeInt(MathUtil.floor(this.position.getY() * 32.0D));
         this.writeInt(MathUtil.floor(this.position.getZ() * 32.0D));
      }

      this.writeByte((byte)((int)(this.yaw * 0.7111111F)));
      this.writeByte((byte)((int)(this.pitch * 0.7111111F)));
      if (!v1_9) {
         this.writeShort(this.item.getId(this.serverVersion.toClientVersion()));
      }

      if (this.serverVersion.isOlderThan(ServerVersion.V_1_15)) {
         this.writeEntityMetadata(this.entityMetadata);
      }

   }

   public void copy(WrapperPlayServerSpawnPlayer wrapper) {
      this.entityID = wrapper.entityID;
      this.uuid = wrapper.uuid;
      this.position = wrapper.position;
      this.yaw = wrapper.yaw;
      this.pitch = wrapper.pitch;
      this.item = wrapper.item;
      this.entityMetadata = wrapper.entityMetadata;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }

   public UUID getUUID() {
      return this.uuid;
   }

   public void setUUID(UUID uuid) {
      this.uuid = uuid;
   }

   public Vector3d getPosition() {
      return this.position;
   }

   public void setPosition(Vector3d position) {
      this.position = position;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   /** @deprecated */
   @Deprecated
   public List<EntityData<?>> getEntityMetadata() {
      return this.entityMetadata;
   }

   /** @deprecated */
   @Deprecated
   public void setEntityMetadata(List<EntityData<?>> entityMetadata) {
      this.entityMetadata = entityMetadata;
   }

   /** @deprecated */
   @Deprecated
   public void setEntityMetadata(EntityMetadataProvider metadata) {
      this.entityMetadata = metadata.entityData(this.serverVersion.toClientVersion());
   }

   /** @deprecated */
   @Deprecated
   public ItemType getItem() {
      return this.item;
   }

   /** @deprecated */
   @Deprecated
   public void setItem(ItemType item) {
      this.item = item;
   }
}
