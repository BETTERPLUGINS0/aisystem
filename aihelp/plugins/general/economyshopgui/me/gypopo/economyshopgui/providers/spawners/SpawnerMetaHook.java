package me.gypopo.economyshopgui.providers.spawners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mc.rellox.spawnermeta.api.spawner.IVirtual;
import mc.rellox.spawnermeta.spawner.type.SpawnerType;
import mc.rellox.spawnermeta.utility.DataManager;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.providers.SpawnerManager;
import me.gypopo.economyshopgui.providers.SpawnerProvider;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

public class SpawnerMetaHook implements SpawnerProvider {
   public String getProviderName() {
      return "SPAWNERMETA";
   }

   public ItemStack setSpawnedType(@Nullable ShopItem shopItem, ItemStack item, EntityType type) {
      SpawnerType entity = SpawnerType.of(type);
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

      item = DataManager.getSpawner(entity, 1);
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
      IVirtual virtual = IVirtual.of(item);
      return virtual == null ? null : virtual.getType().entity().name();
   }
}
