package me.gypopo.economyshopgui.providers.spawners;

import com.bgsoftware.wildstacker.api.WildStacker;
import com.bgsoftware.wildstacker.api.upgrades.SpawnerUpgrade;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import me.gypopo.economyshopgui.EconomyShopGUI;
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

public class WildStackerHook implements SpawnerProvider {
   private WildStacker ws;
   private FileConfiguration wsconfig;
   private List<SpawnerUpgrade> upgrades = new ArrayList();
   private final String itemName;
   private final List<String> itemLore;

   public WildStackerHook(WildStacker wildStacker) {
      this.ws = wildStacker;
      this.wsconfig = EconomyShopGUI.getInstance().loadConfiguration(new File(EconomyShopGUI.getInstance().getDataFolder().getParent() + "/WildStacker/config.yml"), "WildStacker config");
      String name = this.wsconfig.getString("spawners.spawner-item.name", "Spawner");
      this.itemName = name != null ? ChatColor.translateAlternateColorCodes('&', name.replace("{0}", String.valueOf(1)).replace("{1}", "%spawner-type%").replace("{2}", "")) : ChatColor.GOLD + "x1 &f&o%spawner-type% Spawners";
      List<String> lore = this.wsconfig.getStringList("spawners.spawner-item.lore");
      this.itemLore = lore != null ? (List)lore.stream().map((s) -> {
         return ChatColor.translateAlternateColorCodes('&', s).replace("{0}", String.valueOf(1)).replace("{1}", "%spawner-type%");
      }).collect(Collectors.toList()) : Collections.singletonList(ChatColor.GRAY + "%spawner-type%");
      if (!this.upgrades.isEmpty()) {
         this.upgrades.clear();
      }

      List<Integer> ids = new ArrayList();
      Iterator var5 = this.ws.getUpgradesManager().getAllUpgrades().iterator();

      while(var5.hasNext()) {
         SpawnerUpgrade upgrade = (SpawnerUpgrade)var5.next();
         ids.add(upgrade.getId());
      }

      Collections.sort(ids);
      var5 = ids.iterator();

      while(var5.hasNext()) {
         int id = (Integer)var5.next();
         this.upgrades.add(this.ws.getUpgradesManager().getUpgrade(id));
      }

   }

   public String getProviderName() {
      return "WILDSTACKER";
   }

   public ItemStack setSpawnedType(@Nullable ShopItem shopItem, ItemStack item, EntityType type) {
      ItemMeta meta = item.getItemMeta();
      if (shopItem == null) {
         BlockStateMeta blockStateMeta = (BlockStateMeta)meta;
         CreatureSpawner creatureSpawner = (CreatureSpawner)blockStateMeta.getBlockState();
         creatureSpawner.setSpawnedType(type);
         blockStateMeta.setBlockState(creatureSpawner);
         item = EconomyShopGUI.getInstance().versionHandler.setNBTInt(item, "spawners-amount", 1);
      }

      String name = meta.hasDisplayName() ? meta.getDisplayName() : null;
      List<String> lore = meta.hasLore() ? meta.getLore() : null;
      if (name == null && this.itemName != null) {
         meta.setDisplayName(this.itemName.replace("%spawner-type%", this.getEntityName(type)));
      }

      if (name == null && this.itemName == null) {
         EconomyShopGUI.getInstance().getMetaUtils().setDisplayName(meta, SpawnerManager.getDefaultName(type));
      }

      if (lore == null && this.itemLore != null) {
         meta.setLore((List)this.itemLore.stream().map((s) -> {
            return s.replace("%spawner-type%", this.getEntityName(type));
         }).collect(Collectors.toList()));
      }

      item.setItemMeta(meta);
      return item;
   }

   public boolean isShopSpawner(ItemStack itemToMatch, ItemStack shopItem) {
      return EconomyShopGUI.getInstance().isSimilar(itemToMatch, shopItem, new ArrayList(Arrays.asList("spawners-amount", "spawners-upgrade", "display::Name")));
   }

   public Double getSpawnerSellPrice(ItemStack itemToSearch, double baseSellPrice) {
      return this.getSellPrice(itemToSearch, baseSellPrice);
   }

   public String getSpawnedType(ItemStack item) {
      ItemMeta itemMeta = item.getItemMeta();
      BlockStateMeta blockStateMeta = (BlockStateMeta)itemMeta;
      CreatureSpawner creatureSpawner = (CreatureSpawner)blockStateMeta.getBlockState();
      return creatureSpawner.getSpawnedType().name();
   }

   private Double getSellPrice(ItemStack spawner, double baseSellPrice) {
      Integer amount = EconomyShopGUI.getInstance().versionHandler.getNBTInt(spawner, "spawners-amount");
      amount = amount == null ? 1 : amount;
      boolean multiplyCost = this.wsconfig.getBoolean("spawners.spawner-upgrades.multiply-stack-amount", true);
      Integer upgradeID = EconomyShopGUI.getInstance().versionHandler.getNBTInt(spawner, "spawners-upgrade");
      upgradeID = upgradeID != null && upgradeID != -1 ? upgradeID : 0;
      double upgradeCost = this.getTotalUpgradeCost(upgradeID);
      return multiplyCost ? (double)amount * upgradeCost + (double)amount * baseSellPrice : upgradeCost + (double)amount * baseSellPrice;
   }

   private double getTotalUpgradeCost(Integer upgradeID) {
      if (upgradeID != null && upgradeID != 0) {
         double cost = 0.0D;
         Iterator var4 = this.upgrades.iterator();

         while(var4.hasNext()) {
            SpawnerUpgrade upgrade = (SpawnerUpgrade)var4.next();
            cost += upgrade.getCost();
            if (upgrade.getId() == upgradeID) {
               break;
            }
         }

         return cost;
      } else {
         return 0.0D;
      }
   }

   private String getEntityName(EntityType type) {
      String[] arr = type.name().toLowerCase().replace("_", " ").split(" ");
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < arr.length; ++i) {
         sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
      }

      return sb.toString().trim();
   }
}
