/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.database;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.database.Database;
import com.andrei1058.bedwars.libs.hikari.HikariConfig;
import com.andrei1058.bedwars.libs.hikari.HikariDataSource;
import com.andrei1058.bedwars.shop.quickbuy.QuickBuyElement;
import com.andrei1058.bedwars.stats.PlayerStats;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MySQL
implements Database {
    private HikariDataSource dataSource;
    private final String host = BedWars.config.getYml().getString("database.host");
    private final String database = BedWars.config.getYml().getString("database.database");
    private final String user = BedWars.config.getYml().getString("database.user");
    private final String pass = BedWars.config.getYml().getString("database.pass");
    private final int port = BedWars.config.getYml().getInt("database.port");
    private final boolean ssl = BedWars.config.getYml().getBoolean("database.ssl");
    private final boolean certificateVerification = BedWars.config.getYml().getBoolean("database.verify-certificate", true);
    private final int poolSize = BedWars.config.getYml().getInt("database.pool-size", 10);
    private final int maxLifetime = BedWars.config.getYml().getInt("database.max-lifetime", 1800);

    public boolean connect() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName("BedWars1058MySQLPool");
        hikariConfig.setMaximumPoolSize(this.poolSize);
        hikariConfig.setMaxLifetime((long)this.maxLifetime * 1000L);
        hikariConfig.setJdbcUrl("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database);
        hikariConfig.setUsername(this.user);
        hikariConfig.setPassword(this.pass);
        hikariConfig.addDataSourceProperty("useSSL", String.valueOf(this.ssl));
        if (!this.certificateVerification) {
            hikariConfig.addDataSourceProperty("verifyServerCertificate", String.valueOf(false));
        }
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("encoding", "UTF-8");
        hikariConfig.addDataSourceProperty("useUnicode", "true");
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
        hikariConfig.addDataSourceProperty("jdbcCompliantTruncation", "false");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "275");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30L)));
        this.dataSource = new HikariDataSource(hikariConfig);
        try {
            this.dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
     * Exception decompiling
     */
    @Override
    public boolean hasStats(UUID uuid) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:538)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         *     at async.DecompilerRunnable.cfrDecompilation(DecompilerRunnable.java:348)
         *     at async.DecompilerRunnable.call(DecompilerRunnable.java:309)
         *     at async.DecompilerRunnable.call(DecompilerRunnable.java:31)
         *     at util.ExecutorUtil.lambda$submitAsync$0(ExecutorUtil.java:19)
         *     at java.util.concurrent.CompletableFuture$AsyncSupply.run(CompletableFuture.java:1604)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
         *     at java.lang.Thread.run(Thread.java:750)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public void init() {
        try (Connection connection = this.dataSource.getConnection();){
            String sql = "CREATE TABLE IF NOT EXISTS global_stats (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(200), uuid VARCHAR(200), first_play TIMESTAMP NULL DEFAULT NULL, last_play TIMESTAMP NULL DEFAULT NULL, wins INT(200), kills INT(200), final_kills INT(200), looses INT(200), deaths INT(200), final_deaths INT(200), beds_destroyed INT(200), games_played INT(200));";
            try (Statement statement = connection.createStatement();){
                statement.executeUpdate(sql);
            }
            sql = "CREATE TABLE IF NOT EXISTS quick_buy_2 (uuid VARCHAR(36) PRIMARY KEY, slot_19 VARCHAR(200), slot_20 VARCHAR(200), slot_21 VARCHAR(200), slot_22 VARCHAR(200), slot_23 VARCHAR(200), slot_24 VARCHAR(200), slot_25 VARCHAR(200),slot_28 VARCHAR(200), slot_29 VARCHAR(200), slot_30 VARCHAR(200), slot_31 VARCHAR(200), slot_32 VARCHAR(200), slot_33 VARCHAR(200), slot_34 VARCHAR(200),slot_37 VARCHAR(200), slot_38 VARCHAR(200), slot_39 VARCHAR(200), slot_40 VARCHAR(200), slot_41 VARCHAR(200), slot_42 VARCHAR(200), slot_43 VARCHAR(200));";
            statement = connection.createStatement();
            try {
                statement.executeUpdate(sql);
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
            sql = "CREATE TABLE IF NOT EXISTS player_levels (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid VARCHAR(200), level INT(200), xp INT(200), name VARCHAR(200) CHARACTER SET utf8, next_cost INT(200));";
            statement = connection.createStatement();
            try {
                statement.executeUpdate(sql);
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
            sql = "CREATE TABLE IF NOT EXISTS player_language (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid VARCHAR(200), iso VARCHAR(200));";
            statement = connection.createStatement();
            try {
                statement.executeUpdate(sql);
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveStats(PlayerStats stats) {
        block21: {
            try (Connection connection = this.dataSource.getConnection();){
                if (this.hasStats(stats.getUuid())) {
                    String sql = "UPDATE global_stats SET first_play=?, last_play=?, wins=?, kills=?, final_kills=?, looses=?, deaths=?, final_deaths=?, beds_destroyed=?, games_played=?, name=? WHERE uuid = ?;";
                    try (PreparedStatement statement = connection.prepareStatement(sql);){
                        statement.setTimestamp(1, stats.getFirstPlay() != null ? Timestamp.from(stats.getFirstPlay()) : null);
                        statement.setTimestamp(2, stats.getLastPlay() != null ? Timestamp.from(stats.getLastPlay()) : null);
                        statement.setInt(3, stats.getWins());
                        statement.setInt(4, stats.getKills());
                        statement.setInt(5, stats.getFinalKills());
                        statement.setInt(6, stats.getLosses());
                        statement.setInt(7, stats.getDeaths());
                        statement.setInt(8, stats.getFinalDeaths());
                        statement.setInt(9, stats.getBedsDestroyed());
                        statement.setInt(10, stats.getGamesPlayed());
                        statement.setString(11, stats.getName());
                        statement.setString(12, stats.getUuid().toString());
                        statement.executeUpdate();
                        break block21;
                    }
                }
                String sql = "INSERT INTO global_stats (name, uuid, first_play, last_play, wins, kills, final_kills, looses, deaths, final_deaths, beds_destroyed, games_played) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                try (PreparedStatement statement = connection.prepareStatement(sql);){
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
        String sql = "SELECT first_play, last_play, wins, kills, final_kills, looses, deaths, final_deaths,beds_destroyed, games_played FROM global_stats WHERE uuid = ?;";
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, uuid.toString());
            try (ResultSet result = statement.executeQuery();){
                if (result.next()) {
                    Timestamp firstPlay = result.getTimestamp(1);
                    Timestamp lastPlay = result.getTimestamp(2);
                    stats.setFirstPlay(firstPlay != null ? firstPlay.toInstant() : null);
                    stats.setLastPlay(lastPlay != null ? lastPlay.toInstant() : null);
                    stats.setWins(result.getInt(3));
                    stats.setKills(result.getInt(4));
                    stats.setFinalKills(result.getInt(5));
                    stats.setLosses(result.getInt(6));
                    stats.setDeaths(result.getInt(7));
                    stats.setFinalDeaths(result.getInt(8));
                    stats.setBedsDestroyed(result.getInt(9));
                    stats.setGamesPlayed(result.getInt(10));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    @Override
    public void setQuickBuySlot(UUID uuid, String shopPath, int slot) {
        Object sql = "SELECT uuid FROM quick_buy_2 WHERE uuid = ?;";
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement((String)sql);){
            statement.setString(1, uuid.toString());
            try (ResultSet result = statement.executeQuery();){
                PreparedStatement statement2;
                if (!result.next()) {
                    sql = "INSERT INTO quick_buy_2 VALUES(0, ?, ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ');";
                    statement2 = connection.prepareStatement((String)sql);
                    try {
                        statement2.setString(1, uuid.toString());
                        statement2.executeUpdate();
                    } finally {
                        if (statement2 != null) {
                            statement2.close();
                        }
                    }
                }
                BedWars.debug("UPDATE SET SLOT " + slot + " identifier " + shopPath);
                sql = "UPDATE quick_buy_2 SET slot_" + slot + " = ? WHERE uuid = ?;";
                statement2 = connection.prepareStatement((String)sql);
                try {
                    statement2.setString(1, shopPath);
                    statement2.setString(2, uuid.toString());
                    statement2.executeUpdate();
                } finally {
                    if (statement2 != null) {
                        statement2.close();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public String getQuickBuySlots(UUID uuid, int slot) {
        String sql = "SELECT slot_" + slot + " FROM quick_buy_2 WHERE uuid = ?;";
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, uuid.toString());
            try (ResultSet result = statement.executeQuery();){
                if (!result.next()) return "";
                String string = result.getString(1);
                return string;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    public HashMap<Integer, String> getQuickBuySlots(UUID uuid, int[] slot) {
        HashMap<Integer, String> results = new HashMap<Integer, String>();
        if (slot.length == 0) {
            return results;
        }
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM quick_buy_2 WHERE uuid = ?;");){
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /*
     * Exception decompiling
     */
    @Override
    public boolean hasQuickBuy(UUID uuid) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:538)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         *     at async.DecompilerRunnable.cfrDecompilation(DecompilerRunnable.java:348)
         *     at async.DecompilerRunnable.call(DecompilerRunnable.java:309)
         *     at async.DecompilerRunnable.call(DecompilerRunnable.java:31)
         *     at util.ExecutorUtil.lambda$submitAsync$0(ExecutorUtil.java:19)
         *     at java.util.concurrent.CompletableFuture$AsyncSupply.run(CompletableFuture.java:1604)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
         *     at java.lang.Thread.run(Thread.java:750)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int getColumn(UUID player, String column) {
        String sql = "SELECT ? FROM global_stats WHERE uuid = ?;";
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, column);
            statement.setString(2, player.toString());
            try (ResultSet result = statement.executeQuery();){
                if (!result.next()) return 0;
                int n = result.getInt(column);
                return n;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public Object[] getLevelData(UUID uuid) {
        String sql = "SELECT level, xp, name, next_cost FROM player_levels WHERE uuid = ?;";
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, uuid.toString());
            try (ResultSet result = statement.executeQuery();){
                if (!result.next()) return new Object[]{1, 0, "", 0};
                Object[] objectArray = new Object[]{result.getInt(1), result.getInt(2), result.getString(3), result.getInt(4)};
                return objectArray;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[]{1, 0, "", 0};
    }

    @Override
    public void setLevelData(UUID uuid, int level, int xp, String displayName, int nextCost) {
        block35: {
            String sql = "SELECT uuid from player_levels WHERE uuid = ?;";
            try (Connection connection = this.dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql);){
                statement.setString(1, uuid.toString());
                try (ResultSet result = statement.executeQuery();){
                    if (!result.next()) {
                        sql = "INSERT INTO player_levels VALUES (?, ?, ?, ?, ?, ?);";
                        try (PreparedStatement statement2 = connection.prepareStatement(sql);){
                            statement2.setInt(1, 0);
                            statement2.setString(2, uuid.toString());
                            statement2.setInt(3, level);
                            statement2.setInt(4, xp);
                            statement2.setString(5, displayName);
                            statement2.setInt(6, nextCost);
                            statement2.executeUpdate();
                            break block35;
                        }
                    }
                    sql = displayName == null ? "UPDATE player_levels SET level=?, xp=? WHERE uuid = ?;" : "UPDATE player_levels SET level=?, xp=?, name=?, next_cost=? WHERE uuid = ?;";
                    try (PreparedStatement statement2 = connection.prepareStatement(sql);){
                        statement2.setInt(1, level);
                        statement2.setInt(2, xp);
                        if (displayName != null) {
                            statement2.setString(3, displayName);
                            statement2.setInt(4, nextCost);
                            statement2.setString(5, uuid.toString());
                        } else {
                            statement2.setString(3, uuid.toString());
                        }
                        statement2.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setLanguage(UUID uuid, String iso) {
        block33: {
            String sql = "SELECT iso FROM player_language WHERE uuid = ?;";
            try (Connection connection = this.dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql);){
                statement.setString(1, uuid.toString());
                try (ResultSet result = statement.executeQuery();){
                    if (result.next()) {
                        sql = "UPDATE player_language SET iso = ? WHERE uuid = ?;";
                        try (PreparedStatement statement2 = connection.prepareStatement(sql);){
                            statement2.setString(1, iso);
                            statement2.setString(2, uuid.toString());
                            statement2.executeUpdate();
                            break block33;
                        }
                    }
                    sql = "INSERT INTO player_language VALUES (0, ?, ?);";
                    try (PreparedStatement statement2 = connection.prepareStatement(sql);){
                        statement2.setString(1, uuid.toString());
                        statement2.setString(2, iso);
                        statement2.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public String getLanguage(UUID uuid) {
        String sql = "SELECT iso FROM player_language WHERE uuid = ?;";
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, uuid.toString());
            try (ResultSet result = statement.executeQuery();){
                if (!result.next()) return Language.getDefaultLanguage().getIso();
                String string = result.getString(1);
                return string;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Language.getDefaultLanguage().getIso();
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
        try (Connection con = this.dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);){
            int index = hasQuick ? 0 : 1;
            for (int key : updateSlots.keySet()) {
                String identifier = updateSlots.get(key);
                ps.setString(++index, identifier.trim().isEmpty() ? null : identifier);
            }
            ps.setString(hasQuick ? updateSlots.size() + 1 : 1, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

