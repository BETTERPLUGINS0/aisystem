package me.gypopo.economyshopgui.commands;

import java.util.Collections;
import java.util.List;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.base.PluginCommands;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.methodes.SendMessage;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SellGUI extends PluginCommands {
   public SellGUI(EconomyShopGUI plugin, List<String> aliases, List<String> disabledWorlds) {
      super(plugin, (String)aliases.get(0), "Opens the sell GUI", "/" + (String)aliases.remove(0), "EconomyShopGUI.sellgui", aliases, disabledWorlds);
   }

   public boolean execute(CommandSender sender, String commandLabel, String[] args) {
      if (this.plugin.badYMLParse != null) {
         SendMessage.warnMessage(sender, (String)"This command cannot be executed now, please fix the configuration formatting first!");
         return true;
      } else {
         Player p;
         if (sender instanceof Player) {
            p = (Player)sender;
            if (!this.canUse(p)) {
               return true;
            }

            (new me.gypopo.economyshopgui.objects.SellGUI()).open(p);
         } else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
            p = this.plugin.getServer().getPlayer(args[0]);
            if (p != null) {
               (new me.gypopo.economyshopgui.objects.SellGUI()).open(p);
            } else {
               SendMessage.warnMessage(sender, (Translatable)Lang.REAL_PLAYER.get());
            }
         }

         return false;
      }
   }

   public List<String> tabComplete(CommandSender commandSender, String s, String[] args) {
      return Collections.emptyList();
   }
}
