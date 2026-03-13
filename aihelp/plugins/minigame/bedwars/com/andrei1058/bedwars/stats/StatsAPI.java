/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.stats;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.stats.PlayerStats;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class StatsAPI
implements BedWars.IStats {
    private static StatsAPI instance;

    public static StatsAPI getInstance() {
        if (instance == null) {
            instance = new StatsAPI();
        }
        return instance;
    }

    private StatsAPI() {
    }

    private PlayerStats getData(UUID uuid) {
        PlayerStats stats = BedWars.getStatsManager().getUnsafe(uuid);
        if (stats == null) {
            stats = BedWars.getRemoteDatabase().fetchStats(uuid);
        }
        return stats;
    }

    @Override
    public Timestamp getPlayerFirstPlay(UUID p) {
        Instant firstPlay = this.getData(p).getFirstPlay();
        if (firstPlay == null) {
            return null;
        }
        return Timestamp.from(firstPlay);
    }

    @Override
    public Timestamp getPlayerLastPlay(UUID p) {
        Instant lastPlay = this.getData(p).getLastPlay();
        if (lastPlay == null) {
            return null;
        }
        return Timestamp.from(lastPlay);
    }

    @Override
    public int getPlayerWins(UUID p) {
        return this.getData(p).getWins();
    }

    @Override
    public int getPlayerKills(UUID p) {
        return this.getData(p).getKills();
    }

    @Override
    public int getPlayerTotalKills(UUID p) {
        return this.getData(p).getTotalKills();
    }

    @Override
    public int getPlayerFinalKills(UUID p) {
        return this.getData(p).getFinalKills();
    }

    @Override
    public int getPlayerLoses(UUID p) {
        return this.getData(p).getLosses();
    }

    @Override
    public int getPlayerDeaths(UUID p) {
        return this.getData(p).getDeaths();
    }

    @Override
    public int getPlayerFinalDeaths(UUID p) {
        return this.getData(p).getFinalDeaths();
    }

    @Override
    public int getPlayerBedsDestroyed(UUID p) {
        return this.getData(p).getBedsDestroyed();
    }

    @Override
    public int getPlayerGamesPlayed(UUID p) {
        return this.getData(p).getGamesPlayed();
    }
}

