package me.SuperRonanCraft.BetterRTP.player.commands.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommandHelpable;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesHelp;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CmdHelp implements RTPCommand, RTPCommandHelpable {
   public String getName() {
      return "help";
   }

   public void execute(CommandSender sendi, String label, String[] args) {
      List<String> list = new ArrayList();
      list.add(MessagesHelp.PREFIX.get());
      list.add(MessagesHelp.MAIN.get());
      Iterator var5 = BetterRTP.getInstance().getCmd().commands.iterator();

      while(var5.hasNext()) {
         RTPCommand cmd = (RTPCommand)var5.next();
         if (cmd.permission().check(sendi) && cmd instanceof RTPCommandHelpable) {
            String help = ((RTPCommandHelpable)cmd).getHelp();
            list.add(help);
         }
      }

      Message_RTP.sms(sendi, (List)list, (List)Collections.singletonList(label));
   }

   public List<String> tabComplete(CommandSender sendi, String[] args) {
      return null;
   }

   @NotNull
   public PermissionNode permission() {
      return PermissionNode.USE;
   }

   public String getHelp() {
      return MessagesHelp.HELP.get();
   }
}
