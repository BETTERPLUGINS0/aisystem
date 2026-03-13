package com.volmit.iris.engine.framework.placer;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IObjectPlacer;
import com.volmit.iris.engine.object.IrisObjectPlacement;
import com.volmit.iris.engine.object.TileData;
import com.volmit.iris.util.math.RNG;
import org.bukkit.block.data.BlockData;

public class HeightmapObjectPlacer implements IObjectPlacer {
   private final long s;
   private final IrisObjectPlacement config;
   private final IObjectPlacer oplacer;

   public HeightmapObjectPlacer(Engine engine, RNG rng, int x, int yv, int z, IrisObjectPlacement config, IObjectPlacer oplacer) {
      this.s = var2.nextLong() + (long)var4 + (long)var5 - (long)var3;
      this.config = var6;
      this.oplacer = var7;
   }

   public int getHighest(int param1Int1, int param1Int2, IrisData data) {
      return (int)Math.round(this.config.getHeightmap().getNoise(this.s, (double)var1, (double)var2, var3));
   }

   public int getHighest(int param1Int1, int param1Int2, IrisData data, boolean param1Boolean) {
      return (int)Math.round(this.config.getHeightmap().getNoise(this.s, (double)var1, (double)var2, var3));
   }

   public void set(int param1Int1, int param1Int2, int param1Int3, BlockData param1BlockData) {
      this.oplacer.set(var1, var2, var3, var4);
   }

   public BlockData get(int param1Int1, int param1Int2, int param1Int3) {
      return this.oplacer.get(var1, var2, var3);
   }

   public boolean isPreventingDecay() {
      return this.oplacer.isPreventingDecay();
   }

   public boolean isCarved(int x, int y, int z) {
      return this.oplacer.isCarved(var1, var2, var3);
   }

   public boolean isSolid(int param1Int1, int param1Int2, int param1Int3) {
      return this.oplacer.isSolid(var1, var2, var3);
   }

   public boolean isUnderwater(int param1Int1, int param1Int2) {
      return this.oplacer.isUnderwater(var1, var2);
   }

   public int getFluidHeight() {
      return this.oplacer.getFluidHeight();
   }

   public boolean isDebugSmartBore() {
      return this.oplacer.isDebugSmartBore();
   }

   public void setTile(int param1Int1, int param1Int2, int param1Int3, TileData param1TileData) {
      this.oplacer.setTile(var1, var2, var3, var4);
   }

   public <T> void setData(int xx, int yy, int zz, T data) {
      this.oplacer.setData(var1, var2, var3, var4);
   }

   public <T> T getData(int xx, int yy, int zz, Class<T> t) {
      return this.oplacer.getData(var1, var2, var3, var4);
   }

   public Engine getEngine() {
      return null;
   }
}
