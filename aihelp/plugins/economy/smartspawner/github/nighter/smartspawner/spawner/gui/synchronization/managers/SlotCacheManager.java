package github.nighter.smartspawner.spawner.gui.synchronization.managers;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.gui.layout.GuiButton;
import github.nighter.smartspawner.spawner.gui.layout.GuiLayout;
import java.util.Iterator;
import java.util.Map;

public class SlotCacheManager {
   private final SmartSpawner plugin;
   private volatile int cachedStorageSlot = -1;
   private volatile int cachedExpSlot = -1;
   private volatile int cachedSpawnerInfoSlot = -1;

   public SlotCacheManager(SmartSpawner plugin) {
      this.plugin = plugin;
      this.initializeSlotPositions();
   }

   public void initializeSlotPositions() {
      GuiLayout layout = this.plugin.getGuiLayoutConfig().getCurrentMainLayout();
      if (layout == null) {
         this.cachedStorageSlot = -1;
         this.cachedExpSlot = -1;
         this.cachedSpawnerInfoSlot = -1;
      } else {
         int storageSlot = -1;
         int expSlot = -1;
         int spawnerInfoSlot = -1;
         Iterator var5 = layout.getAllButtons().values().iterator();

         while(var5.hasNext()) {
            GuiButton button = (GuiButton)var5.next();
            if (button.isEnabled()) {
               if (button.isInfoButton()) {
                  spawnerInfoSlot = button.getSlot();
               } else {
                  String action = this.getAnyActionFromButton(button);
                  if (action != null) {
                     byte var9 = -1;
                     switch(action.hashCode()) {
                     case -1397348986:
                        if (action.equals("open_storage")) {
                           var9 = 0;
                        }
                        break;
                     case 1853584776:
                        if (action.equals("collect_exp")) {
                           var9 = 1;
                        }
                     }

                     switch(var9) {
                     case 0:
                        storageSlot = button.getSlot();
                        break;
                     case 1:
                        expSlot = button.getSlot();
                     }
                  }
               }
            }
         }

         this.cachedStorageSlot = storageSlot;
         this.cachedExpSlot = expSlot;
         this.cachedSpawnerInfoSlot = spawnerInfoSlot;
      }
   }

   private String getAnyActionFromButton(GuiButton button) {
      Map<String, String> actions = button.getActions();
      if (actions != null && !actions.isEmpty()) {
         String action = (String)actions.get("click");
         if (action != null && !action.isEmpty()) {
            return action;
         } else {
            action = (String)actions.get("left_click");
            if (action != null && !action.isEmpty()) {
               return action;
            } else {
               action = (String)actions.get("right_click");
               return action != null && !action.isEmpty() ? action : null;
            }
         }
      } else {
         return null;
      }
   }

   public int getStorageSlot() {
      return this.cachedStorageSlot;
   }

   public int getExpSlot() {
      return this.cachedExpSlot;
   }

   public int getSpawnerInfoSlot() {
      return this.cachedSpawnerInfoSlot;
   }

   public void clearAndReinitialize() {
      this.initializeSlotPositions();
   }
}
