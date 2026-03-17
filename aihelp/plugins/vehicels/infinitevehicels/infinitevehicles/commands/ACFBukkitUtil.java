package me.PM2.infinitevehicles.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import me.PM2.infinitevehicles.locales.MessageKeyProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ACFBukkitUtil {
   public static String formatLocation(Location loc) {
      return var0 == null ? null : var0.getWorld().getName() + ":" + var0.getBlockX() + "," + var0.getBlockY() + "," + var0.getBlockZ();
   }

   public static String color(String message) {
      return ChatColor.translateAlternateColorCodes('&', var0);
   }

   /** @deprecated */
   @Deprecated
   public static void sendMsg(CommandSender player, String message) {
      var1 = color(var1);
      String[] var2 = ACFPatterns.NEWLINE.split(var1);
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         var0.sendMessage(var5);
      }

   }

   public static Location stringToLocation(String storedLoc) {
      return stringToLocation(var0, (World)null);
   }

   public static Location stringToLocation(String storedLoc, World forcedWorld) {
      if (var0 == null) {
         return null;
      } else {
         String[] var2 = ACFPatterns.COLON.split(var0);
         double var5;
         double var7;
         double var9;
         if (var2.length < 4 && (var2.length != 3 || var1 == null)) {
            if (var2.length == 2) {
               String[] var12 = ACFPatterns.COMMA.split(var2[1]);
               if (var12.length == 3) {
                  String var13 = var1 != null ? var1.getName() : var2[0];
                  var5 = Double.parseDouble(var12[0]);
                  var7 = Double.parseDouble(var12[1]);
                  var9 = Double.parseDouble(var12[2]);
                  return new Location(Bukkit.getWorld(var13), var5, var7, var9);
               }
            }

            return null;
         } else {
            String var3 = var1 != null ? var1.getName() : var2[0];
            int var4 = var2.length == 3 ? 0 : 1;
            var5 = Double.parseDouble(var2[var4]);
            var7 = Double.parseDouble(var2[var4 + 1]);
            var9 = Double.parseDouble(var2[var4 + 2]);
            Location var11 = new Location(Bukkit.getWorld(var3), var5, var7, var9);
            if (var2.length >= 6) {
               var11.setPitch(Float.parseFloat(var2[4]));
               var11.setYaw(Float.parseFloat(var2[5]));
            }

            return var11;
         }
      }
   }

   public static String fullLocationToString(Location loc) {
      return var0 == null ? null : (new StringBuilder(64)).append(var0.getWorld().getName()).append(':').append(ACFUtil.precision(var0.getX(), 4)).append(':').append(ACFUtil.precision(var0.getY(), 4)).append(':').append(ACFUtil.precision(var0.getZ(), 4)).append(':').append(ACFUtil.precision((double)var0.getPitch(), 4)).append(':').append(ACFUtil.precision((double)var0.getYaw(), 4)).toString();
   }

   public static String fullBlockLocationToString(Location loc) {
      return var0 == null ? null : (new StringBuilder(64)).append(var0.getWorld().getName()).append(':').append(var0.getBlockX()).append(':').append(var0.getBlockY()).append(':').append(var0.getBlockZ()).append(':').append(ACFUtil.precision((double)var0.getPitch(), 4)).append(':').append(ACFUtil.precision((double)var0.getYaw(), 4)).toString();
   }

   public static String blockLocationToString(Location loc) {
      return var0 == null ? null : (new StringBuilder(32)).append(var0.getWorld().getName()).append(':').append(var0.getBlockX()).append(':').append(var0.getBlockY()).append(':').append(var0.getBlockZ()).toString();
   }

   public static double distance(@NotNull Entity e1, @NotNull Entity e2) {
      return distance(var0.getLocation(), var1.getLocation());
   }

   public static double distance2d(@NotNull Entity e1, @NotNull Entity e2) {
      return distance2d(var0.getLocation(), var1.getLocation());
   }

   public static double distance2d(@NotNull Location loc1, @NotNull Location loc2) {
      var0 = var0.clone();
      var0.setY(var1.getY());
      return distance(var0, var1);
   }

   public static double distance(@NotNull Location loc1, @NotNull Location loc2) {
      return var0.getWorld() != var1.getWorld() ? 0.0D : var0.distance(var1);
   }

   public static Location getTargetLoc(Player player) {
      return getTargetLoc(var0, 128);
   }

   public static Location getTargetLoc(Player player, int maxDist) {
      return getTargetLoc(var0, var1, 1.5D);
   }

   public static Location getTargetLoc(Player player, int maxDist, double addY) {
      try {
         Location var4 = var0.getTargetBlock((Set)null, var1).getLocation();
         var4.setY(var4.getY() + var2);
         return var4;
      } catch (Exception var5) {
         return null;
      }
   }

   public static Location getRandLoc(Location loc, int radius) {
      return getRandLoc(var0, var1, var1, var1);
   }

   public static Location getRandLoc(Location loc, int xzRadius, int yRadius) {
      return getRandLoc(var0, var1, var2, var1);
   }

   @NotNull
   public static Location getRandLoc(Location loc, int xRadius, int yRadius, int zRadius) {
      Location var4 = var0.clone();
      var4.setX(ACFUtil.rand(var0.getX() - (double)var1, var0.getX() + (double)var1));
      var4.setY(ACFUtil.rand(var0.getY() - (double)var2, var0.getY() + (double)var2));
      var4.setZ(ACFUtil.rand(var0.getZ() - (double)var3, var0.getZ() + (double)var3));
      return var4;
   }

   public static String removeColors(String msg) {
      return ChatColor.stripColor(color(var0));
   }

   public static String replaceChatString(String message, String replace, String with) {
      return replaceChatString(var0, Pattern.compile(Pattern.quote(var1), 2), var2);
   }

   public static String replaceChatString(String message, Pattern replace, String with) {
      String[] var3 = var1.split(var0 + "1");
      if (var3.length < 2) {
         return var1.matcher(var0).replaceAll(var2);
      } else {
         var0 = var3[0];

         for(int var4 = 1; var4 < var3.length; ++var4) {
            String var5 = ChatColor.getLastColors(var0);
            var0 = var0 + var2 + var5 + var3[var4];
         }

         return var0.substring(0, var0.length() - 1);
      }
   }

   public static boolean isWithinDistance(@NotNull Player p1, @NotNull Player p2, int dist) {
      return isWithinDistance(var0.getLocation(), var1.getLocation(), var2);
   }

   public static boolean isWithinDistance(@NotNull Location loc1, @NotNull Location loc2, int dist) {
      return var0.getWorld() == var1.getWorld() && var0.distance(var1) <= (double)var2;
   }

   /** @deprecated */
   public static Player findPlayerSmart(CommandSender requester, String search) {
      CommandManager var2 = CommandManager.getCurrentCommandManager();
      if (var2 != null) {
         return findPlayerSmart(var2.getCommandIssuer(var0), var1);
      } else {
         throw new IllegalStateException("You may not use the ACFBukkitUtil#findPlayerSmart(CommandSender) async to the command execution.");
      }
   }

   public static Player findPlayerSmart(CommandIssuer issuer, String search) {
      CommandSender var2 = (CommandSender)var0.getIssuer();
      if (var1 == null) {
         return null;
      } else {
         String var3 = ACFUtil.replace(var1, ":confirm", "");
         List var4 = Bukkit.getServer().matchPlayer(var3);
         ArrayList var5 = new ArrayList();
         findMatches(var1, var2, var4, var5);
         if (var4.size() <= 1 && var5.size() <= 1) {
            if (var4.isEmpty()) {
               if (!var0.getManager().isValidName(var3)) {
                  var0.sendError((MessageKeyProvider)MinecraftMessageKeys.IS_NOT_A_VALID_NAME, "{name}", var3);
                  return null;
               } else {
                  Player var7 = (Player)ACFUtil.getFirstElement(var5);
                  if (var7 == null) {
                     var0.sendError((MessageKeyProvider)MinecraftMessageKeys.NO_PLAYER_FOUND_SERVER, "{search}", var3);
                     return null;
                  } else {
                     var0.sendInfo((MessageKeyProvider)MinecraftMessageKeys.PLAYER_IS_VANISHED_CONFIRM, "{vanished}", var7.getName());
                     return null;
                  }
               }
            } else {
               return (Player)var4.get(0);
            }
         } else {
            String var6 = (String)var4.stream().map(Player::getName).collect(Collectors.joining(", "));
            var0.sendError((MessageKeyProvider)MinecraftMessageKeys.MULTIPLE_PLAYERS_MATCH, "{search}", var3, "{all}", var6);
            return null;
         }
      }
   }

   private static void findMatches(String search, CommandSender requester, List<Player> matches, List<Player> confirmList) {
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Player var5 = (Player)var4.next();
         if (var1 instanceof Player && !((Player)var1).canSee(var5)) {
            if (var1.hasPermission("acf.seevanish")) {
               if (!var0.endsWith(":confirm")) {
                  var3.add(var5);
                  var4.remove();
               }
            } else {
               var4.remove();
            }
         }
      }

   }

   public static boolean isValidName(@Nullable String name) {
      return var0 != null && !var0.isEmpty() && ACFPatterns.VALID_NAME_PATTERN.matcher(var0).matches();
   }

   static boolean isValidItem(ItemStack item) {
      return var0 != null && var0.getType() != Material.AIR && var0.getAmount() > 0;
   }

   public static Locale stringToLocale(String locale) {
      String[] var1 = ACFPatterns.UNDERSCORE.split(var0);
      return var1.length > 1 ? new Locale(var1[0], var1[1]) : new Locale(var1[0]);
   }
}
