/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.locale;

import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;

public class PluginMessageKeyProvider
implements MessageKeyProvider {
    private final MessageKey key;

    PluginMessageKeyProvider(PluginMessage pluginMessage) {
        this.key = MessageKey.of(PluginMessageKeyProvider.convertConstantToLanguageKey("vehiclesplus." + pluginMessage.name()));
    }

    @Override
    public MessageKey getMessageKey() {
        return this.key;
    }

    private static String convertConstantToLanguageKey(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] stringArray = string.split("_");
        for (int i = 0; i < stringArray.length; ++i) {
            String string2 = stringArray[i].toLowerCase();
            if (i > 0) {
                stringBuilder.append(".");
            }
            stringBuilder.append(string2);
        }
        return stringBuilder.toString();
    }
}

