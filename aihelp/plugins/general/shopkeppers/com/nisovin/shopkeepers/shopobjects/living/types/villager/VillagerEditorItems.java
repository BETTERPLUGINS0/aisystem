package com.nisovin.shopkeepers.shopobjects.living.types.villager;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Supplier;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.inventory.ItemStack;

public class VillagerEditorItems {
   private static final Map<Profession, Supplier<ItemStack>> PROFESSION_EDITOR_ITEMS;
   private static final Map<Type, Optional<Color>> VILLAGER_TYPE_EDITOR_ITEM_COLORS;

   public static ItemStack getProfessionEditorItem(Profession profession) {
      Supplier<ItemStack> itemSupplier = (Supplier)PROFESSION_EDITOR_ITEMS.getOrDefault(profession, () -> {
         return new ItemStack(Material.BARRIER);
      });
      ItemStack iconItem = (ItemStack)itemSupplier.get();

      assert iconItem != null;

      return iconItem;
   }

   public static ItemStack getVillagerTypeEditorItem(Type villagerType) {
      ItemStack iconItem = new ItemStack(Material.LEATHER_CHESTPLATE);
      Color color = (Color)((Optional)VILLAGER_TYPE_EDITOR_ITEM_COLORS.getOrDefault(villagerType, Optional.empty())).orElse((Object)null);
      if (color != null) {
         ItemUtils.setLeatherColor(iconItem, color);
      }

      return iconItem;
   }

   private VillagerEditorItems() {
   }

   static {
      PROFESSION_EDITOR_ITEMS = Map.ofEntries(new Entry[]{Map.entry(Profession.ARMORER, () -> {
         return new ItemStack(Material.BLAST_FURNACE);
      }), Map.entry(Profession.BUTCHER, () -> {
         return new ItemStack(Material.SMOKER);
      }), Map.entry(Profession.CARTOGRAPHER, () -> {
         return new ItemStack(Material.CARTOGRAPHY_TABLE);
      }), Map.entry(Profession.CLERIC, () -> {
         return new ItemStack(Material.BREWING_STAND);
      }), Map.entry(Profession.FARMER, () -> {
         return new ItemStack(Material.WHEAT);
      }), Map.entry(Profession.FISHERMAN, () -> {
         return new ItemStack(Material.FISHING_ROD);
      }), Map.entry(Profession.FLETCHER, () -> {
         return new ItemStack(Material.FLETCHING_TABLE);
      }), Map.entry(Profession.LEATHERWORKER, () -> {
         return new ItemStack(Material.LEATHER);
      }), Map.entry(Profession.LIBRARIAN, () -> {
         return new ItemStack(Material.LECTERN);
      }), Map.entry(Profession.MASON, () -> {
         return new ItemStack(Material.STONECUTTER);
      }), Map.entry(Profession.SHEPHERD, () -> {
         return new ItemStack(Material.LOOM);
      }), Map.entry(Profession.TOOLSMITH, () -> {
         return new ItemStack(Material.SMITHING_TABLE);
      }), Map.entry(Profession.WEAPONSMITH, () -> {
         return new ItemStack(Material.GRINDSTONE);
      }), Map.entry(Profession.NITWIT, () -> {
         ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
         return ItemUtils.setLeatherColor(item, Color.GREEN);
      }), Map.entry(Profession.NONE, () -> {
         return new ItemStack(Material.BARRIER);
      })});
      VILLAGER_TYPE_EDITOR_ITEM_COLORS = Map.ofEntries(new Entry[]{Map.entry(Type.PLAINS, Optional.empty()), Map.entry(Type.DESERT, (Optional)Unsafe.castNonNull(Optional.of(Color.ORANGE))), Map.entry(Type.JUNGLE, (Optional)Unsafe.castNonNull(Optional.of(Color.YELLOW.mixColors(new Color[]{Color.ORANGE})))), Map.entry(Type.SAVANNA, (Optional)Unsafe.castNonNull(Optional.of(Color.RED))), Map.entry(Type.SNOW, (Optional)Unsafe.castNonNull(Optional.of(DyeColor.CYAN.getColor()))), Map.entry(Type.SWAMP, (Optional)Unsafe.castNonNull(Optional.of(DyeColor.PURPLE.getColor()))), Map.entry(Type.TAIGA, (Optional)Unsafe.castNonNull(Optional.of(Color.WHITE.mixDyes(new DyeColor[]{DyeColor.BROWN}))))});
   }
}
