/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.vipfeatures.api;

import com.andrei1058.vipfeatures.api.BoosterType;
import com.andrei1058.vipfeatures.api.DatabaseAdapter;
import com.andrei1058.vipfeatures.api.MiniGame;
import com.andrei1058.vipfeatures.api.MiniGameAlreadyRegistered;
import com.andrei1058.vipfeatures.api.ParticleType;
import com.andrei1058.vipfeatures.api.SpellType;
import com.andrei1058.vipfeatures.api.TrailType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface IVipFeatures {
    public void registerMiniGame(MiniGame var1) throws MiniGameAlreadyRegistered;

    public void givePlayerItemStack(Player var1);

    public ParticlesUtil getParticlesUtil();

    public VersionUtil getVersionUtil();

    public SpellsUtil getSpellsUtil();

    public TrailsUtil getTrailsUtil();

    public BoostersUtil getBoostersUtil();

    public Plugin getVipFeatures();

    public void setDatabaseAdapter(DatabaseAdapter var1);

    public DatabaseAdapter getDatabaseAdapter();

    public static interface BoostersUtil {
        public BoosterType getPlayerBooster(Player var1);

        public void togglePlayerBooster(Player var1, BoosterType var2);
    }

    public static interface TrailsUtil {
        public TrailType getPlayerTrails(Player var1);

        public void togglePlayerTrails(Player var1, TrailType var2);
    }

    public static interface VersionUtil {
        public String getForCurrentVersion(String var1, String var2, String var3);
    }

    public static interface SpellsUtil {
        public SpellType getPlayerSpells(Player var1);

        public void togglePlayerSpells(Player var1, SpellType var2);

        default public String getZombieOwnerMetaKey() {
            return "avf-owner";
        }
    }

    public static interface ParticlesUtil {
        public ParticleType getPlayerParticles(Player var1);

        public void togglePlayerParticles(Player var1, ParticleType var2);
    }
}

