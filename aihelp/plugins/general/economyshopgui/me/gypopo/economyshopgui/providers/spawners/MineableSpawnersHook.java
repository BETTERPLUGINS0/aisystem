package me.gypopo.economyshopgui.providers.spawners;

import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Nullable;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.providers.SpawnerManager;
import me.gypopo.economyshopgui.providers.SpawnerProvider;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class MineableSpawnersHook implements SpawnerProvider {
   public String getProviderName() {
      return "MINEABLESPAWNERS";
   }

   public ItemStack setSpawnedType(@Nullable ShopItem shopItem, ItemStack item, EntityType type) {
      ItemMeta meta = item.getItemMeta();
      if (shopItem == null) {
         BlockStateMeta blockStateMeta = (BlockStateMeta)meta;
         CreatureSpawner creatureSpawner = (CreatureSpawner)blockStateMeta.getBlockState();
         creatureSpawner.setSpawnedType(type);
         blockStateMeta.setBlockState(creatureSpawner);
         item = EconomyShopGUI.getInstance().versionHandler.setNBTString(item, "ms_mob", type.name());
      }

      if (!meta.hasDisplayName()) {
         EconomyShopGUI.getInstance().getMetaUtils().setDisplayName(meta, SpawnerManager.getDefaultName(type));
      }

      item.setItemMeta(meta);
      return item;
   }

   public boolean isShopSpawner(ItemStack itemToMatch, ItemStack shopItem) {
      return EconomyShopGUI.getInstance().isSimilar(itemToMatch, shopItem, new ArrayList(Arrays.asList("ms_mob", "display::Lore", "display::Name")));
   }

   public Double getSpawnerSellPrice(ItemStack itemToSearch, double baseSellPrice) {
      return baseSellPrice;
   }

   public String getSpawnedType(ItemStack item) {
      return EconomyShopGUI.getInstance().versionHandler.getSpawnerType(item);
   }
}
