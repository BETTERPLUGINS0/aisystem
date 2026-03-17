/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util;

import java.lang.reflect.Method;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.util.PaperImpl;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.Nullable;

public class ForksUtil {
    private static boolean paper = false;
    private static Method getCause;

    public static void init() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            paper = true;
        } catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        if (ForksUtil.isFork()) {
            try {
                getCause = PlayerKickEvent.class.getDeclaredMethod("getCause", new Class[0]);
            } catch (NoSuchMethodException noSuchMethodException) {
                getCause = null;
            }
            QualityArmoryVehicles.getPlugin().getLogger().info("Found paper. Loaded support.");
        }
    }

    public static boolean isFlyKick(PlayerKickEvent playerKickEvent) {
        if (paper && getCause != null) {
            try {
                return getCause.invoke(playerKickEvent, new Object[0]).toString().equals("FLYING_PLAYER");
            } catch (Throwable throwable) {
                // empty catch block
            }
        }
        return playerKickEvent.getReason().equals("Flying is not enabled on this server");
    }

    public static void sendComponent(CommandSender commandSender, String string, @Nullable String string2, @Nullable String string3) {
        string = ChatColor.translateAlternateColorCodes((char)'&', (String)string);
        if (string2 != null) {
            string2 = ChatColor.translateAlternateColorCodes((char)'&', (String)string2);
        }
        try {
            if (paper && PaperImpl.sendComponent(commandSender, string, string2, string3)) {
                return;
            }
        } catch (Throwable throwable) {
            // empty catch block
        }
        TextComponent textComponent = new TextComponent(string);
        if (string2 != null) {
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (BaseComponent[])new TextComponent[]{new TextComponent(string2)}));
        }
        if (string3 != null) {
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, string3));
        }
        commandSender.spigot().sendMessage((BaseComponent)textComponent);
    }

    public static boolean isFork() {
        return paper;
    }
}

