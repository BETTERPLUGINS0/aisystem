package com.dfsek.terra.bukkit;

import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.handles.BukkitItemHandle;
import com.dfsek.terra.bukkit.handles.BukkitWorldHandle;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlatformImpl extends AbstractPlatform {
   private static final Logger LOGGER = LoggerFactory.getLogger(PlatformImpl.class);
   private final ItemHandle itemHandle = new BukkitItemHandle();
   private final WorldHandle handle = new BukkitWorldHandle();
   private final TerraBukkitPlugin plugin;
   private int generationThreads = getMoonriseGenerationThreadsWithReflection();

   public PlatformImpl(TerraBukkitPlugin plugin) {
      if (this.generationThreads == 0) {
         this.generationThreads = 1;
      }

      this.plugin = plugin;
      this.load();
   }

   public TerraBukkitPlugin getPlugin() {
      return this.plugin;
   }

   public boolean reload() {
      this.getTerraConfig().load(this);
      this.getRawConfigRegistry().clear();
      boolean succeed = this.getRawConfigRegistry().loadAll(this);
      Bukkit.getWorlds().forEach((world) -> {
         ChunkGenerator patt0$temp = world.getGenerator();
         if (patt0$temp instanceof BukkitChunkGeneratorWrapper) {
            BukkitChunkGeneratorWrapper wrapper = (BukkitChunkGeneratorWrapper)patt0$temp;
            this.getConfigRegistry().get(wrapper.getPack().getRegistryKey()).ifPresent((pack) -> {
               wrapper.setPack(pack);
               LOGGER.info("Replaced pack in chunk generator for world {}", world);
            });
         }

      });
      return succeed;
   }

   @NotNull
   public String platformName() {
      return "Bukkit";
   }

   public void runPossiblyUnsafeTask(@NotNull Runnable runnable) {
      this.plugin.getGlobalRegionScheduler().run(this.plugin, (task) -> {
         runnable.run();
      });
   }

   @NotNull
   public WorldHandle getWorldHandle() {
      return this.handle;
   }

   @NotNull
   public File getDataFolder() {
      return this.plugin.getDataFolder();
   }

   @NotNull
   public ItemHandle getItemHandle() {
      return this.itemHandle;
   }

   public int getGenerationThreads() {
      return this.generationThreads;
   }

   public void register(TypeRegistry registry) {
      super.register(registry);
      registry.registerLoader(BlockState.class, (type, o, loader, depthTracker) -> {
         return this.handle.createBlockState((String)o);
      }).registerLoader(PlatformBiome.class, (type, o, loader, depthTracker) -> {
         return this.parseBiome((String)o, depthTracker);
      }).registerLoader(EntityType.class, (type, o, loader, depthTracker) -> {
         return EntityType.valueOf((String)o);
      });
   }

   protected BukkitPlatformBiome parseBiome(String id, DepthTracker depthTracker) throws LoadException {
      NamespacedKey key = NamespacedKey.fromString(id);
      if (key != null && key.namespace().equals("minecraft")) {
         return new BukkitPlatformBiome((Biome)RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).getOrThrow(key));
      } else {
         throw new LoadException("Invalid biome identifier " + id, depthTracker);
      }
   }
}
