package es.outlook.adriansrj.nbt.mca;

import es.outlook.adriansrj.nbt.nbt.io.NBTDeserializer;
import es.outlook.adriansrj.nbt.nbt.io.NBTSerializer;
import es.outlook.adriansrj.nbt.nbt.io.NamedTag;
import es.outlook.adriansrj.nbt.nbt.tag.CompoundTag;
import es.outlook.adriansrj.nbt.nbt.tag.ListTag;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Iterator;

public class Chunk {
   public static final int DEFAULT_DATA_VERSION = 2567;
   private boolean partial;
   private boolean raw;
   private int lastMCAUpdate;
   private CompoundTag data;
   private int dataVersion;
   private long lastUpdate;
   private long inhabitedTime;
   private int[] biomes;
   private CompoundTag heightMaps;
   private CompoundTag carvingMasks;
   private Section[] sections = new Section[16];
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

   Chunk(int var1) {
      this.lastMCAUpdate = var1;
   }

   public Chunk(CompoundTag var1) {
      this.data = var1;
      this.initReferences(-1L);
   }

   private void initReferences(long var1) {
      if (this.data == null) {
         throw new NullPointerException("data cannot be null");
      } else if (var1 != -1L && (var1 & 65536L) != 0L) {
         this.raw = true;
      } else {
         CompoundTag var3;
         if ((var3 = this.data.getCompoundTag("Level")) == null) {
            throw new IllegalArgumentException("data does not contain \"Level\" tag");
         } else {
            this.dataVersion = this.data.getInt("DataVersion");
            this.inhabitedTime = var3.getLong("InhabitedTime");
            this.lastUpdate = var3.getLong("LastUpdate");
            if ((var1 & 1L) != 0L) {
               this.biomes = var3.getIntArray("Biomes");
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
               Iterator var4 = var3.getListTag("Sections").asCompoundTagList().iterator();

               while(var4.hasNext()) {
                  CompoundTag var5 = (CompoundTag)var4.next();
                  byte var6 = var5.getByte("Y");
                  if (var6 <= 15 && var6 >= 0) {
                     Section var7 = new Section(var5, this.dataVersion, var1);
                     if (!var7.isEmpty()) {
                        this.sections[var6] = var7;
                     }
                  }
               }
            }

            if (var1 != -1L) {
               this.data = null;
               this.partial = true;
            }

         }
      }
   }

   public int serialize(RandomAccessFile var1, int var2, int var3) {
      if (this.partial) {
         throw new UnsupportedOperationException("Partially loaded chunks cannot be serialized");
      } else {
         ByteArrayOutputStream var4 = new ByteArrayOutputStream(4096);
         BufferedOutputStream var5 = new BufferedOutputStream(CompressionType.ZLIB.compress(var4));
         Throwable var6 = null;

         try {
            (new NBTSerializer(false)).toStream((NamedTag)(new NamedTag((String)null, this.updateHandle(var2, var3))), var5);
         } catch (Throwable var15) {
            var6 = var15;
            throw var15;
         } finally {
            if (var5 != null) {
               if (var6 != null) {
                  try {
                     var5.close();
                  } catch (Throwable var14) {
                     var6.addSuppressed(var14);
                  }
               } else {
                  var5.close();
               }
            }

         }

         byte[] var17 = var4.toByteArray();
         var1.writeInt(var17.length + 1);
         var1.writeByte(CompressionType.ZLIB.getID());
         var1.write(var17);
         return var17.length + 5;
      }
   }

   public void deserialize(RandomAccessFile var1) {
      this.deserialize(var1, -1L);
   }

