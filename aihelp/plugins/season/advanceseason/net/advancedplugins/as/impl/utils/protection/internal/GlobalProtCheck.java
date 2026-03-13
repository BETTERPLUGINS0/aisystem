/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.utils.protection.internal;

import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.protection.ProtectionType;
import net.advancedplugins.as.impl.utils.protection.events.FakeAdvancedBlockBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class GlobalProtCheck
implements ProtectionType,
Listener {
    public GlobalProtCheck() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)ASManager.getInstance());
    }

    @Override
    public String getName() {
        return "vanilla";
    }

    @Override
    public boolean canAttack(Player player, Player player2) {
        return true;
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        boolean bl;
        Block block = location.getBlock();
        block.setMetadata("blockbreakevent-ignore", (MetadataValue)new FixedMetadataValue((Plugin)ASManager.getInstance(), (Object)true));
        FakeAdvancedBlockBreakEvent fakeAdvancedBlockBreakEvent = new FakeAdvancedBlockBreakEvent(block, player);
        Bukkit.getPluginManager().callEvent((Event)fakeAdvancedBlockBreakEvent);
        boolean bl2 = bl = fakeAdvancedBlockBreakEvent.isCancelled() && !block.hasMetadata("ae-fake-cancel");
        if (block.hasMetadata("blockbreakevent-ignore")) {
            block.removeMetadata("blockbreakevent-ignore", (Plugin)ASManager.getInstance());
        }
        if (block.hasMetadata("ae-fake-cancel")) {
            block.removeMetadata("ae-fake-cancel", (Plugin)ASManager.getInstance());
        }
        return !bl;
    }

    @Override
    public boolean isProtected(Location location) {
        return false;
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    private void onFakeBlockBreak(FakeAdvancedBlockBreakEvent fakeAdvancedBlockBreakEvent) {
        fakeAdvancedBlockBreakEvent.setCancelled(true);
        fakeAdvancedBlockBreakEvent.getBlock().setMetadata("ae-fake-cancel", (MetadataValue)new FixedMetadataValue((Plugin)ASManager.getInstance(), (Object)true));
    }
}

