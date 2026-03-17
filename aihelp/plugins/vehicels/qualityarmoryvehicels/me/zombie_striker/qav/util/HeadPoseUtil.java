/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.util.EulerAngle
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.hooks.model.ModelEngineHook;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

public class HeadPoseUtil {
    private static final Method GET_HANDLE;
    private static final Field YAW;
    static HashMap<VehicleEntity, Float> chaningPos;

    public static void setHeadPoseUsingReflection(VehicleEntity vehicleEntity) {
        HeadPoseUtil.setHeadPoseUsingReflection(vehicleEntity, vehicleEntity.getModelEntity());
    }

    public static void setHeadPoseUsingReflection(VehicleEntity vehicleEntity, ArmorStand armorStand) {
        if (!vehicleEntity.getType().enableBodyFix() || vehicleEntity.getDriverSeat() == vehicleEntity.getModelEntity()) {
            // empty if block
        }
        HeadPoseUtil.updateArmorstandPart(vehicleEntity, armorStand, armorStand.getHeadPose().getX(), vehicleEntity.getAngleRotation(), armorStand.getHeadPose().getZ());
        if (ModelEngineHook.isInitialized()) {
            HeadPoseUtil.setYaw(vehicleEntity, (float)Math.toDegrees(vehicleEntity.getAngleRotation()));
        }
    }

    public static void updateArmorstandPart(VehicleEntity vehicleEntity, ArmorStand armorStand, double d, double d2, double d3) {
        armorStand.setHeadPose(new EulerAngle(d, d2, d3));
    }

    public static void setYaw(@NotNull VehicleEntity vehicleEntity, float f) {
        Main.DEBUG("Setting yaw to " + f);
        if (XReflection.supports(13)) {
            vehicleEntity.getDriverSeat().setRotation(f, vehicleEntity.getDriverSeat().getLocation().getPitch());
        } else {
            EulerAngle eulerAngle = ((ArmorStand)vehicleEntity.getDriverSeat()).getHeadPose();
            try {
                if (GET_HANDLE != null && YAW != null) {
                    YAW.set(GET_HANDLE.invoke(vehicleEntity.getDriverSeat(), new Object[0]), Float.valueOf(f));
                    ((ArmorStand)vehicleEntity.getDriverSeat()).setHeadPose(eulerAngle);
                }
            } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                Main.DEBUG("Unable to set yaw with nms: " + reflectiveOperationException.getMessage());
            }
        }
    }

    static {
        Method method = null;
        Field field = null;
        try {
            Class<?> clazz = XReflection.getCraftClass("entity.CraftEntity");
            Class<?> clazz2 = XReflection.getNMSClass("world.entity", "Entity");
            method = clazz.getMethod("getHandle", new Class[0]);
            field = clazz2.getField("yaw");
        } catch (NoSuchFieldException | NoSuchMethodException reflectiveOperationException) {
            // empty catch block
        }
        GET_HANDLE = method;
        YAW = field;
        chaningPos = new HashMap();
    }
}

