package emanondev.itemedit.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface PagedGui extends Gui {
   int getPage();

   @NotNull
   default ItemStack getPreviousPageItem() {
      return this.loadLanguageDescription(this.getGuiItem("buttons.previous-page", Material.BARRIER), "gui.previous-page.description", new String[]{"%page%", String.valueOf(this.getPage()), "%target_page%", String.valueOf(this.getPage() - 1)});
   }

   @NotNull
   default ItemStack getNextPageItem() {
      return this.loadLanguageDescription(this.getGuiItem("buttons.next-page", Material.BARRIER), "gui.next-page.description", new String[]{"%page%", String.valueOf(this.getPage()), "%target_page%", String.valueOf(this.getPage() + 1)});
   }
}
