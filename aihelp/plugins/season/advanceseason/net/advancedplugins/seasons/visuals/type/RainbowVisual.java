/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.Particle$DustOptions
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.seasons.visuals.type;

import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.visuals.type.IVisualType;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class RainbowVisual
implements IVisualType {
    private Location loc = null;
    private boolean enabled = true;

    @Override
    public void tick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.activate(player);
        }
    }

    @Override
    public void tickSync() {
    }

    @Override
    public SeasonType getType() {
        return SeasonType.WINTER;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean bl) {
        this.enabled = bl;
    }

    @Override
    public void activate(Player player) {
        if (this.loc == null) {
            this.loc = player.getLocation().add(10.0, 0.0, 0.0);
        }
        double d = 30.0;
        Color[] colorArray = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, Color.LIME, Color.BLUE, Color.FUCHSIA, Color.PURPLE};
        int n = 200;
        double d2 = d * 0.1;
        double d3 = 0.3;
        for (int i = colorArray.length - 1; i >= 0; --i) {
            Particle.DustOptions dustOptions = new Particle.DustOptions(colorArray[i], 1.0f);
            for (int j = 0; j < n; ++j) {
                double d4 = Math.PI * (double)j / (double)n;
                double d5 = d * Math.cos(d4);
                double d6 = d * Math.sin(d4) * 1.4 + (double)i * d3;
                d6 = d6 - d + d3 * (double)colorArray.length;
                player.getWorld().spawnParticle(Particle.REDSTONE, this.loc.clone().add(d5, d6, 0.0), 1, (Object)dustOptions);
            }
        }
    }
}

