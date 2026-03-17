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
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Skin;
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

public class DefaultBoat
extends DefaultVehicleModel {
    @Override
    public VehicleModel build() {
        return VehicleModel.builder().id("ExampleBoat").displayName("&cExample &aBoat").typeId("boats").part(new Skin(0.0, 0.3, 0.0, new ItemBuilder(XMaterial.LEATHER_BOOTS.parseItem()).customModelData(4, itemBuilder -> itemBuilder.durability(4).unbreakable()).armorColor(Color.WHITE).getItemStack(), HolderItemPosition.HEAD)).part(new Seat(-0.5, -0.5, 0.5, true)).part(new Seat(-0.5, -0.5, -0.5, false)).part(new Seat(-2.3, -0.2, -0.6, -90, false)).permissions(new Permissions("ExampleBoat")).sounds(this.defaultSounds).availableColors(ColorList.of(Color.RED, Color.BLACK, Color.WHITE, Color.GRAY, Color.SILVER, Color.BLUE)).maxSpeed(new UpgradableSetting(100, 200, 5, 1000.0, "km/h")).fuelTank(new UpgradableSetting(50, 100, 5, 1000.0, "L")).turningRadius(new UpgradableSetting(5, 10, 1, 1000.0, "")).acceleration(new UpgradableSetting(50, 100, 1, 1000.0, "")).exhaust(new Exhaust(true, -3.5, 0.0, 0.0, XParticle.LARGE_SMOKE.get())).horn(new Horn(true, new Sounds.Sound(XSound.BLOCK_NOTE_BLOCK_BASS.name(), 3))).drift(false).exitWhileMoving(true).price(100000.0).fuel(new Fuel("gasoline", 6.0)).health(100).trunkSize(0).hitbox(new Hitbox(4.0, 2.0, 1.2)).gearbox(new Gearbox(false)).build();
    }
}

