/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.impl;

import nl.sbdeveloper.vehiclesplus.api.vehicles.HolderItemPosition;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.DefaultVehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Seat;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Rotor;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Skin;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.UpgradableSetting;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Exhaust;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Fuel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Gearbox;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.HeightLimit;
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

public class DefaultHelicopter
extends DefaultVehicleModel {
    @Override
    public VehicleModel build() {
        return VehicleModel.builder().id("ExampleHelicopter").displayName("&cExample &aHelicopter").typeId("helicopters").part(new Skin(0.0, 0.0, 0.0, new ItemBuilder(XMaterial.LEATHER_BOOTS.parseItem()).customModelData(3, itemBuilder -> itemBuilder.durability(3).unbreakable()).armorColor(Color.BLACK).getItemStack(), HolderItemPosition.HEAD)).part(new Seat(0.0, -0.8, 0.0, true)).part(new Seat(-1.0, -0.8, 0.0, false)).part(new Rotor()).permissions(new Permissions("ExampleHelicopter")).sounds(this.defaultSounds).availableColors(ColorList.of(Color.RED, Color.BLACK, Color.WHITE, Color.GRAY, Color.SILVER, Color.BLUE)).maxSpeed(new UpgradableSetting(100, 200, 5, 1000.0, "km/h")).fuelTank(new UpgradableSetting(50, 100, 5, 1000.0, "L")).turningRadius(new UpgradableSetting(7, 15, 1, 1000.0, "")).acceleration(new UpgradableSetting(50, 100, 1, 1000.0, "")).exhaust(new Exhaust(true, -3.0, 2.0, 0.0, XParticle.LARGE_SMOKE.get())).horn(new Horn(false, new Sounds.Sound(XSound.BLOCK_NOTE_BLOCK_BASS.name(), 3))).heightLimit(new HeightLimit(-64.0, 320.0)).drift(true).exitWhileMoving(true).price(100000.0).fuel(new Fuel("gasoline", 6.0)).health(100).trunkSize(0).hitbox(new Hitbox(2.0, 1.0, 1.0)).gearbox(new Gearbox(false)).build();
    }
}

