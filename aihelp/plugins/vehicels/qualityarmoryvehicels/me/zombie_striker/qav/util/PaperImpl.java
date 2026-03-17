/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util;

import java.lang.reflect.Method;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.StyleSetter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public class PaperImpl {
    private static Method sendMessage;

    public static boolean sendComponent(CommandSender commandSender, String string, @Nullable String string2, @Nullable String string3) {
        if (sendMessage == null) {
            return false;
        }
        try {
            StyleSetter<Component> styleSetter = Component.text(string);
            if (string2 != null) {
                styleSetter = styleSetter.hoverEvent(HoverEvent.showText(Component.text(string2)));
            }
            if (string3 != null) {
                styleSetter = styleSetter.clickEvent(ClickEvent.openUrl(string3));
            }
            sendMessage.invoke(commandSender, styleSetter);
            return true;
        } catch (Throwable throwable) {
            return false;
        }
    }

    static {
        try {
            sendMessage = CommandSender.class.getMethod("sendMessage", Component.class);
        } catch (Throwable throwable) {
            sendMessage = null;
        }
    }
}

