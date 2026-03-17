/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.zombie_striker.qg.QAMain
 *  me.zombie_striker.qg.ammo.Ammo
 *  me.zombie_striker.qg.api.QualityArmory
 *  me.zombie_striker.qg.armor.BulletProtectionUtil
 *  me.zombie_striker.qg.boundingbox.AbstractBoundingBox
 *  me.zombie_striker.qg.boundingbox.BoundingBoxManager
 *  me.zombie_striker.qg.handlers.BulletWoundHandler
 *  me.zombie_striker.qg.handlers.ParticleHandlers
 *  me.zombie_striker.qg.handlers.SoundHandler
 *  org.bukkit.Effect
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.attribute.AttributeModifier
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package me.zombie_striker.qav.finput.inputs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.finput.FInput;
import me.zombie_striker.qav.finput.FInputManager;
import me.zombie_striker.qav.util.BlockCollisionUtil;
import me.zombie_striker.qg.QAMain;
import me.zombie_striker.qg.ammo.Ammo;
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.armor.BulletProtectionUtil;
import me.zombie_striker.qg.boundingbox.AbstractBoundingBox;
import me.zombie_striker.qg.boundingbox.BoundingBoxManager;
import me.zombie_striker.qg.handlers.BulletWoundHandler;
import me.zombie_striker.qg.handlers.ParticleHandlers;
import me.zombie_striker.qg.handlers.SoundHandler;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FShootBullet
implements FInput {
    public FShootBullet() {
        FInputManager.add(this);
    }

    @Override
    public void onInput(VehicleEntity vehicleEntity) {
        ItemStack itemStack;
        Ammo ammo;
        boolean bl;
        block7: {
            bl = false;
            try {
                ammo = QualityArmory.getAmmoByName((String)"556");
                if (ammo == null) break block7;
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
        ammo = (Player)vehicleEntity.getDriverSeat().getPassenger();
        if (ammo != null && bl) {
            Vector vector = QualityArmoryVehicles.rotateRelToCar(vehicleEntity, (Entity)vehicleEntity.getModelEntity(), vehicleEntity.getType().getCenterFromControlSeat().clone().add(new Vector(vehicleEntity.getBoundingBox().getWidth() * vehicleEntity.getDirection().getX(), vehicleEntity.getDirection().getY(), vehicleEntity.getDirection().getZ() * vehicleEntity.getBoundingBox().getWidth())), false);
            itemStack = vehicleEntity.getDriverSeat();
            Location location = itemStack.getLocation().add(vector).add(vector.clone().normalize());
            Vector vector2 = ammo.getLocation().getDirection().normalize();
            if (vector2.getY() < 0.0) {
                vector2.setY(0);
                vector2.normalize();
            }
            FShootBullet.shootInstantVector((Player)ammo, 0.3, 3.0, 2, 300);
            ammo.getWorld().playSound(location, "bulletbig", 10.0f, 1.0f);
        }
    }

    @Override
    public String getName() {
        return FInputManager.LAUNCHER_556;
    }

    /*
     * Could not resolve type clashes
     */
    public static void shootInstantVector(Player player, double d, double d2, int n, int n2) {
        for (int i = 0; i < n; ++i) {
            int n3;
            Location location = player.getEyeLocation().clone();
            Vector vector = player.getLocation().getDirection().normalize();
            vector.add(new Vector(Math.random() * 2.0 * d - d, Math.random() * 2.0 * d - d, Math.random() * 2.0 * d - d));
            Vector vector2 = vector.clone().multiply(0.2);
            Entity entity = null;
            boolean bl = false;
            Location location2 = null;
            int n4 = (int)FShootBullet.getTargetedSolidMaxDistance(vector2, location, n2) / 2;
            double d3 = n4;
            ArrayList<Location> arrayList = new ArrayList<Location>();
            ArrayList<Location> arrayList2 = new ArrayList<Location>();
            Location location3 = location.clone().add(vector.clone().multiply(n4));
            for (Entity entity2 : location3.getWorld().getNearbyEntities(location3, (double)n4, (double)n4, (double)n4)) {
                Player player2;
                double d4;
                if (!(entity2 instanceof Damageable) || QAMain.avoidTypes.contains(entity2.getType()) || entity2 == player || entity2 == player.getVehicle() || entity2 == player.getPassenger() || (d4 = entity2.getLocation().distance(location)) > d3) continue;
                AbstractBoundingBox abstractBoundingBox = BoundingBoxManager.getBoundingBox((Entity)entity2);
                Location location4 = location.clone();
                if (entity2 instanceof Player && (player2 = (Player)entity2).getGameMode() == GameMode.SPECTATOR) continue;
                boolean bl2 = false;
                n3 = 0;
                while ((double)n3 < d4 / QAMain.bulletStep) {
                    location4.add(vector2);
                    if (abstractBoundingBox.intersects((Entity)player, location4, entity2)) {
                        bl2 = true;
                        break;
                    }
                    ++n3;
                }
                if (!bl2) continue;
                location2 = location4;
                d3 = d4;
                entity = entity2;
                bl = abstractBoundingBox.allowsHeadshots() && abstractBoundingBox.intersectsHead(location4, entity2);
                if (!bl) continue;
                QAMain.DEBUG((String)"Headshot!");
                if (!QAMain.headshotPling) continue;
                try {
                    player.playSound(player.getLocation(), QAMain.headshot_sound, 2.0f, 1.0f);
                    if (QAMain.isVersionHigherThan((int)1, (int)9)) continue;
                    try {
                        player.playSound(player.getLocation(), Sound.valueOf((String)"LAVA_POP"), 6.0f, 1.0f);
                    } catch (Error | Exception throwable) {
                    }
                } catch (Error | Exception throwable) {
                    player.playSound(player.getLocation(), Sound.valueOf((String)"LAVA_POP"), 1.0f, 1.0f);
                }
            }
            if (entity != null) {
                if (!(entity instanceof Player) || QualityArmory.allowGunsInRegion((Location)entity.getLocation())) {
                    boolean bl3 = false;
                    double d5 = d2 * 1.0 * (double)(bl ? (QAMain.HeadshotOneHit ? 50 : 2) : 1);
                    if (entity instanceof Player) {
                        bl3 = BulletProtectionUtil.stoppedBullet((Player)player, location2, (Vector)vector);
                    }
                    if (entity instanceof Player) {
                        Player player3 = (Player)entity;
                        if (QAMain.enableArmorIgnore) {
                            try {
                                double d6 = 0.0;
                                double d7 = 0.0;
                                for (Player player4 : new ItemStack[]{player3.getInventory().getHelmet(), player3.getInventory().getChestplate(), player3.getInventory().getLeggings(), player3.getInventory().getBoots()}) {
                                    if (player4 == null) continue;
                                    if (player4.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ARMOR) != null && !Objects.requireNonNull(player4.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ARMOR)).isEmpty()) {
                                        for (AttributeModifier attributeModifier : (Collection)Optional.ofNullable(player4.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ARMOR)).orElse(new ArrayList())) {
                                            d6 += attributeModifier.getAmount();
                                        }
                                    }
                                    for (AttributeModifier attributeModifier : (Collection)Optional.ofNullable(player4.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ARMOR_TOUGHNESS)).orElse(new ArrayList())) {
                                        d7 += attributeModifier.getAmount();
                                    }
                                }
                                d5 *= 1.0 - Math.min(20.0, Math.max(d6 / 5.0, d6 - d5 / (d7 / 4.0 + 2.0))) / 25.0;
                            } catch (Error | Exception throwable) {
                                // empty catch block
                            }
                        }
                        if (!bl3) {
                            BulletWoundHandler.bulletHit((Player)((Player)entity), (double)1.0);
                        } else {
                            entity.sendMessage(QAMain.S_BULLETPROOFSTOPPEDBLEEDING);
                        }
                    }
                    ((Damageable)entity).damage(d5, (Entity)player);
                    if (entity instanceof LivingEntity) {
                        ((LivingEntity)entity).setNoDamageTicks(0);
                    }
                    QAMain.DEBUG((String)("Damaging entity " + entity.getName()));
                }
            } else {
                QAMain.DEBUG((String)"No enities hit.");
            }
            double d8 = 0.0;
            List list = location.getWorld().getPlayers();
            list.remove(player);
            double d9 = d3;
            for (double d10 = 0.0; d10 < d9; d10 += QAMain.bulletStep) {
                block40: {
                    location.add(vector2);
                    n3 = FShootBullet.isSolid(location);
                    if (n3 != 0 && !arrayList.contains(new Location(location.getWorld(), (double)location.getBlockX(), (double)location.getBlockY(), (double)location.getBlockZ()))) {
                        arrayList.add(new Location(location.getWorld(), (double)location.getBlockX(), (double)location.getBlockY(), (double)location.getBlockZ()));
                    }
                    if (QAMain.destructableBlocks.contains(BlockCollisionUtil.getMaterial(location))) {
                        arrayList2.add(location);
                    }
                    try {
                        int n5 = 3;
                        if (d10 % (double)n5 != 0.0) break block40;
                        ArrayList<Player> arrayList3 = new ArrayList<Player>();
                        for (Player player4 : list) {
                            if (!(player4.getLocation().distance(location) < (double)(n5 * 2))) continue;
                            try {
                                location.getWorld().playSound(location, Sound.BLOCK_DISPENSER_LAUNCH, 2.0f, 3.0f);
                            } catch (Error error) {
                                location.getWorld().playSound(location, Sound.valueOf((String)"SHOOT_ARROW"), 2.0f, 2.0f);
                            }
                            arrayList3.add(player4);
                        }
                        for (Player player4 : arrayList3) {
                            list.remove(player4);
                        }
                    } catch (Error | Exception throwable) {
                        if (d10 % 30.0 != 0.0) break block40;
                        try {
                            location.getWorld().playSound(location, Sound.BLOCK_DISPENSER_LAUNCH, 2.0f, 2.0f);
                            location.getWorld().playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 2.0f, 2.0f);
                        } catch (Error error) {
                            location.getWorld().playSound(location, Sound.valueOf((String)"SHOOT_ARROW"), 2.0f, 2.0f);
                            location.getWorld().playSound(location, Sound.valueOf((String)"FIRE_IGNITE"), 2.0f, 2.0f);
                        }
                    }
                }
                if (!FShootBullet.isSolid(location)) {
                    if (!QAMain.enableBulletTrails) continue;
                    if (d8 >= QAMain.smokeSpacing * (double)i) {
                        try {
                            me.zombie_striker.qav.qamini.ParticleHandlers.spawnParticle(1.0, 1.0, 1.0, location);
                        } catch (Error | Exception throwable) {
                            ParticleHandlers.spawnParticle((double)1.0, (double)1.0, (double)1.0, (Location)location);
                        }
                        d8 = 0.0;
                        continue;
                    }
                    d8 += QAMain.bulletStep;
                    continue;
                }
                location.getWorld().playEffect(location, Effect.STEP_SOUND, (Object)BlockCollisionUtil.getMaterial(location));
                break;
            }
            for (Location location5 : arrayList2) {
                location5.getBlock().breakNaturally();
            }
            if (!QAMain.blockBreakTexture) continue;
            for (Location location6 : arrayList) {
                location.getWorld().playSound(location, SoundHandler.getSoundWhenShot((Block)location.getBlock()), 2.0f, 1.0f);
            }
        }
    }

    public static double getTargetedSolidMaxDistance(Vector vector, Location location, double d) {
        Location location2 = location.clone();
        int n = 0;
        while ((double)n < d) {
            if (BlockCollisionUtil.getMaterial(location2) != Material.AIR && FShootBullet.isSolid(location2)) {
                return location.distance(location2);
            }
            location2.add(vector);
            ++n;
        }
        return d;
    }

    public static boolean isSolid(Location location) {
        return BlockCollisionUtil.isSolid(location);
    }
}

