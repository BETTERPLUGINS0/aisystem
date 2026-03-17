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
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.StorageVehicle;
import nl.sbdeveloper.vehiclesplus.storage.db.DataStorageHCP;
import nl.sbdeveloper.vehiclesplus.utils.jackson.JacksonHelper;
import org.bukkit.plugin.java.JavaPlugin;

public class MySQLDB
extends DataStorageHCP {
    private final String dbName = VehiclesPlus.getStorage().getConfig().getDataSettings().getDatabase();

    public MySQLDB(JavaPlugin javaPlugin) {
        super(javaPlugin);
        javaPlugin.getLogger().info("Connecting to the MySQL database '" + this.dbName + "'...");
        String string = VehiclesPlus.getStorage().getConfig().getDataSettings().getHost();
        int n = VehiclesPlus.getStorage().getConfig().getDataSettings().getPort();
        String string2 = VehiclesPlus.getStorage().getConfig().getDataSettings().getUsername();
        String string3 = VehiclesPlus.getStorage().getConfig().getDataSettings().getPassword();
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + string + ":" + n + "/" + this.dbName);
        hikariConfig.setUsername(string2);
        hikariConfig.setPassword(string3);
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.addDataSourceProperty("cachePrepStmts", (Object)"true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", (Object)"250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", (Object)"2048");
        this.source = new HikariDataSource(hikariConfig);
    }

    @Override
    public void prepare() {
        this.execute((Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS vehicles (vehicleUUID varchar(36) NOT NULL, data JSON NOT NULL, UNIQUE (vehicleUUID))");
            preparedStatement.execute();
            PreparedStatement preparedStatement2 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS garages (name varchar(36) NOT NULL, data JSON NOT NULL, UNIQUE (name))");
            preparedStatement2.execute();
            return null;
        });
    }

    @Override
    public List<StorageVehicle> loadVehicles() {
        return this.execute((Connection connection) -> {
            ArrayList<StorageVehicle> arrayList = new ArrayList<StorageVehicle>();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM vehicles");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String string = resultSet.getString("data");
                try {
                    arrayList.add(JacksonHelper.fromJSON(StorageVehicle.class, string, false));
                } catch (IOException iOException) {
                    VehiclesPlus.getInstance().getLogger().log(Level.SEVERE, "Couldn't load a vehicle", iOException);
                }
            }
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
        this.execute((Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM vehicles WHERE vehicleUUID = ?");
            preparedStatement.setString(1, storageVehicle.getUuid().toString());
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
        this.plugin.getLogger().info("Closing the connection to the MySQL database '" + this.dbName + "'...");
        super.closeConnection();
    }
}

