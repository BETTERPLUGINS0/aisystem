package me.gypopo.economyshopgui.commands.editshop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.base.PluginCommands;
import me.gypopo.economyshopgui.commands.editshop.subcommands.AddHandItem;
import me.gypopo.economyshopgui.commands.editshop.subcommands.AddItem;
import me.gypopo.economyshopgui.commands.editshop.subcommands.AddSection;
import me.gypopo.economyshopgui.commands.editshop.subcommands.DeleteItem;
import me.gypopo.economyshopgui.commands.editshop.subcommands.DeleteSection;
import me.gypopo.economyshopgui.commands.editshop.subcommands.EditItem;
import me.gypopo.economyshopgui.commands.editshop.subcommands.EditSection;
import me.gypopo.economyshopgui.commands.editshop.subcommands.Import;
import me.gypopo.economyshopgui.commands.editshop.subcommands.InstallLayout;
import me.gypopo.economyshopgui.commands.editshop.subcommands.Migrate;
import me.gypopo.economyshopgui.commands.editshop.subcommands.ShopStands;
import me.gypopo.economyshopgui.commands.editshop.subcommands.TransactionLogs;
import me.gypopo.economyshopgui.commands.editshop.subcommands.UpdateLayout;
import me.gypopo.economyshopgui.commands.editshop.subcommands.UploadLayout;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.PermissionsCache;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class CommandManager extends PluginCommands {
   private final ArrayList<SubCommad> subCommands = new ArrayList();

   public CommandManager(EconomyShopGUI plugin, List<String> disabledWorlds) {
      super(plugin, "editshop", "Allows to edit the shop with ingame commands", "/editshop <subCommand> ...", "EconomyShopGUI.eshop", Collections.singletonList("eshop"), disabledWorlds);
      Methods methods = new Methods(plugin);
      this.subCommands.add(new AddItem(plugin, methods));
      this.subCommands.add(new EditItem(plugin, methods));
      this.subCommands.add(new DeleteItem(plugin, methods));
      this.subCommands.add(new AddHandItem(plugin, methods));
      this.subCommands.add(new Import(plugin, methods));
      this.subCommands.add(new AddSection(plugin, methods));
      this.subCommands.add(new EditSection(plugin, methods));
      this.subCommands.add(new DeleteSection(plugin, methods));
      this.subCommands.add(new Migrate(plugin, methods));
      this.subCommands.add(new UploadLayout());
      this.subCommands.add(new InstallLayout());
      this.subCommands.add(new UpdateLayout());
      this.subCommands.add(new ShopStands(plugin, methods));
      this.subCommands.add(new TransactionLogs(plugin, methods));
   }

   public boolean execute(CommandSender sender, String label, String[] args) {
      if (this.plugin.badYMLParse != null) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4This command cannot be executed now, please fix the configuration formatting first!"));
         return true;
      } else {
         if (sender instanceof Player) {
            Player player = (Player)sender;
            if (this.hasAccessInWorld(player)) {
               if (args.length > 0) {
                  Iterator var8 = this.subCommands.iterator();

                  while(var8.hasNext()) {
                     SubCommad subCommad = (SubCommad)var8.next();
                     if (args[0].toLowerCase().equalsIgnoreCase(subCommad.getName())) {
                        if (subCommad.hasPermission(sender)) {
                           subCommad.perform(sender, args);
                        } else {
                           SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS.get());
                        }

                        return true;
                     }
                  }
               }

               if (this.hasPermission(player)) {
                  this.sendAllSyntaxes(sender);
               } else {
                  SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS.get());
               }
            }
         } else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender || sender instanceof RemoteConsoleCommandSender) {
            if (args.length > 0) {
               if (args[0].equalsIgnoreCase("addhanditem") || args[0].equalsIgnoreCase("uploadLayout") || args[0].equalsIgnoreCase("installLayout")) {
                  SendMessage.warnMessage(sender, (Translatable)Lang.REAL_PLAYER.get());
                  return true;
               }

               Iterator var4 = this.subCommands.iterator();

               while(var4.hasNext()) {
                  SubCommad subCommad = (SubCommad)var4.next();
                  if (args[0].toLowerCase().equalsIgnoreCase(subCommad.getName())) {
                     subCommad.perform(sender, args);
                     return true;
                  }
               }
            }

            this.sendAllSyntaxes(sender);
         }

         return true;
      }
   }

   private boolean hasPermission(CommandSender source) {
      return this.subCommands.stream().anyMatch((c) -> {
         return c.hasPermission(source);
      });
   }

   public List<String> tabComplete(CommandSender commandSender, String s, String[] args) {
      if (!this.hasPermission(commandSender)) {
         return Collections.emptyList();
      } else {
         List<String> tabCompletions = new ArrayList();
         Iterator var5;
         SubCommad subCommad;
         if (args.length == 1) {
            var5 = this.subCommands.iterator();

            while(var5.hasNext()) {
               subCommad = (SubCommad)var5.next();
               if (PermissionsCache.hasPermission(commandSender, "EconomyShopGUI.eshop." + subCommad.getName())) {
                  tabCompletions.add(subCommad.getName());
               }
            }

            if (!args[0].isEmpty()) {
               List<String> completions = new ArrayList();
               StringUtil.copyPartialMatches(args[0], tabCompletions, completions);
               Collections.sort(completions);
               return completions;
            } else {
               return tabCompletions;
            }
         } else {
            if (args.length >= 2) {
               var5 = this.subCommands.iterator();

               while(var5.hasNext()) {
                  subCommad = (SubCommad)var5.next();
                  if (args[0].equalsIgnoreCase(subCommad.getName())) {
                     return subCommad.getTabCompletion(args);
                  }
               }
            }

            return Collections.emptyList();
         }
      }
   }

   private void sendAllSyntaxes(Object logger) {
      SendMessage.sendMessage(logger, ChatColor.DARK_GREEN + "----------------------------------------");
      Iterator var2 = this.subCommands.iterator();

      while(true) {
         SubCommad subCommad;
         do {
            if (!var2.hasNext()) {
               SendMessage.sendMessage(logger, ChatColor.DARK_GREEN + "----------------------------------------");
               return;
            }

            subCommad = (SubCommad)var2.next();
         } while(logger instanceof ConsoleCommandSender && subCommad.getName().equals("addhanditem"));

         SendMessage.sendMessage(logger, " ");
         SendMessage.sendMessage(logger, ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + subCommad.getSyntax() + " - " + subCommad.getDescription());
         SendMessage.sendMessage(logger, " ");
      }
   }
}
