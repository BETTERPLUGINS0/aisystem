package me.SuperRonanCraft.BetterRTP.references.customEvents;

import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class RTP_CommandEvent extends RTPEvent implements Cancellable {
   boolean cancelled;
   CommandSender sendi;
   RTPCommand cmd;
   private static final HandlerList handler = new HandlerList();

   public RTP_CommandEvent(CommandSender sendi, RTPCommand cmd) {
      this.sendi = sendi;
      this.cmd = cmd;
   }

   public CommandSender getSendi() {
      return this.sendi;
   }

   public RTPCommand getCmd() {
      return this.cmd;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean b) {
      this.cancelled = b;
   }
}
