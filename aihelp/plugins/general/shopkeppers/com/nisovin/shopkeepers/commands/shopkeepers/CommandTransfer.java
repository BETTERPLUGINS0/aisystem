package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.api.user.User;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperFilter;
import com.nisovin.shopkeepers.commands.arguments.TargetShopkeeperFallback;
import com.nisovin.shopkeepers.commands.arguments.UserByNameArgument;
import com.nisovin.shopkeepers.commands.arguments.UserByUUIDArgument;
import com.nisovin.shopkeepers.commands.arguments.UserNameArgument;
import com.nisovin.shopkeepers.commands.arguments.UserUUIDArgument;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.FirstOfArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.commands.util.UserArgumentUtils;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.player.AbstractPlayerShopkeeper;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.CommandSender;

class CommandTransfer extends Command {
   private static final String ARGUMENT_SHOPKEEPER = "shopkeeper";
   private static final String ARGUMENT_NEW_OWNER = "new-owner";
   private static final String ARGUMENT_NEW_OWNER_UUID = "new-owner:uuid";
   private static final String ARGUMENT_NEW_OWNER_NAME = "new-owner:name";
   private static final UserByNameArgument USER_BY_NAME_ARGUMENT = new UserByNameArgument("new-owner");
   private static final UserByUUIDArgument USER_BY_UUID_ARGUMENT = new UserByUUIDArgument("new-owner");

   CommandTransfer() {
      super("transfer");
      this.setPermission("shopkeeper.transfer");
      this.setDescription(Messages.commandDescriptionTransfer);
      this.addArgument(new TargetShopkeeperFallback(new ShopkeeperArgument("shopkeeper", ShopkeeperFilter.PLAYER.and(ShopkeeperFilter.withAccess(DefaultUITypes.EDITOR()))), ShopkeeperArgumentUtils.TargetShopkeeperFilter.PLAYER));
      this.addArgument(new FirstOfArgument("new-owner", Arrays.asList(new UserUUIDArgument("new-owner:uuid"), new UserNameArgument("new-owner:name")), false));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      AbstractPlayerShopkeeper shopkeeper = (AbstractPlayerShopkeeper)context.get("shopkeeper");
      UUID newOwnerUUID = (UUID)context.getOrNull("new-owner:uuid");
      String newOwnerName = (String)context.getOrNull("new-owner:name");

      assert newOwnerUUID != null ^ newOwnerName != null;

      User newOwner;
      if (newOwnerUUID != null) {
         newOwner = UserArgumentUtils.findUser(newOwnerUUID);
         if (newOwner == null) {
            Text error = USER_BY_UUID_ARGUMENT.getInvalidArgumentErrorMsg(newOwnerUUID.toString());
            TextUtils.sendMessage(sender, error);
            return;
         }
      } else {
         assert newOwnerName != null;

         List<User> matchingUsers = UserArgumentUtils.UserNameMatcher.EXACT.match(newOwnerName, true).toList();
         if (matchingUsers.isEmpty()) {
            Text error = USER_BY_NAME_ARGUMENT.getInvalidArgumentErrorMsg(newOwnerName);
            TextUtils.sendMessage(sender, error);
            return;
         }

         if (matchingUsers.size() > 1) {
            UserArgumentUtils.handleAmbiguousUserName(sender, newOwnerName, matchingUsers);
            return;
         }

         newOwner = (User)matchingUsers.getFirst();
      }

      if (shopkeeper.canEdit(sender, false)) {
         shopkeeper.setOwner(newOwner);
         TextUtils.sendMessage(sender, Messages.ownerSet, "owner", TextUtils.getPlayerText(newOwner));
         ShopkeepersPlugin.getInstance().getShopkeeperStorage().save();
      }
   }
}
