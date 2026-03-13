/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.api.util;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.server.VersionSupport;
import com.andrei1058.bedwars.api.util.BlockRay;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class BlastProtectionUtil {
    private final VersionSupport versionSupport;
    private final BedWars api;

    public BlastProtectionUtil(VersionSupport versionSupport, BedWars api) {
        this.versionSupport = versionSupport;
        this.api = api;
    }

    public boolean isProtected(@NotNull IArena arena, Location pov, @NotNull Block block, double step) {
        if (arena.isProtected(block.getLocation()) || arena.isTeamBed(block.getLocation())) {
            return true;
        }
        boolean rayBlockedByGlass = this.api.getConfigs().getMainConfig().getBoolean("blast-protection.ray-blocked-by-glass");
        Location target = block.getLocation();
        LinkedList<Vector> targetVectors = new LinkedList<Vector>();
        double alteredRayStep = 0.73;
        for (double XrayRadius = alteredRayStep * -1.0; XrayRadius <= alteredRayStep; XrayRadius += alteredRayStep) {
            for (double YrayRadius = alteredRayStep * -1.0; YrayRadius <= alteredRayStep; YrayRadius += alteredRayStep) {
                for (double ZrayRadius = alteredRayStep * -1.0; ZrayRadius <= alteredRayStep; ZrayRadius += alteredRayStep) {
                    targetVectors.add(pov.clone().toVector().toBlockVector().add(new Vector(XrayRadius, YrayRadius, ZrayRadius)));
                }
            }
        }
        AtomicInteger protectedTimes = new AtomicInteger();
        int totalRays = targetVectors.size();
        targetVectors.forEach(targetVector -> {
            BlockRay ray;
            try {
                ray = new BlockRay(block.getWorld(), (Vector)targetVector, target.toVector(), step);
            } catch (IllegalArgumentException ignored) {
                return;
            }
            while (ray.hasNext()) {
                Block nextBlock = ray.next();
                if (nextBlock.getType() == Material.AIR) continue;
                if (rayBlockedByGlass && this.versionSupport.isGlass(nextBlock.getType())) {
                    protectedTimes.getAndIncrement();
                    return;
                }
                if (arena.isBlockPlaced(nextBlock) || arena.isAllowMapBreak()) continue;
                protectedTimes.getAndIncrement();
                return;
            }
        });
        return totalRays - protectedTimes.get() < 6;
    }
}

