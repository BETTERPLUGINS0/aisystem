package com.nisovin.shopkeepers.compat.v1_21_R9;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.container.protection.ProtectedContainers;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetBlockEvent;

public class CopperChestProtectionListener implements Listener {
   private final ProtectedContainers protectedContainers;

   public CopperChestProtectionListener(SKShopkeepersPlugin plugin) {
      Validate.notNull(plugin, (String)"plugin");
      this.protectedContainers = plugin.getProtectedContainers();
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityTargetBlockEvent(EntityTargetBlockEvent e) {
      Block targetBlock = e.getTarget();
      if (targetBlock != null) {
         if (this.protectedContainers.isProtectedContainer(targetBlock)) {
            e.setTarget((Block)null);
         }
      }
   }
}
