/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.block.data.Rail
 *  org.bukkit.entity.Player
 *  org.bukkit.material.PoweredRail
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.vehicles;

import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.util.BlockCollisionUtil;
import me.zombie_striker.qav.util.HeadPoseUtil;
import me.zombie_striker.qav.util.xseries.XBlock;
import me.zombie_striker.qav.util.xseries.XMaterial;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.Player;
import org.bukkit.material.PoweredRail;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class AbstractTrain
extends AbstractVehicle {
    public AbstractTrain(String string, int n) {
        super(string, n);
    }

    @Override
    public void handleTurnLeft(VehicleEntity vehicleEntity, Player player) {
    }

    @Override
    public void handleTurnRight(VehicleEntity vehicleEntity, Player player) {
    }

    @Override
    public void handleSpeedIncrease(VehicleEntity vehicleEntity, Player player) {
        if (!this.handleFuel(vehicleEntity, player)) {
            return;
        }
        vehicleEntity.setSpeed(Math.min(vehicleEntity.getSpeed() + 0.1, vehicleEntity.getType().getMaxSpeed()));
    }

    @Override
    public void handleSpeedDecrease(VehicleEntity vehicleEntity, Player player) {
        if (!this.handleFuel(vehicleEntity, player)) {
            return;
        }
        vehicleEntity.setSpeed(Math.max(vehicleEntity.getSpeed() - 0.1, -vehicleEntity.getType().getMaxBackupSpeed()));
    }

    @Override
    public void handleSpace(VehicleEntity vehicleEntity, Player player) {
        if (vehicleEntity.getSpeed() > 0.0) {
            vehicleEntity.setSpeed(Math.max(vehicleEntity.getSpeed() - 0.1, -vehicleEntity.getType().getMaxBackupSpeed()));
        } else {
            vehicleEntity.setSpeed(Math.max(vehicleEntity.getSpeed() + 0.1, -vehicleEntity.getType().getMaxSpeed()));
        }
    }

    @Override
    public void tick(VehicleEntity vehicleEntity) {
        int n;
        Location location = vehicleEntity.getDriverSeat().getLocation();
        Block block = location.getBlock();
        int n2 = this.getDirectionFromRail(vehicleEntity, block);
        if (n2 != (n = AbstractTrain.getDirectionInternalID(vehicleEntity))) {
            float f = (float)AbstractTrain.getAngleFromDirection(n2);
            vehicleEntity.setAngle(f);
            HeadPoseUtil.setHeadPoseUsingReflection(vehicleEntity);
        }
        Vector vector = vehicleEntity.getDirection().clone();
        vector.normalize().multiply(vehicleEntity.getSpeed());
        if (!BlockCollisionUtil.isSolid(location)) {
            vector.setY(Math.max(-1.0, vehicleEntity.getDriverSeat().getVelocity().getY() - 0.05));
        }
        if (!this.isOnRail(vehicleEntity)) {
            return;
        }
        if (BlockCollisionUtil.getMaterial(location).equals((Object)XMaterial.POWERED_RAIL.parseMaterial())) {
            if (XMaterial.supports(13)) {
                if (!XBlock.isPowered(block)) {
                    vector = new Vector(0, 0, 0);
                }
            } else {
                PoweredRail poweredRail = new PoweredRail(BlockCollisionUtil.getMaterial(location), block.getData());
                if (!poweredRail.isPowered()) {
                    vector = new Vector(0, 0, 0);
                }
            }
        }
        vehicleEntity.getDriverSeat().setVelocity(vector);
        this.handleOtherStands(vehicleEntity, vector);
    }

    private int getDirectionFromRail(VehicleEntity vehicleEntity, Block block) {
        int n = -1;
        int n2 = AbstractTrain.getDirectionInternalID(vehicleEntity);
        try {
            if (block.getBlockData() instanceof Rail) {
                Rail rail = (Rail)block.getBlockData();
                n = AbstractTrain.getDirectionID(rail.getShape().name());
            }
        } catch (Throwable throwable) {
            n = block.getData();
        }
        switch (n) {
            case 2: 
            case 3: {
                if (n2 != 1 && n2 != 3) break;
                return 1;
            }
            case 4: 
            case 5: {
                if (n2 != 0 && n2 != 2) break;
                return n2;
            }
            case 1: {
                if (vehicleEntity.getAngleRotation() >= 1.5707963267948966 && vehicleEntity.getAngleRotation() < 4.71238898038469) {
                    return 1;
                }
                return 3;
            }
            case 0: {
                if (vehicleEntity.getAngleRotation() < 1.5707963267948966 || vehicleEntity.getAngleRotation() > 4.71238898038469) {
                    return 0;
                }
                return 2;
            }
            case 9: {
                if (n2 == 0) {
                    return 3;
                }
                if (n2 == 1) {
                    return 2;
                }
                return n2;
            }
            case 7: {
                if (n2 == 2) {
                    return 1;
                }
                if (n2 == 3) {
                    return 0;
                }
                return n2;
            }
            case 8: {
                if (n2 == 0) {
                    return 1;
                }
                if (n2 == 3) {
                    return 2;
                }
                return n2;
            }
            case 6: {
                if (n2 == 2) {
                    return 3;
                }
                if (n2 == 1) {
                    return 2;
                }
                return n2;
            }
        }
        return AbstractTrain.getDirectionInternalID(vehicleEntity);
    }

    public static int getDirectionID(String string) {
        switch (string.toUpperCase()) {
            case "ASCENDING_WEST": 
            case "ASCENDING_EAST": {
                return 2;
            }
            case "ASCENDING_NORTH": {
                return 4;
            }
            case "ASCENDING_SOUTH": {
                return 5;
            }
            case "EAST_WEST": {
                return 1;
            }
            case "NORTH_SOUTH": {
                return 0;
            }
            case "NORTH_EAST": {
                return 9;
            }
            case "SOUTH_WEST": {
                return 7;
            }
            case "NORTH_WEST": {
                return 8;
            }
            case "SOUTH_EAST": {
                return 6;
            }
        }
        return -1;
    }

    public static int getDirectionInternalID(VehicleEntity vehicleEntity) {
        if (vehicleEntity.getAngleRotation() <= 0.7853981633974483) {
            return 0;
        }
        if (vehicleEntity.getAngleRotation() <= 2.356194490192345) {
            return 1;
        }
        if (vehicleEntity.getAngleRotation() <= 3.9269908169872414) {
            return 2;
        }
        if (vehicleEntity.getAngleRotation() <= 5.497787143782138) {
            return 3;
        }
        return 0;
    }

    public static double getAngleFromDirection(int n) {
        if (n == 0) {
            return 0.0;
        }
        if (n == 1) {
            return 1.5707963267948966;
        }
        if (n == 2) {
            return Math.PI;
        }
        if (n == 3) {
            return 4.71238898038469;
        }
        return 0.0;
    }

    public boolean isOnRail(@NotNull VehicleEntity vehicleEntity) {
        return this.isRail(vehicleEntity.getDriverSeat().getLocation());
    }

    public boolean isRail(@NotNull Location location) {
        XMaterial xMaterial = XMaterial.matchXMaterial(BlockCollisionUtil.getMaterial(location));
        return xMaterial.equals(XMaterial.RAIL) || xMaterial.equals(XMaterial.ACTIVATOR_RAIL) || xMaterial.equals(XMaterial.POWERED_RAIL) || xMaterial.equals(XMaterial.DETECTOR_RAIL);
    }
}

