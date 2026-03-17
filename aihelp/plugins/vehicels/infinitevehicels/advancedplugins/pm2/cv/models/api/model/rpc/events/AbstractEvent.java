package advancedplugins.pm2.cv.models.api.model.rpc.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public abstract class AbstractEvent extends Event {
   public AbstractEvent() {
      super(!Bukkit.isPrimaryThread());
   }
}
