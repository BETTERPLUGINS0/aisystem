package me.SuperRonanCraft.BetterRTP.player.commands.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommandHelpable;
import me.SuperRonanCraft.BetterRTP.references.PermissionCheck;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP_Info;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesHelp;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesUsage;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CmdWorld implements RTPCommand, RTPCommandHelpable {
   public String getName() {
      return "world";
   }

   public void execute(CommandSender sendi, String label, String[] args) {
      if (args.length >= 2) {
         World world = Bukkit.getWorld(args[1]);
         if (world == null) {
            world = Bukkit.getWorld(args[1].replace("_", " "));
         }

         if (world != null) {
            CmdTeleport.teleport(sendi, label, world, HelperRTP_Info.getBiomes(args, 2, sendi));
         } else {
            MessagesCore.NOTEXIST.send(sendi, (Object)args[1]);
         }
      } else {
         this.usage(sendi, label);
      }

   }

   public List<String> tabComplete(CommandSender sendi, String[] args) {
      List<String> list = new ArrayList();
      if (args.length == 2) {
         Iterator var4 = Bukkit.getWorlds().iterator();

         while(var4.hasNext()) {
            World w = (World)var4.next();
            String _wName = w.getName().replace(" ", "_");
            if (w.getName().startsWith(args[1]) && !BetterRTP.getInstance().getRTP().getDisabledWorlds().contains(_wName) && PermissionCheck.getAWorld(sendi, _wName)) {
               list.add(_wName);
            }
         }
      } else if (args.length >= 3 && PermissionNode.BIOME.check(sendi)) {
         HelperRTP_Info.addBiomes(list, args);
      }

      return list;
   }

   @NotNull
   public PermissionNode permission() {
      return PermissionNode.WORLD;
   }

   public void usage(CommandSender sendi, String label) {
      MessagesUsage.WORLD.send(sendi, label);
   }

   public String getHelp() {
      return MessagesHelp.WORLD.get();
   }
}
