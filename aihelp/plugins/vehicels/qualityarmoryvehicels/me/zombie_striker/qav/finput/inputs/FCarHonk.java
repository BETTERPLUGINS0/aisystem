/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.finput.inputs;

import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.finput.FInput;
import me.zombie_striker.qav.finput.FInputManager;

public class FCarHonk
implements FInput {
    public FCarHonk() {
        FInputManager.add(this);
    }

    @Override
    public void onInput(VehicleEntity vehicleEntity) {
        vehicleEntity.getDriverSeat().getWorld().playSound(vehicleEntity.getDriverSeat().getLocation(), "honk", 1.0f, 1.0f);
    }

    @Override
    public String getName() {
        return FInputManager.CAR_HONK;
    }
}

