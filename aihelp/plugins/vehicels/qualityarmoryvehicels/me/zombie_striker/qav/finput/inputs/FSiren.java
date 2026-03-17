/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.zombie_striker.qg.guns.utils.WeaponSounds
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package me.zombie_striker.qav.finput.inputs;

import java.util.HashMap;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.finput.FInput;
import me.zombie_striker.qav.finput.FInputManager;
import me.zombie_striker.qg.guns.utils.WeaponSounds;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class FSiren
implements FInput {
    HashMap<VehicleEntity, BukkitTask> storedSirens = new HashMap();

    public FSiren() {
        FInputManager.add(this);
    }

    @Override
    public void onInput(final VehicleEntity vehicleEntity) {
        if (this.storedSirens.containsKey(vehicleEntity)) {
            this.storedSirens.remove(vehicleEntity).cancel();
        } else {
            this.storedSirens.put(vehicleEntity, new BukkitRunnable(){

                public void run() {
                    if (vehicleEntity == null || vehicleEntity.isInvalid()) {
                        this.cancel();
                        FSiren.this.storedSirens.remove(vehicleEntity);
                        return;
                    }
                    try {
                        vehicleEntity.getDriverSeat().getWorld().playSound(vehicleEntity.getDriverSeat().getLocation(), WeaponSounds.SIREN.getSoundName(), 2.0f, 1.0f);
                    } catch (Error | Exception throwable) {
                        vehicleEntity.getDriverSeat().getWorld().playSound(vehicleEntity.getDriverSeat().getLocation(), "siren", 2.0f, 1.0f);
                    }
                }
            }.runTaskTimer((Plugin)QualityArmoryVehicles.getPlugin(), 0L, 20L));
        }
    }

    @Override
    public String getName() {
        return FInputManager.POLICE_SIREN;
    }
}

