package com.badbones69.crazyauctions.api;

import com.badbones69.crazyauctions.Methods;
import com.badbones69.crazyauctions.api.enums.Files;
import com.badbones69.crazyauctions.api.enums.ShopType;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CrazyManager {
   private boolean sellingEnabled;
   private boolean biddingEnabled;

   public void load() {
      this.sellingEnabled = Files.config.getConfiguration().getBoolean("Settings.Feature-Toggle.Selling", true);
      this.biddingEnabled = Files.config.getConfiguration().getBoolean("Settings.Feature-Toggle.Bidding", true);
   }

   public void unload() {
      Files.data.save();
   }

   public boolean isSellingEnabled() {
      return this.sellingEnabled;
   }

   public boolean isBiddingEnabled() {
      return this.biddingEnabled;
   }

   public ArrayList<ItemStack> getItems(Player player, ShopType type) {
      FileConfiguration data = Files.data.getConfiguration();
      ArrayList<ItemStack> items = new ArrayList();
      if (data.contains("Items")) {
         Iterator var5 = data.getConfigurationSection("Items").getKeys(false).iterator();

         while(var5.hasNext()) {
            String i = (String)var5.next();
            if (data.getString("Items." + i + ".Seller").equalsIgnoreCase(player.getUniqueId().toString())) {
               if (data.getBoolean("Items." + i + ".Biddable")) {
                  if (type == ShopType.BID) {
                     items.add(Methods.fromBase64(data.getString("Items." + i + ".Item")));
                  }
               } else if (type == ShopType.SELL) {
                  items.add(Methods.fromBase64(data.getString("Items." + i + ".Item")));
               }
            }
         }
      }

      return items;
   }
}
