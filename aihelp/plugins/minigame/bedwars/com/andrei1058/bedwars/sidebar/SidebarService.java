/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.sidebar;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.sidebar.PlayerSidebarInitEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.api.sidebar.ISidebar;
import com.andrei1058.bedwars.api.sidebar.ISidebarService;
import com.andrei1058.bedwars.libs.sidebar.SidebarManager;
import com.andrei1058.bedwars.metrics.MetricsManager;
import com.andrei1058.bedwars.sidebar.BwSidebar;
import com.andrei1058.bedwars.sidebar.ScoreboardListener;
import com.andrei1058.bedwars.sidebar.thread.RefreshLifeTask;
import com.andrei1058.bedwars.sidebar.thread.RefreshPlaceholdersTask;
import com.andrei1058.bedwars.sidebar.thread.RefreshPlayerListTask;
import com.andrei1058.bedwars.sidebar.thread.RefreshTabHeaderFooterTask;
import com.andrei1058.bedwars.sidebar.thread.RefreshTitleTask;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SidebarService
implements ISidebarService {
    private static SidebarService instance;
    private final SidebarManager sidebarHandler;
    private final HashMap<UUID, BwSidebar> sidebars = new HashMap();

    public static boolean init(JavaPlugin plugin) {
        if (null == instance) {
            instance = new SidebarService();
            Logger log = Bukkit.getLogger();
            int playerListRefreshInterval = BedWars.config.getInt("scoreboard-settings.player-list.names-refresh-interval");
            if (playerListRefreshInterval < 1) {
                Bukkit.getLogger().info("Scoreboard names list refresh is disabled. (It is set to " + playerListRefreshInterval + ").");
            } else {
                if (playerListRefreshInterval < 20) {
                    log.warning("Scoreboard names list refresh interval is set to: " + playerListRefreshInterval);
                    log.warning("It is not recommended to use a value under 20 ticks.");
                    log.warning("If you expect performance issues please increase its timer.");
                }
                Bukkit.getScheduler().runTaskTimer((Plugin)plugin, (Runnable)new RefreshPlayerListTask(), 1L, (long)playerListRefreshInterval);
            }
            MetricsManager.appendPie("sb_list_refresh_interval", () -> String.valueOf(playerListRefreshInterval));
            int placeholdersRefreshInterval = BedWars.config.getInt("scoreboard-settings.sidebar.placeholders-refresh-interval");
            if (placeholdersRefreshInterval < 1) {
                log.info("Scoreboard placeholders refresh is disabled. (It is set to " + placeholdersRefreshInterval + ").");
            } else {
                if (placeholdersRefreshInterval < 20) {
                    log.warning("Scoreboard placeholders refresh interval is set to: " + placeholdersRefreshInterval);
                    log.warning("It is not recommended to use a value under 20 ticks.");
                    log.warning("If you expect performance issues please increase its timer.");
                }
                Bukkit.getScheduler().runTaskTimer((Plugin)plugin, (Runnable)new RefreshPlaceholdersTask(), 1L, (long)placeholdersRefreshInterval);
            }
            MetricsManager.appendPie("sb_placeholder_refresh_interval", () -> String.valueOf(placeholdersRefreshInterval));
            int titleRefreshInterval = BedWars.config.getInt("scoreboard-settings.sidebar.title-refresh-interval");
            if (titleRefreshInterval < 1) {
                log.info("Scoreboard title refresh is disabled. (It is set to " + titleRefreshInterval + ").");
            } else {
                if (titleRefreshInterval < 4) {
                    log.warning("Scoreboard title refresh interval is set to: " + titleRefreshInterval);
                    log.warning("If you expect performance issues please increase its timer.");
                }
                Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)plugin, (Runnable)new RefreshTitleTask(), 1L, (long)titleRefreshInterval);
            }
            MetricsManager.appendPie("sb_title_refresh_interval", () -> String.valueOf(titleRefreshInterval));
            int healthAnimationInterval = BedWars.config.getInt("scoreboard-settings.health.animation-refresh-interval");
            if (healthAnimationInterval < 1) {
                log.info("Scoreboard health animation refresh is disabled. (It is set to " + healthAnimationInterval + ").");
            } else {
                if (healthAnimationInterval < 20) {
                    log.warning("Scoreboard health animation refresh interval is set to: " + healthAnimationInterval);
                    log.warning("It is not recommended to use a value under 20 ticks.");
                    log.warning("If you expect performance issues please increase its timer.");
                }
                Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)plugin, (Runnable)new RefreshLifeTask(), 1L, (long)healthAnimationInterval);
            }
            MetricsManager.appendPie("sb_health_refresh_interval", () -> String.valueOf(healthAnimationInterval));
            int tabHeaderFooterRefreshInterval = BedWars.config.getInt("scoreboard-settings.tab-header-footer.refresh-interval");
            if (tabHeaderFooterRefreshInterval < 1 || !BedWars.config.getBoolean("scoreboard-settings.tab-header-footer.enable")) {
                log.info("Scoreboard Tab header-footer refresh is disabled.");
            } else {
                if (tabHeaderFooterRefreshInterval < 20) {
                    log.warning("Scoreboard tab header-footer refresh interval is set to: " + tabHeaderFooterRefreshInterval);
                    log.warning("It is not recommended to use a value under 20 ticks.");
                    log.warning("If you expect performance issues please increase its timer.");
                }
                Bukkit.getScheduler().runTaskTimer((Plugin)plugin, (Runnable)new RefreshTabHeaderFooterTask(), 1L, (long)tabHeaderFooterRefreshInterval);
            }
            MetricsManager.appendPie("sb_header_footer_refresh_interval", () -> String.valueOf(tabHeaderFooterRefreshInterval));
            boolean lobbySidebar = BedWars.config.getBoolean("scoreboard-settings.sidebar.enable-lobby-sidebar") && BedWars.getServerType() == ServerType.MULTIARENA;
            MetricsManager.appendPie("sb_lobby_enable", () -> String.valueOf(lobbySidebar));
            boolean gameSidebar = BedWars.config.getBoolean("scoreboard-settings.sidebar.enable-game-sidebar");
            MetricsManager.appendPie("sb_game_enable", () -> String.valueOf(gameSidebar));
            BedWars.registerEvents(new ScoreboardListener());
        }
        return SidebarService.instance.sidebarHandler != null;
    }

    private SidebarService() {
        this.sidebarHandler = SidebarManager.init();
    }

    @Override
    public void giveSidebar(@NotNull Player player, @Nullable IArena arena, boolean delay) {
        BwSidebar sidebar = this.sidebars.getOrDefault(player.getUniqueId(), null);
        if (null != sidebar) {
            if (null == arena) {
                if (!BedWars.config.getBoolean("scoreboard-settings.sidebar.enable-lobby-sidebar") || BedWars.getServerType() == ServerType.SHARED) {
                    this.remove(sidebar);
                    return;
                }
            } else if (!BedWars.config.getBoolean("scoreboard-settings.sidebar.enable-game-sidebar")) {
                this.remove(sidebar);
                return;
            }
        }
        if (!BedWars.config.getBoolean("scoreboard-settings.sidebar.enable-lobby-sidebar") && null == arena) {
            return;
        }
        if (!BedWars.config.getBoolean("scoreboard-settings.sidebar.enable-game-sidebar") && null != arena) {
            return;
        }
        List<String> lines = null;
        if (null == arena) {
            if (BedWars.getServerType() != ServerType.SHARED) {
                lines = Language.getList(player, Messages.SCOREBOARD_LOBBY);
            }
        } else if (arena.getStatus() == GameState.waiting) {
            lines = arena.isSpectator(player) ? Language.getScoreboard(player, "sidebar." + arena.getGroup() + ".waiting.spectator", Messages.SCOREBOARD_DEFAULT_WAITING_SPEC) : Language.getScoreboard(player, "sidebar." + arena.getGroup() + ".waiting.player", Messages.SCOREBOARD_DEFAULT_WAITING);
        } else if (arena.getStatus() == GameState.starting) {
            lines = arena.isSpectator(player) ? Language.getScoreboard(player, "sidebar." + arena.getGroup() + ".starting.spectator", Messages.SCOREBOARD_DEFAULT_STARTING_SPEC) : Language.getScoreboard(player, "sidebar." + arena.getGroup() + ".starting.player", Messages.SCOREBOARD_DEFAULT_STARTING);
        } else if (arena.getStatus() == GameState.playing) {
            ITeam holderExTeam;
            lines = arena.isSpectator(player) ? (null == (holderExTeam = arena.getExTeam(player.getUniqueId())) ? Language.getScoreboard(player, "sidebar." + arena.getGroup() + ".playing.spectator", Messages.SCOREBOARD_DEFAULT_PLAYING_SPEC) : Language.getScoreboard(player, "sidebar." + arena.getGroup() + ".playing.eliminated", Messages.SCOREBOARD_DEFAULT_PLAYING_SPEC_ELIMINATED)) : Language.getScoreboard(player, "sidebar." + arena.getGroup() + ".playing.alive", Messages.SCOREBOARD_DEFAULT_PLAYING);
        } else if (arena.getStatus() == GameState.restarting) {
            ITeam holderExTeam;
            ITeam holderTeam = arena.getTeam(player);
            ITeam iTeam = holderExTeam = null == holderTeam ? arena.getExTeam(player.getUniqueId()) : null;
            lines = null == holderTeam && null == holderExTeam ? Language.getScoreboard(player, "sidebar." + arena.getGroup() + ".restarting.spectator", Messages.SCOREBOARD_DEFAULT_RESTARTING_SPEC) : (null == holderTeam && holderExTeam.equals(arena.getWinner()) ? Language.getScoreboard(player, "sidebar." + arena.getGroup() + ".restarting.winner-eliminated", Messages.SCOREBOARD_DEFAULT_RESTARTING_WIN2) : (null == holderExTeam && holderTeam.equals(arena.getWinner()) ? Language.getScoreboard(player, "sidebar." + arena.getGroup() + ".restarting.winner-alive", Messages.SCOREBOARD_DEFAULT_RESTARTING_WIN1) : Language.getScoreboard(player, "sidebar." + arena.getGroup() + ".restarting.loser", Messages.SCOREBOARD_DEFAULT_RESTARTING_LOSER)));
        }
        if (null == lines || lines.isEmpty()) {
            if (null != sidebar) {
                this.remove(sidebar);
            }
            return;
        }
        ArrayList<String> title = new ArrayList<String>(Arrays.asList(lines.get(0).split(",")));
        if (lines.size() == 1) {
            lines = new ArrayList<String>();
        }
        lines = lines.subList(1, lines.size());
        boolean newlyAdded = false;
        if (null == sidebar) {
            sidebar = new BwSidebar(player);
            newlyAdded = true;
            PlayerSidebarInitEvent event = new PlayerSidebarInitEvent(player, sidebar);
            Bukkit.getPluginManager().callEvent((Event)event);
            if (event.isCancelled()) {
                return;
            }
        }
        sidebar.setContent(title, lines, arena);
        if (newlyAdded) {
            this.sidebars.put(player.getUniqueId(), sidebar);
        }
    }

    public void remove(@NotNull BwSidebar sidebar) {
        this.sidebars.remove(sidebar.getPlayer().getUniqueId());
        sidebar.remove();
    }

    @Override
    public void remove(@NotNull Player player) {
        BwSidebar sidebar = this.sidebars.remove(player.getUniqueId());
        if (null != sidebar) {
            sidebar.remove();
        }
    }

    public static SidebarService getInstance() {
        return instance;
    }

    protected SidebarManager getSidebarHandler() {
        return this.sidebarHandler;
    }

    @Override
    public void refreshTitles() {
        this.sidebars.forEach((k, v) -> v.getHandle().refreshTitle());
    }

    @Override
    public void refreshPlaceholders() {
        this.sidebars.forEach((k, v) -> v.getHandle().refreshPlaceholders());
    }

    @Override
    public void refreshPlaceholders(IArena arena) {
        this.sidebars.forEach((k, v) -> {
            if (v.getArena() != null && v.getArena().equals(arena)) {
                v.getHandle().refreshPlaceholders();
            }
        });
    }

    @Override
    public void refreshTabList() {
        this.sidebars.forEach((k, v) -> v.getHandle().playerTabRefreshAnimation());
    }

    public void refreshTabHeaderFooter() {
        this.sidebars.forEach((k, v) -> {
            if (null != v && null != v.getHeaderFooter()) {
                this.sidebarHandler.sendHeaderFooter(v.getPlayer(), v.getHeaderFooter());
            }
        });
    }

    @Override
    public void refreshHealth() {
        this.sidebars.forEach((k, v) -> {
            if (null != v.getArena()) {
                v.getHandle().playerHealthRefreshAnimation();
                for (Player player : v.getArena().getPlayers()) {
                    v.getHandle().setPlayerHealth(player, (int)Math.ceil(player.getHealth()));
                }
            }
        });
    }

    @Override
    @Nullable
    public ISidebar getSidebar(@NotNull Player player) {
        return this.sidebars.getOrDefault(player.getUniqueId(), null);
    }

    public void refreshHealth(IArena arena, Player player, int health) {
        this.sidebars.forEach((k, v) -> {
            if (null != v.getArena() && v.getArena().equals(arena)) {
                v.getHandle().setPlayerHealth(player, health);
            }
        });
    }

    public void handleReJoin(IArena arena, Player player) {
        this.sidebars.forEach((k, v) -> {
            if (null != v.getArena() && v.getArena().equals(arena)) {
                v.giveUpdateTabFormat(player, false);
            }
        });
    }

    public void handleJoin(IArena arena, Player player, @Nullable Boolean spectator) {
        this.sidebars.forEach((k, v) -> {
            if (null != v.getArena() && v.getArena().equals(arena) && !v.getPlayer().equals((Object)player)) {
                v.giveUpdateTabFormat(player, false, spectator);
            }
        });
    }

    public void applyLobbyTab(Player player) {
        this.sidebars.forEach((k, v) -> {
            if (null == v.getArena() && !v.getPlayer().equals((Object)player)) {
                v.giveUpdateTabFormat(player, false);
            }
        });
    }

    public void handleInvisibility(ITeam team, Player player, boolean toggle) {
        this.sidebars.forEach((k, v) -> {
            if (null != v.getArena() && v.getArena().equals(team.getArena())) {
                v.handleInvisibilityPotion(player, toggle);
            }
        });
    }
}

