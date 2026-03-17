/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.statics;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.MovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.strategies.AirMovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.strategies.LandMovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.strategies.WaterMovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.MovementType;

public class StrategyFactory {
    private static final Map<MovementType, Class<? extends MovementStrategy>> strategyMap = new HashMap<MovementType, Class<? extends MovementStrategy>>();

    public static <T extends MovementStrategy> Class<T> getStrategyClass(MovementType movementType) {
        return strategyMap.get((Object)movementType);
    }

    public static <T extends MovementStrategy> T createStrategy(VehicleModel vehicleModel, MovementType movementType) {
        Class<T> clazz = StrategyFactory.getStrategyClass(movementType);
        if (clazz != null) {
            try {
                Constructor<? extends MovementStrategy> constructor = StrategyFactory.findConstructor(clazz, new Object[]{vehicleModel});
                if (constructor != null) {
                    return (T)constructor.newInstance(vehicleModel);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        throw new IllegalArgumentException("No strategy found for MovementType: " + String.valueOf((Object)movementType));
    }

    private static Constructor<? extends MovementStrategy> findConstructor(Class<? extends MovementStrategy> clazz, Object[] objectArray) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() != objectArray.length) continue;
            boolean bl = true;
            Class<?>[] classArray = constructor.getParameterTypes();
            for (int i = 0; i < objectArray.length; ++i) {
                if (classArray[i].isInstance(objectArray[i])) continue;
                bl = false;
                break;
            }
            if (!bl) continue;
            return constructor;
        }
        return null;
    }

    static {
        strategyMap.put(MovementType.LAND, LandMovementStrategy.class);
        strategyMap.put(MovementType.AIR, AirMovementStrategy.class);
        strategyMap.put(MovementType.WATER, WaterMovementStrategy.class);
    }
}

