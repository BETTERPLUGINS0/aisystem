package github.nighter.smartspawner.spawner.config;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

public class SpawnerMobHeadTexture {
   private static final Map<EntityType, ItemStack> HEAD_CACHE = new EnumMap(EntityType.class);
   private static final Map<EntityType, SkullMeta> BASE_META_CACHE = new EnumMap(EntityType.class);
   private static final Map<Material, ItemStack> ITEM_HEAD_CACHE = new EnumMap(Material.class);
   private static final ItemStack DEFAULT_SPAWNER_BLOCK;

   private static boolean isBedrockPlayer(Player player) {
      SmartSpawner plugin = SmartSpawner.getInstance();
      return plugin != null && plugin.getIntegrationManager() != null && plugin.getIntegrationManager().getFloodgateHook() != null ? plugin.getIntegrationManager().getFloodgateHook().isBedrockPlayer(player) : false;
   }

   public static ItemStack getCustomHead(EntityType entityType, Player player, Consumer<ItemMeta> metaModifier) {
      ItemStack item;
      if (entityType == null) {
         item = DEFAULT_SPAWNER_BLOCK.clone();
         if (metaModifier != null) {
            item.editMeta(metaModifier);
         }

         return item;
      } else if (isBedrockPlayer(player)) {
         item = DEFAULT_SPAWNER_BLOCK.clone();
         if (metaModifier != null) {
            item.editMeta(metaModifier);
         }

         return item;
      } else {
         return getCustomHead(entityType, metaModifier);
      }
   }

   public static ItemStack getCustomHead(EntityType entityType) {
      return getCustomHead(entityType, (Consumer)null);
   }

   public static ItemStack getCustomHead(EntityType entityType, Consumer<ItemMeta> metaModifier) {
      if (entityType == null) {
         ItemStack item = DEFAULT_SPAWNER_BLOCK.clone();
         if (metaModifier != null) {
            item.editMeta(metaModifier);
         }

         return item;
      } else {
         SmartSpawner plugin = SmartSpawner.getInstance();
         if (plugin == null) {
            ItemStack item = DEFAULT_SPAWNER_BLOCK.clone();
            if (metaModifier != null) {
               item.editMeta(metaModifier);
            }

            return item;
         } else {
            SpawnerSettingsConfig settingsConfig = plugin.getSpawnerSettingsConfig();
            if (settingsConfig == null) {
               ItemStack item = DEFAULT_SPAWNER_BLOCK.clone();
               if (metaModifier != null) {
                  item.editMeta(metaModifier);
               }

               return item;
            } else {
               Material material = settingsConfig.getMaterial(entityType);
               ItemStack item;
               if (material != Material.PLAYER_HEAD) {
                  item = new ItemStack(material);
                  if (metaModifier != null) {
                     item.editMeta(metaModifier);
                  }

                  return item;
               } else if (!settingsConfig.hasCustomTexture(entityType)) {
                  item = new ItemStack(material);
                  if (metaModifier != null) {
                     item.editMeta(metaModifier);
                  }

                  return item;
               } else {
                  SkullMeta baseMeta = (SkullMeta)BASE_META_CACHE.get(entityType);
                  if (baseMeta == null) {
                     try {
                        String texture = settingsConfig.getCustomTexture(entityType);
                        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
                        PlayerTextures textures = profile.getTextures();
                        URL url = new URL("http://textures.minecraft.net/texture/" + texture);
                        textures.setSkin(url);
                        profile.setTextures(textures);
                        ItemStack tempHead = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta tempMeta = (SkullMeta)tempHead.getItemMeta();
                        tempMeta.setOwnerProfile(profile);
                        baseMeta = tempMeta.clone();
                        BASE_META_CACHE.put(entityType, baseMeta);
                     } catch (Exception var12) {
                        var12.printStackTrace();
                        ItemStack item = new ItemStack(material);
                        if (metaModifier != null) {
                           item.editMeta(metaModifier);
                        }

                        return item;
                     }
                  }

                  ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                  SkullMeta meta = baseMeta.clone();
                  if (metaModifier != null) {
                     metaModifier.accept(meta);
                  }

                  head.setItemMeta(meta);
                  if (metaModifier == null && !HEAD_CACHE.containsKey(entityType)) {
                     HEAD_CACHE.put(entityType, head.clone());
                  }

                  return head;
               }
            }
         }
      }
   }

