package libs.com.ryderbelserion.vital.paper.api.builders.gui;

import java.util.function.Consumer;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.types.PaginatedGui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PaginatedBuilder extends BaseGuiBuilder<PaginatedGui, PaginatedBuilder> {
   private int pageSize = 0;

   @NotNull
   @Contract("_ -> this")
   public final PaginatedBuilder pageSize(int pageSize) {
      this.pageSize = pageSize;
      return this;
   }

   @NotNull
   public final PaginatedGui create() {
      PaginatedGui gui = new PaginatedGui(this.getTitle(), this.pageSize, this.getRows(), this.getInteractionComponents());
      Consumer<PaginatedGui> consumer = this.getConsumer();
      if (consumer != null) {
         consumer.accept(gui);
      }

      return gui;
   }
}
