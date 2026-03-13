package com.nisovin.shopkeepers.villagers;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class RegularVillagers {
   private final SKShopkeepersPlugin plugin;
   private final VillagerInteractionListener villagerInteractionListener;
   private final BlockVillagerSpawnListener blockVillagerSpawnListener;
   private final BlockZombieVillagerCuringListener blockZombieVillagerCuringListener;

   public RegularVillagers(SKShopkeepersPlugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
      this.villagerInteractionListener = new VillagerInteractionListener(plugin);
      this.blockVillagerSpawnListener = new BlockVillagerSpawnListener();
      this.blockZombieVillagerCuringListener = new BlockZombieVillagerCuringListener();
   }

   public void onEnable() {
      Bukkit.getPluginManager().registerEvents(this.villagerInteractionListener, this.plugin);
      if (Settings.blockVillagerSpawns || Settings.blockWanderingTraderSpawns) {
         Bukkit.getPluginManager().registerEvents(this.blockVillagerSpawnListener, this.plugin);
      }

      if (Settings.disableZombieVillagerCuring) {
         Bukkit.getPluginManager().registerEvents(this.blockZombieVillagerCuringListener, this.plugin);
      }

   }

   public void onDisable() {
      HandlerList.unregisterAll(this.villagerInteractionListener);
      HandlerList.unregisterAll(this.blockVillagerSpawnListener);
      HandlerList.unregisterAll(this.blockZombieVillagerCuringListener);
   }
}
