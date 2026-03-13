/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.PlaceholderAPI
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.libs.sidebar;

import com.andrei1058.bedwars.libs.sidebar.PAPISupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

class PAPIAdapter
implements PAPISupport {
    PAPIAdapter() {
    }

    @Override
    public String replacePlaceholders(Player p, String s) {
        return PlaceholderAPI.setPlaceholders((Player)p, (String)s);
    }

    @Override
    public boolean hasPlaceholders(@NotNull String s) {
        Pattern pattern = Pattern.compile("%([^%]+)%");
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }
}

