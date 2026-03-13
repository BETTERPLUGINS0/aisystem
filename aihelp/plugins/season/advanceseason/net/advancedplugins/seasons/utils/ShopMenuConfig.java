/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.utils;

import net.advancedplugins.as.impl.utils.DataHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class ShopMenuConfig
extends DataHandler {
    private final boolean enabled = this.isEnabled();
    private final boolean allowedOnAllWorlds = this.getBoolean("allowOnAllWorlds", true);

    public ShopMenuConfig(JavaPlugin javaPlugin) {
        super("menus/seasonShop", javaPlugin);
        if (!this.enabled) {
            return;
        }
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isAllowedOnAllWorlds() {
        return this.allowedOnAllWorlds;
    }
}

