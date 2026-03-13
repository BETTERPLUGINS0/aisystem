package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.util.collection.KList;
import java.io.File;
import java.util.Collection;
import java.util.List;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;

public class IrisWorld {
   private static final KList<Player> NO_PLAYERS = new KList();
   private static final KList<? extends Entity> NO_ENTITIES = new KList();
   private String name;
   private File worldFolder;
   private long seed;
   private Environment environment;
   private World realWorld;
   private int minHeight;
   private int maxHeight;

   public static IrisWorld fromWorld(World world) {
      return bindWorld(builder().build(), var0);
   }

   private static IrisWorld bindWorld(IrisWorld iw, World world) {
      return var0.name(var1.getName()).worldFolder(var1.getWorldFolder()).minHeight(var1.getMinHeight()).maxHeight(var1.getMaxHeight()).realWorld(var1).environment(var1.getEnvironment());
   }

   public long getRawWorldSeed() {
      return this.seed;
   }

   public void setRawWorldSeed(long seed) {
      this.seed = var1;
   }

   public boolean tryGetRealWorld() {
      if (this.hasRealWorld()) {
         return true;
      } else {
         World var1 = Bukkit.getWorld(this.name);
         if (var1 != null) {
            this.realWorld = var1;
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean hasRealWorld() {
      return this.realWorld != null;
   }

   public List<Player> getPlayers() {
      return (List)(this.hasRealWorld() ? this.realWorld().getPlayers() : NO_PLAYERS);
   }

   public void evacuate() {
      if (this.hasRealWorld()) {
         IrisToolbelt.evacuate(this.realWorld());
      }

   }

   public void bind(WorldInfo worldInfo) {
      this.name(var1.getName()).worldFolder(new File(Bukkit.getWorldContainer(), var1.getName())).minHeight(var1.getMinHeight()).maxHeight(var1.getMaxHeight()).environment(var1.getEnvironment());
   }

   public void bind(World world) {
      if (!this.hasRealWorld()) {
         bindWorld(this, var1);
      }
   }

   public Location spawnLocation() {
      if (this.hasRealWorld()) {
         return this.realWorld().getSpawnLocation();
      } else {
         Iris.error("This world is not real yet, cannot get spawn location! HEADLESS!");
         return null;
      }
   }

   public <T extends Entity> Collection<? extends T> getEntitiesByClass(Class<T> t) {
      return (Collection)(this.hasRealWorld() ? this.realWorld().getEntitiesByClass(var1) : NO_ENTITIES);
   }

   public int getHeight() {
      return this.maxHeight - this.minHeight;
   }

   @Generated
   IrisWorld(final String name, final File worldFolder, final long seed, final Environment environment, final World realWorld, final int minHeight, final int maxHeight) {
      this.name = var1;
      this.worldFolder = var2;
      this.seed = var3;
      this.environment = var5;
      this.realWorld = var6;
      this.minHeight = var7;
      this.maxHeight = var8;
   }

   @Generated
   public static IrisWorld.IrisWorldBuilder builder() {
      return new IrisWorld.IrisWorldBuilder();
   }

   @Generated
   public String name() {
      return this.name;
   }

   @Generated
   public File worldFolder() {
      return this.worldFolder;
   }

   @Generated
   public Environment environment() {
      return this.environment;
   }

   @Generated
   public World realWorld() {
      return this.realWorld;
   }

   @Generated
   public int minHeight() {
      return this.minHeight;
   }

   @Generated
   public int maxHeight() {
      return this.maxHeight;
   }

   @Generated
   public IrisWorld name(final String name) {
      this.name = var1;
      return this;
   }

   @Generated
   public IrisWorld worldFolder(final File worldFolder) {
      this.worldFolder = var1;
      return this;
   }

   @Generated
   public IrisWorld environment(final Environment environment) {
      this.environment = var1;
      return this;
   }

   @Generated
   public IrisWorld realWorld(final World realWorld) {
      this.realWorld = var1;
      return this;
   }

   @Generated
   public IrisWorld minHeight(final int minHeight) {
      this.minHeight = var1;
      return this;
   }

   @Generated
   public IrisWorld maxHeight(final int maxHeight) {
      this.maxHeight = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisWorld)) {
         return false;
      } else {
         IrisWorld var2 = (IrisWorld)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.seed != var2.seed) {
            return false;
         } else if (this.minHeight() != var2.minHeight()) {
            return false;
         } else if (this.maxHeight() != var2.maxHeight()) {
            return false;
         } else {
            String var3 = this.name();
            String var4 = var2.name();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            label59: {
               File var5 = this.worldFolder();
               File var6 = var2.worldFolder();
               if (var5 == null) {
                  if (var6 == null) {
                     break label59;
                  }
               } else if (var5.equals(var6)) {
                  break label59;
               }

               return false;
            }

            Environment var7 = this.environment();
            Environment var8 = var2.environment();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            World var9 = this.realWorld();
            World var10 = var2.realWorld();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisWorld;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = this.seed;
      int var9 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      var9 = var9 * 59 + this.minHeight();
      var9 = var9 * 59 + this.maxHeight();
      String var5 = this.name();
      var9 = var9 * 59 + (var5 == null ? 43 : var5.hashCode());
      File var6 = this.worldFolder();
      var9 = var9 * 59 + (var6 == null ? 43 : var6.hashCode());
      Environment var7 = this.environment();
      var9 = var9 * 59 + (var7 == null ? 43 : var7.hashCode());
      World var8 = this.realWorld();
      var9 = var9 * 59 + (var8 == null ? 43 : var8.hashCode());
      return var9;
   }

   @Generated
   public String toString() {
      String var10000 = this.name();
      return "IrisWorld(name=" + var10000 + ", worldFolder=" + String.valueOf(this.worldFolder()) + ", seed=" + this.seed + ", environment=" + String.valueOf(this.environment()) + ", realWorld=" + String.valueOf(this.realWorld()) + ", minHeight=" + this.minHeight() + ", maxHeight=" + this.maxHeight() + ")";
   }

   @Generated
   public static class IrisWorldBuilder {
      @Generated
      private String name;
      @Generated
      private File worldFolder;
      @Generated
      private long seed;
      @Generated
      private Environment environment;
      @Generated
      private World realWorld;
      @Generated
      private int minHeight;
      @Generated
      private int maxHeight;

      @Generated
      IrisWorldBuilder() {
      }

      @Generated
      public IrisWorld.IrisWorldBuilder name(final String name) {
         this.name = var1;
         return this;
      }

      @Generated
      public IrisWorld.IrisWorldBuilder worldFolder(final File worldFolder) {
         this.worldFolder = var1;
         return this;
      }

      @Generated
      public IrisWorld.IrisWorldBuilder seed(final long seed) {
         this.seed = var1;
         return this;
      }

      @Generated
      public IrisWorld.IrisWorldBuilder environment(final Environment environment) {
         this.environment = var1;
         return this;
      }

      @Generated
      public IrisWorld.IrisWorldBuilder realWorld(final World realWorld) {
         this.realWorld = var1;
         return this;
      }

      @Generated
      public IrisWorld.IrisWorldBuilder minHeight(final int minHeight) {
         this.minHeight = var1;
         return this;
      }

      @Generated
      public IrisWorld.IrisWorldBuilder maxHeight(final int maxHeight) {
         this.maxHeight = var1;
         return this;
      }

      @Generated
      public IrisWorld build() {
         return new IrisWorld(this.name, this.worldFolder, this.seed, this.environment, this.realWorld, this.minHeight, this.maxHeight);
      }

      @Generated
      public String toString() {
         String var10000 = this.name;
         return "IrisWorld.IrisWorldBuilder(name=" + var10000 + ", worldFolder=" + String.valueOf(this.worldFolder) + ", seed=" + this.seed + ", environment=" + String.valueOf(this.environment) + ", realWorld=" + String.valueOf(this.realWorld) + ", minHeight=" + this.minHeight + ", maxHeight=" + this.maxHeight + ")";
      }
   }
}
