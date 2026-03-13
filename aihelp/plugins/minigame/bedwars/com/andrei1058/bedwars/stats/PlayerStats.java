/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.stats;

import java.time.Instant;
import java.util.UUID;

public class PlayerStats {
    private final UUID uuid;
    private String name;
    private Instant firstPlay;
    private Instant lastPlay;
    private int wins;
    private int kills;
    private int finalKills;
    private int totalKills;
    private int losses;
    private int deaths;
    private int finalDeaths;
    private int bedsDestroyed;
    private int gamesPlayed;

    public PlayerStats(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Instant getFirstPlay() {
        return this.firstPlay;
    }

    public void setFirstPlay(Instant firstPlay) {
        this.firstPlay = firstPlay;
    }

    public Instant getLastPlay() {
        return this.lastPlay;
    }

    public void setLastPlay(Instant lastPlay) {
        this.lastPlay = lastPlay;
    }

    public int getWins() {
        return this.wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getKills() {
        return this.kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
        this.totalKills = kills + this.finalKills;
    }

    public int getFinalKills() {
        return this.finalKills;
    }

    public void setFinalKills(int finalKills) {
        this.finalKills = finalKills;
        this.totalKills = this.kills + finalKills;
    }

    public int getLosses() {
        return this.losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getFinalDeaths() {
        return this.finalDeaths;
    }

    public void setFinalDeaths(int finalDeaths) {
        this.finalDeaths = finalDeaths;
    }

    public int getBedsDestroyed() {
        return this.bedsDestroyed;
    }

    public void setBedsDestroyed(int bedsDestroyed) {
        this.bedsDestroyed = bedsDestroyed;
    }

    public int getGamesPlayed() {
        return this.gamesPlayed;
    }

    public void setGamesPlayed(int gamePlayed) {
        this.gamesPlayed = gamePlayed;
    }

    public int getTotalKills() {
        return this.totalKills;
    }
}

