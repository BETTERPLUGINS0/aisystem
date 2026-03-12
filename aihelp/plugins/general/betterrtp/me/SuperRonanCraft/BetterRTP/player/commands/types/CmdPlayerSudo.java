package me.SuperRonanCraft.BetterRTP.player.commands.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_PlayerInfo;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_TYPE;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesUsage;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldLocation;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdPlayerSudo implements RTPCommand {
   public String getName() {
      return "player_sudo";
   }

   public void execute(CommandSender sendi, String label, String[] args) {
      if (args.length == 2) {
         if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline()) {
            HelperRTP.tp(Bukkit.getPlayer(args[1]), sendi, Bukkit.getPlayer(args[1]).getWorld(), (List)null, RTP_TYPE.FORCED, (WorldLocation)null, new RTP_PlayerInfo(false, true, false, false, false));
         } else if (Bukkit.getPlayer(args[1]) != null) {
            MessagesCore.NOTONLINE.send(sendi, (Object)args[1]);
         } else {
            this.usage(sendi, label);
         }
      } else if (args.length >= 3) {
         if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline()) {
            World world = Bukkit.getWorld(args[2]);
            if (world != null) {
               HelperRTP.tp(Bukkit.getPlayer(args[1]), sendi, world, (List)null, RTP_TYPE.FORCED, (WorldLocation)null, new RTP_PlayerInfo(false, true, false, false, false));
            } else {
               MessagesCore.NOTEXIST.send(sendi, (Object)args[2]);
            }
         } else if (Bukkit.getPlayer(args[1]) != null) {
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
      Iterator var4;
      if (args.length == 2) {
         var4 = Bukkit.getOnlinePlayers().iterator();

         while(var4.hasNext()) {
            Player p = (Player)var4.next();
            if (p.getDisplayName().toLowerCase().startsWith(args[1].toLowerCase())) {
               list.add(p.getName());
            }
         }
      } else if (args.length == 3) {
         var4 = Bukkit.getWorlds().iterator();

         while(var4.hasNext()) {
            World w = (World)var4.next();
            if (w.getName().startsWith(args[2]) && !BetterRTP.getInstance().getRTP().getDisabledWorlds().contains(w.getName())) {
               list.add(w.getName());
            }
         }
      }

      return list;
   }

   @NotNull
   public PermissionNode permission() {
      return PermissionNode.RTP_OTHER;
   }

   public void usage(CommandSender sendi, String label) {
      MessagesUsage.RTP_OTHER.send(sendi, label);
   }
}
