package emanondev.itemedit;

import emanondev.itemedit.compability.Hooks;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class Util {
   private Util() {
      throw new UnsupportedOperationException();
   }

   public static void sendMessage(@NotNull CommandSender sender, String message) {
      if (message != null && !message.isEmpty()) {
         sender.sendMessage(message);
      }
   }

   public static void sendMessage(@NotNull CommandSender sender, BaseComponent... message) {
      if (sender instanceof Player) {
         ((Player)sender).spigot().sendMessage(message);
      } else {
         sender.sendMessage(BaseComponent.toPlainText(message));
      }

   }

   public static void logToFile(String message) {
      try {
         File dataFolder = ItemEdit.get().getDataFolder();
         if (!dataFolder.exists()) {
            dataFolder.mkdir();
         }

         Date date = new Date();
         File saveTo = new File(ItemEdit.get().getDataFolder(), "logs" + File.separatorChar + (new SimpleDateFormat(ItemEdit.get().getConfig().loadMessage("log.file-format", "yyyy.MM.dd", false), Locale.ENGLISH)).format(date) + ".log");
         if (!saveTo.getParentFile().exists()) {
            saveTo.getParentFile().mkdirs();
         }

         if (!saveTo.exists()) {
            saveTo.createNewFile();
         }

         FileWriter fw = new FileWriter(saveTo, true);
         PrintWriter pw = new PrintWriter(fw);
         pw.println((new SimpleDateFormat(ItemEdit.get().getConfig().loadMessage("log.log-date-format", "[dd.MM.yyyy HH:mm:ss]", false), Locale.ENGLISH)).format(date) + message);
         pw.flush();
         pw.close();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public static boolean hasBannedWords(@NotNull Player user, String text) {
      if (user.hasPermission("itemedit.bypass.censure")) {
         return false;
      } else {
         String message = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', text.toLowerCase(Locale.ENGLISH)));
         Iterator var3 = ItemEdit.get().getConfig().getStringList("blocked.regex").iterator();

         String bannedWord;
         do {
            if (!var3.hasNext()) {
               var3 = ItemEdit.get().getConfig().getStringList("blocked.words").iterator();

               do {
                  if (!var3.hasNext()) {
                     return false;
                  }

                  bannedWord = (String)var3.next();
               } while(!message.contains(bannedWord.toLowerCase(Locale.ENGLISH)));

               if (ItemEdit.get().getConfig().getBoolean("blocked.log.console", true)) {
                  sendMessage(Bukkit.getConsoleSender(), (String)("user: §e" + user.getName() + "§r attempt to write '" + text + "'§r (stripped by colors and lowcased) was blocked by word: §e" + bannedWord.toLowerCase(Locale.ENGLISH)));
               }

               if (ItemEdit.get().getConfig().getBoolean("blocked.log.file", true)) {
                  logToFile("user: '" + user.getName() + "' attempt to write '" + text + "' (stripped by colors and lowcased to '" + message + "') was blocked by word: '" + bannedWord.toLowerCase(Locale.ENGLISH) + "'");
               }

               sendMessage(user, (String)ItemEdit.get().getLanguageConfig(user).loadMessage("blocked-by-censure", "", (Player)null, true));
               return true;
            }

            bannedWord = (String)var3.next();
         } while(!Pattern.compile(bannedWord).matcher(message).find());

         if (ItemEdit.get().getConfig().getBoolean("blocked.log.console", true)) {
            sendMessage(Bukkit.getConsoleSender(), (String)("user: §e" + user.getName() + "§r attempt to write '" + text + "'§r (stripped by colors and lowcased) was blocked by regex: §e" + bannedWord));
         }

         if (ItemEdit.get().getConfig().getBoolean("blocked.log.file", true)) {
            logToFile("user: '" + user.getName() + "' attempt to write '" + text + "' (stripped by colors and lowcased to '" + message + "') was blocked by regex: '" + bannedWord + "'");
         }

         sendMessage(user, (String)ItemEdit.get().getLanguageConfig(user).loadMessage("blocked-by-censure", "", (Player)null, true));
         return true;
      }
   }

   public static String formatText(CommandSender sender, String text, String basePermission) {
      if (hasMiniMessageAPI() && sender.hasPermission(basePermission + ".minimessage")) {
         text = Hooks.getMiniMessageUtil().fromMiniToText(text);
      }

      text = ChatColor.translateAlternateColorCodes('&', text);
      if (basePermission != null) {
         ChatColor[] var3 = ChatColor.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ChatColor style = var3[var5];
            if (style.isFormat()) {
               if (!sender.hasPermission(basePermission + ".format." + style.name().toLowerCase(Locale.ENGLISH))) {
                  text = text.replaceAll(style.toString(), "");
               }
            } else if (!sender.hasPermission(basePermission + ".color." + style.name().toLowerCase(Locale.ENGLISH))) {
               text = text.replaceAll(style.toString(), "");
            }
         }

         if (sender.hasPermission(basePermission + ".color.hexa")) {
            try {
               for(int from = 0; text.indexOf("&#", from) >= 0; text = text.replace(text.substring(from - 1, from + 7), net.md_5.bungee.api.ChatColor.of(text.substring(from, from + 7)).toString())) {
                  from = text.indexOf("&#", from) + 1;
               }
            } catch (Throwable var7) {
            }
         }
      }

      return text;
   }

   public static boolean isAllowedRenameItem(CommandSender sender, Material type) {
      if (sender.hasPermission("itemedit.bypass.rename_type_restriction")) {
         return true;
      } else {
         List<String> values = ItemEdit.get().getConfig().getStringList("blocked.type-blocked-rename");
         if (values.isEmpty()) {
            return true;
         } else {
            String id = type.name();
            Iterator var4 = values.iterator();

            String name;
            do {
               if (!var4.hasNext()) {
                  return true;
               }

               name = (String)var4.next();
            } while(!id.equalsIgnoreCase(name));

            sendMessage(sender, ItemEdit.get().getLanguageConfig(sender).loadMessage("blocked-by-type-restriction", "", (Player)null, true));
            return false;
         }
      }
   }

   public static ItemStack getDyeItemFromColor(DyeColor color) {
      try {
         return new ItemStack(Material.valueOf(color.name() + "_DYE"));
      } catch (Exception var2) {
         return new ItemStack(Material.valueOf("INK_SACK"), 1, (short)0, getDataByColor(color));
      }
   }

   public static ItemStack getWoolItemFromColor(DyeColor color) {
      try {
         return new ItemStack(Material.valueOf(color.name() + "_WOOL"));
      } catch (Exception var2) {
         return new ItemStack(Material.valueOf("WOOL"), 1, (short)0, getDataByColor(color));
      }
   }

   public static Material getBannerItemFromColor(DyeColor color) {
      try {
         return Material.valueOf(color.name() + "_BANNER");
      } catch (Exception var2) {
         return Material.valueOf("BANNER");
      }
   }

   public static DyeColor getColorFromBanner(ItemStack banner) {
      try {
         String name = banner.getType().name();
         return DyeColor.valueOf(name.substring(0, name.length() - 7));
      } catch (Exception var2) {
         return getColorByData((byte)banner.getDurability());
      }
   }

   public static DyeColor getColorByData(byte color) {
      switch(color) {
      case 0:
         return DyeColor.BLACK;
      case 1:
         return DyeColor.RED;
      case 2:
         return DyeColor.GREEN;
      case 3:
         return DyeColor.BROWN;
      case 4:
         return DyeColor.BLUE;
      case 5:
         return DyeColor.PURPLE;
      case 6:
         return DyeColor.CYAN;
      case 7:
         return DyeColor.LIGHT_GRAY;
      case 8:
         return DyeColor.GRAY;
      case 9:
         return DyeColor.PINK;
      case 10:
         return DyeColor.LIME;
      case 11:
         return DyeColor.YELLOW;
      case 12:
         return DyeColor.LIGHT_BLUE;
      case 13:
         return DyeColor.MAGENTA;
      case 14:
         return DyeColor.ORANGE;
      case 15:
         return DyeColor.WHITE;
      default:
         throw new IllegalStateException();
      }
   }

   public static Byte getDataByColor(DyeColor color) {
      String var1 = color.name();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1955522002:
         if (var1.equals("ORANGE")) {
            var2 = 9;
         }
         break;
      case -1923613764:
         if (var1.equals("PURPLE")) {
            var2 = 11;
         }
         break;
      case -1848981747:
         if (var1.equals("SILVER")) {
            var2 = 13;
         }
         break;
      case -1680910220:
         if (var1.equals("YELLOW")) {
            var2 = 16;
         }
         break;
      case 81009:
         if (var1.equals("RED")) {
            var2 = 12;
         }
         break;
      case 2041946:
         if (var1.equals("BLUE")) {
            var2 = 1;
         }
         break;
      case 2083619:
         if (var1.equals("CYAN")) {
            var2 = 3;
         }
         break;
      case 2196067:
         if (var1.equals("GRAY")) {
            var2 = 4;
         }
         break;
      case 2336725:
         if (var1.equals("LIME")) {
            var2 = 7;
         }
         break;
      case 2455926:
         if (var1.equals("PINK")) {
            var2 = 10;
         }
         break;
      case 63281119:
         if (var1.equals("BLACK")) {
            var2 = 0;
         }
         break;
      case 63473942:
         if (var1.equals("BROWN")) {
            var2 = 2;
         }
         break;
      case 68081379:
         if (var1.equals("GREEN")) {
            var2 = 5;
         }
         break;
      case 82564105:
         if (var1.equals("WHITE")) {
            var2 = 15;
         }
         break;
      case 305548803:
         if (var1.equals("LIGHT_BLUE")) {
            var2 = 6;
         }
         break;
      case 305702924:
         if (var1.equals("LIGHT_GRAY")) {
            var2 = 14;
         }
         break;
      case 1546904713:
         if (var1.equals("MAGENTA")) {
            var2 = 8;
         }
      }

      switch(var2) {
      case 0:
         return 0;
      case 1:
         return 4;
      case 2:
         return 3;
      case 3:
         return 6;
      case 4:
         return 8;
      case 5:
         return 2;
      case 6:
         return 12;
      case 7:
         return 10;
      case 8:
         return 13;
      case 9:
         return 14;
      case 10:
         return 9;
      case 11:
         return 5;
      case 12:
         return 1;
      case 13:
      case 14:
         return 7;
      case 15:
         return 15;
      case 16:
         return 11;
      default:
         throw new IllegalStateException();
      }
   }

   public static boolean isAllowedChangeLore(CommandSender sender, Material type) {
      if (sender.hasPermission("itemedit.bypass.lore_type_restriction")) {
         return true;
      } else {
         List<String> values = ItemEdit.get().getConfig().getStringList("blocked.type-blocked-lore");
         if (values.isEmpty()) {
            return true;
         } else {
            String id = type.name();
            Iterator var4 = values.iterator();

            String name;
            do {
               if (!var4.hasNext()) {
                  return true;
               }

               name = (String)var4.next();
            } while(!id.equalsIgnoreCase(name));

            sendMessage(sender, ItemEdit.get().getLanguageConfig(sender).loadMessage("blocked-by-type-restriction-lore", "", (Player)null, true));
            return false;
         }
      }
   }

   public static boolean hasMiniMessageAPI() {
      return Hooks.hasMiniMessage();
   }
}
