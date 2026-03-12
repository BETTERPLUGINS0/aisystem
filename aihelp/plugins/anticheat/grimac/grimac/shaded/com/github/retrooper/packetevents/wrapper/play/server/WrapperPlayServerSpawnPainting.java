package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Direction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.PaintingType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Optional;
import java.util.UUID;

public class WrapperPlayServerSpawnPainting extends PacketWrapper<WrapperPlayServerSpawnPainting> {
   private int entityId;
   private UUID uuid;
   @Nullable
   private PaintingType type;
   private Vector3i position;
   private Direction direction;

   public WrapperPlayServerSpawnPainting(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSpawnPainting(int entityId, Vector3i position, Direction direction) {
      this(entityId, new UUID(0L, 0L), (PaintingType)null, position, direction);
   }

   public WrapperPlayServerSpawnPainting(int entityId, UUID uuid, Vector3i position, Direction direction) {
      this(entityId, uuid, (PaintingType)null, position, direction);
   }

   public WrapperPlayServerSpawnPainting(int entityId, UUID uuid, @Nullable PaintingType type, Vector3i position, Direction direction) {
      super((PacketTypeCommon)PacketType.Play.Server.SPAWN_PAINTING);
      this.entityId = entityId;
      this.uuid = uuid;
      this.type = type;
      this.position = position;
      this.direction = direction;
   }

   public void read() {
      this.entityId = this.readVarInt();
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         this.uuid = this.readUUID();
      } else {
         this.uuid = new UUID(0L, 0L);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
         this.type = PaintingType.getById(this.readVarInt());
      } else {
         this.type = PaintingType.getByTitle(this.readString(13));
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.position = this.readBlockPosition();
      } else {
         int x = this.readInt();
         int y = this.readInt();
         int z = this.readInt();
         this.position = new Vector3i(x, y, z);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.direction = Direction.getByHorizontalIndex(this.readUnsignedByte());
      } else {
         this.direction = Direction.getByHorizontalIndex(this.readInt());
      }

   }

   public void write() {
      this.writeVarInt(this.entityId);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         this.writeUUID(this.uuid);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
         this.writeVarInt(this.type.getId());
      } else {
         this.writeString(this.type.getTitle(), 13);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         long positionVector = this.position.getSerializedPosition(this.serverVersion);
         this.writeLong(positionVector);
      } else {
         this.writeInt(this.position.x);
         this.writeShort(this.position.y);
         this.writeInt(this.position.z);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.writeByte(this.direction.getHorizontalIndex());
      } else {
         this.writeInt(this.direction.getHorizontalIndex());
      }

   }

   public void copy(WrapperPlayServerSpawnPainting wrapper) {
      this.entityId = wrapper.entityId;
      this.uuid = wrapper.uuid;
      this.type = wrapper.type;
      this.position = wrapper.position;
      this.direction = wrapper.direction;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public UUID getUUID() {
      return this.uuid;
   }

   public void setUUID(UUID uuid) {
      this.uuid = uuid;
   }

   public Optional<PaintingType> getType() {
      return Optional.ofNullable(this.type);
   }

   public void setType(@Nullable PaintingType type) {
      this.type = type;
   }

   public Vector3i getPosition() {
      return this.position;
   }

   public void setPosition(Vector3i position) {
      this.position = position;
   }

   public Direction getDirection() {
      return this.direction;
   }

   public void setDirection(Direction direction) {
      this.direction = direction;
   }
}
