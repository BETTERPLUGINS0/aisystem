package me.gypopo.economyshopgui.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.base.PluginCommand;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.methodes.SendMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

public class ShopGive extends PluginCommand {
   public ShopGive(EconomyShopGUI plugin, List<String> disabledWorlds) {
      super(plugin, "shopgive", "Give a shop item to a player", "/shopgive <section> <itemIndex> [player] [amount]", "EconomyShopGUI.shopgive", disabledWorlds);
   }

   public boolean execute(CommandSender sender, String label, String[] args) {
      if (this.plugin.badYMLParse != null) {
         SendMessage.warnMessage(sender, (String)"This command cannot be executed now, please fix the configuration formatting first!");
         return true;
      } else if (!this.hasAccessInWorld(sender)) {
         return true;
      } else if (args.length > 0) {
         if (this.plugin.getShopSections().stream().noneMatch((s) -> {
            return s.equals(args[0]);
         })) {
            SendMessage.sendMessage(sender, (Translatable)Lang.NO_SHOP_FOUND.get());
            return true;
         } else if (args.length > 1) {
            if (this.plugin.getSection(args[0]).getShopItemLocs().stream().noneMatch((s) -> {
               return s.equals(args[1]);
            })) {
               SendMessage.sendMessage(sender, (Translatable)Lang.SECTION_DOES_NOT_CONTAIN_ITEM.get().replace("%shopsection%", args[0]).replace("%itemLoc%", args[1]));
               return true;
            } else {
               if (args.length > 2 && !this.isInt(args[2])) {
                  Player p = this.plugin.getServer().getPlayer(args[2]);
                  if (p != null) {
                     ItemStack item = this.plugin.getSection(args[0]).getShopItem(args[0] + "." + args[1]).getItemToGive();
                     if (args.length > 3) {
                        this.setAmount(item, args[3]);
                     }

                     SendMessage.sendMessage(sender, (Translatable)Lang.SHOP_ITEM_GIVEN.get().replace("%amount%", String.valueOf(item.getAmount())).replace("%section%", args[0]).replace("%itemIndex%", args[1]).replace("%material%", this.plugin.getMaterialName(item.getType().toString())));
                     this.giveItem(p, item);
                  } else {
                     SendMessage.warnMessage(sender, (Translatable)Lang.PLAYER_NOT_ONLINE.get());
                  }
               } else if (sender instanceof Player) {
                  ItemStack item = this.plugin.getSection(args[0]).getShopItem(args[0] + "." + args[1]).getItemToGive();
                  if (args.length > 2) {
                     this.setAmount(item, args[2]);
                  }

                  SendMessage.sendMessage(sender, (Translatable)Lang.SHOP_ITEM_RECEIVED.get().replace("%amount%", String.valueOf(item.getAmount())).replace("%section%", args[0]).replace("%itemIndex%", args[1]).replace("%material%", this.plugin.getMaterialName(item.getType().toString())));
                  this.giveItem((Player)sender, item);
               } else {
                  SendMessage.sendMessage(sender, (String)(ChatColor.GREEN + "/shopgive <section> <itemIndex> [player] [amount]"));
               }

               return true;
            }
         } else {
            SendMessage.sendMessage(sender, (String)(ChatColor.GREEN + "/shopgive <section> <itemIndex> [player] [amount]"));
            return true;
         }
      } else {
         SendMessage.infoMessage(sender, (String)"/shopgive <section> <itemIndex> [player] [amount]");
         return true;
      }
   }

   private void giveItem(Player p, ItemStack item) {
      List<ItemStack> items = new ArrayList(p.getInventory().addItem(new ItemStack[]{item}).values());

      for(int i = 0; i < items.size() && i != 2; ++i) {
         p.getWorld().dropItem(p.getLocation(), (ItemStack)items.get(i));
      }

   }

   private ItemStack[] getStacks(ItemStack item) {
      int amount = item.getAmount();
      int maxStackSize = item.getMaxStackSize();
      double div = (double)amount / (double)maxStackSize;
      int fullStacks = (int)Math.floor(div);
      int stacks = div == Math.floor(div) ? fullStacks : (int)Math.ceil(div);
      int amountLeft = amount;
      ItemStack[] items = new ItemStack[stacks];
      if (fullStacks >= 1) {
         for(int i = 0; i < fullStacks; ++i) {
            ItemStack is = new ItemStack(item);
            is.setAmount(maxStackSize);
            items[i] = is;
            amountLeft -= maxStackSize;
         }
      }

      if (amountLeft >= 1) {
         ItemStack is = new ItemStack(item);
         is.setAmount(amountLeft);
         items[stacks - 1] = is;
      }

      return items;
   }

   private void setAmount(ItemStack item, String s) {
      try {
         item.setAmount(Integer.parseInt(s));
      } catch (NumberFormatException var4) {
      }

   }

   private boolean isLimit(String s, ItemStack item) {
      try {
         return Integer.parseInt(s) > 128;
      } catch (NumberFormatException var4) {
         return true;
      }
   }

   private boolean isInt(String s) {
      try {
         Integer.parseInt(s);
         return true;
      } catch (NumberFormatException var3) {
         return false;
      }
   }

   private boolean hasAccessInWorld(CommandSender sender) {
      return !(sender instanceof Player) ? true : this.hasAccessInWorld((Player)sender);
   }

   public List<String> tabComplete(CommandSender commandSender, String s, String[] args) {
      ArrayList completions;
      switch(args.length) {
      case 1:
         if (!args[0].isEmpty()) {
            completions = new ArrayList();
            StringUtil.copyPartialMatches(args[0], this.plugin.getShopSections(), completions);
            Collections.sort(completions);
            return completions;
         }

         return this.plugin.getShopSections();
      case 2:
         if (!this.isSection(args[0])) {
            return null;
         } else {
            if (!args[1].isEmpty()) {
               completions = new ArrayList();
               StringUtil.copyPartialMatches(args[1], this.plugin.getSection(args[0]).getShopItemLocs(), completions);
               Collections.sort(completions);
               return completions;
            }

            return this.plugin.getSection(args[0]).getShopItemLocs();
         }
      case 3:
         return (List)this.plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
      case 4:
         return Arrays.asList("1", "16", "32", "64", "128");
      default:
         return null;
      }
   }

   private boolean isSection(String section) {
      return this.plugin.getShopSections().contains(section);
   }
}
