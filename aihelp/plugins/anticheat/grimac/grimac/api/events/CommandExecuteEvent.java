package ac.grim.grimac.api.events;

import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import lombok.Generated;
import org.bukkit.event.HandlerList;

/** @deprecated */
@Deprecated(
   since = "1.2.1.0",
   forRemoval = true
)
public class CommandExecuteEvent extends FlagEvent {
   private static final HandlerList handlers = new HandlerList();
   private final String command;

   public CommandExecuteEvent(GrimUser player, AbstractCheck check, String verbose, String command) {
      super(player, check, verbose);
      this.command = command;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   @Generated
   public String getCommand() {
      return this.command;
   }
}
