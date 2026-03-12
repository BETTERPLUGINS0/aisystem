package emanondev.itemedit;

import emanondev.itemedit.compability.Hooks;
import emanondev.itemedit.utility.ItemUtils;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class UtilsString {
   private UtilsString() {
      throw new UnsupportedOperationException();
   }

   public static void updateDescription(@Nullable ItemStack item, @Nullable List<String> desc, @Nullable Player p, boolean color, String... holders) {
      if (item != null) {
         String title;
         ArrayList lore;
         if (desc != null && !desc.isEmpty()) {
            if (desc.size() == 1) {
               if (desc.get(0) != null) {
                  if (!((String)desc.get(0)).startsWith(ChatColor.RESET + "")) {
                     title = ChatColor.RESET + (String)desc.get(0);
                  } else {
                     title = (String)desc.get(0);
                  }
               } else {
                  title = null;
               }

               lore = null;
            } else {
               if (!((String)desc.get(0)).startsWith(ChatColor.RESET + "")) {
                  title = ChatColor.RESET + (String)desc.get(0);
               } else {
                  title = (String)desc.get(0);
               }

               lore = new ArrayList();

               for(int i = 1; i < desc.size(); ++i) {
                  if (desc.get(i) != null) {
                     if (!((String)desc.get(i)).startsWith(ChatColor.RESET + "")) {
                        lore.add(ChatColor.RESET + (String)desc.get(i));
                     } else {
                        lore.add((String)desc.get(i));
                     }
                  } else {
                     lore.add("");
                  }
               }
            }
         } else {
            title = " ";
            lore = null;
         }

         title = fix(title, p, color, holders);
         lore = fix((List)lore, p, color, holders);
         ItemMeta meta = ItemUtils.getMeta(item);
         meta.setDisplayName(title);
         meta.setLore(lore);
         item.setItemMeta(meta);
      }
   }

   @Contract("!null, _, _, _ -> !null")
   @Nullable
   public static ArrayList<String> fix(@Nullable List<String> list, @Nullable Player player, boolean color, String... holders) {
      if (list == null) {
         return null;
      } else {
         ArrayList<String> newList = new ArrayList();
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            String line = (String)var5.next();
            newList.add(fix(line, player, color, holders));
         }

         return newList;
      }
   }

   public static ItemStack setDescription(@Nullable ItemStack item, @Nullable List<String> description, @Nullable Player player, boolean color, String... holders) {
      if (ItemUtils.isAirOrNull(item)) {
         return null;
      } else {
         ItemStack itemCopy = new ItemStack(item);
         updateDescription(itemCopy, description, player, color, holders);
         return itemCopy;
      }
   }

   @Contract("!null, _, _, _ -> !null")
   public static String fix(@Nullable String text, @Nullable Player player, boolean color, String... holders) {
      if (text == null) {
         return null;
      } else if (holders != null && holders.length % 2 != 0) {
         throw new IllegalArgumentException("holder without replacer");
      } else {
         if (holders != null && holders.length > 0) {
            for(int i = 0; i < holders.length; i += 2) {
               text = text.replace(holders[i], holders[i + 1]);
            }
         }

         if (player != null && Hooks.isPAPIEnabled()) {
            text = PlaceholderAPI.setPlaceholders(player, text);
         }

         if (Hooks.hasMiniMessage()) {
            text = Hooks.getMiniMessageUtil().fromMiniToText(text);
         }

         if (color) {
            text = ChatColor.translateAlternateColorCodes('&', text);
         }

         return text;
      }
   }

   @Contract("!null -> !null")
   @Nullable
   public static String revertColors(@Nullable String text) {
      return text == null ? null : text.replace("§", "&");
   }

   @Contract("!null -> !null")
   @Nullable
   public static String clearColors(@Nullable String text) {
      return text == null ? null : ChatColor.stripColor(text);
   }

   @NotNull
   public static String formatNumber(double value, int decimals, boolean optional) {
      DecimalFormat df = new DecimalFormat("0");
      df.setMaximumFractionDigits(decimals);
      df.setMinimumFractionDigits(optional ? 0 : decimals);
      return df.format(value);
   }
}
