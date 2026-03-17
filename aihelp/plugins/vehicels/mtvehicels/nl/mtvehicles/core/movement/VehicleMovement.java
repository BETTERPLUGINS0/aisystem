/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.block.Block
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.block.data.type.Fence
 *  org.bukkit.block.data.type.Slab
 *  org.bukkit.block.data.type.Slab$Type
 *  org.bukkit.block.data.type.Snow
 *  org.bukkit.block.data.type.TrapDoor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_19_R2.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_21_R2.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_21_R3.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_21_R5.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_21_R6.entity.CraftEntity
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.TNTPrimed
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 */
package nl.mtvehicles.core.movement;

import com.google.common.collect.Sets;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.events.HornUseEvent;
import nl.mtvehicles.core.events.TankShootEvent;
import nl.mtvehicles.core.events.VehicleRegionEnterEvent;
import nl.mtvehicles.core.events.VehicleRegionLeaveEvent;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.enums.ServerVersion;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.utils.BossBarUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.movement.PacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class VehicleMovement {
    protected Object packet;
    protected Player player;
    protected VehicleType vehicleType;
    protected String license;
    protected ArmorStand standMain;
    protected ArmorStand standSkin;
    protected ArmorStand standMainSeat;
    @Nullable
    protected ArmorStand standRotors;
    protected boolean isFalling = false;
    protected boolean extremeFalling = false;
    protected boolean headlightsEnabled = false;

    public void vehicleMovement(Player player, Object packet) {
        if (!PacketHandler.isObjectPacket(packet)) {
            return;
        }
        this.packet = packet;
        this.player = player;
        AtomicLong lastUsed = new AtomicLong(0L);
        if (player.getVehicle() == null) {
            return;
        }
        Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof ArmorStand)) {
            return;
        }
        if (vehicle.getCustomName() == null) {
            return;
        }
        if (vehicle.getCustomName().replace("MTVEHICLES_MAINSEAT_", "").isEmpty()) {
            return;
        }
        this.license = vehicle.getCustomName().replace("MTVEHICLES_MAINSEAT_", "");
        if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + this.license) == null) {
            return;
        }
        if (VehicleData.speed.get(this.license) == null) {
            VehicleData.speed.put(this.license, 0.0);
            return;
        }
        this.vehicleType = VehicleData.type.get(this.license);
        if (this.vehicleType == null) {
            return;
        }
        if (VehicleData.fuel.get(this.license) < 1.0) {
            BossBarUtils.setBossBarValue(0.0, this.license);
            if (this.vehicleType.canFly()) {
                this.isFalling = true;
                this.extremeFalling = this.vehicleType.isHelicopter() && (Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.EXTREME_HELICOPTER_FALL) != false;
            } else {
                return;
            }
        }
        BossBarUtils.setBossBarValue(VehicleData.fuel.get(this.license) / 100.0, this.license);
        this.standMain = VehicleData.autostand.get("MTVEHICLES_MAIN_" + this.license);
        this.standSkin = VehicleData.autostand.get("MTVEHICLES_SKIN_" + this.license);
        this.standMainSeat = VehicleData.autostand.get("MTVEHICLES_MAINSEAT_" + this.license);
        this.standRotors = VehicleData.autostand.get("MTVEHICLES_WIEKENS_" + this.license);
        if (ConfigModule.vehicleDataConfig.getHealth(this.license) == 0.0) {
            this.standMain.getWorld().spawnParticle(Particle.SMOKE_NORMAL, this.standMain.getLocation(), 2);
            if (!VehicleData.isVehicleDestroyed(this.license)) {
                if (((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.EXPLODING_VEHICLE)).booleanValue()) {
                    Bukkit.getScheduler().runTask((Plugin)Main.instance, () -> {
                        this.standMain.getLocation().add(0.0, 0.0, 0.0).createExplosion(2.0f);
                        VehicleData.markVehicleAsDestroyed(this.license);
                    });
                }
                if (((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.DESTRUCTIBLE_VEHICLE)).booleanValue()) {
                    Bukkit.getScheduler().runTask((Plugin)Main.instance, () -> {
                        String l = this.license;
                        VehicleUtils.despawnVehicle(l);
                        VehicleUtils.getVehicle(l).delete();
                    });
                }
            }
            return;
        }
        if (((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.HEADLIGHTS_ENABLED)).booleanValue() && VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_17)) {
            this.headlightsEnabled = true;
        }
        Main.schedulerRun(() -> {
            this.standSkin.teleport(new Location(this.standMain.getLocation().getWorld(), this.standMain.getLocation().getX(), this.standMain.getLocation().getY(), this.standMain.getLocation().getZ(), this.standMain.getLocation().getYaw(), this.standMain.getLocation().getPitch()));
            if (DependencyModule.isDependencyEnabled(SoftDependency.WORLD_GUARD)) {
                Set<String> newRegions = DependencyModule.worldGuard.getRegionNames(this.standMain.getLocation());
                if (VehicleData.lastRegions.containsKey(this.license)) {
                    MTVEvent event;
                    Set<String> lastRegions = VehicleData.lastRegions.get(this.license);
                    if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.RIDE, this.vehicleType, this.standMain.getLocation(), player)) {
                        player.getVehicle().eject();
                        VehicleData.speed.put(this.license, 0.0);
                        ConfigModule.messagesConfig.sendMessage((CommandSender)player, Message.CANNOT_DO_THAT_HERE);
                        return;
                    }
                    for (String leftRegion : Sets.difference(lastRegions, newRegions)) {
                        event = new VehicleRegionLeaveEvent(this.license, leftRegion);
                        event.setPlayer(player);
                        event.call();
                        if (!event.isCancelled()) continue;
                        player.getVehicle().eject();
                        VehicleData.speed.put(this.license, 0.0);
                        Bukkit.getScheduler().runTaskLater((Plugin)Main.instance, () -> {
                            this.standMain.teleport(new Location(player.getWorld(), player.getLocation().getX(), this.standMain.getLocation().getY(), player.getLocation().getZ()));
                            this.standSkin.teleport(new Location(player.getWorld(), player.getLocation().getX(), this.standSkin.getLocation().getY(), player.getLocation().getZ()));
                        }, 5L);
                        return;
                    }
                    for (String enteredRegion : Sets.difference(newRegions, lastRegions)) {
                        event = new VehicleRegionEnterEvent(this.license, enteredRegion);
                        event.setPlayer(player);
                        event.call();
                        if (!event.isCancelled()) continue;
                        player.getVehicle().eject();
                        VehicleData.speed.put(this.license, 0.0);
                        Bukkit.getScheduler().runTaskLater((Plugin)Main.instance, () -> {
                            this.standMain.teleport(new Location(player.getWorld(), player.getLocation().getX(), this.standMain.getLocation().getY(), player.getLocation().getZ()));
                            this.standSkin.teleport(new Location(player.getWorld(), player.getLocation().getX(), this.standSkin.getLocation().getY(), player.getLocation().getZ()));
                        }, 5L);
                        return;
                    }
                }
                VehicleData.lastRegions.put(this.license, newRegions);
            }
            this.updateStand();
            if (!this.vehicleType.isHelicopter()) {
                this.blockCheck();
            }
            this.mainSeat();
            if (VehicleData.seatsize.get(this.license + "addon") != null) {
                for (int i = 1; i <= VehicleData.seatsize.get(this.license + "addon"); ++i) {
                    ArmorStand standAddon = VehicleData.autostand.get("MTVEHICLES_ADDON" + i + "_" + this.license);
                    standAddon.teleport(this.standMain.getLocation());
                }
            }
            if (this.vehicleType.isHelicopter()) {
                this.rotors();
            }
            if (ConfigModule.vehicleDataConfig.isHornEnabled(this.license) && this.steerIsJumping() && !this.isFalling) {
                if (VehicleData.lastUsage.containsKey(player.getName())) {
                    lastUsed.set(VehicleData.lastUsage.get(player.getName()));
                }
                if (System.currentTimeMillis() - lastUsed.get() >= Long.parseLong(ConfigModule.defaultConfig.get(DefaultConfig.Option.HORN_COOLDOWN).toString()) * 1000L) {
                    HornUseEvent api = new HornUseEvent(this.license);
                    api.setPlayer(player);
                    Main.schedulerRun(api::call);
                    if (!api.isCancelled()) {
                        this.standMain.getWorld().playSound(this.standMain.getLocation(), Objects.requireNonNull(ConfigModule.defaultConfig.get(DefaultConfig.Option.HORN_TYPE).toString()), 0.9f, 1.0f);
                        VehicleData.lastUsage.put(player.getName(), System.currentTimeMillis());
                    }
                }
            }
            if (this.vehicleType.isTank() && this.steerIsJumping()) {
                if (VehicleData.lastUsage.containsKey(player.getName())) {
                    lastUsed.set(VehicleData.lastUsage.get(player.getName()));
                }
                if (System.currentTimeMillis() - lastUsed.get() >= Long.parseLong(ConfigModule.defaultConfig.get(DefaultConfig.Option.TANK_COOLDOWN).toString()) * 1000L) {
                    this.standMain.getWorld().playEffect(this.standMain.getLocation(), Effect.BLAZE_SHOOT, 1, 1);
                    this.standMain.getWorld().playEffect(this.standMain.getLocation(), Effect.GHAST_SHOOT, 1, 1);
                    this.standMain.getWorld().playEffect(this.standMain.getLocation(), Effect.WITHER_BREAK_BLOCK, 1, 1);
                    double xOffset = 4.0;
                    double yOffset = 1.6;
                    double zOffset = 0.0;
                    Location locvp = this.standMain.getLocation().clone();
                    Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
                    float zvp = (float)(fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
                    float xvp = (float)(fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
                    Location loc = new Location(this.standMain.getWorld(), (double)xvp, this.standMain.getLocation().getY() + yOffset, (double)zvp, fbvp.getYaw(), fbvp.getPitch());
                    this.spawnParticles(this.standMain, loc);
                    this.tankShoot(this.standMain, loc);
                    VehicleData.lastUsage.put(player.getName(), System.currentTimeMillis());
                }
            }
            if (this.headlightsEnabled && vehicle.getWorld().getTime() >= 13000L && this.vehicleType.isCar() && !vehicle.isEmpty()) {
                this.addAndRemoveLight();
            }
            this.rotation();
            this.move();
        });
    }

    protected void rotation() {
        int rotationSpeed = VehicleData.getRotationSpeed(this.license);
        Location locBelow = new Location(this.standMain.getLocation().getWorld(), this.standMain.getLocation().getX(), this.standMain.getLocation().getY() - 0.2, this.standMain.getLocation().getZ(), this.standMain.getLocation().getYaw(), this.standMain.getLocation().getPitch());
        Material blockTypeBelow = locBelow.getBlock().getType();
        if (this.isFalling && this.vehicleType.isHelicopter()) {
            return;
        }
        if (this.vehicleType.isHelicopter() && !blockTypeBelow.equals((Object)Material.AIR)) {
            return;
        }
        if (ConfigModule.defaultConfig.isIceSlippery() && blockTypeBelow.toString().contains("ICE")) {
            rotationSpeed *= 2;
        }
        if (ConfigModule.defaultConfig.usePlayerFacingDriving()) {
            this.rotateVehicle(this.player.getLocation().getYaw());
        } else {
            int rotation;
            int n = rotation = VehicleData.speed.get(this.license) < 0.1 ? rotationSpeed / 3 : rotationSpeed;
            if (this.steerGetXxa() > 0.0f) {
                this.rotateVehicle(this.standMain.getLocation().getYaw() - (float)rotation);
            } else if (this.steerGetXxa() < 0.0f) {
                this.rotateVehicle(this.standMain.getLocation().getYaw() + (float)rotation);
            }
        }
    }

    protected void rotateVehicle(float yaw) {
        Main.schedulerRun(() -> {
            this.standMain.setRotation(yaw, this.standMain.getLocation().getPitch());
            this.standMainSeat.setRotation(yaw, this.standMain.getLocation().getPitch());
            this.standSkin.setRotation(yaw, this.standMain.getLocation().getPitch());
        });
    }

    protected void move() {
        double maxSpeed = VehicleData.getSpeed(VehicleData.DataSpeed.MAXSPEED, this.license);
        double accelerationSpeed = VehicleData.getSpeed(VehicleData.DataSpeed.ACCELERATION, this.license);
        double brakingSpeed = VehicleData.getSpeed(VehicleData.DataSpeed.BRAKING, this.license);
        double maxSpeedBackwards = VehicleData.getSpeed(VehicleData.DataSpeed.MAXSPEEDBACKWARDS, this.license);
        Location locBelow = new Location(this.standMain.getLocation().getWorld(), this.standMain.getLocation().getX(), this.standMain.getLocation().getY() - 0.2, this.standMain.getLocation().getZ(), this.standMain.getLocation().getYaw(), this.standMain.getLocation().getPitch());
        if ((double)this.steerGetZza() == 0.0 && !locBelow.getBlock().getType().equals((Object)Material.AIR)) {
            this.putFrictionSpeed();
        }
        if ((double)this.steerGetZza() > 0.0) {
            VehicleData.frictionBlocked.remove(this.license);
            if (VehicleData.speed.get(this.license) < 0.0) {
                VehicleData.speed.put(this.license, VehicleData.speed.get(this.license) + brakingSpeed);
                return;
            }
            this.putFuelUsage();
            if (VehicleData.speed.get(this.license) > maxSpeed - accelerationSpeed) {
                return;
            }
            VehicleData.speed.put(this.license, VehicleData.speed.get(this.license) + accelerationSpeed);
        }
        if ((double)this.steerGetZza() < 0.0) {
            VehicleData.frictionBlocked.remove(this.license);
            if (VehicleData.speed.get(this.license) > 0.0) {
                VehicleData.speed.put(this.license, VehicleData.speed.get(this.license) - brakingSpeed);
                return;
            }
            this.putFuelUsage();
            if (VehicleData.speed.get(this.license) < -maxSpeedBackwards) {
                return;
            }
            VehicleData.speed.put(this.license, VehicleData.speed.get(this.license) - accelerationSpeed);
        }
    }

    protected void putFrictionSpeed() {
        BigDecimal round;
        double frictionSpeed = VehicleData.getSpeed(VehicleData.DataSpeed.FRICTION, this.license);
        String blockBelowName = new Location(this.standMain.getLocation().getWorld(), this.standMain.getLocation().getX(), this.standMain.getLocation().getY() - 0.2, this.standMain.getLocation().getZ(), this.standMain.getLocation().getYaw(), this.standMain.getLocation().getPitch()).getBlock().getType().toString();
        if (ConfigModule.defaultConfig.isIceSlippery() && blockBelowName.contains("ICE")) {
            frictionSpeed *= 0.5;
        }
        if (Double.parseDouble(String.valueOf(round = BigDecimal.valueOf(VehicleData.speed.get(this.license)).setScale(1, 1))) == 0.0) {
            VehicleData.speed.put(this.license, 0.0);
            return;
        }
        if (Double.parseDouble(String.valueOf(round)) > 0.01) {
            VehicleData.speed.put(this.license, VehicleData.speed.get(this.license) - frictionSpeed);
            return;
        }
        if (Double.parseDouble(String.valueOf(round)) < 0.01) {
            VehicleData.speed.put(this.license, VehicleData.speed.get(this.license) + frictionSpeed);
        }
    }

    @ToDo(value="Trapdoors")
    protected boolean blockCheck() {
        Location loc = this.getLocationOfBlockAhead();
        String locY = String.valueOf(this.standMain.getLocation().getY());
        Location locBlockAbove = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1.0, loc.getZ(), loc.getYaw(), loc.getPitch());
        Location locBlockBelow = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1.0, loc.getZ(), loc.getYaw(), loc.getPitch());
        String drivingOnY = locY.substring(locY.length() - 2);
        boolean isOnGround = drivingOnY.contains(".0");
        boolean isOnSlab = drivingOnY.contains(".5");
        boolean isPassable = this.isPassable(loc.getBlock());
        boolean isAbovePassable = this.isPassable(locBlockAbove.getBlock());
        double difference = Double.parseDouble("0." + locY.split("\\.")[1]);
        BlockData blockData = loc.getBlock().getBlockData();
        BlockData blockDataBelow = locBlockBelow.getBlock().getBlockData();
        if (this.standMain.getLocation().getBlock().getType().toString().contains("PATH") || this.standMain.getLocation().getBlock().getType().toString().contains("FARMLAND")) {
            if (!isAbovePassable) {
                VehicleData.speed.put(this.license, 0.0);
                return false;
            }
            if (!loc.getBlock().getType().toString().contains("PATH") && !loc.getBlock().getType().toString().contains("FARMLAND")) {
                this.pushVehicleUp(0.0625);
                return true;
            }
            return false;
        }
        if (loc.getBlock().getType().toString().contains("CARPET")) {
            if (!((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.DRIVE_ON_CARPETS)).booleanValue()) {
                VehicleData.speed.put(this.license, 0.0);
                return false;
            }
            if (!isAbovePassable) {
                VehicleData.speed.put(this.license, 0.0);
                return false;
            }
            if (isOnGround) {
                this.pushVehicleUp(0.0625);
            }
            return true;
        }
        if (blockData instanceof Snow) {
            int layers = ((Snow)blockData).getLayers();
            double layerHeight = this.getLayerHeight(layers);
            if (VehicleData.speed.get(this.license) > 0.1) {
                VehicleData.speed.put(this.license, 0.1);
            }
            if (layerHeight == difference) {
                return false;
            }
            double snowDifference = layerHeight - difference;
            this.pushVehicleUp(snowDifference);
            return true;
        }
        if (blockData instanceof Fence || loc.getBlock().getType().toString().contains("WALL") || blockData instanceof TrapDoor) {
            VehicleData.speed.put(this.license, 0.0);
            return false;
        }
        if (ConfigModule.defaultConfig.isHoneySlowdownEnabled() && locBlockBelow.getBlock().getType() == Material.HONEY_BLOCK) {
            if (VehicleData.speed.get(this.license) > 0.05) {
                VehicleData.speed.put(this.license, Math.max(VehicleData.speed.get(this.license) * 0.2, 0.05));
            }
            return false;
        }
        if (ConfigModule.defaultConfig.isIceSlippery() && locBlockBelow.getBlock().getType().toString().contains("ICE") && VehicleData.speed.get(this.license) > 0.05) {
            VehicleData.speed.put(this.license, Math.max(VehicleData.speed.get(this.license) * 1.1, VehicleData.getSpeed(VehicleData.DataSpeed.MAXSPEED, this.license) * 1.2));
        }
        if (ConfigModule.defaultConfig.driveUpSlabs().isSlabs()) {
            if (isOnSlab) {
                Slab slab;
                if (isPassable) {
                    this.pushVehicleDown(0.5);
                    return false;
                }
                if (blockData instanceof Slab && (slab = (Slab)blockData).getType().equals((Object)Slab.Type.BOTTOM)) {
                    return false;
                }
                if (!isAbovePassable) {
                    VehicleData.speed.put(this.license, 0.0);
                    return false;
                }
                this.pushVehicleUp(0.5);
                return true;
            }
            if (!isPassable) {
                Slab slab;
                if (blockData instanceof Slab && (slab = (Slab)blockData).getType().equals((Object)Slab.Type.BOTTOM)) {
                    if (!isAbovePassable) {
                        VehicleData.speed.put(this.license, 0.0);
                        return false;
                    }
                    if (isOnGround) {
                        this.pushVehicleUp(0.5);
                    } else if (0.5 - difference > 0.0) {
                        this.pushVehicleUp(0.5 - difference);
                    }
                }
                VehicleData.speed.put(this.license, 0.0);
                return false;
            }
        } else {
            if (ConfigModule.defaultConfig.driveUpSlabs().isBlocks()) {
                Slab slab;
                if (!isOnSlab && !isPassable) {
                    Slab slab2;
                    if (blockData instanceof Slab && (slab2 = (Slab)blockData).getType().equals((Object)Slab.Type.BOTTOM)) {
                        VehicleData.speed.put(this.license, 0.0);
                        return false;
                    }
                    if (!isAbovePassable) {
                        VehicleData.speed.put(this.license, 0.0);
                        return false;
                    }
                    if (isOnGround) {
                        this.pushVehicleUp(1.0);
                    } else if (1.0 - difference > 0.0) {
                        this.pushVehicleUp(1.0 - difference);
                    }
                    return true;
                }
                if (isPassable) {
                    this.pushVehicleDown(0.5);
                    return false;
                }
                if (blockData instanceof Slab && (slab = (Slab)blockData).getType().equals((Object)Slab.Type.BOTTOM)) {
                    return false;
                }
                if (!isAbovePassable) {
                    VehicleData.speed.put(this.license, 0.0);
                    return false;
                }
                this.pushVehicleUp(0.5);
                return true;
            }
            if (ConfigModule.defaultConfig.driveUpSlabs().isBoth()) {
                Slab slab;
                if (isOnSlab) {
                    Slab slab3;
                    if (isPassable) {
                        this.pushVehicleDown(0.5);
                        return false;
                    }
                    if (blockData instanceof Slab && (slab3 = (Slab)blockData).getType().equals((Object)Slab.Type.BOTTOM)) {
                        return false;
                    }
                    if (!isAbovePassable) {
                        VehicleData.speed.put(this.license, 0.0);
                        return false;
                    }
                    this.pushVehicleUp(0.5);
                    return true;
                }
                if (!isPassable) {
                    Slab slab4;
                    if (!isAbovePassable) {
                        VehicleData.speed.put(this.license, 0.0);
                        return false;
                    }
                    if (blockData instanceof Slab && (slab4 = (Slab)blockData).getType().equals((Object)Slab.Type.BOTTOM)) {
                        if (isOnGround) {
                            this.pushVehicleUp(0.5);
                        } else if (0.5 - difference > 0.0) {
                            this.pushVehicleUp(0.5 - difference);
                        }
                        return true;
                    }
                    if (isOnGround) {
                        this.pushVehicleUp(1.0);
                    } else if (1.0 - difference > 0.0) {
                        this.pushVehicleUp(1.0 - difference);
                    }
                    return true;
                }
                if (blockDataBelow instanceof Slab && (slab = (Slab)blockDataBelow).getType().equals((Object)Slab.Type.BOTTOM)) {
                    this.pushVehicleDown(0.5);
                    return false;
                }
            }
        }
        return false;
    }

    protected double getLayerHeight(int layers) {
        switch (layers) {
            case 1: {
                return 0.125;
            }
            case 2: {
                return 0.25;
            }
            case 3: {
                return 0.375;
            }
            case 4: {
                return 0.5;
            }
            case 5: {
                return 0.625;
            }
            case 6: {
                return 0.75;
            }
            case 7: {
                return 0.875;
            }
        }
        return 1.0;
    }

    protected void mainSeat() {
        if (VehicleData.seatsize.get(this.license) != null) {
            for (int i = 2; i <= VehicleData.seatsize.get(this.license); ++i) {
                ArmorStand seatas = VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + this.license);
                double xOffset = VehicleData.seatx.get("MTVEHICLES_SEAT" + i + "_" + this.license);
                double yOffset = VehicleData.seaty.get("MTVEHICLES_SEAT" + i + "_" + this.license);
                double zOffset = VehicleData.seatz.get("MTVEHICLES_SEAT" + i + "_" + this.license);
                Location locvp = this.standMain.getLocation().clone();
                Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
                float zvp = (float)(fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
                float xvp = (float)(fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
                Location loc = new Location(this.standMain.getWorld(), (double)xvp, this.standMain.getLocation().getY() + yOffset, (double)zvp, fbvp.getYaw(), fbvp.getPitch());
                this.teleportSeat(seatas, loc);
            }
        }
        double xOffset = VehicleData.mainx.get("MTVEHICLES_MAINSEAT_" + this.license);
        double yOffset = VehicleData.mainy.get("MTVEHICLES_MAINSEAT_" + this.license);
        double zOffset = VehicleData.mainz.get("MTVEHICLES_MAINSEAT_" + this.license);
        Location locvp = this.standMain.getLocation().clone();
        Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
        float zvp = (float)(fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
        float xvp = (float)(fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
        Location loc = new Location(this.standMain.getWorld(), (double)xvp, this.standMain.getLocation().getY() + yOffset, (double)zvp, fbvp.getYaw(), fbvp.getPitch());
        this.teleportSeat(this.standMainSeat, loc);
    }

    @VersionSpecific
    protected void teleportSeat(ArmorStand seat, Location loc) {
        if (VersionModule.getServerVersion().is1_12()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_13()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_15()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_16()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_17()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_18_R1()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_18_R2()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_19()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_19_R2()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_19_R2.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_19_R3()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_20_R1()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_20_R2()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_20_R3()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_20_R4()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_21_R1()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_21_R2()) {
            this.teleportSeat(((CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_21_R3()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_21_R3.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_21_R4()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_21_R5()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_21_R5.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } else if (VersionModule.getServerVersion().is1_21_R6()) {
            this.teleportSeat(((org.bukkit.craftbukkit.v1_21_R6.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        }
    }

    @VersionSpecific
    protected static String getTeleportMethod() {
        if (VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_18_R1)) {
            return "a";
        }
        return "setLocation";
    }

    protected void teleportSeat(Object seat, double x, double y, double z, float yaw, float pitch) {
        Main.schedulerRun(() -> {
            try {
                Method method = seat.getClass().getSuperclass().getSuperclass().getDeclaredMethod(VehicleMovement.getTeleportMethod(), Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
                method.invoke(seat, x, y, z, Float.valueOf(yaw), Float.valueOf(pitch));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected void updateStand() {
        boolean space;
        Location loc = this.standMain.getLocation();
        Location locBlockAhead = this.getLocationOfBlockAhead();
        Location locBlockAheadAndBelow = new Location(locBlockAhead.getWorld(), locBlockAhead.getX(), locBlockAhead.getY() - 1.0, locBlockAhead.getZ(), locBlockAhead.getPitch(), locBlockAhead.getYaw());
        Location locBelow = new Location(loc.getWorld(), loc.getX(), loc.getY() - 0.2, loc.getZ(), loc.getYaw(), loc.getPitch());
        Material block = locBelow.getBlock().getType();
        String blockName = block.toString();
        boolean bl = space = !this.isFalling && this.steerIsJumping();
        if (this.vehicleType.canFly()) {
            if (this.vehicleType.isAirplane() && this.isFalling && !block.equals((Object)Material.AIR)) {
                this.putFrictionSpeed();
                this.standMain.setVelocity(new Vector(loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getX(), 0.0, loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getZ()));
                return;
            }
            if (this.vehicleType.isHelicopter() && !this.isPassable(locBelow.getBlock()) || this.vehicleType.isAirplane() && VehicleData.fuel.get(this.license) < 1.0 && !block.equals((Object)Material.AIR)) {
                VehicleData.speed.put(this.license, 0.0);
            }
            if (space) {
                double takeOffSpeed;
                double d = takeOffSpeed = (Double)ConfigModule.defaultConfig.get(DefaultConfig.Option.TAKE_OFF_SPEED) > 0.0 ? (Double)ConfigModule.defaultConfig.get(DefaultConfig.Option.TAKE_OFF_SPEED) : 0.4;
                if (this.vehicleType.isAirplane() && VehicleData.speed.get(this.license) < takeOffSpeed) {
                    double y = this.isPassable(locBelow.getBlock()) ? -0.2 : 0.0;
                    this.standMain.setVelocity(new Vector(loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getX(), y, loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getZ()));
                    return;
                }
                this.putFuelUsage();
                if (loc.getY() > (double)((Integer)ConfigModule.defaultConfig.get(DefaultConfig.Option.MAX_FLYING_HEIGHT)).intValue()) {
                    return;
                }
                this.standMain.setVelocity(new Vector(loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getX(), 0.2, loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getZ()));
                return;
            }
            if (this.extremeFalling) {
                if (this.standMain.isOnGround() && VehicleData.fallDamage.get(this.license) == null) {
                    double damageAmount = (Double)ConfigModule.defaultConfig.get(DefaultConfig.Option.HELICOPTER_FALL_DAMAGE) <= 0.0 ? ((Double)DefaultConfig.Option.HELICOPTER_FALL_DAMAGE.getDefaultValue()).doubleValue() : ((Double)ConfigModule.defaultConfig.get(DefaultConfig.Option.HELICOPTER_FALL_DAMAGE)).doubleValue();
                    Main.schedulerRun(() -> {
                        this.player.damage(damageAmount);
                        if (VehicleData.seatsize.get(this.license) != null) {
                            for (int i = 2; i <= VehicleData.seatsize.get(this.license); ++i) {
                                ArmorStand seat = VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + this.license);
                                List passengers = seat.getPassengers();
                                for (Entity p : passengers) {
                                    if (!(p instanceof LivingEntity)) continue;
                                    ((LivingEntity)p).damage(damageAmount);
                                }
                            }
                        }
                    });
                    VehicleData.fallDamage.put(this.license, true);
                }
                this.standMain.setGravity(true);
                return;
            }
            this.putFuelUsage();
            this.standMain.setVelocity(new Vector(loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getX(), -0.2, loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getZ()));
            return;
        }
        if (this.vehicleType.isHover()) {
            if (block.equals((Object)Material.AIR)) {
                this.standMain.setVelocity(new Vector(loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getX(), -0.8, loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getZ()));
                return;
            }
            this.standMain.setVelocity(new Vector(loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getX(), 1.0E-5, loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getZ()));
            return;
        }
        if (this.vehicleType.isBoat()) {
            if (!this.boatPassable(blockName) && !this.isPassable(locBelow.getBlock())) {
                VehicleData.speed.put(this.license, 0.0);
            }
            if (this.isPassable(locBelow.getBlock()) && !this.boatPassable(blockName)) {
                this.standMain.setVelocity(new Vector(loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getX(), -0.8, loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getZ()));
                return;
            }
            this.standMain.setVelocity(new Vector(loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getX(), 0.01, loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getZ()));
            return;
        }
        if (blockName.contains("WATER")) {
            this.standMain.setVelocity(new Vector(loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getX(), -0.8, loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getZ()));
            return;
        }
        if (this.isPassable(locBlockAhead.getBlock()) && this.isPassable(locBlockAheadAndBelow.getBlock())) {
            if (this.isPassable(locBelow.getBlock())) {
                this.standMain.setVelocity(new Vector(loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getX(), -0.8, loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getZ()));
                return;
            }
            if (blockName.contains("CARPET")) {
                this.standMain.setVelocity(new Vector(loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getX(), -0.7375, loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getZ()));
                return;
            }
        }
        this.standMain.setVelocity(new Vector(loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getX(), 0.0, loc.getDirection().multiply(VehicleData.speed.get(this.license).doubleValue()).getZ()));
    }

    protected void putFuelUsage() {
        double newFuel;
        if (!((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED)).booleanValue() || !((Boolean)ConfigModule.vehicleDataConfig.get(this.license, VehicleDataConfig.Option.FUEL_ENABLED)).booleanValue()) {
            return;
        }
        double fuelMultiplier = Double.parseDouble(ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_MULTIPLIER).toString());
        if (fuelMultiplier < 0.1 || fuelMultiplier > 10.0) {
            fuelMultiplier = 1.0;
        }
        if ((newFuel = VehicleData.fuel.get(this.license) - fuelMultiplier * VehicleData.fuelUsage.get(this.license)) < 0.0) {
            VehicleData.fuel.put(this.license, 0.0);
        } else {
            VehicleData.fuel.put(this.license, newFuel);
        }
    }

    protected boolean isPassable(Block block) {
        return block.isPassable();
    }

    protected void rotors() {
        float yawAdd;
        double xOffset = VehicleData.wiekenx.get("MTVEHICLES_WIEKENS_" + this.license);
        double yOffset = VehicleData.wiekeny.get("MTVEHICLES_WIEKENS_" + this.license);
        double zOffset = VehicleData.wiekenz.get("MTVEHICLES_WIEKENS_" + this.license);
        Location locvp = this.standMain.getLocation().clone();
        Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
        float zvp = (float)(fbvp.getZ() + zOffset * Math.sin(Math.toRadians(this.standRotors.getLocation().getYaw())));
        float xvp = (float)(fbvp.getX() + zOffset * Math.cos(Math.toRadians(this.standRotors.getLocation().getYaw())));
        float f = yawAdd = this.isFalling ? 5.0f : 15.0f;
        if (this.extremeFalling) {
            yawAdd = 0.0f;
        }
        Location loc = new Location(this.standMain.getWorld(), (double)xvp, this.standMain.getLocation().getY() + yOffset, (double)zvp, this.standRotors.getLocation().getYaw() + yawAdd, this.standRotors.getLocation().getPitch());
        Main.schedulerRun(() -> this.standRotors.teleport(loc));
    }

    protected void pushVehicleUp(double plus) {
        Location newLoc = new Location(this.standMain.getLocation().getWorld(), this.standMain.getLocation().getX(), this.standMain.getLocation().getY() + plus, this.standMain.getLocation().getZ(), this.standMain.getLocation().getYaw(), this.standMain.getLocation().getPitch());
        Main.schedulerRun(() -> this.standMain.teleport(newLoc));
    }

    protected void pushVehicleDown(double minus) {
        this.pushVehicleUp(-minus);
    }

    protected Location getLocationOfBlockAhead() {
        double xOffset = 0.7;
        double yOffset = 0.4;
        double zOffset = 0.0;
        Location locvp = this.standMain.getLocation().clone();
        Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
        float zvp = (float)(fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
        float xvp = (float)(fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
        return new Location(this.standMain.getWorld(), (double)xvp, this.standMain.getLocation().getY() + yOffset, (double)zvp, fbvp.getYaw(), fbvp.getPitch());
    }

    protected boolean steerIsJumping() {
        boolean isJumping = false;
        try {
            if (VersionModule.getServerVersion().isOlderOrEqualTo(ServerVersion.v1_21_R1)) {
                String declaredMethod = "d";
                if (VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_20_R2) && VersionModule.getServerVersion().isOlderThan(ServerVersion.v1_20_R4)) {
                    declaredMethod = "e";
                } else if (VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_20_R4)) {
                    declaredMethod = "f";
                }
                Method method = this.packet.getClass().getDeclaredMethod(declaredMethod, new Class[0]);
                isJumping = (Boolean)method.invoke(this.packet, new Object[0]);
            } else {
                Object input = this.packet.getClass().getDeclaredMethod("b", new Class[0]).invoke(this.packet, new Object[0]);
                isJumping = (Boolean)input.getClass().getDeclaredMethod("e", new Class[0]).invoke(input, new Object[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isJumping;
    }

    protected float steerGetXxa() {
        float Xxa = 0.0f;
        try {
            if (VersionModule.getServerVersion().isOlderOrEqualTo(ServerVersion.v1_21_R1)) {
                String declaredMethod = "b";
                if (VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_19_R3) && VersionModule.getServerVersion().isOlderThan(ServerVersion.v1_20_R4)) {
                    declaredMethod = "a";
                }
                Method method = this.packet.getClass().getDeclaredMethod(declaredMethod, new Class[0]);
                Xxa = ((Float)method.invoke(this.packet, new Object[0])).floatValue();
            } else {
                Object input = this.packet.getClass().getDeclaredMethod("b", new Class[0]).invoke(this.packet, new Object[0]);
                if (((Boolean)input.getClass().getDeclaredMethod("c", new Class[0]).invoke(input, new Object[0])).booleanValue()) {
                    Xxa = 1.0f;
                } else if (((Boolean)input.getClass().getDeclaredMethod("d", new Class[0]).invoke(input, new Object[0])).booleanValue()) {
                    Xxa = -1.0f;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Xxa;
    }

    protected float steerGetZza() {
        float Zza = 0.0f;
        try {
            if (VersionModule.getServerVersion().isOlderOrEqualTo(ServerVersion.v1_21_R1)) {
                String declaredMethod = "c";
                if (VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_20_R2) && VersionModule.getServerVersion().isOlderThan(ServerVersion.v1_20_R4)) {
                    declaredMethod = "d";
                } else if (VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_20_R4)) {
                    declaredMethod = "e";
                }
                Method method = this.packet.getClass().getDeclaredMethod(declaredMethod, new Class[0]);
                Zza = ((Float)method.invoke(this.packet, new Object[0])).floatValue();
            } else {
                Object input = this.packet.getClass().getDeclaredMethod("b", new Class[0]).invoke(this.packet, new Object[0]);
                if (((Boolean)input.getClass().getDeclaredMethod("a", new Class[0]).invoke(input, new Object[0])).booleanValue()) {
                    Zza = 1.0f;
                } else if (((Boolean)input.getClass().getDeclaredMethod("b", new Class[0]).invoke(input, new Object[0])).booleanValue()) {
                    Zza = -1.0f;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Zza;
    }

    @VersionSpecific
    protected void spawnParticles(ArmorStand stand, Location loc) {
        stand.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 2);
        stand.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 2);
        stand.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 5);
        if (!VersionModule.getServerVersion().isOlderOrEqualTo(ServerVersion.v1_13)) {
            stand.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 5);
        }
    }

    public void tankShoot(ArmorStand stand, Location loc) {
        if (!((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.TANK_TNT)).booleanValue()) {
            return;
        }
        TankShootEvent api = new TankShootEvent(this.license);
        api.setPlayer(this.player);
        Main.schedulerRun(api::call);
        if (api.isCancelled()) {
            return;
        }
        Main.schedulerRun(() -> {
            TNTPrimed tnt = (TNTPrimed)loc.getWorld().spawn(loc, TNTPrimed.class);
            tnt.setFuseTicks(20);
            tnt.setVelocity(stand.getLocation().getDirection().multiply(3.0));
        });
    }

    public boolean boatPassable(String blockName) {
        return blockName.contains("WATER") || blockName.contains("SEAGRASS") || blockName.contains("KELP") || blockName.contains("CORAL") || blockName.contains("PICKLE");
    }

    private void addAndRemoveLight() {
        double xOffset = 0.7;
        double yOffset = 0.4;
        double zOffset = 0.0;
        Location locvp = this.standMain.getLocation().clone();
        Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset).multiply(2));
        float zvp = (float)(fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
        float xvp = (float)(fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
        Location loc = new Location(this.standMain.getWorld(), (double)xvp, this.standMain.getLocation().getY() + yOffset, (double)zvp, fbvp.getYaw(), fbvp.getPitch());
        if (loc.getBlock().getType().equals((Object)Material.AIR)) {
            loc.getBlock().setType(Material.LIGHT);
            Bukkit.getScheduler().runTaskLater((Plugin)Main.instance, () -> loc.getBlock().setType(Material.AIR), 10L);
        }
    }
}

