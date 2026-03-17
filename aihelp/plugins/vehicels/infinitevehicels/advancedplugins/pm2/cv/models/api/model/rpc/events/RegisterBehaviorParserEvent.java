package advancedplugins.pm2.cv.models.api.model.rpc.events;

import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchBehaviorParser;
import java.util.Set;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RegisterBehaviorParserEvent extends AbstractEvent implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final Set<BlockbenchBehaviorParser> behaviorParsers;
   private boolean cancelled;

   public RegisterBehaviorParserEvent(Set<BlockbenchBehaviorParser> var1) {
      this.behaviorParsers = var1;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return handlers;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public void register(BlockbenchBehaviorParser var1) {
      this.behaviorParsers.add(var1);
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean var1) {
      this.cancelled = var1;
   }
}
