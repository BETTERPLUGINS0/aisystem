package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.user.User;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperUUIDArgument;
import com.nisovin.shopkeepers.commands.arguments.TargetShopkeeperArgument;
import com.nisovin.shopkeepers.commands.arguments.UserByNameArgument;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.CommandSourceRejectedException;
import com.nisovin.shopkeepers.commands.lib.arguments.AnyFallbackArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.AnyStringFallback;
import com.nisovin.shopkeepers.commands.lib.arguments.FirstOfArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.LiteralArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.NamedArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PlayerByNameArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PlayerNameArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PlayerUUIDArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PositiveIntegerArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.UserArgumentUtils;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.debug.Debug;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.tradelog.data.PlayerRecord;
import com.nisovin.shopkeepers.tradelog.data.ShopRecord;
import com.nisovin.shopkeepers.tradelog.data.TradeRecord;
import com.nisovin.shopkeepers.tradelog.history.PlayerSelector;
import com.nisovin.shopkeepers.tradelog.history.ShopSelector;
import com.nisovin.shopkeepers.tradelog.history.TradingHistoryProvider;
import com.nisovin.shopkeepers.tradelog.history.TradingHistoryRequest;
import com.nisovin.shopkeepers.tradelog.history.TradingHistoryResult;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Range;
import com.nisovin.shopkeepers.util.java.TimeUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

class CommandHistory extends Command {
   private static final String ARGUMENT_TARGET_PLAYERS = "target-players";
   private static final String ARGUMENT_ALL_PLAYERS = "all-players";
   private static final String ARGUMENT_ALL_PLAYERS_ALIAS = "all";
   private static final String ARGUMENT_SELF = "self";
   private static final String ARGUMENT_PLAYER = "player";
   private static final String ARGUMENT_PLAYER_UUID = "player:uuid";
   private static final String ARGUMENT_PLAYER_NAME = "player:name";
   private static final String ARGUMENT_TARGET_SHOPS = "target-shops";
   private static final String ARGUMENT_OWN = "own";
   private static final String ARGUMENT_ALL_SHOPS = "all-shops";
   private static final String ARGUMENT_ALL_SHOPS_ALIAS = "all";
   private static final String ARGUMENT_ADMIN_SHOPS = "admin";
   private static final String ARGUMENT_PLAYER_SHOPS = "player-shops";
   private static final String ARGUMENT_PLAYER_SHOPS_ALIAS = "player";
   private static final String ARGUMENT_SHOP = "shop";
   private static final String ARGUMENT_SHOP_EXISTING = "shop:shopkeeper";
   private static final String ARGUMENT_SHOP_UUID = "shop:uuid";
   private static final String ARGUMENT_OWNER = "owner";
   private static final String ARGUMENT_OWNER_UUID = "owner:uuid";
   private static final String ARGUMENT_OWNER_NAME = "owner:name";
   private static final UserByNameArgument PLAYER_BY_NAME_ARGUMENT = new UserByNameArgument("player:name");
   private static final UserByNameArgument OWNER_BY_NAME_ARGUMENT = new UserByNameArgument("owner:name");
   private static final String ARGUMENT_PAGE = "page";
   private static final int ENTRIES_PER_PAGE = 10;
   private final SKShopkeepersPlugin plugin;

