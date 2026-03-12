package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLongArray;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Column {
   private final int x;
   private final int z;
   private final boolean fullChunk;
   private final BaseChunk[] chunks;
   private final TileEntity[] tileEntities;
   private final boolean hasHeightmaps;
   @Nullable
   private NBTCompound heightmapsNbt;
   @Nullable
   private Map<HeightmapType, long[]> heightmaps;
   private final boolean hasBiomeData;
   private int[] biomeDataInts;
   private byte[] biomeDataBytes;

   public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities, int[] biomeData) {
      this.x = x;
      this.z = z;
      this.fullChunk = fullChunk;
      this.chunks = (BaseChunk[])Arrays.copyOf(chunks, chunks.length);
      this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
      this.hasHeightmaps = false;
      this.heightmapsNbt = new NBTCompound();
      this.hasBiomeData = true;
      this.biomeDataInts = biomeData != null ? Arrays.copyOf(biomeData, biomeData.length) : null;
   }

   public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities) {
      this.x = x;
      this.z = z;
      this.fullChunk = fullChunk;
      this.chunks = (BaseChunk[])Arrays.copyOf(chunks, chunks.length);
      this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
      this.hasHeightmaps = false;
      this.heightmapsNbt = new NBTCompound();
      this.hasBiomeData = false;
      this.biomeDataInts = new int[1024];
   }

   public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities, NBTCompound heightmapsNbt) {
      this.x = x;
      this.z = z;
      this.fullChunk = fullChunk;
      this.chunks = (BaseChunk[])Arrays.copyOf(chunks, chunks.length);
      this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
      this.hasHeightmaps = true;
      this.heightmapsNbt = heightmapsNbt;
      this.hasBiomeData = false;
      this.biomeDataInts = new int[1024];
   }

   public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities, Map<HeightmapType, long[]> heightmaps) {
      this.x = x;
      this.z = z;
      this.fullChunk = fullChunk;
      this.chunks = (BaseChunk[])Arrays.copyOf(chunks, chunks.length);
      this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
      this.hasHeightmaps = true;
      this.heightmapsNbt = null;
      this.heightmaps = heightmaps;
      this.hasBiomeData = false;
      this.biomeDataInts = new int[1024];
   }

   public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities, NBTCompound heightmapsNbt, int[] biomeDataInts) {
      this.x = x;
      this.z = z;
      this.fullChunk = fullChunk;
      this.chunks = (BaseChunk[])Arrays.copyOf(chunks, chunks.length);
      this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
      this.hasHeightmaps = true;
      this.heightmapsNbt = heightmapsNbt;
      this.hasBiomeData = true;
      this.biomeDataInts = biomeDataInts != null ? Arrays.copyOf(biomeDataInts, biomeDataInts.length) : null;
   }

   public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities, NBTCompound heightmapsNbt, byte[] biomeData) {
      this.x = x;
      this.z = z;
      this.fullChunk = fullChunk;
      this.chunks = (BaseChunk[])Arrays.copyOf(chunks, chunks.length);
      this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
      this.hasHeightmaps = true;
      this.heightmapsNbt = heightmapsNbt;
      this.hasBiomeData = true;
      this.biomeDataBytes = biomeData != null ? Arrays.copyOf(biomeData, biomeData.length) : null;
   }

   public Column(int chunkX, int chunkZ, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities, byte[] biomeDataBytes) {
      this.x = chunkX;
      this.z = chunkZ;
      this.fullChunk = fullChunk;
      this.chunks = (BaseChunk[])Arrays.copyOf(chunks, chunks.length);
      this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
      this.hasHeightmaps = false;
      this.heightmapsNbt = new NBTCompound();
      this.hasBiomeData = true;
      this.biomeDataBytes = biomeDataBytes != null ? Arrays.copyOf(biomeDataBytes, biomeDataBytes.length) : null;
   }

   public int getX() {
      return this.x;
   }

   public int getZ() {
      return this.z;
   }

   public boolean isFullChunk() {
      return this.fullChunk;
   }

   public BaseChunk[] getChunks() {
      return this.chunks;
   }

   public TileEntity[] getTileEntities() {
      return this.tileEntities;
   }

   public boolean hasHeightMaps() {
      return this.hasHeightmaps;
   }

   /** @deprecated */
   @Deprecated
   public NBTCompound getHeightMaps() {
      if (this.heightmapsNbt == null) {
         this.heightmapsNbt = new NBTCompound();
         Iterator var1 = this.getHeightmaps().entrySet().iterator();

         while(var1.hasNext()) {
            Entry<HeightmapType, long[]> entry = (Entry)var1.next();
            this.heightmapsNbt.setTag(((HeightmapType)entry.getKey()).getSerializationKey(), new NBTLongArray((long[])entry.getValue()));
         }
      }

      return this.heightmapsNbt;
   }

   public Map<HeightmapType, long[]> getHeightmaps() {
      if (this.heightmaps == null) {
         if (this.hasHeightmaps && !this.heightmapsNbt.isEmpty()) {
            this.heightmaps = new EnumMap(HeightmapType.class);
            Iterator var1 = this.heightmapsNbt.getTags().entrySet().iterator();

            while(var1.hasNext()) {
               Entry<String, NBT> tag = (Entry)var1.next();
               HeightmapType heightmapType = HeightmapType.getHeightmapType((String)tag.getKey());
               if (heightmapType != null && tag.getValue() instanceof NBTLongArray) {
                  long[] array = ((NBTLongArray)tag.getValue()).getValue();
                  this.heightmaps.put(heightmapType, array);
               }
            }
         } else {
            this.heightmaps = Collections.emptyMap();
         }
      }

      return this.heightmaps;
   }

   public boolean hasBiomeData() {
      return this.hasBiomeData;
   }

   public int[] getBiomeDataInts() {
      return this.biomeDataInts;
   }

   public byte[] getBiomeDataBytes() {
      return this.biomeDataBytes;
   }
}
