package me.SuperRonanCraft.BetterRTP.player.commands.types;

import java.util.List;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommandHelpable;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesHelp;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CmdReload implements RTPCommand, RTPCommandHelpable {
   public String getName() {
      return "reload";
   }

   public void execute(CommandSender sendi, String label, String[] args) {
      BetterRTP.getInstance().reload(sendi);
   }

   public List<String> tabComplete(CommandSender sendi, String[] args) {
      return null;
   }

   @NotNull
   public PermissionNode permission() {
      return PermissionNode.RELOAD;
   }

   public String getHelp() {
      return MessagesHelp.RELOAD.get();
   }
}
