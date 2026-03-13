/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.listeners.chat;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.commands.shout.ShoutCommand;
import com.andrei1058.bedwars.configuration.Permissions;
import com.andrei1058.bedwars.support.papi.SupportPAPI;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.Nullable;

public class ChatFormatting
implements Listener {
    @EventHandler(ignoreCancelled=true)
    public void onChat(AsyncPlayerChatEvent e) {
        if (e == null) {
            return;
        }
        Player p = e.getPlayer();
        if (BedWars.getServerType() == ServerType.SHARED && Arena.getArenaByPlayer(p) == null) {
            e.getRecipients().removeIf(pl -> Arena.getArenaByPlayer(pl) != null);
            return;
        }
        if (Permissions.hasPermission(p, Permissions.PERMISSION_CHAT_COLOR, Permissions.PERMISSION_VIP, Permissions.PERMISSION_ALL)) {
            e.setMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)e.getMessage()));
        }
        Language language = Language.getPlayerLanguage(p);
        if (BedWars.getServerType() == ServerType.MULTIARENA && p.getWorld().getName().equalsIgnoreCase(BedWars.getLobbyWorld())) {
            ChatFormatting.setRecipients(e, p.getWorld().getPlayers());
        }
        if (Arena.getArenaByPlayer(p) != null) {
            IArena a = Arena.getArenaByPlayer(p);
            if (a.isSpectator(p)) {
                ChatFormatting.setRecipients(e, a.getSpectators());
                e.setFormat(ChatFormatting.parsePHolders(language.m(Messages.FORMATTING_CHAT_SPECTATOR), p, null));
                return;
            }
            if (a.getStatus() == GameState.waiting || a.getStatus() == GameState.starting) {
                ChatFormatting.setRecipients(e, a.getPlayers());
                e.setFormat(ChatFormatting.parsePHolders(language.m(Messages.FORMATTING_CHAT_WAITING), p, null));
                return;
            }
            ITeam team = a.getTeam(p);
            String msg = e.getMessage();
            if (ChatFormatting.isShouting(msg, language)) {
                if (!p.hasPermission(Permissions.PERMISSION_SHOUT_COMMAND) && !p.hasPermission(Permissions.PERMISSION_ALL)) {
                    e.setCancelled(true);
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_NOT_FOUND_OR_INSUFF_PERMS));
                    return;
                }
                if (ShoutCommand.isShoutCooldown(p)) {
                    e.setCancelled(true);
                    p.sendMessage(language.m(Messages.COMMAND_COOLDOWN).replace("{seconds}", String.valueOf(Math.round(ShoutCommand.getShoutCooldown(p)))));
                    return;
                }
                ShoutCommand.updateShout(p);
                ChatFormatting.setRecipients(e, a.getPlayers(), a.getSpectators());
                msg = ChatFormatting.clearShout(msg, language);
                if (msg.isEmpty()) {
                    e.setCancelled(true);
                    return;
                }
                e.setMessage(msg);
                e.setFormat(ChatFormatting.parsePHolders(language.m(Messages.FORMATTING_CHAT_SHOUT), p, team));
                return;
            }
            if (a.getMaxInTeam() == 1) {
                ChatFormatting.setRecipients(e, a.getPlayers(), a.getSpectators());
            } else {
                ChatFormatting.setRecipients(e, team.getMembers());
            }
            e.setFormat(ChatFormatting.parsePHolders(language.m(Messages.FORMATTING_CHAT_TEAM), p, team));
            return;
        }
        e.setFormat(ChatFormatting.parsePHolders(language.m(Messages.FORMATTING_CHAT_LOBBY), p, null));
    }

    private static String parsePHolders(String content, Player player, @Nullable ITeam team) {
        content = content.replace("{vPrefix}", BedWars.getChatSupport().getPrefix(player)).replace("{vSuffix}", BedWars.getChatSupport().getSuffix(player)).replace("{playername}", player.getName()).replace("{level}", BedWars.getLevelSupport().getLevel(player)).replace("{player}", player.getDisplayName());
        if (team != null) {
            String teamFormat = Language.getMsg(player, Messages.FORMAT_PAPI_PLAYER_TEAM_TEAM).replace("{TeamColor}", String.valueOf(team.getColor().chat())).replace("{TeamName}", team.getDisplayName(Language.getPlayerLanguage(player)).toUpperCase());
            content = content.replace("{team}", teamFormat);
        }
        return SupportPAPI.getSupportPAPI().replace(player, content).replace("{message}", "%2$s");
    }

    private static boolean isShouting(String msg, Language lang) {
        return msg.startsWith("!") || msg.startsWith("shout") || msg.startsWith("SHOUT") || msg.startsWith(lang.m(Messages.MEANING_SHOUT));
    }

    private static String clearShout(String msg, Language lang) {
        if (msg.startsWith("!")) {
            msg = msg.replaceFirst("!", "");
        }
        if (msg.startsWith("SHOUT")) {
            msg = msg.replaceFirst("SHOUT", "");
        }
        if (msg.startsWith("shout")) {
            msg = msg.replaceFirst("shout", "");
        }
        if (msg.startsWith(lang.m(Messages.MEANING_SHOUT))) {
            msg = msg.replaceFirst(lang.m(Messages.MEANING_SHOUT), "");
        }
        return msg.trim();
    }

    @SafeVarargs
    public static void setRecipients(AsyncPlayerChatEvent event, List<Player> ... target) {
        if (!BedWars.config.getBoolean("chat-settings.global")) {
            event.getRecipients().clear();
            for (List<Player> list : target) {
                event.getRecipients().addAll(list);
            }
        }
    }
}

