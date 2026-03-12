package me.gypopo.economyshopgui.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.api.events.PostTransactionEvent;
import me.gypopo.economyshopgui.api.events.PreTransactionEvent;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.ActionItem;
import me.gypopo.economyshopgui.objects.DisplayItem;
import me.gypopo.economyshopgui.objects.MainMenu;
import me.gypopo.economyshopgui.objects.SellGUI;
import me.gypopo.economyshopgui.objects.ShopInventory;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.objects.ShopPage;
import me.gypopo.economyshopgui.objects.TransactionItem;
import me.gypopo.economyshopgui.objects.TransactionMenu;
import me.gypopo.economyshopgui.objects.TransactionMenus;
import me.gypopo.economyshopgui.objects.mappings.ClickAction;
import me.gypopo.economyshopgui.objects.shops.ShopSection;
import me.gypopo.economyshopgui.providers.UserManager;
import me.gypopo.economyshopgui.util.BackButton;
import me.gypopo.economyshopgui.util.HolderUtil;
import me.gypopo.economyshopgui.util.PermissionsCache;
import me.gypopo.economyshopgui.util.Transaction;
import me.gypopo.economyshopgui.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class MenuHandler implements Listener {
   private final EconomyShopGUI plugin;
   public static final boolean backOnClose = ConfigManager.getConfig().getBoolean("escape-back");

   public MenuHandler(EconomyShopGUI plugin) {
      this.plugin = plugin;
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onMenuClick(InventoryClickEvent e) {
      Player player = (Player)e.getWhoClicked();
      if (e.getInventory().getHolder() instanceof ShopInventory) {
         if (!(e.getInventory().getHolder() instanceof SellGUI)) {
            e.setCancelled(true);
         }

         if (e.getClick() != ClickType.DOUBLE_CLICK) {
            if (e.getClickedInventory() != null && !e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
               if (e.getCurrentItem() != null && e.getCurrentItem().getType() != XMaterial.AIR.parseMaterial()) {
                  Inventory inventory = e.getInventory();
                  int slot = e.getSlot();
                  InventoryHolder holder = HolderUtil.getHolder(inventory);
                  if (holder instanceof ShopPage) {
                     ShopPage page = (ShopPage)holder;
                     if (this.plugin.navBar.isEnabled(page.getSection()) && slot >= inventory.getSize() - 9 && slot <= inventory.getSize() - 1) {
                        this.plugin.navBar.execute(player, page, slot, page.getPage(), page.isDisabledBackButton());
                     } else {
                        DisplayItem displayItem = this.plugin.getDisplayItem(page.getSection(), page.getItem(slot));
                        if (displayItem == null) {
                           return;
                        }

                        ClickAction action = this.plugin.getSection(page.getSection()).getClickAction(e.getClick());
                        if (action == null) {
                           return;
                        }

                        if (!displayItem.hasItemError()) {
                           if (!this.isAllowedGamemode(player)) {
                              return;
                           }

                           if (displayItem.meetsRequirements(player, false)) {
                              if (displayItem instanceof ActionItem) {
                                 this.plugin.navBar.execute(player, page, ((ActionItem)displayItem).getAction(), page.getPage(), page.isDisabledBackButton());
                                 return;
                              }

                              ShopItem shopItem = (ShopItem)displayItem;
                              if (shopItem.isLinked()) {
                                 if (PermissionsCache.hasPermission(player, "EconomyShopGUI.shop." + shopItem.getSubSection().toLowerCase())) {
                                    this.plugin.getSection(shopItem.getSubSection()).openShopSection(player, page.isDisabledBackButton(), shopItem.section);
                                    return;
                                 }

                                 SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS_TO_OPEN_SHOP.get());
                              }

                              if (shopItem.isDisplayItem()) {
                                 return;
                              }

                              if (action == ClickAction.BUY) {
                                 if (shopItem.getBuyPrice() >= 0.0D) {
                                    if (shopItem.getMaxBuy() == -1) {
                                       this.purchaseItem(player, shopItem);
                                       inventory.setItem(e.getSlot(), this.plugin.getSection(shopItem.section).updateItem(player, shopItem));
                                    } else {
                                       (new TransactionMenu(player, shopItem, page.getRootSection(), page.isDisabledBackButton(), Transaction.Mode.BUY, Transaction.Type.BUY_SCREEN, 1)).open();
                                    }
                                 }
                              } else if (action == ClickAction.SELL_ALL) {
                                 if (shopItem.getSellPrice() < 0.0D || !this.plugin.MMB) {
                                    return;
                                 }

                                 this.sellItem(player, shopItem, Integer.MAX_VALUE);
                              } else if (action == ClickAction.SELL && shopItem.getSellPrice() >= 0.0D) {
                                 if (shopItem.getMaxSell() == -1) {
                                    this.sellItem(player, shopItem, shopItem.getStackSize());
                                    inventory.setItem(e.getSlot(), this.plugin.getSection(shopItem.section).updateItem(player, shopItem));
                                 } else {
                                    (new TransactionMenu(player, shopItem, page.getRootSection(), page.isDisabledBackButton(), Transaction.Mode.SELL, Transaction.Type.SELL_SCREEN, 1)).open();
                                 }
                              }
                           }
                        } else {
                           SendMessage.chatToPlayer(player, Lang.ITEM_ERROR.get());
                        }
                     }
                  } else if (holder instanceof TransactionMenu) {
                     TransactionMenu menu = (TransactionMenu)holder;
                     if (slot >= inventory.getSize() - 9 && slot <= inventory.getSize() - 1) {
                        switch(menu.getTransactionType()) {
                        case SHOPSTAND_BUY_SCREEN:
                        case SHOPSTAND_SELL_SCREEN:
                           if (this.plugin.navBar.isEnableShopStandsNav()) {
                              this.plugin.navBar.execute(player, menu.getShopItem(), slot - (inventory.getSize() - 9), menu.getTransactionMode(), menu.getAmount());
                           }
                           break;
                        default:
                           if (this.plugin.navBar.isEnableTransactionNav()) {
                              this.plugin.navBar.execute(player, menu.getShopItem(), slot - (inventory.getSize() - 9), menu.getRootSection(), menu.isDisabledBackButton(), menu.getTransactionMode(), menu.getTransactionType(), menu.getAmount());
                           }
                        }
                     }

                     TransactionItem item = TransactionMenus.getItemFromSlot(menu.getTransactionType(), slot);
                     if (item != null) {
                        switch(item.getAction().type) {
                        case NONE:
                           break;
                        case BACK:
                           if (menu.getTransactionType() != Transaction.Type.BUY_STACKS_SCREEN) {
                              ShopSection section = this.plugin.getSection(menu.getShopItem().section);
                              section.openShopSection(player, section.getPageForShopItem(menu.getShopItem().itemLoc), menu.isDisabledBackButton(), menu.getRootSection());
                           } else {
                              (new TransactionMenu(player, menu.getShopItem(), menu.getRootSection(), menu.isDisabledBackButton(), menu.getTransactionMode(), Transaction.Type.BUY_SCREEN, menu.getAmount())).open();
                           }
                           break;
                        case INSTA_BUY:
                        case INSTA_SELL:
                           menu.setAmount(item.getItem().getAmount());
                        case CONFIRM_TRANSACTION:
                           this.completeTransaction(menu, player);
                           break;
                        case OPEN_BUY_STACKS:
                           if (!menu.getShopItem().isMaxBuy(menu.getSelectedItem().getMaxStackSize())) {
                              (new TransactionMenu(player, menu.getShopItem(), menu.getRootSection(), menu.isDisabledBackButton(), Transaction.Mode.BUY, Transaction.Type.BUY_STACKS_SCREEN, menu.getAmount())).open();
                           }
                           break;
                        case SELL_ALL:
                           this.completeSellAll(menu, player);
                           break;
                        default:
                           menu.updateAmount(item.getAction());
                        }
                     }
                  } else if (holder instanceof MainMenu) {
                     if (this.plugin.navBar.isEnableMainNav() && slot >= inventory.getSize() - 9 && slot <= inventory.getSize() - 1) {
                        this.plugin.navBar.execute(holder, player, slot - (inventory.getSize() - 9));
                     } else {
                        String section = this.plugin.getMainMenuSectionForSlot(slot);
                        if (section != null && this.plugin.getSections().containsKey(section)) {
                           if (PermissionsCache.hasPermission(player, "EconomyShopGUI.shop." + section.toLowerCase())) {
                              this.plugin.getSection(section).openShopSection(player, false);
                           } else {
                              SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS_TO_OPEN_SHOP.get());
                           }
                        }
                     }
                  } else if (holder instanceof SellGUI && this.plugin.navBar.isEnableSellGUINav() && slot >= inventory.getSize() - 9 && slot <= inventory.getSize() - 1) {
                     e.setCancelled(true);
                     this.plugin.navBar.execute(holder, player, slot - (inventory.getSize() - 9));
                  }

               }
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   public void onMenuClose(InventoryCloseEvent e) {
      if (e.getInventory().getHolder() instanceof ShopInventory) {
         Player player = (Player)e.getPlayer();
         InventoryHolder holder = e.getInventory().getHolder();
         boolean goBack = backOnClose && !UserManager.getUser(player).isOpenNewGUI();
         this.plugin.runTaskLater(() -> {
            this.closeInventory(player, holder, goBack);
         }, 1L);
      }

   }

   private void closeInventory(Player player, InventoryHolder holder, boolean goBack) {
      if (goBack && holder instanceof ShopPage) {
         ShopPage page = (ShopPage)holder;
         if (!page.isDisabledBackButton() || page.isSubSection() && !BackButton.SUB_SECTIONS) {
            if (page.isSubSection()) {
               this.plugin.getSection(page.getRootSection()).openShopSection(player, page.isDisabledBackButton());
            } else {
               new MainMenu(player);
            }
         }
      } else if (goBack && holder instanceof TransactionMenu) {
         TransactionMenu menu = (TransactionMenu)holder;
         if (menu.getTransactionType() != Transaction.Type.BUY_STACKS_SCREEN) {
            ShopSection section = this.plugin.getSection(menu.getShopItem().section);
            section.openShopSection(player, section.getPageForShopItem(menu.getShopItem().itemLoc), menu.isDisabledBackButton());
         } else {
            (new TransactionMenu(player, menu.getShopItem(), menu.getRootSection(), menu.isDisabledBackButton(), Transaction.Mode.getFromType(menu.getTransactionType()), Transaction.Type.BUY_SCREEN, menu.getAmount())).open();
         }
      } else if (holder instanceof SellGUI) {
         ((SellGUI)holder).sellItems(player);
      }

      player.updateInventory();
   }

   private void close(Player player) {
      UserManager.getUser(player).setOpenNewGUI(true);
      player.closeInventory();
   }

   private boolean isAllowedGamemode(Player player) {
      if (this.plugin.bannedGamemodes.contains(player.getGameMode()) && !PermissionsCache.hasPermission(player, "EconomyShopGUI.bypassgamemode")) {
         SendMessage.chatToPlayer(player, Lang.CANNOT_ENTER_SHOP_BANNED_GAMEMODE.get().replace("%gamemode%", player.getGameMode().name().toLowerCase()));
         return false;
      } else {
         return true;
      }
   }

   private void completeTransaction(TransactionMenu menu, Player player) {
      Transaction.Result result = Transaction.Result.SUCCESS;
      double price = 0.0D;
      int amount = 0;
      PreTransactionEvent preTransactionEvent;
      if (menu.getTransactionMode() != Transaction.Mode.SELL) {
         if (menu.getBasePrice() >= 0.0D) {
            int freeSpace = menu.getFreeSpace();
            if (freeSpace != 0) {
               amount = menu.getAmountToSell(freeSpace);
               preTransactionEvent = new PreTransactionEvent(menu.getShopItem(), player, amount, menu.getPrice(amount), menu.getTransactionType());
               Bukkit.getPluginManager().callEvent(preTransactionEvent);
               price = preTransactionEvent.getPrice();
               if (!preTransactionEvent.isCancelled()) {
                  if (this.plugin.getEcoHandler().getEcon(menu.getShopItem().getEcoType()).getBalance(player) >= price) {
                     this.plugin.getEcoHandler().getEcon(menu.getShopItem().getEcoType()).withdrawBalance(player, price);
                     menu.sendTransactionCompleteMessage(price);
                     menu.addItemStacks();
                     if (menu.getShopItem().isCloseMenu()) {
                        this.close(player);
                     } else {
                        menu.open();
                     }
                  } else {
                     SendMessage.chatToPlayer(player, Lang.INSUFFICIENT_MONEY.get().replace("%currency%", this.plugin.getEcoHandler().getEcon(menu.getShopItem().getEcoType()).getFriendly()));
                     result = Transaction.Result.INSUFFICIENT_FUNDS;
                  }
               } else {
                  result = Transaction.Result.TRANSACTION_CANCELLED;
               }
            } else {
               SendMessage.chatToPlayer(player, Lang.MORE_SPACE_NEEDED.get());
               result = Transaction.Result.NO_INVENTORY_SPACE;
            }
         } else {
            SendMessage.chatToPlayer(player, Lang.CANNOT_PURCHASE_ITEM.get());
            result = Transaction.Result.NEGATIVE_ITEM_PRICE;
         }
      } else {
         Double sellPrice = menu.sellAllItems(menu.getAmount());
         if (sellPrice != null) {
            amount = menu.getTotalAmount();
            preTransactionEvent = new PreTransactionEvent(menu.getShopItem(), player, amount, sellPrice, menu.getTransactionType());
            Bukkit.getPluginManager().callEvent(preTransactionEvent);
            price = preTransactionEvent.getPrice();
            if (!preTransactionEvent.isCancelled()) {
               if (price >= 0.0D) {
                  this.plugin.getEcoHandler().getEcon(menu.getShopItem().getEcoType()).depositBalance(player, price);
                  menu.sendTransactionCompleteMessage(price);
                  menu.emptyCache();
                  if (menu.getShopItem().isCloseMenu()) {
                     this.close(player);
                  } else {
                     menu.open();
                  }
               } else {
                  SendMessage.chatToPlayer(player, Lang.ITEM_CANNOT_BE_SOLD.get());
                  result = Transaction.Result.NEGATIVE_ITEM_PRICE;
               }
            } else {
               result = Transaction.Result.TRANSACTION_CANCELLED;
            }
         } else {
            SendMessage.chatToPlayer(player, Lang.NO_ITEM_FOUND.get());
            result = Transaction.Result.NO_ITEMS_FOUND;
         }
      }

      Bukkit.getPluginManager().callEvent(new PostTransactionEvent(menu.getShopItem(), player, amount, price, menu.getTransactionType(), result));
   }

   private void completeSellAll(TransactionMenu menu, Player player) {
      Transaction.Result result = Transaction.Result.SUCCESS;
      Double sellPrice = menu.sellAllItems(Integer.MAX_VALUE);
      if (sellPrice != null) {
         PreTransactionEvent preTransactionEvent = new PreTransactionEvent(menu.getShopItem(), player, menu.getAmount(), sellPrice, menu.getTransactionType());
         Bukkit.getPluginManager().callEvent(preTransactionEvent);
         sellPrice = preTransactionEvent.getPrice();
         if (!preTransactionEvent.isCancelled()) {
            if (sellPrice >= 0.0D) {
               this.plugin.getEcoHandler().getEcon(menu.getShopItem().getEcoType()).depositBalance(player, sellPrice);
               menu.sendTransactionCompleteMessage(sellPrice);
               menu.emptyCache();
               if (menu.getShopItem().isCloseMenu()) {
                  this.close(player);
               } else {
                  menu.open();
               }
            } else {
               result = Transaction.Result.NEGATIVE_ITEM_PRICE;
            }
         } else {
            result = Transaction.Result.TRANSACTION_CANCELLED;
         }
      } else {
         SendMessage.chatToPlayer(player, Lang.NO_ITEM_TO_BE_SOLD.get());
         result = Transaction.Result.NO_ITEMS_FOUND;
         sellPrice = 0.0D;
      }

      Bukkit.getPluginManager().callEvent(new PostTransactionEvent(menu.getShopItem(), player, menu.getAmount(), sellPrice, menu.getTransactionType(), result));
   }

   private void purchaseItem(Player p, ShopItem shopItem) {
      int amount = shopItem.getStackSize();
      double price = 0.0D;
      if (!this.canBuy(p, shopItem)) {
         SendMessage.chatToPlayer(p, Lang.MORE_SPACE_NEEDED.get());
         this.callPost(shopItem, p, amount, price, Transaction.Type.QUICK_BUY, Transaction.Result.NO_INVENTORY_SPACE);
      } else {
         amount = this.getFinalAmount(shopItem, p);
         if (shopItem.isMinBuy(amount)) {
            SendMessage.chatToPlayer(p, Lang.NOT_ENOUGH_ITEMS_TO_BUY.get().replace("%min-buy%", String.valueOf(shopItem.getMinBuy())));
            this.callPost(shopItem, p, amount, price, Transaction.Type.QUICK_BUY, Transaction.Result.NOT_ENOUGH_ITEMS);
         } else {
            PreTransactionEvent preTransactionEvent = new PreTransactionEvent(Collections.singletonMap(shopItem, amount), Collections.singletonMap(shopItem.getEcoType(), shopItem.getBuyPrice(p) * (double)amount), p, amount, Transaction.Type.QUICK_BUY);
            Bukkit.getPluginManager().callEvent(preTransactionEvent);
            price = preTransactionEvent.getPrice();
            if (preTransactionEvent.isCancelled()) {
               this.callPost(shopItem, p, amount, price, Transaction.Type.QUICK_BUY, Transaction.Result.TRANSACTION_CANCELLED);
            } else if (this.plugin.getEcoHandler().getEcon(shopItem.getEcoType()).getBalance(p) < price) {
               SendMessage.chatToPlayer(p, Lang.INSUFFICIENT_MONEY.get().replace("%currency%", this.plugin.getEcoHandler().getEcon(shopItem.getEcoType()).getFriendly()));
               this.callPost(shopItem, p, amount, price, Transaction.Type.QUICK_BUY, Transaction.Result.INSUFFICIENT_FUNDS);
            } else {
               this.plugin.getEcoHandler().getEcon(shopItem.getEcoType()).withdrawBalance(p, price);
               ItemStack stack = new ItemStack(shopItem.getItemToGive());
               stack.setAmount(amount);
               p.getInventory().addItem(new ItemStack[]{stack});
               SendMessage.sendTransactionMessage(p, amount, price, shopItem, Transaction.Mode.BUY, Transaction.Type.QUICK_BUY);
               if (shopItem.isCloseMenu()) {
                  p.closeInventory();
               } else {
                  this.callPost(shopItem, p, amount, price, Transaction.Type.QUICK_BUY, Transaction.Result.SUCCESS);
               }
            }
         }
      }
   }

   private void sellItem(Player p, ShopItem shopItem, int amount) {
      double price = 0.0D;
      if (!this.canSell(p, shopItem)) {
         SendMessage.chatToPlayer(p, Lang.NO_ITEM_FOUND.get());
         this.callPost(shopItem, p, amount, price, Transaction.Type.QUICK_SELL, Transaction.Result.NO_ITEMS_FOUND);
      } else {
         ArrayList<ItemStack> items = this.getSellAble(p, shopItem, amount);
         amount = items.stream().mapToInt(ItemStack::getAmount).sum();
         if (shopItem.isMinSell(amount)) {
            SendMessage.chatToPlayer(p, Lang.NOT_ENOUGH_ITEMS_TO_SELL.get().replace("%min-sell%", String.valueOf(shopItem.getMinSell())).replace("%quantity%", String.valueOf(amount)));
            this.callPost(shopItem, p, amount, price, Transaction.Type.QUICK_SELL, Transaction.Result.NOT_ENOUGH_ITEMS);
         } else {
            price = items.stream().mapToDouble((i) -> {
               return shopItem.getSellPrice(p, i);
            }).sum();
            PreTransactionEvent preTransactionEvent = new PreTransactionEvent(shopItem, p, amount, price, Transaction.Type.QUICK_SELL);
            Bukkit.getPluginManager().callEvent(preTransactionEvent);
            price = preTransactionEvent.getPrice();
            if (preTransactionEvent.isCancelled()) {
               this.callPost(shopItem, p, amount, price, Transaction.Type.QUICK_SELL, Transaction.Result.TRANSACTION_CANCELLED);
            } else {
               this.plugin.getEcoHandler().getEcon(shopItem.getEcoType()).depositBalance(p, price);
               items.forEach((item) -> {
                  p.getInventory().removeItem(new ItemStack[]{item});
               });
               SendMessage.sendTransactionMessage(p, amount, price, shopItem, Transaction.Mode.SELL, Transaction.Type.QUICK_SELL);
               if (shopItem.isCloseMenu()) {
                  p.closeInventory();
               } else {
                  this.callPost(shopItem, p, amount, price, Transaction.Type.QUICK_SELL, Transaction.Result.SUCCESS);
               }
            }
         }
      }
   }

   private void callPost(ShopItem shopItem, Player p, int amount, double price, Transaction.Type type, Transaction.Result result) {
      Bukkit.getPluginManager().callEvent(new PostTransactionEvent(shopItem, p, amount, price, type, result));
   }

   private ArrayList<ItemStack> getSellAble(Player p, ShopItem shopItem, int limit) {
      if (shopItem.isMaxSell(limit)) {
         limit = shopItem.getMaxSell();
      }

      ArrayList<ItemStack> items = new ArrayList();

      for(int i = 0; i < 36; ++i) {
         ItemStack itemInInv = p.getInventory().getItem(i);
         if (itemInInv != null && !itemInInv.getType().equals(Material.AIR) && shopItem.match(itemInInv)) {
            if (limit < itemInInv.getAmount()) {
               ItemStack item = new ItemStack(itemInInv);
               item.setAmount(limit);
               items.add(item);
               break;
            }

            items.add(itemInInv);
            limit -= itemInInv.getAmount();
         }
      }

      return items;
   }

   private boolean canSell(Player p, ShopItem shopItem) {
      for(int i = 0; i < 36; ++i) {
         ItemStack itemInInv = p.getInventory().getItem(i);
         if (itemInInv != null && !itemInInv.getType().equals(Material.AIR) && shopItem.match(itemInInv)) {
            return true;
         }
      }

      return false;
   }

   private boolean canBuy(Player p, ShopItem shopItem) {
      return p.getInventory().firstEmpty() != -1 || Arrays.stream(p.getInventory().getContents()).anyMatch((stack) -> {
         return stack != null && stack.isSimilar(shopItem.getItemToGive()) && stack.getAmount() < stack.getMaxStackSize();
      });
   }

   private int getFinalAmount(ShopItem shopItem, Player p) {
      int freeSpace = 0;
      int maxStackSize = shopItem.getItemToGive().getMaxStackSize();
      List<ItemStack> items = Arrays.asList(p.getInventory().getContents());

      for(int i = 0; i < 36; ++i) {
         ItemStack item = (ItemStack)items.get(i);
         if (item != null && !item.getType().equals(Material.AIR)) {
            if (item.isSimilar(shopItem.getItemToGive()) && item.getAmount() < maxStackSize) {
               freeSpace += maxStackSize - item.getAmount();
            }
         } else {
            freeSpace += maxStackSize;
         }
      }

      if (shopItem.isMaxBuy(freeSpace)) {
         freeSpace = shopItem.getMaxBuy();
      }

      return shopItem.getStackSize() > freeSpace && !this.plugin.dropItemsOnGround ? freeSpace : shopItem.getStackSize();
   }
}
