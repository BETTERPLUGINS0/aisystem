package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUnloadChunk extends PacketWrapper<WrapperPlayServerUnloadChunk> {
   private int chunkX;
   private int chunkZ;

   public WrapperPlayServerUnloadChunk(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerUnloadChunk(int chunkX, int chunkZ) {
      super((PacketTypeCommon)PacketType.Play.Server.UNLOAD_CHUNK);
      this.chunkX = chunkX;
      this.chunkZ = chunkZ;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
         long chunkKey = this.readLong();
         this.chunkX = getChunkX(chunkKey);
         this.chunkZ = getChunkZ(chunkKey);
      } else {
         this.chunkX = this.readInt();
         this.chunkZ = this.readInt();
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
         this.writeLong(getChunkKey(this.chunkX, this.chunkZ));
      } else {
         this.writeInt(this.chunkX);
         this.writeInt(this.chunkZ);
      }

   }

   public void copy(WrapperPlayServerUnloadChunk wrapper) {
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
