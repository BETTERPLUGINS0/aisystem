package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.commands.PlayerCommand;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.ConfigUtils;
import com.nisovin.shopkeepers.util.inventory.ItemData;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class CommandYaml extends PlayerCommand {
   private static final int MAX_OUTPUT_LENGTH = 15;

   CommandYaml() {
      super("yaml");
      this.setPermission("shopkeeper.debug");
      this.setDescription(Text.of("Prints the YAML representation of the held item."));
      this.setHiddenInParentHelp(true);
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      assert input.getSender() instanceof Player;

      Player player = (Player)input.getSender();
      ItemStack itemInHand = player.getInventory().getItemInMainHand();
      if (ItemUtils.isEmpty(itemInHand)) {
         player.sendMessage(String.valueOf(ChatColor.GRAY) + "No item in hand!");
      } else {
         String itemStackYaml = ConfigUtils.toConfigYamlWithoutTrailingNewline("item-in-hand", itemInHand);
         Object itemDataSerialized = (new ItemData(UnmodifiableItemStack.ofNonNull(itemInHand))).serialize();
         String itemDataYaml = ConfigUtils.toConfigYamlWithoutTrailingNewline("item-in-hand-config-data", itemDataSerialized);
         String[] itemStackYamlLines = StringUtils.splitLines(itemStackYaml);
         String[] itemDataYamlLines = StringUtils.splitLines(itemDataYaml);
         player.sendMessage(String.valueOf(ChatColor.YELLOW) + "Serialized ItemStack:");
         String[] var10;
         int var11;
         int var12;
         String line;
         if (itemStackYamlLines.length > 15) {
            player.sendMessage(String.valueOf(ChatColor.GRAY) + "The output is too large for the chat.");
         } else {
            var10 = itemStackYamlLines;
            var11 = itemStackYamlLines.length;

            for(var12 = 0; var12 < var11; ++var12) {
               line = var10[var12];
               player.sendMessage(line);
            }
         }

         player.sendMessage("");
         player.sendMessage(String.valueOf(ChatColor.YELLOW) + "Config item data:");
         if (itemDataYamlLines.length > 15) {
            player.sendMessage(String.valueOf(ChatColor.GRAY) + "The output is too large for the chat.");
         } else {
            var10 = itemDataYamlLines;
            var11 = itemDataYamlLines.length;

            for(var12 = 0; var12 < var11; ++var12) {
               line = var10[var12];
               player.sendMessage(line);
            }
         }

         player.sendMessage("");
         player.sendMessage(String.valueOf(ChatColor.GRAY) + "Note: The output is also logged to the console for easier copying.");
         Log.info("Serialized ItemStack:\n" + itemStackYaml);
         Log.info("Config item data:\n" + itemDataYaml);
      }
   }
}
