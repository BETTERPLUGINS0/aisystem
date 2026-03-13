package me.gypopo.economyshopgui.providers.spawners;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.providers.SpawnerManager;
import me.gypopo.economyshopgui.providers.SpawnerProvider;
import org.bukkit.ChatColor;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SilkSpawnersV2Hook implements SpawnerProvider {
   private FileConfiguration ssConfig;
   private boolean customName = true;
   private String itemName;
   private List<String> itemLore;
   private String prefix;
   private String oldPrefix;

   public SilkSpawnersV2Hook() {
      this.itemName = ChatColor.LIGHT_PURPLE + "Spawner";
      this.itemLore = new ArrayList();
      this.prefix = ChatColor.YELLOW + "";
      this.oldPrefix = "";
   }

   public String getProviderName() {
      try {
         File conf = new File(EconomyShopGUI.getInstance().getDataFolder().getParent() + "/SilkSpawners_v2/config.yml");
         this.ssConfig = EconomyShopGUI.getInstance().loadConfiguration(conf, "SilkSpawners config");
         this.loadValues();
      } catch (Exception var2) {
         SendMessage.warnMessage("Failed to load custom spawner names from SilkSpawnersV2, using default...");
         this.customName = false;
         var2.printStackTrace();
      }

      return "SILKSPAWNERSV2";
   }

   private void loadValues() {
      this.itemName = ChatColor.translateAlternateColorCodes('$', this.ssConfig.getString("spawner.item.name"));
      this.itemLore = (List)this.ssConfig.getStringList("spawner.item.lore").stream().map((s) -> {
         return ChatColor.translateAlternateColorCodes('$', s);
      }).collect(Collectors.toList());
      this.prefix = ChatColor.translateAlternateColorCodes('$', this.ssConfig.getString("spawner.item.prefix"));
      String oldPrefix = this.ssConfig.contains("spawner.item.prefix-old") ? "spawner.item.prefix-old" : "spawner.item.prefixOld";
      this.oldPrefix = ChatColor.translateAlternateColorCodes('$', this.ssConfig.getString(oldPrefix));
   }

   public ItemStack setSpawnedType(@Nullable ShopItem shopItem, ItemStack item, EntityType type) {
      String name = null;
      List<String> lore = null;
      ItemMeta iMeta;
      if (shopItem != null) {
         iMeta = item.getItemMeta();
         if (iMeta.hasDisplayName()) {
            name = iMeta.getDisplayName();
         }

         if (iMeta.hasLore()) {
            lore = iMeta.getLore();
         }
      }

      if (this.customName) {
         iMeta = item.getItemMeta();
         iMeta.setDisplayName(this.itemName);
         List<String> l = iMeta.hasLore() ? iMeta.getLore() : new ArrayList();
         ((List)l).add(0, this.serializedName(type));
         ((List)l).addAll(1, this.itemLore);
         iMeta.setLore((List)l);
         item.setItemMeta(iMeta);
      }

      if (shopItem != null) {
         iMeta = item.getItemMeta();
         if (name != null) {
            iMeta.setDisplayName(name);
         }

         if (this.itemName == null || this.itemName.isEmpty()) {
            EconomyShopGUI.getInstance().getMetaUtils().setDisplayName(iMeta, SpawnerManager.getDefaultName(type));
         }

         if (lore != null) {
            iMeta.setLore(lore);
         }

         item.setItemMeta(iMeta);
      }

      return item;
   }

   private String serializedName(EntityType type) {
      return this.prefix + type.getName().substring(0, 1).toUpperCase() + type.getName().substring(1);
   }

   public boolean isShopSpawner(ItemStack itemToMatch, ItemStack shopItem) {
      return this.getSpawnedType(itemToMatch).equals(this.getSpawnedType(shopItem)) && EconomyShopGUI.getInstance().isSimilar(itemToMatch, shopItem, new ArrayList(Arrays.asList("display", "BlockEntityTag")));
   }

   public Double getSpawnerSellPrice(ItemStack itemToSearch, double baseSellPrice) {
      return baseSellPrice;
   }

   public String getSpawnedType(ItemStack item) {
      try {
         if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            String lore = (String)item.getItemMeta().getLore().get(0);
            if (lore.startsWith(this.prefix)) {
               return EntityType.fromName(lore.replace(this.prefix, "").toLowerCase()).name();
            }

            if (!this.oldPrefix.equals("") && lore.startsWith(this.oldPrefix)) {
               return EntityType.fromName(lore.replace(this.oldPrefix, "").toLowerCase()).name();
            }
         }
      } catch (NullPointerException var5) {
      }

      ItemMeta itemMeta = item.getItemMeta();
      BlockStateMeta blockStateMeta = (BlockStateMeta)itemMeta;
      CreatureSpawner creatureSpawner = (CreatureSpawner)blockStateMeta.getBlockState();
      return creatureSpawner.getSpawnedType().name();
   }
}