   public static ItemStack getItemSpawnerHead(Material itemMaterial, Player player, Consumer<ItemMeta> metaModifier) {
      ItemStack item;
      if (itemMaterial == null) {
         item = DEFAULT_SPAWNER_BLOCK.clone();
         if (metaModifier != null) {
            item.editMeta(metaModifier);
         }

         return item;
      } else if (isBedrockPlayer(player)) {
         item = DEFAULT_SPAWNER_BLOCK.clone();
         if (metaModifier != null) {
            item.editMeta(metaModifier);
         }

         return item;
      } else {
         return getItemSpawnerHead(itemMaterial, metaModifier);
      }
   }

   public static ItemStack getItemSpawnerHead(Material itemMaterial, Consumer<ItemMeta> metaModifier) {
      if (itemMaterial == null) {
         ItemStack item = DEFAULT_SPAWNER_BLOCK.clone();
         if (metaModifier != null) {
            item.editMeta(metaModifier);
         }

         return item;
      } else {
         SmartSpawner plugin = SmartSpawner.getInstance();
         if (plugin != null && plugin.getItemSpawnerSettingsConfig() != null) {
            ItemSpawnerSettingsConfig.ItemHeadData headData = plugin.getItemSpawnerSettingsConfig().getHeadData(itemMaterial);
            Material headMaterial = headData.getMaterial();
            ItemStack item = new ItemStack(headMaterial);
            if (metaModifier != null) {
               item.editMeta(metaModifier);
            }

            if (metaModifier == null && !ITEM_HEAD_CACHE.containsKey(itemMaterial)) {
               ITEM_HEAD_CACHE.put(itemMaterial, item.clone());
            }

            return item;
         } else {
            ItemStack item = DEFAULT_SPAWNER_BLOCK.clone();
            if (metaModifier != null) {
               item.editMeta(metaModifier);
            }

            return item;
         }
      }
   }

   public static void clearCache() {
      HEAD_CACHE.clear();
      BASE_META_CACHE.clear();
      ITEM_HEAD_CACHE.clear();
   }

   public static void prewarmCache() {
      SmartSpawner plugin = SmartSpawner.getInstance();
      if (plugin != null) {
         Scheduler.runTaskAsync(() -> {
            SpawnerSettingsConfig settingsConfig = plugin.getSpawnerSettingsConfig();
            if (settingsConfig != null) {
               EntityType[] commonTypes = new EntityType[]{EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER, EntityType.ENDERMAN, EntityType.BLAZE, EntityType.SLIME, EntityType.MAGMA_CUBE, EntityType.GHAST, EntityType.PIG, EntityType.COW, EntityType.CHICKEN, EntityType.SHEEP, EntityType.IRON_GOLEM, EntityType.WITHER_SKELETON, EntityType.ZOGLIN, EntityType.HOGLIN, EntityType.CAVE_SPIDER};
               EntityType[] var3 = commonTypes;
               int var4 = commonTypes.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  EntityType type = var3[var5];

                  try {
                     if (settingsConfig.getMaterial(type) == Material.PLAYER_HEAD && settingsConfig.hasCustomTexture(type)) {
                        getCustomHead(type, (Consumer)null);
                     }
                  } catch (Exception var8) {
                  }
               }

            }
         });
      }
   }

   static {
      DEFAULT_SPAWNER_BLOCK = new ItemStack(Material.SPAWNER);
   }
}