   CommandHistory(SKShopkeepersPlugin plugin) {
      super("history");
      this.plugin = plugin;
      this.setDescription(Messages.commandDescriptionHistory);
      this.addArgument((new FirstOfArgument("target-players", Arrays.asList(new LiteralArgument("self"), (new LiteralArgument("all-players", Arrays.asList("all"))).setDisplayName("all"), new FirstOfArgument("player", Arrays.asList(new PlayerUUIDArgument("player:uuid"), new AnyStringFallback((new PlayerByNameArgument("player:name")).transformed((player) -> {
         return player.getName();
      }))), false)), true, true)).optional());
      this.addArgument((new FirstOfArgument("target-shops", Arrays.asList((new LiteralArgument("all-shops", Arrays.asList("all"))).setDisplayName("all"), new LiteralArgument("admin"), (new LiteralArgument("player-shops", Arrays.asList("player"))).setDisplayName("player"), new LiteralArgument("own"), new NamedArgument(new FirstOfArgument("owner", Arrays.asList(new PlayerUUIDArgument("owner:uuid"), new PlayerNameArgument("owner:name")), false)), new AnyFallbackArgument(new NamedArgument(new FirstOfArgument("shop", Arrays.asList(new ShopkeeperArgument("shop:shopkeeper"), new ShopkeeperUUIDArgument("shop:uuid")))), new TargetShopkeeperArgument("shop:shopkeeper"))), true, true)).optional());
      this.addArgument((new PositiveIntegerArgument("page")).orDefaultValue(1));
   }

