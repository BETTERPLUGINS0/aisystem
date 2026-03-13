package tntrun.commands.setup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.bukkit.boss.BarColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;

public class SetupTabCompleter implements TabCompleter {
   private static final List<String> COMMANDS = Arrays.asList("help", "create", "setlobby", "reloadbars", "reloadtitles", "reloadmsg", "reloadconfig", "setbarcolor", "addkit", "deletekit", "deletelobby", "setp1", "setp2", "clear", "addtowhitelist", "setlanguage", "resetstats", "resetcachedrank", "givedoublejumps");
   private static final List<String> ARENA_COMMANDS = Arrays.asList("setarena", "setloselevel", "setspawn", "addspawn", "setspectate", "finish", "deletespectate", "deletespawnpoints", "setgameleveldestroydelay", "setregenerationdelay", "setmaxplayers", "setminplayers", "setvotepercent", "settimelimit", "setcountdown", "setmoneyreward", "setteleport", "enable", "disable", "setreward", "enablekits", "disablekits", "linkkit", "unlinkkit", "delete", "setdamage", "setfee", "setcurrency", "configure", "forcejoin");
   private static final List<String> TELEPORT_COMMANDS = Arrays.asList("lobby", "previous");
   private static final List<String> DAMAGE_COMMANDS = Arrays.asList("yes", "no", "zero");

   public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
      if (!(sender instanceof Player)) {
         return null;
      } else if (!sender.hasPermission("tntrun.setup")) {
         return null;
      } else {
         List<String> list = new ArrayList();
         List<String> auto = new ArrayList();
         Iterator var8;
         if (args.length == 1) {
            list.addAll(COMMANDS);
            list.addAll(ARENA_COMMANDS);
         } else if (args.length == 2) {
            if (ARENA_COMMANDS.contains(args[0])) {
               list.addAll(TNTRun.getInstance().amanager.getArenasNames());
            } else if (!args[0].equalsIgnoreCase("setbarcolor") && !args[0].equalsIgnoreCase("setbarcolour")) {
               if (args[0].equalsIgnoreCase("deletekit")) {
                  list.addAll(TNTRun.getInstance().getKitManager().getKits());
               } else if (args[0].equalsIgnoreCase("setlanguage")) {
                  list.addAll(TNTRun.getInstance().getLanguage().getTranslatedLanguages());
               }
            } else {
               var8 = Arrays.asList((BarColor[])BarColor.class.getEnumConstants()).iterator();

               while(var8.hasNext()) {
                  BarColor color = (BarColor)var8.next();
                  list.add(color.toString());
               }

               list.add("RANDOM");
            }
         } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setteleport")) {
               list.addAll(TELEPORT_COMMANDS);
            } else if (args[0].equalsIgnoreCase("setdamage")) {
               list.addAll(DAMAGE_COMMANDS);
            } else if (args[0].equalsIgnoreCase("linkkit")) {
               list.addAll(TNTRun.getInstance().getKitManager().getKits());
            } else if (args[0].equalsIgnoreCase("unlinkkit")) {
               Arena arena = TNTRun.getInstance().amanager.getArenaByName(args[1]);
               if (arena != null) {
                  list.addAll(arena.getStructureManager().getLinkedKits());
               }
            }
         }

         var8 = list.iterator();

         while(var8.hasNext()) {
            String s = (String)var8.next();
            if (s.startsWith(args[args.length - 1])) {
               auto.add(s);
            }
         }

         return auto.isEmpty() ? list : auto;
      }
   }
}
