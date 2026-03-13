package me.gypopo.economyshopgui.methodes;

import github.scarsz.discordsrv.DiscordSRV;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.providers.DiscordSRVProvider;
import me.gypopo.economyshopgui.util.ChatUtil;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.Transaction;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SendMessage {
   private static EconomyShopGUI plugin;
   private static DiscordSRVProvider discordSRV;
   private static boolean advancedTransactionStore;
   private static boolean logPlayerTransactions;
   private static boolean logTransactions;
   private static boolean onScreenTitle;

   public SendMessage(EconomyShopGUI plugin) {
      SendMessage.plugin = plugin;
   }

   public static void init() {
      if (ConfigManager.getConfig().getBoolean("log-player-transactions")) {
         logPlayerTransactions = true;
      }

      if (ConfigManager.getConfig().getBoolean("transaction-log")) {
         logTransactions = true;
      }

      if (ConfigManager.getConfig().getBoolean("advanced-transaction-log", true)) {
         advancedTransactionStore = true;
      }

      if (ConfigManager.getConfig().getBoolean("on-screen-title")) {
         onScreenTitle = true;
      }

      if (ConfigManager.getConfig().getBoolean("enable-discordsrv-hook") && plugin.getServer().getPluginManager().getPlugin("DiscordSRV") != null) {
         if (DiscordSRV.isReady) {
            discordSRV = new DiscordSRVProvider(plugin, ConfigManager.getConfig().getConfigurationSection("discordsrv-transactions"));
         } else {
            DiscordSRV.api.subscribe(discordSRV = new DiscordSRVProvider(plugin));
            infoMessage("Waiting for DiscordSRV to initialize...");
         }
      }

      plugin.getTransactionLog().setup();
   }

   public static void sendTransactionMessage(Player player, int amount, double price, ShopItem shopItem, Transaction.Mode mode, Transaction.Type type) {
      plugin.getMetaUtils().sendTransactionMessage(player, amount, price, shopItem, mode, type);
      Translatable message = Lang.PLAYER_MADE_TRANSACTION.get();
      String resetColor = ChatUtil.getLastColors(message.toString().split("%material%")[0]);
      if (resetColor.isEmpty()) {
         resetColor = "§f";
      }

      logPlayerTransaction(message.replace("%playername%", player.getName()).replace("%player_displayname%", plugin.getDisplayName(player)).replace("%bought/sold%", mode.getName()).replace("%amount%", String.valueOf(amount)).replace("%material%", shopItem instanceof ShopItem.Shulker ? shopItem.getDisplayname() + resetColor + "(" + shopItem.getItemPath() + ")" + ((ShopItem.Shulker)shopItem).getContents().entrySet().stream().map((entry) -> {
         return entry.getValue() + "x " + ((ShopItem)entry.getKey()).getDisplayname() + resetColor + "(" + ((ShopItem)entry.getKey()).getItemPath() + ")";
      }).collect(Collectors.toList()) : shopItem.getDisplayname() + resetColor + "(" + shopItem.getItemPath() + ")").replace("%amountofmoney%", plugin.formatPrice(shopItem.getEcoType(), price) + resetColor).replace("%buy/sell-method%", type.getName()));
      if (discordSRV != null && discordSRV.enabled) {
         plugin.runTaskAsync(() -> {
            discordSRV.logTransaction(player, amount, mode.getName(), shopItem.getDisplayname(), plugin.formatPrice(shopItem.getEcoType(), price));
         });
      }

      if (onScreenTitle && (type == Transaction.Type.SELL_ALL_COMMAND || type == Transaction.Type.SELL_GUI_SCREEN || shopItem.isCloseMenu())) {
         sendTitle(player, mode, Collections.singletonMap(shopItem.getEcoType(), price));
      }

      if (advancedTransactionStore) {
         plugin.getTransactionLog().log(player.getUniqueId(), shopItem, Collections.singletonMap(shopItem.getEcoType(), price), amount, mode, type);
      }

   }

   public static void sendTransactionMessage(Player player, int amount, Map<EcoType, Double> prices, Map<ShopItem, Integer> items, Transaction.Type type) {
      plugin.getMetaUtils().sendTransactionMessage(player, amount, prices, items, type);
      Translatable message = Lang.PLAYER_MADE_TRANSACTION_MULTIPLE_ITEMS.get();
      String resetColor = ChatUtil.getLastColors(message.toString().split("%items%")[0]);
      if (resetColor.isEmpty()) {
         resetColor = "§f";
      }

      logPlayerTransaction(message.replace("%playername%", player.getName()).replace("%player_displayname%", plugin.getDisplayName(player)).replace("%bought/sold%", Lang.SOLD.get().toString()).replace("%items%", getItemDisplay(resetColor, items, true)).replace("%amountofmoney%", getCurrencyDisplay(resetColor, prices)).replace("%buy/sell-method%", type.getName()));
      if (discordSRV != null && discordSRV.enabled) {
         plugin.runTaskAsync(() -> {
            discordSRV.logTransaction(player, amount, Lang.SOLD.get().getLegacy(), getItemDisplay(resetColor, items, false), getCurrencyDisplay((String)null, prices));
         });
      }

      if (onScreenTitle) {
         sendTitle(player, prices);
      }

      if (advancedTransactionStore) {
         plugin.getTransactionLog().log(player.getUniqueId(), items, prices, amount, Transaction.Mode.SELL, type);
      }

   }

   private static String getItemDisplay(String resetColor, Map<ShopItem, Integer> items, boolean logs) {
      StringBuilder sb = new StringBuilder();
      int i = 0;

      for(Iterator var5 = items.entrySet().iterator(); var5.hasNext(); ++i) {
         Entry<ShopItem, Integer> entry = (Entry)var5.next();
         if (entry.getKey() instanceof ShopItem.Shulker) {
            Map<ShopItem, Integer> contents = ((ShopItem.Shulker)entry.getKey()).getContents();
            sb.append(entry.getValue()).append("x ").append(((ShopItem)entry.getKey()).getDisplayname());
            sb.append(contents.entrySet().stream().map((e) -> {
               return e.getValue() + "x " + ((ShopItem)e.getKey()).getDisplayname();
            }).collect(Collectors.toList()));
         } else {
            sb.append(entry.getValue()).append("x ").append(((ShopItem)entry.getKey()).getDisplayname());
         }

         if (logs) {
            if (resetColor != null) {
               sb.append(resetColor);
            }

            sb.append("(").append(((ShopItem)entry.getKey()).getItemPath()).append(")");
         }

         if (resetColor != null) {
            sb.append(resetColor);
         }

         if (i != items.size() - 1) {
            sb.append(", ");
         }
      }

      return sb.toString();
   }

   private static String getCurrencyDisplay(String resetColor, Map<EcoType, Double> prices) {
      StringBuilder sb = new StringBuilder();
      int i = 0;

      for(Iterator var4 = prices.entrySet().iterator(); var4.hasNext(); ++i) {
         Entry<EcoType, Double> entry = (Entry)var4.next();
         sb.append(plugin.formatPrice((EcoType)entry.getKey(), (Double)entry.getValue()));
         sb.append(resetColor);
         if (i != prices.size() - 1) {
            sb.append(", ");
         }
      }

      return sb.toString();
   }

   private static void sendTitle(Player player, Map<EcoType, Double> prices) {
      sendTitle(player, (Transaction.Mode)null, prices);
   }

   private static void sendTitle(Player player, Transaction.Mode mode, Map<EcoType, Double> prices) {
      String message = mode != null && mode != Transaction.Mode.SELL ? Lang.ON_SCREEN_TITLE_BOUGHT.get().getLegacy() : Lang.ON_SCREEN_TITLE_SOLD.get().getLegacy();
      String resetColor = ChatColor.getLastColors(message.split("%price%")[0]);
      if (resetColor.isEmpty()) {
         resetColor = "§f";
      }

      player.sendTitle(message.replace("%price%", getCurrencyDisplay(resetColor, prices)), "");
   }

   public static void sendMessage(Object logger, Translatable message) {
      plugin.getMetaUtils().send(logger, message);
   }

   public static void sendMessage(Object logger, String msg) {
      plugin.getMetaUtils().sendLegacy(logger, msg);
   }

   public static void chatToPlayer(Player p, Translatable message) {
      plugin.getMetaUtils().send(p, message);
   }

   public static void chatToPlayer(Player p, String message) {
      plugin.getMetaUtils().sendLegacy(p, message);
   }

   public static void chatToPlayer(Player p, TextComponent message) {
      TextComponent text = new TextComponent(TextComponent.fromLegacyText(Lang.SHOP_PREFIX.get().getLegacy()));
      text.addExtra(message);
      p.spigot().sendMessage(text);
   }

   public static void broadcastMessage(Translatable msg) {
      plugin.getMetaUtils().broadcast(msg);
   }

   public static void logDebugMessage(String msg) {
      if (ConfigManager.getConfig().getBoolean("debug")) {
         plugin.getMetaUtils().logLegacyDebugMessage(msg);
      }

   }

   public static void logDebugMessage(Translatable message) {
      if (ConfigManager.getConfig().getBoolean("debug")) {
         plugin.getMetaUtils().logDebugMessage(message);
      }

   }

   public static void logDevDebugMessage(Translatable msg) {
      plugin.getMetaUtils().logDevDebugMessage(msg);
   }

   public static void logPlayerTransaction(Translatable message) {
      if (logPlayerTransactions) {
         plugin.getMetaUtils().logPlayerTransaction(message);
      }

      if (logTransactions) {
         plugin.getTransactionLog().log(ChatUtil.stripColor(message.toString()));
      }

   }

   public static void infoMessage(String msg) {
      plugin.getMetaUtils().infoLegacyMessage(msg);
   }

   public static void infoMessage(Translatable message) {
      plugin.getMetaUtils().infoMessage(message);
   }

   public static void infoMessage(Object logger, String msg) {
      plugin.getMetaUtils().infoLegacyMessage(logger, msg);
   }

   public static void infoMessage(Object logger, Translatable message) {
      plugin.getMetaUtils().infoMessage(logger, message);
   }

   public static void warnMessage(String msg) {
      plugin.getMetaUtils().warnLegacyMessage(msg);
   }

   public static void warnMessage(Translatable message) {
      plugin.getMetaUtils().warnMessage(message);
   }

   public static void warnMessage(Object logger, String msg) {
      plugin.getMetaUtils().warnLegacyMessage(logger, msg);
   }

   public static void warnMessage(Object logger, Translatable message) {
      plugin.getMetaUtils().warnMessage(logger, message);
   }

   public static void errorMessage(String message) {
      plugin.getMetaUtils().errorLegacyMessage(message);
   }

   public static void errorMessage(Translatable message) {
      plugin.getMetaUtils().errorMessage(message);
   }

   public static void errorMessage(Object logger, String msg) {
      plugin.getMetaUtils().errorLegacyMessage(logger, msg);
   }

   public static void errorMessage(Object logger, Translatable message) {
      plugin.getMetaUtils().errorMessage(logger, message);
   }

   public static void errorItemShops(String itemPath) {
      warnMessage(Lang.ITEMS_PATH_IN_SHOPS_CONFIG.get().replace("%location%", itemPath));
   }

   public static void errorShops(String section, String path) {
      warnMessage(Lang.ITEMS_LOCATION_IN_SHOPS.get().replace("%section%", section).replace("%location%", path));
   }

   public static void errorItemConfig(String path) {
      warnMessage(Lang.ITEMS_PATH_IN_CONFIG.get().replace("%location%", path));
   }

   public static void errorItemSections(String itemPath) {
      warnMessage(Lang.ITEMS_PATH_IN_SECTIONS_CONFIG.get().replace("%location%", itemPath));
   }

   public static void errorSections(String section, String path) {
      warnMessage(Lang.ITEMS_LOCATION_IN_SECTIONS.get().replace("%section%", section).replace("%location%", path));
   }
}
