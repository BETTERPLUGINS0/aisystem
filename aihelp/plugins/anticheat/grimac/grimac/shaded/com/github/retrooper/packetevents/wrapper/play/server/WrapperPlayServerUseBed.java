package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUseBed extends PacketWrapper<WrapperPlayServerUseBed> {
   private int entityId;
   private Vector3i position;

   public WrapperPlayServerUseBed(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerUseBed(int entityId, Vector3i position) {
      super((PacketTypeCommon)PacketType.Play.Server.USE_BED);
      this.entityId = entityId;
      this.position = position;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.entityId = this.readVarInt();
         this.position = this.readBlockPosition();
      } else {
         this.entityId = this.readInt();
         int x = this.readInt();
         int y = this.readUnsignedByte();
         int z = this.readInt();
         this.position = new Vector3i(x, y, z);
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.writeVarInt(this.entityId);
         this.writeBlockPosition(this.position);
      } else {
         this.writeInt(this.entityId);
         this.writeInt(this.position.getX());
         this.writeByte(this.position.getY());
         this.writeInt(this.position.getZ());
      }

   }

   public void copy(WrapperPlayServerUseBed wrapper) {
      wrapper.entityId = this.entityId;
      wrapper.position = this.position;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public Vector3i getPosition() {
      return this.position;
   }

   public void setPosition(Vector3i position) {
      this.position = position;
   }
}
