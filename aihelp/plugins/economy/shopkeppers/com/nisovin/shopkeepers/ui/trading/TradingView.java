package com.nisovin.shopkeepers.ui.trading;

import com.nisovin.shopkeepers.api.events.ShopkeeperTradeCompletedEvent;
import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.currency.Currencies;
import com.nisovin.shopkeepers.currency.Currency;
import com.nisovin.shopkeepers.debug.Debug;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.bukkit.ConfigUtils;
import com.nisovin.shopkeepers.util.bukkit.MerchantUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.InventoryUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Lazy;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.view.MerchantView;
import org.bukkit.inventory.view.builder.MerchantInventoryViewBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TradingView extends View {
   private static final Set<? extends Class<? extends InventoryEvent>> ADDITIONAL_INVENTORY_EVENTS = Collections.singleton(TradeSelectEvent.class);
   protected static final int BUY_ITEM_1_SLOT_ID = 0;
   protected static final int BUY_ITEM_2_SLOT_ID = 1;
   protected static final int RESULT_ITEM_SLOT_ID = 2;

   public TradingView(TradingViewProvider provider, Player player, UIState uiState) {
      super(provider, player, uiState);
   }

   protected TradingViewProvider getTradingViewProvider() {
      return (TradingViewProvider)this.getProvider();
   }

   private final List<TradingListener> getTradingListeners() {
      return this.getTradingViewProvider().getTradingListeners();
   }

   @Nullable
   protected InventoryView openInventoryView() {
      Player player = this.getPlayer();
      Shopkeeper shopkeeper = this.getShopkeeperNonNull();
      String title = this.getInventoryTitle();
      List<? extends TradingRecipe> recipes = shopkeeper.getTradingRecipes(player);
      if (recipes.isEmpty()) {
         this.debugNotOpeningUI(player, "Shopkeeper has no offers.");
         TextUtils.sendMessage(player, (Text)Messages.cannotTradeNoOffers);
         return null;
      } else {
         return this.openTradeWindow(title, recipes);
      }
   }

   @Nullable
   protected InventoryView openTradeWindow(String title, List<? extends TradingRecipe> recipes) {
      Player player = this.getPlayer();
      Merchant merchant = this.setupMerchant(recipes);
      if (Settings.incrementVillagerStatistics) {
         player.incrementStatistic(Statistic.TALKED_TO_VILLAGER);
      }

      MerchantInventoryViewBuilder<MerchantView> menuBuilder = (MerchantInventoryViewBuilder)MenuType.MERCHANT.builder();
      Compat.getProvider().setInventoryViewTitle(menuBuilder, title);
      MerchantView inventoryView = (MerchantView)menuBuilder.merchant(merchant).build(player);
      player.openInventory(inventoryView);
      return inventoryView;
   }

   protected Merchant setupMerchant(List<? extends TradingRecipe> recipes) {
      Merchant merchant = Bukkit.createMerchant();
      this.setupMerchantRecipes(merchant, recipes);
      return merchant;
   }

   protected void setupMerchantRecipes(Merchant merchant, List<? extends TradingRecipe> recipes) {
      List<MerchantRecipe> merchantRecipes = this.createMerchantRecipes(recipes);
      merchant.setRecipes(merchantRecipes);
   }

   protected List<MerchantRecipe> createMerchantRecipes(List<? extends TradingRecipe> recipes) {
      List<MerchantRecipe> merchantRecipes = new ArrayList();
      Iterator var3 = recipes.iterator();

      while(var3.hasNext()) {
         TradingRecipe recipe = (TradingRecipe)var3.next();
         merchantRecipes.add(this.createMerchantRecipe(recipe));
      }

      return merchantRecipes;
   }

   protected MerchantRecipe createMerchantRecipe(TradingRecipe recipe) {
      return MerchantUtils.createMerchantRecipe(recipe);
   }

   protected String getInventoryTitle() {
      String title = this.getShopkeeperNonNull().getName();
      if (title.isEmpty()) {
         title = Messages.tradingTitleDefault;
      }

      return Messages.tradingTitlePrefix + title;
   }

   public void updateInventory() {
      this.updateTrades();
   }

   protected void updateTrades() {
      if (this.isOpen()) {
         Player player = this.getPlayer();
         InventoryView openInventory = player.getOpenInventory();

         assert openInventory.getType() == InventoryType.MERCHANT;

         MerchantInventory merchantInventory = (MerchantInventory)openInventory.getTopInventory();
         Merchant merchant = merchantInventory.getMerchant();
         List<MerchantRecipe> oldMerchantRecipes = merchant.getRecipes();
         Shopkeeper shopkeeper = this.getShopkeeperNonNull();
         List<? extends TradingRecipe> recipes = shopkeeper.getTradingRecipes(player);
         List<MerchantRecipe> newMerchantRecipes = this.createMerchantRecipes(recipes);
         if (MerchantUtils.MERCHANT_RECIPES_IGNORE_USES_EXCEPT_BLOCKED.equals(oldMerchantRecipes, newMerchantRecipes)) {
            Log.debug(() -> {
               String var10000 = this.getContext().getLogPrefix();
               return var10000 + "Trades are still up-to-date for player " + player.getName();
            });
         } else {
            Log.debug(() -> {
               String var10000 = this.getContext().getLogPrefix();
               return var10000 + "Updating trades for player " + player.getName();
            });
            this.ensureNoFewerRecipes(oldMerchantRecipes, newMerchantRecipes);
            merchant.setRecipes(newMerchantRecipes);
            Compat.getProvider().updateTrades(player);
         }
      }
   }

   private void ensureNoFewerRecipes(List<? extends MerchantRecipe> oldMerchantRecipes, List<MerchantRecipe> newMerchantRecipes) {
      int oldRecipeCount = oldMerchantRecipes.size();
      int newRecipeCount = newMerchantRecipes.size();
      if (newRecipeCount < oldRecipeCount) {
         for(int i = 0; i < oldRecipeCount; ++i) {
            MerchantRecipe oldRecipe = (MerchantRecipe)oldMerchantRecipes.get(i);
            MerchantRecipe newRecipe;
            if (i < newRecipeCount) {
               newRecipe = (MerchantRecipe)newMerchantRecipes.get(i);
            } else {
               newRecipe = null;
            }

            if (!MerchantUtils.MERCHANT_RECIPES_EQUAL_ITEMS.equals(oldRecipe, newRecipe)) {
               oldRecipe.setMaxUses(0);
               newMerchantRecipes.add(i, oldRecipe);
               ++newRecipeCount;
               if (newRecipeCount == oldRecipeCount) {
                  break;
               }
            }
         }

         assert newRecipeCount == oldRecipeCount;

      }
   }

   protected Set<? extends Class<? extends InventoryEvent>> getAdditionalInventoryEvents() {
      return ADDITIONAL_INVENTORY_EVENTS;
   }

   protected void onInventoryClose(@Nullable InventoryCloseEvent closeEvent) {
   }

   protected void onInventoryEventEarly(InventoryEvent event) {
      if (event instanceof TradeSelectEvent) {
         TradeSelectEvent tradeSelectEvent = (TradeSelectEvent)event;
         this.getTradingListeners().forEach((listener) -> {
            listener.onTradeSelect(this, tradeSelectEvent);
         });
      }

   }

   private boolean canSlotHoldItemStack(@Nullable @ReadOnly ItemStack slotItem, @ReadOnly ItemStack itemStack) {
      if (ItemUtils.isEmpty(slotItem)) {
         return true;
      } else {
         assert slotItem != null;

         if (!itemStack.isSimilar(slotItem)) {
            return false;
         } else {
            return slotItem.getAmount() + itemStack.getAmount() <= slotItem.getMaxStackSize();
         }
      }
   }

   protected void onInventoryClickLate(InventoryClickEvent clickEvent) {
      this.getTradingListeners().forEach((listener) -> {
         listener.onInventoryClick(this, clickEvent);
      });
      Player player = this.getPlayer();
      Shopkeeper shopkeeper = this.getShopkeeperNonNull();
      if (clickEvent.isCancelled()) {
         Log.debug(() -> {
            String var10000 = this.getContext().getLogPrefix();
            return var10000 + "Some plugin has cancelled the trading UI click of player " + player.getName();
         });
      } else {
         int rawSlot = clickEvent.getRawSlot();
         InventoryAction action = clickEvent.getAction();
         MerchantInventory merchantInventory = (MerchantInventory)clickEvent.getInventory();
         UnmodifiableItemStack resultSlotItem = UnmodifiableItemStack.of(merchantInventory.getItem(2));
         ItemStack cursor = clickEvent.getCursor();
         if (action == InventoryAction.COLLECT_TO_CURSOR && ItemUtils.isSimilar(resultSlotItem, cursor)) {
            Log.debug(() -> {
               String var10000 = this.getContext().getLogPrefix();
               return var10000 + "Prevented unsupported type of trading UI click by player " + player.getName() + ": " + String.valueOf(action);
            });
            clickEvent.setCancelled(true);
            InventoryUtils.updateInventoryLater(player);
         } else if (rawSlot == 2) {
            if (action != InventoryAction.CLONE_STACK) {
               clickEvent.setCancelled(true);
               InventoryUtils.updateInventoryLater(player);
               TradingContext tradingContext = new TradingContext(shopkeeper, clickEvent);
               this.setupTradingContext(tradingContext);
               Trade trade = this.checkForTrade(tradingContext, false);
               if (trade != null) {
                  assert resultSlotItem != null;

                  assert trade.getTradingRecipe().getResultItem().isSimilar(resultSlotItem);

                  PlayerInventory playerInventory = player.getInventory();
                  boolean isCursorEmpty = ItemUtils.isEmpty(cursor);
                  UnmodifiableItemStack resultItem;
                  boolean resultItemEmpty;
                  if (action != InventoryAction.PICKUP_ALL && action != InventoryAction.PICKUP_HALF) {
                     if (action != InventoryAction.DROP_ONE_SLOT && action != InventoryAction.DROP_ALL_SLOT) {
                        if (action == InventoryAction.HOTBAR_SWAP) {
                           int hotbarButton = clickEvent.getHotbarButton();
                           if (hotbarButton >= 0 && hotbarButton <= 8) {
                              if (this.handleTrade(trade)) {
                                 UnmodifiableItemStack resultItem = trade.getTradeEvent().getResultItem();
                                 boolean resultItemEmpty = ItemUtils.isEmpty(resultItem);
                                 if (!resultItemEmpty) {
                                    assert resultItem != null;

                                    if (!ItemUtils.isEmpty(playerInventory.getItem(hotbarButton))) {
                                       Log.debug(() -> {
                                          return this.getContext().getLogPrefix() + "Not handling trade: The hotbar slot is not empty.";
                                       });
                                       this.onTradeAborted(tradingContext, false);
                                       return;
                                    }
                                 }

                                 if (!this.finalTradePreparation(trade)) {
                                    return;
                                 }

                                 this.preApplyTrade(trade);
                                 if (!resultItemEmpty) {
                                    assert resultItem != null;

                                    playerInventory.setItem(hotbarButton, ItemUtils.asItemStack(resultItem));
                                 }

                                 this.commonApplyTrade(trade);
                              }

                              this.updateTrades();
                           }
                        } else if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                           while(true) {
                              if (this.handleTrade(trade)) {
                                 label179: {
                                    resultItem = trade.getTradeEvent().getResultItem();
                                    resultItemEmpty = ItemUtils.isEmpty(resultItem);
                                    ItemStack[] newPlayerContents = null;
                                    if (!resultItemEmpty) {
                                       assert resultItem != null;

                                       newPlayerContents = playerInventory.getStorageContents();
                                       List<ItemStack> listView = Arrays.asList(newPlayerContents);
                                       List<ItemStack> hotbarView = listView.subList(0, 9);
                                       List<ItemStack> contentsView = listView.subList(9, 36);
                                       Collections.reverse(hotbarView);
                                       Collections.reverse(contentsView);
                                       if (InventoryUtils.addItems(newPlayerContents, resultItem) != 0) {
                                          Log.debug(() -> {
                                             return this.getContext().getLogPrefix() + "Not handling trade: Not enough inventory space.";
                                          });
                                          this.onTradeAborted(tradingContext, false);
                                          break label179;
                                       }

                                       Collections.reverse(hotbarView);
                                       Collections.reverse(contentsView);
                                    }

                                    if (!this.finalTradePreparation(trade)) {
                                       return;
                                    }

                                    this.preApplyTrade(trade);
                                    if (!resultItemEmpty) {
                                       assert newPlayerContents != null;

                                       InventoryUtils.setStorageContents(playerInventory, newPlayerContents);
                                    }

                                    this.commonApplyTrade(trade);
                                    Trade previousTrade = trade;
                                    trade = this.checkForTrade(tradingContext, true);
                                    if (trade != null && trade.getTradingRecipe().equals(previousTrade.getTradingRecipe())) {
                                       continue;
                                    }
                                 }
                              }

                              this.updateTrades();
                              break;
                           }
                        }
                     }
                  } else {
                     if (this.handleTrade(trade)) {
                        resultItem = trade.getTradeEvent().getResultItem();
                        resultItemEmpty = ItemUtils.isEmpty(resultItem);
                        if (!resultItemEmpty) {
                           assert resultItem != null;

                           if (!this.canSlotHoldItemStack(cursor, ItemUtils.asItemStack(resultItem))) {
                              Log.debug(() -> {
                                 return this.getContext().getLogPrefix() + "Not handling trade: The cursor cannot hold the result items.";
                              });
                              this.onTradeAborted(tradingContext, false);
                              return;
                           }
                        }

                        if (!this.finalTradePreparation(trade)) {
                           return;
                        }

                        this.preApplyTrade(trade);
                        if (!resultItemEmpty) {
                           assert resultItem != null;

                           ItemStack resultCursor;
                           if (isCursorEmpty) {
                              resultCursor = ItemUtils.asItemStack(resultItem);
                           } else {
                              resultCursor = ItemUtils.increaseItemAmount(cursor, resultItem.getAmount());
                           }

                           player.setItemOnCursor(resultCursor);
                        }

                        this.commonApplyTrade(trade);
                     }

                     this.updateTrades();
                  }

               }
            }
         }
      }
   }

   private void clearResultSlotForInvalidTrade(MerchantInventory merchantInventory) {
   }

   @Nullable
   private Trade checkForTrade(TradingContext tradingContext, boolean silent) {
      return this.checkForTrade(tradingContext, silent, silent, true);
   }

   @Nullable
   private Trade checkForTrade(TradingContext tradingContext, boolean silent, boolean slientStrictItemComparison, boolean isInTradingContext) {
      tradingContext.startNewTrade();
      Player tradingPlayer = tradingContext.getTradingPlayer();
      MerchantInventory merchantInventory = tradingContext.getMerchantInventory();
      ItemStack offeredItem1 = ItemUtils.getNullIfEmpty(merchantInventory.getItem(0));
      ItemStack offeredItem2 = ItemUtils.getNullIfEmpty(merchantInventory.getItem(1));
      ItemStack resultItem = merchantInventory.getItem(2);
      if (ItemUtils.isEmpty(resultItem)) {
         if (!silent) {
            Log.debug(() -> {
               return this.getContext().getLogPrefix() + "Not handling trade: There is no item in the clicked result slot (no trade available).";
            });
            if (Debug.isDebugging(DebugOptions.emptyTrades)) {
               int selectedRecipeIndex = merchantInventory.getSelectedRecipeIndex();
               Log.debug("Selected trading recipe index: " + selectedRecipeIndex);
               TradingRecipe selectedTradingRecipe = MerchantUtils.getSelectedTradingRecipe(merchantInventory);
               if (selectedTradingRecipe == null) {
                  Log.debug("No trading recipe selected (merchant has no trades).");
               } else {
                  debugLogItemStack("recipeItem1", selectedTradingRecipe.getItem1());
                  debugLogItemStack("recipeItem2", selectedTradingRecipe.getItem2());
                  debugLogItemStack("recipeResultItem", selectedTradingRecipe.getResultItem());
               }

               debugLogItemStack("offeredItem1", offeredItem1);
               debugLogItemStack("offeredItem2", offeredItem2);
            }
         }

         return null;
      } else {
         TradingRecipe tradingRecipe = MerchantUtils.getActiveTradingRecipe(merchantInventory);
         if (tradingRecipe == null) {
            if (!silent) {
               TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeUnexpectedTrade);
               Log.debug(() -> {
                  return this.getContext().getLogPrefix() + "Not handling trade: Could not find the active trading recipe!";
               });
            }

            this.onTradeAborted(tradingContext, silent);
            this.clearResultSlotForInvalidTrade(merchantInventory);
            return null;
         } else {
            UnmodifiableItemStack recipeResultItem = tradingRecipe.getResultItem();
            if (!recipeResultItem.equals(resultItem)) {
               if (!silent) {
                  TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeUnexpectedTrade);
                  if (Debug.isDebugging()) {
                     Log.debug(this.getContext().getLogPrefix() + "Not handling trade: The trade result item does not match the expected item of the active trading recipe!");
                     debugLogItemStack("recipeResultItem", recipeResultItem);
                     debugLogItemStack("resultItem", resultItem);
                  }
               }

               this.onTradeAborted(tradingContext, silent);
               this.clearResultSlotForInvalidTrade(merchantInventory);
               return null;
            } else {
               UnmodifiableItemStack requiredItem1 = tradingRecipe.getItem1();
               UnmodifiableItemStack requiredItem2 = tradingRecipe.getItem2();

               assert !ItemUtils.isEmpty(requiredItem1);

               boolean swappedItemOrder = false;
               if (!this.matches(offeredItem1, offeredItem2, requiredItem1, requiredItem2)) {
                  if (!this.matches(offeredItem1, offeredItem2, requiredItem2, requiredItem1)) {
                     if (!silent) {
                        TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeUnexpectedTrade);
                        Log.debug(() -> {
                           return this.getContext().getLogPrefix() + "Not handling trade: Could not match the offered items to the active trading recipe!";
                        });
                     }

                     this.onTradeAborted(tradingContext, silent);
                     this.clearResultSlotForInvalidTrade(merchantInventory);
                     return null;
                  }

                  swappedItemOrder = true;
                  ItemStack temp = offeredItem1;
                  offeredItem1 = offeredItem2;
                  offeredItem2 = temp;
               }

               assert offeredItem1 != null;

               if (Settings.useStrictItemComparison) {
                  boolean item1Similar = ItemUtils.isSimilar(requiredItem1, offeredItem1);
                  Lazy<Boolean> item2Similar = new Lazy(() -> {
                     return ItemUtils.isSimilar(requiredItem2, offeredItem2);
                  });
                  if (!item1Similar || !(Boolean)item2Similar.get()) {
                     if (!slientStrictItemComparison) {
                        TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeItemsNotStrictlyMatching);
                        if (Debug.isDebugging()) {
                           String errorMsg = "The offered items do not strictly match the required items.";
                           if (isInTradingContext) {
                              this.debugPreventedTrade(errorMsg);
                           } else {
                              String var10000 = this.getContext().getLogPrefix();
                              Log.debug(var10000 + errorMsg);
                           }

                           Log.debug("Active trading recipe: " + ItemUtils.getSimpleRecipeInfo(tradingRecipe));
                           if (!item1Similar) {
                              debugLogItemStack("requiredItem1", requiredItem1);
                              debugLogItemStack("offeredItem1", offeredItem1);
                           }

                           if (!(Boolean)item2Similar.get()) {
                              debugLogItemStack("requiredItem2", requiredItem2);
                              debugLogItemStack("offeredItem2", offeredItem2);
                           }
                        }
                     }

                     this.onTradeAborted(tradingContext, slientStrictItemComparison);
                     this.clearResultSlotForInvalidTrade(merchantInventory);
                     return null;
                  }
               }

               Trade trade = new Trade(tradingContext, tradingContext.getTradeCount(), tradingRecipe, offeredItem1, offeredItem2, swappedItemOrder);
               this.setupTrade(trade);
               tradingContext.setCurrentTrade(trade);
               return trade;
            }
         }
      }
   }

   private boolean matches(@Nullable ItemStack offeredItem1, @Nullable ItemStack offeredItem2, @Nullable UnmodifiableItemStack requiredItem1, @Nullable UnmodifiableItemStack requiredItem2) {
      int offeredItem1Amount = ItemUtils.getItemStackAmount(offeredItem1);
      int offeredItem2Amount = ItemUtils.getItemStackAmount(offeredItem2);
      int requiredItem1Amount = ItemUtils.getItemStackAmount(requiredItem1);
      int requiredItem2Amount = ItemUtils.getItemStackAmount(requiredItem2);
      return offeredItem1Amount >= requiredItem1Amount && offeredItem2Amount >= requiredItem2Amount && Compat.getProvider().matches(offeredItem1, requiredItem1) && Compat.getProvider().matches(offeredItem2, requiredItem2);
   }

   protected final void debugPreventedTrade(String reason) {
      Log.debug(() -> {
         String var10000 = this.getContext().getLogPrefix();
         return var10000 + "Prevented trade by " + this.getPlayer().getName() + ": " + reason;
      });
   }

   protected void setupTradingContext(TradingContext tradingContext) {
   }

   protected void setupTrade(Trade trade) {
   }

   private boolean handleTrade(Trade trade) {
      assert trade != null;

      if (!this.prepareTrade(trade)) {
         this.onTradeAborted(trade.getTradingContext(), false);
         return false;
      } else {
         Player tradingPlayer = trade.getTradingPlayer();
         ShopkeeperTradeEvent tradeEvent = trade.callTradeEvent();
         this.onPostTradeEvent(trade);
         if (tradeEvent.isCancelled()) {
            Log.debug(() -> {
               String var10000 = this.getContext().getLogPrefix();
               return var10000 + "Some plugin cancelled the trade event of player " + tradingPlayer.getName();
            });
            this.onTradeAborted(trade.getTradingContext(), false);
            return false;
         } else {
            InventoryClickEvent clickEvent = trade.getInventoryClickEvent();
            if (!clickEvent.isCancelled()) {
               Log.warning(this.getContext().getLogPrefix() + "Some plugin tried to uncancel the inventory click event of the trade event!");
               clickEvent.setCancelled(true);
            }

            if (tradeEvent.isResultItemAltered()) {
               Log.debug(() -> {
                  return this.getContext().getLogPrefix() + "Some plugin altered the result item.";
               });
            }

            if (tradeEvent.isReceivedItem1Altered()) {
               Log.debug(() -> {
                  return this.getContext().getLogPrefix() + "Some plugin altered the first received item.";
               });
            }

            if (tradeEvent.isReceivedItem2Altered()) {
               Log.debug(() -> {
                  return this.getContext().getLogPrefix() + "Some plugin altered the second received item.";
               });
            }

            return true;
         }
      }
   }

   private void commonApplyTrade(Trade trade) {
      MerchantInventory merchantInventory = trade.getMerchantInventory();
      merchantInventory.setItem(2, (ItemStack)null);
      TradingRecipe tradingRecipe = trade.getTradingRecipe();
      ItemStack newOfferedItem1 = ItemUtils.decreaseItemAmount(trade.getOfferedItem1(), ItemUtils.getItemStackAmount(tradingRecipe.getItem1()));
      ItemStack newOfferedItem2 = ItemUtils.decreaseItemAmount(trade.getOfferedItem2(), ItemUtils.getItemStackAmount(tradingRecipe.getItem2()));
      boolean itemOrderSwapped = trade.isItemOrderSwapped();
      merchantInventory.setItem(itemOrderSwapped ? 1 : 0, newOfferedItem1);
      merchantInventory.setItem(itemOrderSwapped ? 0 : 1, newOfferedItem2);
      Player player = trade.getTradingPlayer();
      if (Settings.incrementVillagerStatistics) {
         player.incrementStatistic(Statistic.TRADED_WITH_VILLAGER);
      }

      this.onTradeApplied(trade);
      ShopkeeperTradeEvent tradeEvent = trade.getTradeEvent();
      tradeEvent.getTradeEffects().forEach((tradeEffect) -> {
         tradeEffect.onTradeApplied(tradeEvent);
      });
      ShopkeeperTradeCompletedEvent tradeCompletedEvent = new ShopkeeperTradeCompletedEvent(tradeEvent);
      Bukkit.getPluginManager().callEvent(tradeCompletedEvent);
      boolean silent = trade.getTradeNumber() > 1;
      if (!silent) {
         Settings.tradeSucceededSound.play(player);
      }

      this.getTradingListeners().forEach((listener) -> {
         listener.onTradeCompleted(trade, silent);
      });
      Log.debug(() -> {
         String var10000 = trade.getShopkeeper().getLogPrefix();
         return var10000 + "Trade (#" + trade.getTradeNumber() + ") by " + player.getName() + ": " + ItemUtils.getSimpleRecipeInfo(tradingRecipe);
      });
      this.onTradeCompleted(trade);
      this.onTradeOver(trade.getTradingContext());
   }

   protected boolean prepareTrade(Trade trade) {
      return true;
   }

   protected void onPostTradeEvent(Trade trade) {
   }

   protected boolean finalTradePreparation(Trade trade) {
      return true;
   }

   protected void onTradeAborted(TradingContext tradingContext, boolean silent) {
      Trade trade = tradingContext.getCurrentTrade();
      if (trade != null && trade.isTradeEventCalled()) {
         ShopkeeperTradeEvent tradeEvent = trade.getTradeEvent();
         tradeEvent.getTradeEffects().forEach((tradeEffect) -> {
            tradeEffect.onTradeAborted(tradeEvent);
         });
      }

      this.getTradingListeners().forEach((listener) -> {
         listener.onTradeAborted(tradingContext, silent);
      });
      if (!silent && tradingContext.getTradeCount() == 1) {
         Settings.tradeFailedSound.play(tradingContext.getTradingPlayer());
      }

      this.onTradeOver(tradingContext);
   }

   protected void preApplyTrade(Trade trade) {
   }

   protected void onTradeApplied(Trade trade) {
   }

   protected void onTradeCompleted(Trade trade) {
   }

   protected void onTradeOver(TradingContext tradingContext) {
   }

   private static void debugLogItemStack(String itemStackName, @Nullable UnmodifiableItemStack itemStack) {
      debugLogItemStack(itemStackName, ItemUtils.asItemStackOrNull(itemStack));
   }

   private static void debugLogItemStack(String itemStackName, @Nullable @ReadOnly ItemStack itemStack) {
      Object itemStackData = itemStack != null ? itemStack : "<empty>";
      Log.debug(ConfigUtils.toConfigYamlWithoutTrailingNewline(itemStackName, itemStackData));
   }

   protected int getAmountAfterTaxes(int amount) {
      assert amount >= 0;

      if (Settings.taxRate == 0) {
         return amount;
      } else {
         int taxes;
         if (Settings.taxRoundUp) {
            taxes = (int)Math.ceil((double)amount * ((double)Settings.taxRate / 100.0D));
         } else {
            taxes = (int)Math.floor((double)amount * ((double)Settings.taxRate / 100.0D));
         }

         return Math.max(0, Math.min(amount - taxes, amount));
      }
   }

   protected int addReceivedItem(@ReadWrite ItemStack[] contents, @Nullable UnmodifiableItemStack receivedItem) {
      if (ItemUtils.isEmpty(receivedItem)) {
         return 0;
      } else {
         assert receivedItem != null;

         int amountAfterTaxes = this.getAmountAfterTaxes(receivedItem.getAmount());
         return amountAfterTaxes <= 0 ? 0 : InventoryUtils.addItems(contents, receivedItem, amountAfterTaxes);
      }
   }

   protected int addCurrencyItems(@ReadWrite ItemStack[] contents, int amount) {
      if (amount <= 0) {
         return 0;
      } else {
         int remaining = amount;
         if (Currencies.isHighCurrencyEnabled() && amount > Settings.highCurrencyMinCost) {
            Currency highCurrency = Currencies.getHigh();
            int highCurrencyAmount = amount / highCurrency.getValue();
            if (highCurrencyAmount > 0) {
               ItemStack currencyItems = Currencies.getHigh().getItemData().createItemStack(highCurrencyAmount);
               int remainingHighCurrency = InventoryUtils.addItems(contents, currencyItems);

               assert remainingHighCurrency >= 0 && remainingHighCurrency <= highCurrencyAmount;

               remaining = amount - (highCurrencyAmount - remainingHighCurrency) * highCurrency.getValue();

               assert remaining >= 0;

               if (remaining <= 0) {
                  return 0;
               }
            }
         }

         ItemStack currencyItems = Currencies.getBase().getItemData().createItemStack(remaining);
         return InventoryUtils.addItems(contents, currencyItems);
      }
   }
}
