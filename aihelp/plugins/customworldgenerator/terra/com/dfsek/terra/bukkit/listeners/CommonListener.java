package com.dfsek.terra.bukkit.listeners;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.hooks.MultiverseGeneratorPluginHook;
import com.dfsek.terra.bukkit.world.BukkitBiomeInfo;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import java.util.List;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Wolf.Variant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonListener implements Listener {
   private static final Logger logger = LoggerFactory.getLogger(CommonListener.class);
   private static final List<SpawnReason> WOLF_VARIANT_SPAWN_REASONS;
   private final Platform platform;

   public CommonListener(Platform platform) {
      this.platform = platform;
   }

   @EventHandler
   public void onPluginEnable(PluginEnableEvent event) {
      if (event.getPlugin().getName().equals("Multiverse-Core")) {
         try {
            Class.forName("org.mvplugins.multiverse.core.MultiverseCoreApi");
            MultiverseGeneratorPluginHook.register(this.platform);
         } catch (ClassNotFoundException var3) {
            logger.debug("Multiverse v5 is not installed.");
         } catch (IllegalStateException var4) {
            logger.error("Failed to register Terra generator plugin to multiverse.", var4);
         }
      }

   }

   private void applyWolfVariant(Wolf wolf) {
      if (wolf.getVariant() == Variant.PALE) {
         World world = wolf.getWorld();
         ChunkGenerator var4 = world.getGenerator();
         if (var4 instanceof BukkitChunkGeneratorWrapper) {
            BukkitChunkGeneratorWrapper wrapper = (BukkitChunkGeneratorWrapper)var4;
            ConfigPack pack = (ConfigPack)this.platform.getConfigRegistry().get(wrapper.getPack().getRegistryKey()).orElse((Object)null);
            if (pack != null) {
               NamespacedKey biomeKey = wolf.getWorld().getBiome(wolf.getLocation()).getKey();
               pack.getBiomeProvider().stream().filter((biome) -> {
                  NamespacedKey key = ((BukkitBiomeInfo)((BukkitPlatformBiome)biome.getPlatformBiome()).getContext().get(BukkitBiomeInfo.class)).biomeKey();
                  return key.equals(biomeKey);
               }).findFirst().ifPresent((biome) -> {
                  NamespacedKey vanillaBiomeKey = ((BukkitPlatformBiome)biome.getPlatformBiome()).getHandle().getKey();
                  String var3 = vanillaBiomeKey.toString();
                  byte var4 = -1;
                  switch(var3.hashCode()) {
                  case -1169390966:
                     if (var3.equals("minecraft:forest")) {
                        var4 = 4;
                     }
                     break;
                  case -1145090426:
                     if (var3.equals("minecraft:grove")) {
                        var4 = 3;
                     }
                     break;
                  case -483592960:
                     if (var3.equals("minecraft:snowy_taiga")) {
                        var4 = 0;
                     }
                     break;
                  case 412611404:
                     if (var3.equals("minecraft:old_growth_spruce_taiga")) {
                        var4 = 2;
                     }
                     break;
                  case 658494778:
                     if (var3.equals("minecraft:old_growth_pine_taiga")) {
                        var4 = 1;
                     }
                  }

                  switch(var4) {
                  case 0:
                     wolf.setVariant(Variant.ASHEN);
                     break;
                  case 1:
                     wolf.setVariant(Variant.BLACK);
                     break;
                  case 2:
                     wolf.setVariant(Variant.CHESTNUT);
                     break;
                  case 3:
                     wolf.setVariant(Variant.SNOWY);
                     break;
                  case 4:
                     wolf.setVariant(Variant.WOODS);
                  }

               });
            }
         }
      }
   }

   @EventHandler
   public void onWolfSpawn(CreatureSpawnEvent event) {
      LivingEntity var3 = event.getEntity();
      if (var3 instanceof Wolf) {
         Wolf wolf = (Wolf)var3;
         if (!WOLF_VARIANT_SPAWN_REASONS.contains(event.getSpawnReason())) {
            logger.debug("Ignoring wolf spawned with reason: " + String.valueOf(event.getSpawnReason()));
         } else {
            this.applyWolfVariant(wolf);
         }
      }
   }

   @EventHandler
   public void onChunkGenerate(ChunkLoadEvent event) {
      if (event.isNewChunk()) {
         Entity[] var2 = event.getChunk().getEntities();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Entity entity = var2[var4];
            if (entity instanceof Wolf) {
               Wolf wolf = (Wolf)entity;
               this.applyWolfVariant(wolf);
            }
         }

      }
   }

   static {
      WOLF_VARIANT_SPAWN_REASONS = List.of(SpawnReason.SPAWNER, SpawnReason.TRIAL_SPAWNER, SpawnReason.SPAWNER_EGG, SpawnReason.NATURAL);
   }
}
