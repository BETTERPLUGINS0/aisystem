/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Player
 */
package nl.mtvehicles.core.infrastructure.vehicle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@ToDo(value="make nicer, move, refactor - for better API usage")
public class VehicleData {
    public static HashMap<String, Double> speed = new HashMap();
    @Deprecated
    public static HashMap<String, Double> speedhigh = new HashMap();
    public static HashMap<String, Integer> maxheight = new HashMap();
    public static HashMap<String, Double> mainx = new HashMap();
    public static HashMap<String, Double> mainy = new HashMap();
    public static HashMap<String, Double> mainz = new HashMap();
    public static HashMap<String, Integer> seatsize = new HashMap();
    public static HashMap<String, Double> seatx = new HashMap();
    public static HashMap<String, Double> seaty = new HashMap();
    public static HashMap<String, Double> seatz = new HashMap();
    public static HashMap<String, Double> wiekenx = new HashMap();
    public static HashMap<String, Double> wiekeny = new HashMap();
    public static HashMap<String, Double> wiekenz = new HashMap();
    public static HashMap<String, VehicleType> type = new HashMap();
    public static HashMap<String, Double> fuel = new HashMap();
    public static HashMap<String, Double> fuelUsage = new HashMap();
    public static HashMap<String, ArmorStand> autostand = new HashMap();
    public static HashMap<String, ArmorStand> autostand2 = new HashMap();
    public static Map<String, Long> lastUsage = new HashMap<String, Long>();
    public static HashMap<String, Boolean> fallDamage = new HashMap();
    private static HashMap<String, Integer> RotationSpeed = new HashMap();
    private static HashMap<String, Double> MaxSpeed = new HashMap();
    private static HashMap<String, Double> AccelerationSpeed = new HashMap();
    private static HashMap<String, Double> BrakingSpeed = new HashMap();
    private static HashMap<String, Double> MaxSpeedBackwards = new HashMap();
    private static HashMap<String, Double> FrictionSpeed = new HashMap();
    public static Set<String> frictionBlocked = new HashSet<String>();
    public static Set<String> brakingBlocked = new HashSet<String>();
    public static HashMap<String, Set<String>> lastRegions = new HashMap();
    public static HashMap<String, Boolean> destroyedVehicles = new HashMap();
    private static HashMap<String, Set<Player>> trunkViewers = new HashMap();

    private VehicleData() {
    }

    public static boolean isTrunkViewer(String licensePlate, Player player) {
        VehicleData.setTrunkViewers(licensePlate);
        return trunkViewers.get(licensePlate).contains(player);
    }

    public static Double getSpeed(@NotNull DataSpeed speedType, @NotNull String licensePlate) {
        if (speedType == DataSpeed.MAXSPEED) {
            return MaxSpeed.get(licensePlate);
        }
        if (speedType == DataSpeed.ACCELERATION) {
            return AccelerationSpeed.get(licensePlate);
        }
        if (speedType == DataSpeed.BRAKING) {
            if (brakingBlocked.contains(licensePlate)) {
                return 0.0;
            }
            return BrakingSpeed.get(licensePlate);
        }
        if (speedType == DataSpeed.MAXSPEEDBACKWARDS) {
            return MaxSpeedBackwards.get(licensePlate);
        }
        if (speedType == DataSpeed.FRICTION) {
            if (frictionBlocked.contains(licensePlate)) {
                return 0.0;
            }
            return FrictionSpeed.get(licensePlate);
        }
        return null;
    }

    public static Integer getRotationSpeed(String licensePlate) {
        return RotationSpeed.get(licensePlate);
    }

    public static void setSpeed(@NotNull DataSpeed speedType, @NotNull String licensePlate, @NotNull Double value) {
        switch (speedType.ordinal()) {
            case 0: {
                MaxSpeed.put(licensePlate, value);
                break;
            }
            case 1: {
                AccelerationSpeed.put(licensePlate, value);
                break;
            }
            case 2: {
                BrakingSpeed.put(licensePlate, value);
                break;
            }
            case 3: {
                MaxSpeedBackwards.put(licensePlate, value);
                break;
            }
            case 4: {
                FrictionSpeed.put(licensePlate, value);
            }
        }
    }

    public static void setRotationSpeed(String licensePlate, Integer value) {
        RotationSpeed.put(licensePlate, value);
    }

    public static boolean trunkViewerAdd(String licensePlate, Player player) {
        VehicleData.setTrunkViewers(licensePlate);
        Set<Player> viewers = trunkViewers.get(licensePlate);
        boolean returns = viewers.add(player);
        trunkViewers.put(licensePlate, viewers);
        return returns;
    }

    public static boolean trunkViewerRemove(String licensePlate, Player player) {
        VehicleData.setTrunkViewers(licensePlate);
        Set<Player> viewers = trunkViewers.get(licensePlate);
        boolean returns = viewers.remove(player);
        trunkViewers.put(licensePlate, viewers);
        return returns;
    }

    public static Set<Player> getTrunkViewers(String licensePlate) {
        VehicleData.setTrunkViewers(licensePlate);
        return trunkViewers.get(licensePlate);
    }

    private static void setTrunkViewers(String licensePlate) {
        if (!trunkViewers.containsKey(licensePlate)) {
            trunkViewers.put(licensePlate, new HashSet());
        }
    }

    public static void markVehicleAsDestroyed(String licensePlate) {
        destroyedVehicles.put(licensePlate, true);
    }

    public static void markVehicleAsRepaired(String licensePlate) {
        destroyedVehicles.remove(licensePlate);
    }

    public static boolean isVehicleDestroyed(String licensePlate) {
        return destroyedVehicles.getOrDefault(licensePlate, false);
    }

    public static enum DataSpeed {
        MAXSPEED,
        ACCELERATION,
        BRAKING,
        MAXSPEEDBACKWARDS,
        FRICTION;

    }
}

