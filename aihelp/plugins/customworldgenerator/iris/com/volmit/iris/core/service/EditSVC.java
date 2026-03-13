package com.volmit.iris.core.service;

import com.volmit.iris.Iris;
import com.volmit.iris.core.edit.BlockEditor;
import com.volmit.iris.core.edit.BukkitBlockEditor;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.plugin.IrisService;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldUnloadEvent;

public class EditSVC implements IrisService {
   private KMap<World, BlockEditor> editors;
   public static boolean deletingWorld = false;

   public void onEnable() {
      this.editors = new KMap();
      Bukkit.getScheduler().scheduleSyncRepeatingTask(Iris.instance, this::update, 1000L, 1000L);
   }

   public void onDisable() {
      this.flushNow();
   }

   public BlockData get(World world, int x, int y, int z) {
      return this.open(var1).get(var2, var3, var4);
   }

   public void set(World world, int x, int y, int z, BlockData d) {
      this.open(var1).set(var2, var3, var4, var5);
   }

   public void setBiome(World world, int x, int y, int z, Biome d) {
      this.open(var1).setBiome(var2, var3, var4, var5);
   }

   public void setBiome(World world, int x, int z, Biome d) {
      this.open(var1).setBiome(var2, var3, var4);
   }

   public Biome getBiome(World world, int x, int y, int z) {
      return this.open(var1).getBiome(var2, var3, var4);
   }

   public Biome getBiome(World world, int x, int z) {
      return this.open(var1).getBiome(var2, var3);
   }

   @EventHandler
   public void on(WorldUnloadEvent e) {
      if (this.editors.containsKey(var1.getWorld()) && !deletingWorld) {
         ((BlockEditor)this.editors.remove(var1.getWorld())).close();
      }

   }

   public void update() {
      Iterator var1 = this.editors.k().iterator();

      while(var1.hasNext()) {
         World var2 = (World)var1.next();
         if (M.ms() - ((BlockEditor)this.editors.get(var2)).last() > 1000L) {
            ((BlockEditor)this.editors.remove(var2)).close();
         }
      }

   }

   public void flushNow() {
      Iterator var1 = this.editors.k().iterator();

      while(var1.hasNext()) {
         World var2 = (World)var1.next();
         ((BlockEditor)this.editors.remove(var2)).close();
      }

   }

   public BlockEditor open(World world) {
      if (this.editors.containsKey(var1)) {
         return (BlockEditor)this.editors.get(var1);
      } else {
         BukkitBlockEditor var2 = new BukkitBlockEditor(var1);
         this.editors.put(var1, var2);
         return var2;
      }
   }
}
