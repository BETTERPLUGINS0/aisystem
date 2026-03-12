package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.LpVector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Location;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Optional;
import java.util.UUID;

public class WrapperPlayServerSpawnEntity extends PacketWrapper<WrapperPlayServerSpawnEntity> {
   private static final float ROTATION_FACTOR = 0.7111111F;
   private static final double VELOCITY_FACTOR = 8000.0D;
   private int entityID;
   private Optional<UUID> uuid;
   private EntityType entityType;
   private Vector3d position;
   private float pitch;
   private float yaw;
   private float headYaw;
   private int data;
   private Optional<Vector3d> velocity;

   public WrapperPlayServerSpawnEntity(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSpawnEntity(int entityID, Optional<UUID> uuid, EntityType entityType, Vector3d position, float pitch, float yaw, float headYaw, int data, Optional<Vector3d> velocity) {
      super((PacketTypeCommon)PacketType.Play.Server.SPAWN_ENTITY);
      this.entityID = entityID;
      this.uuid = uuid;
      this.entityType = entityType;
      this.position = position;
      this.pitch = pitch;
      this.yaw = yaw;
      this.headYaw = headYaw;
      this.data = data;
      this.velocity = velocity;
   }

   public WrapperPlayServerSpawnEntity(int entityID, @Nullable UUID uuid, EntityType entityType, Location location, float headYaw, int data, @Nullable Vector3d velocity) {
      super((PacketTypeCommon)PacketType.Play.Server.SPAWN_ENTITY);
      this.entityID = entityID;
      this.uuid = Optional.ofNullable(uuid);
      this.entityType = entityType;
      this.position = location.getPosition();
      this.pitch = location.getPitch();
      this.yaw = location.getYaw();
      this.headYaw = headYaw;
      this.data = data;
      this.velocity = Optional.ofNullable(velocity);
   }

   public void read() {
      this.entityID = this.readVarInt();
      boolean v1_9 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9);
      boolean v1_15 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
      boolean v1_19 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
      if (v1_9) {
         this.uuid = Optional.of(this.readUUID());
      } else {
         this.uuid = Optional.empty();
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
         this.entityType = EntityTypes.getById(this.serverVersion.toClientVersion(), this.readVarInt());
      } else {
         int id = this.readByte();
         this.entityType = EntityTypes.getByLegacyId(this.serverVersion.toClientVersion(), id);
         if (this.entityType == null) {
            this.entityType = EntityTypes.getById(this.serverVersion.toClientVersion(), id);
         }
      }

      double x;
      double y;
      double z;
      if (v1_9) {
         x = this.readDouble();
         y = this.readDouble();
         z = this.readDouble();
      } else {
         x = (double)this.readInt() / 32.0D;
         y = (double)this.readInt() / 32.0D;
         z = (double)this.readInt() / 32.0D;
      }

      this.position = new Vector3d(x, y, z);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         this.velocity = Optional.of(LpVector3d.read(this));
      }

      if (v1_15) {
         this.pitch = (float)this.readByte() / 0.7111111F;
         this.yaw = (float)this.readByte() / 0.7111111F;
      } else {
         this.yaw = (float)this.readByte() / 0.7111111F;
         this.pitch = (float)this.readByte() / 0.7111111F;
      }

      if (v1_19) {
         this.headYaw = (float)this.readByte() / 0.7111111F;
         this.data = this.readVarInt();
      } else {
         this.data = this.readInt();
      }

      if (this.serverVersion.isOlderThan(ServerVersion.V_1_21_9)) {
         if (!v1_9 && this.data <= 0) {
            this.velocity = Optional.empty();
         } else {
            double velX = (double)this.readShort() / 8000.0D;
            double velY = (double)this.readShort() / 8000.0D;
            double velZ = (double)this.readShort() / 8000.0D;
            this.velocity = Optional.of(new Vector3d(velX, velY, velZ));
         }
      }

   }

   public void write() {
      this.writeVarInt(this.entityID);
      boolean v1_9 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9);
      boolean v1_15 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
      boolean v1_19 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
      if (v1_9) {
         this.writeUUID((UUID)this.uuid.orElse(new UUID(0L, 0L)));
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
         this.writeVarInt(this.entityType.getId(this.serverVersion.toClientVersion()));
      } else if (this.entityType.getLegacyId(this.serverVersion.toClientVersion()) != -1) {
         this.writeByte(this.entityType.getLegacyId(this.serverVersion.toClientVersion()));
      } else {
         this.writeByte(this.entityType.getId(this.serverVersion.toClientVersion()));
      }

      if (v1_9) {
         this.writeDouble(this.position.x);
         this.writeDouble(this.position.y);
         this.writeDouble(this.position.z);
      } else {
         this.writeInt(MathUtil.floor(this.position.x * 32.0D));
         this.writeInt(MathUtil.floor(this.position.y * 32.0D));
         this.writeInt(MathUtil.floor(this.position.z * 32.0D));
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         LpVector3d.write(this, (Vector3d)this.velocity.orElse(Vector3d.zero()));
      }

      if (v1_15) {
         this.writeByte(MathUtil.floor(this.pitch * 0.7111111F));
         this.writeByte(MathUtil.floor(this.yaw * 0.7111111F));
      } else {
         this.writeByte(MathUtil.floor(this.yaw * 0.7111111F));
         this.writeByte(MathUtil.floor(this.pitch * 0.7111111F));
      }

      if (v1_19) {
         this.writeByte(MathUtil.floor(this.headYaw * 0.7111111F));
         this.writeVarInt(this.data);
      } else {
         this.writeInt(this.data);
      }

      if (this.serverVersion.isOlderThan(ServerVersion.V_1_21_9) && (v1_9 || this.data > 0)) {
         Vector3d vel = (Vector3d)this.velocity.orElse(new Vector3d(-1.0D, -1.0D, -1.0D));
         int velX = (int)(vel.x * 8000.0D);
         int velY = (int)(vel.y * 8000.0D);
         int velZ = (int)(vel.z * 8000.0D);
         this.writeShort(velX);
         this.writeShort(velY);
         this.writeShort(velZ);
      }

   }

   public void copy(WrapperPlayServerSpawnEntity wrapper) {
      this.entityID = wrapper.entityID;
      this.uuid = wrapper.uuid;
      this.entityType = wrapper.entityType;
      this.position = wrapper.position;
      this.pitch = wrapper.pitch;
      this.yaw = wrapper.yaw;
      this.headYaw = wrapper.headYaw;
      this.data = wrapper.data;
      this.velocity = wrapper.velocity;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }

   public Optional<UUID> getUUID() {
      return this.uuid;
   }

   public void setUUID(Optional<UUID> uuid) {
      this.uuid = uuid;
   }

   public EntityType getEntityType() {
      return this.entityType;
   }

   public void setEntityType(EntityType entityType) {
      this.entityType = entityType;
   }

   public Vector3d getPosition() {
      return this.position;
   }

   public void setPosition(Vector3d position) {
      this.position = position;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getHeadYaw() {
      return this.headYaw;
   }

   public void setHeadYaw(float headYaw) {
      this.headYaw = headYaw;
   }

   public int getData() {
      return this.data;
   }

   public void setData(int data) {
      this.data = data;
   }

   public Optional<Vector3d> getVelocity() {
      return this.velocity;
   }

   public void setVelocity(Optional<Vector3d> velocity) {
      this.velocity = velocity;
   }
}
