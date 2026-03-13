package com.nisovin.shopkeepers.compat.v1_21_R8_paper;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.container.protection.ProtectedContainers;
import com.nisovin.shopkeepers.util.java.Validate;
import io.papermc.paper.event.entity.ItemTransportingEntityValidateTargetEvent;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

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
   void onItemTransportingEntityValidateTargetEvent(ItemTransportingEntityValidateTargetEvent e) {
      if (e.isAllowed()) {
         if (this.protectedContainers.isProtectedContainer((Block)Unsafe.assertNonNull(e.getBlock()))) {
            e.setAllowed(false);
         }
      }
   }
}