   public boolean testPermission(CommandSender sender) {
      if (!super.testPermission(sender)) {
         return false;
      } else {
         return PermissionUtils.hasPermission(sender, "shopkeeper.history.own") || PermissionUtils.hasPermission(sender, "shopkeeper.history.admin");
      }
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      TradingHistoryProvider tradingHistoryProvider = this.plugin.getTradingHistoryProvider();
      if (tradingHistoryProvider == null) {
         TextUtils.sendMessage(sender, Messages.historyDisabled);
      } else {
         Player executingPlayer = sender instanceof Player ? (Player)sender : null;
         Boolean hasAllPerm = null;
         boolean hasTargetPlayers = context.has("target-players");
         boolean allPlayers = context.has("all-players");
         boolean self = context.has("self");
         UUID playerUUID = (UUID)context.getOrNull("player:uuid");
         String playerName = (String)context.getOrNull("player:name");
         boolean hasTargetShops = context.has("target-shops");
         boolean allShops = context.has("all-shops");
         boolean adminShops = context.has("admin");
         boolean playerShops = context.has("player-shops");
         boolean ownShops = context.has("own");
         Shopkeeper existingShop = (Shopkeeper)context.getOrNull("shop:shopkeeper");
         UUID shopUUID = (UUID)context.getOrNull("shop:uuid");
         UUID ownerUUID = (UUID)context.getOrNull("owner:uuid");
         String ownerName = (String)context.getOrNull("owner:name");
         int page = (Integer)context.get("page");
         if (!hasTargetPlayers) {
            allPlayers = true;
            hasTargetPlayers = true;
            if (!hasTargetShops && executingPlayer != null) {
               ownShops = true;
               hasTargetShops = true;
            }
         }

         assert hasTargetPlayers;

         if (!hasTargetShops) {
            allShops = true;
            hasTargetShops = true;
         }

         assert hasTargetShops;

         boolean ownHistory = false;
         Object playerSelector;
         String selectorPlayerName;
         if (allPlayers) {
            playerSelector = PlayerSelector.ALL;
         } else if (self) {
            if (executingPlayer == null) {
               throw new CommandSourceRejectedException(Text.of("You must be a player in order to use the argument 'self'!"));
            }

            playerSelector = new PlayerSelector.ByUUID(executingPlayer.getUniqueId(), executingPlayer.getName());
            ownHistory = true;
         } else if (playerUUID != null) {
            selectorPlayerName = null;
            if (executingPlayer != null && playerUUID.equals(executingPlayer.getUniqueId())) {
               ownHistory = true;
               selectorPlayerName = executingPlayer.getName();
            }

            playerSelector = new PlayerSelector.ByUUID(playerUUID, selectorPlayerName);
         } else {
            assert playerName != null;

            User playerUser = this.resolveUserByName(sender, playerName, PLAYER_BY_NAME_ARGUMENT);
            if (playerUser == null) {
               return;
            }

            playerSelector = new PlayerSelector.ByUUID(playerUser.getUniqueId(), playerUser.getName());
            if (executingPlayer != null && playerUser.getUniqueId().equals(executingPlayer.getUniqueId())) {
               ownHistory = true;
            }
         }

         selectorPlayerName = null;
         Object shopSelector;
         if (allShops) {
            shopSelector = ShopSelector.ALL;
         } else if (adminShops) {
            shopSelector = ShopSelector.ADMIN_SHOPS;
         } else if (playerShops) {
            shopSelector = ShopSelector.PLAYER_SHOPS;
         } else if (ownShops) {
            if (executingPlayer == null) {
               throw new CommandSourceRejectedException(Text.of("You must be a player in order to use the argument 'own'!"));
            }

            shopSelector = new ShopSelector.ByOwnerUUID(executingPlayer.getUniqueId(), executingPlayer.getName());
            ownHistory = true;
         } else if (existingShop != null) {
            if (PermissionUtils.hasPermission(sender, "shopkeeper.history.admin")) {
               hasAllPerm = true;
               shopSelector = new ShopSelector.ByExistingShop(existingShop);
            } else {
               if (!(existingShop instanceof PlayerShopkeeper)) {
                  throw this.noPermissionException();
               }

               if (executingPlayer == null) {
                  throw this.noPermissionException();
               }

               shopSelector = new ShopSelector.ByExistingShop(existingShop, executingPlayer.getUniqueId(), executingPlayer.getName());
               ownHistory = true;
            }
         } else if (shopUUID != null) {
            if (PermissionUtils.hasPermission(sender, "shopkeeper.history.admin")) {
               hasAllPerm = true;
               shopSelector = new ShopSelector.ByShopUUID(shopUUID);
            } else {
               if (executingPlayer == null) {
                  throw this.noPermissionException();
               }

               shopSelector = new ShopSelector.ByShopUUID(shopUUID, executingPlayer.getUniqueId(), executingPlayer.getName());
               ownHistory = true;
            }
         } else if (ownerUUID != null) {
            String selectorOwnerName = null;
            if (executingPlayer != null && ownerUUID.equals(executingPlayer.getUniqueId())) {
               ownHistory = true;
               selectorOwnerName = executingPlayer.getName();
            }

            shopSelector = new ShopSelector.ByOwnerUUID(ownerUUID, selectorOwnerName);
         } else {
            if (ownerName == null) {
               throw Validate.State.error("Missing shop selector!");
            }

            User ownerUser = this.resolveUserByName(sender, ownerName, OWNER_BY_NAME_ARGUMENT);
            if (ownerUser == null) {
               return;
            }

            shopSelector = new ShopSelector.ByOwnerUUID(ownerUser.getUniqueId(), ownerUser.getName());
            if (executingPlayer != null && ownerUser.getUniqueId().equals(executingPlayer.getUniqueId())) {
               ownHistory = true;
            }
         }

         assert shopSelector != null;

         if (hasAllPerm == null || !hasAllPerm) {
            if (ownHistory) {
               assert executingPlayer != null;

               this.checkPermission(sender, "shopkeeper.history.own");
            } else {
               if (hasAllPerm != null) {
                  assert !hasAllPerm;

                  throw this.noPermissionException();
               }

               this.checkPermission(sender, "shopkeeper.history.admin");
            }
         }

         Range range = new Range.PageRange(page, 10);
         TradingHistoryRequest historyRequest = new TradingHistoryRequest((PlayerSelector)playerSelector, (ShopSelector)shopSelector, range);
         long historyFetchStart = System.nanoTime();
         tradingHistoryProvider.getTradingHistory(historyRequest).thenAcceptAsync((historyResult) -> {
            Validate.State.notNull(historyRequest, (String)"historyResult is null!");

            assert historyResult != null;

            long historyPrintStart = System.nanoTime();
            this.sendTradingHistory(sender, historyRequest, historyResult);
            if (Debug.isDebugging(DebugOptions.commands)) {
               long end = System.nanoTime();
               long fetchDuration = historyPrintStart - historyFetchStart;
               long printDuration = end - historyPrintStart;
               long var10001 = TimeUnit.NANOSECONDS.toMillis(fetchDuration);
               sender.sendMessage("Fetch: " + var10001 + " ms | Print: " + TimeUnit.NANOSECONDS.toMillis(printDuration) + " ms");
            }

         }, SKShopkeepersPlugin.getInstance().getSyncExecutor()).exceptionally((exception) -> {
            TextUtils.sendMessage(sender, Text.parse("&cError: Could not retrieve the trading history!"));
            Log.severe("Error while retrieving trading history!", exception);
            return null;
         });
      }
   }

