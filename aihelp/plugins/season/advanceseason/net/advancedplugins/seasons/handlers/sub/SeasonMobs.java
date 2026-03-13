/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.lumine.mythic.api.mobs.MythicMob
 *  io.lumine.mythic.bukkit.MythicBukkit
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.EntityType
 */
package net.advancedplugins.seasons.handlers.sub;

import com.google.common.collect.ImmutableMap;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.entity.EntityTypeSpawner;
import net.advancedplugins.seasons.entity.MythicMobSpawner;
import net.advancedplugins.seasons.enums.SeasonType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

public class SeasonMobs {
    private final SeasonType seasonType;
    private final ImmutableMap<String, List<EntityTypeSpawner>> typeSpawnersByBiome;

    public SeasonMobs(SeasonType seasonType, FileConfiguration fileConfiguration) {
        this.seasonType = seasonType;
        ImmutableMap.Builder builder = ImmutableMap.builder();
        fileConfiguration.getConfigurationSection("seasons." + seasonType.name()).getValues(false).forEach((string, object) -> {
            if (!(object instanceof List)) {
                return;
            }
            List list = ((List)object).stream().map(Object::toString).map(SeasonMobs::resolveTypeName).filter(Objects::nonNull).collect(Collectors.toList());
            builder.put(string, list);
        });
        this.typeSpawnersByBiome = builder.build();
    }

    private static EntityTypeSpawner resolveTypeName(String string) {
        ArrayDeque<String> arrayDeque = new ArrayDeque<String>(Arrays.asList(string.split(":")));
        int n = arrayDeque.size();
        String string2 = (String)arrayDeque.poll();
        if (n == 1) {
            EntityType entityType = EntityType.fromName((String)string);
            if (entityType != null) {
                return EntityTypeSpawner.bukkit(entityType);
            }
            Core.getInstance().getLogger().warning("Unknown entity type '" + string + "'");
        } else if ("mythicmob".equalsIgnoreCase(string2)) {
            if (!HooksHandler.isEnabled(HookPlugin.MYTHICMOBS)) {
                return null;
            }
            String string3 = (String)arrayDeque.poll();
            MythicMob mythicMob = MythicBukkit.inst().getAPIHelper().getMythicMob(string3);
            if (mythicMob == null) {
                Core.getInstance().getLogger().warning("Unknown MythicMob type '" + string3 + "'");
            }
            String string4 = (String)arrayDeque.poll();
            boolean bl = "hostile".equalsIgnoreCase(string4);
            int n2 = SeasonMobs.tryParseInt(string4);
            if (n2 <= 0) {
                n2 = SeasonMobs.tryParseInt((String)arrayDeque.poll());
            }
            n2 = Math.max(1, n2);
            return new MythicMobSpawner(mythicMob, bl, n2);
        }
        return null;
    }

    private static int tryParseInt(String string) {
        try {
            return Math.max(1, Integer.parseInt(string));
        } catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }

    public SeasonType getSeasonType() {
        return this.seasonType;
    }

    public ImmutableMap<String, List<EntityTypeSpawner>> getTypeSpawnersByBiome() {
        return this.typeSpawnersByBiome;
    }
}

