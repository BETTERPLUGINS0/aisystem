package tntrun.commands;

import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Logger;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;
import tntrun.utils.Utils;

public class GameCommands implements CommandExecutor {
   private TNTRun plugin;

   public GameCommands(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (!(sender instanceof Player)) {
         Messages.sendMessage(sender, "&c You must be a player");
         return false;
      } else {
         Player player = (Player)sender;
         if (args.length < 1) {
            Messages.sendMessage(player, "&7============" + Messages.trprefix + "============", false);
            Messages.sendMessage(player, "&c Please use &6/tr help");
            return false;
         } else {
            if (args[0].equalsIgnoreCase("help")) {
               Messages.sendMessage(player, "&7============" + Messages.trprefix + "============", false);
               player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr lobby", true), Utils.getTextComponent(Messages.helplobby)});
               player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr list [arena]", true), Utils.getTextComponent(Messages.helplist)});
               player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr join [arena]", true), Utils.getTextComponent(Messages.helpjoin)});
               player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr spectate {arena}", true), Utils.getTextComponent(Messages.helpspectate)});
               player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr autojoin [pvp|nopvp]", true), Utils.getTextComponent(Messages.helpautojoin)});
               player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr leave", true), Utils.getTextComponent(Messages.helpleave)});
               player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr vote", true), Utils.getTextComponent(Messages.helpvote)});
               player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr info", true), Utils.getTextComponent(Messages.helpinfo)});
               player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr stats", true), Utils.getTextComponent(Messages.helpstats)});
               player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr leaderboard [size]", true), Utils.getTextComponent(Messages.helplb)});
               player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr listkit [kit]", true), Utils.getTextComponent(Messages.helplistkit)});
               player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr listrewards {arena}", true), Utils.getTextComponent(Messages.helplistrewards)});
               player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr start {arena}", true), Utils.getTextComponent(Messages.helpstart)});
               if (player.hasPermission("tntrun.setup")) {
                  player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/tr cmds", true), Utils.getTextComponent(Messages.helpcmds)});
               }
            } else if (args[0].equalsIgnoreCase("lobby")) {
               this.plugin.getGlobalLobby().joinLobby(player);
            } else {
               int kitcount;
               StringJoiner message;
               Arena arena;
               String var10001;
               if (args[0].equalsIgnoreCase("list")) {
                  if (args.length >= 2) {
                     arena = this.plugin.amanager.getArenaByName(args[1]);
                     if (arena == null) {
                        Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[1]));
                        return false;
                     }

                     var10001 = Messages.trprefix;
                     Messages.sendMessage(player, "&7============" + var10001 + "============", false);
                     var10001 = arena.getArenaName();
                     Messages.sendMessage(player, "&7Arena Details: &a" + var10001, false);
                     String arenaStatus = arena.getStatusManager().isArenaEnabled() ? "Enabled" : "Disabled";
                     String var10000 = String.valueOf(ChatColor.BOLD);
                     String bigspace = var10000 + " ";
                     var10001 = String.valueOf(ChatColor.GOLD);
                     player.sendMessage(var10001 + "Status " + String.valueOf(ChatColor.DARK_GRAY) + "............................ " + String.valueOf(ChatColor.RED) + arenaStatus);
                     var10001 = String.valueOf(ChatColor.GOLD);
                     player.sendMessage(var10001 + "Min Players " + String.valueOf(ChatColor.DARK_GRAY) + "..............." + bigspace + String.valueOf(ChatColor.RED) + arena.getStructureManager().getMinPlayers());
                     var10001 = String.valueOf(ChatColor.GOLD);
                     player.sendMessage(var10001 + "Max Players " + String.valueOf(ChatColor.DARK_GRAY) + "............." + bigspace + String.valueOf(ChatColor.RED) + arena.getStructureManager().getMaxPlayers());
                     var10001 = String.valueOf(ChatColor.GOLD);
                     player.sendMessage(var10001 + "Time Limit " + String.valueOf(ChatColor.DARK_GRAY) + "...................... " + String.valueOf(ChatColor.RED) + arena.getStructureManager().getTimeLimit() + " seconds");
                     var10001 = String.valueOf(ChatColor.GOLD);
                     player.sendMessage(var10001 + "Countdown " + String.valueOf(ChatColor.DARK_GRAY) + ".................. " + String.valueOf(ChatColor.RED) + arena.getStructureManager().getCountdown() + " seconds");
                     var10001 = String.valueOf(ChatColor.GOLD);
                     player.sendMessage(var10001 + "Teleport to " + String.valueOf(ChatColor.DARK_GRAY) + "................ " + String.valueOf(ChatColor.RED) + Utils.getTitleCase(arena.getStructureManager().getTeleportDestination().toString()));
                     var10001 = String.valueOf(ChatColor.GOLD);
                     player.sendMessage(var10001 + "Player Count " + String.valueOf(ChatColor.DARK_GRAY) + "..........." + bigspace + String.valueOf(ChatColor.RED) + arena.getPlayersManager().getPlayersCount());
                     var10001 = String.valueOf(ChatColor.GOLD);
                     player.sendMessage(var10001 + "Vote Percent " + String.valueOf(ChatColor.DARK_GRAY) + "........... " + String.valueOf(ChatColor.RED) + arena.getStructureManager().getVotePercent());
                     var10001 = String.valueOf(ChatColor.GOLD);
                     player.sendMessage(var10001 + "PVP Damage " + String.valueOf(ChatColor.DARK_GRAY) + "............... " + String.valueOf(ChatColor.RED) + Utils.getTitleCase(arena.getStructureManager().getDamageEnabled().toString()));
                     String result = arena.getStructureManager().isKitsEnabled() ? "Yes" : "No";
                     var10001 = String.valueOf(ChatColor.GOLD);
                     player.sendMessage(var10001 + "Kits Enabled " + String.valueOf(ChatColor.DARK_GRAY) + "............." + bigspace + String.valueOf(ChatColor.RED) + result);
                     List<String> kitnames = arena.getStructureManager().getLinkedKits();
                     if (kitnames.size() > 0) {
                        var10001 = String.valueOf(ChatColor.GOLD);
                        player.sendMessage(var10001 + "Linked Kits " + String.valueOf(ChatColor.DARK_GRAY) + "................." + bigspace + String.valueOf(ChatColor.RED) + String.join(", ", kitnames));
                     }

                     var10001 = String.valueOf(ChatColor.GOLD);
                     player.sendMessage(var10001 + "Rewards " + String.valueOf(ChatColor.DARK_GRAY) + "....................... " + String.valueOf(ChatColor.RED) + "Use command '/tr listrewards {arena}'");
                     if (arena.getStructureManager().getFee() > 0.0D) {
                        var10001 = String.valueOf(ChatColor.GOLD);
                        player.sendMessage(var10001 + "Join Fee " + String.valueOf(ChatColor.DARK_GRAY) + "....................... " + String.valueOf(ChatColor.RED) + arena.getStructureManager().getArenaCost());
                     }

                     if (arena.getStructureManager().isTestMode()) {
                        var10001 = String.valueOf(ChatColor.GOLD);
                        player.sendMessage(var10001 + "Test Mode " + String.valueOf(ChatColor.DARK_GRAY) + "..................." + bigspace + String.valueOf(ChatColor.RED) + "Enabled");
                     }

                     if (arena.getStructureManager().isArenaStatsEnabled()) {
                        var10001 = String.valueOf(ChatColor.GOLD);
                        player.sendMessage(var10001 + "Arena Stats " + String.valueOf(ChatColor.DARK_GRAY) + ".............." + bigspace + String.valueOf(ChatColor.RED) + "Enabled");
                     }

                     return true;
                  }

                  kitcount = this.plugin.amanager.getArenas().size();
                  Messages.sendMessage(player, Messages.availablearenas.replace("{COUNT}", String.valueOf(kitcount)));
                  if (kitcount == 0) {
                     return false;
                  }

                  message = new StringJoiner(" : ");
                  this.plugin.amanager.getArenasNames().stream().sorted().forEach((arenaname) -> {
                     if (this.plugin.amanager.getArenaByName(arenaname).getStatusManager().isArenaEnabled()) {
                        message.add("&a" + arenaname);
                     } else {
                        message.add("&c" + arenaname + "&a");
                     }

                  });
                  Messages.sendMessage(player, message.toString(), false);
               } else if (args[0].equalsIgnoreCase("join")) {
                  if (args.length == 1 && player.hasPermission("tntrun.joinmenu")) {
                     this.plugin.getMenus().buildJoinMenu(player);
                     return true;
                  }

                  if (args.length != 2) {
                     Messages.sendMessage(player, "&c Invalid number of arguments supplied");
                     return false;
                  }

                  arena = this.plugin.amanager.getArenaByName(args[1]);
                  if (arena == null) {
                     Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[1]));
                     return false;
                  }

                  if (arena.getPlayerHandler().checkJoin(player)) {
                     arena.getPlayerHandler().spawnPlayer(player, Messages.playerjoinedtoothers);
                  }
               } else {
                  Logger var14;
                  if (args[0].equalsIgnoreCase("joinorspectate")) {
                     if (args.length != 2) {
                        return false;
                     }

                     arena = this.plugin.amanager.getArenaByName(args[1]);
                     if (arena == null) {
                        return false;
                     }

                     if (!arena.getStatusManager().isArenaRunning()) {
                        if (arena.getPlayerHandler().checkJoin(player)) {
                           arena.getPlayerHandler().spawnPlayer(player, Messages.playerjoinedtoothers);
                        }
                     } else {
                        if (!this.plugin.getConfig().getBoolean("invitationmessage.allowspectate")) {
                           Messages.sendMessage(player, arena.getStatusManager().getFormattedMessage(Messages.arenarunning));
                           return false;
                        }

                        if (!arena.getPlayerHandler().canSpectate(player)) {
                           return false;
                        }

                        arena.getPlayerHandler().spectatePlayer(player, Messages.playerjoinedasspectator, "");
                        if (Utils.debug()) {
                           var14 = this.plugin.getLogger();
                           var10001 = player.getName();
                           var14.info("Player " + var10001 + " joined arena " + arena.getArenaName() + " as a spectator");
                        }
                     }
                  } else if (args[0].equalsIgnoreCase("spectate")) {
                     if (args.length != 2) {
                        Messages.sendMessage(player, "&c Invalid number of arguments supplied");
                        return false;
                     }

                     arena = this.plugin.amanager.getArenaByName(args[1]);
                     if (arena == null) {
                        Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[1]));
                        return false;
                     }

