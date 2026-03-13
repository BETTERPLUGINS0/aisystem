package tntrun.commands;

import java.util.Iterator;
import java.util.StringJoiner;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;
import tntrun.utils.Bars;
import tntrun.utils.Utils;

public class ConsoleCommands implements CommandExecutor {
   private TNTRun plugin;

   public ConsoleCommands(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (!(sender instanceof ConsoleCommandSender) && !(sender instanceof RemoteConsoleCommandSender) && !(sender instanceof BlockCommandSender)) {
         sender.sendMessage("Console is expected");
         return false;
      } else if (args.length != 0 && !args[0].equalsIgnoreCase("info")) {
         Arena arena;
         if (args.length == 2 && args[0].equalsIgnoreCase("disable")) {
            arena = this.plugin.amanager.getArenaByName(args[1]);
            if (arena != null) {
               arena.getStatusManager().disableArena();
               sender.sendMessage("Arena disabled");
               return true;
            } else {
               Messages.sendMessage(sender, Messages.arenanotexist.replace("{ARENA}", args[1]));
               return false;
            }
         } else if (args.length == 2 && args[0].equalsIgnoreCase("enable")) {
            arena = this.plugin.amanager.getArenaByName(args[1]);
            if (arena == null) {
               Messages.sendMessage(sender, Messages.arenanotexist.replace("{ARENA}", args[1]));
               return false;
            } else {
               if (arena.getStatusManager().isArenaEnabled()) {
                  sender.sendMessage("Arena already enabled.");
               } else if (arena.getStatusManager().enableArena()) {
                  sender.sendMessage("Arena enabled");
               } else {
                  sender.sendMessage("Arena is not configured. Reason: " + arena.getStructureManager().isArenaConfigured());
               }

               return true;
            }
         } else {
            int entries;
            if (args.length >= 1 && args[0].equalsIgnoreCase("leaderboard")) {
               if (!this.plugin.useStats()) {
                  Messages.sendMessage(sender, Messages.statsdisabled);
                  return false;
               } else {
                  entries = this.plugin.getConfig().getInt("leaderboard.maxentries", 10);
                  if (args.length >= 2 && Utils.isNumber(args[1]) && Integer.parseInt(args[1]) > 0 && Integer.parseInt(args[1]) <= entries) {
                     entries = Integer.parseInt(args[1]);
                  }

                  Messages.sendMessage(sender, Messages.leaderhead, false);
                  this.plugin.getStats().getLeaderboard(sender, entries);
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("list")) {
               entries = this.plugin.amanager.getArenas().size();
               Messages.sendMessage(sender, Messages.availablearenas.replace("{COUNT}", String.valueOf(entries)));
               if (entries == 0) {
                  return true;
               } else {
                  StringJoiner message = new StringJoiner(" : ");
                  this.plugin.amanager.getArenasNames().stream().sorted().forEach((arenaname) -> {
                     if (this.plugin.amanager.getArenaByName(arenaname).getStatusManager().isArenaEnabled()) {
                        message.add("&a" + arenaname);
                     } else {
                        message.add("&c" + arenaname + "&a");
                     }

                  });
                  Messages.sendMessage(sender, message.toString(), false);
                  return true;
               }
            } else {
               if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
                  arena = this.plugin.amanager.getArenaByName(args[1]);
                  if (arena == null) {
                     Messages.sendMessage(sender, Messages.arenanotexist.replace("{ARENA}", args[1]));
                     return false;
                  }

                  if (arena.getPlayersManager().getPlayersCount() <= 1) {
                     Messages.sendMessage(sender, Messages.playersrequiredtostart);
                     return false;
                  }

                  if (!arena.getStatusManager().isArenaStarting()) {
                     Messages.sendMessage(sender, "Arena " + arena.getArenaName() + " force-started by console");
                     arena.getGameHandler().forceStartByCommand();
                     return true;
                  }
               } else {
                  if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("cmds")) {
                     this.displayConsoleCommands(sender);
                     return true;
                  }

                  if (args[0].equalsIgnoreCase("reloadconfig")) {
                     this.plugin.reloadConfig();
                     this.plugin.getSignEditor().loadConfiguration();
                     sender.sendMessage("Config reloaded");
                     return true;
                  }

                  if (args[0].equalsIgnoreCase("reloadmsg")) {
                     Messages.loadMessages(this.plugin);
                     sender.sendMessage("Messages reloaded");
                     return true;
                  }

                  if (args[0].equalsIgnoreCase("reloadbars")) {
                     Bars.loadBars(this.plugin);
                     sender.sendMessage("Bars reloaded");
                     return true;
                  }

                  Player onlinePlayer;
                  if (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("spectate")) {
                     if (args.length != 3) {
                        Messages.sendMessage(sender, "&c Invalid number of arguments supplied");
                        return false;
                     }

                     arena = this.plugin.amanager.getArenaByName(args[1]);
                     if (arena == null) {
                        Messages.sendMessage(sender, Messages.arenanotexist.replace("{ARENA}", args[1]));
                        return false;
                     }

                     onlinePlayer = Bukkit.getPlayer(args[2]);
                     if (onlinePlayer != null && onlinePlayer.isOnline()) {
                        if (args[0].equalsIgnoreCase("join")) {
                           if (!arena.getPlayerHandler().checkJoin(onlinePlayer)) {
                              Messages.sendMessage(sender, "&c Player cannot join the arena at this time");
                              return false;
                           }

                           arena.getPlayerHandler().spawnPlayer(onlinePlayer, Messages.playerjoinedtoothers);
                           return true;
                        }

                        if (!arena.getPlayerHandler().canSpectate(onlinePlayer)) {
                           Messages.sendMessage(sender, "&c Player cannot spectate the arena at this time");
                           return false;
                        }

                        arena.getPlayerHandler().spectatePlayer(onlinePlayer, Messages.playerjoinedasspectator, "");
                        if (Utils.debug()) {
                           Logger var10000 = this.plugin.getLogger();
                           String var10001 = onlinePlayer.getName();
                           var10000.info("Player " + var10001 + " joined arena " + arena.getArenaName() + " as a spectator");
                        }

                        return true;
                     }

                     Messages.sendMessage(sender, Messages.playernotonline.replace("{PLAYER}", args[2]));
                     return false;
                  }

                  if (args[0].equalsIgnoreCase("autojoin")) {
                     if (args.length != 3 && args.length != 2) {
                        Messages.sendMessage(sender, "&c Invalid number of arguments supplied");
                        return false;
                     }

                     Player player = args.length == 2 ? Bukkit.getPlayer(args[1]) : Bukkit.getPlayer(args[2]);
                     String arenatype;
                     if (player != null && player.isOnline()) {
                        if (args.length == 3 && !args[1].equalsIgnoreCase("pvp") && !args[1].equalsIgnoreCase("nopvp")) {
                           Messages.sendMessage(sender, "&c Invalid argument supplied");
                           return false;
                        }

                        arenatype = args.length == 3 ? args[1] : "";
                        this.plugin.getMenus().autoJoin(player, arenatype);
                        return true;
                     }

                     arenatype = player != null ? player.getName() : "Player";
                     Messages.sendMessage(sender, Messages.playernotonline.replace("{PLAYER}", arenatype));
                     return false;
                  }

                  if (args[0].equalsIgnoreCase("givedoublejumps")) {
                     if (args.length != 3) {
                        Messages.sendMessage(sender, "&c Invalid number of arguments supplied");
                        return false;
                     }

                     arena = this.plugin.amanager.getPlayerArena(args[1]);
                     if (arena == null) {
                        Messages.sendMessage(sender, "&7 " + args[1] + "&c is not in a TNTRun arena");
                        return false;
                     }

                     if (Utils.isNumber(args[2]) && Integer.parseInt(args[2]) > 0) {
                        arena.getPlayerHandler().incrementDoubleJumps(args[1], Integer.parseInt(args[2]));
                        Messages.sendMessage(sender, "&6 " + args[2] + "&7 doublejumps given to: &6" + args[1]);
                        if (!arena.getStatusManager().isArenaStarting() && this.plugin.getConfig().getBoolean("scoreboard.displaydoublejumps") && this.plugin.getConfig().getBoolean("special.UseScoreboard")) {
                           arena.getScoreboardHandler().updateWaitingScoreboard(Bukkit.getPlayer(args[1]));
                        }

                        return true;
                     }

                     Messages.sendMessage(sender, "&c DoubleJumps must be a positive integer");
                     return false;
                  }

                  if (args[0].equalsIgnoreCase("forcejoin")) {
                     if (args.length != 2) {
                        Messages.sendMessage(sender, "&c Invalid number of arguments supplied");
                        return false;
                     }

                     arena = this.plugin.amanager.getArenaByName(args[1]);
                     if (arena == null) {
                        Messages.sendMessage(sender, Messages.arenanotexist.replace("{ARENA}", args[1]));
                        return false;
                     }

                     Iterator var7 = Bukkit.getOnlinePlayers().iterator();

                     while(var7.hasNext()) {
                        onlinePlayer = (Player)var7.next();
                        if (!onlinePlayer.hasPermission("tntrun.forcejoinbypass") && arena.getPlayerHandler().checkJoin(onlinePlayer)) {
                           arena.getPlayerHandler().spawnPlayer(onlinePlayer, Messages.playerjoinedtoothers);
                        }
                     }

                     return true;
                  }
               }

               return false;
            }
         }
      } else {
         Utils.displayInfo(sender);
         return true;
      }
   }

   private void displayConsoleCommands(CommandSender sender) {
      Messages.sendMessage(sender, "trconsole help");
      Messages.sendMessage(sender, "trconsole list");
      Messages.sendMessage(sender, "trconsole info");
      Messages.sendMessage(sender, "trconsole enable {arena}");
      Messages.sendMessage(sender, "trconsole disable {arena}");
      Messages.sendMessage(sender, "trconsole start {arena}");
      Messages.sendMessage(sender, "trconsole reloadconfig");
      Messages.sendMessage(sender, "trconsole reloadmessages");
      Messages.sendMessage(sender, "trconsole reloadbars");
      Messages.sendMessage(sender, "trconsole leaderboard");
      Messages.sendMessage(sender, "trconsole join {arena} {player}");
      Messages.sendMessage(sender, "trconsole spectate {arena} {player}");
      Messages.sendMessage(sender, "trconsole autojoin [pvp|nopvp] {player}");
      Messages.sendMessage(sender, "trconsole givedoublejumps {player} {amount}");
      Messages.sendMessage(sender, "trconsole forcejoin {arena}");
   }
}
