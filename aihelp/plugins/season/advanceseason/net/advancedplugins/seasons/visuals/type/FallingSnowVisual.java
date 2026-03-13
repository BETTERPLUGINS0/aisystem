/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.seasons.visuals.type;

import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.seasons.biomes.BiomeUtils;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.visuals.type.AbstractFallingLeafVisual;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class FallingSnowVisual
extends AbstractFallingLeafVisual {
    @Override
    public SeasonType getType() {
        return SeasonType.WINTER;
    }

    @Override
    public void activate(Player player) {
        if (BiomeUtils.getNoSnowBiomes().contains(player.getLocation().getBlock().getBiome())) {
            return;
        }
        super.activate(player);
    }

    @Override
    protected void playEffect(Player player, Location location) {
        ASManager.playEffect(Particle.SNOWFLAKE.name(), 0.2f, 20, location);
    }
}

