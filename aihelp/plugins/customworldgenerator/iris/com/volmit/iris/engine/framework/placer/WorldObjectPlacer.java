package com.volmit.iris.engine.framework.placer;

import com.volmit.iris.Iris;
import com.volmit.iris.core.events.IrisLootEvent;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.mantle.EngineMantle;
import com.volmit.iris.engine.object.IObjectPlacer;
import com.volmit.iris.engine.object.InventorySlotType;
import com.volmit.iris.engine.object.TileData;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.data.IrisCustomData;
import com.volmit.iris.util.math.RNG;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.InventoryHolder;

public class WorldObjectPlacer implements IObjectPlacer {
   private final World world;
   private final Engine engine;
   private final EngineMantle mantle;

   public WorldObjectPlacer(World world) {
      PlatformChunkGenerator var2 = IrisToolbelt.access(var1);
      if (var2 != null && var2.getEngine() != null) {
         this.world = var1;
         this.engine = var2.getEngine();
         this.mantle = this.engine.getMantle();
      } else {
         throw new IllegalStateException(var1.getName() + " is not an Iris World!");
      }
   }

   public int getHighest(int x, int z, IrisData data) {
      return this.mantle.getHighest(var1, var2, var3);
   }

   public int getHighest(int x, int z, IrisData data, boolean ignoreFluid) {
      return this.mantle.getHighest(var1, var2, var3, var4);
   }

   public void set(int x, int y, int z, BlockData d) {
      Block var5 = this.world.getBlockAt(var1, var2 + this.world.getMinHeight(), var3);
      if (var2 > this.world.getMinHeight() && var5.getType() != Material.BEDROCK) {
         InventorySlotType var6 = null;
         if (B.isStorageChest(var4)) {
            var6 = InventorySlotType.STORAGE;
         }

         if (var4 instanceof IrisCustomData) {
            IrisCustomData var7 = (IrisCustomData)var4;
            var5.setBlockData(var7.getBase(), false);
            Iris.warn("Tried to place custom block at " + var1 + ", " + var2 + ", " + var3 + " which is not supported!");
         } else {
            var5.setBlockData(var4, false);
         }

         if (var6 != null) {
            RNG var11 = new RNG(Cache.key(var1, var3));
            KList var8 = this.engine.getLootTables(var11, var5);

            try {
               Bukkit.getPluginManager().callEvent(new IrisLootEvent(this.engine, var5, var6, var8));
               if (!var8.isEmpty()) {
                  Iris.debug("IrisLootEvent has been accessed");
               }

               if (var8.isEmpty()) {
                  return;
               }

               InventoryHolder var9 = (InventoryHolder)var5.getState();
               this.engine.addItems(false, var9.getInventory(), var11, var8, var6, this.world, var1, var2, var3, 15);
            } catch (Throwable var10) {
               Iris.reportError(var10);
            }
         }

      }
   }

   public BlockData get(int x, int y, int z) {
      return this.world.getBlockAt(var1, var2 + this.world.getMinHeight(), var3).getBlockData();
   }

   public boolean isPreventingDecay() {
      return this.mantle.isPreventingDecay();
   }

   public boolean isCarved(int x, int y, int z) {
      return this.mantle.isCarved(var1, var2, var3);
   }

   public boolean isSolid(int x, int y, int z) {
      return this.world.getBlockAt(var1, var2 + this.world.getMinHeight(), var3).getType().isSolid();
   }

   public boolean isUnderwater(int x, int z) {
      return this.mantle.isUnderwater(var1, var2);
   }

   public int getFluidHeight() {
      return this.mantle.getFluidHeight();
   }

   public boolean isDebugSmartBore() {
      return this.mantle.isDebugSmartBore();
   }

   public void setTile(int xx, int yy, int zz, TileData tile) {
      var4.toBukkitTry(this.world.getBlockAt(var1, var2 + this.world.getMinHeight(), var3));
   }

   public <T> void setData(int xx, int yy, int zz, T data) {
   }

   public <T> T getData(int xx, int yy, int zz, Class<T> t) {
      return null;
   }

   @Generated
   public World getWorld() {
      return this.world;
   }

   @Generated
   public Engine getEngine() {
      return this.engine;
   }

   @Generated
   public EngineMantle getMantle() {
      return this.mantle;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof WorldObjectPlacer)) {
         return false;
      } else {
         WorldObjectPlacer var2 = (WorldObjectPlacer)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            World var3 = this.getWorld();
            World var4 = var2.getWorld();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof WorldObjectPlacer;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      World var3 = this.getWorld();
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }
}
