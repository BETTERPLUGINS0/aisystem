package me.gypopo.economyshopgui.events;

import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.util.XMaterial;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;

public class SpawnerPlaceEvent implements Listener {
   private final boolean self;
   private final EconomyShopGUI plugin;

   public SpawnerPlaceEvent(EconomyShopGUI plugin) {
      this.plugin = plugin;
      this.self = ConfigManager.getConfig().getBoolean("only-mine-plugin-spawners");
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onBlockPlace(BlockPlaceEvent e) {
      if (!e.isCancelled()) {
         if (e.getBlock().getType() == XMaterial.SPAWNER.parseMaterial()) {
            if (EconomyShopGUI.getInstance().getSpawnerManager().getProvider().getProviderName().equalsIgnoreCase("DEFAULT")) {
               Block b = e.getBlock();
               String spawnertype = EconomyShopGUI.getInstance().versionHandler.getSpawnerType(e.getItemInHand());
               if (spawnertype != null) {
                  EntityType type = EntityType.valueOf(spawnertype.toUpperCase());
                  CreatureSpawner spawner = (CreatureSpawner)b.getState();
                  if (this.self && this.plugin.version >= 114) {
                     spawner.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "SpawnerType"), PersistentDataType.STRING, type.name());
                  }

                  spawner.setSpawnedType(type);
                  spawner.update();
               }

            }
         }
      }
   }
}
