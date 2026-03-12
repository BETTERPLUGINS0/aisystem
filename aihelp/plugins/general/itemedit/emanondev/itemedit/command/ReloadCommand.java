package emanondev.itemedit.command;

import emanondev.itemedit.APlugin;
import emanondev.itemedit.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements TabExecutor {
   private final APlugin plugin;
   private final String permission;

   public ReloadCommand(APlugin plugin) {
      this.plugin = plugin;
      this.permission = plugin.getName().toLowerCase(Locale.ENGLISH) + "." + plugin.getName().toLowerCase(Locale.ENGLISH) + "reload";
   }

   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      return new ArrayList();
   }

   public void sendPermissionLackMessage(@NotNull String permission, CommandSender sender) {
      Util.sendMessage(sender, this.plugin.getLanguageConfig(sender).loadMessage("lack-permission", "&cYou lack of permission %permission%", sender instanceof Player ? (Player)sender : null, true, "%permission%", permission));
   }

   public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (sender.hasPermission(this.permission)) {
         this.plugin.onReload();
         Util.sendMessage(sender, this.plugin.getLanguageConfig(sender).loadMessage(this.plugin.getName().toLowerCase(Locale.ENGLISH) + "reload.success", "", true));
      } else {
         this.sendPermissionLackMessage(this.permission, sender);
      }

      return true;
   }

   public void register() {
      try {
         this.plugin.registerCommand(this.plugin.getName().toLowerCase(Locale.ENGLISH) + "reload", this, (List)null);
      } catch (Exception var2) {
         this.plugin.log("Unable to register command " + ChatColor.YELLOW + this.plugin.getName().toLowerCase(Locale.ENGLISH) + "reload");
         var2.printStackTrace();
      }

   }
}
