package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.FirstOfArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.LiteralArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.StringArgument;
import com.nisovin.shopkeepers.commands.lib.commands.PlayerCommand;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.trading.commandtrading.CommandTradingUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.Arrays;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class CommandSetTradedCommand extends PlayerCommand {
   private static final String ARGUMENT_NEW_COMMAND = "command";
   private static final String ARGUMENT_REMOVE_COMMAND = "-";
   private static final String ARGUMENT_QUERY_COMMAND = "?";

   CommandSetTradedCommand() {
      super("setTradedCommand");
      this.setPermission("shopkeeper.settradedcommand");
      this.setDescription(Messages.commandDescriptionSettradedcommand);
      this.addArgument(new FirstOfArgument("commandArg", Arrays.asList((new LiteralArgument("?")).orDefaultValue("?"), new LiteralArgument("-"), new StringArgument("command", true)), true, true));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      assert input.getSender() instanceof Player;

      Player player = (Player)input.getSender();
      ItemStack itemInHand = player.getInventory().getItemInMainHand();
      if (ItemUtils.isEmpty(itemInHand)) {
         TextUtils.sendMessage(player, (Text)Messages.mustHoldItemInMainHand);
      } else {
         String newTradedCommand = (String)context.getOrNull("command");
         boolean removeCommand = context.has("-");
         String tradedCommand;
         if (removeCommand) {
            tradedCommand = getTradedCommandView(itemInHand);
            CommandTradingUtils.setTradedCommand(itemInHand, (String)null);
            TextUtils.sendMessage(player, (Text)Messages.tradedCommandRemoved, (Object[])("command", tradedCommand));
         } else if (newTradedCommand != null) {
            CommandTradingUtils.setTradedCommand(itemInHand, newTradedCommand);
            TextUtils.sendMessage(player, (Text)Messages.tradedCommandSet, (Object[])("command", newTradedCommand));
         } else {
            tradedCommand = CommandTradingUtils.getTradedCommand(itemInHand);
            if (tradedCommand == null) {
               TextUtils.sendMessage(player, (Text)Messages.tradedCommandViewUnset);
            } else {
               TextUtils.sendMessage(player, (Text)Messages.tradedCommandView, (Object[])("command", tradedCommand));
            }

         }
      }
   }

   private static String getTradedCommandView(ItemStack itemStack) {
      String tradedCommand = CommandTradingUtils.getTradedCommand(itemStack);
      if (tradedCommand == null) {
         tradedCommand = "-";
      }

      return tradedCommand;
   }
}
