/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 */
package nl.sbdeveloper.vehiclesplus.locale;

import co.aikar.commands.MessageType;
import co.aikar.locales.MessageKeyProvider;
import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.handlers.ACFHandler;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessageKeyProvider;

public class Locale {
    private Locale() {
    }

    public static String getMessage(PluginMessage pluginMessage) {
        return Locale.getMessage(new PluginMessageKeyProvider(pluginMessage));
    }

    public static String getMessage(MessageKeyProvider messageKeyProvider) {
        String string = ACFHandler.getManager().formatMessage(null, MessageType.INFO, messageKeyProvider, new String[0]);
        return ChatColor.translateAlternateColorCodes((char)'&', (String)string);
    }

    public static String getMessage(PluginMessage pluginMessage, Map<String, String> map) {
        return Locale.getMessage(new PluginMessageKeyProvider(pluginMessage), map);
    }

    public static String getMessage(MessageKeyProvider messageKeyProvider, Map<String, String> map) {
        String[] stringArray = new String[map.size() * 2];
        int n = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            stringArray[n] = entry.getKey();
            stringArray[n + 1] = entry.getValue();
            n += 2;
        }
        String string = ChatColor.translateAlternateColorCodes((char)'&', (String)ACFHandler.getManager().formatMessage(null, MessageType.INFO, messageKeyProvider, stringArray));
        if (!messageKeyProvider.getMessageKey().getKey().contains("ACTIONBAR_NORMAL") && string.matches(".*%\\w+%.*")) {
            VehiclesPlus.getInstance().getLogger().warning("Message still contains variables: " + (String)string);
        }
        return string;
    }
}

