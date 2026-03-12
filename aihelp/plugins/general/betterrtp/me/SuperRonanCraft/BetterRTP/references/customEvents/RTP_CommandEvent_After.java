package me.SuperRonanCraft.BetterRTP.references.customEvents;

import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import org.bukkit.command.CommandSender;

public class RTP_CommandEvent_After extends RTP_CommandEvent {
   public RTP_CommandEvent_After(CommandSender sendi, RTPCommand cmd) {
      super(sendi, cmd);
   }
}
