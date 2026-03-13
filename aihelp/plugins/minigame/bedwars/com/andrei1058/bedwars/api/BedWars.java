/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.api;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.shop.IContentTier;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.configuration.ConfigManager;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.levels.Level;
import com.andrei1058.bedwars.api.party.Party;
import com.andrei1058.bedwars.api.server.ISetupSession;
import com.andrei1058.bedwars.api.server.RestoreAdapter;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.api.server.VersionSupport;
import com.andrei1058.bedwars.api.sidebar.ISidebarService;
import java.io.File;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface BedWars {
    public IStats getStatsUtil();

    public AFKUtil getAFKUtil();

    public ArenaUtil getArenaUtil();

    public Configs getConfigs();

    public ShopUtil getShopUtil();

    public TeamUpgradesUtil getTeamUpgradesUtil();

    public Level getLevelsUtil();

    public Party getPartyUtil();

    public ISetupSession getSetupSession(UUID var1);

    public boolean isInSetupSession(UUID var1);

    public ServerType getServerType();

    public String getLangIso(Player var1);

    public ParentCommand getBedWarsCommand();

    public RestoreAdapter getRestoreAdapter();

    public void setRestoreAdapter(RestoreAdapter var1) throws IllegalAccessError;

    public void setPartyAdapter(Party var1);

    public VersionSupport getVersionSupport();

    public Language getDefaultLang();

    public String getLobbyWorld();

    public String getForCurrentVersion(String var1, String var2, String var3);

    public void setLevelAdapter(Level var1);

    public boolean isAutoScale();

    public Language getLanguageByIso(String var1);

    public Language getPlayerLanguage(Player var1);

    public File getAddonsPath();

    public ScoreboardUtil getScoreboardUtil();

    public boolean isShuttingDown();

    public ISidebarService getScoreboardManager();

    public static interface ScoreboardUtil {
        public void removePlayerScoreboard(Player var1);

        public void givePlayerScoreboard(Player var1, boolean var2);
    }

    public static interface TeamUpgradesUtil {
        public boolean isWatchingGUI(Player var1);

        public void setWatchingGUI(Player var1);

        public void removeWatchingUpgrades(UUID var1);

        public int getTotalUpgradeTiers(IArena var1);
    }

    public static interface ShopUtil {
        public int calculateMoney(Player var1, Material var2);

        public Material getCurrency(String var1);

        public ChatColor getCurrencyColor(Material var1);

        public String getCurrencyMsgPath(IContentTier var1);

        public String getRomanNumber(int var1);

        public void takeMoney(Player var1, Material var2, int var3);
    }

    public static interface Configs {
        public ConfigManager getMainConfig();

        public ConfigManager getSignsConfig();

        public ConfigManager getGeneratorsConfig();

        public ConfigManager getShopConfig();

        public ConfigManager getUpgradesConfig();
    }

    public static interface ArenaUtil {
        public boolean canAutoScale(String var1);

        public void addToEnableQueue(IArena var1);

        public void removeFromEnableQueue(IArena var1);

        public boolean isPlaying(Player var1);

        public boolean isSpectating(Player var1);

        public void loadArena(String var1, Player var2);

        public void setGamesBeforeRestart(int var1);

        public int getGamesBeforeRestart();

        public IArena getArenaByPlayer(Player var1);

        public void setArenaByPlayer(Player var1, IArena var2);

        public void removeArenaByPlayer(Player var1, IArena var2);

        public IArena getArenaByName(String var1);

        public IArena getArenaByIdentifier(String var1);

        public void setArenaByName(IArena var1);

        public void removeArenaByName(String var1);

        public LinkedList<IArena> getArenas();

        public boolean vipJoin(Player var1);

        public int getPlayers(String var1);

        public boolean joinRandomArena(Player var1);

        public boolean joinRandomFromGroup(Player var1, String var2);

        public LinkedList<IArena> getEnableQueue();

        public void sendLobbyCommandItems(Player var1);
    }

    public static interface AFKUtil {
        public boolean isPlayerAFK(Player var1);

        public void setPlayerAFK(Player var1, boolean var2);

        public int getPlayerTimeAFK(Player var1);
    }

    public static interface IStats {
        public Timestamp getPlayerFirstPlay(UUID var1);

        public Timestamp getPlayerLastPlay(UUID var1);

        public int getPlayerWins(UUID var1);

        public int getPlayerKills(UUID var1);

        public int getPlayerTotalKills(UUID var1);

        public int getPlayerFinalKills(UUID var1);

        public int getPlayerLoses(UUID var1);

        public int getPlayerDeaths(UUID var1);

        public int getPlayerFinalDeaths(UUID var1);

        public int getPlayerBedsDestroyed(UUID var1);

        public int getPlayerGamesPlayed(UUID var1);
    }
}

