package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.EntityPositionData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.teleport.RelativeFlag;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Location;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityTeleport extends PacketWrapper<WrapperPlayServerEntityTeleport> {
   private static final float ROTATION_FACTOR = 0.7111111F;
   private int entityID;
   private EntityPositionData values;
   private RelativeFlag relativeFlags;
   private boolean onGround;

   public WrapperPlayServerEntityTeleport(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerEntityTeleport(int entityID, Location location, boolean onGround) {
      this(entityID, location.getPosition(), location.getYaw(), location.getPitch(), onGround);
   }

   public WrapperPlayServerEntityTeleport(int entityID, Vector3d position, float yaw, float pitch, boolean onGround) {
      this(entityID, position, Vector3d.zero(), yaw, pitch, RelativeFlag.NONE, onGround);
   }

   public WrapperPlayServerEntityTeleport(int entityID, Vector3d position, Vector3d deltaMovement, float yaw, float pitch, RelativeFlag relativeFlags, boolean onGround) {
      this(entityID, new EntityPositionData(position, deltaMovement, yaw, pitch), relativeFlags, onGround);
   }

   public WrapperPlayServerEntityTeleport(int entityID, EntityPositionData values, RelativeFlag relativeFlags, boolean onGround) {
      super((PacketTypeCommon)PacketType.Play.Server.ENTITY_TELEPORT);
      this.entityID = entityID;
      this.values = values;
      this.relativeFlags = relativeFlags;
      this.onGround = onGround;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         this.entityID = this.readVarInt();
         this.values = EntityPositionData.read(this);
         this.relativeFlags = new RelativeFlag(this.readInt());
      } else {
         this.entityID = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) ? this.readVarInt() : this.readInt();
         Vector3d position = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9) ? Vector3d.read(this) : new Vector3d((double)this.readInt() / 32.0D, (double)this.readInt() / 32.0D, (double)this.readInt() / 32.0D);
         float yaw = (float)this.readByte() / 0.7111111F;
         float pitch = (float)this.readByte() / 0.7111111F;
         this.values = new EntityPositionData(position, Vector3d.zero(), yaw, pitch);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.onGround = this.readBoolean();
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         this.writeVarInt(this.entityID);
         EntityPositionData.write(this, this.values);
         this.writeInt(this.relativeFlags.getFullMask());
      } else {
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.writeVarInt(this.entityID);
         } else {
            this.writeInt(this.entityID);
         }

         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            Vector3d.write(this, this.values.getPosition());
         } else {
            Vector3d pos = this.values.getPosition();
            this.writeInt(MathUtil.floor(pos.x * 32.0D));
            this.writeInt(MathUtil.floor(pos.y * 32.0D));
            this.writeInt(MathUtil.floor(pos.z * 32.0D));
         }

         this.writeByte((int)(this.values.getYaw() * 0.7111111F));
         this.writeByte((int)(this.values.getPitch() * 0.7111111F));
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.writeBoolean(this.onGround);
      }

   }

   public void copy(WrapperPlayServerEntityTeleport wrapper) {
      this.entityID = wrapper.entityID;
      this.values = wrapper.values;
      this.relativeFlags = wrapper.relativeFlags;
      this.onGround = wrapper.onGround;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }

   public EntityPositionData getValues() {
      return this.values;
   }

   public void setValues(EntityPositionData values) {
      this.values = values;
   }

   public Vector3d getPosition() {
      return this.values.getPosition();
   }

   public void setPosition(Vector3d position) {
      this.values.setPosition(position);
   }

   public Vector3d getDeltaMovement() {
      return this.values.getDeltaMovement();
   }

   public void setDeltaMovement(Vector3d deltaMovement) {
      this.values.setDeltaMovement(deltaMovement);
   }

   public float getYaw() {
      return this.values.getYaw();
   }

   public void setYaw(float yaw) {
      this.values.setYaw(yaw);
   }

   public float getPitch() {
      return this.values.getPitch();
   }

   public void setPitch(float pitch) {
      this.values.setPitch(pitch);
   }

   public RelativeFlag getRelativeFlags() {
      return this.relativeFlags;
   }

   public void setRelativeFlags(RelativeFlag relativeFlags) {
      this.relativeFlags = relativeFlags;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
   }
}
