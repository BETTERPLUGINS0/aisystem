package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class WrapperPlayServerMultiBlockChange extends PacketWrapper<WrapperPlayServerMultiBlockChange> {
   private Vector3i chunkPosition;
   private Boolean trustEdges;
   private WrapperPlayServerMultiBlockChange.EncodedBlock[] blockData;

   public WrapperPlayServerMultiBlockChange(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerMultiBlockChange(Vector3i chunkPosition, @Nullable Boolean trustEdges, WrapperPlayServerMultiBlockChange.EncodedBlock[] blockData) {
      super((PacketTypeCommon)PacketType.Play.Server.MULTI_BLOCK_CHANGE);
      this.chunkPosition = chunkPosition;
      this.trustEdges = trustEdges;
      this.blockData = blockData;
   }

   public void read() {
      int len;
      int i;
      int i;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
         long encodedPosition = this.readLong();
         len = (int)(encodedPosition >> 42);
         i = (int)(encodedPosition << 44 >> 44);
         int sectionZ = (int)(encodedPosition << 22 >> 42);
         this.chunkPosition = new Vector3i(len, i, sectionZ);
         if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
            this.trustEdges = this.readBoolean();
         }

         this.blockData = new WrapperPlayServerMultiBlockChange.EncodedBlock[this.readVarInt()];

         for(i = 0; i < this.blockData.length; ++i) {
            this.blockData[i] = new WrapperPlayServerMultiBlockChange.EncodedBlock(this.chunkPosition, this.readVarLong());
         }
      } else {
         int chunkX = this.readInt();
         int chunkZ = this.readInt();
         this.chunkPosition = new Vector3i(chunkX, 0, chunkZ);
         len = this.readVarInt();
         this.blockData = new WrapperPlayServerMultiBlockChange.EncodedBlock[len];

         for(i = 0; i < len; ++i) {
            short pos = this.readShort();
            i = (chunkX << 4) + (pos >> 12 & 15);
            int y = pos & 255;
            int z = (chunkZ << 4) + (pos >> 8 & 15);
            int blockId = this.readVarInt();
            this.blockData[i] = new WrapperPlayServerMultiBlockChange.EncodedBlock(blockId, i, y, z);
         }
      }

   }

   public void write() {
      int x;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
         long encodedPos = 0L;
         encodedPos |= ((long)this.chunkPosition.getX() & 4194303L) << 42;
         encodedPos |= ((long)this.chunkPosition.getZ() & 4194303L) << 20;
         this.writeLong(encodedPos | (long)this.chunkPosition.getY() & 1048575L);
         if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
            this.writeBoolean(Boolean.TRUE.equals(this.trustEdges));
         }

         this.writeVarInt(this.blockData.length);
         WrapperPlayServerMultiBlockChange.EncodedBlock[] var3 = this.blockData;
         int var4 = var3.length;

         for(x = 0; x < var4; ++x) {
            WrapperPlayServerMultiBlockChange.EncodedBlock blockDatum = var3[x];
            this.writeVarLong(blockDatum.toLong());
         }
      } else {
         this.writeInt(this.chunkPosition.getX());
         this.writeInt(this.chunkPosition.getZ());
         this.writeVarInt(this.blockData.length);
         WrapperPlayServerMultiBlockChange.EncodedBlock[] var8 = this.blockData;
         int var2 = var8.length;

         for(int var9 = 0; var9 < var2; ++var9) {
            WrapperPlayServerMultiBlockChange.EncodedBlock record = var8[var9];
            x = record.getX() & 15;
            int z = record.getZ() & 15;
            short pos = (short)(x << 12 | z << 8 | record.getY());
            this.writeShort(pos);
            this.writeVarInt(record.getBlockId());
         }
      }

   }

   public void copy(WrapperPlayServerMultiBlockChange wrapper) {
      this.chunkPosition = wrapper.chunkPosition;
      this.trustEdges = wrapper.trustEdges;
      this.blockData = wrapper.blockData;
   }

   public Vector3i getChunkPosition() {
      return this.chunkPosition;
   }

   public void setChunkPosition(Vector3i chunkPosition) {
      this.chunkPosition = chunkPosition;
   }

   public boolean getTrustEdges() {
      return Boolean.TRUE.equals(this.trustEdges);
   }

   public void setTrustEdges(Boolean trustEdges) {
      this.trustEdges = trustEdges;
   }

   public WrapperPlayServerMultiBlockChange.EncodedBlock[] getBlocks() {
      return this.blockData;
   }

   public void setBlocks(WrapperPlayServerMultiBlockChange.EncodedBlock[] blocks) {
      this.blockData = blocks;
   }

   public static class EncodedBlock {
      private int blockID;
      private int x;
      private int y;
      private int z;

      public EncodedBlock(int blockID, int x, int y, int z) {
         this.blockID = blockID;
         this.x = x;
         this.y = y;
         this.z = z;
      }

      public EncodedBlock(WrappedBlockState blockState, int x, int y, int z) {
         this(blockState.getGlobalId(), x, y, z);
      }

      public EncodedBlock(Vector3i chunk, long data) {
         short position = (short)((int)(data & 4095L));
         this.x = (chunk.getX() << 4) + (position >>> 8 & 15);
         this.y = (chunk.getY() << 4) + (position & 15);
         this.z = (chunk.getZ() << 4) + (position >>> 4 & 15);
         this.blockID = (int)(data >>> 12);
      }

      public long toLong() {
         return (long)this.blockID << 12 | (long)((this.x & 15) << 8) | (long)((this.z & 15) << 4) | (long)(this.y & 15);
      }

      public int getBlockId() {
         return this.blockID;
      }

      public void setBlockId(int blockID) {
         this.blockID = blockID;
      }

      public WrappedBlockState getBlockState(ClientVersion version) {
         return WrappedBlockState.getByGlobalId(version, this.blockID);
      }

      public void setBlockState(WrappedBlockState blockState) {
         this.blockID = blockState.getGlobalId();
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }

      public int getZ() {
         return this.z;
      }

      public void setX(int x) {
         this.x = x;
      }

      public void setY(int y) {
         this.y = y;
      }

      public void setZ(int z) {
         this.z = z;
      }
   }
}
