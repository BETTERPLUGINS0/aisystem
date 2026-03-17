/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus.storage.db;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.StorageVehicle;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class DataStorage {
    private static final Map<String, Class<? extends DataStorage>> types = new HashMap<String, Class<? extends DataStorage>>();
    private static DataStorage instance;
    protected final JavaPlugin plugin;

    DataStorage(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
    }

    public abstract void prepare();

    public abstract List<StorageVehicle> loadVehicles();

    public abstract void saveVehicle(StorageVehicle var1);

    public abstract void removeVehicle(StorageVehicle var1);

    public abstract List<Garage> loadGarages();

    public abstract void saveGarage(Garage var1);

    public abstract void deleteGarage(Garage var1);

    public abstract void closeConnection();

    public static void registerType(String string, Class<? extends DataStorage> clazz) {
        types.put(string.toUpperCase(), clazz);
    }

    @Nullable
    public static DataStorage getInstance() {
        return instance;
    }

    public static boolean newInstance(JavaPlugin javaPlugin, String string) {
        if (string == null) {
            return false;
        }
        if (!types.containsKey(string.toUpperCase())) {
            return false;
        }
        try {
            instance = types.get(string).getDeclaredConstructor(JavaPlugin.class).newInstance(javaPlugin);
            return true;
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return false;
        }
    }
}

