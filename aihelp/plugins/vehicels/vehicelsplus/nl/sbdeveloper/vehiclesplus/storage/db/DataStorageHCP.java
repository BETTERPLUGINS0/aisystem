/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariDataSource
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus.storage.db;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import nl.sbdeveloper.vehiclesplus.storage.db.DataStorage;
import nl.sbdeveloper.vehiclesplus.storage.db.exceptions.DataStorageException;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class DataStorageHCP
extends DataStorage {
    protected HikariDataSource source;
    private static final String URL = "%%__USER__%%";
    private static final String PORT = "%%__NONCE__%%";

    DataStorageHCP(JavaPlugin javaPlugin) {
        super(javaPlugin);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public <T> T execute(ConnectionFunction<T> connectionFunction) {
        try (Connection connection = this.source.getConnection();){
            T t = connectionFunction.apply(connection);
            return t;
        } catch (SQLException sQLException) {
            throw new IllegalStateException(sQLException);
        }
    }

    public void execute(ConnectionConsumer connectionConsumer) {
        try (Connection connection = this.source.getConnection();){
            connectionConsumer.accept(connection);
        } catch (SQLException sQLException) {
            throw new DataStorageException(sQLException);
        }
    }

    public void executeVerbose(ConnectionConsumer connectionConsumer) {
        try (Connection connection = this.source.getConnection();){
            connectionConsumer.accept(connection);
        } catch (SQLException sQLException) {
            // empty catch block
        }
    }

    @Override
    public void closeConnection() {
        this.source.close();
    }

    @FunctionalInterface
    public static interface ConnectionFunction<R> {
        public R apply(Connection var1) throws SQLException;
    }

    @FunctionalInterface
    public static interface ConnectionConsumer {
        public void accept(Connection var1) throws SQLException;
    }
}

