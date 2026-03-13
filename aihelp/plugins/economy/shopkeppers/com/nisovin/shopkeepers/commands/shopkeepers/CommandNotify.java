package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.FirstOfArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.LiteralArgument;
import com.nisovin.shopkeepers.commands.lib.commands.PlayerCommand;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.tradenotifications.NotificationUserPreferences;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import java.util.Arrays;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandNotify extends PlayerCommand {
   private static final String ARGUMENT_TRADES = "trades";

   CommandNotify() {
      super("notify");
      this.setDescription(Messages.commandDescriptionNotify);
      this.addArgument(new FirstOfArgument("notification-type", Arrays.asList(new LiteralArgument("trades"))));
   }

   public boolean testPermission(CommandSender sender) {
      return !super.testPermission(sender) ? false : PermissionUtils.hasPermission(sender, "shopkeeper.notify.trades");
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      Player sender = (Player)input.getSender();

      assert context.has("trades");

      if (context.has("trades")) {
         NotificationUserPreferences userPreferences = SKShopkeepersPlugin.getInstance().getTradeNotifications().getUserPreferences();
         boolean newState = !userPreferences.isNotifyOnTrades(sender);
         userPreferences.setNotifyOnTrades(sender, newState);
         if (newState) {
            TextUtils.sendMessage(sender, (Text)Messages.tradeNotificationsEnabled);
         } else {
            TextUtils.sendMessage(sender, (Text)Messages.tradeNotificationsDisabled);
         }
      }

   }
}
