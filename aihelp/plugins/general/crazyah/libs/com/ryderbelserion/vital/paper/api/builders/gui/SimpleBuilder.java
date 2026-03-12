package libs.com.ryderbelserion.vital.paper.api.builders.gui;

import java.util.function.Consumer;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.Gui;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiType;
import org.jetbrains.annotations.NotNull;

public final class SimpleBuilder extends BaseGuiBuilder<Gui, SimpleBuilder> {
   private GuiType guiType;

   public SimpleBuilder(@NotNull GuiType guiType) {
      this.guiType = guiType;
   }

   @NotNull
   public Gui create() {
      Gui gui = this.guiType != null && this.guiType != GuiType.CHEST ? new Gui(this.getTitle(), this.guiType, this.getInteractionComponents()) : new Gui(this.getTitle(), this.getRows(), this.getInteractionComponents());
      Consumer<Gui> consumer = this.getConsumer();
      if (consumer != null) {
         consumer.accept(gui);
      }

      return gui;
   }

   @NotNull
   public SimpleBuilder setType(GuiType guiType) {
      this.guiType = guiType;
      return this;
   }
}
