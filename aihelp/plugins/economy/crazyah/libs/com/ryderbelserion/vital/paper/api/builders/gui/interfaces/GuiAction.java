package libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces;

import org.bukkit.event.Event;

@FunctionalInterface
public interface GuiAction<T extends Event> {
   void execute(T var1);
}
