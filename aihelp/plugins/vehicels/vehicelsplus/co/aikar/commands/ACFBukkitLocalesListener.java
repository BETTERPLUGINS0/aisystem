/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerLocaleChangeEvent
 */
package co.aikar.commands;

import co.aikar.commands.ACFBukkitUtil;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.LogLevel;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Locale;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

class ACFBukkitLocalesListener
implements Listener {
    private final BukkitCommandManager manager;
    private MethodHandle localeMethod1_8 = null;
    private boolean checkedLocaleMethod1_8 = false;

    ACFBukkitLocalesListener(BukkitCommandManager bukkitCommandManager) {
        this.manager = bukkitCommandManager;
    }

    @EventHandler
    void onLocaleChange(PlayerLocaleChangeEvent playerLocaleChangeEvent) {
        if (!this.manager.autoDetectFromClient) {
            return;
        }
        Player player = playerLocaleChangeEvent.getPlayer();
        Locale locale = null;
        try {
            locale = playerLocaleChangeEvent.locale();
        } catch (NoSuchMethodError noSuchMethodError) {
            try {
                if (!playerLocaleChangeEvent.getLocale().equals(this.manager.issuersLocaleString.get(player.getUniqueId()))) {
                    locale = ACFBukkitUtil.stringToLocale(playerLocaleChangeEvent.getLocale());
                }
            } catch (NoSuchMethodError noSuchMethodError2) {
                try {
                    Object object;
                    if (!this.checkedLocaleMethod1_8) {
                        this.checkedLocaleMethod1_8 = true;
                        object = MethodHandles.lookup();
                        MethodType methodType = MethodType.methodType(String.class);
                        this.localeMethod1_8 = ((MethodHandles.Lookup)object).findVirtual(PlayerLocaleChangeEvent.class, "getNewLocale", methodType);
                    }
                    if (this.localeMethod1_8 != null && !((String)(object = this.localeMethod1_8.invoke(playerLocaleChangeEvent))).equals(this.manager.issuersLocaleString.get(player.getUniqueId()))) {
                        locale = ACFBukkitUtil.stringToLocale((String)object);
                    }
                } catch (Throwable throwable) {
                    this.manager.log(LogLevel.ERROR, "Error registering MethodHandle for LocaleChangeEvent", throwable);
                }
            }
        }
        if (locale == null) {
            return;
        }
        this.manager.setPlayerLocale(player, locale);
    }
}

