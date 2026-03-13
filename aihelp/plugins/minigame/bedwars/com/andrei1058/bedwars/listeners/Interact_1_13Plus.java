/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.support.version.common.VersionCommon;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interact_1_13Plus
implements Listener {
    @EventHandler
    public void onInventoryInteract(PlayerInteractEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block b2 = e.getClickedBlock();
        if (b2 == null) {
            return;
        }
        if (b2.getWorld().getName().equals(VersionCommon.api.getLobbyWorld()) || VersionCommon.api.getArenaUtil().getArenaByPlayer(e.getPlayer()) != null) {
            switch (b2.getType().toString()) {
                case "CHIPPED_ANVIL": 
                case "DAMAGED_ANVIL": {
                    if (VersionCommon.api.getConfigs().getMainConfig().getBoolean("inventories.disable-anvil")) {
                        e.setCancelled(true);
                        break;
                    }
                    if (!VersionCommon.api.getArenaUtil().isSpectating(e.getPlayer())) break;
                    e.setCancelled(true);
                }
            }
        }
    }
}

