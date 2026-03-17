/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.maxgamer.quickshop.api.event.ProtectionCheckStatus
 *  org.maxgamer.quickshop.api.event.ShopProtectionCheckEvent
 */
package me.zombie_striker.qav.hooks;

import me.zombie_striker.qav.api.QualityArmoryVehicles;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.maxgamer.quickshop.api.event.ProtectionCheckStatus;
import org.maxgamer.quickshop.api.event.ShopProtectionCheckEvent;

public class QuickShopHook
implements Listener {
    private volatile Location protectionCheckingLocation = null;

    @EventHandler
    public void onQuickShopProtectionChecking(ShopProtectionCheckEvent shopProtectionCheckEvent) {
        if (shopProtectionCheckEvent.getStatus().equals((Object)ProtectionCheckStatus.BEGIN)) {
            this.protectionCheckingLocation = shopProtectionCheckEvent.getLocation();
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        if (this.protectionCheckingLocation != null && blockBreakEvent.getBlock().getLocation().equals((Object)this.protectionCheckingLocation)) {
            this.protectionCheckingLocation = null;
            if (QualityArmoryVehicles.isVehicleByItem(blockBreakEvent.getPlayer().getInventory().getItemInMainHand())) {
                blockBreakEvent.setCancelled(false);
            }
        }
    }
}

