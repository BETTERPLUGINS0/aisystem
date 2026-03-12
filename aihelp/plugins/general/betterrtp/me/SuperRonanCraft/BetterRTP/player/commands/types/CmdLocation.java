package me.SuperRonanCraft.BetterRTP.player.commands.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommandHelpable;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_TYPE;
import me.SuperRonanCraft.BetterRTP.references.PermissionCheck;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesHelp;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesUsage;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.RTPWorld;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldLocation;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CmdLocation implements RTPCommand, RTPCommandHelpable {
   public String getName() {
      return "location";
   }

   public void execute(CommandSender sendi, String label, String[] args) {
      Player p;
      Iterator var5;
      Entry location;
      if (args.length == 2) {
         if (sendi instanceof Player) {
            p = (Player)sendi;
            var5 = getLocations(sendi, p.getWorld()).entrySet().iterator();

            while(var5.hasNext()) {
               location = (Entry)var5.next();
               if (((String)location.getKey()).equalsIgnoreCase(args[1].toLowerCase())) {
                  HelperRTP.tp(p, sendi, (World)null, (List)null, RTP_TYPE.COMMAND, false, false, (WorldLocation)location.getValue());
                  return;
               }
            }

            this.usage(sendi, label);
         } else {
            sendi.sendMessage("Console is not able to execute this command! Try '/rtp help'");
         }
      } else if (args.length == 3 && PermissionNode.RTP_OTHER.check(sendi)) {
         p = Bukkit.getPlayer(args[2]);
         if (p != null && p.isOnline()) {
            var5 = getLocations(sendi, (World)null).entrySet().iterator();

            while(var5.hasNext()) {
               location = (Entry)var5.next();
               if (((String)location.getKey()).equalsIgnoreCase(args[1].toLowerCase())) {
                  HelperRTP.tp(p, sendi, (World)null, (List)null, RTP_TYPE.COMMAND, false, false, (WorldLocation)location.getValue());
                  return;
               }
            }

            this.usage(sendi, label);
         } else if (p != null) {
            MessagesCore.NOTONLINE.send(sendi, (Object)args[1]);
         } else {
            this.usage(sendi, label);
         }
      } else {
         this.usage(sendi, label);
      }

   }

   public List<String> tabComplete(CommandSender sendi, String[] args) {
      List<String> list = new ArrayList();
      if (args.length == 2) {
         Player p = (Player)sendi;
         Iterator var5 = getLocations(sendi, p.getWorld()).keySet().iterator();

         while(var5.hasNext()) {
            String location_name = (String)var5.next();
            if (location_name.toLowerCase().startsWith(args[1].toLowerCase())) {
               list.add(location_name);
            }
         }
      } else if (args.length == 3 && PermissionNode.RTP_OTHER.check(sendi)) {
         Iterator var7 = Bukkit.getOnlinePlayers().iterator();

         while(var7.hasNext()) {
            Player p = (Player)var7.next();
            if (p.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
               list.add(p.getName());
            }
         }
      }

      return list;
   }

   @NotNull
   public PermissionNode permission() {
      return PermissionNode.LOCATION;
   }

   public void usage(CommandSender sendi, String label) {
      MessagesUsage.LOCATION.send(sendi, label);
   }

   public static HashMap<String, RTPWorld> getLocations(CommandSender sendi, @Nullable World world) {
      HashMap<String, RTPWorld> locations = new HashMap();
      boolean needPermission = BetterRTP.getInstance().getSettings().isLocationNeedPermission();
      boolean needSameWorld = BetterRTP.getInstance().getSettings().isUseLocationsInSameWorld();
      if (needSameWorld) {
         needSameWorld = !PermissionNode.BYPASS_LOCATION.check(sendi);
      }

      Iterator var5 = BetterRTP.getInstance().getRTP().getRTPworldLocations().entrySet().iterator();

      while(var5.hasNext()) {
         Entry<String, RTPWorld> location = (Entry)var5.next();
         boolean add = true;
         if (needPermission) {
            add = PermissionCheck.getLocation(sendi, (String)location.getKey());
         }

         if (add && needSameWorld) {
            add = world == null || ((RTPWorld)location.getValue()).getWorld().equals(world);
         }

         if (add) {
            locations.put((String)location.getKey(), (RTPWorld)location.getValue());
         }
      }

      return locations;
   }

   public String getHelp() {
      return MessagesHelp.LOCATION.get();
   }

   public boolean enabled() {
      return BetterRTP.getInstance().getSettings().isLocationEnabled();
   }
}
