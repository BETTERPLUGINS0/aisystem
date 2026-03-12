package emanondev.itemtag.command.itemtag.customflags;

import emanondev.itemtag.ItemTag;
import emanondev.itemtag.command.itemtag.Flag;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.inventory.ItemStack;

public class FurnaceFuel extends CustomFlag {
   private static final String FURNACE_FUEL;

   public FurnaceFuel(Flag cmd) {
      super("furnacefuel", FURNACE_FUEL, cmd);
   }

   @EventHandler
   public void event(FurnaceBurnEvent event) {
      if (ItemTag.getTagItem(event.getFuel()).hasBooleanTag(FURNACE_FUEL)) {
         event.setCancelled(true);
      }

   }

   public ItemStack getGuiItem() {
      return new ItemStack(Material.COAL);
   }

   static {
      FURNACE_FUEL = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":furnacefuel";
   }
}
