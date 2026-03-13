/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.api.arena;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.NextEvent;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.stats.GameStatsHolder;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.arena.team.ITeamAssigner;
import com.andrei1058.bedwars.api.configuration.ConfigManager;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.region.Region;
import com.andrei1058.bedwars.api.tasks.PlayingTask;
import com.andrei1058.bedwars.api.tasks.RestartingTask;
import com.andrei1058.bedwars.api.tasks.StartingTask;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public interface IArena {
    public boolean isSpectator(Player var1);

    public boolean isSpectator(UUID var1);

    public boolean isReSpawning(UUID var1);

    public String getArenaName();

    public void init(World var1);

    public ConfigManager getConfig();

    public boolean isPlayer(Player var1);

    public List<Player> getSpectators();

    public ITeam getTeam(Player var1);

    public ITeam getExTeam(UUID var1);

    public String getDisplayName();

    public void setWorldName(String var1);

    public GameState getStatus();

    public List<Player> getPlayers();

    public int getMaxPlayers();

    public String getGroup();

    public int getMaxInTeam();

    public ConcurrentHashMap<Player, Integer> getRespawnSessions();

    public void updateSpectatorCollideRule(Player var1, boolean var2);

    public void updateNextEvent();

    public boolean addPlayer(Player var1, boolean var2);

    public boolean addSpectator(Player var1, boolean var2, Location var3);

    public void removePlayer(Player var1, boolean var2);

    public void removeSpectator(Player var1, boolean var2);

    public boolean reJoin(Player var1);

    public void disable();

    public void restart();

    public World getWorld();

    public String getDisplayStatus(Language var1);

    public String getDisplayGroup(Player var1);

    public String getDisplayGroup(Language var1);

    public List<ITeam> getTeams();

    public void addPlacedBlock(Block var1);

    public Map<UUID, Long> getFireballCooldowns();

    public void removePlacedBlock(Block var1);

    public boolean isBlockPlaced(Block var1);

    @Deprecated
    public int getPlayerKills(Player var1, boolean var2);

    @Nullable
    public GameStatsHolder getStatsHolder();

    @Deprecated
    public int getPlayerBedsDestroyed(Player var1);

    public List<Block> getSigns();

    public int getIslandRadius();

    public void setGroup(String var1);

    public void setStatus(GameState var1);

    public void changeStatus(GameState var1);

    @Deprecated
    default public boolean isRespawning(Player p) {
        return this.isReSpawning(p);
    }

    public void addSign(Location var1);

    public void refreshSigns();

    @Deprecated
    public void addPlayerKill(Player var1, boolean var2, Player var3);

    @Deprecated
    public void addPlayerBedDestroyed(Player var1);

    @Deprecated
    public ITeam getPlayerTeam(String var1);

    public void checkWinner();

    @Deprecated
    public void addPlayerDeath(Player var1);

    public void setNextEvent(NextEvent var1);

    public NextEvent getNextEvent();

    public void sendPreGameCommandItems(Player var1);

    public void sendSpectatorCommandItems(Player var1);

    public ITeam getTeam(String var1);

    public StartingTask getStartingTask();

    public PlayingTask getPlayingTask();

    public RestartingTask getRestartingTask();

    public List<IGenerator> getOreGenerators();

    public List<String> getNextEvents();

    @Deprecated
    public int getPlayerDeaths(Player var1, boolean var2);

    public void sendDiamondsUpgradeMessages();

    public void sendEmeraldsUpgradeMessages();

    public LinkedList<Vector> getPlaced();

    public void destroyData();

    public int getUpgradeDiamondsCount();

    public int getUpgradeEmeraldsCount();

    public List<Region> getRegionsList();

    public ConcurrentHashMap<Player, Integer> getShowTime();

    public void setAllowSpectate(boolean var1);

    public boolean isAllowSpectate();

    public String getWorldName();

    public int getRenderDistance();

    public boolean startReSpawnSession(Player var1, int var2);

    public boolean isReSpawning(Player var1);

    public Location getReSpawnLocation();

    public Location getSpectatorLocation();

    public Location getWaitingLocation();

    public boolean isProtected(Location var1);

    public void abandonGame(Player var1);

    public int getYKillHeight();

    public Instant getStartTime();

    public ITeamAssigner getTeamAssigner();

    public void setTeamAssigner(ITeamAssigner var1);

    public List<Player> getLeavingPlayers();

    public boolean isAllowMapBreak();

    public void setAllowMapBreak(boolean var1);

    public boolean isTeamBed(Location var1);

    @Nullable
    public ITeam getBedsTeam(Location var1);

    @Nullable
    public ITeam getWinner();
}

