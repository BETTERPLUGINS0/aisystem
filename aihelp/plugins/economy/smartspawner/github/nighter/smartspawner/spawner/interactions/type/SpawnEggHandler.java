package github.nighter.smartspawner.spawner.interactions.type;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.api.events.SpawnerEggChangeEvent;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpawnEggHandler {
   private static final String PERMISSION_CHANGE_TYPE = "smartspawner.changetype";
   private static final String NO_PERMISSION_KEY = "no_permission";
   private static final String CHANGED_MESSAGE_KEY = "entity_changed";
   private static final String INVALID_EGG_KEY = "invalid_egg";
   private static final String SPAWN_EGG_SUFFIX = "_SPAWN_EGG";
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;
   private final MessageService messageService;
   private final Map<Material, EntityType> eggTypeCache;

   public SpawnEggHandler(SmartSpawner plugin) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
      this.messageService = plugin.getMessageService();
      this.eggTypeCache = new HashMap();
      this.initializeEggTypeCache();
   }

   public void handleSpawnEggUse(Player player, CreatureSpawner spawner, SpawnerData spawnerData, ItemStack spawnEgg) {
      if (player != null && spawner != null && spawnerData != null && spawnEgg != null) {
         if (!player.hasPermission("smartspawner.changetype")) {
            this.messageService.sendMessage(player, "no_permission");
         } else {
            Optional<EntityType> optionalEntityType = this.getEntityTypeFromSpawnEgg(spawnEgg.getType());
            if (optionalEntityType.isPresent()) {
               EntityType newType = (EntityType)optionalEntityType.get();
               this.updateSpawner(player, spawner, spawnerData, newType);
               this.consumeItemIfSurvival(player, spawnEgg);
            } else {
               this.messageService.sendMessage(player, "invalid_egg");
            }

         }
      } else {
         this.plugin.getLogger().log(Level.WARNING, "Attempted to handle spawn egg use with null parameters");
      }
   }

   private void updateSpawner(Player player, CreatureSpawner spawner, SpawnerData spawnerData, EntityType newType) {
      if (SpawnerEggChangeEvent.getHandlerList().getRegisteredListeners().length != 0) {
         SpawnerEggChangeEvent e = new SpawnerEggChangeEvent(player, spawner.getLocation(), spawnerData.getEntityType(), newType);
         Bukkit.getPluginManager().callEvent(e);
         if (e.isCancelled()) {
            return;
         }
      }

      spawnerData.setEntityType(newType);
      spawner.setSpawnedType(newType);
      spawner.update();
      Map<String, String> placeholders = new HashMap();
      placeholders.put("entity", this.languageManager.getFormattedMobName(newType));
      placeholders.put("ᴇɴᴛɪᴛʏ", this.languageManager.getSmallCaps((String)placeholders.get("entity")));
      this.messageService.sendMessage((Player)player, "entity_changed", placeholders);
   }

   private void consumeItemIfSurvival(Player player, ItemStack spawnEgg) {
      if (player.getGameMode() == GameMode.SURVIVAL) {
         spawnEgg.setAmount(spawnEgg.getAmount() - 1);
      }

   }

   private Optional<EntityType> getEntityTypeFromSpawnEgg(Material material) {
      if (this.eggTypeCache.containsKey(material)) {
         return Optional.of((EntityType)this.eggTypeCache.get(material));
      } else if (!material.name().endsWith("_SPAWN_EGG")) {
         return Optional.empty();
      } else {
         try {
            String entityName = material.name().replace("_SPAWN_EGG", "");
            EntityType entityType = EntityType.valueOf(entityName);
            this.eggTypeCache.put(material, entityType);
            return Optional.of(entityType);
         } catch (IllegalArgumentException var4) {
            this.plugin.getLogger().log(Level.FINE, "Failed to get entity type from material: " + String.valueOf(material), var4);
            return Optional.empty();
         }
      }
   }

   private void initializeEggTypeCache() {
      Material[] var1 = Material.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Material material = var1[var3];
         if (material.name().endsWith("_SPAWN_EGG")) {
            try {
               String entityName = material.name().replace("_SPAWN_EGG", "");
               EntityType entityType = EntityType.valueOf(entityName);
               this.eggTypeCache.put(material, entityType);
            } catch (IllegalArgumentException var7) {
            }
         }
      }

   }
}
