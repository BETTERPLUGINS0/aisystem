package com.badbones69.crazyauctions;

import com.badbones69.crazyauctions.api.enums.Files;
import com.badbones69.crazyauctions.api.enums.Messages;
import com.badbones69.crazyauctions.api.events.AuctionExpireEvent;
import com.badbones69.crazyauctions.api.events.AuctionWinBidEvent;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Methods {
   private static final CrazyAuctions plugin = CrazyAuctions.get();
   private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");
   private static final Pattern UUID_PATTERN = Pattern.compile("[a-f0-9]{8}(?:-[a-f0-9]{4}){4}[a-f0-9]{8}");

   public static String color(String message) {
      Matcher matcher = HEX_PATTERN.matcher(message);
      StringBuilder buffer = new StringBuilder();

      while(matcher.find()) {
         matcher.appendReplacement(buffer, ChatColor.of(matcher.group()).toString());
      }

      return org.bukkit.ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
   }

   public static String getPrefix() {
      return color(Files.config.getConfiguration().getString("Settings.Prefix", ""));
   }

   public static String getPrefix(String msg) {
      String var10000 = Files.config.getConfiguration().getString("Settings.Prefix", "");
      return color(var10000 + msg);
   }

   public static ItemStack getItemInHand(Player player) {
      return player.getInventory().getItemInMainHand();
   }

   public static void setItemInHand(Player player, ItemStack item) {
      player.getInventory().setItemInMainHand(item);
   }

   public static boolean isInt(String s) {
      try {
         Integer.parseInt(s);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static boolean isLong(String s) {
      try {
         Long.parseLong(s);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static Player getPlayer(String uuid) {
      try {
         return plugin.getServer().getPlayer(UUID.fromString(uuid));
      } catch (Exception var2) {
         return null;
      }
   }

   public static String toBase64(ItemStack itemStack) {
      return Base64.getEncoder().encodeToString(itemStack.serializeAsBytes());
   }

   @NotNull
   public static ItemStack fromBase64(String base64) {
      return ItemStack.deserializeBytes(Base64.getDecoder().decode(base64));
   }

   public static OfflinePlayer getOfflinePlayer(String uuid) {
      return plugin.getServer().getOfflinePlayer(UUID.fromString(uuid));
   }

   public static boolean isUUID(String uuid) {
      return UUID_PATTERN.matcher(uuid).find();
   }

   public static boolean isOnline(String uuid) {
      Iterator var1 = plugin.getServer().getOnlinePlayers().iterator();

      Player player;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         player = (Player)var1.next();
      } while(!player.getUniqueId().toString().equals(uuid));

      return true;
   }

   public static boolean isOnline(String name, CommandSender p) {
      Iterator var2 = plugin.getServer().getOnlinePlayers().iterator();

      Player player;
      do {
         if (!var2.hasNext()) {
            p.sendMessage(Messages.NOT_ONLINE.getMessage(p));
            return false;
         }

         player = (Player)var2.next();
      } while(!player.getName().equalsIgnoreCase(name));

      return true;
   }

   public static boolean hasPermission(Player player, String perm) {
      if (!player.hasPermission("crazyauctions." + perm)) {
         player.sendMessage(Messages.NO_PERMISSION.getMessage(player));
         return false;
      } else {
         return true;
      }
   }

   public static boolean hasPermission(CommandSender sender, String perm) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         if (!player.hasPermission("crazyauctions." + perm)) {
            player.sendMessage(Messages.NO_PERMISSION.getMessage(player));
            return false;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   public static List<ItemStack> getPage(List<ItemStack> list, Integer page) {
      List<ItemStack> items = new ArrayList();
      if (page <= 0) {
         page = 1;
      }

      int max = 45;
      int index = page * max - max;

      int endIndex;
      for(endIndex = index >= list.size() ? list.size() - 1 : index + max; index < endIndex; ++index) {
         if (index < list.size()) {
            items.add((ItemStack)list.get(index));
         }
      }

      while(items.size() == 0 && page > 0) {
         index = page * max - max;

         for(endIndex = index >= list.size() ? list.size() - 1 : index + max; index < endIndex; ++index) {
            if (index < list.size()) {
               items.add((ItemStack)list.get(index));
            }
         }

         page = page - 1;
      }

      return items;
   }

   public static List<Integer> getPageInts(List<Integer> list, Integer page) {
      List<Integer> items = new ArrayList();
      if (page <= 0) {
         page = 1;
      }

      int max = 45;
      int index = page * max - max;

      int endIndex;
      for(endIndex = index >= list.size() ? list.size() - 1 : index + max; index < endIndex; ++index) {
         if (index < list.size()) {
            items.add((Integer)list.get(index));
         }
      }

      while(items.isEmpty() && page > 0) {
         index = page * max - max;

         for(endIndex = index >= list.size() ? list.size() - 1 : index + max; index < endIndex; ++index) {
            if (index < list.size()) {
               items.add((Integer)list.get(index));
            }
         }

         page = page - 1;
      }

      return items;
   }

   public static int getMaxPage(List<ItemStack> list) {
      int maxPage = 1;

      for(int amount = list.size(); amount > 45; ++maxPage) {
         amount -= 45;
      }

      return maxPage;
   }

   public static String convertToTime(long time) {
      Calendar C = Calendar.getInstance();
      Calendar cal = Calendar.getInstance();
      cal.setTimeInMillis(time);
      int total = (int)(cal.getTimeInMillis() / 1000L) - (int)(C.getTimeInMillis() / 1000L);
      int D = 0;
      int H = 0;
      int M = 0;

      byte S;
      for(S = 0; total > 86400; ++D) {
         total -= 86400;
      }

      while(total > 3600) {
         total -= 3600;
         ++H;
      }

      while(total > 60) {
         total -= 60;
         ++M;
      }

      int S = S + total;
      return D + "d " + H + "h " + M + "m " + S + "s ";
   }

   public static long convertToMill(String time) {
      Calendar cal = Calendar.getInstance();
      String[] var2 = time.split(" ");
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String i = var2[var4];
         if (i.contains("D") || i.contains("d")) {
            cal.add(5, Integer.parseInt(i.replace("D", "").replace("d", "")));
         }

         if (i.contains("H") || i.contains("h")) {
            cal.add(10, Integer.parseInt(i.replace("H", "").replace("h", "")));
         }

         if (i.contains("M") || i.contains("m")) {
            cal.add(12, Integer.parseInt(i.replace("M", "").replace("m", "")));
         }

         if (i.contains("S") || i.contains("s")) {
            cal.add(13, Integer.parseInt(i.replace("S", "").replace("s", "")));
         }
      }

      return cal.getTimeInMillis();
   }

   public static boolean isInvFull(Player player) {
      return player.getInventory().firstEmpty() == -1;
   }

   public static void updateAuction() {
      FileConfiguration data = Files.data.getConfiguration();
      Calendar cal = Calendar.getInstance();
      Calendar expireTime = Calendar.getInstance();
      Calendar fullExpireTime = Calendar.getInstance();
      boolean shouldSave = false;
      Iterator var5;
      String i;
      if (data.contains("OutOfTime/Cancelled")) {
         var5 = data.getConfigurationSection("OutOfTime/Cancelled").getKeys(false).iterator();

         while(var5.hasNext()) {
            i = (String)var5.next();
            fullExpireTime.setTimeInMillis(data.getLong("OutOfTime/Cancelled." + i + ".Full-Time"));
            if (cal.after(fullExpireTime)) {
               data.set("OutOfTime/Cancelled." + i, (Object)null);
               shouldSave = true;
            }
         }
      }

      if (data.contains("Items")) {
         var5 = data.getConfigurationSection("Items").getKeys(false).iterator();

         label71:
         while(true) {
            do {
               if (!var5.hasNext()) {
                  break label71;
               }

               i = (String)var5.next();
               expireTime.setTimeInMillis(data.getLong("Items." + i + ".Time-Till-Expire"));
               fullExpireTime.setTimeInMillis(data.getLong("Items." + i + ".Full-Time"));
            } while(!cal.after(expireTime));

            int num;
            for(num = 1; data.contains("OutOfTime/Cancelled." + num); ++num) {
            }

            String winner;
            if (data.getBoolean("Items." + i + ".Biddable") && !data.getString("Items." + i + ".TopBidder").equalsIgnoreCase("None") && plugin.getSupport().getMoney(getPlayer(data.getString("Items." + i + ".TopBidder"))) >= (long)data.getInt("Items." + i + ".Price")) {
               winner = data.getString("Items." + i + ".TopBidder");
               String seller = data.getString("Items." + i + ".Seller");
               long price = data.getLong("Items." + i + ".Price");
               plugin.getSupport().addMoney(getOfflinePlayer(seller), price);
               plugin.getSupport().removeMoney(getOfflinePlayer(winner), price);
               HashMap<String, String> placeholders = new HashMap();
               placeholders.put("%Price%", getPrice(i, false));
               placeholders.put("%price%", getPrice(i, false));
               placeholders.put("%Player%", winner);
               placeholders.put("%player%", winner);
               Player player;
               if (isOnline(winner) && getPlayer(winner) != null) {
                  player = getPlayer(winner);
                  plugin.getServer().getPluginManager().callEvent(new AuctionWinBidEvent(player, fromBase64(data.getString("Items." + i + ".Item")), price));
                  if (player != null) {
                     player.sendMessage(Messages.WIN_BIDDING.getMessage(player, placeholders));
                  }
               }

               if (isOnline(seller) && getPlayer(seller) != null) {
                  player = getPlayer(seller);
                  if (player != null) {
                     player.sendMessage(Messages.SOMEONE_WON_PLAYERS_BID.getMessage(player, placeholders));
                  }
               }

               data.set("OutOfTime/Cancelled." + num + ".Seller", winner);
               data.set("OutOfTime/Cancelled." + num + ".Full-Time", fullExpireTime.getTimeInMillis());
               data.set("OutOfTime/Cancelled." + num + ".StoreID", data.getInt("Items." + i + ".StoreID"));
               data.set("OutOfTime/Cancelled." + num + ".Item", data.getString("Items." + i + ".Item"));
            } else {
               winner = data.getString("Items." + i + ".Seller");
               Player player = getPlayer(winner);
               if (isOnline(winner) && player != null) {
                  player.sendMessage(Messages.ITEM_HAS_EXPIRED.getMessage(player));
               }

               AuctionExpireEvent event = new AuctionExpireEvent(player, fromBase64(data.getString("Items." + i + ".Item")));
               plugin.getServer().getPluginManager().callEvent(event);
               data.set("OutOfTime/Cancelled." + num + ".Seller", data.getString("Items." + i + ".Seller"));
               data.set("OutOfTime/Cancelled." + num + ".Full-Time", fullExpireTime.getTimeInMillis());
               data.set("OutOfTime/Cancelled." + num + ".StoreID", data.getInt("Items." + i + ".StoreID"));
               data.set("OutOfTime/Cancelled." + num + ".Item", data.getString("Items." + i + ".Item"));
            }

            data.set("Items." + i, (Object)null);
            shouldSave = true;
         }
      }

      if (shouldSave) {
         Files.data.save();
      }

   }

   public static String getPrice(String ID, Boolean Expired) {
      long price = 0L;
      FileConfiguration configuration = Files.data.getConfiguration();
      if (Expired) {
         if (configuration.contains("OutOfTime/Cancelled." + ID + ".Price")) {
            price = configuration.getLong("OutOfTime/Cancelled." + ID + ".Price");
         }
      } else if (configuration.contains("Items." + ID + ".Price")) {
         price = configuration.getLong("Items." + ID + ".Price");
      }

      return String.valueOf(price);
   }
}
