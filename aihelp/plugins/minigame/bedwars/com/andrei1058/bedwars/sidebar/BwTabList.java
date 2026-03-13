/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffectType
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.sidebar;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.libs.sidebar.PlayerTab;
import com.andrei1058.bedwars.libs.sidebar.Sidebar;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarLineAnimated;
import com.andrei1058.bedwars.sidebar.BwSidebar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BwTabList {
    private static final char SPECTATOR_PREFIX = 'z';
    private static final char ELIMINATED_FROM_TEAM_PREFIX = 'z';
    private final HashMap<UUID, PlayerTab> deployedPerPlayerTabList = new HashMap();
    private final HashMap<UUID, String> teamOrderPrefix = new HashMap();
    private int teamOrderIndex = 0;
    private final HashMap<UUID, String> playerTabIdentifier = new HashMap();
    private final HashMap<String, Integer> playerTabIdentifierDuplication = new HashMap();
    private final BwSidebar sidebar;

    public BwTabList(BwSidebar sidebar) {
        this.sidebar = sidebar;
    }

    void handlePlayerList() {
        if (null != this.sidebar.getHandle()) {
            this.deployedPerPlayerTabList.clear();
            this.sidebar.getHandle().removeTabs();
        }
        this.handleHealthIcon();
        if (this.isTabFormattingDisabled()) {
            return;
        }
        if (null == this.sidebar.getArena()) {
            if (BedWars.config.getBoolean("scoreboard-settings.player-list.format-lobby-list") && !BedWars.config.getLobbyWorldName().trim().isEmpty()) {
                World lobby = Bukkit.getWorld((String)BedWars.config.getLobbyWorldName());
                if (null == lobby) {
                    return;
                }
                lobby.getPlayers().forEach(inLobby -> this.giveUpdateTabFormat((Player)inLobby, true, null));
            }
            this.giveUpdateTabFormat(this.sidebar.getPlayer(), true, null);
            return;
        }
        this.handleHealthIcon();
        this.sidebar.getArena().getPlayers().forEach(playing -> this.giveUpdateTabFormat((Player)playing, true, null));
        this.sidebar.getArena().getSpectators().forEach(spectating -> this.giveUpdateTabFormat((Player)spectating, true, null));
    }

    public void handleHealthIcon() {
        SidebarLine line;
        if (null == this.sidebar.getHandle()) {
            return;
        }
        if (null == this.sidebar.getArena()) {
            this.sidebar.getHandle().hidePlayersHealth();
            return;
        }
        if (this.sidebar.getArena().getStatus() != GameState.playing) {
            this.sidebar.getHandle().hidePlayersHealth();
            return;
        }
        List<String> animation = Language.getList(this.sidebar.getPlayer(), Messages.FORMATTING_SCOREBOARD_HEALTH);
        if (animation.isEmpty()) {
            return;
        }
        if (animation.size() > 1) {
            String[] lines = new String[animation.size()];
            for (int i = 0; i < animation.size(); ++i) {
                lines[i] = animation.get(i);
            }
            line = new SidebarLineAnimated(lines);
        } else {
            final String text = animation.get(0);
            line = new SidebarLine(){

                @Override
                @NotNull
                public String getLine() {
                    return text;
                }
            };
        }
        if (BedWars.config.getBoolean("scoreboard-settings.health.enable")) {
            this.sidebar.getHandle().showPlayersHealth(line, BedWars.config.getBoolean("scoreboard-settings.health.display-in-tab"));
        }
        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
            if (null != this.sidebar.getArena() && null != this.sidebar.getHandle()) {
                this.sidebar.getArena().getPlayers().forEach(player -> this.sidebar.getHandle().setPlayerHealth((Player)player, (int)Math.ceil(player.getHealth())));
                if (this.sidebar.getArena().isSpectator(this.sidebar.getPlayer())) {
                    this.sidebar.getArena().getSpectators().forEach(player -> this.sidebar.getHandle().setPlayerHealth((Player)player, (int)Math.ceil(player.getHealth())));
                }
            }
        }, 10L);
    }

    public boolean isTabFormattingDisabled() {
        if (null == this.sidebar.getArena()) {
            if (BedWars.getServerType() == ServerType.SHARED && BedWars.config.getBoolean("scoreboard-settings.player-list.format-lobby-list") && !BedWars.config.getLobbyWorldName().trim().isEmpty()) {
                World lobby = Bukkit.getWorld((String)BedWars.config.getLobbyWorldName());
                return null == lobby || !this.sidebar.getPlayer().getWorld().getName().equals(lobby.getName());
            }
            return !BedWars.config.getBoolean("scoreboard-settings.player-list.format-lobby-list");
        }
        GameState status = this.sidebar.getArena().getStatus();
        if (status == GameState.playing) {
            return !BedWars.config.getBoolean("scoreboard-settings.player-list.format-playing-list");
        }
        if (status == GameState.starting) {
            return !BedWars.config.getBoolean("scoreboard-settings.player-list.format-starting-list");
        }
        if (status == GameState.waiting) {
            return !BedWars.config.getBoolean("scoreboard-settings.player-list.format-waiting-list");
        }
        return status != GameState.restarting || !BedWars.config.getBoolean("scoreboard-settings.player-list.format-restarting-list");
    }

    public void giveUpdateTabFormat(@NotNull Player player, boolean skipStateCheck, @Nullable Boolean spectator) {
        if (this.sidebar.getHandle() == null) {
            return;
        }
        String playerTabId = this.getCreatePlayerTabIdentifier(player);
        PlayerTab playerTab = this.deployedPerPlayerTabList.getOrDefault(player.getUniqueId(), null);
        if (null != playerTab) {
            this.sidebar.getHandle().removeTab(playerTab.getIdentifier());
            this.deployedPerPlayerTabList.remove(player.getUniqueId());
        }
        if (!skipStateCheck && this.isTabFormattingDisabled()) {
            return;
        }
        IArena arena = this.sidebar.getArena();
        Sidebar handle = this.sidebar.getHandle();
        if (null == arena) {
            SidebarLine prefix = this.getTabText(Messages.FORMATTING_SB_TAB_LOBBY_PREFIX, player, null);
            SidebarLine suffix = this.getTabText(Messages.FORMATTING_SB_TAB_LOBBY_SUFFIX, player, null);
            PlayerTab tab = handle.playerTabCreate(playerTabId, player, prefix, suffix, PlayerTab.PushingRule.NEVER, this.sidebar.getPlaceholders(player));
            this.deployedPerPlayerTabList.put(player.getUniqueId(), tab);
            return;
        }
        if (arena.isSpectator(player) || spectator != null && spectator.booleanValue()) {
            SidebarLine suffix;
            SidebarLine prefix;
            ITeam exTeam = arena.getExTeam(player.getUniqueId());
            if (null != exTeam) {
                SidebarLine suffix2;
                SidebarLine prefix2;
                HashMap<String, String> replacements = this.getTeamReplacements(exTeam);
                if (arena.getStatus() == GameState.restarting && null != arena.getWinner()) {
                    if (arena.getWinner().equals(exTeam)) {
                        prefix2 = this.getTabText(Messages.FORMATTING_SB_TAB_RESTARTING_WIN2_PREFIX, player, replacements);
                        suffix2 = this.getTabText(Messages.FORMATTING_SB_TAB_RESTARTING_WIN2_SUFFIX, player, replacements);
                    } else {
                        prefix2 = this.getTabText(Messages.FORMATTING_SB_TAB_RESTARTING_ELM_PREFIX, player, replacements);
                        suffix2 = this.getTabText(Messages.FORMATTING_SB_TAB_RESTARTING_ELM_SUFFIX, player, replacements);
                    }
                } else {
                    prefix2 = this.getTabText(Messages.FORMATTING_SB_TAB_PLAYING_ELM_PREFIX, player, replacements);
                    suffix2 = this.getTabText(Messages.FORMATTING_SB_TAB_PLAYING_ELM_SUFFIX, player, replacements);
                }
                PlayerTab tab = handle.playerTabCreate(this.getPlayerTabIdentifierEliminatedInTeam(exTeam, playerTabId), player, prefix2, suffix2, PlayerTab.PushingRule.NEVER, this.sidebar.getPlaceholders(player));
                this.deployedPerPlayerTabList.put(player.getUniqueId(), tab);
                return;
            }
            switch (arena.getStatus()) {
                case waiting: {
                    prefix = this.getTabText(Messages.FORMATTING_SB_TAB_WAITING_PREFIX_SPEC, player, null);
                    suffix = this.getTabText(Messages.FORMATTING_SB_TAB_WAITING_SUFFIX_SPEC, player, null);
                    break;
                }
                case starting: {
                    prefix = this.getTabText(Messages.FORMATTING_SB_TAB_STARTING_PREFIX_SPEC, player, null);
                    suffix = this.getTabText(Messages.FORMATTING_SB_TAB_STARTING_SUFFIX_SPEC, player, null);
                    break;
                }
                case playing: {
                    prefix = this.getTabText(Messages.FORMATTING_SB_TAB_PLAYING_SPEC_PREFIX, player, null);
                    suffix = this.getTabText(Messages.FORMATTING_SB_TAB_PLAYING_SPEC_SUFFIX, player, null);
                    break;
                }
                case restarting: {
                    prefix = this.getTabText(Messages.FORMATTING_SB_TAB_RESTARTING_SPEC_PREFIX, player, null);
                    suffix = this.getTabText(Messages.FORMATTING_SB_TAB_RESTARTING_SPEC_SUFFIX, player, null);
                    break;
                }
                default: {
                    throw new RuntimeException("Unhandled game state..");
                }
            }
            PlayerTab tab = handle.playerTabCreate(this.getPlayerTabIdentifierSpectator(null, playerTabId), player, prefix, suffix, PlayerTab.PushingRule.NEVER, this.sidebar.getPlaceholders(player));
            this.deployedPerPlayerTabList.put(player.getUniqueId(), tab);
            return;
        }
        GameState status = arena.getStatus();
        if (status != GameState.playing) {
            SidebarLine suffix;
            SidebarLine prefix;
            String currentTabId = playerTabId;
            switch (status) {
                case waiting: {
                    prefix = this.getTabText(Messages.FORMATTING_SB_TAB_WAITING_PREFIX, player, null);
                    suffix = this.getTabText(Messages.FORMATTING_SB_TAB_WAITING_SUFFIX, player, null);
                    break;
                }
                case starting: {
                    prefix = this.getTabText(Messages.FORMATTING_SB_TAB_STARTING_PREFIX, player, null);
                    suffix = this.getTabText(Messages.FORMATTING_SB_TAB_STARTING_SUFFIX, player, null);
                    break;
                }
                case restarting: {
                    ITeam team = arena.getTeam(player);
                    currentTabId = this.getPlayerTabIdentifierAliveInTeam(team, playerTabId);
                    HashMap<String, String> replacements = this.getTeamReplacements(team);
                    prefix = this.getTabText(Messages.FORMATTING_SB_TAB_RESTARTING_WIN1_PREFIX, player, replacements);
                    suffix = this.getTabText(Messages.FORMATTING_SB_TAB_RESTARTING_WIN1_SUFFIX, player, replacements);
                    break;
                }
                default: {
                    throw new IllegalStateException("Unhandled game status!");
                }
            }
            PlayerTab t = handle.playerTabCreate(currentTabId, player, prefix, suffix, PlayerTab.PushingRule.NEVER, this.sidebar.getPlaceholders(player));
            this.deployedPerPlayerTabList.put(player.getUniqueId(), t);
            return;
        }
        ITeam team = arena.getTeam(player);
        HashMap<String, String> replacements = this.getTeamReplacements(team);
        SidebarLine prefix = this.getTabText(Messages.FORMATTING_SB_TAB_PLAYING_PREFIX, player, replacements);
        SidebarLine suffix = this.getTabText(Messages.FORMATTING_SB_TAB_PLAYING_SUFFIX, player, replacements);
        PlayerTab teamTab = handle.playerTabCreate(this.getPlayerTabIdentifierAliveInTeam(team, playerTabId), player, prefix, suffix, PlayerTab.PushingRule.PUSH_OTHER_TEAMS, this.sidebar.getPlaceholders(player));
        this.deployedPerPlayerTabList.put(player.getUniqueId(), teamTab);
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            teamTab.setNameTagVisibility(PlayerTab.NameTagVisibility.NEVER);
        }
    }

    @NotNull
    private SidebarLine getTabText(String path, Player targetPlayer, @Nullable HashMap<String, String> replacements) {
        List<String> strings = Language.getList(this.sidebar.getPlayer(), path);
        if (strings.isEmpty()) {
            return new SidebarLine(){

                @Override
                @NotNull
                public String getLine() {
                    return "";
                }
            };
        }
        strings = new ArrayList<String>();
        for (String string : Language.getList(this.sidebar.getPlayer(), path)) {
            String parsed = string.replace("{vPrefix}", BedWars.getChatSupport().getPrefix(targetPlayer)).replace("{vSuffix}", BedWars.getChatSupport().getSuffix(targetPlayer));
            if (null != replacements) {
                for (Map.Entry<String, String> entry : replacements.entrySet()) {
                    parsed = parsed.replace(entry.getKey(), entry.getValue());
                }
            }
            strings.add(parsed);
        }
        if (strings.size() == 1) {
            final String line = strings.get(0);
            return new SidebarLine(){

                @Override
                @NotNull
                public String getLine() {
                    return line;
                }
            };
        }
        String[] lines = new String[strings.size()];
        for (int i = 0; i < lines.length; ++i) {
            lines[i] = strings.get(i);
        }
        return new SidebarLineAnimated(lines);
    }

    private String getCreateTeamTabOrderPrefix(@NotNull ITeam team) {
        Object prefix = this.teamOrderPrefix.getOrDefault(team.getIdentity(), null);
        if (null == prefix) {
            ++this.teamOrderIndex;
            prefix = "" + this.teamOrderIndex;
            this.teamOrderPrefix.put(team.getIdentity(), (String)prefix);
            if (((String)prefix).length() > 3) {
                throw new RuntimeException("Could not generate new order prefixes. Char limit exceeded. Max value is 999.");
            }
        }
        return prefix;
    }

    private String getCreatePlayerTabIdentifier(@NotNull Player player) {
        Object id = this.playerTabIdentifier.getOrDefault(player.getUniqueId(), null);
        if (null == id) {
            id = player.getName().substring(0, Math.min(player.getName().length(), 9));
            if (this.hasPlayerIdentifier((String)id)) {
                Integer lastDuplicationIndex;
                Integer n = lastDuplicationIndex = this.playerTabIdentifierDuplication.getOrDefault(id, 0);
                lastDuplicationIndex = lastDuplicationIndex + 1;
                id = (String)id + lastDuplicationIndex.toString();
                this.playerTabIdentifierDuplication.put((String)id, lastDuplicationIndex);
            }
            this.playerTabIdentifier.put(player.getUniqueId(), (String)id);
        }
        return id;
    }

    private boolean hasPlayerIdentifier(@NotNull String id) {
        for (String string : this.playerTabIdentifier.values()) {
            if (!string.equals(id)) continue;
            return true;
        }
        return false;
    }

    @NotNull
    private String getPlayerTabIdentifierAliveInTeam(ITeam team, Player player) {
        return this.getPlayerTabIdentifierAliveInTeam(team, this.getCreatePlayerTabIdentifier(player));
    }

    @NotNull
    private String getPlayerTabIdentifierAliveInTeam(ITeam team, String playerId) {
        return this.getCreateTeamTabOrderPrefix(team) + playerId;
    }

    @NotNull
    private String getPlayerTabIdentifierEliminatedInTeam(ITeam team, Player player) {
        return this.getPlayerTabIdentifierEliminatedInTeam(team, this.getCreatePlayerTabIdentifier(player));
    }

    @NotNull
    private String getPlayerTabIdentifierEliminatedInTeam(ITeam team, String playerId) {
        return "z" + this.getCreateTeamTabOrderPrefix(team) + playerId;
    }

    @NotNull
    private String getPlayerTabIdentifierSpectator(@Nullable ITeam team, Player player) {
        return this.getPlayerTabIdentifierSpectator(team, this.getCreatePlayerTabIdentifier(player));
    }

    @NotNull
    private String getPlayerTabIdentifierSpectator(@Nullable ITeam team, String playerId) {
        if (null == team) {
            return "z" + playerId;
        }
        return this.getPlayerTabIdentifierEliminatedInTeam(team, playerId);
    }

    @NotNull
    HashMap<String, String> getTeamReplacements(@Nullable ITeam team) {
        HashMap<String, String> replacements = new HashMap<String, String>();
        String displayName = null == team ? "" : team.getDisplayName(Language.getPlayerLanguage(this.sidebar.getPlayer()));
        replacements.put("{teamName}", displayName);
        replacements.put("{teamLetter}", (String)(null == team ? "" : String.valueOf(team.getColor().chat()) + displayName.substring(0, 1)));
        replacements.put("{teamColor}", null == team ? "" : team.getColor().chat().toString());
        return replacements;
    }

    public void onSidebarRemoval() {
        this.sidebar.getHandle().clearLines();
        this.deployedPerPlayerTabList.clear();
        this.playerTabIdentifier.clear();
        this.playerTabIdentifierDuplication.clear();
        this.teamOrderPrefix.clear();
        this.teamOrderIndex = 0;
    }
}

