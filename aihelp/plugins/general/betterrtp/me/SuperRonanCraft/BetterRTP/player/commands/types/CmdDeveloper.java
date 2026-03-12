package me.SuperRonanCraft.BetterRTP.player.commands.types;

import java.util.List;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.RandomLocation;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CmdDeveloper implements RTPCommand {
   public String getName() {
      return "dev";
   }

   public void execute(CommandSender sendi, String label, String[] args) {
      RandomLocation.runChunkTest();
   }

   public List<String> tabComplete(CommandSender sendi, String[] args) {
      return null;
   }

   @NotNull
   public PermissionNode permission() {
      return PermissionNode.DEVELOPER;
   }

   public void usage(CommandSender sendi, String label) {
      sendi.sendMessage("This is for Developement use only!");
   }
}
