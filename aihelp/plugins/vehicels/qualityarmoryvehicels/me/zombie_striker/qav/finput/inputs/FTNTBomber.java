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

public class FTNTBomber
implements FInput {
    public FTNTBomber() {
        FInputManager.add(this);
    }

    @Override
    public void onInput(VehicleEntity vehicleEntity) {
        ItemStack itemStack;
        boolean bl;
        block8: {
            bl = false;
            try {
                Ammo ammo = QualityArmory.getAmmoByName((String)"mininuke");
                if (ammo == null) break block8;
                for (int i = 0; i < vehicleEntity.getTrunk().getSize(); ++i) {
                    itemStack = vehicleEntity.getTrunk().getItem(i);
                    if (itemStack == null || QualityArmory.getAmmo((ItemStack)itemStack) != ammo) continue;
                    bl = true;
                    vehicleEntity.getTrunk().setItem(i, null);
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
            Location location = vehicleEntity.getDriverSeat().getLocation().add(QualityArmoryVehicles.rotateRelToCar(vehicleEntity.getModelEntity(), vehicleEntity.getType().getCenterFromControlSeat(), false)).subtract(0.0, 1.7, 0.0);
            Player player = (Player)vehicleEntity.getDriverSeat().getPassenger();
            itemStack = new Vector(0.0, -0.1, 0.0);
            new BukkitRunnable((Vector)itemStack, location, player, vehicleEntity){
                int distance = 300;
                final /* synthetic */ Vector val$dir;
                final /* synthetic */ Location val$s;
                final /* synthetic */ Player val$player;
                final /* synthetic */ VehicleEntity val$ve;
                {
                    this.val$dir = vector;
                    this.val$s = location;
                    this.val$player = player;
                    this.val$ve = vehicleEntity;
                }

                public void run() {
                    this.val$dir.setY(this.val$dir.getY() - 0.05);
                    int n = 0;
                    while ((double)n < Math.abs(this.val$dir.getY())) {
                        boolean bl;
                        --this.distance;
                        this.val$s.add(this.val$dir);
                        ParticleHandlers.spawnParticle(1.0, 1.0, 1.0, this.val$s);
                        boolean bl2 = false;
                        try {
                            ArrayList arrayList = new ArrayList(this.val$s.getWorld().getNearbyEntities(this.val$s, 1.0, 1.0, 1.0));
                            if (!(arrayList.isEmpty() || arrayList.size() <= 1 && (arrayList.get(0) == this.val$player || arrayList.contains(this.val$ve.getDriverSeat()) && arrayList.size() <= 1 + this.val$ve.getPassagerSeats().size()))) {
                                bl2 = true;
                            }
                        } catch (Error error) {
                            // empty catch block
                        }
                        try {
                            bl = GunUtil.isSolid((Block)this.val$s.getBlock(), (Location)this.val$s);
                        } catch (Error | Exception throwable) {
                            bl = BlockCollisionUtil.isSolid(this.val$s);
                        }
                        if (bl || bl2 || this.distance < 0) {
                            ExplosionHandler.handleAOEExplosion((Entity)this.val$player, this.val$s, 100.0, 8.0);
                            ParticleHandlers.spawnExplosion(this.val$s);
                            try {
                                this.val$ve.getDriverSeat().getWorld().playSound(this.val$s, "warheadexplode", 10.0f, 1.5f);
                                this.val$ve.getDriverSeat().getWorld().playSound(this.val$s, Sound.ENTITY_GENERIC_EXPLODE, 8.0f, 0.7f);
                            } catch (Error error) {
                                this.val$s.getWorld().playEffect(this.val$s, Effect.valueOf((String)"CLOUD"), 0);
                                this.val$s.getWorld().playSound(this.val$s, Sound.valueOf((String)"EXPLODE"), 8.0f, 0.7f);
                            }
                            this.cancel();
                            return;
                        }
                        ++n;
                    }
                }
            }.runTaskTimer((Plugin)QualityArmoryVehicles.getPlugin(), 0L, 1L);
        }
    }

    @Override
    public String getName() {
        return FInputManager.TNTBOMBER;
    }
}

