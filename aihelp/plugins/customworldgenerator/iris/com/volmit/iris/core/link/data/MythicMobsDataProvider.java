package com.volmit.iris.core.link.data;

import com.volmit.iris.core.link.ExternalDataProvider;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.api.mobs.entities.SpawnReason;
import io.lumine.mythic.api.skills.conditions.ILocationCondition;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.adapters.BukkitWorld;
import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.MobExecutor;
import io.lumine.mythic.core.mobs.MobStack;
import io.lumine.mythic.core.skills.SkillCondition;
import io.lumine.mythic.core.spawning.spawners.MythicSpawner;
import io.lumine.mythic.core.utils.annotations.MythicCondition;
import io.lumine.mythic.core.utils.annotations.MythicField;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MythicMobsDataProvider extends ExternalDataProvider {
   public MythicMobsDataProvider() {
      super("MythicMobs");
   }

   public void init() {
   }

   @Nullable
   public Entity spawnMob(@NotNull Location location, @NotNull Identifier entityId) {
      ActiveMob var3 = this.spawnMob(BukkitAdapter.adapt(var1), var2);
      return var3 == null ? null : var3.getEntity().getBukkitEntity();
   }

   private ActiveMob spawnMob(AbstractLocation location, Identifier entityId) {
      MobExecutor var3 = MythicBukkit.inst().getMobManager();
      MythicMob var4 = (MythicMob)var3.getMythicMob(var2.key()).orElse((Object)null);
      if (var4 == null) {
         MobStack var5 = var3.getMythicMobStack(var2.key());
         if (var5 == null) {
            throw new MissingResourceException("Failed to find Mob!", var2.namespace(), var2.key());
         } else {
            return var5.spawn(var1, 1.0D, SpawnReason.OTHER, (MythicSpawner)null);
         }
      } else {
         return var4.spawn(var1, 1.0D, SpawnReason.OTHER, (Consumer)null, (MythicSpawner)null);
      }
   }

   @NotNull
   public Collection<Identifier> getTypes(@NotNull DataType dataType) {
      if (var1 != DataType.ENTITY) {
         return List.of();
      } else {
         MobExecutor var2 = MythicBukkit.inst().getMobManager();
         return Stream.concat(var2.getMobNames().stream(), var2.getMobStacks().stream().map(MobStack::getName)).distinct().map((var0) -> {
            return new Identifier("mythicmobs", var0);
         }).toList();
      }
   }

   public boolean isValidProvider(@NotNull Identifier id, DataType dataType) {
      return var1.namespace().equalsIgnoreCase("mythicmobs") && var2 == DataType.ENTITY;
   }

   @EventHandler
   public void on(MythicConditionLoadEvent event) {
      String var2 = var1.getConditionName();
      byte var3 = -1;
      switch(var2.hashCode()) {
      case -1902540691:
         if (var2.equals("irisbiome")) {
            var3 = 0;
         }
         break;
      case 1604911303:
         if (var2.equals("irisregion")) {
            var3 = 1;
         }
      }

      switch(var3) {
      case 0:
         var1.register(new MythicMobsDataProvider.IrisBiomeCondition(var1.getConditionName(), var1.getConfig()));
         break;
      case 1:
         var1.register(new MythicMobsDataProvider.IrisRegionCondition(var1.getConditionName(), var1.getConfig()));
      }

   }

   @MythicCondition(
      author = "CrazyDev22",
      name = "irisbiome",
      description = "Tests if the target is within the given list of biomes"
   )
   public static class IrisBiomeCondition extends SkillCondition implements ILocationCondition {
      @MythicField(
         name = "biome",
         aliases = {"b"},
         description = "A list of biomes to check"
      )
      private Set<String> biomes = ConcurrentHashMap.newKeySet();
      @MythicField(
         name = "surface",
         aliases = {"s"},
         description = "If the biome check should only be performed on the surface"
      )
      private boolean surface;

      public IrisBiomeCondition(String line, MythicLineConfig mlc) {
         super(var1);
         String var3 = var2.getString(new String[]{"biome", "b"}, "", new String[0]);
         this.biomes.addAll(Arrays.asList(var3.split(",")));
         this.surface = var2.getBoolean(new String[]{"surface", "s"}, false);
      }

      public boolean check(AbstractLocation target) {
         PlatformChunkGenerator var2 = IrisToolbelt.access(((BukkitWorld)var1.getWorld()).getBukkitWorld());
         if (var2 == null) {
            return false;
         } else {
            Engine var3 = var2.getEngine();
            if (var3 == null) {
               return false;
            } else {
               IrisBiome var4 = this.surface ? var3.getSurfaceBiome(var1.getBlockX(), var1.getBlockZ()) : var3.getBiomeOrMantle(var1.getBlockX(), var1.getBlockY() - var3.getMinHeight(), var1.getBlockZ());
               return this.biomes.contains(var4.getLoadKey());
            }
         }
      }
   }

   @MythicCondition(
      author = "CrazyDev22",
      name = "irisregion",
      description = "Tests if the target is within the given list of biomes"
   )
   public static class IrisRegionCondition extends SkillCondition implements ILocationCondition {
      @MythicField(
         name = "region",
         aliases = {"r"},
         description = "A list of regions to check"
      )
      private Set<String> regions = ConcurrentHashMap.newKeySet();

      public IrisRegionCondition(String line, MythicLineConfig mlc) {
         super(var1);
         String var3 = var2.getString(new String[]{"region", "r"}, "", new String[0]);
         this.regions.addAll(Arrays.asList(var3.split(",")));
      }

      public boolean check(AbstractLocation target) {
         PlatformChunkGenerator var2 = IrisToolbelt.access(((BukkitWorld)var1.getWorld()).getBukkitWorld());
         if (var2 == null) {
            return false;
         } else {
            Engine var3 = var2.getEngine();
            if (var3 == null) {
               return false;
            } else {
               IrisRegion var4 = var3.getRegion(var1.getBlockX(), var1.getBlockZ());
               return this.regions.contains(var4.getLoadKey());
            }
         }
      }
   }
}
