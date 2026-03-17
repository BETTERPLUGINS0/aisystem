/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.zombie_striker.qav.debugmanager;

import java.util.ArrayList;
import java.util.List;
import me.zombie_striker.qav.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugManager {
    private static final List<Player> playerRecievers = new ArrayList<Player>();
    private static boolean displayInConsole = true;
    private static final String debugPrefix = Main.prefix + ChatColor.GREEN + " [DEBUG]" + ChatColor.WHITE + " ";

    public static void sendDebugMessages(String string) {
        if (Main.debug && displayInConsole) {
            Bukkit.getConsoleSender().sendMessage(debugPrefix + string);
        }
        if (!Main.debug && !Main.debugWithCommand) {
            return;
        }
        if (playerRecievers.size() > 0) {
            boolean bl = false;
            for (Player player2 : playerRecievers) {
                if (!player2.isOnline()) {
                    bl = true;
                    continue;
                }
                player2.sendMessage(debugPrefix + string);
            }
            if (bl) {
                playerRecievers.removeIf(player -> !player.isOnline());
            }
        }
    }

    public static void addReciever(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            playerRecievers.add((Player)commandSender);
        }
    }

    public static void removeReciever(CommandSender commandSender) {
        playerRecievers.remove(commandSender);
    }

    public static boolean toggleReciever(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            if (playerRecievers.contains(commandSender)) {
                DebugManager.removeReciever(commandSender);
                return false;
            }
            DebugManager.addReciever(commandSender);
            return true;
        }
        return false;
    }

    public static void setShouldDisplayInConsole(boolean bl) {
        displayInConsole = bl;
    }
}

