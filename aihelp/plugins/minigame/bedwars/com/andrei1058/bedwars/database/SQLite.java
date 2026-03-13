/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.database;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.database.Database;
import com.andrei1058.bedwars.shop.quickbuy.QuickBuyElement;
import com.andrei1058.bedwars.stats.PlayerStats;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SQLite
implements Database {
    private String url;
    private Connection connection;

    public SQLite() {
        File dataFolder;
        File folder = new File(String.valueOf(BedWars.plugin.getDataFolder()) + "/Cache");
        if (!folder.exists() && !folder.mkdir()) {
            BedWars.plugin.getLogger().severe("Could not create /Cache folder!");
        }
        if (!(dataFolder = new File(folder.getPath() + "/shop.db")).exists()) {
            try {
                if (!dataFolder.createNewFile()) {
                    BedWars.plugin.getLogger().severe("Could not create /Cache/shop.db file!");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        this.url = "jdbc:sqlite:" + String.valueOf(dataFolder);
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.getConnection(this.url);
        } catch (ClassNotFoundException | SQLException e) {
            if (e instanceof ClassNotFoundException) {
                BedWars.plugin.getLogger().severe("Could Not Found SQLite Driver on your system!");
            }
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        try {
            this.checkConnection();
            String sql = "CREATE TABLE IF NOT EXISTS global_stats (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(200), uuid VARCHAR(36), first_play TIMESTAMP NULL DEFAULT NULL, last_play TIMESTAMP DEFAULT NULL, wins INTEGER(10), kills INTEGER(10), final_kills INTEGER(10), looses INTEGER(10), deaths INTEGER(10), final_deaths INTEGER(10), beds_destroyed INTEGER(10), games_played INTEGER(10));";
            try (Statement statement = this.connection.createStatement();){
                statement.executeUpdate(sql);
            }
            try (Statement st = this.connection.createStatement();){
                sql = "CREATE TABLE IF NOT EXISTS quick_buy_2 (uuid VARCHAR(36) PRIMARY KEY, slot_19 VARCHAR(200), slot_20 VARCHAR(200), slot_21 VARCHAR(200), slot_22 VARCHAR(200), slot_23 VARCHAR(200), slot_24 VARCHAR(200), slot_25 VARCHAR(200),slot_28 VARCHAR(200), slot_29 VARCHAR(200), slot_30 VARCHAR(200), slot_31 VARCHAR(200), slot_32 VARCHAR(200), slot_33 VARCHAR(200), slot_34 VARCHAR(200),slot_37 VARCHAR(200), slot_38 VARCHAR(200), slot_39 VARCHAR(200), slot_40 VARCHAR(200), slot_41 VARCHAR(200), slot_42 VARCHAR(200), slot_43 VARCHAR(200));";
                st.executeUpdate(sql);
            }
            st = this.connection.createStatement();
            try {
                sql = "CREATE TABLE IF NOT EXISTS player_levels (id INTEGER PRIMARY KEY AUTOINCREMENT, uuid VARCHAR(200), level INTEGER, xp INTEGER, name VARCHAR(200), next_cost INTEGER);";
                st.executeUpdate(sql);
            } finally {
                if (st != null) {
                    st.close();
                }
            }
            st = this.connection.createStatement();
            try {
                sql = "CREATE TABLE IF NOT EXISTS  player_language (id INTEGER PRIMARY KEY AUTOINCREMENT, uuid VARCHAR(200), iso VARCHAR(200));";
                st.executeUpdate(sql);
            } finally {
                if (st != null) {
                    st.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean hasStats(UUID uuid) {
        String sql = "SELECT uuid FROM global_stats WHERE uuid = ?;";
        try {
            this.checkConnection();
            try (PreparedStatement statement = this.connection.prepareStatement(sql);){
                boolean bl;
                block14: {
                    statement.setString(1, uuid.toString());
                    ResultSet result = statement.executeQuery();
                    try {
                        bl = result.next();
                        if (result == null) break block14;
                    } catch (Throwable throwable) {
                        if (result != null) {
                            try {
                                result.close();
                            } catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        }
                        throw throwable;
                    }
                    result.close();
                }
                return bl;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void saveStats(PlayerStats stats) {
        block15: {
            try {
                this.checkConnection();
                if (this.hasStats(stats.getUuid())) {
                    String sql = "UPDATE global_stats SET last_play=?, wins=?, kills=?, final_kills=?, looses=?, deaths=?, final_deaths=?, beds_destroyed=?, games_played=?, name=? WHERE uuid = ?;";
                    try (PreparedStatement statement = this.connection.prepareStatement(sql);){
                        statement.setTimestamp(1, Timestamp.from(stats.getLastPlay()));
                        statement.setInt(2, stats.getWins());
                        statement.setInt(3, stats.getKills());
                        statement.setInt(4, stats.getFinalKills());
                        statement.setInt(5, stats.getLosses());
                        statement.setInt(6, stats.getDeaths());
                        statement.setInt(7, stats.getFinalDeaths());
                        statement.setInt(8, stats.getBedsDestroyed());
                        statement.setInt(9, stats.getGamesPlayed());
                        statement.setString(10, stats.getName());
                        statement.setString(11, stats.getUuid().toString());
                        statement.executeUpdate();
                        break block15;
                    }
                }
                String sql = "INSERT INTO global_stats (name, uuid, first_play, last_play, wins, kills, final_kills, looses, deaths, final_deaths, beds_destroyed, games_played) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                try (PreparedStatement statement = this.connection.prepareStatement(sql);){
                    statement.setString(1, stats.getName());
                    statement.setString(2, stats.getUuid().toString());
                    statement.setTimestamp(3, Timestamp.from(stats.getFirstPlay()));
                    statement.setTimestamp(4, Timestamp.from(stats.getLastPlay()));
                    statement.setInt(5, stats.getWins());
                    statement.setInt(6, stats.getKills());
                    statement.setInt(7, stats.getFinalKills());
                    statement.setInt(8, stats.getLosses());
                    statement.setInt(9, stats.getDeaths());
                    statement.setInt(10, stats.getFinalDeaths());
                    statement.setInt(11, stats.getBedsDestroyed());
                    statement.setInt(12, stats.getGamesPlayed());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public PlayerStats fetchStats(UUID uuid) {
        PlayerStats stats = new PlayerStats(uuid);
        String sql = "SELECT * FROM global_stats WHERE uuid = ?;";
        try {
            this.checkConnection();
            try (PreparedStatement statement = this.connection.prepareStatement(sql);){
                statement.setString(1, uuid.toString());
                try (ResultSet result = statement.executeQuery();){
                    if (result.next()) {
                        stats.setFirstPlay(result.getTimestamp("first_play").toInstant());
                        stats.setLastPlay(result.getTimestamp("last_play").toInstant());
                        stats.setWins(result.getInt("wins"));
                        stats.setKills(result.getInt("kills"));
                        stats.setFinalKills(result.getInt("final_kills"));
                        stats.setLosses(result.getInt("looses"));
                        stats.setDeaths(result.getInt("deaths"));
                        stats.setFinalDeaths(result.getInt("final_deaths"));
                        stats.setBedsDestroyed(result.getInt("beds_destroyed"));
                        stats.setGamesPlayed(result.getInt("games_played"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    @Override
    public void setQuickBuySlot(UUID p, String shopPath, int slot) {
        try {
            this.checkConnection();
            try (PreparedStatement statement = this.connection.prepareStatement("SELECT uuid FROM quick_buy_2 WHERE uuid = ?;");){
                statement.setString(1, p.toString());
                try (ResultSet rs = statement.executeQuery();){
                    PreparedStatement ps;
                    if (!rs.next()) {
                        ps = this.connection.prepareStatement("INSERT INTO quick_buy_2 (uuid, slot_19, slot_20, slot_21, slot_22, slot_23, slot_24, slot_25, slot_28, slot_29, slot_30, slot_31, slot_32, slot_33, slot_34, slot_37, slot_38, slot_39, slot_40, slot_41, slot_42, slot_43) VALUES(?,' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ');");
                        try {
                            ps.setString(1, p.toString());
                            ps.execute();
                        } finally {
                            if (ps != null) {
                                ps.close();
                            }
                        }
                    }
                    BedWars.debug("UPDATE SET SLOT " + slot + " identifier " + shopPath);
                    ps = this.connection.prepareStatement("UPDATE quick_buy_2 SET slot_" + slot + " = ? WHERE uuid = ?;");
                    try {
                        ps.setString(1, shopPath);
                        ps.setString(2, p.toString());
                        ps.executeUpdate();
                    } finally {
                        if (ps != null) {
                            ps.close();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getQuickBuySlots(UUID p, int slot) {
        String result = "";
        try {
            this.checkConnection();
            try (PreparedStatement ps = this.connection.prepareStatement("SELECT slot_" + slot + " FROM quick_buy_2 WHERE uuid = ?;");){
                ps.setString(1, p.toString());
                try (ResultSet rs = ps.executeQuery();){
                    if (rs.next()) {
                        result = rs.getString("slot_" + slot);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean hasQuickBuy(UUID uuid) {
        try {
            this.checkConnection();
            try (Statement statement = this.connection.createStatement();
                 ResultSet rs = statement.executeQuery("SELECT uuid FROM quick_buy_2 WHERE uuid = '" + uuid.toString() + "';");){
                if (!rs.next()) return false;
                rs.close();
                boolean bl = true;
                return bl;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int getColumn(UUID player, String column) {
        String sql = "SELECT ? FROM global_stats WHERE uuid = ?;";
        try {
            this.checkConnection();
            try (PreparedStatement statement = this.connection.prepareStatement(sql);){
                statement.setString(1, column);
                statement.setString(2, player.toString());
                try (ResultSet result = statement.executeQuery();){
                    if (!result.next()) return 0;
                    int n = result.getInt(column);
                    return n;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object[] getLevelData(UUID player) {
        Object[] r = new Object[]{1, 0, "", 0};
        try {
            this.checkConnection();
            try (PreparedStatement ps = this.connection.prepareStatement("SELECT level, xp, name, next_cost FROM player_levels WHERE uuid = ?;");){
                ps.setString(1, player.toString());
                try (ResultSet rs = ps.executeQuery();){
                    if (rs.next()) {
                        r[0] = rs.getInt("level");
                        r[1] = rs.getInt("xp");
                        r[2] = rs.getString("name");
                        r[3] = rs.getInt("next_cost");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public void setLevelData(UUID player, int level, int xp, String displayName, int nextCost) {
        block28: {
            try {
                this.checkConnection();
                try (PreparedStatement pss = this.connection.prepareStatement("SELECT uuid from player_levels WHERE uuid = ?;");){
                    pss.setString(1, player.toString());
                    try (ResultSet rs = pss.executeQuery();){
                        if (!rs.next()) {
                            try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO player_levels (uuid, level, xp, name, next_cost) VALUES (?, ?, ?, ?, ?);");){
                                ps.setString(1, player.toString());
                                ps.setInt(2, level);
                                ps.setInt(3, xp);
                                ps.setString(4, displayName);
                                ps.setInt(5, nextCost);
                                ps.executeUpdate();
                                break block28;
                            }
                        }
                        try (PreparedStatement ps = displayName == null ? this.connection.prepareStatement("UPDATE player_levels SET level=?, xp=? WHERE uuid = '" + player.toString() + "';") : this.connection.prepareStatement("UPDATE player_levels SET level=?, xp=?, name=?, next_cost=? WHERE uuid = '" + player.toString() + "';");){
                            ps.setInt(1, level);
                            ps.setInt(2, xp);
                            if (displayName != null) {
                                ps.setString(3, displayName);
                                ps.setInt(4, nextCost);
                            }
                            ps.executeUpdate();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setLanguage(UUID player, String iso) {
        block27: {
            try {
                this.checkConnection();
                try (Statement statement = this.connection.createStatement();
                     ResultSet rs = statement.executeQuery("SELECT iso FROM player_language WHERE uuid = '" + player.toString() + "';");){
                    if (rs.next()) {
                        try (Statement st = this.connection.createStatement();){
                            st.executeUpdate("UPDATE player_language SET iso='" + iso + "' WHERE uuid = '" + player.toString() + "';");
                            break block27;
                        }
                    }
                    try (PreparedStatement st = this.connection.prepareStatement("INSERT INTO player_language (uuid, iso) VALUES (?, ?);");){
                        st.setString(1, player.toString());
                        st.setString(2, iso);
                        st.execute();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getLanguage(UUID player) {
        String iso = Language.getDefaultLanguage().getIso();
        try {
            this.checkConnection();
            try (PreparedStatement ps = this.connection.prepareStatement("SELECT iso FROM player_language WHERE uuid = ?;");){
                ps.setString(1, player.toString());
                try (ResultSet rs = ps.executeQuery();){
                    if (rs.next()) {
                        iso = rs.getString("iso");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return iso;
    }

    @Override
    public void pushQuickBuyChanges(HashMap<Integer, String> updateSlots, UUID uuid, List<QuickBuyElement> elements) {
        if (updateSlots.isEmpty()) {
            return;
        }
        boolean hasQuick = this.hasQuickBuy(uuid);
        if (!hasQuick) {
            for (QuickBuyElement element : elements) {
                if (updateSlots.containsKey(element.getSlot())) continue;
                updateSlots.put(element.getSlot(), element.getCategoryContent().getIdentifier());
            }
        }
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        int i = 0;
        if (hasQuick) {
            for (Map.Entry<Integer, String> entry : updateSlots.entrySet()) {
                columns.append("slot_").append(entry.getKey()).append("=?");
                if (++i == updateSlots.size()) continue;
                columns.append(", ");
            }
        } else {
            for (Map.Entry<Integer, String> entry : updateSlots.entrySet()) {
                columns.append("slot_").append(entry.getKey());
                values.append("?");
                if (++i == updateSlots.size()) continue;
                columns.append(", ");
                values.append(", ");
            }
        }
        String sql = hasQuick ? "UPDATE quick_buy_2 SET " + String.valueOf(columns) + " WHERE uuid=?;" : "INSERT INTO quick_buy_2 (uuid," + String.valueOf(columns) + ") VALUES (?," + String.valueOf(values) + ");";
        try {
            this.checkConnection();
            try (PreparedStatement ps = this.connection.prepareStatement(sql);){
                int index = hasQuick ? 0 : 1;
                for (int key : updateSlots.keySet()) {
                    String identifier = updateSlots.get(key);
                    ps.setString(++index, identifier.trim().isEmpty() ? null : identifier);
                }
                ps.setString(hasQuick ? updateSlots.size() + 1 : 1, uuid.toString());
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<Integer, String> getQuickBuySlots(UUID uuid, int[] slot) {
        HashMap<Integer, String> results = new HashMap<Integer, String>();
        if (slot.length == 0) {
            return results;
        }
        try {
            this.checkConnection();
            try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM quick_buy_2 WHERE uuid = ?;");){
                ps.setString(1, uuid.toString());
                try (ResultSet rs = ps.executeQuery();){
                    if (rs.next()) {
                        for (int i : slot) {
                            String id = rs.getString("slot_" + i);
                            if (null == id || id.isEmpty()) continue;
                            results.put(i, id);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void checkConnection() throws SQLException {
        boolean renew = false;
        if (this.connection == null) {
            renew = true;
        } else if (this.connection.isClosed()) {
            renew = true;
        }
        if (renew) {
            this.connection = DriverManager.getConnection(this.url);
        }
    }
}

