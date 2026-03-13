package com.volmit.iris.core.edit;

import com.volmit.iris.Iris;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.BlockPosition;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import lombok.Generated;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;

public class DustRevealer {
   private final Engine engine;
   private final World world;
   private final BlockPosition block;
   private final String key;
   private final KList<BlockPosition> hits;

   public DustRevealer(Engine engine, World world, BlockPosition block, String key, KList<BlockPosition> hits) {
      this.engine = var1;
      this.world = var2;
      this.block = var3;
      this.key = var4;
      this.hits = var5;
      J.s(() -> {
         new BlockSignal(var2.getBlockAt(var3.getX(), var3.getY(), var3.getZ()), 10);
         if (M.r(0.25D)) {
            var2.playSound(var3.toBlock(var2).getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.0F, RNG.r.f(0.2F, 2.0F));
         }

         J.a(() -> {
            while(BlockSignal.active.get() > 128) {
               J.sleep(5L);
            }

            try {
               this.is(new BlockPosition(var3.getX() + 1, var3.getY(), var3.getZ()));
               this.is(new BlockPosition(var3.getX() - 1, var3.getY(), var3.getZ()));
               this.is(new BlockPosition(var3.getX(), var3.getY() + 1, var3.getZ()));
               this.is(new BlockPosition(var3.getX(), var3.getY() - 1, var3.getZ()));
               this.is(new BlockPosition(var3.getX(), var3.getY(), var3.getZ() + 1));
               this.is(new BlockPosition(var3.getX(), var3.getY(), var3.getZ() - 1));
               this.is(new BlockPosition(var3.getX() + 1, var3.getY(), var3.getZ() + 1));
               this.is(new BlockPosition(var3.getX() + 1, var3.getY(), var3.getZ() - 1));
               this.is(new BlockPosition(var3.getX() - 1, var3.getY(), var3.getZ() + 1));
               this.is(new BlockPosition(var3.getX() - 1, var3.getY(), var3.getZ() - 1));
               this.is(new BlockPosition(var3.getX() + 1, var3.getY() + 1, var3.getZ()));
               this.is(new BlockPosition(var3.getX() + 1, var3.getY() - 1, var3.getZ()));
               this.is(new BlockPosition(var3.getX() - 1, var3.getY() + 1, var3.getZ()));
               this.is(new BlockPosition(var3.getX() - 1, var3.getY() - 1, var3.getZ()));
               this.is(new BlockPosition(var3.getX(), var3.getY() + 1, var3.getZ() - 1));
               this.is(new BlockPosition(var3.getX(), var3.getY() + 1, var3.getZ() + 1));
               this.is(new BlockPosition(var3.getX(), var3.getY() - 1, var3.getZ() - 1));
               this.is(new BlockPosition(var3.getX(), var3.getY() - 1, var3.getZ() + 1));
               this.is(new BlockPosition(var3.getX() - 1, var3.getY() + 1, var3.getZ() - 1));
               this.is(new BlockPosition(var3.getX() - 1, var3.getY() + 1, var3.getZ() + 1));
               this.is(new BlockPosition(var3.getX() - 1, var3.getY() - 1, var3.getZ() - 1));
               this.is(new BlockPosition(var3.getX() - 1, var3.getY() - 1, var3.getZ() + 1));
               this.is(new BlockPosition(var3.getX() + 1, var3.getY() + 1, var3.getZ() - 1));
               this.is(new BlockPosition(var3.getX() + 1, var3.getY() + 1, var3.getZ() + 1));
               this.is(new BlockPosition(var3.getX() + 1, var3.getY() - 1, var3.getZ() - 1));
               this.is(new BlockPosition(var3.getX() + 1, var3.getY() - 1, var3.getZ() + 1));
            } catch (Throwable var3x) {
               Iris.reportError(var3x);
               var3x.printStackTrace();
            }

         });
      }, RNG.r.i(2, 8));
   }

   public static void spawn(Block block, VolmitSender sender) {
      World var2 = var0.getWorld();
      Engine var3 = IrisToolbelt.access(var2).getEngine();
      if (var3 != null) {
         String var4 = var3.getObjectPlacementKey(var0.getX(), var0.getY() - var0.getWorld().getMinHeight(), var0.getZ());
         if (var4 != null) {
            var2.playSound(var0.getLocation(), Sound.ITEM_LODESTONE_COMPASS_LOCK, 1.0F, 0.1F);
            var1.sendMessage("Found object " + var4);
            J.a(() -> {
               new DustRevealer(var3, var2, new BlockPosition(var0.getX(), var0.getY(), var0.getZ()), var4, new KList());
            });
         }
      }

   }

   private boolean is(BlockPosition a) {
      int var2 = var1.getY() - this.world.getMinHeight();
      if (this.isValidTry(var1) && this.engine.getObjectPlacementKey(var1.getX(), var2, var1.getZ()) != null && this.engine.getObjectPlacementKey(var1.getX(), var2, var1.getZ()).equals(this.key)) {
         this.hits.add((Object)var1);
         new DustRevealer(this.engine, this.world, var1, this.key, this.hits);
         return true;
      } else {
         return false;
      }
   }

   private boolean isValidTry(BlockPosition b) {
      return !this.hits.contains(var1);
   }

   @Generated
   public Engine getEngine() {
      return this.engine;
   }

   @Generated
   public World getWorld() {
      return this.world;
   }

   @Generated
   public BlockPosition getBlock() {
      return this.block;
   }

   @Generated
   public String getKey() {
      return this.key;
   }

   @Generated
   public KList<BlockPosition> getHits() {
      return this.hits;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof DustRevealer)) {
         return false;
      } else {
         DustRevealer var2 = (DustRevealer)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label71: {
               Engine var3 = this.getEngine();
               Engine var4 = var2.getEngine();
               if (var3 == null) {
                  if (var4 == null) {
                     break label71;
                  }
               } else if (var3.equals(var4)) {
                  break label71;
               }

               return false;
            }

            World var5 = this.getWorld();
            World var6 = var2.getWorld();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label57: {
               BlockPosition var7 = this.getBlock();
               BlockPosition var8 = var2.getBlock();
               if (var7 == null) {
                  if (var8 == null) {
                     break label57;
                  }
               } else if (var7.equals(var8)) {
                  break label57;
               }

               return false;
            }

            String var9 = this.getKey();
            String var10 = var2.getKey();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            KList var11 = this.getHits();
            KList var12 = var2.getHits();
            if (var11 == null) {
               if (var12 == null) {
                  return true;
               }
            } else if (var11.equals(var12)) {
               return true;
            }

            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof DustRevealer;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      Engine var3 = this.getEngine();
      int var8 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      World var4 = this.getWorld();
      var8 = var8 * 59 + (var4 == null ? 43 : var4.hashCode());
      BlockPosition var5 = this.getBlock();
      var8 = var8 * 59 + (var5 == null ? 43 : var5.hashCode());
      String var6 = this.getKey();
      var8 = var8 * 59 + (var6 == null ? 43 : var6.hashCode());
      KList var7 = this.getHits();
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getEngine());
      return "DustRevealer(engine=" + var10000 + ", world=" + String.valueOf(this.getWorld()) + ", block=" + String.valueOf(this.getBlock()) + ", key=" + this.getKey() + ", hits=" + String.valueOf(this.getHits()) + ")";
   }
}
