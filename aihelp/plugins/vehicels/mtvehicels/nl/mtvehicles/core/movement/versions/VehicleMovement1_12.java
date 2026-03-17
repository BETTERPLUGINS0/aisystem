/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 */
package nl.mtvehicles.core.movement.versions;

import net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.movement.VehicleMovement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class VehicleMovement1_12
extends VehicleMovement {
    @Override
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
        byte data = loc.getBlock().getData();
        byte dataBelow = locBlockBelow.getBlock().getData();
        if (this.vehicleType.isBoat()) {
            if (!locBlockBelow.getBlock().getType().toString().contains("WATER")) {
                VehicleData.speed.put(this.license, 0.0);
                return false;
            }
            return false;
        }
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
        if (loc.getBlock().getType().toString().contains("SNOW") && !loc.getBlock().getType().toString().contains("SNOW_BLOCK")) {
            int layers = data + 1;
            double layerHeight = this.getLayerHeight(layers);
            if (VehicleData.speed.get(this.license) > 0.1) {
                VehicleData.speed.put(this.license, 0.1);
            }
            if (layerHeight == difference) {
                return false;
            }
            double snowDifference = layerHeight - difference;
            this.pushVehicleUp(snowDifference);
            return false;
        }
        if (loc.getBlock().getType().toString().contains("FENCE") || loc.getBlock().getType().toString().contains("WALL") || loc.getBlock().getType().toString().contains("TRAPDOOR") || loc.getBlock().getType().toString().contains("TRAP_DOOR")) {
            VehicleData.speed.put(this.license, 0.0);
            return false;
        }
        if (ConfigModule.defaultConfig.driveUpSlabs().isSlabs()) {
            if (isOnSlab) {
                if (isPassable) {
                    this.pushVehicleDown(0.5);
                    return false;
                }
                if ((loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) && !loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) {
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
                if ((loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) && !loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) {
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
                if (!isOnSlab && !isPassable) {
                    if ((loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) && !loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) {
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
                if ((loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) && !loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) {
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
                if (isOnSlab) {
                    if (isPassable) {
                        this.pushVehicleDown(0.5);
                        return false;
                    }
                    if ((loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) && !loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) {
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
                    if (!isAbovePassable) {
                        VehicleData.speed.put(this.license, 0.0);
                        return false;
                    }
                    if ((loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) && !loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) {
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
                if ((locBlockBelow.getBlock().getType().toString().contains("STEP") || locBlockBelow.getBlock().getType().toString().contains("SLAB")) && !locBlockBelow.getBlock().getType().toString().contains("DOUBLE") && dataBelow < 9) {
                    this.pushVehicleDown(0.5);
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean isPassable(Block block) {
        String blockName = block.getType().toString();
        return blockName.contains("AIR") || blockName.contains("FLOWER") || blockName.contains("ROSE") || blockName.contains("PLANT") || block.getType().equals((Object)Material.BROWN_MUSHROOM) || block.getType().equals((Object)Material.RED_MUSHROOM) || blockName.contains("LONG_GRASS") || blockName.contains("SAPLING") || blockName.contains("DEAD_BUSH") || blockName.contains("TORCH") || blockName.contains("BANNER");
    }

    @Override
    protected void rotateVehicle(float yaw) {
        Location loc = this.standMain.getLocation();
        this.standMain.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), yaw, loc.getPitch()));
        this.standMainSeat.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), yaw, loc.getPitch()));
        this.standSkin.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), yaw, loc.getPitch()));
    }

    @Override
    protected boolean steerIsJumping() {
        PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)this.packet;
        return ppisv.c();
    }

    @Override
    protected float steerGetXxa() {
        PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)this.packet;
        return ppisv.a();
    }

    @Override
    protected float steerGetZza() {
        PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)this.packet;
        return ppisv.b();
    }
}