   public void deserialize(RandomAccessFile var1, long var2) {
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
            throw new IOException("invalid data tag: " + (var7 == null ? "null" : var7.getClass().getName()));
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public int getBiomeAt(int var1, int var2) {
      if (this.dataVersion < 2202) {
         return this.biomes != null && this.biomes.length == 256 ? this.biomes[this.getBlockIndex(var1, var2)] : -1;
      } else {
         throw new IllegalStateException("cannot get biome using Chunk#getBiomeAt(int,int) from biome data with DataVersion of 2202 or higher, use Chunk#getBiomeAt(int,int,int) instead");
      }
   }

   public int getBiomeAt(int var1, int var2, int var3) {
      if (this.dataVersion < 2202) {
         return this.biomes != null && this.biomes.length == 256 ? this.biomes[this.getBlockIndex(var1, var3)] : -1;
      } else if (this.biomes != null && this.biomes.length == 1024) {
         int var4 = (var1 & 15) >> 2;
         int var5 = (var2 & 15) >> 2;
         int var6 = (var3 & 15) >> 2;
         return this.biomes[this.getBiomeIndex(var4, var5, var6)];
      } else {
         return -1;
      }
   }

   /** @deprecated */
   @Deprecated
   public void setBiomeAt(int var1, int var2, int var3) {
      this.checkRaw();
      if (this.dataVersion < 2202) {
         if (this.biomes == null || this.biomes.length != 256) {
            this.biomes = new int[256];
            Arrays.fill(this.biomes, -1);
         }

         this.biomes[this.getBlockIndex(var1, var2)] = var3;
      } else {
         if (this.biomes == null || this.biomes.length != 1024) {
            this.biomes = new int[1024];
            Arrays.fill(this.biomes, -1);
         }

         int var4 = (var1 & 15) >> 2;
         int var5 = (var2 & 15) >> 2;

         for(int var6 = 0; var6 < 64; ++var6) {
            this.biomes[this.getBiomeIndex(var4, var6, var5)] = var3;
         }
      }

   }

   public void setBiomeAt(int var1, int var2, int var3, int var4) {
      this.checkRaw();
      if (this.dataVersion < 2202) {
         if (this.biomes == null || this.biomes.length != 256) {
            this.biomes = new int[256];
            Arrays.fill(this.biomes, -1);
         }

         this.biomes[this.getBlockIndex(var1, var3)] = var4;
      } else {
         if (this.biomes == null || this.biomes.length != 1024) {
            this.biomes = new int[1024];
            Arrays.fill(this.biomes, -1);
         }

         int var5 = (var1 & 15) >> 2;
         int var6 = (var3 & 15) >> 2;
         this.biomes[this.getBiomeIndex(var5, var2, var6)] = var4;
      }

   }

   int getBiomeIndex(int var1, int var2, int var3) {
      return var2 * 16 + var3 * 4 + var1;
   }

   public CompoundTag getBlockStateAt(int var1, int var2, int var3) {
      Section var4 = this.sections[MCAUtil.blockToChunk(var2)];
      return var4 == null ? null : var4.getBlockStateAt(var1, var2, var3);
   }

   public void setBlockStateAt(int var1, int var2, int var3, CompoundTag var4, boolean var5) {
      this.checkRaw();
      int var6 = MCAUtil.blockToChunk(var2);
      Section var7 = this.sections[var6];
      if (var7 == null) {
         var7 = this.sections[var6] = Section.newSection();
      }

      var7.setBlockStateAt(var1, var2, var3, var4, var5);
   }

   public int getDataVersion() {
      return this.dataVersion;
   }

   public void setDataVersion(int var1) {
      this.checkRaw();
      this.dataVersion = var1;
      Section[] var2 = this.sections;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Section var5 = var2[var4];
         if (var5 != null) {
            var5.dataVersion = var1;
         }
      }

   }

   public int getLastMCAUpdate() {
      return this.lastMCAUpdate;
   }

   public void setLastMCAUpdate(int var1) {
      this.checkRaw();
      this.lastMCAUpdate = var1;
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String var1) {
      this.checkRaw();
      this.status = var1;
   }

   public Section getSection(int var1) {
      return this.sections[var1];
   }

   public void setSection(int var1, Section var2) {
      this.checkRaw();
      this.sections[var1] = var2;
   }

   public long getLastUpdate() {
      return this.lastUpdate;
   }

   public void setLastUpdate(long var1) {
      this.checkRaw();
      this.lastUpdate = var1;
   }

   public long getInhabitedTime() {
      return this.inhabitedTime;
   }

   public void setInhabitedTime(long var1) {
      this.checkRaw();
      this.inhabitedTime = var1;
   }

   public int[] getBiomes() {
      return this.biomes;
   }

   public void setBiomes(int[] var1) {
      this.checkRaw();
      if (var1 != null && (this.dataVersion < 2202 && var1.length != 256 || this.dataVersion >= 2202 && var1.length != 1024)) {
         throw new IllegalArgumentException("biomes array must have a length of " + (this.dataVersion < 2202 ? "256" : "1024"));
      } else {
         this.biomes = var1;
      }
   }

