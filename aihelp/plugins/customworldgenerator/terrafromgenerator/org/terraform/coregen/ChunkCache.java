package org.terraform.coregen;

import java.util.Arrays;
import org.terraform.biome.BiomeBank;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.datastructs.CompressedChunkBools;

public class ChunkCache {
   public final TerraformWorld tw;
   public final int chunkX;
   public final int chunkZ;
   public static final float CHUNKCACHE_INVAL;
   float[] heightMapCache;
   short[] highestGroundCache;
   short[] transformedGroundCache;
   float[] yBarrierNoiseCache;
   CompressedChunkBools solids;
   BiomeBank[] biomeCache;

   public ChunkCache(TerraformWorld tw, int chunkX, int chunkZ) {
      this.tw = tw;
      this.chunkX = chunkX;
      this.chunkZ = chunkZ;
      this.initInternalCache();
   }

   private void initInternalCache() {
      this.heightMapCache = new float[256];
      Arrays.fill(this.heightMapCache, CHUNKCACHE_INVAL);
      this.transformedGroundCache = new short[256];
      Arrays.fill(this.transformedGroundCache, (short)((int)CHUNKCACHE_INVAL));
      this.yBarrierNoiseCache = new float[256];
      Arrays.fill(this.yBarrierNoiseCache, CHUNKCACHE_INVAL);
      this.highestGroundCache = new short[256];
      Arrays.fill(this.highestGroundCache, (short)((int)CHUNKCACHE_INVAL));
      this.solids = new CompressedChunkBools();
      this.biomeCache = new BiomeBank[256];
   }

   public void cacheSolid(int interChunkX, int interChunkY, int interChunkZ) {
      this.solids.set(interChunkX, interChunkY, interChunkZ);
   }

   public void cacheNonSolid(int interChunkX, int interChunkY, int interChunkZ) {
      this.solids.unSet(interChunkX, interChunkY, interChunkZ);
   }

   public boolean isSolid(int interChunkX, int interChunkY, int interChunkZ) {
      return this.solids.isSet(interChunkX, interChunkY, interChunkZ);
   }

   public double getHeightMapHeight(int rawX, int rawZ) {
      return (double)this.heightMapCache[(rawX & 15) + 16 * (rawZ & 15)];
   }

   public short getHighestGround(int rawX, int rawZ) {
      return this.highestGroundCache[(rawX & 15) + 16 * (rawZ & 15)];
   }

   public short getTransformedHeight(int chunkSubX, int chunkSubZ) {
      return this.transformedGroundCache[chunkSubX + 16 * chunkSubZ];
   }

   public void writeTransformedHeight(int chunkSubX, int chunkSubZ, short val) {
      this.transformedGroundCache[chunkSubX + 16 * chunkSubZ] = val;
   }

   public float getYBarrierNoise(int chunkSubX, int chunkSubZ) {
      return this.yBarrierNoiseCache[chunkSubX + 16 * chunkSubZ];
   }

   public void cacheYBarrierNoise(int chunkSubX, int chunkSubZ, float val) {
      this.yBarrierNoiseCache[chunkSubX + 16 * chunkSubZ] = val;
   }

   public void cacheHeightMap(int rawX, int rawZ, double value) {
      this.heightMapCache[(rawX & 15) + 16 * (rawZ & 15)] = (float)value;
   }

   public void cacheHighestGround(int rawX, int rawZ, short value) {
      this.highestGroundCache[(rawX & 15) + 16 * (rawZ & 15)] = value;
   }

   public BiomeBank getBiome(int rawX, int rawZ) {
      return this.biomeCache[(rawX & 15) + 16 * (rawZ & 15)];
   }

   public BiomeBank cacheBiome(int rawX, int rawZ, BiomeBank value) {
      this.biomeCache[(rawX & 15) + 16 * (rawZ & 15)] = value;
      return value;
   }

   public int hashCode() {
      return this.tw.hashCode() ^ this.chunkX + this.chunkZ * 31;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ChunkCache)) {
         return false;
      } else {
         ChunkCache chunk = (ChunkCache)obj;
         return this.tw == chunk.tw && this.chunkX == chunk.chunkX && this.chunkZ == chunk.chunkZ;
      }
   }

   public String toString() {
      String var10000 = this.tw.getName();
      return var10000 + ":" + this.chunkX + "," + this.chunkZ;
   }

   static {
      CHUNKCACHE_INVAL = (float)(TerraformGeneratorPlugin.injector.getMinY() - 1);
   }
}
