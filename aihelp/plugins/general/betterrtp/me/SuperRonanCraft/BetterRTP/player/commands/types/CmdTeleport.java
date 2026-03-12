package me.SuperRonanCraft.BetterRTP.player.commands.types;

import java.util.List;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_TYPE;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdTeleport implements RTPCommand {
   public static void teleport(CommandSender sendi, String label, World world, List<String> biomes) {
      if (sendi instanceof Player) {
         HelperRTP.tp((Player)sendi, sendi, world, biomes, RTP_TYPE.COMMAND);
      } else {
         Message_RTP.sms(sendi, "Must be a player to use this command! Try '/" + label + " help'");
      }

   }

   public void execute(CommandSender sendi, String label, String[] args) {
      teleport(sendi, label, (World)null, (List)null);
   }

   public List<String> tabComplete(CommandSender sendi, String[] args) {
      return null;
   }

   @NotNull
   public PermissionNode permission() {
      return PermissionNode.USE;
   }

   public String getName() {
      return null;
   }
}
