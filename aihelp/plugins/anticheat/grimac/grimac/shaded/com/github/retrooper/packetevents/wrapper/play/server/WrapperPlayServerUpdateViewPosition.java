package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUpdateViewPosition extends PacketWrapper<WrapperPlayServerUpdateViewPosition> {
   private int chunkX;
   private int chunkZ;

   public WrapperPlayServerUpdateViewPosition(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerUpdateViewPosition(int chunkX, int chunkZ) {
      super((PacketTypeCommon)PacketType.Play.Server.UPDATE_VIEW_POSITION);
      this.chunkX = chunkX;
      this.chunkZ = chunkZ;
   }

   public void read() {
      this.chunkX = this.readVarInt();
      this.chunkZ = this.readVarInt();
   }

   public void write() {
      this.writeVarInt(this.chunkX);
      this.writeVarInt(this.chunkZ);
   }

   public void copy(WrapperPlayServerUpdateViewPosition wrapper) {
      this.chunkX = wrapper.chunkX;
      this.chunkZ = wrapper.chunkZ;
   }

   public int getChunkX() {
      return this.chunkX;
   }

   public void setChunkX(int chunkX) {
      this.chunkX = chunkX;
   }

   public int getChunkZ() {
      return this.chunkZ;
   }

   public void setChunkZ(int chunkZ) {
      this.chunkZ = chunkZ;
   }
}
