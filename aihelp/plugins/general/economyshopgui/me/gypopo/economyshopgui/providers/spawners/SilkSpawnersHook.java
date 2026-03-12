package me.gypopo.economyshopgui.providers.spawners;

import de.dustplanet.util.SilkUtil;
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

public class SilkSpawnersHook implements SpawnerProvider {
   private SilkUtil su;

   public SilkSpawnersHook(SilkUtil silkUtil) {
      this.su = silkUtil;
   }

   public String getProviderName() {
      return "SILKSPAWNERS";
   }

   public ItemStack setSpawnedType(@Nullable ShopItem shopItem, ItemStack item, EntityType type) {
      String entityType = type.name();

      String targetEntityType;
      try {
         targetEntityType = (String)this.su.getDisplayNameToMobID().get(entityType);
      } catch (NullPointerException var7) {
         targetEntityType = entityType;
      }

      if (targetEntityType == null) {
         targetEntityType = entityType;
      }

      ItemMeta meta = item.getItemMeta();
      if (!meta.hasDisplayName()) {
         EconomyShopGUI.getInstance().getMetaUtils().setDisplayName(meta, SpawnerManager.getDefaultName(type));
      }

      item.setItemMeta(meta);
      return this.su.nmsProvider.setNBTEntityID(item, targetEntityType);
   }

   public boolean isShopSpawner(ItemStack itemToMatch, ItemStack shopItem) {
      return EconomyShopGUI.getInstance().isSimilar(itemToMatch, shopItem, new ArrayList(Arrays.asList("display::Name")));
   }

   public Double getSpawnerSellPrice(ItemStack itemToSearch, double baseSellPrice) {
      return baseSellPrice;
   }

   public String getSpawnedType(ItemStack item) {
      ItemMeta itemMeta = item.getItemMeta();
      BlockStateMeta blockStateMeta = (BlockStateMeta)itemMeta;
      CreatureSpawner creatureSpawner = (CreatureSpawner)blockStateMeta.getBlockState();
      return creatureSpawner.getSpawnedType().name();
   }
}
