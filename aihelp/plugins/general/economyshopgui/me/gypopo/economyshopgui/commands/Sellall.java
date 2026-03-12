package me.gypopo.economyshopgui.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.api.events.PostTransactionEvent;
import me.gypopo.economyshopgui.api.events.PreTransactionEvent;
import me.gypopo.economyshopgui.commands.base.PluginCommands;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.PermissionsCache;
import me.gypopo.economyshopgui.util.SmartStack;
import me.gypopo.economyshopgui.util.Sold;
import me.gypopo.economyshopgui.util.Transaction;
import me.gypopo.economyshopgui.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.util.StringUtil;

public class Sellall extends PluginCommands {
   private final boolean oldSellallCommand;

   public Sellall(EconomyShopGUI plugin, List<String> aliases, List<String> disabledWorlds) {
      super(plugin, (String)aliases.get(0), "Sell all items from your inventory, hand items or specific items", "/" + (String)aliases.remove(0) + " <inventory/item/hand> [qty]", (String)null, aliases, disabledWorlds);
      this.oldSellallCommand = plugin.badYMLParse == null && ConfigManager.getConfig().getBoolean("old-sellall-command");
   }

   public boolean execute(CommandSender sender, String label, String[] args) {
      if (this.plugin.badYMLParse != null) {
         SendMessage.warnMessage(sender, (String)"This command cannot be executed now, please fix the configuration formatting first!");
         return true;
      } else {
         if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!this.canUse(player)) {
               return true;
            }

            String usages;
            if (args.length == 0) {
               if (this.oldSellallCommand) {
                  if (PermissionsCache.hasPermission(player, "EconomyShopGUI.sellall")) {
                     this.sellInventory(player);
                  } else {
                     SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS.get());
                  }
               } else {
                  usages = this.getCommandUsages(player);
                  if (!usages.isEmpty()) {
                     SendMessage.chatToPlayer(player, Lang.SELLALL_COMMAND_USAGES.get());
                     SendMessage.chatToPlayer(player, usages);
                  } else {
                     SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS.get());
                  }
               }
            } else {
               usages = args[0].toUpperCase(Locale.ENGLISH);
               byte var6 = -1;
               switch(usages.hashCode()) {
               case 2209903:
                  if (usages.equals("HAND")) {
                     var6 = 1;
                  }
                  break;
               case 765995324:
                  if (usages.equals("INVENTORY")) {
                     var6 = 0;
                  }
               }

               switch(var6) {
               case 0:
                  if (PermissionsCache.hasPermission(player, "EconomyShopGUI.sellall")) {
                     this.sellInventory(player);
                  } else {
                     SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS.get());
                  }
                  break;
               case 1:
                  if (PermissionsCache.hasPermission(player, "EconomyShopGUI.sellallhand")) {
                     int qty = 0;
                     if (args.length >= 2) {
                        try {
                           qty = Integer.parseInt(args[1]);
                           if (qty <= 0) {
                              throw new NumberFormatException();
                           }
                        } catch (NumberFormatException var11) {
                           SendMessage.chatToPlayer(player, Lang.NO_VALID_AMOUNT.get());
                           return true;
                        }
                     }

                     this.sellHand(player, qty);
                  } else {
                     SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS.get());
                  }
                  break;
               default:
                  if (PermissionsCache.hasPermission(player, "EconomyShopGUI.sellallitem")) {
                     XMaterial mat = this.getItem(args[0].toUpperCase(Locale.ENGLISH), player);
                     if (mat == null) {
                        return true;
                     }

                     if (mat == XMaterial.AIR) {
                        SendMessage.chatToPlayer(player, Lang.CANNOT_SELL_AIR.get());
                        return true;
                     }

                     int quantity;
                     if (args.length >= 2) {
                        try {
                           quantity = Integer.parseInt(args[1]);
                           if (quantity <= 0) {
                              throw new NumberFormatException();
                           }
                        } catch (NumberFormatException var10) {
                           SendMessage.chatToPlayer(player, Lang.NO_VALID_AMOUNT.get());
                           return true;
                        }
                     } else {
                        quantity = Integer.MAX_VALUE;
                     }

                     this.removeItems(player, mat, quantity);
                  } else {
                     SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS.get());
                  }
               }
            }
         } else {
            SendMessage.warnMessage(sender, (Translatable)Lang.REAL_PLAYER.get());
         }

         return true;
      }
   }

   private void sellInventory(Player player) {
      Transaction.Result result = Transaction.Result.SUCCESS;
      Map<Integer, SmartStack> toRemove = new HashMap();
      Map<ShopItem, Sold> CACHE = new HashMap();
      int amount = 0;

      for(int i = 0; i <= 35; ++i) {
         ItemStack itemInInv = player.getInventory().getItem(i);
         if (itemInInv != null && itemInInv.getType() != Material.AIR) {
            ShopItem shopItem = this.plugin.createItem.matchShopItem(itemInInv, player, "sellall");
            if (shopItem != null && shopItem.getSellPrice() >= 0.0D) {
               int qty = itemInInv.getAmount();
               if (shopItem.isMaxSell(((Sold)CACHE.getOrDefault(shopItem, new Sold())).getAmount() + qty)) {
                  if (((Sold)CACHE.getOrDefault(shopItem, new Sold())).getAmount() >= shopItem.getMaxSell()) {
                     continue;
                  }

                  qty = shopItem.getMaxSell() - ((Sold)CACHE.getOrDefault(shopItem, new Sold())).getAmount();
               }

               Sold sold = (Sold)CACHE.computeIfAbsent(shopItem, (k) -> {
                  return new Sold();
               });
               sold.addValues(qty, shopItem.getSellPrice(player, itemInInv, qty));
               amount += qty;
               ItemStack itemStack = new ItemStack(itemInInv);
               itemStack.setAmount(itemStack.getAmount() - qty);
               toRemove.put(i, new SmartStack(itemStack));
               sold.addIndex(i);
            } else if (this.plugin.sellShulkers && XMaterial.isShulker(itemInInv.getType())) {
               Integer sold = this.sellShulker(CACHE, toRemove, player, itemInInv, "sellall", i);
               if (sold != null) {
                  amount += sold;
               }
            }
         }
      }

      Iterator var12 = (new HashSet(CACHE.entrySet())).iterator();

      while(var12.hasNext()) {
         Entry<ShopItem, Sold> sold = (Entry)var12.next();
         if (((ShopItem)sold.getKey()).isMinSell(((Sold)sold.getValue()).getAmount())) {
            CACHE.remove(sold.getKey());
            amount -= ((Sold)sold.getValue()).getAmount();
            Set var10000 = ((Sold)sold.getValue()).getIndexes();
            Objects.requireNonNull(toRemove);
            var10000.forEach(toRemove::remove);
         }
      }

      Map<EcoType, Double> prices = new HashMap();
      Map<ShopItem, Integer> items = new HashMap();
      if (amount > 0) {
         prices = this.callPreTransactionEvent(this.getItems(items, CACHE), this.getPrices((Map)prices, CACHE), player, amount);
         if (prices != null) {
            this.removeItems(player.getInventory(), toRemove);
            ((Map)prices).forEach((type, price) -> {
               this.plugin.getEcoHandler().getEcon(type).depositBalance(player, price);
            });
            SendMessage.sendTransactionMessage(player, amount, (Map)prices, items, Transaction.Type.SELL_ALL_COMMAND);
         } else {
            result = Transaction.Result.TRANSACTION_CANCELLED;
         }
      } else {
         result = Transaction.Result.NO_ITEMS_FOUND;
         SendMessage.chatToPlayer(player, Lang.NO_ITEM_FOUND.get());
      }

      Bukkit.getPluginManager().callEvent(new PostTransactionEvent(items, (Map)prices, player, amount, Transaction.Type.SELL_ALL_COMMAND, result));
   }

   private void sellHand(Player player, int quantity) {
      Transaction.Result result = Transaction.Result.SUCCESS;
      ItemStack itemInHand = this.plugin.versionHandler.getItemInHand(player);
      ShopItem shopItem = null;
      double sellPrice = 0.0D;
      if (itemInHand != null && itemInHand.getType() != Material.AIR) {
         shopItem = this.plugin.createItem.matchShopItem(itemInHand, player, "sellallhand");
         if (shopItem != null && shopItem.getSellPrice() >= 0.0D) {
            if (quantity == 0 || quantity >= itemInHand.getAmount()) {
               quantity = itemInHand.getAmount();
            }

            if (shopItem.isMaxSell(quantity)) {
               quantity = shopItem.getMaxSell();
            }

            sellPrice = this.callPreTransactionEvent(shopItem, player, quantity, shopItem.getSellPrice(player, itemInHand, quantity));
            if (!shopItem.isMinSell(quantity)) {
               if (sellPrice >= 0.0D) {
                  if (quantity >= itemInHand.getAmount()) {
                     quantity = itemInHand.getAmount();
                     if (this.plugin.version <= 110) {
                        player.getInventory().removeItem(new ItemStack[]{itemInHand});
                     } else {
                        itemInHand.setAmount(0);
                     }
                  } else {
                     itemInHand.setAmount(itemInHand.getAmount() - quantity);
                  }

                  this.plugin.getEcoHandler().getEcon(shopItem.getEcoType()).depositBalance(player, sellPrice);
                  SendMessage.sendTransactionMessage(player, quantity, sellPrice, shopItem, Transaction.Mode.SELL, Transaction.Type.SELL_ALL_COMMAND);
               } else {
                  result = Transaction.Result.TRANSACTION_CANCELLED;
               }
            } else {
               SendMessage.chatToPlayer(player, Lang.NOT_ENOUGH_ITEMS_TO_SELL.get().replace("%min-sell%", String.valueOf(shopItem.getMinSell())).replace("%quantity%", String.valueOf(quantity)));
               result = Transaction.Result.NOT_ENOUGH_ITEMS;
            }
         } else if (this.plugin.sellShulkers && XMaterial.isShulker(itemInHand.getType())) {
            Map<ShopItem, Sold> CACHE = new HashMap();
            Map<Integer, SmartStack> toRemove = new HashMap();
            Integer sold = this.sellShulker(CACHE, toRemove, player, itemInHand, "sellallhand", player.getInventory().getHeldItemSlot());
            if (sold != null) {
               Map<EcoType, Double> prices = this.getPrices(CACHE);
               Map<ShopItem, Integer> items = this.getItems(CACHE);
               prices = this.callPreTransactionEvent(items, prices, player, sold);
               if (prices != null) {
                  this.removeItems(player.getInventory(), toRemove);
                  prices.forEach((type, price) -> {
                     this.plugin.getEcoHandler().getEcon(type).depositBalance(player, price);
                  });
                  SendMessage.sendTransactionMessage(player, sold, prices, items, Transaction.Type.SELL_ALL_COMMAND);
               } else {
                  result = Transaction.Result.TRANSACTION_CANCELLED;
               }
            } else {
               result = Transaction.Result.NO_ITEMS_FOUND;
               SendMessage.chatToPlayer(player, Lang.NO_ITEM_FOUND.get());
            }
         } else {
            result = Transaction.Result.NO_ITEMS_FOUND;
            SendMessage.chatToPlayer(player, Lang.NO_ITEM_FOUND.get());
         }
      } else {
         result = Transaction.Result.NO_ITEMS_FOUND;
         SendMessage.chatToPlayer(player, Lang.CANNOT_SELL_AIR.get());
      }

      Bukkit.getPluginManager().callEvent(new PostTransactionEvent(shopItem, player, quantity, sellPrice, Transaction.Type.SELL_ALL_COMMAND, result));
   }

   private XMaterial getItem(String material, Player player) {
      if (material != null && !material.isEmpty()) {
         Optional<XMaterial> optionalMat = XMaterial.matchXMaterial(material);
         if (!optionalMat.isPresent()) {
            SendMessage.warnMessage(player, (Translatable)Lang.INVALID_ITEM_TYPE.get().replace("%material%", material).replace("%type%", material));
            return null;
         } else {
            XMaterial mat = (XMaterial)optionalMat.get();
            if (!mat.isSupported()) {
               SendMessage.warnMessage(player, (String)(Lang.MATERIAL_NOT_SUPPORTED.get() + " (" + material + ")"));
               return null;
            } else {
               return mat;
            }
         }
      } else {
         SendMessage.warnMessage(player, (String)(Lang.NEED_ITEM_MATERIAL.get() + " (" + material + ")"));
         return null;
      }
   }

   private void removeItems(Player p, XMaterial mat, int amount) {
      Transaction.Result result = Transaction.Result.SUCCESS;
      Map<Integer, SmartStack> toRemove = new HashMap();
      Map<ShopItem, Sold> CACHE = new HashMap();
      int removed = 0;

      for(int i = 0; i < 36; ++i) {
         ItemStack is = p.getInventory().getItem(i);
         if (is != null && !is.getType().equals(Material.AIR) && XMaterial.matchXMaterial(is).equals(mat)) {
            ShopItem shopItem = this.plugin.createItem.matchShopItem(is, p, "sellallitem");
            if (shopItem != null && shopItem.getSellPrice() >= 0.0D) {
               int qty = is.getAmount();
               if (shopItem.isMaxSell(((Sold)CACHE.getOrDefault(shopItem, new Sold())).getAmount() + qty)) {
                  if (((Sold)CACHE.getOrDefault(shopItem, new Sold())).getAmount() >= shopItem.getMaxSell()) {
                     continue;
                  }

                  qty = shopItem.getMaxSell() - ((Sold)CACHE.getOrDefault(shopItem, new Sold())).getAmount();
               }

               if (qty > amount) {
                  qty = amount;
               }

               Sold sold = (Sold)CACHE.computeIfAbsent(shopItem, (k) -> {
                  return new Sold();
               });
               sold.addValues(qty, shopItem.getSellPrice(p, is, qty));
               removed += qty;
               amount -= qty;
               ItemStack itemStack = new ItemStack(is);
               itemStack.setAmount(is.getAmount() - qty);
               toRemove.put(i, new SmartStack(itemStack));
               sold.addIndex(i);
               if (amount <= 0) {
                  break;
               }
            } else if (this.plugin.sellShulkers && XMaterial.isShulker(is.getType())) {
               Integer sold = this.sellShulker(CACHE, toRemove, p, is, "sellallitem", i);
               if (sold != null) {
                  removed += sold;
               }
            }
         }
      }

      Iterator var14 = (new HashSet(CACHE.entrySet())).iterator();

      while(var14.hasNext()) {
         Entry<ShopItem, Sold> sold = (Entry)var14.next();
         if (((ShopItem)sold.getKey()).isMinSell(((Sold)sold.getValue()).getAmount())) {
            removed -= ((Sold)sold.getValue()).getAmount();
            CACHE.remove(sold.getKey());
            Set var10000 = ((Sold)sold.getValue()).getIndexes();
            Objects.requireNonNull(toRemove);
            var10000.forEach(toRemove::remove);
         }
      }

      Map<EcoType, Double> prices = new HashMap();
      Map<ShopItem, Integer> items = new HashMap();
      if (removed > 0) {
         prices = this.callPreTransactionEvent(this.getItems(items, CACHE), this.getPrices((Map)prices, CACHE), p, removed);
         if (prices != null) {
            this.removeItems(p.getInventory(), toRemove);
            ((Map)prices).forEach((type, price) -> {
               this.plugin.getEcoHandler().getEcon(type).depositBalance(p, price);
            });
            if (items.size() == 1) {
               SendMessage.sendTransactionMessage(p, removed, ((Map)prices).values().stream().mapToDouble((d) -> {
                  return d;
               }).sum(), (ShopItem)items.keySet().stream().findFirst().get(), Transaction.Mode.SELL, Transaction.Type.SELL_ALL_COMMAND);
            } else {
               SendMessage.sendTransactionMessage(p, removed, (Map)prices, items, Transaction.Type.SELL_ALL_COMMAND);
            }
         } else {
            result = Transaction.Result.TRANSACTION_CANCELLED;
         }
      } else {
         result = Transaction.Result.NO_ITEMS_FOUND;
         SendMessage.chatToPlayer(p, Lang.NO_ITEM_FOUND.get());
      }

      Bukkit.getPluginManager().callEvent(new PostTransactionEvent(items, (Map)prices, p, removed, Transaction.Type.SELL_ALL_COMMAND, result));
   }

   private Integer sellShulker(Map<ShopItem, Sold> CACHE, Map<Integer, SmartStack> toRemove, Player p, ItemStack box, String perm, int slot) {
      Map<ShopItem, Sold> bCACHE = new HashMap();
      Map<Integer, ItemStack> items = new HashMap();
      int removed = 0;
      if (box.getItemMeta() instanceof BlockStateMeta) {
         BlockStateMeta bm = (BlockStateMeta)box.getItemMeta();
         if (bm.getBlockState() instanceof ShulkerBox) {
            Inventory inv = ((ShulkerBox)bm.getBlockState()).getInventory();
            if (inv.isEmpty()) {
               return null;
            }

            for(int i = 0; i < 27; ++i) {
               ItemStack is = inv.getItem(i);
               if (is != null && !is.getType().equals(Material.AIR)) {
                  ShopItem shopItem = this.plugin.createItem.matchShopItem(is, p, perm);
                  if (shopItem != null && shopItem.getSellPrice() >= 0.0D) {
                     int qty = is.getAmount();
                     if (shopItem.isMaxSell(((Sold)bCACHE.getOrDefault(shopItem, new Sold())).getAmount() + ((Sold)CACHE.getOrDefault(shopItem, new Sold())).getAmount() + qty)) {
                        if (((Sold)bCACHE.getOrDefault(shopItem, new Sold())).getAmount() + ((Sold)CACHE.getOrDefault(shopItem, new Sold())).getAmount() >= shopItem.getMaxSell()) {
                           continue;
                        }

                        qty = shopItem.getMaxSell() - ((Sold)bCACHE.getOrDefault(shopItem, new Sold())).getAmount();
                     }

                     Sold sold = (Sold)bCACHE.computeIfAbsent(shopItem, (k) -> {
                        return new Sold();
                     });
                     sold.addValues(qty, shopItem.getSellPrice(p, is, qty));
                     removed += qty;
                     ItemStack stack = new ItemStack(is);
                     stack.setAmount(is.getAmount() - qty);
                     items.put(i, stack);
                     sold.addIndex(i);
                  }
               }
            }

            Iterator var18 = (new HashSet(bCACHE.entrySet())).iterator();

            while(var18.hasNext()) {
               Entry<ShopItem, Sold> sold = (Entry)var18.next();
               if (((ShopItem)sold.getKey()).isMinSell(((Sold)sold.getValue()).getAmount())) {
                  bCACHE.remove(sold.getKey());
                  removed -= ((Sold)sold.getValue()).getAmount();
                  Set var10000 = ((Sold)sold.getValue()).getIndexes();
                  Objects.requireNonNull(items);
                  var10000.forEach(items::remove);
               }
            }

            if (!bCACHE.isEmpty()) {
               toRemove.put(slot, new SmartStack.Shulker(box, items));
               bCACHE.forEach((item, soldx) -> {
                  CACHE.merge(item, soldx, Sold::addValue);
               });
               return removed;
            }
         }
      }

      return null;
   }

   private void removeItems(Inventory inv, Map<Integer, SmartStack> items) {
      items.forEach((i, stack) -> {
         if (stack instanceof SmartStack.Shulker) {
            this.emptyShulker((SmartStack.Shulker)stack);
         } else if (stack.getItem().getAmount() != 0) {
            inv.setItem(i, stack.getItem());
         } else {
            inv.clear(i);
         }

      });
   }

   private void emptyShulker(SmartStack.Shulker shulker) {
      ItemStack sBox = shulker.getItem();
      BlockStateMeta sbm = (BlockStateMeta)sBox.getItemMeta();
      ShulkerBox s = (ShulkerBox)sbm.getBlockState();
      shulker.getItems().forEach((i, item) -> {
         if (item.getAmount() != 0) {
            s.getInventory().setItem(i, item);
         } else {
            s.getInventory().clear(i);
         }

      });
      sbm.setBlockState(s);
      sBox.setItemMeta(sbm);
   }

   private Map<EcoType, Double> getPrices(Map<EcoType, Double> prices, Map<ShopItem, Sold> CACHE) {
      Iterator var3 = CACHE.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<ShopItem, Sold> entry = (Entry)var3.next();
         prices.merge(((ShopItem)entry.getKey()).getEcoType(), ((Sold)entry.getValue()).getPrice(), Double::sum);
      }

      return prices;
   }

   private Map<ShopItem, Integer> getItems(Map<ShopItem, Integer> items, Map<ShopItem, Sold> CACHE) {
      Iterator var3 = CACHE.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<ShopItem, Sold> entry = (Entry)var3.next();
         items.merge((ShopItem)entry.getKey(), ((Sold)entry.getValue()).getAmount(), Integer::sum);
      }

      return items;
   }

   private Map<EcoType, Double> getPrices(Map<ShopItem, Sold> CACHE) {
      Map<EcoType, Double> prices = new HashMap();
      Iterator var3 = CACHE.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<ShopItem, Sold> entry = (Entry)var3.next();
         prices.merge(((ShopItem)entry.getKey()).getEcoType(), ((Sold)entry.getValue()).getPrice(), Double::sum);
      }

      return prices;
   }

   private Map<ShopItem, Integer> getItems(Map<ShopItem, Sold> CACHE) {
      Map<ShopItem, Integer> items = new HashMap();
      Iterator var3 = CACHE.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<ShopItem, Sold> entry = (Entry)var3.next();
         items.merge((ShopItem)entry.getKey(), ((Sold)entry.getValue()).getAmount(), Integer::sum);
      }

      return items;
   }

   private double callPreTransactionEvent(ShopItem shopItem, Player player, int quantity, double sellPrice) {
      PreTransactionEvent preTransactionEvent = new PreTransactionEvent(shopItem, player, quantity, sellPrice, Transaction.Type.SELL_ALL_COMMAND);
      Bukkit.getPluginManager().callEvent(preTransactionEvent);
      sellPrice = preTransactionEvent.getPrice();
      return preTransactionEvent.isCancelled() ? -1.0D : sellPrice;
   }

   private Map<EcoType, Double> callPreTransactionEvent(Map<ShopItem, Integer> items, Map<EcoType, Double> prices, Player player, int amount) {
      PreTransactionEvent preTransactionEvent = new PreTransactionEvent(items, prices, player, amount, Transaction.Type.SELL_ALL_COMMAND);
      Bukkit.getPluginManager().callEvent(preTransactionEvent);
      return preTransactionEvent.isCancelled() ? null : preTransactionEvent.getPrices();
   }

   private String getCommandUsages(Player p) {
      StringBuilder builder = new StringBuilder();
      if (PermissionsCache.hasPermission(p, "EconomyShopGUI.sellall")) {
         builder.append(Lang.SELLALL_INVENTORY_COMMAND_USAGE.get().getLegacy()).append("\n");
      }

      if (PermissionsCache.hasPermission(p, "EconomyShopGUI.sellallhand")) {
         builder.append(Lang.SELLALL_HAND_COMMAND_USAGE.get().getLegacy()).append("\n");
      }

      if (PermissionsCache.hasPermission(p, "EconomyShopGUI.sellallitem")) {
         builder.append(Lang.SELLALL_ITEM_COMMAND_USAGE.get().getLegacy()).append("\n");
      }

      return builder.toString();
   }

   public List<String> tabComplete(CommandSender commandSender, String s, String[] args) {
      if (PermissionsCache.hasPermission(commandSender, "EconomyShopGUI.sellall") && PermissionsCache.hasPermission(commandSender, "EconomyShopGUI.sellallhand") && PermissionsCache.hasPermission(commandSender, "EconomyShopGUI.sellallitem")) {
         if (args.length == 1) {
            List<String> tabCompletions = this.plugin.getSupportedMatNames();
            tabCompletions.add("hand");
            tabCompletions.add("inventory");
            if (!args[0].isEmpty()) {
               List<String> completions = new ArrayList();
               StringUtil.copyPartialMatches(args[0], tabCompletions, completions);
               Collections.sort(completions);
               return completions;
            } else {
               return tabCompletions;
            }
         } else if (args.length == 2) {
            if (!args[1].isEmpty()) {
               List<String> completions = new ArrayList();
               StringUtil.copyPartialMatches(args[1], Arrays.asList("8", "16", "32", "64", "128", "256", "512", "1024"), completions);
               Collections.sort(completions);
               return completions;
            } else {
               return Arrays.asList("8", "16", "32", "64", "128", "256", "512", "1024");
            }
         } else {
            return Collections.emptyList();
         }
      } else {
         return Collections.emptyList();
      }
   }
}
