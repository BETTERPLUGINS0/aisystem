/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.vehicles;

import java.util.ArrayList;
import java.util.List;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.vehicles.AbstractCar;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AbstractDrill
extends AbstractCar {
    private final ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

    public AbstractDrill(String string, int n) {
        super(string, n);
    }

    @Override
    public void tick(VehicleEntity vehicleEntity) {
        super.tick(vehicleEntity);
        if (vehicleEntity.getModelEntities().isEmpty()) {
            return;
        }
        Location location = vehicleEntity.getCenter().clone().add(0.0, 2.0, 0.0).add(vehicleEntity.getDirection());
        this.near(location.getBlock()).forEach(block -> {
            block.getDrops(this.pickaxe).forEach(itemStack -> QualityArmoryVehicles.giveOrDrop(vehicleEntity.getTrunk(), location, itemStack));
            block.setType(Material.AIR);
        });
    }

    @NotNull
    private List<Block> near(@NotNull Block block) {
        ArrayList<Block> arrayList = new ArrayList<Block>();
        for (double d = block.getLocation().getX() - 2.0; d <= block.getLocation().getX(); d += 1.0) {
            for (double d2 = block.getLocation().getY() - 2.0; d2 <= block.getLocation().getY(); d2 += 1.0) {
                Location location = new Location(block.getWorld(), d, d2, (double)block.getZ());
                arrayList.add(location.getBlock());
            }
        }
        return arrayList;
    }
}

