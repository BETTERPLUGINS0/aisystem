package com.volmit.iris.core.tools;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisWorld;
import com.volmit.iris.engine.platform.BukkitChunkGenerator;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;

public class IrisWorldCreator {
   private String name;
   private boolean studio = false;
   private String dimensionName = null;
   private long seed = 1337L;

   public IrisWorldCreator dimension(String loadKey) {
      this.dimensionName = var1;
      return this;
   }

   public IrisWorldCreator name(String name) {
      this.name = var1;
      return this;
   }

   public IrisWorldCreator seed(long seed) {
      this.seed = var1;
      return this;
   }

   public IrisWorldCreator studioMode() {
      this.studio = true;
      return this;
   }

   public IrisWorldCreator productionMode() {
      this.studio = false;
      return this;
   }

   public WorldCreator create() {
      IrisDimension var1 = IrisData.loadAnyDimension(this.dimensionName, (IrisData)null);
      IrisWorld var2 = IrisWorld.builder().name(this.name).minHeight(var1.getMinHeight()).maxHeight(var1.getMaxHeight()).seed(this.seed).worldFolder(new File(Bukkit.getWorldContainer(), this.name)).environment(this.findEnvironment()).build();
      BukkitChunkGenerator var3 = new BukkitChunkGenerator(var2, this.studio, this.studio ? var1.getLoader().getDataFolder() : new File(var2.worldFolder(), "iris/pack"), this.dimensionName);
      return (new WorldCreator(this.name)).environment(var2.environment()).generateStructures(true).generator(var3).seed(this.seed);
   }

   private Environment findEnvironment() {
      IrisDimension var1 = IrisData.loadAnyDimension(this.dimensionName, (IrisData)null);
      return var1 != null && var1.getEnvironment() != null ? var1.getEnvironment() : Environment.NORMAL;
   }

   public IrisWorldCreator studio(boolean studio) {
      this.studio = var1;
      return this;
   }
}
