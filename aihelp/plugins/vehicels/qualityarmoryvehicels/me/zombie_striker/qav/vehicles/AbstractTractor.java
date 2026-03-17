/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.vehicles;

import java.util.ArrayList;
import java.util.List;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.util.BlockCollisionUtil;
import me.zombie_striker.qav.util.xseries.XMaterial;
import me.zombie_striker.qav.vehicles.AbstractCar;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class AbstractTractor
extends AbstractCar {
    public AbstractTractor(String string, int n) {
        super(string, n);
    }

    @Override
    public void tick(VehicleEntity vehicleEntity) {
        super.tick(vehicleEntity);
        if (vehicleEntity.getModelEntities().size() == 0) {
            return;
        }
        this.near(vehicleEntity.getCenter().clone().subtract(0.0, 1.0, 0.0).getBlock()).forEach(block -> {
            if (BlockCollisionUtil.getMaterial(block.getLocation()) == XMaterial.FARMLAND.parseMaterial()) {
                block.getLocation().add(0.0, 1.0, 0.0).getBlock().setType(XMaterial.WHEAT.parseMaterial());
            }
        });
    }

    @NotNull
    private List<Block> near(@NotNull Block block) {
        ArrayList<Block> arrayList = new ArrayList<Block>();
        for (double d = block.getLocation().getX() - 2.0; d <= block.getLocation().getX(); d += 1.0) {
            for (double d2 = block.getLocation().getZ() - 2.0; d2 <= block.getLocation().getZ(); d2 += 1.0) {
                Location location = new Location(block.getWorld(), d, (double)block.getY(), d2);
                arrayList.add(location.getBlock());
            }
        }
        return arrayList;
    }

    @Override
    boolean canJump() {
        return false;
    }
}

