package me.casperge.realisticseasons.season;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

public class SeasonChunk {
   private int x;
   private int z;
   private String worldname;
   private Long loaded;
   private int hash;

   public SeasonChunk(String var1, int var2, int var3, Long var4) {
      this.x = var2;
      this.z = var3;
      this.worldname = var1;
      this.loaded = var4;
      this.hash = 0;
   }

   public SeasonChunk(String var1, int var2, int var3) {
      this.x = var2;
      this.z = var3;
      this.worldname = var1;
      this.loaded = 0L;
      this.hash = 0;
   }

   public SeasonChunk(Chunk var1) {
      this.x = var1.getX();
      this.z = var1.getZ();
      this.worldname = var1.getWorld().getName();
      this.hash = 0;
      this.loaded = 0L;
   }

   public int getX() {
      return this.x;
   }

   public Long getLoadTime() {
      return this.loaded;
   }

   public int getZ() {
      return this.z;
   }

   public String getWorldName() {
      return this.worldname;
   }

   public World getWorld() {
      return Bukkit.getWorld(this.worldname);
   }

   public Chunk getChunk() {
      return this.x <= 1875000 && this.z <= 1875000 && this.x >= -1875000 && this.z >= -1875000 ? Bukkit.getWorld(this.worldname).getChunkAt(this.x, this.z) : null;
   }

   public String toString() {
      return this.x + ", " + this.z + " world: " + this.worldname;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            SeasonChunk var2 = (SeasonChunk)var1;
            return var2.x == this.x && var2.z == this.z && (this.worldname == var2.worldname || this.worldname != null && this.worldname.equals(var2.worldname));
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.hash == 0) {
         this.hash = Objects.hash(new Object[]{this.x, this.z, this.worldname});
      }

      return this.hash;
   }
}
