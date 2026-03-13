/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.utils.protection.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class FakeAdvancedBlockBreakEvent
extends BlockBreakEvent {
    private final Player player;
    private boolean dropItems;
    private boolean cancel;

    public FakeAdvancedBlockBreakEvent(@NotNull Block block, @NotNull Player player) {
        super(block, player);
        this.player = player;
        this.dropItems = true;
    }

    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    public void setDropItems(boolean bl) {
        this.dropItems = bl;
    }

    public boolean isDropItems() {
        return this.dropItems;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean bl) {
        this.cancel = bl;
    }
}

