/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Item
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GameEndListener
implements Listener {
    @EventHandler
    public void cleanInventoriesAndDroppedItems(@NotNull GameEndEvent event) {
        if (event.getArena().getPlayers().isEmpty()) {
            return;
        }
        for (UUID p : event.getAliveWinners()) {
            Bukkit.getPlayer((UUID)p).getInventory().clear();
        }
        World game = event.getArena().getWorld();
        for (Entity item : game.getEntities()) {
            if (!(item instanceof Item) && !(item instanceof ItemStack)) continue;
            item.remove();
        }
    }
}

