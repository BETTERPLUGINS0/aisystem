package me.ag4.playershop.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import me.ag4.playershop.PlayerShop;
import me.ag4.playershop.Utils;
import me.ag4.playershop.files.Config;
import me.ag4.playershop.files.Lang;
import me.ag4.playershop.hooks.WorldGuardAPI;
import me.ag4.playershop.manager.ShopDefaultAccess;
import me.ag4.playershop.manager.ShopOwnerAccess;
import me.ag4.playershop.objects.Shop;
import me.ag4.playershop.objects.SignMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class PlayerShopAPI {
   public static List<Shop> getPlayerShopList() {
      return DataUtils.shopList;
   }

   public static Shop getPlayerShop(String key) {
      Iterator var1 = getPlayerShopList().iterator();

      Shop shop;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         shop = (Shop)var1.next();
      } while(!shop.getKey().equals(key));

      return shop;
   }

   public static Shop getPlayerShop(Location location) {
      Iterator var1 = getPlayerShopList().iterator();

      Shop shop;
      World shopWorldBlock;
      int shopXBlock;
      int shopYBlock;
      int shopZBlock;
      World worldBlock;
      int xBlock;
      int yBlock;
      int zBlock;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         shop = (Shop)var1.next();
         Location shopLocation = shop.getLocation();
         shopWorldBlock = shopLocation.getWorld();
         shopXBlock = shopLocation.getBlockX();
         shopYBlock = shopLocation.getBlockY();
         shopZBlock = shopLocation.getBlockZ();
         worldBlock = location.getWorld();
         xBlock = location.getBlockX();
         yBlock = location.getBlockY();
         zBlock = location.getBlockZ();
      } while(shopWorldBlock != worldBlock || shopXBlock != xBlock || shopYBlock != yBlock || shopZBlock != zBlock);

      return shop;
   }

   public static Shop createShop(Player p, Location location) {
      String uniqueKey = UUID.randomUUID().toString();
      return new Shop(uniqueKey, p.getUniqueId(), p.getName(), 0.0D, location);
   }

   public static BlockFace getBlockFaceFromAngle(double angle) {
      angle = (angle + 360.0D) % 360.0D;
      if (angle >= 45.0D && angle < 135.0D) {
         return BlockFace.WEST;
      } else if (angle >= 135.0D && angle < 225.0D) {
         return BlockFace.SOUTH;
      } else {
         return angle >= 225.0D && angle < 315.0D ? BlockFace.EAST : BlockFace.NORTH;
      }
   }

   public static int getPlayerShopCount(UUID uuid) {
      int count = 0;
      Iterator var2 = getPlayerShopList().iterator();

      while(var2.hasNext()) {
         Shop shop = (Shop)var2.next();
         if (Objects.equals(shop.getOwner(), uuid)) {
            ++count;
         }
      }

      return count;
   }

   public static int getPlayerShopCount(Player player) {
      int count = 0;
      Iterator var2 = getPlayerShopList().iterator();

      while(var2.hasNext()) {
         Shop shop = (Shop)var2.next();
         if (Objects.equals(shop.getOwner(), player.getUniqueId())) {
            ++count;
         }
      }

      return count;
   }

   public static int getPlayerShopMax(Player player) {
      int highestLimit = PlayerShop.getInstance().config().getInt("Group.default");
      if (highestLimit == 0) {
         return Integer.MAX_VALUE;
      } else {
         Iterator var2 = ((ConfigurationSection)Objects.requireNonNull(PlayerShop.getInstance().config().getConfigurationSection("Group"))).getKeys(false).iterator();

         while(var2.hasNext()) {
            String group = (String)var2.next();
            if (player.hasPermission("playershop." + group)) {
               int limit = PlayerShop.getInstance().config().getInt("Group." + group);
               if (limit == 0) {
                  return Integer.MAX_VALUE;
               }

               if (limit > highestLimit) {
                  highestLimit = limit;
               }
            }
         }

         return highestLimit;
      }
   }

   public static boolean buySection(int s) {
      return s == 6 || s == 7 || s == 8 || s == 15 || s == 16 || s == 17 || s == 24 || s == 25 || s == 26;
   }

   public static boolean isEnabledWorld(Player p) {
      return PlayerShop.getInstance().config().getStringList("Enabled-Worlds").contains(p.getWorld().getName());
   }

   public static boolean isPermissionEnabled() {
      return PlayerShop.getInstance().config().getBoolean("Permission.Enable");
   }

   public static boolean getWorldGuardFlagState(Block block, Player player) {
      if (!Config.IS_WORLDGUARD_ENABLE.toBoolean()) {
         return false;
      } else {
         return !WorldGuardAPI.getFlagState(block, player);
      }
   }

   public static boolean isItemInBlacklist(ItemStack item) {
      ItemMeta meta = item.getItemMeta();
      String itemDisplayName = meta != null ? meta.getDisplayName() : null;
      String itemMaterial = item.getType().name();
      List<String> itemLore = meta != null && meta.hasLore() ? meta.getLore() : new ArrayList();
      String itemCustomModelData = meta != null && meta.hasCustomModelData() ? String.valueOf(meta.getCustomModelData()) : null;
      ConfigurationSection blackListItemsSection = PlayerShop.getInstance().config().getConfigurationSection("BlackListItems");
      if (blackListItemsSection != null) {
         Iterator var7 = blackListItemsSection.getKeys(false).iterator();

         String configLore;
         String configCustomModelData;
         do {
            String configMaterial;
            do {
               String configDisplayName;
               do {
                  do {
                     ConfigurationSection itemSection;
                     do {
                        if (!var7.hasNext()) {
                           return false;
                        }

                        String section = (String)var7.next();
                        itemSection = blackListItemsSection.getConfigurationSection(section);
                     } while(itemSection == null);

                     configDisplayName = itemSection.getString("DisplayName");
                     configMaterial = itemSection.getString("Material");
                     configLore = itemSection.getString("Lore");
                     configCustomModelData = itemSection.getString("CustomModelData");
                  } while(configDisplayName != null && !Utils.hex(configDisplayName).equals(itemDisplayName));
               } while(configMaterial != null && !configMaterial.equalsIgnoreCase(itemMaterial));
            } while(configLore != null && !((List)itemLore).stream().anyMatch((line) -> {
               return Utils.hex(configLore).contains(Utils.hex(line));
            }));
         } while(configCustomModelData != null && !configCustomModelData.equals(itemCustomModelData));

         return true;
      } else {
         return false;
      }
   }

   public static boolean isPlaceMerge(Block block) {
      BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
      BlockFace[] var2 = faces;
      int var3 = faces.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockFace face = var2[var4];
         Block relativeBlock = block.getRelative(face);
         if (getPlayerShop(relativeBlock.getLocation()) != null) {
            return false;
         }
      }

      return true;
   }

   public static boolean checkPrice(double value) {
      try {
         int roundedValue = (int)(value * 100.0D + 0.5D);
         double decimalPlaces = value * 100.0D - (double)roundedValue;
         return decimalPlaces == 0.0D || Math.abs(decimalPlaces) < 0.01D;
      } catch (NumberFormatException var5) {
         return false;
      }
   }

   public static boolean isShopChest(ItemStack item) {
      ItemMeta meta = item.getItemMeta();
      if (meta == null) {
         return false;
      } else {
         return meta.getDisplayName().equals(Utils.hex(PlayerShop.getInstance().config().getString("Craft.Name"))) && item.getType().equals(Material.CHEST);
      }
   }

   public static void removeShop(String key) {
      Shop shop = getPlayerShop(key);
      if (shop != null) {
         getPlayerShopList().remove(shop);
         shop.removeShop();
      }

   }

   public static void addBuySection(Inventory inv, double price) {
      ItemStack diamond = new ItemStack((Material)Objects.requireNonNull(Material.getMaterial((String)Objects.requireNonNull(PlayerShop.getInstance().config().getString("GUI-Item")))));
      ItemMeta diamondMeta = diamond.getItemMeta();

      assert diamondMeta != null;

      diamondMeta.setDisplayName(Lang.GUI_Price.toString().replace("{price}", Utils.formatPrice(price)));
      diamond.setItemMeta(diamondMeta);
      ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
      ItemMeta glassMeta = glass.getItemMeta();

      assert glassMeta != null;

      glassMeta.setDisplayName(Utils.hex("&r"));
      glass.setItemMeta(glassMeta);
      inv.setItem(6, glass);
      inv.setItem(7, glass);
      inv.setItem(8, glass);
      inv.setItem(15, glass);
      inv.setItem(16, diamond);
      inv.setItem(17, glass);
      inv.setItem(24, glass);
      inv.setItem(25, glass);
      inv.setItem(26, glass);
   }

   public static void removeBuySection(Inventory inv) {
      inv.setItem(6, (ItemStack)null);
      inv.setItem(7, (ItemStack)null);
      inv.setItem(8, (ItemStack)null);
      inv.setItem(15, (ItemStack)null);
      inv.setItem(16, (ItemStack)null);
      inv.setItem(17, (ItemStack)null);
      inv.setItem(24, (ItemStack)null);
      inv.setItem(25, (ItemStack)null);
      inv.setItem(26, (ItemStack)null);
   }

   public static void updateBlock(Block block, Player player) {
      Vector playerDirection = player.getLocation().getDirection();
      block.setType(Material.CHEST, false);
      double angle = Math.atan2(playerDirection.getX(), playerDirection.getZ());
      angle = Math.toDegrees(angle);
      BlockFace facingDirection = getBlockFaceFromAngle(angle);
      BlockData blockData = block.getBlockData();
      ((Directional)blockData).setFacing(facingDirection);
      block.setBlockData(blockData);
   }

   public static void openSign(Player target, Shop shop) {
      SignMenu signGui = new SignMenu(PlayerShop.getPlugin(PlayerShop.class));
      SignMenu.Menu menu = signGui.newMenu(Arrays.asList("", Lang.Sign_Line_2.toString(), Lang.Sign_Line_3.toString(), Lang.Sign_Line_4.toString())).reopenIfFail(true).response((player, strings) -> {
         try {
            double price = Double.parseDouble(strings[0]);
            if (checkPrice(price)) {
               if (price >= 0.01D) {
                  shop.setPrice(price);
                  player.sendMessage(Lang.Price_Set.toString().replace("{price}", Utils.formatPrice(price)));
                  player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
               } else {
                  player.sendMessage(Lang.Error_Price_0.toString());
               }
            } else {
               player.sendMessage(Lang.Error_Price_Enter.toString());
            }
         } catch (NumberFormatException var5) {
            player.sendMessage(Lang.Error_Price_Enter.toString());
         }

         return true;
      });
      menu.open(target);
   }

   public static void openShop(Player player, Shop shop) {
      if (shop.getOwner().equals(player.getUniqueId())) {
         new ShopOwnerAccess(player, shop);
      } else {
         new ShopDefaultAccess(player, shop);
      }

   }

   public static void broadCastPurchaseSetUp() {
      Bukkit.getScheduler().runTaskTimerAsynchronously(PlayerShop.getInstance(), () -> {
         List<UUID> keys = new ArrayList(DataUtils.purchase.keySet());
         Iterator var1 = keys.iterator();

         while(var1.hasNext()) {
            UUID uuid = (UUID)var1.next();
            Player player = Bukkit.getPlayer(uuid);
            double price = (Double)DataUtils.purchase.get(uuid);
            if (player != null && price != 0.0D) {
               player.sendMessage(Utils.hex(Lang.BROADCAST_PURCHASES.toString().replace("{price}", Utils.formatPrice(price))));
               DataUtils.purchase.remove(uuid);
            }
         }

      }, 20L, 1200L);
   }
}
