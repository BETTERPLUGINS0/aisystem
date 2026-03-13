/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.upgrades.upgradeaction;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.upgrades.UpgradeAction;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class DispatchCommand
implements UpgradeAction {
    private final CommandType commandType;
    private final String command;

    public DispatchCommand(CommandType commandType, String command) {
        this.commandType = commandType;
        this.command = command;
    }

    @Override
    public void onBuy(@Nullable Player player, ITeam team) {
        String buyerName = player == null ? "null" : player.getName();
        String buyerUUID = player == null ? "null" : player.getUniqueId().toString();
        String teamName = team.getName();
        String teamDisplay = team.getDisplayName(Language.getDefaultLanguage());
        String teamColor = team.getColor().chat().toString();
        String arenaIdentifier = team.getArena().getArenaName();
        String arenaWorld = team.getArena().getWorldName();
        String arenaDisplay = team.getArena().getDisplayName();
        String arenaGroup = team.getArena().getGroup();
        this.commandType.dispatch(team, this.command.replace("{buyer}", buyerName).replace("{buyer_uuid}", buyerUUID).replace("{team}", teamName).replace("{team_display}", teamDisplay).replace("{team_color}", teamColor).replace("{arena}", arenaIdentifier).replace("{arena_world}", arenaWorld).replace("{arena_display}", arenaDisplay).replace("{arena_group}", arenaGroup));
    }

    public static enum CommandType {
        ONCE_AS_CONSOLE,
        FOREACH_MEMBER_AS_CONSOLE,
        FOREACH_MEMBER_AS_PLAYER;


        private void dispatch(ITeam team, String command) {
            switch (this.ordinal()) {
                case 0: {
                    if (((String)command).startsWith("/")) {
                        command = ((String)command).replaceFirst("/", "");
                    }
                    Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)command);
                    break;
                }
                case 1: {
                    if (((String)command).startsWith("/")) {
                        command = ((String)command).replaceFirst("/", "");
                    }
                    for (Player player : team.getMembers()) {
                        String playerName = player.getName();
                        String playerUUID = player.getUniqueId().toString();
                        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)((String)command).replace("{player}", playerName).replace("{player_uuid}", playerUUID));
                    }
                    break;
                }
                case 2: {
                    if (!((String)command).startsWith("/")) {
                        command = "/" + (String)command;
                    }
                    for (Player player : team.getMembers()) {
                        String playerName = player.getName();
                        String playerUUID = player.getUniqueId().toString();
                        player.chat(((String)command).replace("{player}", playerName).replace("{player_uuid}", playerUUID));
                    }
                    break;
                }
            }
        }
    }
}

