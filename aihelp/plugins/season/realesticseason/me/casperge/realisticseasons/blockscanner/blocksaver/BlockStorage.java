package me.casperge.realisticseasons.blockscanner.blocksaver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.blockscanner.SimpleLocation;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class BlockStorage {
   private RealisticSeasons main;
   private HashMap<String, HashSet<SimpleLocation>> placedFlowers = new HashMap();
   private HashMap<String, HashSet<SimpleLocation>> placedPresents = new HashMap();
   private HashMap<String, HashSet<SimpleLocation>> placedEggs = new HashMap();
   private AtomicBoolean isReading = new AtomicBoolean(false);

   public BlockStorage(RealisticSeasons var1) {
      this.main = var1;
   }

   public void load(ConfigurationSection var1, StoredBlockType var2) {
      Iterator var4;
      String var5;
      HashSet var6;
      List var7;
      Iterator var8;
      String var9;
      String[] var10;
      if (var2 == StoredBlockType.FLOWER) {
         synchronized(this.placedFlowers) {
            var4 = var1.getKeys(false).iterator();

            while(var4.hasNext()) {
               var5 = (String)var4.next();
               var6 = new HashSet();
               var7 = var1.getStringList(var5);
               var8 = var7.iterator();

               while(var8.hasNext()) {
                  var9 = (String)var8.next();
                  var10 = var9.split(",");
                  var6.add(new SimpleLocation(Integer.valueOf(var10[0]), Integer.valueOf(var10[1]), Integer.valueOf(var10[2]), var10[3]));
               }

               this.placedFlowers.put(var5, var6);
            }
         }
      } else if (var2 == StoredBlockType.PRESENT) {
         synchronized(this.placedPresents) {
            var4 = var1.getKeys(false).iterator();

            while(var4.hasNext()) {
               var5 = (String)var4.next();
               var6 = new HashSet();
               var7 = var1.getStringList(var5);
               var8 = var7.iterator();

               while(var8.hasNext()) {
                  var9 = (String)var8.next();
                  var10 = var9.split(",");
                  var6.add(new SimpleLocation(Integer.valueOf(var10[0]), Integer.valueOf(var10[1]), Integer.valueOf(var10[2]), var10[3]));
               }

               this.placedPresents.put(var5, var6);
            }
         }
      } else if (var2 == StoredBlockType.EGG) {
         synchronized(this.placedEggs) {
            var4 = var1.getKeys(false).iterator();

            while(var4.hasNext()) {
               var5 = (String)var4.next();
               var6 = new HashSet();
               var7 = var1.getStringList(var5);
               var8 = var7.iterator();

               while(var8.hasNext()) {
                  var9 = (String)var8.next();
                  var10 = var9.split(",");
                  var6.add(new SimpleLocation(Integer.valueOf(var10[0]), Integer.valueOf(var10[1]), Integer.valueOf(var10[2]), var10[3]));
               }

               this.placedEggs.put(var5, var6);
            }
         }
      }

   }

   public void save(YamlConfiguration var1) {
      var1.set("placedblocks", (Object)null);
      var1.set("placedpresents", (Object)null);
      var1.set("placedeggs", (Object)null);
      Iterator var3;
      String var4;
      ArrayList var5;
      Iterator var6;
      SimpleLocation var7;
      synchronized(this.placedFlowers) {
         var3 = this.placedFlowers.keySet().iterator();

         while(true) {
            if (!var3.hasNext()) {
               break;
            }

            var4 = (String)var3.next();
            var5 = new ArrayList();
            var6 = ((HashSet)this.placedFlowers.get(var4)).iterator();

            while(var6.hasNext()) {
               var7 = (SimpleLocation)var6.next();
               var5.add(var7.getX() + "," + var7.getY() + "," + var7.getZ() + "," + var7.getWorldName());
            }

            var1.set("placedblocks." + var4, var5);
         }
      }

      synchronized(this.placedPresents) {
         var3 = this.placedPresents.keySet().iterator();

         while(var3.hasNext()) {
            var4 = (String)var3.next();
            var5 = new ArrayList();
            var6 = ((HashSet)this.placedPresents.get(var4)).iterator();

            while(var6.hasNext()) {
               var7 = (SimpleLocation)var6.next();
               var5.add(var7.getX() + "," + var7.getY() + "," + var7.getZ() + "," + var7.getWorldName());
            }

            var1.set("placedpresents." + var4, var5);
         }
      }

      synchronized(this.placedEggs) {
         var3 = this.placedEggs.keySet().iterator();

         while(var3.hasNext()) {
            var4 = (String)var3.next();
            var5 = new ArrayList();
            var6 = ((HashSet)this.placedEggs.get(var4)).iterator();

            while(var6.hasNext()) {
               var7 = (SimpleLocation)var6.next();
               var5.add(var7.getX() + "," + var7.getY() + "," + var7.getZ() + "," + var7.getWorldName());
            }

            var1.set("placedeggs." + var4, var5);
         }

      }
   }

   public void logPlacement(Location var1, final StoredBlockType var2) {
      final SimpleLocation var3 = new SimpleLocation(var1, 0, 0);
      Bukkit.getScheduler().runTaskAsynchronously(this.main, new Runnable() {
         public void run() {
            if (BlockStorage.this.isReading.get()) {
               int var1 = 0;

               while(true) {
                  try {
                     Thread.sleep(100L);
                  } catch (InterruptedException var11) {
                     var11.printStackTrace();
                     break;
                  }

                  ++var1;
                  if (!BlockStorage.this.isReading.get()) {
                     break;
                  }

                  if (var1 > 200) {
                     return;
                  }
               }
            }

            Iterator var2x;
            String var3x;
            HashSet var4;
            HashSet var12;
            if (var2 == StoredBlockType.FLOWER) {
               synchronized(BlockStorage.this.placedFlowers) {
                  var2x = BlockStorage.this.placedFlowers.keySet().iterator();

                  do {
                     if (!var2x.hasNext()) {
                        var12 = new HashSet();
                        var12.add(var3);
                        BlockStorage.this.placedFlowers.put(var3.getWorldName(), var12);
                        return;
                     }

                     var3x = (String)var2x.next();
                  } while(!var3x.equalsIgnoreCase(var3.getWorldName()));

                  var4 = (HashSet)BlockStorage.this.placedFlowers.get(var3x);
                  var4.add(var3);
                  BlockStorage.this.placedFlowers.put(var3.getWorldName(), var4);
               }
            } else if (var2 == StoredBlockType.PRESENT) {
               synchronized(BlockStorage.this.placedPresents) {
                  var2x = BlockStorage.this.placedPresents.keySet().iterator();

                  do {
                     if (!var2x.hasNext()) {
                        var12 = new HashSet();
                        var12.add(var3);
                        BlockStorage.this.placedPresents.put(var3.getWorldName(), var12);
                        return;
                     }

                     var3x = (String)var2x.next();
                  } while(!var3x.equalsIgnoreCase(var3.getWorldName()));

                  var4 = (HashSet)BlockStorage.this.placedPresents.get(var3x);
                  var4.add(var3);
                  BlockStorage.this.placedPresents.put(var3.getWorldName(), var4);
               }
            } else if (var2 == StoredBlockType.EGG) {
               synchronized(BlockStorage.this.placedEggs) {
                  var2x = BlockStorage.this.placedEggs.keySet().iterator();

                  do {
                     if (!var2x.hasNext()) {
                        var12 = new HashSet();
                        var12.add(var3);
                        BlockStorage.this.placedEggs.put(var3.getWorldName(), var12);
                        return;
                     }

                     var3x = (String)var2x.next();
                  } while(!var3x.equalsIgnoreCase(var3.getWorldName()));

                  var4 = (HashSet)BlockStorage.this.placedEggs.get(var3x);
                  var4.add(var3);
                  BlockStorage.this.placedEggs.put(var3.getWorldName(), var4);
               }
            }
         }
      });
   }

   public void logBreak(Location var1, final StoredBlockType var2) {
      final SimpleLocation var3 = new SimpleLocation(var1, 0, 0);
      Bukkit.getScheduler().runTaskAsynchronously(this.main, new Runnable() {
         public void run() {
            if (BlockStorage.this.isReading.get()) {
               int var1 = 0;

               while(true) {
                  try {
                     Thread.sleep(200L);
                  } catch (InterruptedException var10) {
                     var10.printStackTrace();
                     break;
                  }

                  ++var1;
                  if (!BlockStorage.this.isReading.get()) {
                     break;
                  }

                  if (var1 > 20) {
                     return;
                  }
               }
            }

            Iterator var2x;
            String var3x;
            if (var2 == StoredBlockType.FLOWER) {
               synchronized(BlockStorage.this.placedFlowers) {
                  var2x = BlockStorage.this.placedFlowers.keySet().iterator();

                  while(var2x.hasNext()) {
                     var3x = (String)var2x.next();
                     if (var3x.equalsIgnoreCase(var3.getWorldName()) && ((HashSet)BlockStorage.this.placedFlowers.get(var3x)).contains(var3)) {
                        ((HashSet)BlockStorage.this.placedFlowers.get(var3x)).remove(var3);
                     }
                  }

               }
            } else if (var2 == StoredBlockType.PRESENT) {
               synchronized(BlockStorage.this.placedPresents) {
                  var2x = BlockStorage.this.placedPresents.keySet().iterator();

                  while(var2x.hasNext()) {
                     var3x = (String)var2x.next();
                     if (var3x.equalsIgnoreCase(var3.getWorldName()) && ((HashSet)BlockStorage.this.placedPresents.get(var3x)).contains(var3)) {
                        ((HashSet)BlockStorage.this.placedPresents.get(var3x)).remove(var3);
                     }
                  }

               }
            } else if (var2 == StoredBlockType.EGG) {
               synchronized(BlockStorage.this.placedEggs) {
                  var2x = BlockStorage.this.placedEggs.keySet().iterator();

                  while(var2x.hasNext()) {
                     var3x = (String)var2x.next();
                     if (var3x.equalsIgnoreCase(var3.getWorldName()) && ((HashSet)BlockStorage.this.placedEggs.get(var3x)).contains(var3)) {
                        ((HashSet)BlockStorage.this.placedEggs.get(var3x)).remove(var3);
                     }
                  }

               }
            }
         }
      });
   }

   public boolean isStored(Location var1, StoredBlockType var2) {
      if (!this.main.getSettings().keepPlayerPlacedPlants && var2 == StoredBlockType.FLOWER) {
         return false;
      } else {
         this.isReading.set(true);
         Iterator var4;
         String var5;
         SimpleLocation var6;
         if (var2 == StoredBlockType.FLOWER) {
            synchronized(this.placedFlowers) {
               var4 = this.placedFlowers.keySet().iterator();

               while(var4.hasNext()) {
                  var5 = (String)var4.next();
                  if (var5.equalsIgnoreCase(var1.getWorld().getName())) {
                     var6 = new SimpleLocation(var1, 0, 0);
                     if (((HashSet)this.placedFlowers.get(var5)).contains(var6)) {
                        this.isReading.set(false);
                        return true;
                     }
                     break;
                  }
               }
            }
         } else if (var2 == StoredBlockType.PRESENT) {
            synchronized(this.placedPresents) {
               var4 = this.placedPresents.keySet().iterator();

               while(var4.hasNext()) {
                  var5 = (String)var4.next();
                  if (var5.equalsIgnoreCase(var1.getWorld().getName())) {
                     var6 = new SimpleLocation(var1, 0, 0);
                     if (((HashSet)this.placedPresents.get(var5)).contains(var6)) {
                        this.isReading.set(false);
                        return true;
                     }
                     break;
                  }
               }
            }
         } else if (var2 == StoredBlockType.EGG) {
            synchronized(this.placedEggs) {
               var4 = this.placedEggs.keySet().iterator();

               while(var4.hasNext()) {
                  var5 = (String)var4.next();
                  if (var5.equalsIgnoreCase(var1.getWorld().getName())) {
                     var6 = new SimpleLocation(var1, 0, 0);
                     if (((HashSet)this.placedEggs.get(var5)).contains(var6)) {
                        this.isReading.set(false);
                        return true;
                     }
                     break;
                  }
               }
            }
         }

         this.isReading.set(false);
         return false;
      }
   }

   public List<SimpleLocation> getPresents(Chunk var1) {
      this.isReading.set(true);
      ArrayList var2 = new ArrayList();
      synchronized(this.placedPresents) {
         Iterator var4 = this.placedPresents.keySet().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            if (var5.equalsIgnoreCase(var1.getWorld().getName())) {
               Iterator var6 = ((HashSet)this.placedPresents.get(var5)).iterator();

               while(var6.hasNext()) {
                  SimpleLocation var7 = (SimpleLocation)var6.next();
                  if (var7.getX() >> 4 == var1.getX() && var7.getZ() >> 4 == var1.getZ()) {
                     var2.add(var7);
                  }
               }
               break;
            }
         }
      }

      this.isReading.set(false);
      return var2;
   }

   public List<SimpleLocation> getEggs(Chunk var1) {
      this.isReading.set(true);
      ArrayList var2 = new ArrayList();
      synchronized(this.placedEggs) {
         Iterator var4 = this.placedEggs.keySet().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            if (var5.equalsIgnoreCase(var1.getWorld().getName())) {
               Iterator var6 = ((HashSet)this.placedEggs.get(var5)).iterator();

               while(var6.hasNext()) {
                  SimpleLocation var7 = (SimpleLocation)var6.next();
                  if (var7.getX() >> 4 == var1.getX() && var7.getZ() >> 4 == var1.getZ()) {
                     var2.add(var7);
                  }
               }
               break;
            }
         }
      }

      this.isReading.set(false);
      return var2;
   }
}