   public CompoundTag getHeightMaps() {
      return this.heightMaps;
   }

   public void setHeightMaps(CompoundTag var1) {
      this.checkRaw();
      this.heightMaps = var1;
   }

   public CompoundTag getCarvingMasks() {
      return this.carvingMasks;
   }

   public void setCarvingMasks(CompoundTag var1) {
      this.checkRaw();
      this.carvingMasks = var1;
   }

   public ListTag<CompoundTag> getEntities() {
      return this.entities;
   }

   public void setEntities(ListTag<CompoundTag> var1) {
      this.checkRaw();
      this.entities = var1;
   }

   public ListTag<CompoundTag> getTileEntities() {
      return this.tileEntities;
   }

   public void setTileEntities(ListTag<CompoundTag> var1) {
      this.checkRaw();
      this.tileEntities = var1;
   }

   public ListTag<CompoundTag> getTileTicks() {
      return this.tileTicks;
   }

   public void setTileTicks(ListTag<CompoundTag> var1) {
      this.checkRaw();
      this.tileTicks = var1;
   }

   public ListTag<CompoundTag> getLiquidTicks() {
      return this.liquidTicks;
   }

   public void setLiquidTicks(ListTag<CompoundTag> var1) {
      this.checkRaw();
      this.liquidTicks = var1;
   }

   public ListTag<ListTag<?>> getLights() {
      return this.lights;
   }

   public void setLights(ListTag<ListTag<?>> var1) {
      this.checkRaw();
      this.lights = var1;
   }

   public ListTag<ListTag<?>> getLiquidsToBeTicked() {
      return this.liquidsToBeTicked;
   }

   public void setLiquidsToBeTicked(ListTag<ListTag<?>> var1) {
      this.checkRaw();
      this.liquidsToBeTicked = var1;
   }

   public ListTag<ListTag<?>> getToBeTicked() {
      return this.toBeTicked;
   }

   public void setToBeTicked(ListTag<ListTag<?>> var1) {
      this.checkRaw();
      this.toBeTicked = var1;
   }

   public ListTag<ListTag<?>> getPostProcessing() {
      return this.postProcessing;
   }

   public void setPostProcessing(ListTag<ListTag<?>> var1) {
      this.checkRaw();
      this.postProcessing = var1;
   }

   public CompoundTag getStructures() {
      return this.structures;
   }

   public void setStructures(CompoundTag var1) {
      this.checkRaw();
      this.structures = var1;
   }

   int getBlockIndex(int var1, int var2) {
      return (var2 & 15) * 16 + (var1 & 15);
   }

   public void cleanupPalettesAndBlockStates() {
      this.checkRaw();
      Section[] var1 = this.sections;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Section var4 = var1[var3];
         if (var4 != null) {
            var4.cleanupPaletteAndBlockStates();
         }
      }

   }

   private void checkRaw() {
      if (this.raw) {
         throw new UnsupportedOperationException("cannot update field when working with raw data");
      }
   }

   public static Chunk newChunk() {
      return newChunk(2567);
   }

   public static Chunk newChunk(int var0) {
      Chunk var1 = new Chunk(0);
      var1.dataVersion = var0;
      var1.data = new CompoundTag();
      var1.data.put("Level", new CompoundTag());
      var1.status = "mobs_spawned";
      return var1;
   }

   public CompoundTag getHandle() {
      return this.data;
   }

   public CompoundTag updateHandle(int var1, int var2) {
      if (this.raw) {
         return this.data;
      } else {
         this.data.putInt("DataVersion", this.dataVersion);
         CompoundTag var3 = this.data.getCompoundTag("Level");
         var3.putInt("xPos", var1);
         var3.putInt("zPos", var2);
         var3.putLong("LastUpdate", this.lastUpdate);
         var3.putLong("InhabitedTime", this.inhabitedTime);
         if (this.dataVersion < 2202) {
            if (this.biomes != null && this.biomes.length == 256) {
               var3.putIntArray("Biomes", this.biomes);
            }
         } else if (this.biomes != null && this.biomes.length == 1024) {
            var3.putIntArray("Biomes", this.biomes);
         }

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

         for(int var5 = 0; var5 < this.sections.length; ++var5) {
            if (this.sections[var5] != null) {
               var4.add(this.sections[var5].updateHandle(var5));
            }
         }

         var3.put("Sections", var4);
         return this.data;
      }
   }
}
