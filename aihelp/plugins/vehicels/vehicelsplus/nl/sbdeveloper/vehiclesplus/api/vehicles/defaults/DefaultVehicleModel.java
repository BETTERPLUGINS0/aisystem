/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.defaults;

import java.lang.reflect.InvocationTargetException;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Sounds;
import nl.sbdeveloper.vehiclesplus.utils.MainUtil;

public abstract class DefaultVehicleModel {
    protected final Sounds defaultSounds = Sounds.builder().idle(new Sounds.Sound("vp.idle", 6)).start(new Sounds.Sound("vp.start", 2)).accelerate(new Sounds.Sound("vp.accelerate", 2)).driving(new Sounds.Sound("vp.driving", 2)).slowingDown(new Sounds.Sound("vp.slowingdown", 2)).build();

    public abstract VehicleModel build();

    public static <T extends DefaultVehicleModel> T constructBuilder(Class<T> clazz) {
        try {
            return (T)((DefaultVehicleModel)clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            throw new RuntimeException("Could not construct a default vehicle model!", reflectiveOperationException);
        }
    }

    public static <T extends DefaultVehicleModel> T constructBuiltInBuilder(String string) {
        try {
            Class<?> clazz = Class.forName("nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.impl.Default" + MainUtil.capitalize(string));
            return (T)((DefaultVehicleModel)clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            throw new RuntimeException("Could not construct a default vehicle model!", reflectiveOperationException);
        }
    }
}

