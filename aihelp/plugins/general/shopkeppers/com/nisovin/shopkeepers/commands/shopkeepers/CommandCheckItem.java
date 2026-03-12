package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.commands.PlayerCommand;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.currency.Currencies;
import com.nisovin.shopkeepers.currency.Currency;
import com.nisovin.shopkeepers.shopcreation.ShopCreationItem;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.inventory.ItemData;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class CommandCheckItem extends PlayerCommand {
   CommandCheckItem() {
      super("checkitem");
      this.setPermission("shopkeeper.debug");
      this.setDescription(Text.of("Shows debugging information for the currently held items."));
      this.setHiddenInParentHelp(true);
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      assert input.getSender() instanceof Player;

      Player player = (Player)input.getSender();
      ItemStack mainHandItem = player.getInventory().getItemInMainHand();
      ItemStack offHandItem = player.getInventory().getItemInOffHand();
      ShopCreationItem mainHandShopCreationItem = new ShopCreationItem(mainHandItem);
      ShopCreationItem offHandShopCreationItem = new ShopCreationItem(offHandItem);
      player.sendMessage(String.valueOf(ChatColor.YELLOW) + "Item in main hand / off hand:");
      boolean var10001 = ItemUtils.isSimilar(mainHandItem, offHandItem);
      player.sendMessage("- Similar to off-hand item: " + toDisplayString(var10001));
      var10001 = ItemUtils.matchesData(mainHandItem, offHandItem);
      player.sendMessage("- NBT matching off-hand item: " + toDisplayString(var10001));
      var10001 = Compat.getProvider().matches(mainHandItem, offHandItem);
      player.sendMessage("- Trade matching off-hand item: " + toDisplayString(var10001));
      String var10 = checkItems(mainHandShopCreationItem, offHandShopCreationItem, ShopCreationItem::isShopCreationItem);
      player.sendMessage("- Is shop creation item: " + var10);
      var10 = checkItems(mainHandShopCreationItem, offHandShopCreationItem, ShopCreationItem::hasShopType);
      player.sendMessage("- Has shop type: " + var10);
      var10 = checkItems(mainHandShopCreationItem, offHandShopCreationItem, ShopCreationItem::hasObjectType);
      player.sendMessage("- Has object type: " + var10);
      Iterator var8 = Currencies.getAll().iterator();

      while(var8.hasNext()) {
         Currency currency = (Currency)var8.next();
         var10 = currency.getId();
         ItemData var10004 = currency.getItemData();
         Objects.requireNonNull(var10004);
         player.sendMessage("- Is currency item '" + var10 + "': " + checkItems(mainHandItem, offHandItem, var10004::matches));
      }

   }

   private static String checkItems(ItemStack mainHand, ItemStack offHand, Predicate<? super ItemStack> check) {
      String var10000 = toDisplayString(check.test(mainHand));
      return var10000 + " / " + toDisplayString(check.test(offHand));
   }

   private static String checkItems(ShopCreationItem mainHand, ShopCreationItem offHand, Predicate<? super ShopCreationItem> check) {
      String var10000 = toDisplayString(check.test(mainHand));
      return var10000 + " / " + toDisplayString(check.test(offHand));
   }

   private static String toDisplayString(boolean bool) {
      return bool ? "yes" : "no";
   }
}
