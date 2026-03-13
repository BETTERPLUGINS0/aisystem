package libs.com.ryderbelserion.vital.paper.modules;

import libs.com.ryderbelserion.vital.paper.modules.interfaces.IPaperModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EventRegistry {
   private final JavaPlugin plugin;

   public EventRegistry(@NotNull JavaPlugin plugin) {
      this.plugin = plugin;
   }

   public void addEvent(@NotNull IPaperModule event) {
      this.plugin.getServer().getPluginManager().registerEvents(event, this.plugin);
   }
}
