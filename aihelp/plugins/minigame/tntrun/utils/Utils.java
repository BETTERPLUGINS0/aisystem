package tntrun.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;

public class Utils {
   private static Map<String, String> ranks = new HashMap();
   private static Map<String, String> colours = new HashMap();
   private static final Logger log = TNTRun.getInstance().getLogger();

   public static boolean isNumber(String text) {
      try {
         Integer.parseInt(text);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static boolean isDouble(String text) {
      try {
         Double.parseDouble(text);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static String getFormattedTime(int totalSeconds) {
      int hours = totalSeconds / 3600;
      int minutes = totalSeconds % 3600 / 60;
      int seconds = totalSeconds % 60;
      return String.format("%02d:%02d:%02d", hours, minutes, seconds);
   }

   public static int playerCount() {
      int pCount = 0;

      Arena arena;
      for(Iterator var1 = TNTRun.getInstance().amanager.getArenas().iterator(); var1.hasNext(); pCount += arena.getPlayersManager().getPlayersCount()) {
         arena = (Arena)var1.next();
      }

      return pCount;
   }

   public static int spectatorCount() {
      int sCount = 0;

      Arena arena;
      for(Iterator var1 = TNTRun.getInstance().amanager.getArenas().iterator(); var1.hasNext(); sCount += arena.getPlayersManager().getSpectatorsCount()) {
         arena = (Arena)var1.next();
      }

      return sCount;
   }

   public static int pvpPlayerCount() {
      int pCount = 0;

      Arena arena;
      for(Iterator var1 = TNTRun.getInstance().amanager.getPvpArenas().iterator(); var1.hasNext(); pCount += arena.getPlayersManager().getPlayersCount()) {
         arena = (Arena)var1.next();
      }

      return pCount;
   }

   public static int nonPvpPlayerCount() {
      int pCount = 0;

      Arena arena;
      for(Iterator var1 = TNTRun.getInstance().amanager.getNonPvpArenas().iterator(); var1.hasNext(); pCount += arena.getPlayersManager().getPlayersCount()) {
         arena = (Arena)var1.next();
      }

      return pCount;
   }

   public static List<String> getTNTRunPlayers() {
      List<String> names = new ArrayList();
      TNTRun.getInstance().amanager.getArenas().stream().forEach((arena) -> {
         arena.getPlayersManager().getAllParticipantsCopy().stream().forEach((player) -> {
            names.add(player.getName());
         });
      });
      return names;
   }

   public static void displayInfo(CommandSender sender) {
      Messages.sendMessage(sender, "&7============" + Messages.trprefix + "============", false);
      Messages.sendMessage(sender, "&bPlugin Version: &f" + TNTRun.getInstance().getDescription().getVersion(), false);
      Messages.sendMessage(sender, "&bWeb: &f" + TNTRun.getInstance().getSpigotURL(), false);
      Messages.sendMessage(sender, "&bTNTRun_reloaded Author: &fsteve4744", false);
   }

   public static void displayUpdate(Player player) {
      if (player.hasPermission("tntrun.version.check")) {
         String newmsg = " A new version is available!";
         String download = " Click here to download";
         String url = TNTRun.getInstance().getSpigotURL();
         TextComponent tc = getTextComponentPrefix();
         TextComponent message = new TextComponent(" A new version is available!");
         message.setColor(ChatColor.WHITE);
         tc.addExtra(message);
         Content content = new Text(getUpdateMessage().create());
         tc.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new Content[]{content}));
         TextComponent link = buildComponent(" Click here to download", url, url, "", "OPEN_URL");
         link.setColor(ChatColor.AQUA);
         player.spigot().sendMessage(new BaseComponent[]{tc, link});
      }

   }

   private static TextComponent getTextComponentPrefix() {
      TextComponent tc = new TextComponent("[");
      tc.setColor(ChatColor.GRAY);
      TextComponent tc2 = new TextComponent("TNTRun");
      tc2.setColor(ChatColor.GOLD);
      TextComponent tc3 = new TextComponent("]");
      tc3.setColor(ChatColor.GRAY);
      tc.addExtra(tc2);
      tc.addExtra(tc3);
      return tc;
   }

   private static ComponentBuilder getUpdateMessage() {
      ComponentBuilder cb = (new ComponentBuilder("Current version : ")).color(ChatColor.AQUA).append(TNTRun.getInstance().getDescription().getVersion()).color(ChatColor.GOLD);
      cb.append("\nLatest version : ").color(ChatColor.AQUA).append(TNTRun.getInstance().getLatestRelease()).color(ChatColor.GOLD);
      return cb;
   }

   public static void displayHelp(Player player) {
      player.spigot().sendMessage(new BaseComponent[]{getTextComponent("/trsetup setlobby", true), getTextComponent(Messages.setuplobby)});
      player.spigot().sendMessage(new BaseComponent[]{getTextComponent("/trsetup create {arena}", true), getTextComponent(Messages.setupcreate)});
      player.spigot().sendMessage(new BaseComponent[]{getTextComponent("/trsetup setarena {arena}", true), getTextComponent(Messages.setupbounds)});
      player.spigot().sendMessage(new BaseComponent[]{getTextComponent("/trsetup setloselevel {arena}", true), getTextComponent(Messages.setuploselevel)});
      player.spigot().sendMessage(new BaseComponent[]{getTextComponent("/trsetup setspawn {arena}", true), getTextComponent(Messages.setupspawn)});
      player.spigot().sendMessage(new BaseComponent[]{getTextComponent("/trsetup setspectate {arena}", true), getTextComponent(Messages.setupspectate)});
      player.spigot().sendMessage(new BaseComponent[]{getTextComponent("/trsetup finish {arena}", true), getTextComponent(Messages.setupfinish)});
   }

   public static void displayJoinMessage(Player player, String arenaname, String joinMessage) {
      String command = "/tntrun joinorspectate ";
      String border = FormattingCodesParser.parseFormattingCodes(Messages.playerborderinvite);
      String clickAction = "RUN_COMMAND";
      TextComponent jointc = new TextComponent(new BaseComponent[]{TextComponent.fromLegacy(border + "\n")});
      jointc.addExtra(buildComponent(joinMessage, Messages.playerclickinvite.replace("{ARENA}", arenaname), arenaname, "/tntrun joinorspectate ", "RUN_COMMAND"));
      jointc.addExtra(new TextComponent(new BaseComponent[]{TextComponent.fromLegacy("\n" + border)}));
      player.spigot().sendMessage(jointc);
   }

   public static String getTitleCase(String input) {
      String var10000 = input.substring(0, 1).toUpperCase();
      return var10000 + input.substring(1).toLowerCase();
   }

   public static TextComponent getTextComponent(String text) {
      return getTextComponent(text, false);
   }

   public static TextComponent getTextComponent(String text, Boolean click) {
      if (!click) {
         TextComponent tc = new TextComponent(text);
         tc.setColor(ChatColor.RED);
         return tc;
      } else {
         String clickAction = "SUGGEST_COMMAND";
         String splitter = text.contains("{") ? "{" : "[";
         TextComponent tc = buildComponent(text, Messages.helpclick, StringUtils.substringBefore(text, splitter), "", "SUGGEST_COMMAND");
         tc.setColor(ChatColor.GOLD);
         tc.addExtra(getTextComponentDelimiter(" - "));
         return tc;
      }
   }

   private static TextComponent getTextComponentDelimiter(String delim) {
      TextComponent tc = new TextComponent(delim);
      tc.setColor(ChatColor.WHITE);
      return tc;
   }

   public static boolean debug() {
      return TNTRun.getInstance().getConfig().getBoolean("debug", false);
   }

   public static String getDecimalFormat(String amount) {
      String formattedAmount = amount;
      if (!isNumber(amount)) {
         DecimalFormat df = new DecimalFormat("0.00");
         formattedAmount = !amount.endsWith(".00") && !amount.endsWith(".0") ? df.format(Double.valueOf(amount)) : amount.split("\\.")[0];
      }

      return formattedAmount;
   }

   public static String getFormattedCurrency(String amount) {
      String var10000 = TNTRun.getInstance().getConfig().getString("currency.prefix");
      return var10000 + getDecimalFormat(amount) + TNTRun.getInstance().getConfig().getString("currency.suffix");
   }

   public static void cachePlayerGroupData(OfflinePlayer player) {
      String rank = TNTRun.getInstance().getVaultHandler().getPermissions().getPrimaryGroup((String)null, player);
      if (rank != null) {
         if (!rank.equalsIgnoreCase((String)ranks.get(player.getName()))) {
            cacheRank(player);
         }

      }
   }

   private static void cacheRank(final OfflinePlayer player) {
      final FileConfiguration config = TNTRun.getInstance().getConfig();
      String rank = "";
      String cgmeta = "";
      final String pn;
      if (TNTRun.getInstance().getVaultHandler().isPermissions() && config.getBoolean("UseRankInChat.usegroup")) {
         if (player.isOnline()) {
            rank = TNTRun.getInstance().getVaultHandler().getPermissions().getPrimaryGroup((String)null, player);
            if (config.getBoolean("UseRankInChat.groupcolormeta")) {
               cgmeta = TNTRun.getInstance().getVaultHandler().getChat().getGroupInfoString("", rank, "tntrun-color", "");
            }

            if (debug()) {
               log.info("[TNTRun_reloaded] Cached rank " + rank + " for online player " + player.getName());
               log.info("[TNTRun_reloaded] Cached colour " + cgmeta + " for online player " + player.getName());
            }
         } else {
            pn = player.getName();
            (new BukkitRunnable() {
               public void run() {
                  String cgmeta = "";
                  String rank = TNTRun.getInstance().getVaultHandler().getPermissions().getPrimaryGroup((String)null, player);
                  Utils.ranks.put(pn, rank != null ? rank : "");
                  if (config.getBoolean("UseRankInChat.groupcolormeta")) {
                     cgmeta = TNTRun.getInstance().getVaultHandler().getChat().getGroupInfoString("", rank, "tntrun-color", "");
                     Utils.colours.put(pn, cgmeta != null ? cgmeta : "");
                  }

                  if (Utils.debug()) {
                     Utils.log.info("[TNTRun_reloaded] Cached rank " + rank + " for offline player " + pn);
                     Utils.log.info("[TNTRun_reloaded] Cached colour " + cgmeta + " for offline player " + pn);
                  }

               }
            }).runTaskAsynchronously(TNTRun.getInstance());
         }
      } else if (TNTRun.getInstance().getVaultHandler().isChat() && config.getBoolean("UseRankInChat.useprefix")) {
         if (player.isOnline()) {
            rank = TNTRun.getInstance().getVaultHandler().getChat().getPlayerPrefix((String)null, player);
            if (debug()) {
               log.info("[TNTRun_reloaded] Cached prefix " + rank + " for online player " + player.getName());
            }
         } else {
            pn = player.getName();
            (new BukkitRunnable() {
               public void run() {
                  String rank = TNTRun.getInstance().getVaultHandler().getChat().getPlayerPrefix((String)null, player);
                  Utils.ranks.put(pn, rank != null ? rank : "");
                  if (Utils.debug()) {
                     Utils.log.info("[TNTRun_reloaded] Cached prefix " + rank + "for offline player " + pn);
                  }

               }
            }).runTaskAsynchronously(TNTRun.getInstance());
         }
      }

      ranks.put(player.getName(), rank != null ? rank : "");
      colours.put(player.getName(), cgmeta != null ? cgmeta : "");
   }

   public static String getRank(OfflinePlayer player) {
      if (TNTRun.getInstance().getConfig().getBoolean("UseRankInChat.enabled")) {
         if (ranks.containsKey(player.getName())) {
            return (String)ranks.get(player.getName());
         }

         cacheRank(player);
      }

      return "";
   }

   public static String getRank(String playerName) {
      return !TNTRun.getInstance().getConfig().getBoolean("UseRankInChat.enabled") ? "" : (String)ranks.getOrDefault(playerName, "");
   }

   public static String getColourMeta(OfflinePlayer player) {
      FileConfiguration config = TNTRun.getInstance().getConfig();
      return config.getBoolean("UseRankInChat.enabled") && config.getBoolean("UseRankInChat.groupcolormeta") && colours.containsKey(player.getName()) ? (String)colours.get(player.getName()) : "";
   }

   public static String getColourMeta(String playerName) {
      FileConfiguration config = TNTRun.getInstance().getConfig();
      if (config.getBoolean("UseRankInChat.enabled") && config.getBoolean("UseRankInChat.groupcolormeta")) {
         return colours.get(playerName) != null ? (String)colours.get(playerName) : "";
      } else {
         return "";
      }
   }

   public static void removeRankFromCache(String playerName) {
      ranks.remove(playerName);
      colours.remove(playerName);
   }

   public static int getAllowedDoubleJumps(Player player, int max) {
      if (TNTRun.getInstance().getConfig().getBoolean("special.UseDoubleJumpPermissions") && max > 0) {
         String permissionPrefix = "tntrun.doublejumps.";
         Iterator var3 = player.getEffectivePermissions().iterator();

         PermissionAttachmentInfo attachmentInfo;
         do {
            if (!var3.hasNext()) {
               return max;
            }

            attachmentInfo = (PermissionAttachmentInfo)var3.next();
         } while(!attachmentInfo.getPermission().startsWith(permissionPrefix) || !attachmentInfo.getValue());

         String permission = attachmentInfo.getPermission();
         if (!isNumber(permission.substring(permission.lastIndexOf(".") + 1))) {
            return 0;
         } else {
            return Math.min(Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1)), max);
         }
      } else {
         return max;
      }
   }

   public static void displayPartyInvite(Player player, String target) {
      String command1 = "/tntrun party accept ";
      String command2 = "/tntrun party decline ";
      String clickAction = "RUN_COMMAND";
      TextComponent accept = buildComponent(Messages.partyclickaccept, Messages.partyaccepttext, player.getName(), "/tntrun party accept ", "RUN_COMMAND");
      TextComponent decline = buildComponent(Messages.partyclickdecline, Messages.partydeclinetext, player.getName(), "/tntrun party decline ", "RUN_COMMAND");
      accept.addExtra(" | ");
      Bukkit.getPlayer(target).spigot().sendMessage(new BaseComponent[]{accept, decline});
   }

   private static TextComponent buildComponent(String text, String hoverMessage, String target, String command, String clickAction) {
      TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
      Content content = new Text(ChatColor.translateAlternateColorCodes('&', hoverMessage));
      component.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.valueOf(clickAction), command + target));
      component.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new Content[]{content}));
      return component;
   }
}