   @Nullable
   private User resolveUserByName(CommandSender sender, String userName, UserByNameArgument userByNameArgument) {
      List<User> matchingUsers = UserArgumentUtils.UserNameMatcher.EXACT.match(userName, true).toList();
      if (matchingUsers.isEmpty()) {
         Text error = userByNameArgument.getInvalidArgumentErrorMsg(userName);
         TextUtils.sendMessage(sender, error);
         return null;
      } else if (matchingUsers.size() > 1) {
         UserArgumentUtils.handleAmbiguousUserName(sender, userName, matchingUsers);
         return null;
      } else {
         return (User)matchingUsers.getFirst();
      }
   }

   private void sendTradingHistory(CommandSender sender, TradingHistoryRequest historyRequest, TradingHistoryResult historyResult) {
      assert sender != null && historyRequest != null && historyResult != null;

      PlayerSelector playerSelector = historyRequest.playerSelector;
      ShopSelector shopSelector = historyRequest.shopSelector;
      int totalTrades = historyResult.getTotalTradesCount();
      int startIndex = historyRequest.range.getStartIndex(totalTrades);
      int page = startIndex / 10 + 1;
      int maxPage = Math.max(1, (int)Math.ceil((double)totalTrades / 10.0D));
      Map<String, Object> headerArgs = new HashMap();
      headerArgs.put("page", page);
      headerArgs.put("maxPage", maxPage);
      headerArgs.put("tradesCount", totalTrades);
      Text headerPlayers;
      if (playerSelector == PlayerSelector.ALL) {
         headerPlayers = Messages.historyHeaderAllPlayers;
      } else {
         if (!(playerSelector instanceof PlayerSelector.ByUUID)) {
            throw new IllegalStateException("Unexpected player selector type: " + playerSelector.getClass().getName());
         }

         PlayerSelector.ByUUID playerByUuidSelector = (PlayerSelector.ByUUID)playerSelector;
         String playerName = playerByUuidSelector.getPlayerName();
         UUID playerUuid = playerByUuidSelector.getPlayerUUID();
         headerPlayers = Messages.historyHeaderSpecificPlayer;
         headerPlayers.setPlaceholderArguments("player", TextUtils.getPlayerText(playerName, playerUuid));
      }

      headerArgs.put("players", headerPlayers);
      Text headerShops;
      if (shopSelector == ShopSelector.ALL) {
         headerShops = Messages.historyHeaderAllShops;
      } else if (shopSelector == ShopSelector.ADMIN_SHOPS) {
         headerShops = Messages.historyHeaderAdminShops;
      } else if (shopSelector == ShopSelector.PLAYER_SHOPS) {
         headerShops = Messages.historyHeaderPlayerShops;
      } else if (shopSelector instanceof ShopSelector.ByOwnerUUID) {
         ShopSelector.ByOwnerUUID byOwnerSelector = (ShopSelector.ByOwnerUUID)shopSelector;
         UUID ownerUuid = byOwnerSelector.getOwnerUUID();
         String ownerName = byOwnerSelector.getOwnerName();
         headerShops = Messages.historyHeaderAllOwnedShops;
         headerShops.setPlaceholderArguments("owner", TextUtils.getPlayerText(ownerName, ownerUuid));
      } else {
         if (!(shopSelector instanceof ShopSelector.ByShopIdentifier)) {
            throw new IllegalStateException("Unexpected shop selector type: " + shopSelector.getClass().getName());
         }

         ShopSelector.ByShopIdentifier byShopSelector = (ShopSelector.ByShopIdentifier)shopSelector;
         Text shopIdentifier = byShopSelector.getShopIdentifier();
         UUID ownerUuid = byShopSelector.getOwnerUUID();
         if (ownerUuid == null) {
            headerShops = Messages.historyHeaderSpecificShop;
            headerShops.setPlaceholderArguments("shop", shopIdentifier);
         } else {
            String ownerName = byShopSelector.getOwnerName();
            headerShops = Messages.historyHeaderSpecificOwnedShop;
            headerShops.setPlaceholderArguments("shop", shopIdentifier);
            headerShops.setPlaceholderArguments("owner", TextUtils.getPlayerText(ownerName, ownerUuid));
         }
      }

      headerArgs.put("shops", headerShops);
      TextUtils.sendMessage(sender, (Text)Messages.historyHeader, (Map)headerArgs);
      if (totalTrades == 0) {
         TextUtils.sendMessage(sender, Messages.historyNoTradesFound);
      } else {
         Map<String, Object> entryArgs = new HashMap();
         int index = startIndex;

         for(Iterator var36 = historyResult.getTrades().iterator(); var36.hasNext(); ++index) {
            TradeRecord trade = (TradeRecord)var36.next();
            entryArgs.clear();
            PlayerRecord player = trade.getPlayer();
            Instant timestamp = trade.getTimestamp();
            ShopRecord shop = trade.getShop();
            PlayerRecord shopOwner = shop.getOwner();
            UnmodifiableItemStack item1 = trade.getItem1();
            UnmodifiableItemStack item2 = trade.getItem2();
            UnmodifiableItemStack resultItem = trade.getResultItem();
            Text entryMsg = item2 == null ? Messages.historyEntryOneItem : Messages.historyEntryTwoItems;
            entryArgs.put("index", index + 1);
            entryArgs.put("player", TextUtils.getPlayerText(player));
            entryArgs.put("item1Amount", item1.getAmount());
            entryArgs.put("item1", TextUtils.getItemText(item1));
            entryArgs.put("resultItemAmount", resultItem.getAmount());
            entryArgs.put("resultItem", TextUtils.getItemText(resultItem));
            String formattedTimestamp = Settings.DerivedSettings.dateTimeFormatter.format(timestamp);
            entryArgs.put("timeAgo", Text.hoverEvent(Text.of(formattedTimestamp)).childText(TimeUtils.getTimeAgoString(timestamp)).buildRoot());
            if (item2 != null) {
               entryArgs.put("item2Amount", item2.getAmount());
               entryArgs.put("item2", TextUtils.getItemText(item2));
            }

            Text tradeCountText = Text.EMPTY;
            if (trade.getTradeCount() > 1) {
               tradeCountText = Messages.historyEntryTradeCount;
               tradeCountText.setPlaceholderArguments("count", trade.getTradeCount());
            }

            entryArgs.put("trade_count", tradeCountText);
            Text entryShopDisplayText;
            if (shopOwner == null) {
               entryShopDisplayText = Messages.historyEntryAdminShop;
            } else {
               entryShopDisplayText = Messages.historyEntryPlayerShop;
               entryShopDisplayText.setPlaceholderArguments("owner", TextUtils.getPlayerText(shopOwner));
            }

            entryArgs.put("shop", TextUtils.getShopText(entryShopDisplayText, shop.getUniqueId()));
            TextUtils.sendMessage(sender, (Text)entryMsg, (Map)entryArgs);
         }
      }

   }
}
