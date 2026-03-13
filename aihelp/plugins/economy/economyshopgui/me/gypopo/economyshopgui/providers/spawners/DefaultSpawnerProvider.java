package me.gypopo.economyshopgui.providers.spawners;

import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Nullable;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.events.SpawnerBreakEvent;
import me.gypopo.economyshopgui.events.SpawnerInteractEvent;
import me.gypopo.economyshopgui.events.SpawnerPlaceEvent;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.providers.SpawnerManager;
import me.gypopo.economyshopgui.providers.SpawnerProvider;
import me.gypopo.economyshopgui.util.ServerInfo;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DefaultSpawnerProvider implements SpawnerProvider {
   private final EconomyShopGUI plugin;
   private final boolean interact;

   public DefaultSpawnerProvider(EconomyShopGUI plugin, @Nullable SpawnerInteractEvent interactEvent, @Nullable SpawnerPlaceEvent placeEvent, @Nullable SpawnerBreakEvent breakEvent) {
      this.plugin = plugin;
      if (placeEvent != null) {
         this.plugin.getServer().getPluginManager().registerEvents(placeEvent, this.plugin);
      }

      if (breakEvent != null) {
         this.plugin.getServer().getPluginManager().registerEvents(breakEvent, this.plugin);
      }

      if (interactEvent != null) {
         this.plugin.getServer().getPluginManager().registerEvents(interactEvent, this.plugin);
      }

      this.interact = interactEvent == null;
   }

   public String getProviderName() {
      return "DEFAULT";
   }

   public ItemStack setSpawnedType(@Nullable ShopItem shopItem, ItemStack item, EntityType type) {
      if (shopItem == null) {
         item = this.plugin.versionHandler.setSpawnerType(item, type.name());
      }

      ItemMeta meta = item.getItemMeta();
      if (!this.interact) {
         this.hideInteractLore(meta);
      }

      if (!meta.hasDisplayName()) {
         EconomyShopGUI.getInstance().getMetaUtils().setDisplayName(meta, SpawnerManager.getDefaultName(type));
      }

      item.setItemMeta(meta);
      return item;
   }

   public boolean isShopSpawner(ItemStack itemToMatch, ItemStack shopItem) {
      return this.plugin.isSimilar(itemToMatch, shopItem, new ArrayList(Arrays.asList("display::Name")));
   }

   public Double getSpawnerSellPrice(ItemStack itemToSearch, double baseSellPrice) {
      return baseSellPrice;
   }

   public String getSpawnedType(ItemStack item) {
      return this.plugin.versionHandler.getSpawnerType(item);
   }

   private void hideInteractLore(ItemMeta meta) {
      if (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_21_R4) && !ServerInfo.supportsPaper()) {
         ItemFlag flag = ItemFlag.valueOf("HIDE_BLOCK_ENTITY_DATA");
         meta.addItemFlags(new ItemFlag[]{flag});
      } else {
         meta.addItemFlags(ItemFlag.values());
      }

   }
}
