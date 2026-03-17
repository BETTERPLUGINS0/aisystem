package es.outlook.adriansrj.spigui.toolbar;

import es.outlook.adriansrj.spigui.buttons.SGButton;
import es.outlook.adriansrj.spigui.item.ItemBuilder;
import es.outlook.adriansrj.spigui.menu.SGMenu;
import org.bukkit.Material;
import org.bukkit.event.Event.Result;

public class SGDefaultToolbarBuilder implements SGToolbarBuilder {
   public SGButton buildToolbarButton(int var1, int var2, SGToolbarButtonType var3, SGMenu var4) {
      switch(var3) {
      case PREV_BUTTON:
         if (var4.getCurrentPage() > 0) {
            return (new SGButton((new ItemBuilder(Material.ARROW)).name("&a&l← Previous Page").lore("&aClick to move back to", "&apage " + var4.getCurrentPage() + ".").build())).withListener((var1x) -> {
               var1x.setResult(Result.DENY);
               var4.previousPage(var1x.getWhoClicked());
            });
         }

         return null;
      case CURRENT_BUTTON:
         return (new SGButton((new ItemBuilder(Material.NAME_TAG)).name("&7&lPage " + (var4.getCurrentPage() + 1) + " of " + var4.getMaxPage()).lore("&7You are currently viewing", "&7page " + (var4.getCurrentPage() + 1) + ".").build())).withListener((var0) -> {
            var0.setResult(Result.DENY);
         });
      case NEXT_BUTTON:
         if (var4.getCurrentPage() < var4.getMaxPage() - 1) {
            return (new SGButton((new ItemBuilder(Material.ARROW)).name("&a&lNext Page →").lore("&aClick to move forward to", "&apage " + (var4.getCurrentPage() + 2) + ".").build())).withListener((var1x) -> {
               var1x.setResult(Result.DENY);
               var4.nextPage(var1x.getWhoClicked());
            });
         }

         return null;
      case UNASSIGNED:
      default:
         return null;
      }
   }
}
