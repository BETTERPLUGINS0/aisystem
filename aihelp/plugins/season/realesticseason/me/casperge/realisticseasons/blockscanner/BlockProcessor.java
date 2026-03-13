package me.casperge.realisticseasons.blockscanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.temperature.TemperatureSettings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class BlockProcessor {
   private boolean isProcessing = false;
   private HashMap<UUID, Integer> positiveEffects = new HashMap();
   private HashMap<UUID, Integer> negativeEffects = new HashMap();
   private RealisticSeasons main;
   private TemperatureSettings tempsettings;

   public BlockProcessor(RealisticSeasons var1) {
      this.main = var1;
      this.tempsettings = var1.getTemperatureManager().getTempData().getTempSettings();
   }

   public boolean isProcessing() {
      return this.isProcessing;
   }

   public void process(HashMap<UUID, ChunkSystem> var1, HashMap<UUID, SimpleLocation> var2) {
      this.isProcessing = true;
      HashMap var3 = new HashMap();
      HashMap var4 = new HashMap();
      this.positiveEffects.clear();
      this.negativeEffects.clear();
      Iterator var5 = var1.keySet().iterator();

      while(var5.hasNext()) {
         UUID var6 = (UUID)var5.next();
         if (var2.containsKey(var6)) {
            var4.put(var6, 0);
            this.negativeEffects.put(var6, 0);
            this.positiveEffects.put(var6, 0);
         }
      }

      byte var21 = 16;
      Iterator var22 = var4.keySet().iterator();

      while(var22.hasNext()) {
         UUID var7 = (UUID)var22.next();
         SimpleLocation var8 = (SimpleLocation)var2.get(var7);

         for(int var9 = var8.getX() - var21; var9 < var8.getX() + var21; ++var9) {
            int var10;
            int var11;
            for(var10 = var8.getZ() - var21; var10 < var8.getZ() + var21; ++var10) {
               for(var11 = var8.getY() - var21; var11 < var8.getY() + var21; ++var11) {
                  if (var11 < var8.getMaxY() && var11 >= var8.getMinY()) {
                     Material var12 = ((ChunkSystem)var1.get(var7)).getBlockType(var9, var11, var10);
                     boolean var13 = ((ChunkSystem)var1.get(var7)).isNether();
                     if (var12.toString().contains("LEAVE") && var11 != var8.getMinY() && ((ChunkSystem)var1.get(var7)).getBlockType(var9, var11 - 1, var10).equals(Material.AIR) && distanceTo(var9, var11, var10, var8.getX(), var8.getY(), var8.getZ()) < 8.0D) {
                        if (!var3.containsKey(var7)) {
                           var3.put(var7, new ArrayList());
                        }

                        ((List)var3.get(var7)).add(new SimpleLocation(var9, var11, var10, var8.getWorldName()));
                     }

                     if (this.tempsettings != null) {
                        synchronized(this.tempsettings.blockEffects) {
                           if (this.tempsettings.hasBlockEffect(var12)) {
                              int var15 = this.tempsettings.getBlockEffect(var12).getRange();
                              int var16 = this.tempsettings.getBlockEffect(var12).getModifier();
                              double var17 = distanceTo(var8.getX(), var8.getY(), var8.getZ(), var9, var11, var10);
                              var16 -= (int)Math.round(var17 / (double)var15 * (double)var16);
                              if (var12 == Material.NETHER_PORTAL && var13) {
                                 var16 = -var16;
                              }

                              if ((double)var15 >= var17) {
                                 if (!(var17 > 1.4D) || this.hasAnyLineOfSight(var8, new BlockProcessor.SimpleBlock(var9, var11, var10), (ChunkSystem)var1.get(var7), var17)) {
                                    if (var16 >= 0) {
                                       if (var16 > (Integer)this.positiveEffects.get(var7)) {
                                          this.positiveEffects.put(var7, var16);
                                       }
                                    } else if (var16 < (Integer)this.negativeEffects.get(var7)) {
                                       this.negativeEffects.put(var7, var16);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            var10 = 0;
            var11 = 0;
            if (this.positiveEffects.containsKey(var7)) {
               var10 = (Integer)this.positiveEffects.get(var7);
            }

            if (this.negativeEffects.containsKey(var7)) {
               var11 = (Integer)this.negativeEffects.get(var7);
            }

            var4.put(var7, var10 + var11);
         }
      }

      this.handleTemperatureResult(var4);
      this.handleParticleResults(var3);
      this.isProcessing = false;
   }

   public void handleTemperatureResult(final HashMap<UUID, Integer> var1) {
      Bukkit.getScheduler().runTaskLater(this.main, new Runnable() {
         public void run() {
            BlockProcessor.this.main.getTemperatureManager().getTempData().updateBlockEffects(var1);
         }
      }, 0L);
   }

   public void handleParticleResults(final HashMap<UUID, List<SimpleLocation>> var1) {
      Bukkit.getScheduler().runTaskLater(this.main, new Runnable() {
         public void run() {
            BlockProcessor.this.main.getParticleManager().leaveLocations = var1;
         }
      }, 0L);
   }

   public static double distanceTo(int var0, int var1, int var2, int var3, int var4, int var5) {
      return Math.sqrt(Math.pow((double)(var0 - var3), 2.0D) + Math.pow((double)(var1 - var4), 2.0D) + Math.pow((double)(var2 - var5), 2.0D));
   }

   public boolean hasLineOfSight(SimpleLocation var1, BlockProcessor.SimpleBlock var2, ChunkSystem var3, double var4) {
      BlockProcessor.SimpleBlock var6 = new BlockProcessor.SimpleBlock(var1.getX(), var1.getY(), var1.getZ());
      BlockProcessor.SimpleBlock var7 = new BlockProcessor.SimpleBlock(var1.getX(), var1.getY() + 1, var1.getZ());
      return this.hasLineOfSight(var6, var2, var3, var4) || this.hasLineOfSight(var7, var2, var3, var4);
   }

   public boolean hasAnyLineOfSight(SimpleLocation var1, BlockProcessor.SimpleBlock var2, ChunkSystem var3, double var4) {
      if (this.hasLineOfSight(var1, var2, var3, var4)) {
         return true;
      } else {
         BlockProcessor.SimpleBlock[] var6 = new BlockProcessor.SimpleBlock[]{new BlockProcessor.SimpleBlock(var2.getX(), var2.getY() + 1, var2.getZ()), new BlockProcessor.SimpleBlock(var2.getX(), var2.getY() - 1, var2.getZ()), new BlockProcessor.SimpleBlock(var2.getX(), var2.getY(), var2.getZ() - 1), new BlockProcessor.SimpleBlock(var2.getX(), var2.getY(), var2.getZ() + 1), new BlockProcessor.SimpleBlock(var2.getX() + 1, var2.getY(), var2.getZ()), new BlockProcessor.SimpleBlock(var2.getX() - 1, var2.getY(), var2.getZ())};
         BlockProcessor.SimpleBlock[] var7 = var6;
         int var8 = var6.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            BlockProcessor.SimpleBlock var10 = var7[var9];
            Material var11 = var3.getBlockType(var10.getX(), var10.getY(), var10.getZ());
            if ((var11.equals(Material.AIR) || !var11.isOccluding()) && this.hasLineOfSight(var1, var10, var3, var4)) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean hasLineOfSight(BlockProcessor.SimpleBlock var1, BlockProcessor.SimpleBlock var2, ChunkSystem var3, double var4) {
      Vector var6 = new Vector(var1.getX(), var1.getY(), var1.getZ());
      Vector var7 = new Vector(var2.getX(), var2.getY(), var2.getZ());
      Vector var8 = var6.subtract(var7).normalize();
      Vector var9 = var8.multiply(0.5D);

      for(int var10 = 0; (double)var10 < var4 * 2.2D; ++var10) {
         Vector var11 = var7.clone();
         Vector var12 = var9.clone().multiply(var10);
         var11.add(var12);
         if (var12.length() >= var4) {
            break;
         }

         int var13 = var11.getBlockX();
         int var14 = var11.getBlockY();
         int var15 = var11.getBlockZ();
         if (!var3.getBlockType(var13, var14, var15).equals(Material.AIR) && var3.getBlockType(var13, var14, var15).isOccluding()) {
            if (var13 == var1.getX() && var14 == var1.getY() && var15 == var1.getZ()) {
               return true;
            }

            return false;
         }
      }

      return true;
   }

   private class SimpleBlock {
      private int x;
      private int y;
      private int z;

      public SimpleBlock(int param2, int param3, int param4) {
         this.x = var2;
         this.y = var3;
         this.z = var4;
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
   }
}