                     if (!arena.getPlayerHandler().canSpectate(player)) {
                        return false;
                     }

                     arena.getPlayerHandler().spectatePlayer(player, Messages.playerjoinedasspectator, "");
                     if (Utils.debug()) {
                        var14 = this.plugin.getLogger();
                        var10001 = player.getName();
                        var14.info("Player " + var10001 + " joined arena " + arena.getArenaName() + " as a spectator");
                     }
                  } else {
                     String uuid;
                     if (args[0].equalsIgnoreCase("autojoin")) {
                        if (args.length >= 2 && !args[1].equalsIgnoreCase("pvp") && !args[1].equalsIgnoreCase("nopvp")) {
                           Messages.sendMessage(player, "&c Invalid argument supplied");
                           return false;
                        }

                        uuid = args.length >= 2 ? args[1] : "";
                        this.plugin.getMenus().autoJoin(player, uuid);
                     } else if (args[0].equalsIgnoreCase("info")) {
                        Utils.displayInfo(player);
                     } else if (args[0].equalsIgnoreCase("stats")) {
                        if (!this.plugin.useStats()) {
                           Messages.sendMessage(player, Messages.statsdisabled);
                           return false;
                        }

                        uuid = this.plugin.getStats().getPlayerUUID(player);
                        Messages.sendMessage(player, Messages.statshead, false);
                        var10001 = Messages.gamesplayed;
                        Messages.sendMessage(player, var10001 + this.plugin.getStats().getPlayedGames(uuid), false);
                        var10001 = Messages.gameswon;
                        Messages.sendMessage(player, var10001 + this.plugin.getStats().getWins(uuid), false);
                        var10001 = Messages.gameslost;
                        Messages.sendMessage(player, var10001 + this.plugin.getStats().getLosses(uuid), false);
                        var10001 = Messages.winstreak;
                        Messages.sendMessage(player, var10001 + this.plugin.getPData().getWinStreak(player), false);
                     } else if (args[0].equalsIgnoreCase("leaderboard")) {
                        if (!this.plugin.useStats()) {
                           Messages.sendMessage(player, Messages.statsdisabled);
                           return false;
                        }

                        kitcount = this.plugin.getConfig().getInt("leaderboard.maxentries", 10);
                        if (args.length > 1 && Utils.isNumber(args[1]) && Integer.parseInt(args[1]) > 0 && Integer.parseInt(args[1]) <= kitcount) {
                           kitcount = Integer.parseInt(args[1]);
                        }

                        Messages.sendMessage(player, Messages.leaderhead, false);
                        this.plugin.getStats().getLeaderboard(player, kitcount);
                     } else if (args[0].equalsIgnoreCase("leave")) {
                        arena = this.plugin.amanager.getPlayerArena(player.getName());
                        if (arena == null) {
                           Messages.sendMessage(player, Messages.playernotinarena);
                           return false;
                        }

                        arena.getPlayerHandler().leavePlayer(player, Messages.playerlefttoplayer, Messages.playerlefttoothers);
                     } else if (args[0].equalsIgnoreCase("cmds")) {
                        if (!player.hasPermission("tntrun.setup")) {
                           Messages.sendMessage(player, Messages.nopermission);
                           return false;
                        }

                        Messages.sendMessage(player, "&7============" + Messages.trprefix + "============", false);
                        Utils.displayHelp(player);
                        Messages.sendMessage(player, "&7============[&6Other commands&7]============", false);
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup deletespectate {arena}", true), Utils.getTextComponent(Messages.setupdelspectate)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setgameleveldestroydelay {arena} {ticks}", true), Utils.getTextComponent(Messages.setupdelay)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setregenerationdelay {arena} {ticks}", true), Utils.getTextComponent(Messages.setupregendelay)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setmaxplayers {arena} {players}", true), Utils.getTextComponent(Messages.setupmax)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setminplayers {arena} {players}", true), Utils.getTextComponent(Messages.setupmin)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setvotepercent {arena} {0<votepercent<1}", true), Utils.getTextComponent(Messages.setupvote)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup settimelimit {arena} {seconds}", true), Utils.getTextComponent(Messages.setuptimelimit)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setcountdown {arena} {seconds}", true), Utils.getTextComponent(Messages.setupcountdown)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setmoneyreward {arena} {amount}", true), Utils.getTextComponent(Messages.setupmoney)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setteleport {arena} {previous/lobby}", true), Utils.getTextComponent(Messages.setupteleport)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setdamage {arena} {yes/no/zero}", true), Utils.getTextComponent(Messages.setupdamage)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup addkit {kitname}", true), Utils.getTextComponent(Messages.setupaddkit)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup deletekit {kitname}", true), Utils.getTextComponent(Messages.setupdelkit)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup enablekits {arena}", true), Utils.getTextComponent(Messages.setupenablekits)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup disablekits {arena}", true), Utils.getTextComponent(Messages.setupdisablekits)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setbarcolor", true), Utils.getTextComponent(Messages.setupbarcolor)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setP1", true), Utils.getTextComponent(Messages.setupp1)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setP2", true), Utils.getTextComponent(Messages.setupp2)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup clear", true), Utils.getTextComponent(Messages.setupclear)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup configure {arena}", true), Utils.getTextComponent(Messages.setupconfigure)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup reloadbars", true), Utils.getTextComponent(Messages.setupreloadbars)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup reloadtitles", true), Utils.getTextComponent(Messages.setupreloadtitles)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup reloadmsg", true), Utils.getTextComponent(Messages.setupreloadmsg)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup reloadconfig", true), Utils.getTextComponent(Messages.setupreloadconfig)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup enable {arena}", true), Utils.getTextComponent(Messages.setupenable)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup disable {arena}", true), Utils.getTextComponent(Messages.setupdisable)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup delete {arena}", true), Utils.getTextComponent(Messages.setupdelete)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setreward {arena}", true), Utils.getTextComponent(Messages.setupreward)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setfee {arena} {amount}", true), Utils.getTextComponent(Messages.setupfee)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setcurrency {arena} {item}", true), Utils.getTextComponent(Messages.setupcurrency)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup setlobby", true), Utils.getTextComponent(Messages.setuplobby)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup deletelobby", true), Utils.getTextComponent(Messages.setupdellobby)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup addspawn", true), Utils.getTextComponent(Messages.setupaddspawn)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup deletespawnpoints", true), Utils.getTextComponent(Messages.setupdelspawns)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup addtowhitelist", true), Utils.getTextComponent(Messages.setupwhitelist)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup resetstats {player}", true), Utils.getTextComponent(Messages.setupresetstats)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup resetcachedrank {player}", true), Utils.getTextComponent(Messages.setupresetrank)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup forcejoin {arena}", true), Utils.getTextComponent(Messages.setupforcejoin)});
                        player.spigot().sendMessage(new BaseComponent[]{Utils.getTextComponent("/trsetup help", true), Utils.getTextComponent(Messages.setuphelp)});
                     } else if (args[0].equalsIgnoreCase("vote")) {
                        arena = this.plugin.amanager.getPlayerArena(player.getName());
                        if (arena == null) {
                           Messages.sendMessage(player, Messages.playernotinarena);
                           return false;
                        }

                        if (!arena.getPlayersManager().getPlayers().contains(player)) {
                           Messages.sendMessage(player, Messages.playercannotvote);
                           return false;
                        }

                        if (!arena.getPlayerHandler().vote(player)) {
                           Messages.sendMessage(player, Messages.playeralreadyvotedforstart);
                           return false;
                        }

                        Messages.sendMessage(player, Messages.playervotedforstart);
                     } else if (!args[0].equalsIgnoreCase("listkit") && !args[0].equalsIgnoreCase("listkits")) {
                        if (args[0].equalsIgnoreCase("listrewards")) {
                           if (!player.hasPermission("tntrun.listrewards")) {
                              Messages.sendMessage(player, Messages.nopermission);
                              return false;
                           }

                           if (args.length != 2) {
                              Messages.sendMessage(player, "&c Invalid number of arguments supplied");
                              return false;
                           }

                           arena = this.plugin.amanager.getArenaByName(args[1]);
                           if (arena == null) {
                              Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[1]));
                              return false;
                           }

                           arena.getStructureManager().getRewards().listRewards(player, args[1]);
                        } else if (args[0].equalsIgnoreCase("start")) {
                           if (!player.hasPermission("tntrun.start")) {
                              Messages.sendMessage(player, Messages.nopermission);
                              return false;
                           }

                           if (args.length != 2) {
                              Messages.sendMessage(player, "&c Invalid number of arguments supplied");
                              return false;
                           }

                           arena = this.plugin.amanager.getArenaByName(args[1]);
                           if (arena == null) {
                              Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[1]));
                              return false;
                           }

                           if (arena.getPlayersManager().getPlayersCount() <= 1) {
                              Messages.sendMessage(player, Messages.playersrequiredtostart);
                              return false;
                           }

                           if (arena.getStatusManager().isArenaStarting()) {
                              Messages.sendMessage(player, arena.getStatusManager().getFormattedMessage(Messages.arenastarting));
                              return false;
                           }

                           ConsoleCommandSender var15 = this.plugin.getServer().getConsoleSender();
                           var10001 = String.valueOf(ChatColor.GOLD);
                           var15.sendMessage("[TNTRun_reloaded] Arena " + var10001 + arena.getArenaName() + String.valueOf(ChatColor.WHITE) + " force-started by " + String.valueOf(ChatColor.AQUA) + player.getName());
                           arena.getGameHandler().forceStartByCommand();
                        } else {
                           if (!args[0].equalsIgnoreCase("party")) {
                              Messages.sendMessage(player, "&c Invalid argument supplied, please use &6/tr help");
                              return false;
                           }

                           if (!player.hasPermission("tntrun.party")) {
                              Messages.sendMessage(player, Messages.nopermission);
                              return false;
                           }

                           if (!this.plugin.isParties()) {
                              Messages.sendMessage(player, Messages.partynotenabled);
                              return false;
                           }

                           if (!this.validatePartyArgs(args)) {
                              Messages.sendMessage(player, "&c Invalid number of arguments supplied");
                              return false;
                           }

                           this.plugin.getParties().handleCommand(player, args);
                        }
                     } else {
                        if (args.length >= 2) {
                           this.plugin.getKitManager().listKit(args[1], player);
                           return true;
                        }

                        kitcount = this.plugin.getKitManager().getKits().size();
                        Messages.sendMessage(player, Messages.availablekits.replace("{COUNT}", String.valueOf(kitcount)));
                        if (kitcount == 0) {
                           return false;
                        }

                        message = new StringJoiner(" : ");
                        this.plugin.getKitManager().getKits().stream().sorted().forEach((kit) -> {
                           message.add("&a" + kit);
                        });
                        Messages.sendMessage(player, message.toString(), false);
                     }
                  }
               }
            }

            return true;
         }
      }
   }

   private boolean validatePartyArgs(String[] args) {
      if (args.length == 1) {
         return false;
      } else {
         String var2;
         switch((var2 = args[1].toLowerCase()).hashCode()) {
         case -1423461112:
            if (var2.equals("accept")) {
               return args.length == 3;
            }

            return false;
         case -1352294148:
            if (!var2.equals("create")) {
               return false;
            }
            break;
         case -1183699191:
            if (var2.equals("invite")) {
               return args.length == 3;
            }

            return false;
         case -840477601:
            if (var2.equals("unkick")) {
               return args.length == 3;
            }

            return false;
         case 3237038:
            if (!var2.equals("info")) {
               return false;
            }
            break;
         case 3291718:
            if (var2.equals("kick")) {
               return args.length == 3;
            }

            return false;
         case 102846135:
            if (!var2.equals("leave")) {
               return false;
            }
            break;
         case 1542349558:
            if (var2.equals("decline")) {
               return args.length == 3;
            }

            return false;
         default:
            return false;
         }

         if (args.length == 2) {
            return true;
         } else {
            return false;
         }
      }
   }
}
