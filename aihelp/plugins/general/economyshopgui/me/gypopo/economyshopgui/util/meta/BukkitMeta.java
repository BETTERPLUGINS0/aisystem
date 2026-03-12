package me.gypopo.economyshopgui.util.meta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.files.lang.TranslatableRaw;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.MetaUtils;
import me.gypopo.economyshopgui.util.Transaction;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.ItemMeta;

public class BukkitMeta implements MetaUtils {
   private static final BukkitMeta.Config config = new BukkitMeta.Config();
   private final ConsoleCommandSender logger;
   private final EconomyShopGUI plugin;

   public BukkitMeta(EconomyShopGUI plugin) {
      this.plugin = plugin;
      this.logger = plugin.getServer().getConsoleSender();
   }

   public void update() {
      config.update();
   }

   public void send(Player p, Translatable msg) {
      p.sendMessage(config.prefix + msg.get());
   }

   public void send(Object logger, Translatable msg) {
      if (logger instanceof Player) {
         this.send((Player)logger, msg);
      } else if (logger instanceof CommandSender) {
         ((CommandSender)logger).sendMessage((String)msg.get());
      }

   }

   public void send(Translatable msg) {
      this.logger.sendMessage((String)msg.get());
   }

   public void sendLegacy(Player p, String msg) {
      p.sendMessage(config.prefix + msg);
   }

   public void sendLegacy(Object logger, String msg) {
      if (logger instanceof Player) {
         this.sendLegacy((Player)logger, config.prefix + msg);
      } else if (logger instanceof CommandSender) {
         ((CommandSender)logger).sendMessage(msg);
      }

   }

   public void sendLegacy(String msg) {
      this.logger.sendMessage(msg);
   }

   private void send(Player p, TextComponent msg) {
      TextComponent text = new TextComponent(TextComponent.fromLegacyText((String)Lang.SHOP_PREFIX.get().get()));
      text.addExtra(msg);
      p.spigot().sendMessage(text);
   }

   public void sendTransactionMessage(Player p, int amount, double price, ShopItem shopItem, Transaction.Mode mode, Transaction.Type type) {
      String message = (String)(mode == Transaction.Mode.BUY ? Lang.PAY_CONFIRMATION.get().get() : Lang.SELL_CONFIRMATION.get().get());
      this.sendLegacy(p, message.replace("%amount%", String.valueOf(amount)).replace("%material%", shopItem instanceof ShopItem.Shulker ? shopItem.getDisplayname() + ChatColor.GRAY + ((ShopItem.Shulker)shopItem).getContents().entrySet().stream().map((entry) -> {
         return entry.getValue() + "x " + ((ShopItem)entry.getKey()).getDisplayname() + ChatColor.GRAY;
      }).collect(Collectors.toList()) : shopItem.getDisplayname()).replace("%amounttopay%", EconomyShopGUI.getInstance().formatPrice(shopItem.getEcoType(), price)));
   }

