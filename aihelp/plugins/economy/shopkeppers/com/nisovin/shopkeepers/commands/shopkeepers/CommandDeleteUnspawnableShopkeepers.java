package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.commands.Confirmations;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.shopkeeper.spawning.ShopkeeperSpawner;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObject;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class CommandDeleteUnspawnableShopkeepers extends Command {
   private static final int MAX_LIST_COUNT = 5;
   private final SKShopkeeperRegistry shopkeeperRegistry;
   private final Confirmations confirmations;

   CommandDeleteUnspawnableShopkeepers(SKShopkeeperRegistry shopkeeperRegistry, Confirmations confirmations) {
      super("deleteUnspawnableShopkeepers");
      this.shopkeeperRegistry = shopkeeperRegistry;
      this.confirmations = confirmations;
      this.setPermission("shopkeeper.delete-unspawnable-shopkeepers");
      this.setDescription(Text.of("Deletes shopkeepers that failed to spawn."));
      this.setHiddenInParentHelp(true);
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      ShopkeeperSpawner spawner = this.shopkeeperRegistry.getShopkeeperSpawner();
      List<Shopkeeper> unspawnableShopkeepers = spawner.checkUnspawnableShopkeepers(false, true);
      if (unspawnableShopkeepers.isEmpty()) {
         sender.sendMessage(String.valueOf(ChatColor.GREEN) + "There are no shopkeepers that failed to spawn during their last spawn attempt!");
      } else {
         String var10001 = String.valueOf(ChatColor.RED);
         TextUtils.sendMessage(sender, var10001 + "Found " + String.valueOf(ChatColor.YELLOW) + unspawnableShopkeepers.size() + String.valueOf(ChatColor.RED) + " shopkeepers that failed to spawn during their last spawn attempt:");
         int listedCount = 0;
         Iterator var7 = unspawnableShopkeepers.iterator();

         while(var7.hasNext()) {
            Shopkeeper shopkeeper = (Shopkeeper)var7.next();
            ++listedCount;
            if (listedCount > 5) {
               TextUtils.sendMessage(sender, String.valueOf(ChatColor.RED) + "...");
               break;
            }

            var10001 = String.valueOf(ChatColor.RED);
            TextUtils.sendMessage(sender, var10001 + "- " + String.valueOf(ChatColor.YELLOW) + shopkeeper.getId() + String.valueOf(ChatColor.RED) + " at " + String.valueOf(ChatColor.YELLOW) + shopkeeper.getPositionString());
         }

         this.confirmations.awaitConfirmation(sender, () -> {
            int deleted = 0;

            for(Iterator var3 = unspawnableShopkeepers.iterator(); var3.hasNext(); ++deleted) {
               Shopkeeper shopkeeper = (Shopkeeper)var3.next();
               if (!shopkeeper.isValid()) {
                  return;
               }

               AbstractShopObject shopObject = (AbstractShopObject)shopkeeper.getShopObject();
               if (!shopObject.isLastSpawnFailed()) {
                  return;
               }

               shopkeeper.delete();
            }

            SKShopkeepersPlugin.getInstance().getShopkeeperStorage().save();
            String var10001 = String.valueOf(ChatColor.GREEN);
            sender.sendMessage(var10001 + "Deleted " + String.valueOf(ChatColor.YELLOW) + deleted + String.valueOf(ChatColor.GREEN) + " shopkeepers that failed to spawn during their last spawn attempt!");
         });
         TextUtils.sendMessage(sender, String.valueOf(ChatColor.RED) + "Do you want to irrevocably delete these shopkeepers?");
         TextUtils.sendMessage(sender, Messages.confirmationRequired);
      }
   }
}
