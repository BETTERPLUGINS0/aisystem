/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.customitemmanager.pack;

import java.util.HashMap;
import java.util.Map;
import me.zombie_striker.qav.customitemmanager.pack.ResourcepackProvider;
import me.zombie_striker.qav.hooks.ViaVersionHook;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class MultiVersionPackProvider
implements ResourcepackProvider {
    private final Map<String, String> versions;

    public MultiVersionPackProvider(Map<String, String> map) {
        this.versions = map;
    }

    public MultiVersionPackProvider(ConfigurationSection configurationSection) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        configurationSection.getKeys(false).forEach(string -> hashMap.put((String)string, configurationSection.getString(string)));
        this.versions = hashMap;
    }

    @Override
    public String getFor(@Nullable Player player) {
        if (player == null) {
            return this.versions.get("0");
        }
        String string = ViaVersionHook.getVersion(player);
        return this.versions.getOrDefault(string.replace(".", "-"), this.versions.get("0"));
    }

    @Override
    public Object serialize() {
        return new HashMap<String, String>(this.versions);
    }
}

