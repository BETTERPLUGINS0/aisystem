/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.NamespacedKey
 */
package co.aikar.commands;

import co.aikar.commands.ACFPatterns;
import co.aikar.commands.BukkitCommandContexts;
import org.bukkit.NamespacedKey;

class BukkitCommandContexts_1_12 {
    BukkitCommandContexts_1_12() {
    }

    static void register(BukkitCommandContexts bukkitCommandContexts) {
        bukkitCommandContexts.registerContext(NamespacedKey.class, bukkitCommandExecutionContext -> {
            String string = bukkitCommandExecutionContext.popFirstArg();
            String[] stringArray = ACFPatterns.COLON.split(string, 2);
            if (stringArray.length == 1) {
                String string2 = bukkitCommandExecutionContext.getFlagValue("namespace", (String)null);
                if (string2 == null) {
                    return NamespacedKey.minecraft((String)stringArray[0]);
                }
                return new NamespacedKey(string2, stringArray[0]);
            }
            return new NamespacedKey(stringArray[0], stringArray[1]);
        });
    }
}

