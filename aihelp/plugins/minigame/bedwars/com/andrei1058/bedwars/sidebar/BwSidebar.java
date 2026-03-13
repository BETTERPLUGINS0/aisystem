/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.sidebar;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.stats.DefaultStatistics;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.api.sidebar.ISidebar;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.stats.StatisticsOrdered;
import com.andrei1058.bedwars.levels.internal.PlayerLevel;
import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.Sidebar;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarLineAnimated;
import com.andrei1058.bedwars.libs.sidebar.SidebarManager;
import com.andrei1058.bedwars.libs.sidebar.TabHeaderFooter;
import com.andrei1058.bedwars.sidebar.BwSidebarLine;
import com.andrei1058.bedwars.sidebar.BwTabList;
import com.andrei1058.bedwars.sidebar.SidebarService;
import com.andrei1058.bedwars.stats.PlayerStats;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BwSidebar
implements ISidebar {
    private static final SidebarLine EMPTY_TITLE = new SidebarLine(){

        @Override
        @NotNull
        public String getLine() {
            return "";
        }
    };
    private final Player player;
    private IArena arena;
    private Sidebar handle;
    private TabHeaderFooter headerFooter;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat nextEventDateFormat;
    private final ConcurrentLinkedQueue<PlaceholderProvider> persistentProviders = new ConcurrentLinkedQueue();
    private final BwTabList tabList;
    @Nullable
    public StatisticsOrdered topStatistics;

    protected BwSidebar(Player player) {
        this.player = player;
        this.nextEventDateFormat = new SimpleDateFormat(Language.getMsg(player, Messages.FORMATTING_SCOREBOARD_NEXEVENT_TIMER));
        this.nextEventDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.dateFormat = new SimpleDateFormat(Language.getMsg(player, Messages.FORMATTING_SCOREBOARD_DATE));
        this.tabList = new BwTabList(this);
        String poweredBy = BedWars.config.getString("powered-by");
        this.registerPersistentPlaceholder(new PlaceholderProvider("{poweredBy}", () -> poweredBy));
        String serverId = BedWars.config.getString("bungee-settings.server-id");
        this.registerPersistentPlaceholder(new PlaceholderProvider("{server}", () -> serverId));
        String serverIp = BedWars.config.getString("server-ip");
        this.registerPersistentPlaceholder(new PlaceholderProvider("{serverIp}", () -> serverIp));
    }

    public void remove() {
        if (this.handle == null) {
            return;
        }
        this.tabList.onSidebarRemoval();
        this.handle.remove(this.player);
    }

    @Override
    public void setContent(List<String> titleArray, List<String> lineArray, @Nullable IArena arena) {
        this.arena = arena;
        SidebarLine title = this.normalizeTitle(titleArray);
        List lines = this.normalizeLines((List)lineArray);
        if (null == arena) {
            this.setTopStatistics(null);
        }
        ConcurrentLinkedQueue<PlaceholderProvider> placeholders = this.getPlaceholders(this.getPlayer());
        placeholders.addAll(this.persistentProviders);
        if (null == this.handle) {
            this.handle = SidebarService.getInstance().getSidebarHandler().createSidebar(title, lines, placeholders);
            this.handle.add(this.player);
        } else {
            this.handle.clearLines();
            Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
                new ArrayList<PlaceholderProvider>(this.handle.getPlaceholders()).forEach(p -> this.handle.removePlaceholder(p.getPlaceholder()));
                placeholders.forEach(p -> this.handle.addPlaceholder((PlaceholderProvider)p));
                this.handle.setTitle(title);
                lines.forEach(l -> this.handle.addLine((SidebarLine)l));
            }, 2L);
        }
        this.tabList.handlePlayerList();
        this.assignTabHeaderFooter();
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public SidebarLine normalizeTitle(@Nullable List<String> titleArray) {
        String[] data = new String[titleArray.size()];
        for (int x = 0; x < titleArray.size(); ++x) {
            data[x] = titleArray.get(x);
        }
        return null == titleArray || titleArray.isEmpty() ? EMPTY_TITLE : new SidebarLineAnimated(data);
    }

    @Contract(pure=true)
    @NotNull
    public LinkedList<SidebarLine> normalizeLines(@NotNull List<String> lineArray) {
        LinkedList<SidebarLine> lines = new LinkedList<SidebarLine>();
        int teamCount = 0;
        Language language = Language.getPlayerLanguage(this.player);
        String genericTeamFormat = language.m(Messages.FORMATTING_SCOREBOARD_TEAM_GENERIC);
        StatisticsOrdered.StringParser statParser = null == this.topStatistics ? null : this.topStatistics.newParser();
        for (String line : lineArray) {
            String finalTemp;
            String[] divided;
            line = line.replace("{server_ip}", "{serverIp}");
            String scoreLine = null;
            if (null != this.arena) {
                if (line.trim().equals("{team}")) {
                    if (this.arena.getTeams().size() <= teamCount) continue;
                    ITeam team = this.arena.getTeams().get(teamCount++);
                    String teamName = team.getDisplayName(language);
                    String teamLetter = String.valueOf(!teamName.isEmpty() ? Character.valueOf(teamName.charAt(0)) : "");
                    line = genericTeamFormat.replace("{TeamLetter}", teamLetter).replace("{TeamColor}", team.getColor().chat().toString()).replace("{TeamName}", teamName);
                    if (line.contains("{TeamStatus}") && BedWars.getAPI().getVersionSupport().getVersion() >= 10) {
                        line = line.replace("{TeamStatus}", "");
                        scoreLine = "{Team" + team.getName() + "Status}";
                    } else {
                        line = line.replace("{TeamStatus}", "{Team" + team.getName() + "Status}");
                    }
                }
                line = line.replace("{map}", this.arena.getDisplayName()).replace("{map_name}", this.arena.getArenaName()).replace("{group}", this.arena.getDisplayGroup(this.player));
                for (ITeam currentTeam : this.arena.getTeams()) {
                    ChatColor color = currentTeam.getColor().chat();
                    String teamName = currentTeam.getDisplayName(language);
                    String teamLetter = String.valueOf(!teamName.isEmpty() ? Character.valueOf(teamName.charAt(0)) : "");
                    line = line.replace("{Team" + currentTeam.getName() + "Color}", color.toString()).replace("{Team" + currentTeam.getName() + "Name}", teamName).replace("{Team" + currentTeam.getName() + "Letter}", teamLetter);
                    boolean isMember = currentTeam.isMember(this.getPlayer()) || currentTeam.wasMember(this.getPlayer().getUniqueId());
                    if (!isMember) continue;
                    HashMap<String, String> replacements = this.tabList.getTeamReplacements(currentTeam);
                    for (Map.Entry<String, String> entry : replacements.entrySet()) {
                        line = line.replace(entry.getKey(), entry.getValue());
                    }
                }
                if (this.arena.getWinner() != null) {
                    String winnerDisplayName = this.arena.getWinner().getDisplayName(Language.getPlayerLanguage(this.getPlayer()));
                    line = line.replace("{winnerTeamName}", winnerDisplayName).replace("{winnerTeamLetter}", String.valueOf(this.arena.getWinner().getColor().chat()) + winnerDisplayName.substring(0, 1)).replace("{winnerTeamColor}", this.arena.getWinner().getColor().chat().toString());
                }
                if (null != this.topStatistics && null != statParser && null == (line = statParser.parseString(line, language, language.m(Messages.MEANING_NOBODY)))) continue;
            }
            SidebarLine sidebarLine = (divided = (finalTemp = (line = line.replace("{serverIp}", BedWars.config.getString("server-ip")).replace("{poweredBy}", BedWars.config.getString("powered-by")).replace("{version}", BedWars.plugin.getDescription().getVersion()).replace("{server}", BedWars.config.getString("bungee-settings.server-id")))).split(",")).length > 1 ? this.normalizeTitle(Arrays.asList(divided)) : new BwSidebarLine(finalTemp, scoreLine);
            lines.add(sidebarLine);
        }
        return lines;
    }

    @Override
    public void giveUpdateTabFormat(@NotNull Player player, boolean skipStateCheck, @Nullable Boolean spectator) {
        this.tabList.giveUpdateTabFormat(player, skipStateCheck, spectator);
    }

    @Override
    public boolean isTabFormattingDisabled() {
        return this.tabList.isTabFormattingDisabled();
    }

    @Contract(pure=true)
    @NotNull
    ConcurrentLinkedQueue<PlaceholderProvider> getPlaceholders(@NotNull Player player) {
        ConcurrentLinkedQueue<PlaceholderProvider> providers = new ConcurrentLinkedQueue<PlaceholderProvider>();
        providers.add(new PlaceholderProvider("{player}", () -> ((Player)player).getDisplayName()));
        providers.add(new PlaceholderProvider("{money}", () -> String.valueOf(BedWars.getEconomy().getMoney(player))));
        providers.add(new PlaceholderProvider("{playerName}", () -> ((Player)player).getCustomName()));
        providers.add(new PlaceholderProvider("{date}", () -> this.dateFormat.format(new Date(System.currentTimeMillis()))));
        providers.add(new PlaceholderProvider("{version}", () -> BedWars.plugin.getDescription().getVersion()));
        PlayerLevel level = PlayerLevel.getLevelByPlayer(player.getUniqueId());
        if (null != level) {
            providers.add(new PlaceholderProvider("{progress}", level::getProgress));
            providers.add(new PlaceholderProvider("{level}", () -> String.valueOf(level.getLevelName())));
            providers.add(new PlaceholderProvider("{levelUnformatted}", () -> String.valueOf(level.getLevel())));
            providers.add(new PlaceholderProvider("{currentXp}", level::getFormattedCurrentXp));
            providers.add(new PlaceholderProvider("{requiredXp}", level::getFormattedRequiredXp));
        }
        if (this.hasNoArena()) {
            providers.add(new PlaceholderProvider("{on}", () -> String.valueOf(Bukkit.getOnlinePlayers().size())));
            PlayerStats persistentStats = BedWars.getStatsManager().get(player.getUniqueId());
            if (null != persistentStats) {
                providers.add(new PlaceholderProvider("{kills}", () -> String.valueOf(persistentStats.getKills())));
                providers.add(new PlaceholderProvider("{finalKills}", () -> String.valueOf(persistentStats.getFinalKills())));
                providers.add(new PlaceholderProvider("{beds}", () -> String.valueOf(persistentStats.getBedsDestroyed())));
                providers.add(new PlaceholderProvider("{deaths}", () -> String.valueOf(persistentStats.getDeaths())));
                providers.add(new PlaceholderProvider("{finalDeaths}", () -> String.valueOf(persistentStats.getFinalDeaths())));
                providers.add(new PlaceholderProvider("{wins}", () -> String.valueOf(persistentStats.getWins())));
                providers.add(new PlaceholderProvider("{losses}", () -> String.valueOf(persistentStats.getLosses())));
                providers.add(new PlaceholderProvider("{gamesPlayed}", () -> String.valueOf(persistentStats.getGamesPlayed())));
            }
        } else {
            providers.add(new PlaceholderProvider("{on}", () -> String.valueOf(this.arena.getPlayers().size())));
            providers.add(new PlaceholderProvider("{max}", () -> String.valueOf(this.arena.getMaxPlayers())));
            providers.add(new PlaceholderProvider("{nextEvent}", this::getNextEventName));
            if (this.arena.isSpectator(player)) {
                Language lang = Language.getPlayerLanguage(player);
                String targetFormat = lang.m(Messages.FORMAT_SPECTATOR_TARGET);
                providers.add(new PlaceholderProvider("{spectatorTarget}", () -> {
                    if (null == player.getSpectatorTarget() || !(player.getSpectatorTarget() instanceof Player)) {
                        return "";
                    }
                    Player target = (Player)player.getSpectatorTarget();
                    ITeam targetTeam = this.arena.getTeam(target);
                    if (null == targetTeam) {
                        return "";
                    }
                    return targetFormat.replace("{targetTeamColor}", targetTeam.getColor().chat().toString()).replace("{targetDisplayName}", target.getDisplayName()).replace("{targetName}", target.getDisplayName()).replace("{targetTeamName}", targetTeam.getDisplayName(lang));
                }));
            }
            providers.add(new PlaceholderProvider("{time}", () -> {
                GameState status = this.arena.getStatus();
                if (status == GameState.playing || status == GameState.restarting) {
                    return this.getNextEventTime();
                }
                if (status == GameState.starting && this.arena.getStartingTask() != null) {
                    return String.valueOf(this.arena.getStartingTask().getCountdown() + 1);
                }
                return this.dateFormat.format(new Date(System.currentTimeMillis()));
            }));
            if (null != this.arena.getStatsHolder()) {
                this.arena.getStatsHolder().get(player).ifPresent(holder -> {
                    holder.getStatistic(DefaultStatistics.KILLS).ifPresent(st -> providers.add(new PlaceholderProvider("{kills}", () -> String.valueOf(st.getDisplayValue(null)))));
                    holder.getStatistic(DefaultStatistics.KILLS_FINAL).ifPresent(st -> providers.add(new PlaceholderProvider("{finalKills}", () -> String.valueOf(st.getDisplayValue(null)))));
                    holder.getStatistic(DefaultStatistics.BEDS_DESTROYED).ifPresent(st -> providers.add(new PlaceholderProvider("{beds}", () -> String.valueOf(st.getDisplayValue(null)))));
                    holder.getStatistic(DefaultStatistics.DEATHS).ifPresent(st -> providers.add(new PlaceholderProvider("{deaths}", () -> String.valueOf(st.getDisplayValue(null)))));
                });
            }
            for (ITeam currentTeam : this.arena.getTeams()) {
                boolean isMember = currentTeam.isMember(player) || currentTeam.wasMember(player.getUniqueId());
                providers.add(new PlaceholderProvider("{Team" + currentTeam.getName() + "Status}", () -> {
                    Object result = currentTeam.isBedDestroyed() ? (currentTeam.getSize() > 0 ? Language.getMsg(this.getPlayer(), Messages.FORMATTING_SCOREBOARD_BED_DESTROYED).replace("{remainingPlayers}", String.valueOf(currentTeam.getSize())) : Language.getMsg(this.getPlayer(), Messages.FORMATTING_SCOREBOARD_TEAM_ELIMINATED)) : Language.getMsg(this.getPlayer(), Messages.FORMATTING_SCOREBOARD_TEAM_ALIVE);
                    if (isMember) {
                        result = (String)result + Language.getMsg(this.getPlayer(), Messages.FORMATTING_SCOREBOARD_YOUR_TEAM);
                    }
                    return result;
                }));
                if (!isMember) continue;
                providers.add(new PlaceholderProvider("{teamStatus}", () -> {
                    if (currentTeam.isBedDestroyed()) {
                        if (currentTeam.getSize() > 0) {
                            return Language.getMsg(this.getPlayer(), Messages.FORMATTING_SCOREBOARD_BED_DESTROYED).replace("{remainingPlayers}", String.valueOf(currentTeam.getSize()));
                        }
                        return Language.getMsg(this.getPlayer(), Messages.FORMATTING_SCOREBOARD_TEAM_ELIMINATED);
                    }
                    return Language.getMsg(this.getPlayer(), Messages.FORMATTING_SCOREBOARD_TEAM_ALIVE);
                }));
            }
        }
        return providers;
    }

    @NotNull
    private String getNextEventName() {
        if (!(this.arena instanceof Arena)) {
            return "-";
        }
        Arena arena = (Arena)this.arena;
        String st = "-";
        switch (arena.getNextEvent()) {
            case EMERALD_GENERATOR_TIER_II: {
                st = Language.getMsg(this.getPlayer(), Messages.NEXT_EVENT_EMERALD_UPGRADE_II);
                break;
            }
            case EMERALD_GENERATOR_TIER_III: {
                st = Language.getMsg(this.getPlayer(), Messages.NEXT_EVENT_EMERALD_UPGRADE_III);
                break;
            }
            case DIAMOND_GENERATOR_TIER_II: {
                st = Language.getMsg(this.getPlayer(), Messages.NEXT_EVENT_DIAMOND_UPGRADE_II);
                break;
            }
            case DIAMOND_GENERATOR_TIER_III: {
                st = Language.getMsg(this.getPlayer(), Messages.NEXT_EVENT_DIAMOND_UPGRADE_III);
                break;
            }
            case GAME_END: {
                st = Language.getMsg(this.getPlayer(), Messages.NEXT_EVENT_GAME_END);
                break;
            }
            case BEDS_DESTROY: {
                st = Language.getMsg(this.getPlayer(), Messages.NEXT_EVENT_BEDS_DESTROY);
                break;
            }
            case ENDER_DRAGON: {
                st = Language.getMsg(this.getPlayer(), Messages.NEXT_EVENT_DRAGON_SPAWN);
            }
        }
        return st;
    }

    @NotNull
    private String getNextEventTime() {
        if (!(this.arena instanceof Arena)) {
            return this.nextEventDateFormat.format(0L);
        }
        Arena arena = (Arena)this.arena;
        long time = 0L;
        switch (arena.getNextEvent()) {
            case EMERALD_GENERATOR_TIER_II: 
            case EMERALD_GENERATOR_TIER_III: {
                time = (long)arena.upgradeEmeraldsCount * 1000L;
                break;
            }
            case DIAMOND_GENERATOR_TIER_II: 
            case DIAMOND_GENERATOR_TIER_III: {
                time = (long)arena.upgradeDiamondsCount * 1000L;
                break;
            }
            case GAME_END: {
                time = (long)arena.getPlayingTask().getGameEndCountdown() * 1000L;
                break;
            }
            case BEDS_DESTROY: {
                time = (long)arena.getPlayingTask().getBedsDestroyCountdown() * 1000L;
                break;
            }
            case ENDER_DRAGON: {
                time = (long)arena.getPlayingTask().getDragonSpawnCountdown() * 1000L;
            }
        }
        return time == 0L ? "0" : this.nextEventDateFormat.format(new Date(time));
    }

    private boolean hasNoArena() {
        return null == this.arena;
    }

    private void assignTabHeaderFooter() {
        String footerPath;
        String headerPath;
        Language lang;
        block19: {
            block20: {
                ITeam exTeam;
                block21: {
                    block18: {
                        if (!BedWars.config.getBoolean("scoreboard-settings.tab-header-footer.enable")) {
                            return;
                        }
                        lang = Language.getPlayerLanguage(this.player);
                        if (!this.hasNoArena()) break block18;
                        if (BedWars.getServerType() == ServerType.SHARED) {
                            this.headerFooter = null;
                            return;
                        }
                        headerPath = Messages.FORMATTING_SB_TAB_LOBBY_HEADER;
                        footerPath = Messages.FORMATTING_SB_TAB_LOBBY_FOOTER;
                        break block19;
                    }
                    if (!this.arena.isSpectator(this.player)) break block20;
                    exTeam = this.arena.getExTeam(this.player.getUniqueId());
                    if (null != exTeam) break block21;
                    switch (this.arena.getStatus()) {
                        case waiting: {
                            headerPath = Messages.FORMATTING_SB_TAB_WAITING_HEADER_SPEC;
                            footerPath = Messages.FORMATTING_SB_TAB_WAITING_FOOTER_SPEC;
                            break block19;
                        }
                        case starting: {
                            headerPath = Messages.FORMATTING_SB_TAB_STARTING_HEADER_SPEC;
                            footerPath = Messages.FORMATTING_SB_TAB_STARTING_FOOTER_SPEC;
                            break block19;
                        }
                        case playing: {
                            headerPath = Messages.FORMATTING_SB_TAB_PLAYING_SPEC_HEADER;
                            footerPath = Messages.FORMATTING_SB_TAB_PLAYING_SPEC_FOOTER;
                            break block19;
                        }
                        case restarting: {
                            headerPath = Messages.FORMATTING_SB_TAB_RESTARTING_SPEC_HEADER;
                            footerPath = Messages.FORMATTING_SB_TAB_RESTARTING_SPEC_FOOTER;
                            break block19;
                        }
                        default: {
                            throw new IllegalStateException("Unhandled arena status");
                        }
                    }
                }
                if (this.arena.getStatus() == GameState.restarting) {
                    if (null != this.arena.getWinner() && this.arena.getWinner().equals(exTeam)) {
                        headerPath = Messages.FORMATTING_SB_TAB_RESTARTING_WIN2_HEADER;
                        footerPath = Messages.FORMATTING_SB_TAB_RESTARTING_WIN2_FOOTER;
                    } else {
                        headerPath = Messages.FORMATTING_SB_TAB_RESTARTING_ELM_HEADER;
                        footerPath = Messages.FORMATTING_SB_TAB_RESTARTING_ELM_FOOTER;
                    }
                } else {
                    headerPath = Messages.FORMATTING_SB_TAB_PLAYING_ELM_HEADER;
                    footerPath = Messages.FORMATTING_SB_TAB_PLAYING_ELM_FOOTER;
                }
                break block19;
            }
            switch (this.arena.getStatus()) {
                case waiting: {
                    headerPath = Messages.FORMATTING_SB_TAB_WAITING_HEADER;
                    footerPath = Messages.FORMATTING_SB_TAB_WAITING_FOOTER;
                    break;
                }
                case starting: {
                    headerPath = Messages.FORMATTING_SB_TAB_STARTING_HEADER;
                    footerPath = Messages.FORMATTING_SB_TAB_STARTING_FOOTER;
                    break;
                }
                case playing: {
                    headerPath = Messages.FORMATTING_SB_TAB_PLAYING_HEADER;
                    footerPath = Messages.FORMATTING_SB_TAB_PLAYING_FOOTER;
                    break;
                }
                case restarting: {
                    headerPath = Messages.FORMATTING_SB_TAB_RESTARTING_WIN1_HEADER;
                    footerPath = Messages.FORMATTING_SB_TAB_RESTARTING_WIN1_FOOTER;
                    break;
                }
                default: {
                    throw new IllegalStateException("Unhandled arena status");
                }
            }
        }
        this.headerFooter = new TabHeaderFooter((LinkedList<SidebarLine>)this.normalizeLines((List)lang.l(headerPath)), (LinkedList<SidebarLine>)this.normalizeLines((List)lang.l(footerPath)), this.getPlaceholders(this.getPlayer()));
        SidebarManager.getInstance().sendHeaderFooter(this.player, this.headerFooter);
    }

    @Override
    public boolean registerPersistentPlaceholder(PlaceholderProvider placeholderProvider) {
        this.persistentProviders.add(placeholderProvider);
        return true;
    }

    public void handleInvisibilityPotion(@NotNull Player player, boolean _toggle) {
        if (null == this.arena) {
            throw new RuntimeException("This can only be used when the player is in arena");
        }
        this.giveUpdateTabFormat(player, false);
    }

    @Override
    public Sidebar getHandle() {
        return this.handle;
    }

    @Override
    public IArena getArena() {
        return this.arena;
    }

    @Nullable
    public TabHeaderFooter getHeaderFooter() {
        return this.headerFooter;
    }

    public void setHeaderFooter(@Nullable TabHeaderFooter headerFooter) {
        this.headerFooter = headerFooter;
    }

    public void setTopStatistics(@Nullable StatisticsOrdered topStatistics) {
        this.topStatistics = topStatistics;
    }
}

