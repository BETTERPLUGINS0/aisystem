/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.PlaceholderAPI
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.support.papi;

import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class SupportPAPI {
    private static supp supportPAPI = new noPAPI();

    public static supp getSupportPAPI() {
        return supportPAPI;
    }

    public static void setSupportPAPI(supp s) {
        supportPAPI = s;
    }

    public static interface supp {
        public String replace(Player var1, String var2);

        public List<String> replace(Player var1, List<String> var2);
    }

    public static class noPAPI
    implements supp {
        @Override
        public String replace(Player p, String s) {
            return s;
        }

        @Override
        public List<String> replace(Player p, List<String> strings) {
            return strings;
        }
    }

    public static class withPAPI
    implements supp {
        @Override
        public String replace(Player p, String s) {
            return PlaceholderAPI.setPlaceholders((Player)p, (String)s);
        }

        @Override
        public List<String> replace(Player p, List<String> strings) {
            return PlaceholderAPI.setPlaceholders((Player)p, strings);
        }
    }
}

