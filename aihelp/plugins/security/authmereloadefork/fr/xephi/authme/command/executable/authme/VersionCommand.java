package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.datasource.DataSourceType;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VersionCommand implements ExecutableCommand {
   @Inject
   private BukkitService bukkitService;
   @Inject
   private Settings settings;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      sender.sendMessage(ChatColor.GOLD + "==========[ " + AuthMe.getPluginName() + " ABOUT ]==========");
      sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.WHITE + AuthMe.getPluginName() + " v" + AuthMe.getPluginVersion() + ChatColor.GRAY + " (build: " + AuthMe.getPluginBuildNumber() + ")");
      sender.sendMessage(ChatColor.GOLD + "Database Implementation: " + ChatColor.WHITE + ((DataSourceType)this.settings.getProperty(DatabaseSettings.BACKEND)).toString());
      sender.sendMessage(ChatColor.GOLD + "Authors:");
      Collection<Player> onlinePlayers = this.bukkitService.getOnlinePlayers();
      printDeveloper(sender, "Gabriele C.", "sgdc3", "Project manager, Contributor", onlinePlayers);
      printDeveloper(sender, "Lucas J.", "ljacqu", "Main Developer", onlinePlayers);
      printDeveloper(sender, "games647", "games647", "Developer", onlinePlayers);
      printDeveloper(sender, "Hex3l", "Hex3l", "Developer", onlinePlayers);
      printDeveloper(sender, "krusic22", "krusic22", "Support", onlinePlayers);
      sender.sendMessage(ChatColor.GOLD + "Retired authors:");
      printDeveloper(sender, "Alexandre Vanhecke", "xephi59", "Original Author", onlinePlayers);
      printDeveloper(sender, "Gnat008", "gnat008", "Developer, Retired", onlinePlayers);
      printDeveloper(sender, "DNx5", "DNx5", "Developer, Retired", onlinePlayers);
      printDeveloper(sender, "Tim Visee", "timvisee", "Developer, Retired", onlinePlayers);
      sender.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.WHITE + "https://github.com/AuthMe/AuthMeReloaded");
      sender.sendMessage(ChatColor.GOLD + "License: " + ChatColor.WHITE + "GNU GPL v3.0" + ChatColor.GRAY + ChatColor.ITALIC + " (See LICENSE file)");
      sender.sendMessage(ChatColor.GOLD + "Copyright: " + ChatColor.WHITE + "Copyright (c) AuthMe-Team " + (new SimpleDateFormat("yyyy")).format(new Date()) + ". Released under GPL v3 License.");
   }

   private static void printDeveloper(CommandSender sender, String name, String minecraftName, String function, Collection<Player> onlinePlayers) {
      StringBuilder msg = new StringBuilder();
      msg.append(" ").append(ChatColor.WHITE).append(name);
      msg.append(ChatColor.GRAY).append(" // ").append(ChatColor.WHITE).append(minecraftName);
      msg.append(ChatColor.GRAY).append(ChatColor.ITALIC).append(" (").append(function).append(")");
      if (isPlayerOnline(minecraftName, onlinePlayers)) {
         msg.append(ChatColor.GREEN).append(ChatColor.ITALIC).append(" (In-Game)");
      }

      sender.sendMessage(msg.toString());
   }

   private static boolean isPlayerOnline(String minecraftName, Collection<Player> onlinePlayers) {
      Iterator var2 = onlinePlayers.iterator();

      Player player;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         player = (Player)var2.next();
      } while(!player.getName().equalsIgnoreCase(minecraftName));

      return true;
   }
}
