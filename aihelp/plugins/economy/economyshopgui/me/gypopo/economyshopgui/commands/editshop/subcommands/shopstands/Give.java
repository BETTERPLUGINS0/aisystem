package me.gypopo.economyshopgui.commands.editshop.subcommands.shopstands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.editshop.Methods;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.stands.StandType;
import me.gypopo.economyshopgui.util.ItemBuilder;
import me.gypopo.economyshopgui.util.PermissionsCache;
import me.gypopo.economyshopgui.util.exceptions.StandLoadException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

public class Give extends SubCommad {
   private final Methods methods;
   private final EconomyShopGUI plugin;
   StandType type;
   String section;
   String itemLoc;

   public Give(EconomyShopGUI plugin, Methods methods) {
      this.plugin = plugin;
      this.methods = methods;
   }

   public String getName() {
      return "give";
   }

   public String getDescription() {
      return Lang.EDITSHOP_SHOPSTANDS_GIVE_SUBCOMMAND_DESC.get().getLegacy();
   }

   public String getSyntax() {
      return Lang.EDITSHOP_SHOPSTANDS_GIVE_SUBCOMMAND_SYNTAX.get().getLegacy();
   }

   public boolean hasPermission(CommandSender source) {
      return PermissionsCache.hasPermission(source, "EconomyShopGUI.eshop.shopstands." + this.getName());
   }

   public void perform(Object logger, String[] args) {
      if (logger instanceof Player) {
         if (args.length > 2) {
            try {
               this.type = StandType.fromName(args[2]);
            } catch (StandLoadException var7) {
               SendMessage.warnMessage(logger, Lang.INVALID_SHOP_STAND_TYPE.get().replace("%values%", String.join(",", StandType.getSupported())));
               return;
            }

            if (!this.type.getType().isSupported()) {
               SendMessage.warnMessage(logger, Lang.STAND_TYPE_NOT_SUPPORTED.get().replace("%type%", this.type.name()));
            } else if (args.length > 3) {
               this.section = this.methods.getSection(logger, args[3]);
               if (this.section != null) {
                  if (args.length > 4) {
                     this.itemLoc = this.methods.getShopItemLoc(logger, this.section, args[4]);
                     if (this.itemLoc != null) {
                        String item = this.section + "." + this.itemLoc;
                        ItemBuilder builder = new ItemBuilder(this.type.getType().parseItem());
                        builder.setDisplayName(Lang.DEFAULT_SHOP_STAND_ITEM_NAME.get());
                        builder.withLore(Lang.DEFAULT_SHOP_STAND_ITEM_LORE.get().replace("%item%", item));
                        ItemStack stack = this.plugin.versionHandler.setNBTString(builder.build(), "shopstand_item", item);
                        Player player = (Player)logger;
                        if (player.getInventory().firstEmpty() == -1) {
                           SendMessage.chatToPlayer(player, Lang.NOT_ENOUGH_SPACE_INSIDE_INVENTORY.get());
                        } else {
                           player.getInventory().addItem(new ItemStack[]{stack});
                           SendMessage.chatToPlayer(player, Lang.SHOP_STAND_ITEM_RECEIVED.get().replace("%amount%", String.valueOf(1)).replace("%item%", item));
                        }
                     }
                  } else {
                     SendMessage.sendMessage(logger, this.getSyntax());
                  }
               }
            } else {
               SendMessage.sendMessage(logger, this.getSyntax());
            }
         } else {
            SendMessage.sendMessage(logger, this.getSyntax());
         }
      }
   }

   private boolean isSection(String section2) {
      if (this.plugin.getShopSections().contains(section2)) {
         this.section = section2;
         return true;
      } else {
         return false;
      }
   }

   public List<String> getTabCompletion(String[] args) {
      ArrayList completions;
      switch(args.length) {
      case 3:
         if (!args[2].isEmpty()) {
            completions = new ArrayList();
            StringUtil.copyPartialMatches(args[2], StandType.getSupported(), completions);
            Collections.sort(completions);
            return completions;
         }

         return StandType.getSupported();
      case 4:
         if (!args[3].isEmpty()) {
            completions = new ArrayList();
            StringUtil.copyPartialMatches(args[3], this.plugin.getShopSections(), completions);
            Collections.sort(completions);
            return completions;
         }

         return this.plugin.getShopSections();
      case 5:
         if (!this.isSection(args[3])) {
            return null;
         } else {
            if (!args[2].isEmpty()) {
               completions = new ArrayList();
               StringUtil.copyPartialMatches(args[4], this.plugin.getSection(args[3]).getShopItemLocs(), completions);
               Collections.sort(completions);
               return completions;
            }

            return this.plugin.getSection(args[1]).getItemLocs();
         }
      default:
         return null;
      }
   }
}
