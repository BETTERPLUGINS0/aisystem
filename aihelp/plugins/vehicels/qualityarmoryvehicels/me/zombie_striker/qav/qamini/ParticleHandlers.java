/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.zombie_striker.qg.handlers.ParticleHandlers
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Particle$DustOptions
 *  org.bukkit.entity.Player
 */
package me.zombie_striker.qav.qamini;

import me.zombie_striker.qav.qamini.QAMini;
import me.zombie_striker.qav.util.xseries.particles.XParticle;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleHandlers {
    public static boolean is13 = true;

    public static void initValues() {
        is13 = QAMini.isVersionHigherThan(1, 13);
    }

    public static void spawnExplosion(Location location) {
        try {
            me.zombie_striker.qg.handlers.ParticleHandlers.spawnExplosion((Location)location);
            return;
        } catch (Error | Exception throwable) {
            try {
                location.getWorld().spawnParticle(XParticle.EXPLOSION_EMITTER.get(), location, 1);
            } catch (Error | Exception throwable2) {
                // empty catch block
            }
            return;
        }
    }

    public static void spawnMushroomCloud(Location location) {
        try {
            me.zombie_striker.qg.handlers.ParticleHandlers.spawnMushroomCloud((Location)location);
            return;
        } catch (Error | Exception throwable) {
            try {
                for (double d = 0.0; d < Math.PI * 2; d += 0.06544984694978735) {
                    double d2 = 2.0;
                    ParticleHandlers.spawnParticle(1.0, 1.0, 1.0, new Location(location.getWorld(), location.getX() + Math.sin(d) * d2, location.getY(), location.getZ() + Math.cos(d) * d2));
                    d2 = 1.8;
                    ParticleHandlers.spawnParticle(1.0, 0.0, 0.0, new Location(location.getWorld(), location.getX() + Math.sin(d) * d2, location.getY() + 0.5, location.getZ() + Math.cos(d) * d2));
                    d2 = 1.6;
                    ParticleHandlers.spawnParticle(1.0, 0.2, 0.0, new Location(location.getWorld(), location.getX() + Math.sin(d) * d2, location.getY() + 1.0, location.getZ() + Math.cos(d) * d2));
                    d2 = 1.3;
                    ParticleHandlers.spawnParticle(1.0, 0.2, 0.0, new Location(location.getWorld(), location.getX() + Math.sin(d) * d2, location.getY() + 1.5, location.getZ() + Math.cos(d) * d2));
                    d2 = 1.1;
                    ParticleHandlers.spawnParticle(1.0, 0.5, 0.0, new Location(location.getWorld(), location.getX() + Math.sin(d) * d2, location.getY() + 2.0, location.getZ() + Math.cos(d) * d2));
                    d2 = 1.0;
                    ParticleHandlers.spawnParticle(1.0, 0.5, 0.0, new Location(location.getWorld(), location.getX() + Math.sin(d) * d2, location.getY() + 2.5, location.getZ() + Math.cos(d) * d2));
                    d2 = 3.0;
                    ParticleHandlers.spawnParticle(1.0, 0.5, 0.0, new Location(location.getWorld(), location.getX() + Math.sin(d) * d2, location.getY() + 3.0, location.getZ() + Math.cos(d) * d2));
                    d2 = 2.8;
                    ParticleHandlers.spawnParticle(1.0, 0.5, 0.0, new Location(location.getWorld(), location.getX() + Math.sin(d) * d2, location.getY() + 3.5, location.getZ() + Math.cos(d) * d2));
                    d2 = 2.5;
                    ParticleHandlers.spawnParticle(1.0, 1.0, 1.0, new Location(location.getWorld(), location.getX() + Math.sin(d) * d2, location.getY() + 4.0, location.getZ() + Math.cos(d) * d2));
                    d2 = 2.0;
                    ParticleHandlers.spawnParticle(1.0, 1.0, 1.0, new Location(location.getWorld(), location.getX() + Math.sin(d) * d2, location.getY() + 4.5, location.getZ() + Math.cos(d) * d2));
                    d2 = 1.5;
                    ParticleHandlers.spawnParticle(1.0, 0.2, 0.0, new Location(location.getWorld(), location.getX() + Math.sin(d) * d2, location.getY() + 5.0, location.getZ() + Math.cos(d) * d2));
                    d2 = 0.8;
                    ParticleHandlers.spawnParticle(1.0, 0.5, 0.0, new Location(location.getWorld(), location.getX() + Math.sin(d) * d2, location.getY() + 5.5, location.getZ() + Math.cos(d) * d2));
                }
            } catch (Error | Exception throwable2) {
                // empty catch block
            }
            return;
        }
    }

    public static void spawnParticle(double d, double d2, double d3, Location location) {
        try {
            me.zombie_striker.qg.handlers.ParticleHandlers.spawnParticle((double)d, (double)d2, (double)d3, (Location)location);
            return;
        } catch (Error | Exception throwable) {
            try {
                if (is13) {
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB((int)((int)(d * 255.0)), (int)((int)(d2 * 255.0)), (int)((int)(d3 * 255.0))), 1.0f);
                    location.getWorld().spawnParticle(XParticle.DUST.get(), location.getX(), location.getY(), location.getZ(), 0, 0.0, 0.0, 0.0, (Object)dustOptions);
                } else {
                    location.getWorld().spawnParticle(XParticle.DUST.get(), location.getX(), location.getY(), location.getZ(), 0, d, d2, d3, 1.0);
                }
            } catch (Error | Exception throwable2) {
                throwable2.printStackTrace();
            }
            return;
        }
    }

    public static void spawnMuzzleSmoke(Player player, Location location) {
        try {
            me.zombie_striker.qg.handlers.ParticleHandlers.spawnMuzzleSmoke((Player)player, (Location)location);
            return;
        } catch (Error | Exception throwable) {
            try {
                double d = Math.atan2(player.getLocation().getDirection().getX(), player.getLocation().getDirection().getZ());
                double d2 = Math.sin(d -= 0.39269908169872414);
                double d3 = Math.cos(d);
                Location location2 = location.clone().add(d2, 0.0, d3);
                for (int i = 0; i < 2; ++i) {
                    location.getWorld().spawnParticle(XParticle.EFFECT.get(), location2, 0);
                }
            } catch (Error | Exception throwable2) {
                // empty catch block
            }
            return;
        }
    }
}

