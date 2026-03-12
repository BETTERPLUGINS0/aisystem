package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.LpVector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityVelocity extends PacketWrapper<WrapperPlayServerEntityVelocity> {
   private static final double PRECISION_LOSS_FIX = 1.0E-11D;
   private int entityID;
   private Vector3d velocity;

   public WrapperPlayServerEntityVelocity(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerEntityVelocity(int entityID, Vector3d velocity) {
      super((PacketTypeCommon)PacketType.Play.Server.ENTITY_VELOCITY);
      this.entityID = entityID;
      this.velocity = velocity;
   }

   public void read() {
      this.entityID = this.serverVersion.isOlderThan(ServerVersion.V_1_8) ? this.readInt() : this.readVarInt();
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         this.velocity = LpVector3d.read(this);
      } else {
         double velX = (double)this.readShort() / 8000.0D;
         double velY = (double)this.readShort() / 8000.0D;
         double velZ = (double)this.readShort() / 8000.0D;
         this.velocity = new Vector3d(velX, velY, velZ);
      }

   }

   public void write() {
      if (this.serverVersion.isOlderThan(ServerVersion.V_1_8)) {
         this.writeInt(this.entityID);
      } else {
         this.writeVarInt(this.entityID);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         LpVector3d.write(this, this.velocity);
      } else {
         this.writeShort((int)(this.velocity.x * 8000.0D + Math.copySign(1.0E-11D, this.velocity.x)));
         this.writeShort((int)(this.velocity.y * 8000.0D + Math.copySign(1.0E-11D, this.velocity.y)));
         this.writeShort((int)(this.velocity.z * 8000.0D + Math.copySign(1.0E-11D, this.velocity.z)));
      }

   }

   public void copy(WrapperPlayServerEntityVelocity wrapper) {
      this.entityID = wrapper.entityID;
      this.velocity = wrapper.velocity;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }

   public Vector3d getVelocity() {
      return this.velocity;
   }

   public void setVelocity(Vector3d velocity) {
      this.velocity = velocity;
   }
}
