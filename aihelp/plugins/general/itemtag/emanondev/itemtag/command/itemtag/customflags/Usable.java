package emanondev.itemtag.command.itemtag.customflags;

import emanondev.itemedit.utility.InventoryUtils;
import emanondev.itemedit.utility.VersionUtils;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.command.itemtag.Flag;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Event.Result;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Usable extends CustomFlag {
   private static final String USABLE_KEY;

   public Usable(Flag cmd) {
      super("usable", USABLE_KEY, cmd);
   }

   @EventHandler
   private void event(PlayerInteractEvent event) {
      switch(event.getAction()) {
      case RIGHT_CLICK_AIR:
      case RIGHT_CLICK_BLOCK:
         if (ItemTag.getTagItem(event.getItem()).hasBooleanTag(USABLE_KEY)) {
            event.setUseItemInHand(Result.DENY);
            Block b;
            if (event.getItem().getType() == Material.BUCKET && VersionUtils.isVersionAfter(1, 15)) {
               b = event.getPlayer().getTargetBlockExact(7, FluidCollisionMode.SOURCE_ONLY);
            } else {
               b = null;
            }

            Bukkit.getScheduler().runTaskLater(ItemTag.get(), () -> {
               if (!VersionUtils.isVersionAfter(1, 20)) {
                  InventoryUtils.updateView(event.getPlayer());
               }

               if (b != null) {
                  event.getPlayer().sendBlockChange(b.getLocation(), b.getBlockData());
               }

            }, 1L);
         }
      default:
      }
   }

   @EventHandler
   private void event(PlayerBucketFillEvent event) {
      if (!VersionUtils.isVersionAfter(1, 18)) {
         ItemStack item = this.getItemInHand(event.getPlayer());
         if (VersionUtils.isVersionUpTo(1, 8, 9) && (item == null || item.getType() != Material.BUCKET)) {
            item = event.getPlayer().getInventory().getItemInOffHand();
         }

         if (ItemTag.getTagItem(item).hasBooleanTag(USABLE_KEY)) {
            event.setCancelled(true);
         }

      }
   }

   @EventHandler
   private void event(PlayerBucketEmptyEvent event) {
      if (!VersionUtils.isVersionAfter(1, 18)) {
         ItemStack item = this.getItemInHand(event.getPlayer());
         if (VersionUtils.isVersionAfter(1, 9) && (item == null || item.getType() != Material.LAVA_BUCKET && item.getType() != Material.WATER_BUCKET)) {
            item = event.getPlayer().getInventory().getItemInOffHand();
         }

         if (ItemTag.getTagItem(item).hasBooleanTag(USABLE_KEY)) {
            event.setCancelled(true);
         }

      }
   }

   public ItemStack getGuiItem() {
      return new ItemStack(Material.STONE_BUTTON);
   }

   static {
      USABLE_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":usable";
   }
}
