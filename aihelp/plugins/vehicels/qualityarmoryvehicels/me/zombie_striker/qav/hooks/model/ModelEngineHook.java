/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.ticxo.modelengine.api.ModelEngineAPI
 *  com.ticxo.modelengine.api.animation.state.DefaultStateHandler$Property
 *  com.ticxo.modelengine.api.animation.state.ModelState
 *  com.ticxo.modelengine.api.model.ActiveModel
 *  com.ticxo.modelengine.api.model.ModeledEntity
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.hooks.model;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.state.DefaultStateHandler;
import com.ticxo.modelengine.api.animation.state.ModelState;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.VehicleEntity;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModelEngineHook {
    private static final Map<UUID, ModeledEntity> MODELS = new HashMap<UUID, ModeledEntity>();
    private static boolean enabled = false;

    public static void init() {
        enabled = true;
    }

    public static void createModel(VehicleEntity vehicleEntity) {
        if (!enabled) {
            return;
        }
        try {
            ModelEngineHook.createModel0(vehicleEntity);
        } catch (Error | Exception throwable) {
            // empty catch block
        }
    }

    private static void createModel0(@NotNull VehicleEntity vehicleEntity) {
        MODELS.remove(vehicleEntity.getVehicleUUID());
        ActiveModel activeModel = ModelEngineAPI.createActiveModel((String)vehicleEntity.getType().getName());
        if (activeModel == null) {
            return;
        }
        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity((Entity)vehicleEntity.getModelEntity());
        if (modeledEntity == null) {
            return;
        }
        Main.DEBUG("Created model for " + vehicleEntity.getType().getName());
        vehicleEntity.getModelEntity().setHelmet(new ItemStack(Material.AIR));
        modeledEntity.addModel(activeModel, false);
        modeledEntity.getRangeManager().setRenderDistance(100);
        MODELS.put(vehicleEntity.getVehicleUUID(), modeledEntity);
    }

    public static void playAnimation(VehicleEntity vehicleEntity, String string) {
        if (!enabled) {
            return;
        }
        try {
            ModelEngineHook.playAnimation0(vehicleEntity, string);
        } catch (Error | Exception throwable) {
            Main.DEBUG("Failed to play animation for " + vehicleEntity.getType().getName() + ": " + throwable.getMessage());
        }
    }

    private static void playAnimation0(VehicleEntity vehicleEntity, String string) {
        if (!MODELS.containsKey(vehicleEntity.getVehicleUUID())) {
            ModelEngineHook.createModel(vehicleEntity);
        }
        MODELS.get(vehicleEntity.getVehicleUUID()).getModel(vehicleEntity.getType().getName()).getDefaultStateHandler().setProperty(ModelState.WALK, new DefaultStateHandler.Property(string, 10.0, 1.0, 1.0));
        Main.DEBUG("Playing animation for " + vehicleEntity.getType().getName());
    }

    public static boolean isInitialized() {
        return enabled;
    }
}

