package me.gypopo.economyshopgui.providers.spawners;

import dev.rosewood.rosestacker.utils.ItemUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.providers.SpawnerManager;
import me.gypopo.economyshopgui.providers.SpawnerProvider;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class RoseStackerHook implements SpawnerProvider {
   public String getProviderName() {
      return "ROSESTACKER";
   }

   public ItemStack setSpawnedType(@Nullable ShopItem shopItem, ItemStack item, EntityType type) {
      ItemMeta meta = item.getItemMeta();
      if (shopItem == null) {
         BlockStateMeta blockStateMeta = (BlockStateMeta)meta;
         CreatureSpawner creatureSpawner = (CreatureSpawner)blockStateMeta.getBlockState();
         creatureSpawner.setSpawnedType(type);
         blockStateMeta.setBlockState(creatureSpawner);
      }

      String name = null;
      List<String> lore = null;
      if (meta.hasDisplayName()) {
         name = meta.getDisplayName();
      }

      if (meta.hasLore()) {
         lore = meta.getLore();
      }

      ItemStack stack = ItemUtils.getSpawnerAsStackedItemStack(type, 1);
      if (stack != null && stack.hasItemMeta()) {
         if (name == null && stack.getItemMeta().hasDisplayName()) {
            meta.setDisplayName(stack.getItemMeta().getDisplayName());
         }

         if (lore == null && stack.getItemMeta().hasLore()) {
            meta.setLore(stack.getItemMeta().getLore());
         }
      }

      if (!meta.hasDisplayName()) {
         EconomyShopGUI.getInstance().getMetaUtils().setDisplayName(meta, SpawnerManager.getDefaultName(type));
      }

      meta.addItemFlags(ItemFlag.values());
      item.setItemMeta(meta);
      item = EconomyShopGUI.getInstance().versionHandler.setNBTInt(item, "StackSize", 1);
      return item;
   }

   public boolean isShopSpawner(ItemStack itemToMatch, ItemStack shopItem) {
      return EconomyShopGUI.getInstance().isSimilar(itemToMatch, shopItem, new ArrayList(Arrays.asList("StackSize", "EntityType", "display::Name")));
   }

   public Double getSpawnerSellPrice(ItemStack itemToSearch, double baseSellPrice) {
      Integer amount = EconomyShopGUI.getInstance().versionHandler.getNBTInt(itemToSearch, "StackSize");
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
