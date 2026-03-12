package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityRotation extends PacketWrapper<WrapperPlayServerEntityRotation> {
   private static final float ROTATION_FACTOR = 0.7111111F;
   private int entityID;
   private float yaw;
   private float pitch;
   private boolean onGround;

   public WrapperPlayServerEntityRotation(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerEntityRotation(int entityID, float yaw, float pitch, boolean onGround) {
      super((PacketTypeCommon)PacketType.Play.Server.ENTITY_ROTATION);
      this.entityID = entityID;
      this.yaw = yaw;
      this.pitch = pitch;
      this.onGround = onGround;
   }

   public void read() {
      this.entityID = this.readVarInt();
      this.yaw = (float)this.readByte() / 0.7111111F;
      this.pitch = (float)this.readByte() / 0.7111111F;
      this.onGround = this.readBoolean();
   }

   public void copy(WrapperPlayServerEntityRotation wrapper) {
      this.entityID = wrapper.entityID;
      this.yaw = wrapper.yaw;
      this.pitch = wrapper.pitch;
      this.onGround = wrapper.onGround;
   }

   public void write() {
      this.writeVarInt(this.entityID);
      this.writeByte((int)(this.yaw * 0.7111111F));
      this.writeByte((int)(this.pitch * 0.7111111F));
      this.writeBoolean(this.onGround);
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
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

   public boolean isOnGround() {
      return this.onGround;
   }

   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
   }
}
