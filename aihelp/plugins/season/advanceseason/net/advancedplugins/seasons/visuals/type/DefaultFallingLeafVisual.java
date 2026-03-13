/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.seasons.visuals.type;

import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.visuals.type.AbstractFallingLeafVisual;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class DefaultFallingLeafVisual
extends AbstractFallingLeafVisual {
    private final SeasonType seasonType;

    public DefaultFallingLeafVisual(SeasonType seasonType) {
        this.seasonType = seasonType;
    }

    @Override
    public SeasonType getType() {
        return this.seasonType;
    }

    @Override
    protected void playEffect(Player player, Location location) {
        Block block = location.getBlock();
        player.spawnParticle(Particle.BLOCK_DUST, location, 5, (Object)block.getBlockData());
    }
}

