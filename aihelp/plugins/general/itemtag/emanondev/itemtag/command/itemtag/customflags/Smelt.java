package emanondev.itemtag.command.itemtag.customflags;

import emanondev.itemtag.ItemTag;
import emanondev.itemtag.command.itemtag.Flag;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

public class Smelt extends CustomFlag {
   private static final String SMELT;

   public Smelt(Flag cmd) {
      super("smelt", SMELT, cmd);
   }

   @EventHandler
   public void event(FurnaceSmeltEvent event) {
      if (ItemTag.getTagItem(event.getSource()).hasBooleanTag(SMELT)) {
         event.setCancelled(true);
      }

   }

   public ItemStack getGuiItem() {
      return new ItemStack(Material.FURNACE);
   }

   static {
      SMELT = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":smelt";
   }
}
