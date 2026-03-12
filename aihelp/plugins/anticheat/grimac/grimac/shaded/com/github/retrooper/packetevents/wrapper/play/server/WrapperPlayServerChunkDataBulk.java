package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.NetworkChunkData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_7.Chunk_v1_7;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_8.Chunk_v1_8;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_7;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_8;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.io.IOException;
import java.util.BitSet;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class WrapperPlayServerChunkDataBulk extends PacketWrapper<WrapperPlayServerChunkDataBulk> {
   private int[] x;
   private int[] z;
   private BaseChunk[][] chunks;
   private byte[][] biomeData;

   public WrapperPlayServerChunkDataBulk(PacketSendEvent event) {
      super(event);
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.read_1_8();
      } else {
         this.read_1_7();
      }

   }

   private void read_1_8() {
      boolean skylight = this.readBoolean();
      int columns = this.readVarInt();
      this.x = new int[columns];
      this.z = new int[columns];
      this.chunks = new BaseChunk[columns][];
      this.biomeData = new byte[columns][];
      NetworkChunkData[] data = new NetworkChunkData[columns];

      int column;
      for(column = 0; column < columns; ++column) {
         this.x[column] = this.readInt();
         this.z[column] = this.readInt();
         int mask = this.readUnsignedShort();
         int chunks = Integer.bitCount(mask);
         int length = chunks * 10240 + (skylight ? chunks * 2048 : 0);
         byte[] dat = new byte[length];
         data[column] = new NetworkChunkData(mask, true, skylight, dat);
      }

      for(column = 0; column < columns; ++column) {
         BitSet mask = BitSet.valueOf(new long[]{(long)data[column].getMask()});
         BaseChunk[] chunkData = (new ChunkReader_v1_8()).read(this.user.getDimensionType(), mask, (BitSet)null, true, skylight, false, 16, data[column].getData().length, this);
         this.chunks[column] = chunkData;
         this.biomeData[column] = this.readBytes(256);
      }

   }

   private void read_1_7() {
      short columns = this.readShort();
      int deflatedLength = this.readInt();
      boolean skylight = this.readBoolean();
      byte[] deflatedBytes = this.readBytes(deflatedLength);
      byte[] inflated = new byte[196864 * columns];
      Inflater inflater = new Inflater();
      inflater.setInput(deflatedBytes, 0, deflatedLength);

      label99: {
         try {
            inflater.inflate(inflated);
            break label99;
         } catch (DataFormatException var21) {
            (new IOException("Bad compressed data format")).printStackTrace();
         } finally {
            inflater.end();
         }

         return;
      }

      Object originalBuffer = this.buffer;
      Object inflatedBuf = UnpooledByteBufAllocationHelper.wrappedBuffer(inflated);
      this.x = new int[columns];
      this.z = new int[columns];
      this.chunks = new BaseChunk[columns][];
      this.biomeData = new byte[columns][];

      for(int count = 0; count < columns; ++count) {
         int x = this.readInt();
         int z = this.readInt();
         BitSet chunkMask = BitSet.valueOf(new long[]{(long)this.readUnsignedShort()});
         BitSet extendedChunkMask = BitSet.valueOf(new long[]{(long)this.readUnsignedShort()});
         int chunks = 0;
         int extended = 0;

         int length;
         for(length = 0; length < 16; ++length) {
            chunks += chunkMask.get(length) ? 1 : 0;
            extended += extendedChunkMask.get(length) ? 1 : 0;
         }

         length = 8192 * chunks + 256 + 2048 * extended;
         if (skylight) {
            length += 2048 * chunks;
         }

         this.buffer = inflatedBuf;
         BaseChunk[] chunkData = (new ChunkReader_v1_7()).read(this.user.getDimensionType(), chunkMask, extendedChunkMask, true, skylight, false, 16, length, this);
         byte[] biomeDataBytes = this.readBytes(256);
         this.buffer = originalBuffer;
         this.x[count] = x;
         this.z[count] = z;
         this.chunks[count] = chunkData;
         this.biomeData[count] = biomeDataBytes;
      }

      ByteBufHelper.release(inflatedBuf);
   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.write_1_8();
      } else {
         this.write_1_7();
      }

   }

   public void copy(WrapperPlayServerChunkDataBulk wrapper) {
      this.x = wrapper.x;
      this.z = wrapper.z;
      this.chunks = wrapper.chunks;
      this.biomeData = wrapper.biomeData;
   }

   private void write_1_8() {
      boolean skylight = false;
      NetworkChunkData[] data = new NetworkChunkData[this.chunks.length];

      int column;
      for(column = 0; column < this.chunks.length; ++column) {
         data[column] = ChunkReader_v1_8.chunksToData((Chunk_v1_8[])this.chunks[column], this.biomeData[column]);
         if (data[column].hasSkyLight()) {
            skylight = true;
         }
      }

      this.writeBoolean(skylight);
      this.writeVarInt(this.chunks.length);

      for(column = 0; column < this.x.length; ++column) {
         this.writeInt(this.x[column]);
         this.writeInt(this.z[column]);
         this.writeShort(data[column].getMask());
      }

      for(column = 0; column < this.x.length; ++column) {
         this.writeBytes(data[column].getData());
      }

   }

   private void write_1_7() {
      int[] chunkMask = new int[this.chunks.length];
      int[] extendedChunkMask = new int[this.chunks.length];
      int pos = 0;
      byte[] bytes = new byte[0];
      boolean skylight = false;

      for(int count = 0; count < this.chunks.length; ++count) {
         BaseChunk[] column = this.chunks[count];
         NetworkChunkData data = ChunkReader_v1_7.chunksToData((Chunk_v1_7[])column, this.biomeData[count]);
         if (bytes.length < pos + data.getData().length) {
            byte[] newArray = new byte[pos + data.getData().length];
            System.arraycopy(bytes, 0, newArray, 0, bytes.length);
            bytes = newArray;
         }

         if (data.hasSkyLight()) {
            skylight = true;
         }

         System.arraycopy(data.getData(), 0, bytes, pos, data.getData().length);
         pos += data.getData().length;
         chunkMask[count] = data.getMask();
         extendedChunkMask[count] = data.getExtendedChunkMask();
      }

      Deflater deflater = new Deflater(-1);
      byte[] deflatedData = new byte[pos];

      int deflatedLength;
      try {
         deflater.setInput(bytes, 0, pos);
         deflater.finish();
         deflatedLength = deflater.deflate(deflatedData);
      } finally {
         deflater.end();
      }

      this.writeShort(this.chunks.length);
      this.writeInt(deflatedLength);
      this.writeBoolean(skylight);

      int count;
      for(count = 0; count < deflatedLength; ++count) {
         this.writeByte(deflatedData[count]);
      }

      for(count = 0; count < this.chunks.length; ++count) {
         this.writeInt(this.x[count]);
         this.writeInt(this.z[count]);
         this.writeShort((short)(chunkMask[count] & '\uffff'));
         this.writeShort((short)(extendedChunkMask[count] & '\uffff'));
      }

   }

   public int[] getX() {
      return this.x;
   }

   public int[] getZ() {
      return this.z;
   }

   public BaseChunk[][] getChunks() {
      return this.chunks;
   }

   public byte[][] getBiomeData() {
      return this.biomeData;
   }
}
