/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariConfig
 *  com.zaxxer.hikari.HikariDataSource
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus.storage.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.StorageVehicle;
import nl.sbdeveloper.vehiclesplus.storage.db.DataStorageHCP;
import nl.sbdeveloper.vehiclesplus.storage.db.exceptions.DataInvalidException;
import nl.sbdeveloper.vehiclesplus.utils.jackson.JacksonHelper;
import org.bukkit.plugin.java.JavaPlugin;

public class SQLiteDB
extends DataStorageHCP {
    private final String dbName;

    public SQLiteDB(JavaPlugin javaPlugin) {
        super(javaPlugin);
        String string;
        this.dbName = string = VehiclesPlus.getStorage().getConfig().getDataSettings().getDatabase();
        File file = new File(javaPlugin.getDataFolder(), string + ".db");
        javaPlugin.getLogger().info("Connecting to the SQLite database '" + string + "'...");
        if (!file.exists()) {
            try {
                javaPlugin.getLogger().info("Generating the '" + string + ".db' file...");
                if (!file.createNewFile()) {
                    javaPlugin.getLogger().severe("Couldn't generate the '" + string + ".db' file!");
                    return;
                }
            } catch (IOException iOException) {
                javaPlugin.getLogger().severe("Couldn't generate the '" + string + ".db' file!");
                return;
            }
        }
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName(javaPlugin.getName());
        hikariConfig.setUsername(null);
        hikariConfig.setPassword(null);
        hikariConfig.setDriverClassName("org.sqlite.JDBC");
        hikariConfig.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath());
        this.source = new HikariDataSource(hikariConfig);
    }

    @Override
    public void prepare() {
        this.execute((Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS vehicles (vehicleUUID varchar(36) NOT NULL, data TEXT NOT NULL, UNIQUE (vehicleUUID))");
            preparedStatement.execute();
            PreparedStatement preparedStatement2 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS garages (name varchar(36) NOT NULL, data TEXT NOT NULL, UNIQUE (name))");
            preparedStatement2.execute();
            return null;
        });
    }

    @Override
    public List<StorageVehicle> loadVehicles() {
        return this.execute((Connection connection) -> {
            ArrayList<StorageVehicle> arrayList = new ArrayList<StorageVehicle>();
            ArrayList<String> arrayList2 = new ArrayList<String>();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM vehicles");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String string = resultSet.getString("vehicleUUID");
                String string2 = resultSet.getString("data");
                try {
                    StorageVehicle storageVehicle = JacksonHelper.fromJSON(StorageVehicle.class, string2, false);
                    if (storageVehicle == null) {
                        throw new DataInvalidException("Loaded vehicle is null");
                    }
                    if (!VehiclesPlusAPI.getVehicleModels().containsKey(storageVehicle.getVehicleModel().getId())) {
                        throw new DataInvalidException("Loaded vehicle model is invalid");
                    }
                    arrayList.add(storageVehicle);
                } catch (IOException iOException) {
                    VehiclesPlus.getInstance().getLogger().log(Level.SEVERE, "Couldn't load the vehicle with UUID " + string + "! Ignoring for now...", iOException);
                } catch (IllegalArgumentException | DataInvalidException exception) {
                    VehiclesPlus.getInstance().getLogger().log(Level.WARNING, "Couldn't load the vehicle with UUID " + string + "! Removing it...");
                    arrayList2.add(string);
                }
            }
            resultSet.close();
            preparedStatement.close();
            arrayList2.forEach(this::removeVehicle);
            return arrayList;
        });
    }

    @Override
    public void saveVehicle(StorageVehicle storageVehicle) {
        String string;
        try {
            string = JacksonHelper.toJson(storageVehicle, false);
        } catch (JsonProcessingException jsonProcessingException) {
            VehiclesPlus.getInstance().getLogger().log(Level.SEVERE, "Couldn't save the vehicle " + String.valueOf(storageVehicle.getUuid()), jsonProcessingException);
            return;
        }
        this.executeVerbose(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO vehicles (vehicleUUID, data) VALUES (?, ?)");
            preparedStatement.setString(1, storageVehicle.getUuid().toString());
            preparedStatement.setString(2, string);
            preparedStatement.executeUpdate();
        });
        this.execute((Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE vehicles SET data = ? WHERE vehicleUUID = ?");
            preparedStatement.setString(1, string);
            preparedStatement.setString(2, storageVehicle.getUuid().toString());
            preparedStatement.executeUpdate();
            return null;
        });
    }

    @Override
    public void removeVehicle(StorageVehicle storageVehicle) {
        this.removeVehicle(storageVehicle.getUuid().toString());
    }

    private void removeVehicle(String string) {
        this.execute((Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM vehicles WHERE vehicleUUID = ?");
            preparedStatement.setString(1, string.toString());
            preparedStatement.executeUpdate();
            return null;
        });
    }

    @Override
    public List<Garage> loadGarages() {
        return this.execute((Connection connection) -> {
            ArrayList<Garage> arrayList = new ArrayList<Garage>();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM garages");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String string = resultSet.getString("data");
                try {
                    arrayList.add(JacksonHelper.fromJSON(Garage.class, string, false));
                } catch (IOException iOException) {
                    VehiclesPlus.getInstance().getLogger().log(Level.SEVERE, "Couldn't load a garage", iOException);
                }
            }
            return arrayList;
        });
    }

    @Override
    public void saveGarage(Garage garage) {
        String string;
        try {
            string = JacksonHelper.toJson(garage, false);
        } catch (JsonProcessingException jsonProcessingException) {
            VehiclesPlus.getInstance().getLogger().log(Level.SEVERE, "Couldn't save the garage " + garage.getName(), jsonProcessingException);
            return;
        }
        this.executeVerbose(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO garages (name, data) VALUES (?, ?)");
            preparedStatement.setString(1, garage.getName());
            preparedStatement.setString(2, string);
            preparedStatement.executeUpdate();
        });
        this.execute((Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE garages SET data = ? WHERE name = ?");
            preparedStatement.setString(1, string);
            preparedStatement.setString(2, garage.getName());
            preparedStatement.executeUpdate();
            return null;
        });
    }

    @Override
    public void deleteGarage(Garage garage) {
        this.execute((Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM garages WHERE name = ?");
            preparedStatement.executeUpdate();
            return null;
        });
    }

    @Override
    public void closeConnection() {
        this.plugin.getLogger().info("Closing the connection to the SQLite database '" + this.dbName + "'...");
        super.closeConnection();
    }
}

