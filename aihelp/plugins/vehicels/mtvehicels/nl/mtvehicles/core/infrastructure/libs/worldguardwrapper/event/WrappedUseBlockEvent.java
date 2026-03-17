/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.NonNull
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.event;

import java.util.List;
import lombok.NonNull;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.event.AbstractWrappedEvent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WrappedUseBlockEvent
extends AbstractWrappedEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Event originalEvent;
    private final Player player;
    private final World world;
    private final List<Block> blocks;
    private final Material effectiveMaterial;

    @NonNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public WrappedUseBlockEvent(Event originalEvent, Player player, World world, List<Block> blocks, Material effectiveMaterial) {
        this.originalEvent = originalEvent;
        this.player = player;
        this.world = world;
        this.blocks = blocks;
        this.effectiveMaterial = effectiveMaterial;
    }

    public Event getOriginalEvent() {
        return this.originalEvent;
    }

    public Player getPlayer() {
        return this.player;
    }

    public World getWorld() {
        return this.world;
    }

    public List<Block> getBlocks() {
        return this.blocks;
    }

    public Material getEffectiveMaterial() {
        return this.effectiveMaterial;
    }
}

