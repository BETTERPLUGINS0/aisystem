/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.zombie_striker.qg.ammo.Ammo
 *  me.zombie_striker.qg.api.QualityArmory
 *  me.zombie_striker.qg.guns.utils.GunUtil
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package me.zombie_striker.qav.finput.inputs;

import java.util.ArrayList;
import java.util.Objects;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.finput.FInput;
import me.zombie_striker.qav.finput.FInputManager;
import me.zombie_striker.qav.qamini.ExplosionHandler;
import me.zombie_striker.qav.qamini.ParticleHandlers;
import me.zombie_striker.qav.util.BlockCollisionUtil;
import me.zombie_striker.qg.ammo.Ammo;
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.guns.utils.GunUtil;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class F40mmLauncher
implements FInput {
    public F40mmLauncher() {
        FInputManager.add(this);
    }

    @Override
    public void onInput(VehicleEntity vehicleEntity) {
        ItemStack itemStack;
        boolean bl;
        block11: {
            bl = false;
            try {
                Ammo ammo = QualityArmory.getAmmoByName((String)"40mm");
                if (ammo == null) break block11;
                for (int i = 0; i < vehicleEntity.getTrunk().getSize(); ++i) {
                    itemStack = vehicleEntity.getTrunk().getItem(i);
                    if (itemStack == null || QualityArmory.getAmmo((ItemStack)itemStack) != ammo) continue;
                    bl = true;
                    if (itemStack.getAmount() > 1) {
                        itemStack.setAmount(itemStack.getAmount() - 1);
                    } else {
                        itemStack = null;
                    }
                    vehicleEntity.getTrunk().setItem(i, itemStack);
                    break;
                }
            } catch (Error | Exception throwable) {
                // empty catch block
            }
        }
        if (!bl) {
            for (int i = 0; i < vehicleEntity.getTrunk().getSize(); ++i) {
                ItemStack itemStack2 = vehicleEntity.getTrunk().getItem(i);
                if (itemStack2 == null || itemStack2.getType() != Material.TNT) continue;
                bl = true;
                if (itemStack2.getAmount() > 1) {
                    itemStack2.setAmount(itemStack2.getAmount() - 1);
                } else {
                    itemStack2 = null;
                }
                vehicleEntity.getTrunk().setItem(i, itemStack2);
                break;
            }
        }
        if (bl) {
            Entity entity = vehicleEntity.getDriverSeat().getPassenger();
            Location location = ((Player)Objects.requireNonNull(entity)).getEyeLocation();
            itemStack = (Player)vehicleEntity.getDriverSeat().getPassenger();
            final Vector vector = itemStack.getLocation().getDirection().normalize();
            if (vector.getY() < 0.0) {
                vector.setY(0);
                vector.normalize();
            }
            location.add(vector);
            itemStack.getWorld().playSound(location, "warheadlaunch", 10.0f, 1.0f);
            final Location location2 = location;
            new BukkitRunnable((Player)itemStack){
                int distance = 100;
                final int ticks = 3;
                final /* synthetic */ Player val$player;
                {
                    this.val$player = player;
                }

                public void run() {
                    vector.setY(vector.getY() - 0.05);
                    for (int i = 0; i < 3; ++i) {
                        boolean bl;
                        --this.distance;
                        location2.add(vector);
                        ParticleHandlers.spawnParticle(1.0, 1.0, 1.0, location2);
                        boolean bl2 = false;
                        try {
                            ArrayList arrayList = new ArrayList(location2.getWorld().getNearbyEntities(location2, 1.0, 1.0, 1.0));
                            if (!(arrayList.isEmpty() || arrayList.size() <= 1 && arrayList.get(0) == this.val$player)) {
                                bl2 = true;
                            }
                        } catch (Error error) {
                            // empty catch block
                        }
                        try {
                            bl = GunUtil.isSolid((Block)location2.getBlock(), (Location)location2);
                        } catch (Error | Exception throwable) {
                            bl = BlockCollisionUtil.isSolid(location2);
                        }
                        if (!bl && !bl2 && this.distance >= 0) continue;
                        ExplosionHandler.handleAOEExplosion((Entity)this.val$player, location2, 100.0, 3.0);
                        ParticleHandlers.spawnExplosion(location2);
                        try {
                            this.val$player.getWorld().playSound(location2, "warheadexplode", 10.0f, 1.5f);
                            this.val$player.getWorld().playSound(location2, Sound.ENTITY_GENERIC_EXPLODE, 8.0f, 0.7f);
                        } catch (Error error) {
                            location2.getWorld().playEffect(location2, Effect.valueOf((String)"CLOUD"), 0);
                            this.val$player.getWorld().playSound(location2, Sound.valueOf((String)"EXPLODE"), 8.0f, 0.7f);
                        }
                        this.cancel();
                        return;
                    }
                }
            }.runTaskTimer((Plugin)QualityArmoryVehicles.getPlugin(), 0L, 1L);
        }
    }

    @Override
    public String getName() {
        return FInputManager.LAUNCHER_40mm;
    }
}

