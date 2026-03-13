package me.ag4.playershop.objects;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import me.ag4.playershop.PlayerShop;
import me.ag4.playershop.Utils;
import me.ag4.playershop.api.PlayerShopAPI;
import me.ag4.playershop.files.Config;
import me.ag4.playershop.files.PlayersFolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class Shop {
   private final String key;
   private final UUID owner;
   private final String username;
   private final Location location;
   private double price;
   private Inventory inventory;

   public Shop(String key, UUID owner, String username, double price, Location location, Inventory inventory) {
      this.key = key;
      this.owner = owner;
      this.username = username;
      this.location = location;
      this.price = price;
      this.inventory = inventory;
      PlayerShopAPI.getPlayerShopList().add(this);
      this.updateHolo();
   }

   public Shop(String key, UUID owner, String username, double price, Location location) {
      this.key = key;
      this.owner = owner;
      this.price = price;
      this.username = username;
      this.location = location;
      this.inventory = Bukkit.createInventory((InventoryHolder)null, 27);
      PlayerShopAPI.getPlayerShopList().add(this);
      this.updateHolo();
   }

   public String getKey() {
      return this.key;
   }

   public UUID getOwner() {
      return this.owner;
   }

   public String getUsername() {
      return this.username;
   }

   public Double getPrice() {
      return this.price;
   }

   public Location getLocation() {
      return this.location;
   }

   public Inventory getInventory() {
      return this.inventory;
   }

   public OfflinePlayer getPlayer() {
      if (PlayerShop.getInstance().getHookManager().isFloodgateEnabled()) {
         FloodgatePlayer pePlayer = FloodgateApi.getInstance().getPlayer(this.owner);
         if (pePlayer != null) {
            return (OfflinePlayer)(Bukkit.getPlayer(pePlayer.getUsername()) != null ? Bukkit.getPlayer(pePlayer.getCorrectUniqueId()) : Bukkit.getOfflinePlayer(pePlayer.getCorrectUniqueId()));
         } else {
            return (OfflinePlayer)(Bukkit.getPlayer(this.owner) != null ? Bukkit.getPlayerExact(this.username) : Bukkit.getOfflinePlayer(this.owner));
         }
      } else {
         return (OfflinePlayer)(Bukkit.getPlayer(this.owner) != null ? Bukkit.getPlayerExact(this.username) : Bukkit.getOfflinePlayer(this.owner));
      }
   }

   public ItemStack getItem(int slot) {
      return PlayersFolder.get(String.valueOf(this.owner)).getItemStack(this.key + ".items." + slot);
   }

   private ItemStack getMostCommonItem(Inventory inventory) {
      ItemStack[] items = inventory.getContents();
      Map<ItemStack, Integer> itemCounts = new HashMap();
      ItemStack[] var4 = items;
      int maxCount = items.length;

      for(int var6 = 0; var6 < maxCount; ++var6) {
         ItemStack item = var4[var6];
         if (item != null && item.getType() != Material.AIR) {
            itemCounts.put(item, (Integer)itemCounts.getOrDefault(item, 0) + 1);
         }
      }

      if (itemCounts.isEmpty()) {
         return new ItemStack(Material.BARRIER);
      } else {
         ItemStack mostCommonItem = null;
         maxCount = 0;
         Iterator var9 = itemCounts.entrySet().iterator();

         while(var9.hasNext()) {
            Entry<ItemStack, Integer> entry = (Entry)var9.next();
            if ((Integer)entry.getValue() > maxCount) {
               mostCommonItem = (ItemStack)entry.getKey();
               maxCount = (Integer)entry.getValue();
            }
         }

         return mostCommonItem;
      }
   }

   public void setPrice(double price) {
      this.price = price;
      this.updateHolo();
   }

   public void setInventory(Inventory inventory) {
      Inventory invClone = Bukkit.createInventory((InventoryHolder)null, inventory.getSize());
      invClone.setContents(inventory.getContents());
      PlayerShopAPI.removeBuySection(invClone);
      this.inventory = invClone;
   }

   public boolean isReady() {
      return this.price != 0.01D;
   }

   public boolean itemExist(int slot) {
      return this.inventory.getItem(slot) != null;
   }

   public void removeShop() {
      PlayerShopAPI.getPlayerShopList().remove(this);
      this.removeHolo(this.key);
   }

   public void updateHolo() {
      if (PlayerShop.getInstance().config().getBoolean("Hologram.Enable")) {
         Location location = this.location.clone().add(0.5D, 2.0D, 0.5D);
         Hologram hologram = DHAPI.getHologram(this.key);
         String firstLine;
         if (hologram != null) {
            ItemStack itemStack = this.getMostCommonItem(this.inventory);
            String secondLine;
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasCustomModelData()) {
               String var10000 = Config.HOLOGRAM_FIRST_LINE.toString();
               String var10002 = this.getMostCommonItem(this.inventory).getType().name();
               firstLine = var10000.replace("{item}", var10002 + "{CustomModelData:" + itemStack.getItemMeta().getCustomModelData() + "}").replace("{price}", Utils.formatPrice(this.price));
               var10000 = Config.HOLOGRAM_SECOND_LINE.toString();
               var10002 = this.getMostCommonItem(this.inventory).getType().name();
               secondLine = var10000.replace("{item}", var10002 + "{CustomModelData:" + itemStack.getItemMeta().getCustomModelData() + "}").replace("{price}", Utils.formatPrice(this.price));
               DHAPI.setHologramLine(hologram, 0, firstLine);
               DHAPI.setHologramLine(hologram, 1, secondLine);
            } else {
               firstLine = Config.HOLOGRAM_FIRST_LINE.toString().replace("{item}", this.getMostCommonItem(this.inventory).getType().name()).replace("{price}", Utils.formatPrice(this.price));
               secondLine = Config.HOLOGRAM_SECOND_LINE.toString().replace("{item}", this.getMostCommonItem(this.inventory).getType().name()).replace("{price}", Utils.formatPrice(this.price));
               DHAPI.setHologramLine(hologram, 0, firstLine);
               DHAPI.setHologramLine(hologram, 1, secondLine);
            }
         } else {
            String firstLine = Config.HOLOGRAM_FIRST_LINE.toString().replace("{item}", this.getMostCommonItem(this.inventory).getType().name()).replace("{price}", Utils.formatPrice(this.price));
            firstLine = Config.HOLOGRAM_SECOND_LINE.toString().replace("{item}", this.getMostCommonItem(this.inventory).getType().name()).replace("{price}", Utils.formatPrice(this.price));
            List<String> lines = Arrays.asList(firstLine, firstLine);
            DHAPI.createHologram(this.key, location, lines);
         }

      }
   }

   public void removeItem(int slot) {
      this.inventory.setItem(slot, new ItemStack(Material.AIR));
   }

   public void removeHolo(String key) {
      if (PlayerShop.getInstance().config().getBoolean("Hologram.Enable")) {
         DHAPI.removeHologram(key);
      }
   }
}
