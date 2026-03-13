package github.nighter.smartspawner.spawner.utils;

import github.nighter.smartspawner.spawner.properties.VirtualInventory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class ItemStackSerializer {
   private static final List<String> ARMOR_MATERIALS = Arrays.asList("LEATHER", "CHAINMAIL", "IRON", "GOLDEN", "DIAMOND", "NETHERITE");
   private static final List<String> ARMOR_PIECES = Arrays.asList("_HELMET", "_CHESTPLATE", "_LEGGINGS", "_BOOTS");
   private static final List<String> TOOL_TYPES = Arrays.asList("_SWORD", "_PICKAXE", "_AXE", "_SHOVEL", "_HOE");

   public static List<String> serializeInventory(Map<VirtualInventory.ItemSignature, Long> items) {
      Map<Material, ItemStackSerializer.ItemGroup> groupedItems = new HashMap();
      Iterator var2 = items.entrySet().iterator();

      while(true) {
         while(true) {
            while(var2.hasNext()) {
               Entry<VirtualInventory.ItemSignature, Long> entry = (Entry)var2.next();
               ItemStack template = ((VirtualInventory.ItemSignature)entry.getKey()).getTemplateRef();
               Material material = template.getType();
               ItemStackSerializer.ItemGroup group = (ItemStackSerializer.ItemGroup)groupedItems.computeIfAbsent(material, ItemStackSerializer.ItemGroup::new);
               if (material == Material.TIPPED_ARROW) {
                  PotionMeta meta = (PotionMeta)template.getItemMeta();
                  if (meta != null && meta.getBasePotionType() != null) {
                     group.addPotionArrow(meta.getBasePotionType(), ((Long)entry.getValue()).intValue());
                  } else {
                     group.addPotionArrow(PotionType.WATER, ((Long)entry.getValue()).intValue());
                  }
               } else if (isDestructibleItem(material)) {
                  int damage = getDamageValue(template);
                  group.addItem(damage, ((Long)entry.getValue()).intValue());
               } else {
                  group.addItem(0, ((Long)entry.getValue()).intValue());
               }
            }

            List<String> serializedItems = new ArrayList();
            Iterator var10 = groupedItems.values().iterator();

            while(true) {
               while(var10.hasNext()) {
                  ItemStackSerializer.ItemGroup group = (ItemStackSerializer.ItemGroup)var10.next();
                  Entry entry;
                  StringBuilder sb;
                  boolean first;
                  Iterator var16;
                  if (group.getMaterial() == Material.TIPPED_ARROW) {
                     sb = new StringBuilder("TIPPED_ARROW#");
                     first = true;

                     for(var16 = group.getPotionTypeCount().entrySet().iterator(); var16.hasNext(); first = false) {
                        entry = (Entry)var16.next();
                        if (!first) {
                           sb.append(',');
                        }

                        sb.append((String)entry.getKey()).append(':').append(entry.getValue());
                     }

                     serializedItems.add(sb.toString());
                  } else if (!isDestructibleItem(group.getMaterial())) {
                     int totalCount = group.getDamageCount().values().stream().mapToInt(Integer::intValue).sum();
                     String var10001 = group.getMaterial().name();
                     serializedItems.add(var10001 + ":" + totalCount);
                  } else {
                     sb = new StringBuilder(group.getMaterial().name());
                     sb.append(';');
                     first = true;

                     for(var16 = group.getDamageCount().entrySet().iterator(); var16.hasNext(); first = false) {
                        entry = (Entry)var16.next();
                        if (!first) {
                           sb.append(',');
                        }

                        sb.append(entry.getKey()).append(':').append(entry.getValue());
                     }

                     serializedItems.add(sb.toString());
                  }
               }

               return serializedItems;
            }
         }
      }
   }

   public static Map<ItemStack, Integer> deserializeInventory(List<String> data) {
      Map<ItemStack, Integer> result = new HashMap();
      Iterator var2 = data.iterator();

      while(true) {
         while(var2.hasNext()) {
            String entry = (String)var2.next();
            String[] parts;
            int count;
            int damage;
            int var18;
            if (entry.startsWith("TIPPED_ARROW#")) {
               parts = entry.substring("TIPPED_ARROW#".length()).split(",");
               String[] var16 = parts;
               count = parts.length;

               for(var18 = 0; var18 < count; ++var18) {
                  String potionEntry = var16[var18];
                  String[] parts = potionEntry.split(":");
                  String potionTypeName = parts[0];
                  damage = Integer.parseInt(parts[1]);
                  ItemStack arrow = new ItemStack(Material.TIPPED_ARROW);
                  PotionMeta meta = (PotionMeta)arrow.getItemMeta();
                  if (meta != null) {
                     try {
                        PotionType potionType = PotionType.valueOf(potionTypeName);
                        meta.setBasePotionType(potionType);
                        arrow.setItemMeta(meta);
                     } catch (IllegalArgumentException var15) {
                        meta.setBasePotionType(PotionType.WATER);
                        arrow.setItemMeta(meta);
                     }
                  }

                  result.put(arrow, damage);
               }
            } else {
               Material material;
               if (entry.contains(";")) {
                  parts = entry.split(";");
                  material = Material.valueOf(parts[0]);
                  String[] var17 = parts[1].split(",");
                  var18 = var17.length;

                  for(int var8 = 0; var8 < var18; ++var8) {
                     String damageCount = var17[var8];
                     String[] dc = damageCount.split(":");
                     damage = Integer.parseInt(dc[0]);
                     int count = Integer.parseInt(dc[1]);
                     ItemStack item = new ItemStack(material);
                     setDamageValue(item, damage);
                     result.put(item, count);
                  }
               } else {
                  parts = entry.split(":");
                  material = Material.valueOf(parts[0]);
                  count = Integer.parseInt(parts[1]);
                  ItemStack item = new ItemStack(material);
                  result.put(item, count);
               }
            }
         }

         return result;
      }
   }

   private static int getDamageValue(ItemStack item) {
      return item.getItemMeta() instanceof Damageable ? ((Damageable)item.getItemMeta()).getDamage() : 0;
   }

   private static void setDamageValue(ItemStack item, int damage) {
      if (item.getItemMeta() instanceof Damageable) {
         Damageable meta = (Damageable)item.getItemMeta();
         meta.setDamage(damage);
         item.setItemMeta(meta);
      }

   }

   public static boolean isDestructibleItem(Material material) {
      String name = material.name();
      Iterator var2 = TOOL_TYPES.iterator();

      String armorMaterial;
      while(var2.hasNext()) {
         armorMaterial = (String)var2.next();
         if (name.endsWith(armorMaterial)) {
            return true;
         }
      }

      var2 = ARMOR_MATERIALS.iterator();

      while(var2.hasNext()) {
         armorMaterial = (String)var2.next();
         Iterator var4 = ARMOR_PIECES.iterator();

         while(var4.hasNext()) {
            String armorPiece = (String)var4.next();
            if (name.equals(armorMaterial + armorPiece)) {
               return true;
            }
         }
      }

      return name.equals("BOW") || name.equals("FISHING_ROD") || name.equals("FLINT_AND_STEEL") || name.equals("SHEARS") || name.equals("SHIELD") || name.equals("ELYTRA") || name.equals("TRIDENT") || name.equals("CROSSBOW") || name.equals("CARROT_ON_A_STICK") || name.equals("WARPED_FUNGUS_ON_A_STICK") || name.equals("MACE");
   }

   public static class ItemGroup {
      private final Material material;
      private final Map<Integer, Integer> damageCount;
      private final Map<String, Integer> potionTypeCount;

      public ItemGroup(Material material) {
         this.material = material;
         this.damageCount = new HashMap();
         this.potionTypeCount = new HashMap();
      }

      public void addItem(int damage, int count) {
         this.damageCount.merge(damage, count, Integer::sum);
      }

      public void addPotionArrow(PotionType potionType, int count) {
         String potionKey = potionType.name();
         this.potionTypeCount.merge(potionKey, count, Integer::sum);
      }

      @Generated
      public Material getMaterial() {
         return this.material;
      }

      @Generated
      public Map<Integer, Integer> getDamageCount() {
         return this.damageCount;
      }

      @Generated
      public Map<String, Integer> getPotionTypeCount() {
         return this.potionTypeCount;
      }
   }
}
