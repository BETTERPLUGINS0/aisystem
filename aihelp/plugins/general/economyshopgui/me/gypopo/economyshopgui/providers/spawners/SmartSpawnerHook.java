package me.gypopo.economyshopgui.providers.spawners;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.api.SmartSpawnerAPI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.providers.SpawnerManager;
import me.gypopo.economyshopgui.providers.SpawnerProvider;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

public class SmartSpawnerHook implements SpawnerProvider {
   private final SmartSpawnerAPI api = SmartSpawner.getInstance().getAPI();

   public String getProviderName() {
      return "SMART_SPAWNER";
   }

   public ItemStack setSpawnedType(@Nullable ShopItem shopItem, ItemStack item, EntityType type) {
      String name = null;
      List<String> lore = null;
      ItemMeta meta;
      if (item.hasItemMeta()) {
         meta = item.getItemMeta();
         if (meta.hasDisplayName()) {
            name = meta.getDisplayName();
         }

         if (meta.hasLore()) {
            lore = meta.getLore();
         }
      }

      item = this.api.createSpawnerItem(type);
      if (item.hasItemMeta()) {
         meta = item.getItemMeta();
         if (name != null) {
            meta.setDisplayName(name);
         }

         if (!meta.hasDisplayName()) {
            EconomyShopGUI.getInstance().getMetaUtils().setDisplayName(meta, SpawnerManager.getDefaultName(type));
         }

         if (lore != null) {
            meta.setLore(lore);
         }

         item.setItemMeta(meta);
      }

      return item;
   }

   public boolean isShopSpawner(ItemStack itemToMatch, ItemStack shopItem) {
      return EconomyShopGUI.getInstance().isSimilar(itemToMatch, shopItem, new ArrayList(Arrays.asList("display::Lore", "display::Name", "HideFlags")));
   }

   public Double getSpawnerSellPrice(ItemStack itemToSearch, double baseSellPrice) {
      return baseSellPrice;
   }

   public String getSpawnedType(ItemStack item) {
      return this.api.getSpawnerEntityType(item).name();
   }
}
