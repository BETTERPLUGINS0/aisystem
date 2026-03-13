package tntrun.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import tntrun.TNTRun;

public class AutoTabCompleter implements TabCompleter {
   private static final List<String> COMMANDS = Arrays.asList("help", "lobby", "list", "join", "leave", "vote", "info", "stats", "listkits", "autojoin", "leaderboard", "party");
   private static final List<String> PARTY_COMMANDS = Arrays.asList("accept", "create", "decline", "info", "invite", "kick", "leave", "unkick");

   public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
      if (!cmd.getName().equalsIgnoreCase("tntrun") && !cmd.getName().equalsIgnoreCase("tr")) {
         return null;
      } else if (!(sender instanceof Player)) {
         return null;
      } else {
         List<String> list = new ArrayList();
         List<String> auto = new ArrayList();
         if (args.length == 1) {
            list.addAll(COMMANDS);
            if (sender.hasPermission("tntrun.setup")) {
               list.add("cmds");
            }

            if (sender.hasPermission("tntrun.start")) {
               list.add("start");
            }

            if (sender.hasPermission("tntrun.spectate")) {
               list.add("spectate");
            }

            if (sender.hasPermission("tntrun.listrewards")) {
               list.add("listrewards");
            }
         } else if (args.length == 2) {
            if (Stream.of("join", "list", "start", "spectate", "listrewards").anyMatch((sx) -> {
               return sx.equalsIgnoreCase(args[0]);
            })) {
               list.addAll(TNTRun.getInstance().amanager.getArenasNames());
            } else if (args[0].equalsIgnoreCase("party")) {
               list.addAll(PARTY_COMMANDS);
            } else if (!args[0].equalsIgnoreCase("listkits") && !args[0].equalsIgnoreCase("listkit")) {
               if (args[0].equalsIgnoreCase("autojoin")) {
                  list.add("nopvp");
                  list.add("pvp");
               }
            } else {
               list.addAll(TNTRun.getInstance().getKitManager().getKits());
            }
         } else if (args.length == 3 && args[0].equalsIgnoreCase("party")) {
            if (Stream.of("invite", "unkick", "accept", "decline").anyMatch((sx) -> {
               return sx.equalsIgnoreCase(args[1]);
            })) {
               list.addAll(this.getOnlinePlayerNames());
            } else if (args[1].equalsIgnoreCase("kick")) {
               list.addAll(TNTRun.getInstance().getParties().getPartyMembers(sender.getName()));
            }
         }

         Iterator var7 = list.iterator();

         while(var7.hasNext()) {
            String s = (String)var7.next();
            if (s.startsWith(args[args.length - 1])) {
               auto.add(s);
            }
         }

         return auto.isEmpty() ? list : auto;
      }
   }

   private List<String> getOnlinePlayerNames() {
      return (List)Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
   }
}
