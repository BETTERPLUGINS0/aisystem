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

import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.visuals.type.IVisualType;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class AuroraVisual
implements IVisualType {
    private final int extend = Bukkit.getViewDistance() * 16 / 3;
    private final Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB((int)84, (int)255, (int)84), 1.0f);
    private boolean enabled = true;

    @Override
    public void tick() {
        if (!this.isEnabled()) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!Core.getWorldHandler().isWorldEnabled(player.getWorld().getName()) || ASManager.isDay(player.getWorld().getTime()) || !Core.getSeasonHandler().getSeason(player.getWorld()).getType().equals((Object)SeasonType.WINTER)) continue;
            Location location = player.getLocation().clone();
            location.add(0.0, 30.0, 0.0);
            player.spawnParticle(Particle.REDSTONE, location, 2000, (double)this.extend, 2.0, (double)this.extend, 1.0, (Object)this.dustOptions);
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
    }
}

