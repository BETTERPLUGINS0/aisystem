package emanondev.itemedit.gui.button;

import emanondev.itemedit.gui.Gui;
import java.util.function.BiPredicate;
import java.util.function.Function;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FunctionalButton extends SimpleButton {
   private final BiPredicate<InventoryClickEvent, FunctionalButton> onClick;
   private final Function<FunctionalButton, ItemStack> getItem;

   public FunctionalButton(@NotNull Gui gui, @NotNull BiPredicate<InventoryClickEvent, FunctionalButton> onClick, @NotNull Function<FunctionalButton, ItemStack> getItem) {
      super(gui);
      this.onClick = onClick;
      this.getItem = getItem;
   }

   public boolean onClick(@NotNull InventoryClickEvent event) {
      return this.onClick.test(event, this);
   }

   public ItemStack getItem() {
      return (ItemStack)this.getItem.apply(this);
   }
}
