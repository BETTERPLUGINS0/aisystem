package com.volmit.iris.util.data;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.Direction;
import com.volmit.iris.util.math.Position2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;

public class Cuboid implements Iterable<Block>, Cloneable, ConfigurationSerializable {
   protected final String worldName;
   protected int x1;
   protected int y1;
   protected int z1;
   protected int x2;
   protected int y2;
   protected int z2;

   public Cuboid(Location l1, Location l2) {
      if (!var1.getWorld().equals(var2.getWorld())) {
         throw new IllegalArgumentException("locations must be on the same world");
      } else {
         this.worldName = var1.getWorld().getName();
         this.x1 = Math.min(var1.getBlockX(), var2.getBlockX());
         this.y1 = Math.min(var1.getBlockY(), var2.getBlockY());
         this.z1 = Math.min(var1.getBlockZ(), var2.getBlockZ());
         this.x2 = Math.max(var1.getBlockX(), var2.getBlockX());
         this.y2 = Math.max(var1.getBlockY(), var2.getBlockY());
         this.z2 = Math.max(var1.getBlockZ(), var2.getBlockZ());
      }
   }

   public Cuboid(Location l1) {
      this(var1, var1);
   }

   public Cuboid(Cuboid other) {
      this(var1.getWorld().getName(), var1.x1, var1.y1, var1.z1, var1.x2, var1.y2, var1.z2);
   }

   public Cuboid(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
      this.worldName = var1.getName();
      this.x1 = Math.min(var2, var5);
      this.x2 = Math.max(var2, var5);
      this.y1 = Math.min(var3, var6);
      this.y2 = Math.max(var3, var6);
      this.z1 = Math.min(var4, var7);
      this.z2 = Math.max(var4, var7);
   }

   private Cuboid(String worldName, int x1, int y1, int z1, int x2, int y2, int z2) {
      this.worldName = var1;
      this.x1 = Math.min(var2, var5);
      this.x2 = Math.max(var2, var5);
      this.y1 = Math.min(var3, var6);
      this.y2 = Math.max(var3, var6);
      this.z1 = Math.min(var4, var7);
      this.z2 = Math.max(var4, var7);
   }

   public Cuboid(Map<String, Object> map) {
      this.worldName = (String)var1.get("worldName");
      this.x1 = (Integer)var1.get("x1");
      this.x2 = (Integer)var1.get("x2");
      this.y1 = (Integer)var1.get("y1");
      this.y2 = (Integer)var1.get("y2");
      this.z1 = (Integer)var1.get("z1");
      this.z2 = (Integer)var1.get("z2");
   }

   public KList<Entity> getEntities() {
      KList var1 = new KList();
      Iterator var2 = this.getChunks().iterator();

      while(var2.hasNext()) {
         Chunk var3 = (Chunk)var2.next();
         Entity[] var4 = var3.getEntities();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Entity var7 = var4[var6];
            if (this.contains(var7.getLocation())) {
               var1.add((Object)var7);
            }
         }
      }

