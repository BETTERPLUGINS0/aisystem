package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.StringArgument;
import com.nisovin.shopkeepers.commands.lib.commands.PlayerCommand;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.currency.Currencies;
import com.nisovin.shopkeepers.currency.Currency;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemData;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class CommandSetCurrency extends PlayerCommand {
   private static final String ARGUMENT_CURRENCY = "currency";

   CommandSetCurrency() {
      super("setCurrency");
      this.setPermission("shopkeeper.setcurrency");
      this.setDescription(Messages.commandDescriptionSetCurrency);
      this.addArgument((new StringArgument("currency")).optional());
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      assert input.getSender() instanceof Player;

      Player player = (Player)input.getSender();
      String currencyType = (String)context.getOrNull("currency");
      Currency currency;
      if (currencyType != null) {
         currency = Currencies.getById(StringUtils.normalize(currencyType));
         if (currency == null) {
            TextUtils.sendMessage(player, (Text)Messages.unknownCurrency, (Object[])("currency", currencyType));
            return;
         }
      } else {
         currency = Currencies.getBase();
      }

      assert currency != null;

      boolean baseCurrency = currency == Currencies.getBase();
      ItemStack newCurrencyItem = ItemUtils.getOrEmpty(player.getInventory().getItemInMainHand());
      if (baseCurrency && ItemUtils.isEmpty(newCurrencyItem)) {
         TextUtils.sendMessage(player, (Text)Messages.mustHoldItemInMainHand);
      } else {
         (new ArrayList(ShopkeepersAPI.getUIRegistry().getUISessions())).forEach((uiSession) -> {
            if (uiSession.getShopkeeper() instanceof PlayerShopkeeper) {
               uiSession.abort();
            }

         });
         if (baseCurrency) {
            Settings.currencyItem = new ItemData(newCurrencyItem);
         } else {
            Settings.highCurrencyItem = new ItemData(newCurrencyItem);
         }

         Settings.onSettingsChanged();
         Settings.saveConfig();
         TextUtils.sendMessage(player, (Text)Messages.currencyItemSetToMainHandItem, (Object[])("currency", currency.getDisplayName(), "currencyId", currency.getId()));
      }
   }
}
