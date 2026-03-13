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

public class UltimateStackerHook implements SpawnerProvider {
   public String getProviderName() {
      return "ULTIMATESTACKER";
   }

   public ItemStack setSpawnedType(@Nullable ShopItem shopItem, ItemStack item, EntityType type) {
      ItemMeta meta = item.getItemMeta();
      if (shopItem == null) {
         BlockStateMeta blockStateMeta = (BlockStateMeta)meta;
         CreatureSpawner creatureSpawner = (CreatureSpawner)blockStateMeta.getBlockState();
         creatureSpawner.setSpawnedType(type);
         blockStateMeta.setBlockState(creatureSpawner);
         item = EconomyShopGUI.getInstance().versionHandler.setNBTInt(item, "spawner_stack_size", 1);
      }

      if (!meta.hasDisplayName()) {
         EconomyShopGUI.getInstance().getMetaUtils().setDisplayName(meta, SpawnerManager.getDefaultName(type));
      }

      item.setItemMeta(meta);
      return item;
   }

   public boolean isShopSpawner(ItemStack itemToMatch, ItemStack shopItem) {
      return EconomyShopGUI.getInstance().isSimilar(itemToMatch, shopItem, new ArrayList(Arrays.asList("spawner_stack_size", "display::Name")));
   }

   public Double getSpawnerSellPrice(ItemStack itemToSearch, double baseSellPrice) {
      Integer amount = EconomyShopGUI.getInstance().versionHandler.getNBTInt(itemToSearch, "spawner_stack_size");
      amount = amount == null ? 1 : amount;
      return (double)amount * baseSellPrice;
   }

   public String getSpawnedType(ItemStack item) {
      ItemMeta itemMeta = item.getItemMeta();
      BlockStateMeta blockStateMeta = (BlockStateMeta)itemMeta;
      CreatureSpawner creatureSpawner = (CreatureSpawner)blockStateMeta.getBlockState();
      return creatureSpawner.getSpawnedType().name();
   }
}