      return var1;
   }

   public void set(Location l1, Location l2) {
      this.x1 = Math.min(var1.getBlockX(), var2.getBlockX());
      this.y1 = Math.min(var1.getBlockY(), var2.getBlockY());
      this.z1 = Math.min(var1.getBlockZ(), var2.getBlockZ());
      this.x2 = Math.max(var1.getBlockX(), var2.getBlockX());
      this.y2 = Math.max(var1.getBlockY(), var2.getBlockY());
      this.z2 = Math.max(var1.getBlockZ(), var2.getBlockZ());
   }

   public Map<String, Object> serialize() {
      HashMap var1 = new HashMap();
      var1.put("worldName", this.worldName);
      var1.put("x1", this.x1);
      var1.put("y1", this.y1);
      var1.put("z1", this.z1);
      var1.put("x2", this.x2);
      var1.put("y2", this.y2);
      var1.put("z2", this.z2);
      return var1;
   }

   public Cuboid flatten(int level) {
      return new Cuboid(this.getWorld(), this.x1, var1, this.z1, this.x2, var1, this.z2);
   }

   public Location getLowerNE() {
      return new Location(this.getWorld(), (double)this.x1, (double)this.y1, (double)this.z1);
   }

   public Location getUpperSW() {
      return new Location(this.getWorld(), (double)this.x2, (double)this.y2, (double)this.z2);
   }

   public Location getCenter() {
      int var1 = this.getUpperX() + 1;
      int var2 = this.getUpperY() + 1;
      int var3 = this.getUpperZ() + 1;
      return new Location(this.getWorld(), (double)this.getLowerX() + (double)(var1 - this.getLowerX()) / 2.0D, (double)this.getLowerY() + (double)(var2 - this.getLowerY()) / 2.0D, (double)this.getLowerZ() + (double)(var3 - this.getLowerZ()) / 2.0D);
   }

   public World getWorld() {
      World var1 = Bukkit.getWorld(this.worldName);
      if (var1 == null) {
         throw new IllegalStateException("world '" + this.worldName + "' is not loaded");
      } else {
         return var1;
      }
   }

   public int getSizeX() {
      return this.x2 - this.x1 + 1;
   }

   public int getSizeY() {
      return this.y2 - this.y1 + 1;
   }

   public int getSizeZ() {
      return this.z2 - this.z1 + 1;
   }

   public Dimension getDimension() {
      return new Dimension(this.getSizeX(), this.getSizeY(), this.getSizeZ());
   }

   public int getLowerX() {
      return this.x1;
   }

   public int getLowerY() {
      return this.y1;
   }

   public int getLowerZ() {
      return this.z1;
   }

   public int getUpperX() {
      return this.x2;
   }

   public int getUpperY() {
      return this.y2;
   }

   public int getUpperZ() {
      return this.z2;
   }

   public Block[] corners() {
      Block[] var1 = new Block[8];
      World var2 = this.getWorld();
      var1[0] = var2.getBlockAt(this.x1, this.y1, this.z1);
      var1[1] = var2.getBlockAt(this.x1, this.y1, this.z2);
      var1[2] = var2.getBlockAt(this.x1, this.y2, this.z1);
      var1[3] = var2.getBlockAt(this.x1, this.y2, this.z2);
      var1[4] = var2.getBlockAt(this.x2, this.y1, this.z1);
      var1[5] = var2.getBlockAt(this.x2, this.y1, this.z2);
      var1[6] = var2.getBlockAt(this.x2, this.y2, this.z1);
      var1[7] = var2.getBlockAt(this.x2, this.y2, this.z2);
      return var1;
   }

   public Cuboid expand(Cuboid.CuboidDirection dir, int amount) {
      Cuboid var10000;
      switch(var1.ordinal()) {
      case 0:
         var10000 = new Cuboid(this.worldName, this.x1 - var2, this.y1, this.z1, this.x2, this.y2, this.z2);
         break;
      case 1:
         var10000 = new Cuboid(this.worldName, this.x1, this.y1, this.z1 - var2, this.x2, this.y2, this.z2);
         break;
      case 2:
         var10000 = new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2 + var2, this.y2, this.z2);
         break;
      case 3:
         var10000 = new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2 + var2);
         break;
      case 4:
         var10000 = new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2 + var2, this.z2);
         break;
      case 5:
         var10000 = new Cuboid(this.worldName, this.x1, this.y1 - var2, this.z1, this.x2, this.y2, this.z2);
         break;
      default:
         throw new IllegalArgumentException("invalid direction " + String.valueOf(var1));
      }

      return var10000;
   }

   public Cuboid expand(Direction dir, int amount) {
      int var3 = var1.toVector().getBlockX() == 1 ? var2 : 0;
      int var4 = var1.toVector().getBlockX() == -1 ? -var2 : 0;
      int var5 = var1.toVector().getBlockY() == 1 ? var2 : 0;
      int var6 = var1.toVector().getBlockY() == -1 ? -var2 : 0;
      int var7 = var1.toVector().getBlockZ() == 1 ? var2 : 0;
      int var8 = var1.toVector().getBlockZ() == -1 ? -var2 : 0;
      return new Cuboid(this.worldName, this.x1 + var4, this.y1 + var6, this.z1 + var8, this.x2 + var3, this.y2 + var5, this.z2 + var7);
   }

   public Cuboid shift(Cuboid.CuboidDirection dir, int amount) {
      return this.expand(var1, var2).expand(var1.opposite(), -var2);
   }

   public Cuboid outset(Cuboid.CuboidDirection dir, int amount) {
      Cuboid var10000;
      switch(var1.ordinal()) {
      case 6:
         var10000 = this.expand(Cuboid.CuboidDirection.North, var2).expand(Cuboid.CuboidDirection.South, var2).expand(Cuboid.CuboidDirection.East, var2).expand(Cuboid.CuboidDirection.West, var2);
         break;
      case 7:
         var10000 = this.expand(Cuboid.CuboidDirection.Down, var2).expand(Cuboid.CuboidDirection.Up, var2);
         break;
      case 8:
         var10000 = this.outset(Cuboid.CuboidDirection.Horizontal, var2).outset(Cuboid.CuboidDirection.Vertical, var2);
         break;
      default:
         throw new IllegalArgumentException("invalid direction " + String.valueOf(var1));
      }

      Cuboid var3 = var10000;
      return var3;
   }

   public Cuboid inset(Cuboid.CuboidDirection dir, int amount) {
      return this.outset(var1, -var2);
   }

   public boolean contains(int x, int y, int z) {
      return var1 >= this.x1 && var1 <= this.x2 && var2 >= this.y1 && var2 <= this.y2 && var3 >= this.z1 && var3 <= this.z2;
   }

   public boolean contains(Block b) {
      return this.contains(var1.getLocation());
   }

   public boolean contains(Location l) {
      return this.worldName.equals(var1.getWorld().getName()) && this.contains(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ());
   }

   public int volume() {
      return this.getSizeX() * this.getSizeY() * this.getSizeZ();
   }

   public byte averageLightLevel() {
      long var1 = 0L;
      int var3 = 0;
      Iterator var4 = this.iterator();

      while(var4.hasNext()) {
         Block var5 = (Block)var4.next();
         if (var5.isEmpty()) {
            var1 += (long)var5.getLightLevel();
            ++var3;
         }
      }

      return var3 > 0 ? (byte)((int)(var1 / (long)var3)) : 0;
   }

   public Cuboid contract() {
      return this.contract(Cuboid.CuboidDirection.Down).contract(Cuboid.CuboidDirection.South).contract(Cuboid.CuboidDirection.East).contract(Cuboid.CuboidDirection.Up).contract(Cuboid.CuboidDirection.North).contract(Cuboid.CuboidDirection.West);
   }

   public Cuboid contract(Cuboid.CuboidDirection dir) {
      Cuboid var2 = this.getFace(var1.opposite());
      switch(var1.ordinal()) {
      case 0:
         while(var2.containsOnly(Material.AIR) && var2.getLowerX() > this.getLowerX()) {
            var2 = var2.shift(Cuboid.CuboidDirection.North, 1);
         }

         return new Cuboid(this.worldName, this.x1, this.y1, this.z1, var2.getUpperX(), this.y2, this.z2);
      case 1:
         while(var2.containsOnly(Material.AIR) && var2.getLowerZ() > this.getLowerZ()) {
            var2 = var2.shift(Cuboid.CuboidDirection.East, 1);
         }

         return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, var2.getUpperZ());
      case 2:
         while(var2.containsOnly(Material.AIR) && var2.getUpperX() < this.getUpperX()) {
            var2 = var2.shift(Cuboid.CuboidDirection.South, 1);
         }

         return new Cuboid(this.worldName, var2.getLowerX(), this.y1, this.z1, this.x2, this.y2, this.z2);
      case 3:
         while(var2.containsOnly(Material.AIR) && var2.getUpperZ() < this.getUpperZ()) {
            var2 = var2.shift(Cuboid.CuboidDirection.West, 1);
         }

         return new Cuboid(this.worldName, this.x1, this.y1, var2.getLowerZ(), this.x2, this.y2, this.z2);
      case 4:
         while(var2.containsOnly(Material.AIR) && var2.getUpperY() < this.getUpperY()) {
            var2 = var2.shift(Cuboid.CuboidDirection.Up, 1);
         }

         return new Cuboid(this.worldName, this.x1, var2.getLowerY(), this.z1, this.x2, this.y2, this.z2);
      case 5:
         while(var2.containsOnly(Material.AIR) && var2.getLowerY() > this.getLowerY()) {
            var2 = var2.shift(Cuboid.CuboidDirection.Down, 1);
         }

         return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, var2.getUpperY(), this.z2);
      default:
         throw new IllegalArgumentException("Invalid direction " + String.valueOf(var1));
      }
   }

   public Cuboid getFace(Cuboid.CuboidDirection dir) {
      Cuboid var10000;
      switch(var1.ordinal()) {
      case 0:
         var10000 = new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x1, this.y2, this.z2);
         break;
      case 1:
         var10000 = new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z1);
         break;
      case 2:
         var10000 = new Cuboid(this.worldName, this.x2, this.y1, this.z1, this.x2, this.y2, this.z2);
         break;
      case 3:
         var10000 = new Cuboid(this.worldName, this.x1, this.y1, this.z2, this.x2, this.y2, this.z2);
         break;
      case 4:
         var10000 = new Cuboid(this.worldName, this.x1, this.y2, this.z1, this.x2, this.y2, this.z2);
         break;
      case 5:
         var10000 = new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y1, this.z2);
         break;
      default:
         throw new IllegalArgumentException("Invalid direction " + String.valueOf(var1));
      }

      return var10000;
   }

   public boolean containsOnly(Material material) {
      Iterator var2 = this.iterator();

      Block var3;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         var3 = (Block)var2.next();
      } while(var3.getType() == var1);

      return false;
   }

   public Cuboid getBoundingCuboid(Cuboid other) {
      if (var1 == null) {
         return this;
      } else {
         int var2 = Math.min(this.getLowerX(), var1.getLowerX());
         int var3 = Math.min(this.getLowerY(), var1.getLowerY());
         int var4 = Math.min(this.getLowerZ(), var1.getLowerZ());
         int var5 = Math.max(this.getUpperX(), var1.getUpperX());
         int var6 = Math.max(this.getUpperY(), var1.getUpperY());
         int var7 = Math.max(this.getUpperZ(), var1.getUpperZ());
         return new Cuboid(this.worldName, var2, var3, var4, var5, var6, var7);
      }
   }

   public Block getRelativeBlock(int x, int y, int z) {
      return this.getWorld().getBlockAt(this.x1 + var1, this.y1 + var2, this.z1 + var3);
   }

   public Block getRelativeBlock(World w, int x, int y, int z) {
      return var1.getBlockAt(this.x1 + var2, this.y1 + var3, this.z1 + var4);
   }

   public List<Chunk> getChunks() {
      ArrayList var1 = new ArrayList();
      World var2 = this.getWorld();
      int var3 = this.getLowerX() & -16;
      int var4 = this.getUpperX() & -16;
      int var5 = this.getLowerZ() & -16;
      int var6 = this.getUpperZ() & -16;

      for(int var7 = var3; var7 <= var4; var7 += 16) {
         for(int var8 = var5; var8 <= var6; var8 += 16) {
            var1.add(var2.getChunkAt(var7 >> 4, var8 >> 4));
         }
      }

      return var1;
   }

   public Iterator<Block> iterator() {
      return new Cuboid.CuboidIterator(this.getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
   }

   public Iterator<Block> chunkedIterator() {
      return new Cuboid.ChunkedCuboidIterator(this.getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
   }

   public Cuboid clone() {
      return new Cuboid(this);
   }

   public String toString() {
      return "Cuboid: " + this.worldName + "," + this.x1 + "," + this.y1 + "," + this.z1 + "=>" + this.x2 + "," + this.y2 + "," + this.z2;
   }

   public static enum CuboidDirection {
      North,
      East,
      South,
      West,
      Up,
      Down,
      Horizontal,
      Vertical,
      Both,
      Unknown;

      public Cuboid.CuboidDirection opposite() {
         Cuboid.CuboidDirection var10000;
         switch(this.ordinal()) {
         case 0:
            var10000 = South;
            break;
         case 1:
            var10000 = West;
            break;
         case 2:
            var10000 = North;
            break;
         case 3:
            var10000 = East;
            break;
         case 4:
            var10000 = Down;
            break;
         case 5:
            var10000 = Up;
            break;
         case 6:
            var10000 = Vertical;
            break;
         case 7:
            var10000 = Horizontal;
            break;
         case 8:
            var10000 = Both;
            break;
         default:
            var10000 = Unknown;
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Cuboid.CuboidDirection[] $values() {
         return new Cuboid.CuboidDirection[]{North, East, South, West, Up, Down, Horizontal, Vertical, Both, Unknown};
      }
   }

   public static class CuboidIterator implements Iterator<Block> {
      private final World w;
      private final int baseX;
      private final int baseY;
      private final int baseZ;
      private final int sizeX;
      private final int sizeY;
      private final int sizeZ;
      private int x;
      private int y;
      private int z;

      public CuboidIterator(World w, int x1, int y1, int z1, int x2, int y2, int z2) {
         this.w = var1;
         this.baseX = var2;
         this.baseY = var3;
         this.baseZ = var4;
         this.sizeX = Math.abs(var5 - var2) + 1;
         this.sizeY = Math.abs(var6 - var3) + 1;
         this.sizeZ = Math.abs(var7 - var4) + 1;
         this.x = this.y = this.z = 0;
      }

      public boolean hasNext() {
         return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ;
      }

      public Block next() {
         Block var1 = this.w.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
         if (++this.x >= this.sizeX) {
            this.x = 0;
            if (++this.y >= this.sizeY) {
               this.y = 0;
               ++this.z;
            }
         }

         return var1;
      }

      public void remove() {
      }
   }

   public static class ChunkedCuboidIterator implements Iterator<Block> {
      private final World w;
      private final int minRX;
      private final int minY;
      private final int minRZ;
      private final int maxRX;
      private final int maxY;
      private final int maxRZ;
      private final int minCX;
      private final int minCZ;
      private final int maxCX;
      private final int maxCZ;
      private int mX;
      private int mZ;
      private int bX;
      private int rX;
      private int rZ;
      private int y;
      private Position2 chunk;
      private int cX;
      private int cZ;

      public ChunkedCuboidIterator(World w, int x1, int y1, int z1, int x2, int y2, int z2) {
         this.w = var1;
         this.minY = Math.min(var3, var6);
         this.maxY = Math.max(var3, var6);
         int var8 = Math.min(var2, var5);
         int var9 = Math.min(var4, var7);
         int var10 = Math.max(var2, var5);
         int var11 = Math.max(var4, var7);
         this.minRX = var8 & 15;
         this.minRZ = var9 & 15;
         this.maxRX = var10 & 15;
         this.maxRZ = var11 & 15;
         this.minCX = var8 >> 4;
         this.minCZ = var9 >> 4;
         this.maxCX = var10 >> 4;
         this.maxCZ = var11 >> 4;
         this.cX = this.minCX;
         this.cZ = this.minCZ;
         this.rX = var8 & 15;
         this.rZ = var9 & 15;
         this.y = this.minY;
      }

      public boolean hasNext() {
         return this.chunk != null || this.hasNextChunk();
      }

      public boolean hasNextChunk() {
         return this.cX <= this.maxCX && this.cZ <= this.maxCZ;
      }

      public Block next() {
         if (this.chunk == null) {
            this.chunk = new Position2(this.cX, this.cZ);
            if (++this.cX > this.maxCX) {
               this.cX = this.minCX;
               ++this.cZ;
            }

            this.mX = this.chunk.getX() == this.maxCX ? this.maxRX : 15;
            this.mZ = this.chunk.getZ() == this.maxCZ ? this.maxRZ : 15;
            this.rX = this.bX = this.chunk.getX() == this.minCX ? this.minRX : 0;
            this.rZ = this.chunk.getZ() == this.minCZ ? this.minRZ : 0;
         }

         Block var1 = this.w.getBlockAt((this.chunk.getX() << 4) + this.rX, this.y, (this.chunk.getZ() << 4) + this.rZ);
         if (++this.y > this.maxY) {
            this.y = this.minY;
            if (++this.rX > this.mX) {
               if (++this.rZ > this.mZ) {
                  this.chunk = null;
                  return var1;
               }

               this.rX = this.bX;
            }
         }

         return var1;
      }

      public void remove() {
      }
   }
}
