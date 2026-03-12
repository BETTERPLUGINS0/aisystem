package libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces;

import java.util.Set;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.PaginatedBuilder;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.SimpleBuilder;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.objects.components.InteractionComponent;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.types.BaseGui;
import org.jetbrains.annotations.NotNull;

public class Gui extends BaseGui {
   public Gui(String title, int rows, Set<InteractionComponent> components) {
      super(title, rows, components);
   }

   public Gui(String title, GuiType guiType, Set<InteractionComponent> components) {
      super(title, guiType, components);
   }

   public static SimpleBuilder gui(@NotNull GuiType type) {
      return new SimpleBuilder(type);
   }

   public static SimpleBuilder gui() {
      return gui(GuiType.CHEST);
   }

   public static PaginatedBuilder paginated() {
      return new PaginatedBuilder();
   }
}
