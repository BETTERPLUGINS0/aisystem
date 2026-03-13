package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopkeeper;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.AnyStringFallback;
import com.nisovin.shopkeepers.commands.lib.arguments.FirstOfArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.LiteralArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PlayerByNameArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PlayerUUIDArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PositiveIntegerArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.SenderPlayerNameFallback;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.lib.util.PlayerArgumentUtils;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandList extends Command {
   private static final String ARGUMENT_ALL = "all";
   private static final String ARGUMENT_ADMIN = "admin";
   private static final String ARGUMENT_PLAYER = "player";
   private static final String ARGUMENT_PLAYER_NAME = "player:name";
   private static final String ARGUMENT_PLAYER_UUID = "player:uuid";
   private static final String ARGUMENT_PAGE = "page";
   private static final int ENTRIES_PER_PAGE = 8;
   private final ShopkeeperRegistry shopkeeperRegistry;

   CommandList(ShopkeeperRegistry shopkeeperRegistry) {
      super("list");
      this.shopkeeperRegistry = shopkeeperRegistry;
      this.setDescription(Messages.commandDescriptionList);
      this.addArgument(new FirstOfArgument("target", Arrays.asList(new LiteralArgument("all"), new LiteralArgument("admin"), new FirstOfArgument("player", Arrays.asList(new PlayerUUIDArgument("player:uuid"), new SenderPlayerNameFallback(new AnyStringFallback((new PlayerByNameArgument("player:name")).transformed((player) -> {
         return player.getName();
      })))), false)), true, true));
      this.addArgument((new PositiveIntegerArgument("page")).orDefaultValue(1));
   }

   public boolean testPermission(CommandSender sender) {
      if (!super.testPermission(sender)) {
         return false;
      } else {
         return PermissionUtils.hasPermission(sender, "shopkeeper.list.own") || PermissionUtils.hasPermission(sender, "shopkeeper.list.others") || PermissionUtils.hasPermission(sender, "shopkeeper.list.admin");
      }
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      int page = (Integer)context.get("page");
      boolean listAllShops = context.has("all");
      boolean listAdminShops = context.has("admin");
      UUID targetPlayerUUID = (UUID)context.getOrNull("player:uuid");
      String targetPlayerName = (String)context.getOrNull("player:name");

      assert listAllShops ^ listAdminShops ^ targetPlayerUUID != null ^ targetPlayerName != null;

      Object shops;
      if (listAllShops) {
         this.checkPermission(sender, "shopkeeper.list.admin");
         this.checkPermission(sender, "shopkeeper.list.others");
         shops = new ArrayList(this.shopkeeperRegistry.getAllShopkeepers());
      } else if (listAdminShops) {
         this.checkPermission(sender, "shopkeeper.list.admin");
         List<Shopkeeper> adminShops = new ArrayList();
         Iterator var11 = this.shopkeeperRegistry.getAllShopkeepers().iterator();

         while(var11.hasNext()) {
            Shopkeeper shopkeeper = (Shopkeeper)var11.next();
            if (shopkeeper instanceof AdminShopkeeper) {
               adminShops.add(shopkeeper);
            }
         }

         shops = adminShops;
      } else {
         boolean targetOwnShops = false;
         Player senderPlayer = sender instanceof Player ? (Player)sender : null;
         String senderName = (String)Unsafe.assertNonNull(sender.getName());
         if (senderPlayer == null || !senderPlayer.getUniqueId().equals(targetPlayerUUID) && !senderName.equalsIgnoreCase(targetPlayerName)) {
            if (targetPlayerName != null) {
               Player onlinePlayer = Bukkit.getPlayerExact(targetPlayerName);
               if (onlinePlayer != null) {
                  targetPlayerUUID = onlinePlayer.getUniqueId();
                  targetPlayerName = onlinePlayer.getName();
               }
            }
         } else {
            targetOwnShops = true;
            targetPlayerUUID = senderPlayer.getUniqueId();
            targetPlayerName = senderPlayer.getName();
         }

         if (targetOwnShops) {
            this.checkPermission(sender, "shopkeeper.list.own");
         } else {
            this.checkPermission(sender, "shopkeeper.list.others");
         }

         ShopkeeperArgumentUtils.OwnedPlayerShopsResult ownedPlayerShopsResult = ShopkeeperArgumentUtils.getOwnedPlayerShops(targetPlayerUUID, targetPlayerName);

         assert ownedPlayerShopsResult != null;

         Map<? extends UUID, ? extends String> matchingShopOwners = ownedPlayerShopsResult.getMatchingShopOwners();

         assert matchingShopOwners != null;

         if (matchingShopOwners.size() > 1) {
            assert targetPlayerName != null;

            boolean ambiguous = PlayerArgumentUtils.handleAmbiguousPlayerName(sender, targetPlayerName, matchingShopOwners.entrySet());
            if (ambiguous) {
               return;
            }
         }

         targetPlayerUUID = ownedPlayerShopsResult.getPlayerUUID();
         targetPlayerName = ownedPlayerShopsResult.getPlayerName();
         shops = ownedPlayerShopsResult.getShops();
      }

      assert shops != null;

      int shopsCount = ((List)shops).size();
      int maxPage = Math.max(1, (int)Math.ceil((double)shopsCount / 8.0D));
      page = Math.max(1, Math.min(page, maxPage));
      if (listAllShops) {
         TextUtils.sendMessage(sender, Messages.listAllShopsHeader, "shopsCount", shopsCount, "page", page, "maxPage", maxPage);
      } else if (listAdminShops) {
         TextUtils.sendMessage(sender, Messages.listAdminShopsHeader, "shopsCount", shopsCount, "page", page, "maxPage", maxPage);
      } else {
         TextUtils.sendMessage(sender, Messages.listPlayerShopsHeader, "player", TextUtils.getPlayerText(targetPlayerName, targetPlayerUUID), "shopsCount", shopsCount, "page", page, "maxPage", maxPage);
      }

      int startIndex = (page - 1) * 8;
      int endIndex = Math.min(startIndex + 8, shopsCount);

      for(int index = startIndex; index < endIndex; ++index) {
         Shopkeeper shopkeeper = (Shopkeeper)((List)shops).get(index);
         String shopName = shopkeeper.getName();
         TextUtils.sendMessage(sender, Messages.listShopsEntry, "shopIndex", index + 1, "shopUUID", shopkeeper.getUniqueId().toString(), "shopSessionId", shopkeeper.getId(), "shopId", shopkeeper.getId(), "shopName", shopName.isEmpty() ? "" : shopName + " ", "location", shopkeeper.getPositionString(), "shopType", shopkeeper.getType().getIdentifier(), "objectType", shopkeeper.getShopObject().getType().getIdentifier());
      }

   }
}
