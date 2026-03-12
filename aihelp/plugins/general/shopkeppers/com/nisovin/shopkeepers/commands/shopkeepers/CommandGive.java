package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopType;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.nisovin.shopkeepers.commands.arguments.ShopObjectTypeArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopTypeArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopTypeFilter;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.BoundedIntegerArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PlayerArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.SenderPlayerFallback;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopcreation.ShopCreationItem;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.InventoryUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

class CommandGive extends Command {
   private static final String ARGUMENT_PLAYER = "player";
   private static final String ARGUMENT_SHOP_TYPE = "shop-type";
   private static final String ARGUMENT_OBJECT_TYPE = "object-type";
   private static final String ARGUMENT_AMOUNT = "amount";

   CommandGive() {
      super("give");
      this.setPermission("shopkeeper.give");
      this.setDescription(Messages.commandDescriptionGive);
      this.addArgument(new SenderPlayerFallback(new PlayerArgument("player")));
      this.addArgument((new ShopTypeArgument("shop-type", ShopTypeFilter.PLAYER)).optional());
      this.addArgument((new ShopObjectTypeArgument("object-type")).optional());
      this.addArgument((new BoundedIntegerArgument("amount", 1, 1024)).orDefaultValue(1));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      Player targetPlayer = (Player)context.get("player");
      boolean targetSelf = sender.equals(targetPlayer);
      ShopType<?> shopType = (ShopType)context.getOrNull("shop-type");
      ShopObjectType<?> shopObjectType = (ShopObjectType)context.getOrNull("object-type");
      int amount = (Integer)context.get("amount");

      assert amount >= 1 && amount <= 1024;

      ItemStack item = ShopCreationItem.create(amount);
      ShopCreationItem shopCreationItem = new ShopCreationItem(item);
      if (shopType != null) {
         shopCreationItem.setShopType(shopType);
      }

      if (shopObjectType != null) {
         shopCreationItem.setObjectType(shopObjectType);
      }

      shopCreationItem.applyItemMeta();
      PlayerInventory inventory = targetPlayer.getInventory();
      ItemStack[] contents = (ItemStack[])Unsafe.castNonNull(inventory.getStorageContents());
      int remaining = InventoryUtils.addItems(contents, item);
      InventoryUtils.setStorageContents(inventory, contents);
      if (remaining > 0) {
         item.setAmount(remaining);
         targetPlayer.getWorld().dropItem(targetPlayer.getEyeLocation(), item);
      }

      TextUtils.sendMessage(targetPlayer, (Text)Messages.shopCreationItemsReceived, (Object[])("amount", amount));
      if (!targetSelf) {
         TextUtils.sendMessage(sender, Messages.shopCreationItemsGiven, "player", TextUtils.getPlayerText(targetPlayer), "amount", amount);
      }

   }
}
