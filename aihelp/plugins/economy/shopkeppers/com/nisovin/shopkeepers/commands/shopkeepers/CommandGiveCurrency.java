package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.BoundedIntegerArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PlayerArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.SenderPlayerFallback;
import com.nisovin.shopkeepers.commands.lib.arguments.StringArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.currency.Currencies;
import com.nisovin.shopkeepers.currency.Currency;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.InventoryUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import java.util.Arrays;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

class CommandGiveCurrency extends Command {
   private static final String ARGUMENT_PLAYER = "player";
   private static final String ARGUMENT_CURRENCY = "currency";
   private static final String ARGUMENT_AMOUNT = "amount";

   CommandGiveCurrency() {
      super("giveCurrency", Arrays.asList("currency"));
      this.setPermission("shopkeeper.givecurrency");
      this.setDescription(Messages.commandDescriptionGiveCurrency);
      this.addArgument(new SenderPlayerFallback(new PlayerArgument("player")));
      this.addArgument((new StringArgument("currency")).optional());
      this.addArgument((new BoundedIntegerArgument("amount", 1, 1024)).orDefaultValue(1));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      Player targetPlayer = (Player)context.get("player");
      boolean targetSelf = sender.equals(targetPlayer);
      String currencyType = (String)context.getOrNull("currency");
      Currency currency;
      if (currencyType != null) {
         currency = Currencies.getById(StringUtils.normalize(currencyType));
         if (currency == null) {
            TextUtils.sendMessage(sender, Messages.unknownCurrency, "currency", currencyType);
            return;
         }
      } else {
         currency = Currencies.getBase();
      }

      assert currency != null;

      int amount = (Integer)context.get("amount");

      assert amount >= 1 && amount <= 1024;

      ItemStack item = currency.getItemData().createItemStack(amount);

      assert item != null;

      PlayerInventory inventory = targetPlayer.getInventory();
      ItemStack[] contents = (ItemStack[])Unsafe.castNonNull(inventory.getStorageContents());
      int remaining = InventoryUtils.addItems(contents, item);
      InventoryUtils.setStorageContents(inventory, contents);
      if (remaining > 0) {
         item.setAmount(remaining);
         targetPlayer.getWorld().dropItem(targetPlayer.getEyeLocation(), item);
      }

      TextUtils.sendMessage(targetPlayer, (Text)Messages.currencyItemsReceived, (Object[])("amount", amount, "currency", currency.getDisplayName(), "currencyId", currency.getId()));
      if (!targetSelf) {
         TextUtils.sendMessage(sender, Messages.currencyItemsGiven, "player", TextUtils.getPlayerText(targetPlayer), "amount", amount, "currency", currency.getDisplayName(), "currencyId", currency.getId());
      }

   }
}
