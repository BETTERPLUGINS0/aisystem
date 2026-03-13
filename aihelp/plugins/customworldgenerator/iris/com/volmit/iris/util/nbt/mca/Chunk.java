package com.volmit.iris.util.nbt.mca;

import com.volmit.iris.Iris;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.nbt.io.NBTDeserializer;
import com.volmit.iris.util.nbt.io.NBTSerializer;
import com.volmit.iris.util.nbt.io.NamedTag;
import com.volmit.iris.util.nbt.mca.palette.MCABiomeContainer;
import com.volmit.iris.util.nbt.tag.CompoundTag;
import com.volmit.iris.util.nbt.tag.ListTag;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class Chunk {
   public static final int DEFAULT_DATA_VERSION = 2730;
   private final KMap<Integer, Section> sections = new KMap();
   private boolean partial;
   private int lastMCAUpdate;
   private CompoundTag data;
   private int dataVersion;
   private int nativeIrisVersion;
   private long lastUpdate;
   private long inhabitedTime;
   private MCABiomeContainer biomes;
   private CompoundTag heightMaps;
   private CompoundTag carvingMasks;
   private ListTag<CompoundTag> entities;
   private ListTag<CompoundTag> tileEntities;
   private ListTag<CompoundTag> tileTicks;
   private ListTag<CompoundTag> liquidTicks;
   private ListTag<ListTag<?>> lights;
   private ListTag<ListTag<?>> liquidsToBeTicked;
   private ListTag<ListTag<?>> toBeTicked;
   private ListTag<ListTag<?>> postProcessing;
   private String status;
   private CompoundTag structures;

   Chunk(int lastMCAUpdate) {
      this.lastMCAUpdate = var1;
   }

   public Chunk(CompoundTag data) {
      this.data = var1;
      this.initReferences(-1L);
      this.setStatus("full");
   }

   public static Chunk newChunk() {
      World var0 = (World)Bukkit.getServer().getWorlds().get(0);
      Chunk var1 = new Chunk(0);
      var1.dataVersion = 2730;
      var1.data = new CompoundTag();
      var1.biomes = INMS.get().newBiomeContainer(var0.getMinHeight(), var0.getMaxHeight());
      var1.data.put("Level", defaultLevel());
      var1.status = "full";
      return var1;
   }

   public static void injectIrisData(Chunk c) {
      World var1 = (World)Bukkit.getServer().getWorlds().get(0);
      var0.data.put("Iris", nativeIrisVersion());
   }

   private static CompoundTag defaultLevel() {
      CompoundTag var0 = new CompoundTag();
      var0.putString("Status", "full");
      var0.putString("Generator", "Iris Headless " + Iris.instance.getDescription().getVersion());
      return var0;
   }

   private static CompoundTag nativeIrisVersion() {
      CompoundTag var0 = new CompoundTag();
      var0.putString("Generator", "Iris " + Iris.instance.getDescription().getVersion());
      return var0;
   }

   private void initReferences(long loadFlags) {
      if (this.data == null) {
         throw new NullPointerException("data cannot be null");
      } else {
         CompoundTag var3 = this.data;
         World var4 = (World)Bukkit.getServer().getWorlds().get(0);
         this.dataVersion = this.data.getInt("DataVersion");
         this.inhabitedTime = var3.getLong("InhabitedTime");
         this.lastUpdate = var3.getLong("LastUpdate");
         if ((var1 & 1L) != 0L) {
            this.biomes = INMS.get().newBiomeContainer(var4.getMinHeight(), var4.getMaxHeight(), var3.getIntArray("Biomes"));
         }

         if ((var1 & 2L) != 0L) {
            this.heightMaps = var3.getCompoundTag("Heightmaps");
         }

         if ((var1 & 4L) != 0L) {
            this.carvingMasks = var3.getCompoundTag("CarvingMasks");
         }

         if ((var1 & 8L) != 0L) {
            this.entities = var3.containsKey("Entities") ? var3.getListTag("Entities").asCompoundTagList() : null;
         }

         if ((var1 & 16L) != 0L) {
            this.tileEntities = var3.containsKey("TileEntities") ? var3.getListTag("TileEntities").asCompoundTagList() : null;
         }

         if ((var1 & 64L) != 0L) {
            this.tileTicks = var3.containsKey("TileTicks") ? var3.getListTag("TileTicks").asCompoundTagList() : null;
         }

         if ((var1 & 128L) != 0L) {
            this.liquidTicks = var3.containsKey("LiquidTicks") ? var3.getListTag("LiquidTicks").asCompoundTagList() : null;
         }

         if ((var1 & 16384L) != 0L) {
            this.lights = var3.containsKey("Lights") ? var3.getListTag("Lights").asListTagList() : null;
         }

         if ((var1 & 32768L) != 0L) {
            this.liquidsToBeTicked = var3.containsKey("LiquidsToBeTicked") ? var3.getListTag("LiquidsToBeTicked").asListTagList() : null;
         }

         if ((var1 & 256L) != 0L) {
            this.toBeTicked = var3.containsKey("ToBeTicked") ? var3.getListTag("ToBeTicked").asListTagList() : null;
         }

         if ((var1 & 512L) != 0L) {
            this.postProcessing = var3.containsKey("PostProcessing") ? var3.getListTag("PostProcessing").asListTagList() : null;
         }

         this.status = var3.getString("Status");
         if ((var1 & 1024L) != 0L) {
            this.structures = var3.getCompoundTag("Structures");
         }

         if ((var1 & 14336L) != 0L && var3.containsKey("Sections")) {
            Iterator var5 = var3.getListTag("Sections").asCompoundTagList().iterator();

            while(var5.hasNext()) {
               CompoundTag var6 = (CompoundTag)var5.next();
               byte var7 = var6.getByte("Y");
               if (var7 <= 15 && var7 >= 0) {
                  Section var8 = new Section(var6, this.dataVersion, var1);
                  if (!var8.isEmpty()) {
                     this.sections.put(Integer.valueOf(var7), var8);
                  }
               }
            }
         }

         if (var1 != -1L) {
            this.data = null;
            this.partial = true;
         } else {
            this.partial = false;
         }

      }
   }

   public int serialize(RandomAccessFile raf, int xPos, int zPos) {
      if (this.partial) {
         throw new UnsupportedOperationException("Partially loaded chunks cannot be serialized");
      } else {
         ByteArrayOutputStream var4 = new ByteArrayOutputStream(4096);
         BufferedOutputStream var5 = new BufferedOutputStream(CompressionType.ZLIB.compress(var4));

         try {
            (new NBTSerializer(false)).toStream((NamedTag)(new NamedTag((String)null, this.updateHandle(var2, var3))), var5);
         } catch (Throwable var9) {
            try {
               var5.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         var5.close();
         byte[] var10 = var4.toByteArray();
         var1.writeInt(var10.length + 1);
         var1.writeByte(CompressionType.ZLIB.getID());
         var1.write(var10);
         return var10.length + 5;
      }
   }

   public void deserialize(RandomAccessFile raf) {
      this.deserialize(var1, -1L);
   }

   public void deserialize(RandomAccessFile raf, long loadFlags) {
      byte var4 = var1.readByte();
      CompressionType var5 = CompressionType.getFromID(var4);
      if (var5 == null) {
         throw new IOException("invalid compression type " + var4);
      } else {
         BufferedInputStream var6 = new BufferedInputStream(var5.decompress(new FileInputStream(var1.getFD())));
         NamedTag var7 = (new NBTDeserializer(false)).fromStream(var6);
         if (var7 != null && var7.getTag() instanceof CompoundTag) {
            this.data = (CompoundTag)var7.getTag();
            this.initReferences(var2);
         } else {
            String var10002 = var7 == null ? "null" : var7.getClass().getName();
            throw new IOException("invalid data tag: " + var10002);
         }
      }
   }

   public synchronized int getBiomeAt(int blockX, int blockY, int blockZ) {
      return this.biomes.getBiome(var1, var2, var3);
   }

   public synchronized void setBiomeAt(int blockX, int blockY, int blockZ, int biomeID) {
      this.biomes.setBiome(var1, var2, var3, var4);
   }

   int getBiomeIndex(int biomeX, int biomeY, int biomeZ) {
      return var2 * 64 + var3 * 4 + var1;
   }

   public CompoundTag getBlockStateAt(int blockX, int blockY, int blockZ) {
      int var4 = MCAUtil.blockToChunk(var2);
      Section var5 = (Section)this.sections.get(var4);
      return var5 == null ? null : var5.getBlockStateAt(var1, var2, var3);
   }

   public void setBlockStateAt(int blockX, int blockY, int blockZ, CompoundTag state, boolean cleanup) {
      int var6 = MCAUtil.blockToChunk(var2);
      Section var7 = (Section)this.sections.get(var6);
      if (var7 == null) {
         var7 = Section.newSection();
         this.sections.put(var6, var7);
      }

      var7.setBlockStateAt(var1, var2, var3, var4, var5);
   }

   public int getDataVersion() {
      return this.dataVersion;
   }

   public void setDataVersion(int dataVersion) {
      this.dataVersion = var1;
   }

   public int getLastMCAUpdate() {
      return this.lastMCAUpdate;
   }

   public void setLastMCAUpdate(int lastMCAUpdate) {
      this.lastMCAUpdate = var1;
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = var1;
   }

   public Section getSection(int sectionY) {
      return (Section)this.sections.get(var1);
   }

   public void setSection(int sectionY, Section section) {
      this.sections.put(var1, var2);
   }

   public long getLastUpdate() {
      return this.lastUpdate;
   }

   public void setLastUpdate(long lastUpdate) {
      this.lastUpdate = var1;
   }

   public long getInhabitedTime() {
      return this.inhabitedTime;
   }

   public void setInhabitedTime(long inhabitedTime) {
      this.inhabitedTime = var1;
   }

   public CompoundTag getHeightMaps() {
      return this.heightMaps;
   }

   public void setHeightMaps(CompoundTag heightMaps) {
      this.heightMaps = var1;
   }

   public CompoundTag getCarvingMasks() {
      return this.carvingMasks;
   }

   public void setCarvingMasks(CompoundTag carvingMasks) {
      this.carvingMasks = var1;
   }

   public ListTag<CompoundTag> getEntities() {
      return this.entities;
   }

   public void setEntities(ListTag<CompoundTag> entities) {
      this.entities = var1;
   }

   public ListTag<CompoundTag> getTileEntities() {
      return this.tileEntities;
   }

   public void setTileEntities(ListTag<CompoundTag> tileEntities) {
      this.tileEntities = var1;
   }

   public ListTag<CompoundTag> getTileTicks() {
      return this.tileTicks;
   }

   public void setTileTicks(ListTag<CompoundTag> tileTicks) {
      this.tileTicks = var1;
   }

   public ListTag<CompoundTag> getLiquidTicks() {
      return this.liquidTicks;
   }

   public void setLiquidTicks(ListTag<CompoundTag> liquidTicks) {
      this.liquidTicks = var1;
   }

   public ListTag<ListTag<?>> getLights() {
      return this.lights;
   }

   public void setLights(ListTag<ListTag<?>> lights) {
      this.lights = var1;
   }

   public ListTag<ListTag<?>> getLiquidsToBeTicked() {
      return this.liquidsToBeTicked;
   }

   public void setLiquidsToBeTicked(ListTag<ListTag<?>> liquidsToBeTicked) {
      this.liquidsToBeTicked = var1;
   }

   public ListTag<ListTag<?>> getToBeTicked() {
      return this.toBeTicked;
   }

   public void setToBeTicked(ListTag<ListTag<?>> toBeTicked) {
      this.toBeTicked = var1;
   }

   public ListTag<ListTag<?>> getPostProcessing() {
      return this.postProcessing;
   }

   public void setPostProcessing(ListTag<ListTag<?>> postProcessing) {
      this.postProcessing = var1;
   }

   public CompoundTag getStructures() {
      return this.structures;
   }

   public void setStructures(CompoundTag structures) {
      this.structures = var1;
   }

   int getBlockIndex(int blockX, int blockZ) {
      return (var2 & 15) * 16 + (var1 & 15);
   }

   public void cleanupPalettesAndBlockStates() {
      Iterator var1 = this.sections.values().iterator();

      while(var1.hasNext()) {
         Section var2 = (Section)var1.next();
         if (var2 != null) {
            var2.cleanupPaletteAndBlockStates();
         }
      }

   }

   public CompoundTag updateHandle(int xPos, int zPos) {
      this.data.putInt("DataVersion", this.dataVersion);
      CompoundTag var3 = this.data.getCompoundTag("Level");
      var3.putInt("xPos", var1);
      var3.putInt("zPos", var2);
      var3.putLong("LastUpdate", this.lastUpdate);
      var3.putLong("InhabitedTime", this.inhabitedTime);
      var3.putIntArray("Biomes", this.biomes.getData());
      if (this.heightMaps != null) {
         var3.put("Heightmaps", this.heightMaps);
      }

      if (this.carvingMasks != null) {
         var3.put("CarvingMasks", this.carvingMasks);
      }

      if (this.entities != null) {
         var3.put("Entities", this.entities);
      }

      if (this.tileEntities != null) {
         var3.put("TileEntities", this.tileEntities);
      }

      if (this.tileTicks != null) {
         var3.put("TileTicks", this.tileTicks);
      }

      if (this.liquidTicks != null) {
         var3.put("LiquidTicks", this.liquidTicks);
      }

      if (this.lights != null) {
         var3.put("Lights", this.lights);
      }

      if (this.liquidsToBeTicked != null) {
         var3.put("LiquidsToBeTicked", this.liquidsToBeTicked);
      }

      if (this.toBeTicked != null) {
         var3.put("ToBeTicked", this.toBeTicked);
      }

      if (this.postProcessing != null) {
         var3.put("PostProcessing", this.postProcessing);
      }

      var3.putString("Status", this.status);
      if (this.structures != null) {
         var3.put("Structures", this.structures);
      }

      ListTag var4 = new ListTag(CompoundTag.class);
      Iterator var5 = this.sections.keySet().iterator();

      while(var5.hasNext()) {
         int var6 = (Integer)var5.next();
         if (this.sections.get(var6) != null) {
            var4.add(((Section)this.sections.get(var6)).updateHandle(var6));
         }
      }

      var3.put("Sections", var4);
      return this.data;
   }

   public int sectionCount() {
      return this.sections.size();
   }
}
