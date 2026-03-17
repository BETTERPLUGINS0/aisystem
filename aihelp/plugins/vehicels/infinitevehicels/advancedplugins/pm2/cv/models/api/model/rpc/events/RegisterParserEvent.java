package advancedplugins.pm2.cv.models.api.model.rpc.events;

import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.ModelParser;
import java.util.List;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RegisterParserEvent extends AbstractEvent implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final List<ModelParser> parsers;
   private boolean cancelled;

   public RegisterParserEvent(List<ModelParser> var1) {
      this.parsers = var1;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return handlers;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public void register(ModelParser var1) {
      this.parsers.add(var1);
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean var1) {
      this.cancelled = var1;
   }
}
