package emanondev.itemedit.gui.button;

import emanondev.itemedit.gui.Gui;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleButton implements Button {
   private final Gui gui;

   public SimpleButton(@NotNull Gui gui) {
      this.gui = gui;
   }

   @NotNull
   public Gui getGui() {
      return this.gui;
   }
}
