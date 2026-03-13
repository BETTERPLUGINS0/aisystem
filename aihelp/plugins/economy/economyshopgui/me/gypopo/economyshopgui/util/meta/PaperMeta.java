package me.gypopo.economyshopgui.util.meta;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.files.lang.TranslatableComponent;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.util.AdventureUtil;
import me.gypopo.economyshopgui.util.ChatUtil;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.MetaUtils;
import me.gypopo.economyshopgui.util.ServerInfo;
import me.gypopo.economyshopgui.util.Transaction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PaperMeta implements MetaUtils {
   public static final Map<Character, TextColor> COLOR_MAP = new HashMap();
   private static final Component RESET;
   private static final PaperMeta.Config config;
   private final ConsoleCommandSender logger;
   private final EconomyShopGUI plugin;
   private PaperMeta.Utils utils;

   public PaperMeta(EconomyShopGUI plugin) {
      this.logger = plugin.getServer().getConsoleSender();
      this.plugin = plugin;
   }

   public void update() {
      if (ConfigManager.getConfig().getBoolean("use-paper-meta", true) && ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_21_R2)) {
         this.utils = new PaperMeta.PaperUtils();
         this.plugin.paperMeta = true;
      } else {
         this.utils = new PaperMeta.BukkitUtils();
         this.plugin.paperMeta = false;
      }

      config.update();
   }

   private void send(Player p, Component msg) {
      p.sendMessage(config.prefix.append(msg));
   }

   public void send(Player p, Translatable msg) {
      p.sendMessage(config.prefix.append((Component)msg.get()));
   }

   public void send(Object logger, Translatable msg) {
      if (logger instanceof Player) {
         ((Player)logger).sendMessage(config.prefix.append((Component)msg.get()));
      } else {
         ((CommandSender)logger).sendMessage((Component)msg.get());
      }

   }

   public void send(Translatable msg) {
      this.logger.sendMessage((Component)msg.get());
   }

   public void sendLegacy(Player p, String msg) {
      p.sendMessage(AdventureUtil.toLegacy(config.prefix) + msg);
   }

   public void sendLegacy(Object logger, String msg) {
      if (logger instanceof Player) {
         ((Player)logger).sendMessage(AdventureUtil.toLegacy(config.prefix) + msg);
      } else {
         ((CommandSender)logger).sendMessage(msg);
      }

   }

   public void sendLegacy(String msg) {
      this.logger.sendMessage(msg);
   }

   public void sendTransactionMessage(Player p, int amount, double price, ShopItem shopItem, Transaction.Mode mode, Transaction.Type type) {
      Translatable message = mode == Transaction.Mode.BUY ? Lang.PAY_CONFIRMATION.get() : Lang.SELL_CONFIRMATION.get();
      String resetColor = AdventureUtil.getLastColor(message.toString().split("%material%")[0]);
      this.send(p, (Component)message.replace("%amounttopay%", this.plugin.formatPrice(shopItem.getEcoType(), price)).replace("%amount%", String.valueOf(amount)).replace("%material%", (shopItem instanceof ShopItem.Shulker ? shopItem.getDisplayname() + ChatColor.GRAY + ((ShopItem.Shulker)shopItem).getContents().entrySet().stream().map((entry) -> {
         return entry.getValue() + "x " + ((ShopItem)entry.getKey()).getDisplayname() + ChatColor.GRAY;
      }).collect(Collectors.toList()) : shopItem.getDisplayname()) + resetColor).get());
   }

   public void sendTransactionMessage(Player p, int amount, Map<EcoType, Double> prices, Map<ShopItem, Integer> items, Transaction.Type type) {
      Translatable message = Lang.SELL_CONFIRMATION_MULTIPLE_ITEMS.get();
      String resetColor = AdventureUtil.getLastColor(message.toString().split("%items%")[0]);
      String itemDisplays = this.getItemDisplay(resetColor, items);
      HoverEvent<Component> hover = HoverEvent.showText(ChatUtil.getAdventureUtils().formatLegacyAndMini("§7" + this.getItemDisplay("§7", items))).asHoverEvent();
      this.send(p, ((Component)message.replace("%items%", itemDisplays).replace("%amount%", String.valueOf(amount)).replace("%amounttopay%", this.getCurrencyDisplay(resetColor, prices)).get()).hoverEvent(hover));
   }

   private String getItemDisplay(String resetColor, Map<ShopItem, Integer> items) {
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

         sb.append(resetColor);
         if (i != items.size() - 1) {
            sb.append(", ");
         }
      }

      return sb.toString();
   }

   private String getCurrencyDisplay(String resetColor, Map<EcoType, Double> prices) {
      StringBuilder sb = new StringBuilder();
      int i = 0;

      for(Iterator var5 = prices.entrySet().iterator(); var5.hasNext(); ++i) {
         Entry<EcoType, Double> entry = (Entry)var5.next();
         sb.append(this.plugin.formatPrice((EcoType)entry.getKey(), (Double)entry.getValue()));
         sb.append(resetColor);
         if (i != prices.size() - 1) {
            sb.append(", ");
         }
      }

      return sb.toString();
   }

   public Inventory createInventory(InventoryHolder holder, int size, Translatable title) {
      return this.utils.createInventory(holder, size, title);
   }

   public void setRawDisplayName(ItemMeta meta, String s) {
      this.utils.setRawDisplayName(meta, s);
   }

   public void setDisplayName(ItemMeta meta, Translatable s) {
      this.utils.setDisplayName(meta, s);
   }

   public String getRawDisplayName(ItemMeta meta) {
      return this.utils.getRawDisplayName(meta);
   }

   public Translatable getDisplayName(ItemMeta meta) {
      return this.utils.getDisplayName(meta);
   }

   public ItemStack setDisplayname(ItemStack item, Object name) {
      item.setData(DataComponentTypes.ITEM_NAME, ((Component)name).decoration(TextDecoration.ITALIC, false));
      return item;
   }

   public void setRawLore(ItemMeta meta, List<String> lore) {
      this.utils.setRawLore(meta, lore);
   }

   public List<String> getRawLore(ItemMeta meta) {
      return this.utils.getRawLore(meta);
   }

   public void setLore(ItemMeta meta, List<Translatable> lore) {
      this.utils.setLore(meta, lore);
   }

   public void addLore(ItemMeta meta, Translatable line) {
      this.utils.addLore(meta, line);
   }

   public List<Translatable> getLore(ItemMeta meta) {
      return this.utils.getLore(meta);
   }

   public static ItemStack setLore(ItemStack item, ArrayList<String> lore) {
      item.setData(DataComponentTypes.LORE, ItemLore.lore((List)lore.stream().map((s) -> {
         return AdventureUtil.fromGson(s).decoration(TextDecoration.ITALIC, false);
      }).collect(Collectors.toList())));
      return item;
   }

   public static ItemStack setRawLore(ItemStack item, ArrayList<String> lore) {
      item.setData(DataComponentTypes.LORE, ItemLore.lore((List)lore.stream().map((s) -> {
         return ChatUtil.getAdventureUtils().formatLegacyAndMini(s).decoration(TextDecoration.ITALIC, false);
      }).collect(Collectors.toList())));
      return item;
   }

   public void broadcast(Translatable msg) {
      this.plugin.getServer().broadcast(config.prefix.append((Component)msg.get()));
   }

   public void logDevDebugMessage(Translatable msg) {
      this.logger.sendMessage(config.consolePrefix.append(config.debug).append((Component)msg.get()));
   }

   public void logPlayerTransaction(Translatable msg) {
      this.logger.sendMessage(config.prefix.append((Component)msg.get()));
   }

   public void logPlayerTransaction(String msg) {
      this.logger.sendMessage(config.prefix.append(Component.text(msg)));
   }

   public void infoMessage(Translatable msg) {
      this.logger.sendMessage(config.consolePrefix.append(config.info).append((Component)msg.get()));
   }

   public void logDebugMessage(Translatable msg) {
      this.logger.sendMessage(config.consolePrefix.append(config.debug).append((Component)msg.get()));
   }

   public void warnMessage(Translatable msg) {
      this.logger.sendMessage(config.consolePrefix.append(config.warn).append((Component)msg.get()));
   }

   public void errorMessage(Translatable msg) {
      this.logger.sendMessage(config.consolePrefix.append(config.error).append((Component)msg.get()));
   }

   public void infoLegacyMessage(String msg) {
      this.logger.sendMessage(config.consolePrefix.append(config.info).append(Component.text(msg)));
   }

   public void logLegacyDebugMessage(String msg) {
      this.logger.sendMessage(config.consolePrefix.append(config.debug).append(Component.text(msg)));
   }

   public void warnLegacyMessage(String msg) {
      this.logger.sendMessage(config.consolePrefix.append(config.warn).append(Component.text(msg)));
   }

   public void errorLegacyMessage(String msg) {
      this.logger.sendMessage(config.consolePrefix.append(config.error).append(Component.text(msg)));
   }

   public void infoMessage(Object logger, Translatable msg) {
      if (logger instanceof Player) {
         ((Player)logger).sendMessage(config.prefix.color((TextColor)COLOR_MAP.get('a')).append((Component)msg.get()));
      } else {
         ((CommandSender)logger).sendMessage(config.consolePrefix.append(config.info).append((Component)msg.get()));
      }

   }

   public void warnMessage(Object logger, Translatable msg) {
      if (logger instanceof Player) {
         ((Player)logger).sendMessage(config.prefix.color((TextColor)COLOR_MAP.get('c')).append((Component)msg.get()));
      } else {
         ((CommandSender)logger).sendMessage(config.consolePrefix.append(config.warn).append((Component)msg.get()));
      }

   }

   public void errorMessage(Object logger, Translatable msg) {
      if (logger instanceof Player) {
         ((Player)logger).sendMessage(config.prefix.color((TextColor)COLOR_MAP.get('c')).append((Component)msg.get()));
      } else {
         ((CommandSender)logger).sendMessage(config.consolePrefix.append(config.error).append((Component)msg.get()));
      }

   }

   public void infoLegacyMessage(Object logger, String msg) {
      if (logger instanceof Player) {
         ((Player)logger).sendMessage(AdventureUtil.toLegacy(config.prefix) + ChatColor.GREEN + msg);
      } else if (logger instanceof CommandSender) {
         ((CommandSender)logger).sendMessage(AdventureUtil.toLegacy(config.consolePrefix) + config.info + msg);
      }

   }

   public void warnLegacyMessage(Object logger, String msg) {
      if (logger instanceof Player) {
         ((Player)logger).sendMessage(AdventureUtil.toLegacy(config.prefix) + ChatColor.RED + msg);
      } else if (logger instanceof CommandSender) {
         ((CommandSender)logger).sendMessage(AdventureUtil.toLegacy(config.consolePrefix) + config.warn + msg);
      }

   }

   public void errorLegacyMessage(Object logger, String msg) {
      if (logger instanceof Player) {
         ((Player)logger).sendMessage(AdventureUtil.toLegacy(config.prefix) + ChatColor.RED + msg);
      } else if (logger instanceof CommandSender) {
         ((CommandSender)logger).sendMessage(AdventureUtil.toLegacy(config.consolePrefix) + config.error + msg);
      }

   }

   static {
      RESET = ((TextComponent)((TextComponent)Component.text("").color((TextColor)null)).decoration(TextDecoration.BOLD, false)).decoration(TextDecoration.ITALIC, false);
      config = new PaperMeta.Config();
      COLOR_MAP.put('0', TextColor.fromHexString("#000000"));
      COLOR_MAP.put('1', TextColor.fromHexString("#0000AA"));
      COLOR_MAP.put('2', TextColor.fromHexString("#00AA00"));
      COLOR_MAP.put('3', TextColor.fromHexString("#00AAAA"));
      COLOR_MAP.put('4', TextColor.fromHexString("#AA0000"));
      COLOR_MAP.put('5', TextColor.fromHexString("#AA00AA"));
      COLOR_MAP.put('6', TextColor.fromHexString("#FFAA00"));
      COLOR_MAP.put('7', TextColor.fromHexString("#A9A9A9"));
      COLOR_MAP.put('8', TextColor.fromHexString("#555555"));
      COLOR_MAP.put('9', TextColor.fromHexString("#5555FF"));
      COLOR_MAP.put('a', TextColor.fromHexString("#55FF55"));
      COLOR_MAP.put('b', TextColor.fromHexString("#55FFFF"));
      COLOR_MAP.put('c', TextColor.fromHexString("#FF5555"));
      COLOR_MAP.put('d', TextColor.fromHexString("#FF55FF"));
      COLOR_MAP.put('e', TextColor.fromHexString("#FFFF55"));
      COLOR_MAP.put('f', TextColor.fromHexString("#FFFFFF"));
   }

   private static final class PaperUtils implements PaperMeta.Utils {
      private static final Method GET_DISPLAYNAME;
      private static final Method SET_DISPLAYNAME;
      private static final Method GET_LORE;
      private static final Method SET_LORE;

      private PaperUtils() {
      }

      public Inventory createInventory(InventoryHolder holder, int size, Translatable title) {
         return Bukkit.createInventory(holder, size, (Component)title.get());
      }

      public void setRawDisplayName(ItemMeta meta, String s) {
         try {
            SET_DISPLAYNAME.invoke(meta, ChatUtil.getAdventureUtils().formatLegacyAndMini(s).decoration(TextDecoration.ITALIC, false));
         } catch (InvocationTargetException | IllegalAccessException var4) {
            var4.printStackTrace();
         }

      }

      public void setDisplayName(ItemMeta meta, Translatable s) {
         try {
            SET_DISPLAYNAME.invoke(meta, ((Component)s.get()).decoration(TextDecoration.ITALIC, false));
         } catch (InvocationTargetException | IllegalAccessException var4) {
            var4.printStackTrace();
         }

      }

      public String getRawDisplayName(ItemMeta meta) {
         try {
            return AdventureUtil.serialize((Component)GET_DISPLAYNAME.invoke(meta));
         } catch (InvocationTargetException | IllegalAccessException var3) {
            var3.printStackTrace();
            return null;
         }
      }

      public Translatable getDisplayName(ItemMeta meta) {
         try {
            return new TranslatableComponent((Component)GET_DISPLAYNAME.invoke(meta));
         } catch (InvocationTargetException | IllegalAccessException var3) {
            var3.printStackTrace();
            return null;
         }
      }

      public void setRawLore(ItemMeta meta, List<String> lore) {
         try {
            SET_LORE.invoke(meta, lore.stream().map((s) -> {
               return ChatUtil.getAdventureUtils().formatLegacyAndMini(s).decoration(TextDecoration.ITALIC, false);
            }).collect(Collectors.toList()));
         } catch (InvocationTargetException | IllegalAccessException var4) {
            var4.printStackTrace();
         }

      }

      public void setLore(ItemMeta meta, List<Translatable> lore) {
         try {
            SET_LORE.invoke(meta, lore.stream().map((t) -> {
               return ((Component)t.get()).decoration(TextDecoration.ITALIC, false);
            }).collect(Collectors.toList()));
         } catch (InvocationTargetException | IllegalAccessException var4) {
            var4.printStackTrace();
         }

      }

      public void addLore(ItemMeta meta, Translatable line) {
         try {
            List<Component> lore = (List)GET_LORE.invoke(meta);
            if (lore == null) {
               lore = new ArrayList();
            }

            ((List)lore).add(((Component)line.get()).decoration(TextDecoration.ITALIC, false));
            SET_LORE.invoke(meta, lore);
         } catch (InvocationTargetException | IllegalAccessException var4) {
            var4.printStackTrace();
         }

      }

      public List<String> getRawLore(ItemMeta meta) {
         try {
            List<Component> lore = (List)GET_LORE.invoke(meta);
            if (lore == null) {
               lore = new ArrayList();
            }

            return (List)((List)lore).stream().map((c) -> {
               return AdventureUtil.serialize(c);
            }).collect(Collectors.toList());
         } catch (InvocationTargetException | IllegalAccessException var3) {
            var3.printStackTrace();
            return null;
         }
      }

      public List<Translatable> getLore(ItemMeta meta) {
         try {
            List<Component> lore = (List)GET_LORE.invoke(meta);
            if (lore == null) {
               lore = new ArrayList();
            }

            return (List)((List)lore).stream().map(TranslatableComponent::new).collect(Collectors.toList());
         } catch (InvocationTargetException | IllegalAccessException var3) {
            var3.printStackTrace();
            return null;
         }
      }

      // $FF: synthetic method
      PaperUtils(Object x0) {
         this();
      }

      static {
         try {
            SET_DISPLAYNAME = ItemMeta.class.getDeclaredMethod("displayName", Component.class);
            SET_DISPLAYNAME.setAccessible(true);
            GET_DISPLAYNAME = ItemMeta.class.getDeclaredMethod("displayName");
            GET_DISPLAYNAME.setAccessible(true);
            SET_LORE = ItemMeta.class.getDeclaredMethod("lore", List.class);
            SET_LORE.setAccessible(true);
            GET_LORE = ItemMeta.class.getDeclaredMethod("lore");
            GET_LORE.setAccessible(true);
         } catch (NoSuchMethodException var1) {
            throw new RuntimeException(var1);
         }
      }
   }

   private interface Utils {
      Inventory createInventory(InventoryHolder var1, int var2, Translatable var3);

      void setRawDisplayName(ItemMeta var1, String var2);

      void setDisplayName(ItemMeta var1, Translatable var2);

      String getRawDisplayName(ItemMeta var1);

      Translatable getDisplayName(ItemMeta var1);

      void setRawLore(ItemMeta var1, List<String> var2);

      void setLore(ItemMeta var1, List<Translatable> var2);

      void addLore(ItemMeta var1, Translatable var2);

      List<String> getRawLore(ItemMeta var1);

      List<Translatable> getLore(ItemMeta var1);
   }

   private static final class BukkitUtils implements PaperMeta.Utils {
      private BukkitUtils() {
      }

      public Inventory createInventory(InventoryHolder holder, int size, Translatable title) {
         return Bukkit.createInventory(holder, size, title.getLegacy());
      }

      public void setRawDisplayName(ItemMeta meta, String s) {
         meta.setDisplayName(s);
      }

      public void setDisplayName(ItemMeta meta, Translatable s) {
         meta.setDisplayName(s.getLegacy());
      }

      public String getRawDisplayName(ItemMeta meta) {
         return meta.getDisplayName();
      }

      public Translatable getDisplayName(ItemMeta meta) {
         return new TranslatableComponent(meta.getDisplayName());
      }

      public void setRawLore(ItemMeta meta, List<String> lore) {
         meta.setLore(lore);
      }

      public void setLore(ItemMeta meta, List<Translatable> lore) {
         meta.setLore((List)lore.stream().map(Translatable::getLegacy).collect(Collectors.toList()));
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
         return (List)meta.getLore().stream().map(TranslatableComponent::new).collect(Collectors.toList());
      }

      // $FF: synthetic method
      BukkitUtils(Object x0) {
         this();
      }
   }

   private static final class Config {
      public final Component info;
      public final Component debug;
      public final Component warn;
      public final Component error;
      public final Component consolePrefix;
      public Component prefix;

      private Config() {
         this.info = ChatUtil.getAdventureUtils().formatLegacyAndMini("§8[§7INFO§8]§r: ");
         this.debug = ChatUtil.getAdventureUtils().formatLegacyAndMini("§8[§6DEBUG§8]§r: ");
         this.warn = ChatUtil.getAdventureUtils().formatLegacyAndMini("§8[§cWARN§8]§r: ");
         this.error = ChatUtil.getAdventureUtils().formatLegacyAndMini("§8[§4ERROR§8]§r: ");
         this.consolePrefix = ChatUtil.getAdventureUtils().formatLegacyAndMini("§8[§3EconomyShopGUI§8]§r ");
      }

      public void update() {
         this.prefix = ((Component)Lang.SHOP_PREFIX.get().get()).append(PaperMeta.RESET);
      }

      // $FF: synthetic method
      Config(Object x0) {
         this();
      }
   }
}