   public void sendTransactionMessage(Player p, int amount, Map<EcoType, Double> prices, Map<ShopItem, Integer> items, Transaction.Type type) {
      String itemDisplays = this.getItemDisplay(items, false);
      String message = ((String)Lang.SELL_CONFIRMATION_MULTIPLE_ITEMS.get().get()).replace("%items%", itemDisplays).replace("%amount%", String.valueOf(amount)).replace("%amounttopay%", this.getCurrencyDisplay(prices, false));
      TextComponent main = new TextComponent(TextComponent.fromLegacyText(message));
      main.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.GRAY + itemDisplays)));
      if (this.plugin.spigot) {
         this.send(p, main);
      } else {
         this.sendLegacy(p, message);
      }

   }

   private String getItemDisplay(Map<ShopItem, Integer> items, boolean c) {
      return this.getItemDisplay(items, c, false);
   }

   private String getItemDisplay(Map<ShopItem, Integer> items, boolean c, boolean logs) {
      StringBuilder sb = new StringBuilder();
      int i = 0;

      for(Iterator var6 = items.entrySet().iterator(); var6.hasNext(); ++i) {
         Entry<ShopItem, Integer> entry = (Entry)var6.next();
         if (entry.getKey() instanceof ShopItem.Shulker) {
            Map<ShopItem, Integer> contents = ((ShopItem.Shulker)entry.getKey()).getContents();
            sb.append(entry.getValue()).append("x ").append(((ShopItem)entry.getKey()).getDisplayname());
            sb.append(contents.entrySet().stream().map((e) -> {
               return e.getValue() + "x " + ((ShopItem)e.getKey()).getDisplayname();
            }).collect(Collectors.toList()));
         } else {
            sb.append(entry.getValue()).append("x ").append(((ShopItem)entry.getKey()).getDisplayname());
         }

         sb.append("§r");
         if (logs) {
            sb.append("(").append(((ShopItem)entry.getKey()).getItemPath()).append(")");
         }

         if (!c) {
            sb.append("§7");
         }

         if (i != items.size() - 1) {
            sb.append(", ");
         }
      }

      return sb.toString();
   }

   private String getCurrencyDisplay(Map<EcoType, Double> prices, boolean c) {
      return this.getCurrencyDisplay(((String)Lang.SELL_CONFIRMATION_MULTIPLE_ITEMS.get().get()).split("%amountofmoney%")[0], prices, c);
   }

   private String getCurrencyDisplay(String message, Map<EcoType, Double> prices, boolean c) {
      String color = ChatColor.getLastColors(message);
      StringBuilder sb = new StringBuilder();
      int i = 0;

      for(Iterator var7 = prices.entrySet().iterator(); var7.hasNext(); ++i) {
         Entry<EcoType, Double> entry = (Entry)var7.next();
         sb.append(this.plugin.formatPrice((EcoType)entry.getKey(), (Double)entry.getValue()));
         sb.append("§r");
         if (!c) {
            sb.append(color);
         }

         if (i != prices.size() - 1) {
            sb.append(", ");
         }
      }

      return sb.toString();
   }

   public Inventory createInventory(InventoryHolder holder, int size, Translatable title) {
      return Bukkit.createInventory(holder, size, (String)title.get());
   }

   public void setRawDisplayName(ItemMeta meta, String s) {
      meta.setDisplayName(s);
   }

   public void setDisplayName(ItemMeta meta, Translatable s) {
      meta.setDisplayName((String)s.get());
   }

   public String getRawDisplayName(ItemMeta meta) {
      return meta.getDisplayName();
   }

   public Translatable getDisplayName(ItemMeta meta) {
      return new TranslatableRaw(meta.getDisplayName());
   }

   public void setRawLore(ItemMeta meta, List<String> lore) {
      meta.setLore(lore);
   }

   public void setLore(ItemMeta meta, List<Translatable> lore) {
      meta.setLore((List)lore.stream().map((t) -> {
         return (String)t.get();
      }).collect(Collectors.toList()));
   }

   public void addLore(ItemMeta meta, Translatable line) {
      List<String> list = meta.hasLore() ? meta.getLore() : new ArrayList();
      ((List)list).add(line.getLegacy());
      meta.setLore((List)list);
   }

   public List<String> getRawLore(ItemMeta meta) {
      return meta.getLore();
   }

   public List<Translatable> getLore(ItemMeta meta) {
      return (List)meta.getLore().stream().map(TranslatableRaw::new).collect(Collectors.toList());
   }

   public void broadcast(Translatable msg) {
      this.plugin.getServer().broadcastMessage(config.prefix + msg.get());
   }

   public void logDevDebugMessage(Translatable msg) {
      ConsoleCommandSender var10000 = this.logger;
      StringBuilder var10001 = new StringBuilder();
      Objects.requireNonNull(config);
      var10000.sendMessage(var10001.append("§8[§6DEBUG§8]§r: ").append(msg.get()).toString());
   }

   public void logPlayerTransaction(Translatable msg) {
      this.logger.sendMessage(config.prefix + msg.get());
   }

   public void logPlayerTransaction(String msg) {
      this.logger.sendMessage(config.prefix + msg);
   }

   public void infoMessage(Translatable msg) {
      ConsoleCommandSender var10000 = this.logger;
      StringBuilder var10001 = new StringBuilder();
      Objects.requireNonNull(config);
      var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
      Objects.requireNonNull(config);
      var10000.sendMessage(var10001.append("§8[§7INFO§8]§r: ").append(msg.get()).toString());
   }

   public void logDebugMessage(Translatable msg) {
      ConsoleCommandSender var10000 = this.logger;
      StringBuilder var10001 = new StringBuilder();
      Objects.requireNonNull(config);
      var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
      Objects.requireNonNull(config);
      var10000.sendMessage(var10001.append("§8[§6DEBUG§8]§r: ").append(msg.get()).toString());
   }

   public void warnMessage(Translatable msg) {
      ConsoleCommandSender var10000 = this.logger;
      StringBuilder var10001 = new StringBuilder();
      Objects.requireNonNull(config);
      var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
      Objects.requireNonNull(config);
      var10000.sendMessage(var10001.append("§8[§cWARN§8]§r: ").append(msg.get()).toString());
   }

   public void errorMessage(Translatable msg) {
      ConsoleCommandSender var10000 = this.logger;
      StringBuilder var10001 = new StringBuilder();
      Objects.requireNonNull(config);
      var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
      Objects.requireNonNull(config);
      var10000.sendMessage(var10001.append("§8[§4ERROR§8]§r: ").append(msg.get()).toString());
   }

   public void infoLegacyMessage(String msg) {
      ConsoleCommandSender var10000 = this.logger;
      StringBuilder var10001 = new StringBuilder();
      Objects.requireNonNull(config);
      var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
      Objects.requireNonNull(config);
      var10000.sendMessage(var10001.append("§8[§7INFO§8]§r: ").append(msg).toString());
   }

   public void logLegacyDebugMessage(String msg) {
      ConsoleCommandSender var10000 = this.logger;
      StringBuilder var10001 = new StringBuilder();
      Objects.requireNonNull(config);
      var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
      Objects.requireNonNull(config);
      var10000.sendMessage(var10001.append("§8[§6DEBUG§8]§r: ").append(msg).toString());
   }

   public void warnLegacyMessage(String msg) {
      ConsoleCommandSender var10000 = this.logger;
      StringBuilder var10001 = new StringBuilder();
      Objects.requireNonNull(config);
      var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
      Objects.requireNonNull(config);
      var10000.sendMessage(var10001.append("§8[§cWARN§8]§r: ").append(msg).toString());
   }

   public void errorLegacyMessage(String msg) {
      ConsoleCommandSender var10000 = this.logger;
      StringBuilder var10001 = new StringBuilder();
      Objects.requireNonNull(config);
      var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
      Objects.requireNonNull(config);
      var10000.sendMessage(var10001.append("§8[§4ERROR§8]§r: ").append(msg).toString());
   }

   public void infoMessage(Object logger, Translatable msg) {
      if (logger instanceof Player) {
         this.sendLegacy((Player)logger, ChatColor.GREEN.toString() + msg.get());
      } else if (logger instanceof CommandSender) {
         CommandSender var10000 = (CommandSender)logger;
         StringBuilder var10001 = new StringBuilder();
         Objects.requireNonNull(config);
         var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
         Objects.requireNonNull(config);
         var10000.sendMessage(var10001.append("§8[§7INFO§8]§r: ").append(msg.get()).toString());
      }

   }

   public void warnMessage(Object logger, Translatable msg) {
      if (logger instanceof Player) {
         this.sendLegacy((Player)logger, ChatColor.RED.toString() + msg.get());
      } else if (logger instanceof CommandSender) {
         CommandSender var10000 = (CommandSender)logger;
         StringBuilder var10001 = new StringBuilder();
         Objects.requireNonNull(config);
         var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
         Objects.requireNonNull(config);
         var10000.sendMessage(var10001.append("§8[§cWARN§8]§r: ").append(msg.get()).toString());
      }

   }

   public void errorMessage(Object logger, Translatable msg) {
      if (logger instanceof Player) {
         this.sendLegacy((Player)logger, ChatColor.RED.toString() + msg.get());
      } else if (logger instanceof CommandSender) {
         CommandSender var10000 = (CommandSender)logger;
         StringBuilder var10001 = new StringBuilder();
         Objects.requireNonNull(config);
         var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
         Objects.requireNonNull(config);
         var10000.sendMessage(var10001.append("§8[§4ERROR§8]§r: ").append(msg.get()).toString());
      }

   }

   public void infoLegacyMessage(Object logger, String msg) {
      if (logger instanceof Player) {
         this.sendLegacy((Player)logger, ChatColor.GREEN + msg);
      } else if (logger instanceof CommandSender) {
         CommandSender var10000 = (CommandSender)logger;
         StringBuilder var10001 = new StringBuilder();
         Objects.requireNonNull(config);
         var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
         Objects.requireNonNull(config);
         var10000.sendMessage(var10001.append("§8[§7INFO§8]§r: ").append(msg).toString());
      }

   }

   public void warnLegacyMessage(Object logger, String msg) {
      if (logger instanceof Player) {
         this.sendLegacy((Player)logger, ChatColor.RED + msg);
      } else if (logger instanceof CommandSender) {
         CommandSender var10000 = (CommandSender)logger;
         StringBuilder var10001 = new StringBuilder();
         Objects.requireNonNull(config);
         var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
         Objects.requireNonNull(config);
         var10000.sendMessage(var10001.append("§8[§cWARN§8]§r: ").append(msg).toString());
      }

   }

   public void errorLegacyMessage(Object logger, String msg) {
      if (logger instanceof Player) {
         this.sendLegacy((Player)logger, ChatColor.RED + msg);
      } else if (logger instanceof CommandSender) {
         CommandSender var10000 = (CommandSender)logger;
         StringBuilder var10001 = new StringBuilder();
         Objects.requireNonNull(config);
         var10001 = var10001.append("§8[§3EconomyShopGUI§8]§r ");
         Objects.requireNonNull(config);
         var10000.sendMessage(var10001.append("§8[§4ERROR§8]§r: ").append(msg).toString());
      }

   }

   private static final class Config {
      public final String info;
      public final String debug;
      public final String warn;
      public final String error;
      public final String consolePrefix;
      public String prefix;

      private Config() {
         this.info = "§8[§7INFO§8]§r: ";
         this.debug = "§8[§6DEBUG§8]§r: ";
         this.warn = "§8[§cWARN§8]§r: ";
         this.error = "§8[§4ERROR§8]§r: ";
         this.consolePrefix = "§8[§3EconomyShopGUI§8]§r ";
      }

      public void update() {
         this.prefix = Lang.SHOP_PREFIX.get().get() + "§r";
      }

      // $FF: synthetic method
      Config(Object x0) {
         this();
      }
   }
}
