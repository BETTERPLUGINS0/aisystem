package es.outlook.adriansrj.spigui.menu;

import es.outlook.adriansrj.spigui.SpiGUI;
import es.outlook.adriansrj.spigui.buttons.SGButton;
import es.outlook.adriansrj.spigui.toolbar.SGToolbarBuilder;
import es.outlook.adriansrj.spigui.toolbar.SGToolbarButtonType;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

public class SGMenuListener implements Listener {
   private static final ClickType[] PERMITTED_MENU_CLICK_TYPES;
   private static final InventoryAction[] BLOCKED_MENU_ACTIONS;
   private static final InventoryAction[] BLOCKED_ADJACENT_ACTIONS;
   private final JavaPlugin owner;
   private final SpiGUI spiGUI;

   public SGMenuListener(JavaPlugin var1, SpiGUI var2) {
      this.owner = var1;
      this.spiGUI = var2;
   }

   private static boolean shouldIgnoreInventoryEvent(Inventory var0) {
      return var0 == null || var0.getHolder() == null || !(var0.getHolder() instanceof SGMenu);
   }

   public static boolean willHandleInventoryEvent(JavaPlugin var0, Inventory var1) {
      return !shouldIgnoreInventoryEvent(var1) && Objects.equals(((SGMenu)var1.getHolder()).getOwner(), var0);
   }

   @EventHandler
   public void onInventoryClick(InventoryClickEvent var1) {
      if (!shouldIgnoreInventoryEvent(var1.getClickedInventory())) {
         if (Arrays.stream(PERMITTED_MENU_CLICK_TYPES).noneMatch((var1x) -> {
            return var1x == var1.getClick();
         })) {
            var1.setResult(Result.DENY);
         } else if (Arrays.stream(BLOCKED_MENU_ACTIONS).anyMatch((var1x) -> {
            return var1x == var1.getAction();
         })) {
            var1.setResult(Result.DENY);
         } else {
            SGMenu var2 = (SGMenu)var1.getClickedInventory().getHolder();
            if (var2.getOwner().equals(this.owner)) {
               boolean var3 = var2.areDefaultInteractionsBlocked() != null && var2.areDefaultInteractionsBlocked() || this.spiGUI.areDefaultInteractionsBlocked();
               if (var3) {
                  var1.setResult(Result.DENY);
               }

               if (var1.getSlot() > var2.getPageSize()) {
                  int var8 = var1.getSlot() - var2.getPageSize();
                  SGToolbarBuilder var5 = this.spiGUI.getDefaultToolbarBuilder();
                  if (var2.getToolbarBuilder() != null) {
                     var5 = var2.getToolbarBuilder();
                  }

                  SGToolbarButtonType var6 = SGToolbarButtonType.getDefaultForSlot(var8);
                  SGButton var7 = var5.buildToolbarButton(var8, var2.getCurrentPage(), var6, var2);
                  if (var7 != null) {
                     var7.getListener().onClick(var1);
                  }

               } else {
                  SGButton var4;
                  if (var2.isStickiedSlot(var1.getSlot())) {
                     var4 = var2.getButton(0, var1.getSlot());
                     if (var4 != null && var4.getListener() != null) {
                        var4.getListener().onClick(var1);
                     }

                  } else {
                     var4 = var2.getButton(var2.getCurrentPage(), var1.getSlot());
                     if (var4 != null && var4.getListener() != null) {
                        var4.getListener().onClick(var1);
                     }

                  }
               }
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onAdjacentInventoryClick(InventoryClickEvent var1) {
      if (var1.getView().getTopInventory() != null && !shouldIgnoreInventoryEvent(var1.getView().getTopInventory())) {
         if (var1.getClickedInventory() != var1.getView().getTopInventory()) {
            if (Arrays.stream(BLOCKED_ADJACENT_ACTIONS).anyMatch((var1x) -> {
               return var1x == var1.getAction();
            })) {
               var1.setResult(Result.DENY);
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onInventoryDrag(InventoryDragEvent var1) {
      if (!shouldIgnoreInventoryEvent(var1.getInventory())) {
         SGMenu var2 = (SGMenu)var1.getInventory().getHolder();
         if (this.slotsIncludeTopInventory(var1.getView(), var1.getRawSlots())) {
            var1.setResult(Result.DENY);
         }

      }
   }

   @EventHandler
   public void onInventoryClose(InventoryCloseEvent var1) {
      if (!shouldIgnoreInventoryEvent(var1.getInventory())) {
         SGMenu var2 = (SGMenu)var1.getInventory().getHolder();
         if (Objects.equals(var2.getOwner(), this.owner)) {
            if (var2.getOnClose() != null) {
               var2.getOnClose().accept(var2);
            }

         }
      }
   }

   private boolean slotsIncludeTopInventory(InventoryView var1, Set<Integer> var2) {
      return var2.stream().anyMatch((var1x) -> {
         if (var1x >= var1.getTopInventory().getSize()) {
            return false;
         } else {
            return var1x == var1.convertSlot(var1x);
         }
      });
   }

   static {
      PERMITTED_MENU_CLICK_TYPES = new ClickType[]{ClickType.LEFT, ClickType.RIGHT};
      BLOCKED_MENU_ACTIONS = new InventoryAction[]{InventoryAction.MOVE_TO_OTHER_INVENTORY, InventoryAction.COLLECT_TO_CURSOR};
      BLOCKED_ADJACENT_ACTIONS = new InventoryAction[]{InventoryAction.MOVE_TO_OTHER_INVENTORY, InventoryAction.COLLECT_TO_CURSOR};
   }
}
