/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.impl;

import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.HolderItemPosition;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.DefaultVehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.Wheel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Seat;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Skin;
import nl.sbdeveloper.vehiclesplus.api.vehicles.rims.RimDesign;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.UpgradableSetting;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Exhaust;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Fuel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Gearbox;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Hitbox;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Horn;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Permissions;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Sounds;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XSound;
import nl.sbdeveloper.vehiclesplus.libs.xseries.particles.XParticle;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.jackson.ColorList;
import org.bukkit.Color;

public class DefaultCar
extends DefaultVehicleModel {
    @Override
    public VehicleModel build() {
        Optional<RimDesign> optional = VehiclesPlusAPI.getRimDesign(VehiclesPlusPluginManager.getConfig().getDefaultRimDesignId());
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Default rimdesign not found!");
        }
        RimDesign rimDesign = optional.get();
        return VehicleModel.builder().id("ExampleCar").displayName("&cExample &aCar").typeId("cars").part(new Skin(0.0, -0.2, 0.0, new ItemBuilder(XMaterial.LEATHER_BOOTS.parseItem()).customModelData(1, itemBuilder -> itemBuilder.durability(1).unbreakable()).armorColor(Color.WHITE).getItemStack(), HolderItemPosition.HEAD)).part(new Seat(0.3, -1.3, 0.65, true)).part(new Seat(0.3, -1.3, -0.65, false)).part(new Seat(-0.7, -1.3, 0.65, false)).part(new Seat(-0.7, -1.3, -0.65, false)).part(new Wheel(1.89, 0.0, -1.13, rimDesign, Color.GRAY, true, 180)).part(new Wheel(1.89, 0.0, 1.13, rimDesign, Color.GRAY, true, 0)).part(new Wheel(-1.57, 0.0, -1.13, rimDesign, Color.GRAY, false, 180)).part(new Wheel(-1.57, 0.0, 1.13, rimDesign, Color.GRAY, false, 0)).permissions(new Permissions("ExampleCar")).sounds(this.defaultSounds).availableColors(ColorList.of(Color.RED, Color.BLACK, Color.WHITE, Color.GRAY, Color.SILVER, Color.BLUE)).maxSpeed(new UpgradableSetting(100, 200, 5, 1000.0, "km/h")).fuelTank(new UpgradableSetting(50, 100, 5, 1000.0, "L")).turningRadius(new UpgradableSetting(7, 15, 1, 1000.0, "")).acceleration(new UpgradableSetting(50, 100, 5, 1000.0, "")).exhaust(new Exhaust(true, -3.0, 0.1, -1.0, XParticle.LARGE_SMOKE.get())).horn(new Horn(true, new Sounds.Sound(XSound.BLOCK_NOTE_BLOCK_BASS.name(), 3))).drift(true).exitWhileMoving(true).price(100000.0).fuel(new Fuel("gasoline", 6.0)).health(100).trunkSize(27).hitbox(new Hitbox(3.05, 2.0, 1.25)).gearbox(new Gearbox(true)).build();
    }
}

