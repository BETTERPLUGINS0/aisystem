/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.seasons.visuals.type;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.MathUtils;
import net.advancedplugins.seasons.biomes.BiomeUtils;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.visuals.type.IVisualType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class BlizzardVisual
implements IVisualType {
    private ConcurrentHashMap<UUID, BlizzardProgress> progress = new ConcurrentHashMap();
    private boolean enabled = true;

    @Override
    public void tick() {
        Iterator<Map.Entry<UUID, BlizzardProgress>> iterator = this.progress.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, BlizzardProgress> entry = iterator.next();
            BlizzardProgress blizzardProgress = entry.getValue();
            if (blizzardProgress.currentStep < blizzardProgress.locations.length) {
                Location location = blizzardProgress.locations[blizzardProgress.currentStep];
                this.createBlizzardParticles(location);
                ++blizzardProgress.currentStep;
                continue;
            }
            iterator.remove();
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
        Location location;
        if (BiomeUtils.getNoSnowBiomes().contains(player.getLocation().getBlock().getBiome())) {
            return;
        }
        if (player.getLocation().getY() < 64.0) {
            return;
        }
        BlizzardProgress blizzardProgress = new BlizzardProgress();
        int n = MathUtils.randomBetween(-10, 10);
        int n2 = MathUtils.randomBetween(-10, 10);
        blizzardProgress.locations[0] = location = player.getLocation().clone().add((double)n, 1.0, (double)n2);
        double d = MathUtils.randomBetween(-1, 1);
        double d2 = MathUtils.randomBetween(-1, 1);
        double d3 = Math.sqrt(d * d + d2 * d2);
        d /= d3;
        d2 /= d3;
        for (int i = 1; i < blizzardProgress.locations.length; ++i) {
            Location location2 = blizzardProgress.locations[i - 1];
            blizzardProgress.locations[i] = location2.clone().add(d, 0.0, d2);
        }
        this.progress.put(player.getUniqueId(), blizzardProgress);
    }

    private void createBlizzardParticles(Location location) {
        ASManager.playEffect(Particle.SNOWFLAKE.name(), 3.0f, 40, location);
        ASManager.playEffect(Particle.SNOW_SHOVEL.name(), 3.0f, 40, location);
    }

    class BlizzardProgress {
        public Location[] locations = new Location[7];
        public int currentStep = 0;

        BlizzardProgress() {
        }
    }
}

