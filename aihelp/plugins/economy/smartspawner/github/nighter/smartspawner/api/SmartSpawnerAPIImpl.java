package github.nighter.smartspawner.api;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.api.data.SpawnerDataDTO;
import github.nighter.smartspawner.api.data.SpawnerDataModifier;
import github.nighter.smartspawner.api.impl.SpawnerDataModifierImpl;
import github.nighter.smartspawner.spawner.item.SpawnerItemFactory;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SmartSpawnerAPIImpl implements SmartSpawnerAPI {
   private final SmartSpawner plugin;
   private final SpawnerItemFactory itemFactory;

   public SmartSpawnerAPIImpl(SmartSpawner plugin) {
      this.plugin = plugin;
      this.itemFactory = new SpawnerItemFactory(plugin);
   }

   public ItemStack createSpawnerItem(EntityType entityType) {
      return this.itemFactory.createSmartSpawnerItem(entityType);
   }

   public ItemStack createSpawnerItem(EntityType entityType, int amount) {
      return this.itemFactory.createSmartSpawnerItem(entityType, amount);
   }

   public ItemStack createVanillaSpawnerItem(EntityType entityType) {
      return this.itemFactory.createVanillaSpawnerItem(entityType);
   }

   public ItemStack createVanillaSpawnerItem(EntityType entityType, int amount) {
      return this.itemFactory.createVanillaSpawnerItem(entityType, amount);
   }

   public ItemStack createItemSpawnerItem(Material itemMaterial) {
      return this.itemFactory.createItemSpawnerItem(itemMaterial);
   }

   public ItemStack createItemSpawnerItem(Material itemMaterial, int amount) {
      return this.itemFactory.createItemSpawnerItem(itemMaterial, amount);
   }

   public boolean isSmartSpawner(ItemStack item) {
      if (item != null && item.getType() == Material.SPAWNER && item.hasItemMeta()) {
         ItemMeta meta = item.getItemMeta();
         if (meta == null) {
            return false;
         } else {
            return !this.isVanillaSpawner(item) && !this.isItemSpawner(item);
         }
      } else {
         return false;
      }
   }

   public boolean isVanillaSpawner(ItemStack item) {
      if (item != null && item.getType() == Material.SPAWNER && item.hasItemMeta()) {
         ItemMeta meta = item.getItemMeta();
         return meta == null ? false : meta.getPersistentDataContainer().has(new NamespacedKey(this.plugin, "vanilla_spawner"), PersistentDataType.BOOLEAN);
      } else {
         return false;
      }
   }

   public boolean isItemSpawner(ItemStack item) {
      if (item != null && item.getType() == Material.SPAWNER && item.hasItemMeta()) {
         ItemMeta meta = item.getItemMeta();
         return meta == null ? false : meta.getPersistentDataContainer().has(new NamespacedKey(this.plugin, "item_spawner_material"), PersistentDataType.STRING);
      } else {
         return false;
      }
   }

   public EntityType getSpawnerEntityType(ItemStack item) {
      if (item != null && item.getType() == Material.SPAWNER && item.hasItemMeta()) {
         ItemMeta meta = item.getItemMeta();
         if (meta instanceof BlockStateMeta) {
            BlockStateMeta blockMeta = (BlockStateMeta)meta;
            BlockState blockState = blockMeta.getBlockState();
            if (blockState instanceof CreatureSpawner) {
               CreatureSpawner cs = (CreatureSpawner)blockState;
               return cs.getSpawnedType();
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public Material getItemSpawnerMaterial(ItemStack item) {
      if (!this.isItemSpawner(item)) {
         return null;
      } else {
         ItemMeta meta = item.getItemMeta();
         if (meta == null) {
            return null;
         } else {
            String materialName = (String)meta.getPersistentDataContainer().get(new NamespacedKey(this.plugin, "item_spawner_material"), PersistentDataType.STRING);
            if (materialName == null) {
               return null;
            } else {
               try {
                  return Material.valueOf(materialName);
               } catch (IllegalArgumentException var5) {
                  return null;
               }
            }
         }
      }
   }

   public SpawnerDataDTO getSpawnerByLocation(Location location) {
      if (location == null) {
         return null;
      } else {
         SpawnerData spawnerData = this.plugin.getSpawnerManager().getSpawnerByLocation(location);
         return spawnerData != null ? this.convertToDTO(spawnerData) : null;
      }
   }

   public SpawnerDataDTO getSpawnerById(String spawnerId) {
      if (spawnerId == null) {
         return null;
      } else {
         SpawnerData spawnerData = this.plugin.getSpawnerManager().getSpawnerById(spawnerId);
         return spawnerData != null ? this.convertToDTO(spawnerData) : null;
      }
   }

   public List<SpawnerDataDTO> getAllSpawners() {
      return (List)this.plugin.getSpawnerManager().getAllSpawners().stream().map(this::convertToDTO).collect(Collectors.toList());
   }

   public SpawnerDataModifier getSpawnerModifier(String spawnerId) {
      if (spawnerId == null) {
         return null;
      } else {
         SpawnerData spawnerData = this.plugin.getSpawnerManager().getSpawnerById(spawnerId);
         return spawnerData != null ? new SpawnerDataModifierImpl(spawnerData) : null;
      }
   }

   private SpawnerDataDTO convertToDTO(SpawnerData spawnerData) {
      return new SpawnerDataDTO(spawnerData.getSpawnerId(), spawnerData.getSpawnerLocation(), spawnerData.getEntityType(), spawnerData.getSpawnedItemMaterial(), spawnerData.getStackSize(), spawnerData.getMaxStackSize(), spawnerData.getBaseMaxStoragePages(), spawnerData.getBaseMinMobs(), spawnerData.getBaseMaxMobs(), spawnerData.getBaseMaxStoredExp(), spawnerData.getSpawnDelay());
   }
}
