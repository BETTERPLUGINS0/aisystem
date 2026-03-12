package emanondev.itemtag.command.itemtag.customflags;

import emanondev.itemtag.ItemTag;
import emanondev.itemtag.command.itemtag.Flag;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class Placeable extends CustomFlag {
   private static final String PLACEABLE_KEY;
   private long cooldownSeconds;

   public Placeable(Flag cmd) {
      super("placeable", PLACEABLE_KEY, cmd);
      this.reload();
   }

   @EventHandler(
      ignoreCancelled = true
   )
   private void event(BlockPlaceEvent event) {
      if (ItemTag.getTagItem(event.getItemInHand()).hasBooleanTag(PLACEABLE_KEY)) {
         event.setCancelled(true);
         if (this.cooldownSeconds == 0L || !this.getPlugin().getCooldownAPI().hasCooldown(event.getPlayer(), PLACEABLE_KEY)) {
            this.sendLanguageString("feedback.failed-place-attempt", (String)null, event.getPlayer(), new String[0]);
            if (this.cooldownSeconds != 0L) {
               this.getPlugin().getCooldownAPI().setCooldown(event.getPlayer(), PLACEABLE_KEY, this.cooldownSeconds, TimeUnit.SECONDS);
            }
         }
      }

   }

   public void reload() {
      this.cooldownSeconds = Math.max(0L, this.getConfigLong("failed-place-attempt-message-cooldown-seconds"));
   }

   public ItemStack getGuiItem() {
      return new ItemStack(Material.BRICKS);
   }

   static {
      PLACEABLE_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":placeable";
   }
}
